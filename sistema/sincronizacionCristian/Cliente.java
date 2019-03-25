/* 	Sistemas Concurrentes y Distribuidos
	Algoritmos de Sincronización de Relojes - Cristian
	CeroCero
*/
package sistemaDistribuido.sistema.sincronizacionCristian;

import java.net.SocketException;



public class Cliente extends Maquina implements Runnable, ProcesadorMensajeRed{
	
	public static final int PUERTO_ENTRADA = 9901;
	public static final int PUERTO_SALIDA = 8922;
	
	private final int FACTOR_AJUSTE_TIEMPO = 4;
	
	private long tiempoCorrecto;
	private volatile boolean tiempoErroneo;
	private String ipServidor;
	private long t1;
	private long t0;
	private int propagacion;

	
	public Cliente(int tiempoInicial, int delta, int interrupcionesSegundo,TipoReloj reloj,float ro,
			int propagacionInicial, Impresor impresor, String ipServidor) {
		
		super(tiempoInicial, delta, interrupcionesSegundo, reloj, ro,propagacionInicial,impresor);
		this.ipServidor = ipServidor;
		
		try {
			emisor = new Emisor(PUERTO_SALIDA);
			receptor = new Receptor(PUERTO_ENTRADA,this,0);
			receptor.start();
		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void run(){
		int espera = MILISEGUNDOS_EN_SEGUNDO/INTERRUPCIONES_SEGUNDO;
		long nTemporal;
		int esperaExtra;
		int sincronizaciones,i;
		int cuentaAjustes = 0;
		int sigSincro = 1;
		
		propagacion = propagacionInicial;
		while(true){
			try {
				if(tiempoErroneo){
					sincronizaciones = (int) ((tiempoSincronizacion/FACTOR_AJUSTE_TIEMPO)/1000);
					sincronizaciones*=INTERRUPCIONES_SEGUNDO;
					nTemporal = (tiempoCorrecto-tiempoLocal)/sincronizaciones;
					impresor.imprimirLn("N vale: " + nTemporal);
					if(nTemporal < 0){ 
						esperaExtra = (int) (nTemporal*(-1));
						nTemporal = 0;
					}
					else{
						esperaExtra = 0;
					}
					for(i = 0 ; i < sincronizaciones; i++){            
						sleep(espera+esperaExtra);
						tiempoLocal+= nTemporal;
						impresor.imprimirTiempoActualMaquina(tiempoLocal);
					}
					tiempoErroneo = false;
				}
				else{
					sleep(espera);
					tiempoLocal+= n;
					if(cuentaAjustes > 0){
						cuentaAjustes--;
					}
					impresor.imprimirTiempoActualMaquina(tiempoLocal);
					if(tiempoLocal >= sigSincro*tiempoSincronizacion-propagacion){
						sigSincro++;
						sincronizarReloj();
					}
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	private void sincronizarReloj() {
		byte[] solicitud = new byte[1];
		
		synchronized(this){
			t0 = tiempoLocal;
		}
		solicitud[0] = SOLICITAR_HORA;
		impresor.imprimirLn("Se va a enviar un mensaje al servidor");
		emisor.enviarMensaje(solicitud, ipServidor, ServidorTiempo.PUERTO_ENTRADA);
	}

	@Override
	public void procesarMensaje(byte[] buffer, String ip) {
		long tiempoServidor;
		int valorIServ;
		
		impresor.imprimirLn("Se va a procesar un mensaje que se acaba de recibir");
		synchronized(this){
			t1 = tiempoLocal;
		}
	
		tiempoServidor = UtilRed.toLong(buffer, 0);
		valorIServ = UtilRed.toInt(buffer,8); 
		impresor.imprimirLn("Cliente recibe: ");
		impresor.imprimirLn("Tiempo Servidor: "+ tiempoServidor);
		impresor.imprimirLn("I Servidor: "+ valorIServ);
		impresor.imprimirLn("T0 = " + t0);
		impresor.imprimirLn("T1 = " + t1);
		propagacion = (int) ((t1-t0- valorIServ)/2);
		synchronized(this){
			tiempoCorrecto = tiempoServidor+propagacion;
			tiempoErroneo = true;
		}
	}

}
