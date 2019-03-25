/* 	Sistemas Concurrentes y Distribuidos
	Algoritmos de Sincronización de Relojes - Cristian
	CeroCero
*/
package sistemaDistribuido.sistema.sincronizacionCristian;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;



public class Receptor extends Thread implements ConstantesRed{
	private final int PUERTO_ENTRADA;
	private final int RETARDO_RECEPCION_SOLICITUD;
	
	private ProcesadorMensajeRed procesador;
	
	
	
	
	public Receptor(int puerto,ProcesadorMensajeRed procesador, int retardoRecepcion){
		PUERTO_ENTRADA = puerto;
		RETARDO_RECEPCION_SOLICITUD = retardoRecepcion;
		this.procesador = procesador;
		
	}
	
	
	public void run(){
		byte[] buffer;
		DatagramPacket dp;
		
		try(DatagramSocket socket = new DatagramSocket(PUERTO_ENTRADA);){
			buffer = new byte[MAX_TAM_PAQUETE_UDP];
			dp = new DatagramPacket(buffer, MAX_TAM_PAQUETE_UDP);
			while(true){
				socket.receive(dp);
				new Esclavo(dp.getData(),procesador,dp.getAddress().getHostAddress()).start();
			}
		} catch (IOException e) {
			e.printStackTrace();
		} 
	}
	
	public class Esclavo extends Thread{
		private ProcesadorMensajeRed procesador;
		private byte[] buffer;
		private String ip;
		
		
		Esclavo(byte[] buff, ProcesadorMensajeRed procesador, String ip){
			buffer = new byte[1024];
			System.arraycopy(buff, 0, buffer, 0, 1024); // Revisar
			this.procesador = procesador;
			this.ip = ip;
		}
		
		public void run(){
			
			try {
				sleep(RETARDO_RECEPCION_SOLICITUD);
				procesador.procesarMensaje(buffer,ip);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

}
