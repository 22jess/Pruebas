package sistemaDistribuido.visual.exclusionMutuaCentralizado;



import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import sistemaDistribuido.sistema.exclusionMutuaCentralizado.Cliente;
import sistemaDistribuido.sistema.exclusionMutuaCentralizado.tcp.ClienteTCP;
import sistemaDistribuido.sistema.exclusionMutuaCentralizado.udp.ClienteUDP;
import sistemaDistribuido.sistema.exclusionMutuaCentralizado.IdCliente;


public class VentanaCliente extends JFrame implements Interfaz{

	private static final long serialVersionUID = 1L;
	
	private static final int PIXELES_ALTO = 500;
	private static final int PIXELES_ANCHO = 500;
	
	private PanelCampos panelCampos;
	private AreaTextoConScroll areaSucesosRelevantes;
	private Cliente cliente;
	

	public VentanaCliente(){
		
		setSize(PIXELES_ANCHO,PIXELES_ALTO);
		add(BorderLayout.CENTER, areaSucesosRelevantes = new AreaTextoConScroll());
		add(BorderLayout.NORTH,panelCampos = new PanelCampos());
		panelCampos.agregarActionListenerBotonLiberar(new ManejadorBotonLiberarRegionCritica());           
		panelCampos.agregarActionListenerBotonPeticion(new ManejadorBotonPeticionRegionCritica());
		areaSucesosRelevantes.setEditable(false);
		
		setTitle("*Cliente región crítica*");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
	}
	
	private Cliente crearCliente(){
		return new ClienteUDP(this,panelCampos.dameIpServidor());
	}
	
	private class ManejadorBotonPeticionRegionCritica implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			 new HiloProcesador().start(); 
		}
		private class HiloProcesador extends Thread{
			
			public void run(){
				if(cliente == null){
					cliente = crearCliente();
				}
				panelCampos.habilitarBotonPeticion(false);
				cliente.enviarMensajeSolicitudRegionCritica();				
			}
		}
	}
	
	private class ManejadorBotonLiberarRegionCritica implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			new HiloProcesador().start();
		}
		private class HiloProcesador extends Thread{
			public void run(){
				panelCampos.habilitarBotonPeticion(true);
				panelCampos.habilitarBotonLiberar(false);
				cliente.enviarMensajeLiberarRegionCritica();
			}
		}
	}
	
	@Override
	public void imprimirln(String s) {
		areaSucesosRelevantes.append(s +"\n");
	}
	public void actualizarListaClientesEspera(ArrayList<IdCliente> listaEspera){}
	
	
	private class PanelCampos extends JPanel{

		private static final long serialVersionUID = 1L;
		private static final String ETIQUETA_BOTON_PETICION = "Solicitar región";
		private static final String ETIQUETA_BOTON_LIBERAR = "Liberar región";
		private JTextField campoIp;
		private JButton botonPeticionRegion;
		private JButton botonLiberarRegion;
		
		PanelCampos(){
			setLayout(new GridLayout(2,2));
			add(new JLabel("IP servidor:"));
			add(campoIp = new JTextField());
			add(botonPeticionRegion = new JButton(ETIQUETA_BOTON_PETICION));
			add(botonLiberarRegion = new JButton(ETIQUETA_BOTON_LIBERAR));
			botonLiberarRegion.setEnabled(false);
		}
		
		 public String dameIpServidor(){
			 return campoIp.getText();
		 }
		 
		 public void habilitarBotonPeticion(boolean b){
			 botonPeticionRegion.setEnabled(b);
		 }
		 
		 public void habilitarBotonLiberar(boolean b){
			 botonLiberarRegion.setEnabled(b);
		 }
		 
		 
		 public void agregarActionListenerBotonPeticion(ActionListener a){
			 botonPeticionRegion.addActionListener(a);
		 }
		 
		 public void agregarActionListenerBotonLiberar(ActionListener a){
			 botonLiberarRegion.addActionListener(a);
		 }
		 
	}

	@Override
	public void activarBotonLiberarRegionCritica() {
		panelCampos.habilitarBotonLiberar(true);
		
	}
}
