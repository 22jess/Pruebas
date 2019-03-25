package sistemaDistribuido.sistema.clienteServidor.modoUsuario.cuatroUno;

import sistemaDistribuido.sistema.clienteServidor.modoMonitor.Nucleo;
import sistemaDistribuido.sistema.clienteServidor.modoUsuario.Proceso;
import sistemaDistribuido.util.Escribano;

/**
 * 
 */
public class ProcesoCliente extends Proceso{
	private static String LINEA = "_______________________________________";
	private byte[] solicitud;
	private static int LONG_MSJ = 1024;
	private static short CREAR = 0;
	private static short ELIMINAR = 1;
	private static short LEER = 2;
	private static short ESCRIBIR = 3;
	private static int I_EMISOR = 0;
	private static int I_RECEPTOR = 4;
	private static int I_CODOP = 8;
	private static int I_SOLICITUD = 10;
	private static int SERVIDOR_ARCHIVOS = 300;

	/**
	 * 
	 */
	public ProcesoCliente(Escribano esc){
		super(esc);
		start();
	}

	/**
	 * 
	 */
	public void run(){
		imprimeln(LINEA);
		imprimeln("Inicio de proceso");
		while(continuar()) {
			imprimeln(LINEA);
			imprimeln("Esperando datos para continuar.");
			Nucleo.suspenderProceso();		
			byte[] respServidor=new byte[LONG_MSJ];
			imprimeln(LINEA);
			imprimeln("Solicitud de env\u00edo de mensaje.");
			Nucleo.send(SERVIDOR_ARCHIVOS, solicitud);
			imprimeln(LINEA);
			imprimeln("Invocando a recieve()");
			Nucleo.receive(dameID(),respServidor);
			imprimeln(LINEA);
			imprimeln("Procesando respuesta recibida del servidor.");
			String respuesta = procesarRespuesta(respServidor);
			imprimeln("Respuesta del Servidor: "+ respuesta);
			imprimeln(LINEA);
		}
	}

	private void escribirEmisorReceptor(){
		int ID = dameID();
		solicitud[I_EMISOR] = (byte) ((ID>>24) & 0xff);
		solicitud[I_EMISOR+1] = (byte) ((ID>>16) & 0xff);
		solicitud[I_EMISOR+2] = (byte) ((ID>>8) & 0xff);
		solicitud[I_EMISOR+3] = (byte) (ID & 0xff);

		solicitud[I_RECEPTOR] = (byte) ((SERVIDOR_ARCHIVOS>>24) & 0xff);
		solicitud[I_RECEPTOR+1] = (byte) ((SERVIDOR_ARCHIVOS>>16) & 0xff);
		solicitud[I_RECEPTOR+2] = (byte) ((SERVIDOR_ARCHIVOS>>8) & 0xff);
		solicitud[I_RECEPTOR+3] = (byte) (SERVIDOR_ARCHIVOS & 0xff);
	}
	
	private void escribirCODOP(short CODOP) {
		solicitud[I_CODOP] = (byte) ((CODOP>>8) & 0xff);
		solicitud[I_CODOP+1] = (byte) (CODOP & 0xff);
	}
	
	private void escribirCadena(int indice, String cadena) {
		// escribiendo la longitud de caracteres de la cadena
		solicitud[indice] = (byte) cadena.length();
		// escribiendo la cadena
		for (int i=0;i < cadena.length();i++){
			solicitud[i+(indice+1)] = cadena.getBytes()[i];
		}
		Nucleo.reanudarProceso(this);
	}
	
	public void crearArchivo(String nombreArchivo) {
		short CODOP = CREAR;
		// calculando la longitud de la solicitud
		int longitud = I_SOLICITUD + nombreArchivo.length() + 1;
		solicitud = new byte[longitud];
		escribirEmisorReceptor();
		escribirCODOP(CODOP);
		escribirCadena(I_SOLICITUD, nombreArchivo);
		Nucleo.reanudarProceso(this);
	}
	
	public void eliminarArchivo(String nombreArchivo) {
		short CODOP = ELIMINAR;
		// calculando la longitud de la solicitud
		int longitud = I_SOLICITUD + nombreArchivo.length() + 1;
		solicitud = new byte[longitud];
		escribirEmisorReceptor();
		escribirCODOP(CODOP);
		escribirCadena(I_SOLICITUD, nombreArchivo);
		Nucleo.reanudarProceso(this);
	}
	
	public void leerArchivo(String nombreArchivo) {
		short CODOP = LEER;
		// calculando la longitud de la solicitud
		int longitud = I_SOLICITUD + nombreArchivo.length() + 1;
		solicitud = new byte[longitud];
		escribirEmisorReceptor();
		escribirCODOP(CODOP);
		escribirCadena(I_SOLICITUD, nombreArchivo);
		Nucleo.reanudarProceso(this);
	}
	
	public void escribirArchivo(String nombreArchivo, String texto) {
		short CODOP = ESCRIBIR;
		// calculando la longitud de la solicitud
		int longitud = I_SOLICITUD + nombreArchivo.length() + texto.length() + 2;
		solicitud = new byte[longitud];
		escribirEmisorReceptor();
		escribirCODOP(CODOP);
		escribirCadena(I_SOLICITUD, nombreArchivo);
		escribirCadena(I_SOLICITUD + 1 + nombreArchivo.length(), texto);
		Nucleo.reanudarProceso(this);
	}
	
	private String procesarRespuesta(byte[] respuesta) {
		String res = "";
		int longitud = (respuesta[I_CODOP] & 0xFF);
		for(int i = 0; i < longitud; i++) {
			res += (char)respuesta[i+(I_CODOP+1)];
		}
		return res;
	}

	protected void shutdown(){
		Nucleo.nucleo.terminarBloqueo(dameID());
	}
}