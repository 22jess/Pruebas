package sistemaDistribuido.sistema.clienteServidor.modoUsuario.SeisDos;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;

import sistemaDistribuido.sistema.clienteServidor.modoMonitor.Nucleo;
import sistemaDistribuido.sistema.clienteServidor.modoUsuario.Proceso;
import sistemaDistribuido.util.Escribano;
import sistemaDistribuido.util.Pausador;
import sistemaDistribuido.visual.clienteServidor.SeisDos.ClienteFrame;
import sistemaDistribuido.visual.clienteServidor.SeisDos.ServidorFrame;

/**
 * 
 */
public class ProcesoCliente extends Proceso{

	/**
	 * 
	 */
	public ProcesoCliente(Escribano esc){
		super(esc);
		start();
	}

	private void encadenadorDeByte(byte[] solCliente,int idProcesoCliente,int idProcesoServidor,int tamanioMensaje,String mensaje,int tamanioCodop,String codop) {

		int DIEZ = 10;
		int CIEN = 100;
		int CERO = 0;
		int OCHO = 8;
		int DOS = 2;
		int TRES = 3;
		int CUATRO = 4;
		int SEIS = 6;
		int SIETE = 7;
		Integer idOrigenNumero = idProcesoCliente;
        String cadenaOrigenIdNumero = idOrigenNumero.toString();
		Integer idDestinoNumero = idProcesoServidor;
        String cadenaDestinoIdNumero = idDestinoNumero.toString();
		imprime("Generando mensaje a ser enviado, llenando los campos necesarios");
		if(idProcesoCliente < DIEZ) {
	        int i=CERO;
			for(;i<TRES;i++)
				solCliente[i] = (byte)CERO;
	        for(;i<CUATRO;i++)
				solCliente[i] = (byte)Character.digit(cadenaOrigenIdNumero.charAt(i-TRES), DIEZ);
		} else if(idProcesoCliente < CIEN) {
	        int i=CERO;
			for(;i<DOS;i++)
				solCliente[i] = (byte)CERO;
	        for(;i<CUATRO;i++)
				solCliente[i] = (byte)Character.digit(cadenaOrigenIdNumero.charAt(i-DOS), DIEZ);
		}
		if(idProcesoServidor < DIEZ) {
	        int i=CUATRO;
			for(;i<SIETE;i++)
				solCliente[i] = (byte)CERO;
	        for(;i<OCHO;i++)
				solCliente[i] = (byte)Character.digit(cadenaDestinoIdNumero.charAt(i-SIETE), DIEZ);
		} else if(idProcesoServidor < CIEN) {
	        int i=CUATRO;
			for(;i<SEIS;i++)
				solCliente[i] = (byte)CERO;
	        for(;i<OCHO;i++)
				solCliente[i] = (byte)Character.digit(cadenaDestinoIdNumero.charAt(i-SEIS), DIEZ);
		}
		int tamanioArreglo = OCHO+tamanioCodop+tamanioMensaje;
		byte[] codopByte = codop.getBytes(); 
		byte[] mensajeByte = mensaje.getBytes(); 
		for(int i=OCHO;i<tamanioArreglo;i++) {
			if(i < (OCHO+tamanioCodop)) {
				solCliente[i] = codopByte[i-OCHO];
			} else {
				solCliente[i] = mensajeByte[i-tamanioCodop-OCHO];
			}
		}
	}

	/**
	 * 
	 */
	public void run(){
		int TAMANIO_SOLICITUD = 2000;
		int OCHO = 8;
		imprimeln("Proceso cliente en ejecucion.");
		imprimeln("Esperando datos para continuar.");
		imprimeln("Inicio de proceso");
		while(true) {//mio
			Nucleo.suspenderProceso();
			String codop = ClienteFrame.codigosOperacion.getSelectedItem();//mio
			String mensaje = ClienteFrame.campoMensaje.getText();//mio
			int longitudCodop = codop.length();//mio
			int longitudCadena = mensaje.length();//mio
			int idOrigen = ClienteFrame.proc.dameID();//mio
			int idDestino = ServidorFrame.proc.dameID();//mio
			byte[] solCliente=new byte[OCHO+longitudCodop+longitudCadena];
			byte[] respCliente=new byte[TAMANIO_SOLICITUD];
//			solCliente[0]=(byte)10;
			encadenadorDeByte(solCliente,idOrigen,idDestino, longitudCadena,mensaje,longitudCodop,codop);//mio
			System.out.println("<<<<<<"+Arrays.toString(solCliente));//mio
			imprimeln("Solicitud de envío de mensaje");
			//Nucleo.send(idDestino,solCliente);
			Nucleo.send(248,solCliente);
			imprimeln("Invocando a receive()");
			Pausador.pausa(1000);  //sin esta línea es posible que Servidor solicite send antes que Cliente solicite receive
			//Nucleo.receive(idOrigen,respCliente);
			Nucleo.receive(dameID(),respCliente);
			try {//mio
				System.out.println("CONI__"+new String(respCliente, "UTF-8").substring(0,30)+"_mensaje");//mio
				imprimeln("el servidor me envia un "+new String(respCliente, "UTF-8").substring(0,30));
			} catch (UnsupportedEncodingException e) {//mio
				e.printStackTrace();//mio
			}//mio
		}//mio
	}
}