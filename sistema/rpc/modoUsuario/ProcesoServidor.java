package sistemaDistribuido.sistema.rpc.modoUsuario;

//import sistemaDistribuido.sistema.rpc.modoMonitor.RPC;   //para siguiente actividad
import sistemaDistribuido.sistema.clienteServidor.modoMonitor.Nucleo;
import sistemaDistribuido.sistema.clienteServidor.modoUsuario.Proceso;
import sistemaDistribuido.util.Escribano;

/**
 * 
 */
public class ProcesoServidor extends Proceso{
	//private LibreriaServidor lib;   //para la actividad "Conexion Dinamica"

	/**
	 * 
	 */
	public ProcesoServidor(Escribano esc){
		super(esc);
		//lib=new LibreriaServidor(esc);   //para la actividad "Resguardos..."
		start();
	}

	/**
	 * Resguardo del servidor
	 */
	public void run(){
		imprimeln("Proceso servidor en ejecucion.");
		//idUnico=RPC.exportarInterfaz("FileServer", "3.1", asa)  //para la actividad "Conexion Dinamica"

		while(continuar()){
			Nucleo.receive(dameID(),null);
			//...
			//desempacar parametros
			//c=lib.suma(a,b)
		}

		//RPC.deregistrarInterfaz(nombreServidor, version, idUnico)  //para la actividad "Conexion Dinamica"
	}
}
