package sistemaDistribuido.sistema.clienteServidor.modoUsuario.cuatroUno;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.ByteBuffer;
import java.util.Arrays;

import sistemaDistribuido.sistema.clienteServidor.modoMonitor.Nucleo;
import sistemaDistribuido.sistema.clienteServidor.modoUsuario.Proceso;
import sistemaDistribuido.util.Escribano;
import sistemaDistribuido.util.Pausador;

/**
 * 
 */
public class ProcesoServidor extends Proceso{
	private static String LINEA = "_______________________________________";
	private static int LONG_MSJ = 1024;
	private int I_CODOP = 8;
	private int I_SOLICITUD = 10;
	private int ID_EMISOR;
    private static int I_EMISOR = 0;
    private static int I_RECEPTOR = 4;

	/**
	 * 
	 */
	public ProcesoServidor(Escribano esc){
		super(esc);
		start();
	}

	/**
	 * 
	 */
	public void run(){
		imprimeln(LINEA);
		imprimeln("Inicio de proceso");
		byte[] solicitud=new byte[LONG_MSJ];
		while(continuar()){
			imprimeln(LINEA);
			imprimeln("Invocando a recieve()");
			Nucleo.receive(dameID(),solicitud);
			imprimeln(LINEA);
			imprimeln("Procesando petición recibida del cliente.");
			byte[] respuesta = procesarSolicitud(solicitud);
			Pausador.pausa(1000);  //sin esta línea es posible que Servidor solicite send antes que Cliente solicite receive
			imprimeln(LINEA);
			imprimeln("Solicitud de envío de mensaje.");
			Nucleo.send(ID_EMISOR,respuesta);
			imprimeln(LINEA);
		}
	}

    private void escribirEmisorReceptor(byte[] solicitud, byte[] receptor){
        int ID = dameID();
        solicitud[I_EMISOR] = (byte) ((ID>>24) & 0xff);
        solicitud[I_EMISOR+1] = (byte) ((ID>>16) & 0xff);
        solicitud[I_EMISOR+2] = (byte) ((ID>>8) & 0xff);
        solicitud[I_EMISOR+3] = (byte) (ID & 0xff);

        for (int i = 0; i < I_RECEPTOR; i++){
            solicitud[I_RECEPTOR+i] = receptor[i];
        }
        ByteBuffer convertidor = ByteBuffer.wrap(receptor);
        ID_EMISOR = convertidor.getInt();
    }

	private byte[] procesarSolicitud(byte[] solicitud) {
		// obteniendo el codigo de operación
		short codop = (short)(((solicitud[I_CODOP] & 0xFF) << 8) | (solicitud[I_CODOP+1] & 0xFF));
		String nombreArchivo = "";
		int longitud = (solicitud[I_SOLICITUD] & 0xFF);
		for(int i = 0; i < longitud; i++) {
			nombreArchivo += (char)solicitud[i+(I_SOLICITUD+1)];
		}
		String respuesta;
		switch(codop) {
		case 0:
			imprimeln("Solicitud: crear el archivo " + nombreArchivo);
			respuesta = crearArchivo(nombreArchivo);
			break;
		case 1:
			imprimeln("Solicitud: eliminar el archivo " + nombreArchivo);
			respuesta = eliminarArchivo(nombreArchivo);
			break;
		case 2:
			imprimeln("Solicitud: lectura del archivo " + nombreArchivo);
			respuesta = leerArchivo(nombreArchivo);
			break;
		case 3:
			imprimeln("Solicitud: escritura en el archivo " + nombreArchivo);
			String texto = "";
			int offset = I_SOLICITUD+longitud+1;
			longitud = (solicitud[offset] & 0xFF);
			for(int i = 0; i < longitud; i++) {
				texto += (char)solicitud[i+(offset+1)];
			}
			respuesta = escribirArchivo(nombreArchivo, texto);
			break;
		default:
			respuesta = "Error en la solicitud!";
		}
		// calculando la longitud de la respuesta
		longitud = I_CODOP + respuesta.length() + 1;
		byte[] res = new byte[longitud];
        byte[] receptor = Arrays.copyOfRange(solicitud, I_EMISOR, I_RECEPTOR);
        escribirEmisorReceptor(res, receptor);
		escribirCadena(res, I_CODOP, respuesta);	
		return res;
	}
	
	private void escribirCadena(byte[] mensaje, int indice, String cadena) {
		// escribiendo la longitud de caracteres de la cadena
		mensaje[indice] = (byte) cadena.length();
		// escribiendo la cadena
		for (int i=0;i < cadena.length();i++){
			mensaje[i+(indice+1)] = cadena.getBytes()[i];
		}
	}
	
	private String crearArchivo(String nombreArchivo) {
		File archivo = new File(nombreArchivo);	
		try {
			if(archivo.createNewFile()) {
				return "Archivo creado exitosamente!";
			}
		} catch (IOException e) {
			return "Error al crear el archivo!";
		}
		return "El archivo ya existe!";
	}
	
	private String eliminarArchivo(String nombreArchivo) {
		File archivo = new File(nombreArchivo);
		String texto;
		if(archivo.delete()){
			texto = "Archivo eliminado exitosamente!";
		}else{
			texto = "El archivo no existe!";
		}
		return texto;
	}
	
	private String leerArchivo(String nombreArchivo) {
		File archivo = new File(nombreArchivo);
		String texto = "";
		try{
			BufferedReader in = new BufferedReader(new FileReader(archivo));			
			String s = in.readLine();
			while(s != null){
				texto += s;
				s = in.readLine();
			}
			in.close();
		}catch(FileNotFoundException e){
			texto = "El archivo no existe!";
		}catch(IOException e){
			texto = "Error al leer el archivo!";
		}
		return texto;
	}
	
	private String escribirArchivo(String nombreArchivo, String texto) {
		File archivo = new File(nombreArchivo);
		String res = "Texto escrito en el archivo exitosamente!";
		try{
			PrintWriter out=new PrintWriter(new FileWriter(archivo));
			out.println(texto);
			out.close();
		}catch(IOException e){
			res = "Error al escribir en el archivo!";
		}
		return res;
	}

    protected void shutdown(){
        Nucleo.nucleo.terminarBloqueo(dameID());
    }
}