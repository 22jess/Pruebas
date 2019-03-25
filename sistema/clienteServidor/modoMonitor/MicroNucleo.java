/* 	Sistemas Concurrentes y Distribuidos
	Modelo Cliente/Servidor - Mecanismo de Comunicación Básico
	CeroCero
*/
package sistemaDistribuido.sistema.clienteServidor.modoMonitor;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.HashMap;

import sistemaDistribuido.sistema.clienteServidor.modoMonitor.MicroNucleoBase;
import sistemaDistribuido.sistema.clienteServidor.modoUsuario.Proceso;
import sistemaDistribuido.sistema.clienteServidor.modoUsuario.ceroCero.Mensaje;
import sistemaDistribuido.sistema.exclusionMutuaCentralizado.udp.UtilRed;

/**
 * 
 */
public final class MicroNucleo extends MicroNucleoBase{
	private static MicroNucleo nucleo=new MicroNucleo();
	private HashMap<Integer,byte[]> procesosEsperaRecibirMsje;
	private HashMap<Integer,ParMaquinaProceso> procesosEnviaronMsje;
	private DatagramSocket socketEmision;
	private Object obj;
	
	private static final int PUERTO_EMISION = 22022;
	/**
	 * 
	 */
	private MicroNucleo(){
		try {
			socketEmision = new DatagramSocket(PUERTO_EMISION);
			procesosEsperaRecibirMsje = new HashMap<Integer,byte[]>();
			procesosEnviaronMsje = new HashMap<Integer,ParMaquinaProceso>();
			obj = new Object();
		} catch (SocketException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 
	 */
	public final static MicroNucleo obtenerMicroNucleo(){
		return nucleo;
	}

	/*---Metodos para probar el paso de mensajes entre los procesos cliente y servidor en ausencia de datagramas.
    Esta es una forma incorrecta de programacion "por uso de variables globales" (en este caso atributos de clase)
    ya que, para empezar, no se usan ambos parametros en los metodos y fallaria si dos procesos invocaran
    simultaneamente a receiveFalso() al reescriir el atributo mensaje---*/
	byte[] mensaje;

	public void sendFalso(int dest,byte[] message){
		System.arraycopy(message,0,mensaje,0,message.length);
		notificarHilos();  //Reanuda la ejecucion del proceso que haya invocado a receiveFalso()
	}

	public void receiveFalso(int addr,byte[] message){
		mensaje=message;
		suspenderProceso();
	}
	/*---------------------------------------------------------*/

	/**
	 * 
	 */
	protected boolean iniciarModulos(){
		return true;
	}

	/**
	 * 
	 */
	protected void sendVerdadero(int dest,byte[] message){
		ParMaquinaProceso pmp;
		
		imprimeln("Buscando en listas locales el par (máquina, proceso) " +
				"que corresponde al parámetro dest de la llamada a send");
		if(procesosEnviaronMsje.containsKey(dest)){
			pmp = procesosEnviaronMsje.get(dest);
			
		}else{
			imprimeln("Enviando mensaje de búsqueda del servidor");
			pmp = dameDestinatarioDesdeInterfaz();
			imprimeln("Recibido mensaje que contiene la ubicación (máquina, proceso) del servidor");
		}
		imprimeln("Completando campos de encabezado del mensaje a ser enviado");
		System.arraycopy(UtilRed.toBytes(dameIdProceso()), 0, message, 0, 4);
		System.arraycopy(UtilRed.toBytes(pmp.dameID()), 0, message, 4, 4);
		imprimeln("Enviando mensaje por la red");
		try {
			socketEmision.send(new DatagramPacket(
					message,message.length,InetAddress.getByName(pmp.dameIP()),damePuertoRecepcion()
					));
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
	}

	/**
	 * 
	 */
	protected void receiveVerdadero(int addr,byte[] message){
		
		procesosEsperaRecibirMsje.put(addr, message);
		synchronized(obj){
			try {
				while(procesosEsperaRecibirMsje.containsKey(addr)){
					obj.wait();
				}
				imprimeln("Recibido mensaje proveniente de la red");
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		//suspenderProceso();
	}

	/**
	 * Para el(la) encargad@ de direccionamiento por servidor de nombres en pr�ctica 5  
	 */
	protected void sendVerdadero(String dest,byte[] message){
	}

	/**
	 * Para el(la) encargad@ de primitivas sin bloqueo en pr�ctica 5
	 */
	protected void sendNBVerdadero(int dest,byte[] message){
	}

	/**
	 * Para el(la) encargad@ de primitivas sin bloqueo en pr�ctica 5
	 */
	protected void receiveNBVerdadero(int addr,byte[] message){
	}

	/**
	 * 
	 */

	
	public void terminarBloqueo(int idProceso){
		Proceso p = dameProcesoLocal(idProceso);
		reanudarProceso(p);
	}

	public void run(){
		byte[] buffer = new byte[Mensaje.MAX_TAM_BUFFER];
		String ip;
		DatagramSocket socketRecepcion;
		DatagramPacket dp = new DatagramPacket(buffer, buffer.length);
		Mensaje m;
		Proceso p;
		
		socketRecepcion = dameSocketRecepcion();
		while(seguirEsperandoDatagramas()){
			try {
				socketRecepcion.receive(dp);
				ip = dp.getAddress().getHostAddress();
				m = new Mensaje(dp.getData());
				imprimeln("IP del emisor: " + ip);
				imprimeln("ID del emisor: " + m.dameIdEmisor());
				imprimeln("Buscando proceso correspondiente al campo dest del mensaje recibido");
				if((p = dameProcesoLocal(m.dameIdReceptor())) != null){
					if(procesosEsperaRecibirMsje.containsKey(p.dameID())){
						procesosEnviaronMsje.put(m.dameIdEmisor(), new IdCompuestoProceso(ip, m.dameIdEmisor()));
						imprimeln("Copiando el mensaje hacia el espacio del proceso");
						System.arraycopy(buffer, 0, procesosEsperaRecibirMsje.get(p.dameID()), 0, buffer.length);
						procesosEsperaRecibirMsje.remove(m.dameIdReceptor());
						synchronized(obj){
							obj.notifyAll();
						}
						//reanudarProceso(p);
					}
					else{
						//TA
					}
				}
				else{
					//AU
					imprimeln("Proceso destinatario no encontrado según campo dest " +
							"del mensaje recibido");
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}