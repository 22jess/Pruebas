package sistemaDistribuido.sistema.rpc.modoUsuario;

//import sistemaDistribuido.sistema.rpc.modoMonitor.RPC;  //para siguiente actividad
import sistemaDistribuido.sistema.clienteServidor.modoMonitor.Nucleo;
import sistemaDistribuido.sistema.rpc.modoUsuario.Libreria;
import sistemaDistribuido.util.Escribano;

public class LibreriaCliente extends Libreria{

	/**
	 * 
	 */
	public LibreriaCliente(Escribano esc){
		super(esc);
	}

	/**
	 * Ejemplo de resguardo del cliente suma
	 */
	protected void suma(){
		int asaDest=0;
		//saca parametros de pila

		//asaDest=RPC.importarInterfaz(nombreServidor, version)  //para la siguiente actividad
		Nucleo.send(asaDest,null);
		//devuelve valor izquierdo a traves de la pila
	}

}