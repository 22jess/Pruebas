package sistemaDistribuido.visual.exclusionMutuaCentralizado;


import java.util.ArrayList;

import sistemaDistribuido.sistema.exclusionMutuaCentralizado.IdCliente;



public interface Interfaz {
	public void imprimirln(String s);
	
	public void actualizarListaClientesEspera(ArrayList<IdCliente> listaEspera);

	public void activarBotonLiberarRegionCritica();
	
}
