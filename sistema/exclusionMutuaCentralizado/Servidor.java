package sistemaDistribuido.sistema.exclusionMutuaCentralizado;

import java.util.ArrayList;

import sistemaDistribuido.visual.exclusionMutuaCentralizado.Interfaz;




public abstract class Servidor implements Constantes {
	public static final int PUERTO_ENTRADA = 3312;
	
	
	protected static final int PUERTO_SALIDA = 3317;
	protected static final int MAX_REGIONES_CRITICAS = 1;
	
	
	
	protected Interfaz interfaz;
	protected volatile boolean[] regionesCriticasLibres;
	protected ArrayList<IdCliente> clientesEspera;
	
	
	public Servidor(Interfaz interfaz){
		this.interfaz = interfaz;
		regionesCriticasLibres = new boolean[MAX_REGIONES_CRITICAS];
		for(int i = 0; i < MAX_REGIONES_CRITICAS; i++){
			regionesCriticasLibres[i] = true;
		}
		clientesEspera = new ArrayList<IdCliente>();
	}
	
	
	public void agregarClienteLista(IdCliente id){
		if(!clientesEspera.contains(id)){
			clientesEspera.add(id);
		}
	}
	
	public abstract void notificarSiguienteClienteEspera();
}
