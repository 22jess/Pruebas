/* 	Sistemas Concurrentes y Distribuidos
	Algoritmos de Sincronización de Relojes - Cristian
	CeroCero
*/
package sistemaDistribuido.visual.sincronizacionCristian;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;


import sistemaDistribuido.sistema.sincronizacionCristian.Cliente;
import sistemaDistribuido.sistema.sincronizacionCristian.Impresor;


public class VentanaCliente  extends VentanaMaquina{
	private static final long serialVersionUID = 1L;
	
	private Cliente cliente;
	private PanelCamposCliente panelCamposCliente;
	
	
	public VentanaCliente(){
		agregarActionListenerBotonIniciar(new ManejadorBotonIniciar(this));
		add(BorderLayout.NORTH,panelCamposCliente = new PanelCamposCliente());
		
		panelCampos.fijaRo((float)0.2);
		panelCampos.fijaEstimadoPropagacion(20);		
		panelCampos.fijaInterrupcionesSegundo(10);
		panelCampos.fijaDelta(10000);
		panelCampos.fijaIntervaloDelta((float)(10000/(2*0.2)));
		panelCampos.fijaTiempoInicial(0);
		panelCamposCliente.fijaIp("127.0.0.1");
		
		

		setVisible(true);
	}
	
	
	class ManejadorBotonIniciar implements ActionListener{
		Impresor impresor;

		ManejadorBotonIniciar(Impresor impresor){
			this.impresor = impresor;
		}
		@Override
		public void actionPerformed(ActionEvent e) {
			cliente = new Cliente(Integer.parseInt(panelCampos.dameTiempoRelojInicial()),
								  Integer.parseInt(panelCampos.dameDistorsiondelta()),
								  Integer.parseInt(panelCampos.dameInterrupcionesPorSegundo()),
								  panelCampos.dameTipoReloj(),
								  Float.parseFloat(panelCampos.dameRo()),
								  Integer.parseInt(panelCampos.dameEstimadoPropagacionMsjes()),
								  impresor,panelCamposCliente.dameIpServidor());
			
			cliente.start();
		}
	}
	
	class PanelCamposCliente extends JPanel{
		
		private static final long serialVersionUID = 1L;
		private JTextField campoIp;
		
		
		public PanelCamposCliente(){
			setLayout(new GridLayout(1,2));
			add(new JLabel("IP:"));
			add(campoIp = new JTextField());
		}


		public void fijaIp(String ip) {
			campoIp.setText(ip);
			
		}


		public String dameIpServidor() {
			// TODO Auto-generated method stub
			return campoIp.getText();
		}
	}


	@Override
	public void imprimir(String s) {
		// TODO Auto-generated method stub
		areaTexto.append(s);
	}


	@Override
	public void imprimirLn(String s) {
		// TODO Auto-generated method stub
		areaTexto.append(s + "\n");
	}


	@Override
	public void imprimirTiempoActualMaquina(long t) {
		// TODO Auto-generated method stub
		panelCampos.fijaTiempoActual(t);
		
	}


	@Override
	public void imprimirN(long n) {
		panelCampos.fijaN(n);
		
	}

}
