package sistemaDistribuido.sistema.clienteServidor.modoUsuario.ceroCero;

import sistemaDistribuido.sistema.clienteServidor.modoMonitor.Nucleo;
import sistemaDistribuido.sistema.clienteServidor.modoUsuario.Proceso;
import sistemaDistribuido.util.Escribano;
import sistemaDistribuido.util.Pausador;

/**
 * 
 */
public class ProcesoServidor extends Proceso{

	public ProcesoServidor(Escribano esc){
		super(esc);
		start();
	}
	

	public void run(){
		byte[] solicitud = new byte[Mensaje.MAX_TAM_BUFFER];
		Mensaje mensajeSolicitud;
		Mensaje mensajeRespuesta;
		
		
		imprimeln("Inicio de proceso");
		imprimeln("Proceso servidor en ejecucion.");
		while(continuar()){
			imprimeln("Invocando a receive()");
			Nucleo.receive(dameID(),solicitud);
			mensajeRespuesta = null;
			mensajeSolicitud = new Mensaje(solicitud);
			switch(mensajeSolicitud.dameCodOp()){
			case CREAR_ARCHIVO:		mensajeRespuesta = crearArchivo(mensajeSolicitud);		break;
			case ELIMINAR_ARCHIVO:	mensajeRespuesta = eliminarArchivo(mensajeSolicitud);	break;
			case ESCRIBIR_ARCHIVO:	mensajeRespuesta = escribirArchivo(mensajeSolicitud);	break;
			case LEER_ARCHIVO:		mensajeRespuesta = leerArchivo(mensajeSolicitud);		break;
			default:
			}
			
			imprimeln("Solicitud de envío de mensaje");
			Pausador.pausa(1000);  //sin esta linea es posible que Servidor solicite send antes que Cliente solicite receive
			imprimeln("enviando respuesta");
			Nucleo.send(mensajeSolicitud.dameIdEmisor(),mensajeRespuesta.dameBuffer());
		}
	}


	private Mensaje leerArchivo(Mensaje sol) {
		Mensaje respuesta;
		byte[] msjeRespuestaBytes;
		byte tamNombreArchivo;
		byte tamRespuesta;
		
		imprimeln("Se ha solicitado la operación \"leer archivo \".");
		tamNombreArchivo = sol.dameValorByte(10);
		imprimeln("Nombre del archivo: " + new String(sol.dameBytes(11,tamNombreArchivo)));
		imprimeln("Generando mensaje a ser enviado, llenando los campos necesarios");
		msjeRespuestaBytes = "Se ha leido el archivo!".getBytes();
		tamRespuesta = (byte) msjeRespuestaBytes.length;
		respuesta = new Mensaje(9+tamRespuesta);
		respuesta.agregarDatos(tamRespuesta);
		respuesta.agregarDatos(msjeRespuestaBytes);
		
		return respuesta;
	}


	private Mensaje escribirArchivo(Mensaje sol) {
		Mensaje respuesta;
		byte[] msjeRespuestaBytes;
		byte tamNombreArchivo;
		byte tamRespuesta;
		byte tamContenido;
		
		imprimeln("Se ha solicitado la operación \"escribir en archivo \".");
		tamNombreArchivo = sol.dameValorByte(10);
		imprimeln("Nombre del archivo: " + new String(sol.dameBytes(11,tamNombreArchivo)));
		tamContenido = sol.dameValorByte(11+tamNombreArchivo);
		imprimeln("Contenido del archivo: " + new String(
				sol.dameBytes(12+tamNombreArchivo,tamContenido)));
		
		imprimeln("Generando mensaje a ser enviado, llenando los campos necesarios");
		msjeRespuestaBytes = "Se ha escrito en el archivo!".getBytes();
		tamRespuesta = (byte) msjeRespuestaBytes.length;
		respuesta = new Mensaje(9+tamRespuesta);
		respuesta.agregarDatos(tamRespuesta);
		respuesta.agregarDatos(msjeRespuestaBytes);
		
		return respuesta;
		
	}


	private Mensaje eliminarArchivo(Mensaje sol) {
		Mensaje respuesta;
		byte[] msjeRespuestaBytes;
		byte tamNombreArchivo;
		byte tamRespuesta;
		
		imprimeln("Se ha solicitado la operación \"eliminar archivo \".");
		tamNombreArchivo = sol.dameValorByte(10);
		imprimeln("Nombre del archivo: " + new String(sol.dameBytes(11,tamNombreArchivo)));
		imprimeln("Generando mensaje a ser enviado, llenando los campos necesarios");
		msjeRespuestaBytes = "Se ha eliminado el archivo!".getBytes();
		tamRespuesta = (byte) msjeRespuestaBytes.length;
		respuesta = new Mensaje(9+tamRespuesta);
		respuesta.agregarDatos(tamRespuesta);
		respuesta.agregarDatos(msjeRespuestaBytes);
		
		return respuesta;
		
	}


	private Mensaje crearArchivo(Mensaje sol) {
		Mensaje respuesta;
		byte[] msjeRespuestaBytes;
		byte tamNombreArchivo;
		byte tamRespuesta;
		
		imprimeln("Se ha solicitado la operación \"crear archivo \".");
		tamNombreArchivo = sol.dameValorByte(10);
		imprimeln("Nombre del archivo: " + new String(sol.dameBytes(11,tamNombreArchivo)));
		imprimeln("Generando mensaje a ser enviado, llenando los campos necesarios");
		msjeRespuestaBytes = "Se ha creado el archivo!".getBytes();
		tamRespuesta = (byte) msjeRespuestaBytes.length;
		respuesta = new Mensaje(9+tamRespuesta);
		respuesta.agregarDatos(tamRespuesta);
		respuesta.agregarDatos(msjeRespuestaBytes);
		return respuesta;
		
	}
	
	protected void shutdown(){
		Nucleo.nucleo.terminarBloqueo(dameID());
	}
	
	
	
	
	
	
	
	
	
	
}
