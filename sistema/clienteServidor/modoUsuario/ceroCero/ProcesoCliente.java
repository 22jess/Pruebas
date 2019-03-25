package sistemaDistribuido.sistema.clienteServidor.modoUsuario.ceroCero;

import sistemaDistribuido.sistema.clienteServidor.modoMonitor.Nucleo;
import sistemaDistribuido.sistema.clienteServidor.modoUsuario.Proceso;
import sistemaDistribuido.util.Escribano;


public class ProcesoCliente extends Proceso{

	private String nombreArchivo;
	private CodigoOperacion tipoSolicitud;
	private String contenidoArchivo;
	//private static final int SERVIDOR_ARCHIVOS = 2;

	public ProcesoCliente(Escribano esc){
		super(esc);
		start();
	}
	
	public void run(){
		
		imprimeln("Inicio de proceso");
		imprimeln("Proceso cliente en ejecucion.");
		imprimeln("Esperando datos para continuar.");
		Nucleo.suspenderProceso();
		imprimeln("Generando mensaje a ser enviado, llenando los campos necesarios");
		imprimeln("Solicitud de envío de mensaje");
		if(tipoSolicitud != null){
			switch(tipoSolicitud){
			case CREAR_ARCHIVO:		crearArchivo();		break;
			case ELIMINAR_ARCHIVO:	eliminarArchivo();	break;
			case ESCRIBIR_ARCHIVO:	escribirArchivo();	break;
			case LEER_ARCHIVO:		leerArchivo();		break;
			default:	; 	
			}
		}
	}


	private void crearArchivo() {
		Mensaje m;
		byte[] respuesta = new byte[Mensaje.MAX_TAM_BUFFER];
		byte tamRespuesta;
		
		byte[] nombreArchivoBytes = nombreArchivo.getBytes();
		byte tamNombreArchivo = (byte)nombreArchivoBytes.length; 
		
		m  = new Mensaje(11+tamNombreArchivo);
		m.fijaCodigoOperacion(CodigoOperacion.CREAR_ARCHIVO);
		m.agregarDatos(tamNombreArchivo);
		m.agregarDatos(nombreArchivoBytes);
		Nucleo.send(248,m.dameBuffer());
		imprimeln("Invocando a receive()");
		Nucleo.receive(dameID(),respuesta);
		m = new Mensaje(respuesta);
		imprimeln("Procesando respuesta recibida del servidor");
		tamRespuesta = m.dameValorByte(8);
		imprimeln("el servidor me envio: " + new String(m.dameBytes(9,tamRespuesta)));
	}

	private void leerArchivo() {
		Mensaje m;
		byte[] respuesta = new byte[Mensaje.MAX_TAM_BUFFER];
		
		byte[] nombreArchivoBytes = nombreArchivo.getBytes();
		byte tamNombreArchivo =(byte) nombreArchivoBytes.length;
		byte tamRespuesta;
		
		m  = new Mensaje(11+tamNombreArchivo);
		m.fijaCodigoOperacion(CodigoOperacion.LEER_ARCHIVO);
		m.agregarDatos(tamNombreArchivo);
		m.agregarDatos(nombreArchivoBytes);
		Nucleo.send(248,m.dameBuffer());
		imprimeln("Invocando a receive()");
		Nucleo.receive(dameID(),respuesta);
		m = new Mensaje(respuesta);
		imprimeln("Procesando respuesta recibida del servidor");
		tamRespuesta = m.dameValorByte(8);
		imprimeln("el servidor me envio: " + new String(m.dameBytes(9,tamRespuesta)));
		
	}

	private void escribirArchivo() {
		Mensaje m;
		byte[] respuesta = new byte[Mensaje.MAX_TAM_BUFFER];
		byte tamRespuesta;
		
		byte[] nombreArchivoBytes = nombreArchivo.getBytes();
		byte[] contenidoArchivoBytes = contenidoArchivo.getBytes();
		byte tamNombreArchivo = (byte) nombreArchivoBytes.length;
		byte tamContenidoArchivo = (byte) contenidoArchivoBytes.length; 
		
		m  = new Mensaje(12+tamNombreArchivo+tamContenidoArchivo);
		m.fijaCodigoOperacion(CodigoOperacion.ESCRIBIR_ARCHIVO);
		m.agregarDatos(tamNombreArchivo);
		m.agregarDatos(nombreArchivoBytes);
		m.agregarDatos(tamContenidoArchivo);
		m.agregarDatos(contenidoArchivoBytes);
		Nucleo.send(248,m.dameBuffer());
		imprimeln("Invocando a receive()");
		Nucleo.receive(dameID(),respuesta);
		m = new Mensaje(respuesta);
		imprimeln("Procesando respuesta recibida del servidor");
		tamRespuesta = m.dameValorByte(8);
		imprimeln("el servidor me envio: " + new String(m.dameBytes(9,tamRespuesta)));
	}

	private void eliminarArchivo() {
		Mensaje m;
		byte[] respuesta = new byte[Mensaje.MAX_TAM_BUFFER];
		byte tamRespuesta;
		
		byte[] nombreArchivoBytes = nombreArchivo.getBytes();
		byte tamNombreArchivo = (byte) nombreArchivoBytes.length; 
		
		m  = new Mensaje(11+tamNombreArchivo);
		m.fijaCodigoOperacion(CodigoOperacion.ELIMINAR_ARCHIVO);
		m.agregarDatos(tamNombreArchivo);
		m.agregarDatos(nombreArchivoBytes);
		Nucleo.send(248,m.dameBuffer());
		imprimeln("Invocando a receive()");
		Nucleo.receive(dameID(),respuesta);
		m = new Mensaje(respuesta);
		imprimeln("Procesando respuesta recibida del servidor");
		tamRespuesta = m.dameValorByte(8);
		imprimeln("el servidor me envio: " + new String(m.dameBytes(9,tamRespuesta)));
		
	}

	public void fijaSolicitudAElaborar(CodigoOperacion t) {
		tipoSolicitud = t;
	}

	public void fijaNombreArchivo(String s) {
		nombreArchivo = s;
	}

	public void fijaContenidoArchivo(String s) {
		contenidoArchivo = s;
	}
	
	protected void shutdown(){
		Nucleo.nucleo.terminarBloqueo(dameID());
	}
}
