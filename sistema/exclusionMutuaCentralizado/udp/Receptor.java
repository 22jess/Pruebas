package sistemaDistribuido.sistema.exclusionMutuaCentralizado.udp;



import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

import sistemaDistribuido.sistema.exclusionMutuaCentralizado.ProcesadorMensaje;

public class Receptor extends Thread{
	
	private DatagramSocket socket;
	private ProcesadorMensaje procesadorMensajes;
	
	public Receptor(int puertoEntrada,ProcesadorMensaje procesadorMensajes)throws SocketException{
		this.procesadorMensajes = procesadorMensajes;
		socket = new DatagramSocket(puertoEntrada);
	}

	
	public void run(){
		byte[] buffer;
		DatagramPacket dp;
		
		buffer = new byte[1024];
		dp = new DatagramPacket(buffer, buffer.length);
		
		while(true){
			try {
				socket.receive(dp);
				new Esclavo(dp).start();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	private class Esclavo extends Thread{
		String ip;
		byte[] buffer;
		
		
		public Esclavo(DatagramPacket dp){
			ip = dp.getAddress().getHostAddress();
			buffer = new byte[dp.getLength()];
			System.arraycopy(dp.getData(), 0, buffer, 0, buffer.length);
		}
		
		public void run(){
			procesadorMensajes.procesarMensaje(new MensajeUDP(buffer), ip);
		}
	}
}
