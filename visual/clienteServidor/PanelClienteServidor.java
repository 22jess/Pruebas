package sistemaDistribuido.visual.clienteServidor;

import java.awt.Panel;
import java.awt.Button;
import java.awt.event.ActionListener;

import java.awt.Choice;

public class PanelClienteServidor extends Panel{
  private static final long serialVersionUID=1;
  private Button botonCliente,botonServidor;
  private Choice opcionesIntegrantes;

  public PanelClienteServidor(){
     botonCliente=new Button("Cliente");
     botonServidor=new Button("Servidor");
     opcionesIntegrantes = new Choice();
     add(opcionesIntegrantes);
     add(botonCliente);
     add(botonServidor);
  }
  
  public void agregarIntegrantes(Integrante[] integrantes){
	  for(Integrante i : integrantes){
		  opcionesIntegrantes.add(i.dameNickname());
	  }
  }
  
  public Button dameBotonCliente(){
    return botonCliente;
  }
  
  public Button dameBotonServidor(){
    return botonServidor;
  }
  
  public void agregarActionListener(ActionListener al){
    botonCliente.addActionListener(al);
    botonServidor.addActionListener(al);
  }
  
  public String dameNicknameSeleccionado(){
	  return opcionesIntegrantes.getSelectedItem();
  }
}