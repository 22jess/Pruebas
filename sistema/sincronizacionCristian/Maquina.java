/* 	Sistemas Concurrentes y Distribuidos
	Algoritmos de Sincronización de Relojes - Cristian
	CeroCero
*/
package sistemaDistribuido.sistema.sincronizacionCristian;



public abstract class Maquina extends Thread implements Constantes,ConstantesRed{
	protected final float RO;
	protected final int INTERRUPCIONES_SEGUNDO;
	protected final int DISTORSION_DELTA;
	
	protected int propagacionInicial;
	protected Emisor emisor;
	protected Receptor receptor;
	protected long tiempoLocal;
	protected long n;
	protected Impresor impresor;
	protected long tiempoSincronizacion;
	
	

	public Maquina(int relojInicial, int delta, int interrupcionesSegundo, TipoReloj reloj,float ro,
			int propagacionInicial, Impresor impresor){
		
		RO = ro;
		n = 100;
		
		if(reloj == TipoReloj.LENTO){
			interrupcionesSegundo*= (1-RO);
		}
		else if(reloj == TipoReloj.RAPIDO){
			interrupcionesSegundo*= (1+RO);
		}
		INTERRUPCIONES_SEGUNDO = interrupcionesSegundo;
		
		DISTORSION_DELTA = delta;
		
		tiempoLocal = relojInicial;
		this.propagacionInicial = propagacionInicial;
		this.impresor = impresor;
		
		tiempoSincronizacion = (int)(DISTORSION_DELTA/(2*RO)); 
		tiempoSincronizacion -= tiempoSincronizacion*RO; // Considerarse como maquina lenta.
		impresor.imprimirLn("Tiempo correcto para sincronizar..." + tiempoSincronizacion);
	
	}
}