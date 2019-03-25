package sistemaDistribuido.sistema.exclusionMutuaCentralizado.tcp;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;

import sistemaDistribuido.sistema.exclusionMutuaCentralizado.Constantes;
import sistemaDistribuido.sistema.exclusionMutuaCentralizado.IdCliente;
import sistemaDistribuido.sistema.exclusionMutuaCentralizado.Mensaje;
import sistemaDistribuido.sistema.exclusionMutuaCentralizado.Servidor;
import sistemaDistribuido.visual.exclusionMutuaCentralizado.Interfaz;


public class ServidorTCP extends Servidor implements Runnable{
	private ServerSocket socket;
	private HashMap<String,Esclavo> conexiones;
	
	
	public ServidorTCP(Interfaz interfaz) {
		super(interfaz);
		try {
			socket = new ServerSocket(Servidor.PUERTO_ENTRADA);
		} catch (IOException e) {
			e.printStackTrace();
		}
		conexiones = new HashMap<String,Esclavo>();
		new Thread(this).start();
	}
	
	
	public void run(){
	
		Socket s;
		Esclavo esclavo;
		
		interfaz.imprimirln("Servidor TCP en espera de recibir mensajes.");
		while(true){
			try {
				s = socket.accept();
				esclavo = new Esclavo(s);
				conexiones.put(s.getInetAddress().getHostAddress(),esclavo);
				esclavo.start();
				
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
	}
	
	
	class Esclavo extends Thread implements Constantes{
		private BufferedReader entrada;
		private PrintWriter salida;
		Socket socket;
		volatile boolean terminar;

		public Esclavo(Socket s) {
			try {
				socket = s;
				salida = new PrintWriter(s.getOutputStream(),true);
				entrada = new BufferedReader(new InputStreamReader(s.getInputStream()));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		public void run(){
			Mensaje mensaje;
			String respuesta;
			IdCliente idCliente;
			int idRegion;
			
			
			while(!terminar){
				try {
					mensaje = new MensajeTCP(entrada.readLine());
					respuesta = "";
					idRegion = mensaje.dameIdRegionCritica();
					switch(mensaje.dameCodOperacion()){
					case COD_SOLICITAR_REGION:
						interfaz.imprimirln("Se solicitó acceder a la region crítica.");
						synchronized(this){
							if(regionesCriticasLibres[idRegion]){
								interfaz.imprimirln("La region crítica está disponible.");
								respuesta = Integer.toString(COD_OK);
								regionesCriticasLibres[idRegion] = false;
							}
							else{
								interfaz.imprimirln("La region crítica NO está disponible.");
								respuesta = Integer.toString(COD_DENEGAR_ACCESO);
								idCliente = new IdCliente(socket.getInetAddress().getHostAddress());
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
					if(respuesta != ""){
						interfaz.imprimirln("Enviando respuesta al cliente.");
						salida.println(respuesta);
					}
				} catch (IOException e) {
					e.printStackTrace();
					//terminar = true;
				}
			}
		}
		
		void enviarMensaje(String s){
			salida.println(s);
		}
	}
	

	@Override
	public void notificarSiguienteClienteEspera() {
		IdCliente idCliente;
		Esclavo esclavo;
		Mensaje m;
		
		if(clientesEspera.size() > 0){
			idCliente = clientesEspera.remove(0);
			if((esclavo = conexiones.get(idCliente.dameIp())) != null){
				m = new MensajeTCP();
				m.fijaCodOperacion(COD_OK);
				esclavo.enviarMensaje(m.toString());
				regionesCriticasLibres[idCliente.dameIdRegionCritica()] = false;
				interfaz.actualizarListaClientesEspera(clientesEspera);
			}
		}
	}
}
