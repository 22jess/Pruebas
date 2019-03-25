package sistemaDistribuido.sistema.exclusionMutuaCentralizado.udp;



import java.net.SocketException;

import sistemaDistribuido.sistema.exclusionMutuaCentralizado.Cliente;
import sistemaDistribuido.sistema.exclusionMutuaCentralizado.IdCliente;
import sistemaDistribuido.sistema.exclusionMutuaCentralizado.Mensaje;
import sistemaDistribuido.sistema.exclusionMutuaCentralizado.ProcesadorMensaje;
import sistemaDistribuido.sistema.exclusionMutuaCentralizado.Servidor;
import sistemaDistribuido.visual.exclusionMutuaCentralizado.Interfaz;


public class ServidorUDP  extends Servidor implements ProcesadorMensaje{
	private Emisor emisor;
	
	
	public ServidorUDP(Interfaz interfaz){
		super(interfaz);
		Receptor receptor;
		
		try {
			emisor = new Emisor(PUERTO_SALIDA);
			receptor = new Receptor(PUERTO_ENTRADA,this);
			receptor.start();
			interfaz.imprimirln("Servidor UDP en espera de recibir mensajes.");
		} catch (SocketException e) {
			e.printStackTrace();
		}
	}

	@Override
	public  void procesarMensaje(Mensaje mensaje, String ip) {
		IdCliente idCliente;
		byte[] respuesta = new byte[1];
		int idRegion;
		
		idRegion = mensaje.dameIdRegionCritica();
		switch(mensaje.dameCodOperacion()){
		case COD_SOLICITAR_REGION:
			interfaz.imprimirln("Se solicitó acceder a la region crítica.");
			synchronized(this){
				if(regionesCriticasLibres[idRegion]){
					interfaz.imprimirln("La region crítica está disponible.");
					respuesta[0] = COD_OK;
					regionesCriticasLibres[idRegion] = false;
				}
				else{
					interfaz.imprimirln("La region crítica NO está disponible.");
					respuesta[0] = COD_DENEGAR_ACCESO;

					idCliente = new IdCliente(ip);
								idCliente.fijaIdRegionCritica(mensaje.dameIdRegionCritica());
								agregarClienteLista(idCliente);
								interfaz.actualizarListaClientesEspera(clientesEspera);
				}
			}
			break;
		case COD_LIBERAR_REGION:
			interfaz.imprimirln("Se solicitó liberar la region crítica.");
			synchronized(this){
				interfaz.imprimirln("Liberando a la region crítica.");
				regionesCriticasLibres[idRegion] = true;
				notificarSiguienteClienteEspera();
			}
			break;
		}
		interfaz.imprimirln("Enviando respuesta al cliente.");
		emisor.enviarMensaje(respuesta, ip, Cliente.PUERTO_ENTRADA);
	}

	@Override
	public void notificarSiguienteClienteEspera() {
		byte[] respuesta = new byte[1];
		IdCliente idCliente;
		
		if(clientesEspera.size() > 0){
			respuesta[0] = COD_OK;
			idCliente = clientesEspera.remove(0);
			emisor.enviarMensaje(respuesta, idCliente.dameIp(), Cliente.PUERTO_ENTRADA);
			regionesCriticasLibres[idCliente.dameIdRegionCritica()] = false;
			interfaz.actualizarListaClientesEspera(clientesEspera);
		}
	}

}
