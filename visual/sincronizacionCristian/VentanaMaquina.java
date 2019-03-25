/* 	Sistemas Concurrentes y Distribuidos
	Algoritmos de Sincronización de Relojes - Cristian
	CeroCero
*/
package sistemaDistribuido.visual.sincronizacionCristian;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import sistemaDistribuido.sistema.sincronizacionCristian.Constantes;
import sistemaDistribuido.sistema.sincronizacionCristian.Impresor;
import sistemaDistribuido.sistema.sincronizacionCristian.TipoReloj;


public abstract class VentanaMaquina extends JFrame implements Constantes, Impresor{
	private static final long serialVersionUID = 1L;
	
	
	protected static final String ETIQUETA_BOTON_INICIAR = "Iniciar";
	private static final int PIXELES_ALTO = 190;
	private static final int PIXELES_ANCHO = 900;
	
	protected PanelCampos panelCampos;
	protected AreaTextoConScroll areaTexto;
	
	

	public VentanaMaquina(){
		add(BorderLayout.WEST,panelCampos = new PanelCampos());
		add(BorderLayout.CENTER,areaTexto = new AreaTextoConScroll());
		setTitle("Sincronización de Relojes - Cristian");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		areaTexto.setEditable(false);
		setSize(PIXELES_ANCHO,PIXELES_ALTO);
	}
	
	class AreaTextoConScroll extends JScrollPane{

		private static final long serialVersionUID = 1L;
		private JTextArea areaTexto;
		
		AreaTextoConScroll(){
			setViewportView(areaTexto = new JTextArea(20,10));
		}
		
		public void setEditable(boolean b){
			areaTexto.setEditable(b);
		}

		public void append(String s) {
			areaTexto.append(s);
		}
	}

	class PanelCampos extends JPanel{
		private static final long serialVersionUID = 1L;
		
		private JTextField camposTexto[] = new JTextField[8];
		private final String etiquetas[] ={
				"Tiempo reloj inicial",
				"Distorsion Delta:",
				"Interrupciones/segundo (H)",
				"Estimado propagacion mensajes",
				"Ro",
				"Intervalo tiempo delta/2*ro",
				"Tiempo actual",
				"N"
		};
		private JComboBox<TipoReloj> opcionesReloj;
		private JButton botonIniciar;
		
		PanelCampos(){
			setLayout(new GridLayout(10,2));
			for(int i = 0; i < 8; i++){
				add(new JLabel(etiquetas[i]));
				add(camposTexto[i] = new JTextField(5));
			}
			add(new JLabel("Tipo reloj"));
			add(opcionesReloj = new JComboBox<TipoReloj>());
			for(TipoReloj t : TipoReloj.values()){
				opcionesReloj.addItem(t);
			}
			add(botonIniciar = new JButton(ETIQUETA_BOTON_INICIAR));
		}
		
		public void agregarActionListenerBotonIniciar(ActionListener a){
			botonIniciar.addActionListener(a);
		}
		
		public String dameTiempoRelojInicial(){
			return camposTexto[0].getText();
		}
		
		public String dameDistorsiondelta(){
			return camposTexto[1].getText();
		}
		
		public String dameInterrupcionesPorSegundo(){
			return camposTexto[2].getText();
		}
		
		public String dameEstimadoPropagacionMsjes(){
			return camposTexto[3].getText();
		}
		
		public String dameRo(){
			return camposTexto[4].getText();
		}
		
		public TipoReloj dameTipoReloj(){
			return (TipoReloj) opcionesReloj.getSelectedItem();
		}
		
		public void fijaIntervaloDelta(float n) {
			// TODO Auto-generated method stub
			camposTexto[5].setText(Float.toString(n));
		}
		
		public void fijaRo(float n) {
			// TODO Auto-generated method stub
			camposTexto[4].setText(Float.toString(n));
		}
		
		public void fijaEstimadoPropagacion(int n) {
			// TODO Auto-generated method stub
			camposTexto[3].setText(Integer.toString(n));
		}
		
		public void fijaInterrupcionesSegundo(int n) {
			// TODO Auto-generated method stub
			camposTexto[2].setText(Integer.toString(n));
		}
		public void fijaDelta(int n) {
			// TODO Auto-generated method stub
			camposTexto[1].setText(Integer.toString(n));
		}
		public void fijaTiempoInicial(int n) {
			// TODO Auto-generated method stub
			camposTexto[0].setText(Integer.toString(n));
		}
		public void fijaN(long n) {
			// TODO Auto-generated method stub
			camposTexto[7].setText(Long.toString(n));
			
		}

		public void fijaTiempoActual(long t) {
			// TODO Auto-generated method stub
			camposTexto[6].setText(Long.toString(t));
		}
	}
	
	void agregarActionListenerBotonIniciar(ActionListener a){
		panelCampos.agregarActionListenerBotonIniciar(a);
	}
}
