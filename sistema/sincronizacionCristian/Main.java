/* 	Sistemas Concurrentes y Distribuidos
	Algoritmos de Sincronización de Relojes - Cristian
	CeroCero
*/
package sistemaDistribuido.sistema.sincronizacionCristian;

import java.util.Scanner;

import sistemaDistribuido.visual.sincronizacionCristian.VentanaCliente;
import sistemaDistribuido.visual.sincronizacionCristian.VentanaServidor;


public class Main {
	
	public static final int OPC_SERVIDOR = 1;
	public static final int OPC_CLIENTE = 2;

	public static void main(String[] args) {
		int opcion;
		Scanner scan;
		if(args.length >= 1){
			opcion = Integer.parseInt(args[0]);
		}
		else{
			scan = new Scanner(System.in);
			System.out.println("Dame el tipo de maquina que quieres:");
			System.out.println("1. Servidor.");
			System.out.println("2. Cliente.");
			opcion = scan.nextInt();
			scan.close();
		}
		switch(opcion){
		case OPC_SERVIDOR:	new VentanaServidor();			break;
		case OPC_CLIENTE:	new VentanaCliente();			break;
		default: System.out.println("OPCION INVALIDA!");	break;
		}
	}

}
