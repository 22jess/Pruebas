package sistemaDistribuido.visual.clienteServidor.SeisDos;

import sistemaDistribuido.sistema.clienteServidor.modoUsuario.SeisDos.ProcesoServidor;
import sistemaDistribuido.visual.clienteServidor.MicroNucleoFrame;
import sistemaDistribuido.visual.clienteServidor.ProcesoFrame;

public class ServidorFrame extends ProcesoFrame{
  private static final long serialVersionUID=1;
  public static ProcesoServidor proc;

  public ServidorFrame(MicroNucleoFrame frameNucleo){
    super(frameNucleo,"Servidor de Archivos");
    proc=new ProcesoServidor(this);
    fijarProceso(proc);
  }
}