/* 	Sistemas Concurrentes y Distribuidos
	Algoritmos de Sincronización de Relojes - Cristian
	CeroCero
*/
package sistemaDistribuido.sistema.sincronizacionCristian;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.net.InetAddress;

public class Emisor {
	private DatagramSocket socket;
	
	
	public Emisor(int puertoSalida) throws SocketException{
		socket = new DatagramSocket(puertoSalida);
	}
	
	public void enviarMensaje(byte[] bytes, String ip, int ptoDest){
		
		try {
			socket.send(new DatagramPacket(bytes,bytes.length,InetAddress.getByName(ip),ptoDest));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void enviarMensaje(String s, String ip, int ptoDest){
		enviarMensaje(s.getBytes(),ip,ptoDest);
	}
	
}
