/* 	Sistemas Concurrentes y Distribuidos
	Algoritmos de Sincronización de Relojes - Cristian
	CeroCero
*/
package sistemaDistribuido.sistema.sincronizacionCristian;

import java.net.SocketException;



public class ServidorTiempo extends Maquina implements Runnable, ProcesadorMensajeRed{
	
	public final static int PUERTO_ENTRADA = 20020;
	public final static int PUERTO_SALIDA = 20025;
	
	
	private final int MILISEG_CONTROLADOR_INTERRUPCIONES;
	private final int RETARDO_ENVIO_RESPUESTA;
	

	public ServidorTiempo(int tiempoInicial, int delta, int interrupcionesSegundo,
			TipoReloj tipoReloj, float ro, int tiempoPropInicial, int controlInterrup, 
			int retardoRecepcion, int retardoEnvio, Impresor impresor) {
		
		super(tiempoInicial, delta, interrupcionesSegundo, tipoReloj,ro,tiempoPropInicial,impresor);
		MILISEG_CONTROLADOR_INTERRUPCIONES = controlInterrup;
		RETARDO_ENVIO_RESPUESTA = retardoEnvio;
	
		receptor = new Receptor(PUERTO_ENTRADA, this, retardoRecepcion);
		receptor.start();
		try {
			emisor = new Emisor(PUERTO_SALIDA);
		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void run(){
		int espera = MILISEGUNDOS_EN_SEGUNDO/INTERRUPCIONES_SEGUNDO;
	
		while(true){
			try {
				sleep(espera);
				tiempoLocal+= n;
				impresor.imprimirTiempoActualMaquina(tiempoLocal);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

		}
	}

	public void procesarMensaje(byte[] buffer,String ip) {
		
		byte[] respuesta;
		respuesta = new byte[12];
		
		impresor.imprimirLn("Se va a enviar al cliente..." + tiempoLocal);
		System.arraycopy(UtilRed.toBytes(tiempoLocal), 0, respuesta, 0, 8);
		System.arraycopy(UtilRed.toBytes(MILISEG_CONTROLADOR_INTERRUPCIONES), 0, respuesta, 8, 4);
		try {
			sleep(MILISEG_CONTROLADOR_INTERRUPCIONES);
			sleep(RETARDO_ENVIO_RESPUESTA);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		emisor.enviarMensaje(respuesta, ip, Cliente.PUERTO_ENTRADA);
	}
	
	
}
