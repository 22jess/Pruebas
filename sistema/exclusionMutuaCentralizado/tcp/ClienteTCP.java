package sistemaDistribuido.sistema.exclusionMutuaCentralizado.tcp;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

import sistemaDistribuido.sistema.exclusionMutuaCentralizado.Cliente;

import sistemaDistribuido.sistema.exclusionMutuaCentralizado.Constantes;
import sistemaDistribuido.sistema.exclusionMutuaCentralizado.Mensaje;
import sistemaDistribuido.sistema.exclusionMutuaCentralizado.Servidor;
import sistemaDistribuido.visual.exclusionMutuaCentralizado.Interfaz;



public class ClienteTCP extends Cliente implements Constantes, Runnable{
	
	private PrintWriter salida;
	private BufferedReader entrada;
	private Socket conexion;

	public ClienteTCP(Interfaz interfaz, String ipServidor) {		
		super(interfaz, ipServidor);
		
		try {
			conexion = new Socket(ipServidor, Servidor.PUERTO_ENTRADA);
			
			salida = new PrintWriter(conexion.getOutputStream(), true);
		    entrada = new BufferedReader(new InputStreamReader(conexion.getInputStream()));
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		new Thread(this).start();
	}
	
	public void enviarMensajeSolicitudRegionCritica(){
		Mensaje m = new MensajeTCP();
		
		interfaz.imprimirln("Solicitando acceso a la region crítica");
		m.fijaCodOperacion(COD_SOLICITAR_REGION);
		m.fijaIdRegionCritica(0);
		salida.println(m.toString());
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
	
	public void enviarMensajeLiberarRegionCritica(){
		Mensaje m = new MensajeTCP();
		
		interfaz.imprimirln("Liberando la region crítica");
		m.fijaCodOperacion(COD_LIBERAR_REGION);
		m.fijaIdRegionCritica(0);
		salida.println(m.toString());
	}
	
	public void terminar(){
		try {
			if(conexion != null && !conexion.isClosed()){
				conexion.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		while(true){
			try {
				procesarMensaje(new MensajeTCP(entrada.readLine()), "");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
