package sistemaDistribuido.visual.clienteServidor.ceroCero;

import sistemaDistribuido.sistema.clienteServidor.modoMonitor.Nucleo;
import sistemaDistribuido.sistema.clienteServidor.modoUsuario.ceroCero.*;
import sistemaDistribuido.visual.clienteServidor.MicroNucleoFrame;
import sistemaDistribuido.visual.clienteServidor.ProcesoFrame;
import java.awt.Label;
import java.awt.TextField;
import java.awt.Choice;
import java.awt.Button;
import java.awt.Panel;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class ClienteFrame extends ProcesoFrame{
	private static final long serialVersionUID=1;
	private sistemaDistribuido.sistema.clienteServidor.modoUsuario.ceroCero.ProcesoCliente proc;
	private Choice codigosOperacion;
	private TextField campoNombreArchivo;
	private TextField campoContenidoArchivo;
	private Button botonSolicitud;
	

	public ClienteFrame(MicroNucleoFrame frameNucleo){
		super(frameNucleo,"Cliente de Archivos");
		add("South",construirPanelSolicitud());
		validate();
		proc=new ProcesoCliente(this);
		fijarProceso(proc);
		setSize(700,400);
	}

	public Panel construirPanelSolicitud(){
		Panel p=new Panel();
		codigosOperacion=new Choice();
		for(OperacionArchivo o : OperacionArchivo.values()){
			codigosOperacion.add(o.toString());
		}
		campoNombreArchivo=new TextField(10);
		botonSolicitud=new Button("Solicitar");
		botonSolicitud.addActionListener(new ManejadorSolicitud());
		p.add(new Label("Operacion:"));
		p.add(codigosOperacion);
		p.add(new Label("Nombre archivo:"));
		p.add(campoNombreArchivo);
		p.add(new Label("Contenido archivo:"));
		p.add(campoContenidoArchivo = new TextField(10));
		p.add(botonSolicitud);
		campoContenidoArchivo.setText("Meh");
		campoNombreArchivo.setText("Archivito.txt");
		return p;
	}

	class ManejadorSolicitud implements ActionListener{
		public void actionPerformed(ActionEvent e) {
			String com=e.getActionCommand();
			OperacionArchivo operacion;
			
			if (com.equals("Solicitar")){
				botonSolicitud.setEnabled(false);
				operacion = OperacionArchivo.valueOf(codigosOperacion.getSelectedItem());
				imprimeln("Solicitud a enviar: "+operacion);
				imprimeln("Mensaje a enviar: "+campoNombreArchivo.getText());
				switch(operacion){
				case CREAR:	
					proc.fijaSolicitudAElaborar(CodigoOperacion.CREAR_ARCHIVO);
					proc.fijaNombreArchivo(campoNombreArchivo.getText());
				break;
				case ELIMINAR:
					proc.fijaSolicitudAElaborar(CodigoOperacion.ELIMINAR_ARCHIVO);
					proc.fijaNombreArchivo(campoNombreArchivo.getText());
				break;
				case LEER:	
					proc.fijaSolicitudAElaborar(CodigoOperacion.LEER_ARCHIVO);
					proc.fijaNombreArchivo(campoNombreArchivo.getText());
					break;
				case ESCRIBIR:
					proc.fijaSolicitudAElaborar(CodigoOperacion.ESCRIBIR_ARCHIVO);
					proc.fijaNombreArchivo(campoNombreArchivo.getText());
					proc.fijaContenidoArchivo(campoContenidoArchivo.getText());
					break;
				default: ;
				}
				Nucleo.reanudarProceso(proc);
			}
		}
	}
}
