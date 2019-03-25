package sistemaDistribuido.visual.exclusionMutuaCentralizado;



import java.awt.BorderLayout;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import sistemaDistribuido.sistema.exclusionMutuaCentralizado.IdCliente;
import sistemaDistribuido.sistema.exclusionMutuaCentralizado.tcp.ServidorTCP;
import sistemaDistribuido.sistema.exclusionMutuaCentralizado.udp.ServidorUDP;


public class VentanaServidor extends JFrame implements Interfaz{

	
	private static final long serialVersionUID = 1L;
	
	private static final int PIXELES_ALTO = 650;
	private static final int PIXELES_ANCHO = 500;
	
	private AreaTextoConScroll areaSucesosRelevantes;
	private PanelProcesosEspera panelEspera;
	
	public VentanaServidor() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		add(BorderLayout.SOUTH, panelEspera = new PanelProcesosEspera());
		add(BorderLayout.NORTH, areaSucesosRelevantes = new AreaTextoConScroll());
		
		areaSucesosRelevantes.setEditable(false);
		setSize(PIXELES_ANCHO,PIXELES_ALTO);
		
		setTitle("*Coordinador región crítica*");
		new ServidorUDP(this);
		
		setVisible(true);
		
	}

	@Override
	public void imprimirln(String s) {
		areaSucesosRelevantes.append(s + "\n");
		
	}
	
	public void actualizarListaClientesEspera(ArrayList<IdCliente> listaEspera){
		panelEspera.imprimirListaEspera(listaEspera);
	}
	
	private class PanelProcesosEspera extends JPanel{
	
		private static final long serialVersionUID = 1L;

		private AreaTextoConScroll areaListaProcesosEspera; 
		
		PanelProcesosEspera(){
			setLayout(new BorderLayout());
			add(BorderLayout.NORTH, new JLabel("*Lista de espera*"));
			add(BorderLayout.CENTER, areaListaProcesosEspera = new AreaTextoConScroll());
			areaListaProcesosEspera.setEditable(false);
		}

		public void imprimirListaEspera(ArrayList<IdCliente> listaEspera) {
			areaListaProcesosEspera.setText("");
			for(IdCliente i : listaEspera){
				areaListaProcesosEspera.append(i.toString() + "\n");
			}
		}
	}

	@Override
	public void activarBotonLiberarRegionCritica() {}
}
