/* 	Sistemas Concurrentes y Distribuidos
	Algoritmos de Sincronización de Relojes - Cristian
	CeroCero
*/
package sistemaDistribuido.visual.sincronizacionCristian;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import sistemaDistribuido.sistema.sincronizacionCristian.Impresor;
import sistemaDistribuido.sistema.sincronizacionCristian.ServidorTiempo;



public class VentanaServidor extends VentanaMaquina {
	private static final long serialVersionUID = 1L;
	
	private PanelCamposServidor panelCamposServ;
	
	private ServidorTiempo servidor;

	public VentanaServidor(){
		
		setTitle("*Servidor Tiempo*");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		add(BorderLayout.EAST,panelCamposServ = new PanelCamposServidor());
		agregarActionListenerBotonIniciar(new ManejadorBotonIniciar(this));
		
		//************************
		panelCampos.fijaRo((float)0.2);
		panelCampos.fijaEstimadoPropagacion(20);		
		panelCampos.fijaInterrupcionesSegundo(10);
		panelCampos.fijaDelta(10000);
		panelCampos.fijaIntervaloDelta((float)(10000/(2*0.2)));
		panelCampos.fijaTiempoInicial(0);
		panelCamposServ.fijaTControlInterrup(2000);
		panelCamposServ.fijadameRetardoEnvioRespuesta(1);
		panelCamposServ.fijaRetardoRecepcionSolicitud(1);
		//************************
		
		setVisible(true);
		
		
		
	}
	
	class ManejadorBotonIniciar implements ActionListener{
		Impresor impresor;
		
		ManejadorBotonIniciar(Impresor impresor){
			this.impresor = impresor;
		}
		
		public void actionPerformed(ActionEvent e) {
			servidor= new ServidorTiempo(
								Integer.parseInt(panelCampos.dameTiempoRelojInicial()),
								Integer.parseInt(panelCampos.dameDistorsiondelta()),
								Integer.parseInt(panelCampos.dameInterrupcionesPorSegundo()),
								panelCampos.dameTipoReloj(),
								Float.parseFloat(panelCampos.dameRo()),
								Integer.parseInt(panelCampos.dameEstimadoPropagacionMsjes()),
								Integer.parseInt(panelCamposServ.dameTControlInterrup()),
								Integer.parseInt(panelCamposServ.dameRetardoEnvioRespuesta()),
								Integer.parseInt(panelCamposServ.dameRetardoRecepcionSolicitud()),
								impresor);
			servidor.start();
		}
	}
	
	
	class PanelCamposServidor extends JPanel{
		
		private static final long serialVersionUID = 1L;
		private JTextField campoTiempoControladorInterrupciones;
		private JTextField campoRetardoRecepcionSolicitud;
		private JTextField campoRetardoEnvioRespuesta;
		
		
		public PanelCamposServidor(){
			setLayout(new GridLayout(5,2));
			add(new JLabel("Ret. resp."));
			add(campoRetardoEnvioRespuesta = new JTextField());
			add(new JLabel("Ret. recepción"));
			add(campoRetardoRecepcionSolicitud = new JTextField());
			add(new JLabel("I"));
			add(campoTiempoControladorInterrupciones = new JTextField());
		}

		public void fijaRetardoRecepcionSolicitud(int i) {
			campoRetardoRecepcionSolicitud.setText(Integer.toString(i));
			
		}

		public void fijadameRetardoEnvioRespuesta(int i) {
			campoRetardoEnvioRespuesta.setText(Integer.toString(i));
			
		}

		public void fijaTControlInterrup(int i) {
			campoTiempoControladorInterrupciones.setText(Integer.toString(i));
			
		}

		public String dameRetardoRecepcionSolicitud() {
			// TODO Auto-generated method stub
			return campoRetardoRecepcionSolicitud.getText();
		}


		public String dameRetardoEnvioRespuesta() {
			// TODO Auto-generated method stub
			return campoRetardoEnvioRespuesta.getText();
		}


		public String dameTControlInterrup() {
			return campoTiempoControladorInterrupciones.getText();
		}

	}


	@Override
	public void imprimir(String s) {
		areaTexto.append(s);
		
	}


	@Override
	public void imprimirLn(String s) {
		areaTexto.append(s+"\n");
	}


	@Override
	public void imprimirTiempoActualMaquina(long t) {
		panelCampos.fijaTiempoActual(t);
		
	}


	@Override
	public void imprimirN(long n) {
		panelCampos.fijaN(n);
	}
	

	
	
}
