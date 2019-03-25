/* 	Sistemas Concurrentes y Distribuidos
	Modelo Cliente/Servidor - Mecanismo de Comunicación Básico
	CeroCero
	Tiempo=04:10
*/
package sistemaDistribuido.visual.clienteServidor;

import sistemaDistribuido.sistema.clienteServidor.modoMonitor.Nucleo;
import sistemaDistribuido.sistema.clienteServidor.modoMonitor.ParMaquinaProceso;
import sistemaDistribuido.util.Escribano;
import sistemaDistribuido.util.Pausador;
import sistemaDistribuido.visual.clienteServidor.PanelClienteServidor;
import sistemaDistribuido.visual.clienteServidor.ProcesoFrame;
import sistemaDistribuido.visual.util.PanelInformador;
import sistemaDistribuido.visual.util.PanelIPID;
import microKernelBasedSystem.util.WriterManager;
import microKernelBasedSystem.util.Writer;
import java.awt.Frame;
import java.awt.BorderLayout;
import java.awt.Panel;
import java.awt.event.WindowListener;
import java.awt.event.WindowEvent;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class MicroNucleoFrame extends Frame implements WindowListener,Escribano,ParMaquinaProceso{
	private static final long serialVersionUID=1;
	private PanelClienteServidor panelBotones;
	private ProcesoFrame procesos[]=new ProcesoFrame[2];
	protected PanelInformador informador;
	protected PanelIPID destinatario;
	private WriterManager writerMan=new WriterManager(this);
	private Integrante[] integrantes;
	
	
	private static final int NUM_INTEGRANTES = 3;

	private static final String[] nicknamesIntegrantes = {"CuatroUno","CeroCero","SeisDos"};

	public MicroNucleoFrame(){
		super("Practicas 1 y 2 - Modelo Cliente/Servidor");
		integrantes = new Integrante[NUM_INTEGRANTES];
		for(int i = 0; i < NUM_INTEGRANTES; i++){
			integrantes[i] = new Integrante(nicknamesIntegrantes[i],i+1);
		}
		setLayout(new BorderLayout());
		informador=new PanelInformador();
		destinatario=new PanelIPID();
		add("North",destinatario);
		add("Center",informador);
		add("South",construirPanelSur());
		setSize(500,300);
		addWindowListener(this);
		
	}
	
	
	public void levantarServidores(){
		
		
	}

	public void imprime(String s){
		informador.imprime(s);
	}

	public void imprimeln(String s){
		informador.imprimeln(s);
	}

	public String dameIP(){
		return destinatario.dameIP();
	}

	public int dameID(){
		int i=0;
		try{
			i=Integer.parseInt(destinatario.dameID());
		}catch(NumberFormatException e){
			imprimeln(e.getMessage());
		}
		return i;
	}

	protected Panel construirPanelSur(){
		panelBotones=new PanelClienteServidor();
		panelBotones.agregarIntegrantes(integrantes);
		panelBotones.agregarActionListener(new ManejadorBotones());
		return panelBotones;
	}

	protected void levantarProcesoFrame(ProcesoFrame p){
		ProcesoFrame temp[];
		boolean encontro=false;
		int i;
		for(i=0;i<procesos.length;i++){
			if (procesos[i]==null){
				procesos[i]=p;
				encontro=true;
				imprimeln("Ventana de proceso "+procesos[i].dameIdProceso()+" iniciada.");
				break;
			}
		}
		if (!encontro){
			temp=new ProcesoFrame[procesos.length+1];
			for(i=0;i<procesos.length;i++){
				temp[i]=procesos[i];
			}
			temp[i]=p;
			procesos=temp;
		}
	}

	class ManejadorBotones implements ActionListener{

		public void actionPerformed(ActionEvent e){
			//String com=e.getActionCommand();
			int idServidor;
			String nicknameSeleccionado;
			
			idServidor = -1;
			nicknameSeleccionado = panelBotones.dameNicknameSeleccionado();
			for(int i = 0; i < NUM_INTEGRANTES; i++){
				if(integrantes[i].dameNickname().equals(nicknameSeleccionado)){
					idServidor = i+1;
					break;
				}
			}
			switch(e.getActionCommand()){
			case "Cliente":
				switch(idServidor){
				case 1:	levantarProcesoFrame(new sistemaDistribuido.visual.clienteServidor.cuatroUno.ClienteFrame(MicroNucleoFrame.this));	break;
				case 2:	levantarProcesoFrame(new sistemaDistribuido.visual.clienteServidor.ceroCero.ClienteFrame(MicroNucleoFrame.this));	break;
				case 3:	levantarProcesoFrame(new sistemaDistribuido.visual.clienteServidor.SeisDos.ClienteFrame(MicroNucleoFrame.this));	break;
				default: ;
				}
				break;
			case "Servidor":
				switch(idServidor){
				case 1: levantarProcesoFrame(new sistemaDistribuido.visual.clienteServidor.cuatroUno.ServidorFrame(MicroNucleoFrame.this));	break;
				case 2: levantarProcesoFrame(new sistemaDistribuido.visual.clienteServidor.ceroCero.ServidorFrame(MicroNucleoFrame.this));	break;
				case 3: levantarProcesoFrame(new sistemaDistribuido.visual.clienteServidor.SeisDos.ServidorFrame(MicroNucleoFrame.this));	break;
				default: ;
				}
				break;
			default: ;	break;
			}
		}
	}

	public void cerrarFrameProceso(ProcesoFrame pf){
		synchronized(procesos){
			for(int i=0;i<procesos.length;i++){
				if (procesos[i]!=null && procesos[i].equals(pf)){
					imprimeln("Cerrando ventana del proceso "+pf.dameIdProceso());
					Pausador.pausa(2000);
					pf.setVisible(false);
					procesos[i]=null;
					break;
				}
			}
		}
	}

	public void finalizar(){
		imprimeln("Terminando ventana del micronucleo...");
		synchronized(procesos){
			for(int i=0;i<procesos.length;i++){
				if (procesos[i]!=null){
					cerrarFrameProceso(procesos[i]);
				}
			}
		}
		Pausador.pausa(2000);
		System.exit(0);
	}

	public void windowClosing(WindowEvent e){
		Nucleo.cerrarSistema();
	}

	public void windowActivated(WindowEvent e){
	}
	public void windowClosed(WindowEvent e){
	}
	public void windowDeactivated(WindowEvent e){
	}
	public void windowDeiconified(WindowEvent e){
	}
	public void windowIconified(WindowEvent e){
	}
	public void windowOpened(WindowEvent e){
	}

	public void finish() {
		finalizar();
	}

	public void print(String s) {
		imprime(s);
	}

	public void println(String s) {
		imprimeln(s);
	}

	public WriterManager getWriterManager() {
		return writerMan;
	}
	
	public void appendWriter(Writer w){
	}

	public static void main(String args[]){
		MicroNucleoFrame mnf=new MicroNucleoFrame();
		mnf.setVisible(true);
		mnf.imprimeln("Ventana del micronucleo iniciada.");
		Nucleo.iniciarSistema(mnf,2001,2002,mnf);
		mnf.levantarServidores();
	}
}
