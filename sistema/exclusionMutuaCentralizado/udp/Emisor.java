package sistemaDistribuido.sistema.exclusionMutuaCentralizado.udp;


import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

public class Emisor {
	
	private DatagramSocket socket;
	
	public Emisor(int puertoSalida) throws SocketException{
		socket = new DatagramSocket(puertoSalida);
	}
	
	public void enviarMensaje(byte[] bytes, String ipDest,  int puertoDest){
		try {
			socket.send(new DatagramPacket(bytes,bytes.length,InetAddress.getByName(ipDest),
					puertoDest));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
