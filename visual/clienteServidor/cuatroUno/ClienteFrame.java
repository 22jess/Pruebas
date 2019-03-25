/*
 * CuatroUno
 * Actividad 2
 * Tiempo=02:00
 */
package sistemaDistribuido.visual.clienteServidor.cuatroUno;

import sistemaDistribuido.sistema.clienteServidor.modoUsuario.cuatroUno.ProcesoCliente;
import sistemaDistribuido.visual.clienteServidor.MicroNucleoFrame;
import sistemaDistribuido.visual.clienteServidor.ProcesoFrame;
import java.awt.Label;
import java.awt.TextField;
import java.awt.Choice;
import java.awt.Button;
import java.awt.Panel;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.ActionEvent;

public class ClienteFrame extends ProcesoFrame{
	private static final long serialVersionUID=1;
	private ProcesoCliente proc;
	private Choice codigosOperacion;
	private TextField campoMensaje;
	private Button botonSolicitud;
	private String codop1,codop2,codop3,codop4;
	private String defecto1 = "nombreArchivo.ext";
	private String defecto2 = "nombreArchivo.ext";
	private String defecto3 = "nombreArchivo.ext";
	private String defecto4 = "nombreArchivo.ext�textoAEscribir";
	private String defectoE = "nombreArchivo.ext";

	public ClienteFrame(MicroNucleoFrame frameNucleo){
		super(frameNucleo,"Cliente de Archivos");
		add("South",construirPanelSolicitud());
		validate();
		proc=new ProcesoCliente(this);
		fijarProceso(proc);
	}

	public Panel construirPanelSolicitud(){
		Panel p=new Panel();
		codigosOperacion=new Choice();
		codop1="Crear";
		codop2="Eliminar";
		codop3="Leer";
		codop4="Escribir";
		codigosOperacion.add(codop1);
		codigosOperacion.add(codop2);
		codigosOperacion.add(codop3);
		codigosOperacion.add(codop4);
		codigosOperacion.addItemListener(new ManejadorOpciones());
		campoMensaje=new TextField(20);
		campoMensaje.setText(defecto1);
		botonSolicitud=new Button("Solicitar");
		botonSolicitud.addActionListener(new ManejadorSolicitud());
		p.add(new Label("Operacion:"));
		p.add(codigosOperacion);
		p.add(new Label("Datos:"));
		p.add(campoMensaje);
		p.add(botonSolicitud);
		return p;
	}
	
	class ManejadorOpciones implements ItemListener{
		public void itemStateChanged(ItemEvent ie) {
			if (codigosOperacion.getSelectedItem().equals(codop1)) {
				campoMensaje.setText(defecto1);
			}else if (codigosOperacion.getSelectedItem().equals(codop2)) {
				campoMensaje.setText(defecto2);
			}else if (codigosOperacion.getSelectedItem().equals(codop3)) {
				campoMensaje.setText(defecto3);
			}else if (codigosOperacion.getSelectedItem().equals(codop4)) {
				campoMensaje.setText(defecto4);
			}else {
				campoMensaje.setText(defectoE);
			}
		}
	}

	class ManejadorSolicitud implements ActionListener{
		public void actionPerformed(ActionEvent e) {
			String com=e.getActionCommand();
			if (com.equals("Solicitar")){
				botonSolicitud.setEnabled(false);
				com=codigosOperacion.getSelectedItem();
				imprimeln("Solicitud a enviar: "+com);
				imprimeln("Mensaje a enviar: "+campoMensaje.getText());
				if(com.equals(codop1)) {
					proc.crearArchivo(campoMensaje.getText());
				}else if(com.equals(codop2)) {
					proc.eliminarArchivo(campoMensaje.getText());
				}else if(com.equals(codop3)) {
					proc.leerArchivo(campoMensaje.getText());
				}else if(com.equals(codop4)) {
					String[] solicitud = campoMensaje.getText().split("¬");
					proc.escribirArchivo(solicitud[0], solicitud[1]);
				}else {
					imprimeln("El mensaje contiene errores.");
				}
				botonSolicitud.setEnabled(true);
			}
		}
	}
}