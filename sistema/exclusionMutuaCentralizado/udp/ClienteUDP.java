package sistemaDistribuido.sistema.exclusionMutuaCentralizado.udp;


import java.net.SocketException;

import sistemaDistribuido.sistema.exclusionMutuaCentralizado.Cliente;
import sistemaDistribuido.sistema.exclusionMutuaCentralizado.Mensaje;
import sistemaDistribuido.visual.exclusionMutuaCentralizado.Interfaz;


public class ClienteUDP extends Cliente{
	
	private Emisor emisor;

	public ClienteUDP(Interfaz interfaz,String ipServidor) {
		super(interfaz,ipServidor);
		Receptor receptor;
		
		
		this.ipServidor = ipServidor;
		try {
			emisor = new Emisor(PUERTO_SALIDA);
			receptor = new Receptor(PUERTO_ENTRADA,this);
			receptor.start();
		} catch (SocketException e) {
			e.printStackTrace();
		}
		
	}
	
	@Override
	public void enviarMensajeSolicitudRegionCritica() {
		Mensaje m = new MensajeUDP(new byte[5]);
		m.fijaCodOperacion(COD_SOLICITAR_REGION);
		m.fijaIdRegionCritica(0);
		interfaz.imprimirln("Solicitando acceso a la region crítica");
		emisor.enviarMensaje(m.dameContenido(),ipServidor,ServidorUDP.PUERTO_ENTRADA);
		synchronized (this) {
			esperar = true;
			while(esperar){
				try {
					wait();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

	@Override
	public void enviarMensajeLiberarRegionCritica() {
		Mensaje m = new MensajeUDP(new byte[5]);
		m.fijaCodOperacion(COD_LIBERAR_REGION);
		m.fijaIdRegionCritica(0);
		interfaz.imprimirln("Liberando la region crítica");
		emisor.enviarMensaje(m.dameContenido(),ipServidor,ServidorUDP.PUERTO_ENTRADA);
	}

}
