package sistemaDistribuido.sistema.exclusionMutuaCentralizado;

import sistemaDistribuido.visual.exclusionMutuaCentralizado.Interfaz;


public abstract class Cliente implements ProcesadorMensaje,Constantes{
	public static final int PUERTO_ENTRADA = 2018;
	protected static final int PUERTO_SALIDA = 19999;
	
	protected Interfaz interfaz;
	protected String ipServidor;
	
	protected boolean esperar;
	
	public Cliente(Interfaz interfaz,String ipServidor){
		this.interfaz = interfaz;
		this.ipServidor = ipServidor;
	}
	public abstract void enviarMensajeSolicitudRegionCritica();
	public abstract void enviarMensajeLiberarRegionCritica();

	
	public void procesarMensaje(Mensaje mensaje, String ip) {
		
		switch(mensaje.dameCodOperacion()){
		case COD_OK:
			synchronized (this){
				esperar = false;
				notify();
			}
			interfaz.imprimirln("Entró en la region crítica");
			interfaz.activarBotonLiberarRegionCritica();
			;	break;
		case COD_DENEGAR_ACCESO: 
			interfaz.imprimirln("No pudo entrar a la región crítica. Esperando...");
		}
	}
}
