package sistemaDistribuido.sistema.clienteServidor.modoUsuario.SeisDos;

import java.util.Arrays;

import sistemaDistribuido.sistema.clienteServidor.modoMonitor.Nucleo;
import sistemaDistribuido.sistema.clienteServidor.modoUsuario.Proceso;
import sistemaDistribuido.util.Escribano;
import sistemaDistribuido.util.Pausador;

/**
 * 
 */
public class ProcesoServidor extends Proceso{

	/**
	 * 
	 */
	public ProcesoServidor(Escribano esc){
		super(esc);
		start();
	}

	/**
	 * 
	 */
	public void run(){
		int TAMANIO_SOLICITUD = 2000;
		int ESPACIO_RESPUESTA = 1980;
		int OCHO = 8;
		int TRES = 3;
		imprimeln("Proceso servidor en ejecucion.");
		byte[] solServidor=new byte[TAMANIO_SOLICITUD];
		byte[] respServidor;
		byte[] dato = new byte[OCHO];
		int ID_SERVER;
		while(continuar()){
			Nucleo.receive(dameID(),solServidor);
			ID_SERVER = solServidor[TRES];
			imprime("Procesando peticion recibida del cliente");
			System.out.println(">>>>>"+Arrays.toString(solServidor));//mio
			for(int i=0;i<OCHO;i++)
				dato[i] = solServidor[i];
			imprimeln("el cliente envia un "+Arrays.toString(dato));
			respServidor=new byte[TAMANIO_SOLICITUD];
			for(int i=OCHO;i<ESPACIO_RESPUESTA;i++)
				respServidor[i-OCHO] = solServidor[i];
			Pausador.pausa(1000);  //sin esta línea es posible que Servidor solicite send antes que Cliente solicite receive
			imprimeln("enviando respuesta a cliente id:"+ID_SERVER);
			Nucleo.send(ID_SERVER,respServidor);
		}
	}
}