/* 	Sistemas Concurrentes y Distribuidos
	Algoritmos de Sincronización de Relojes - Cristian
	CeroCero
*/
package sistemaDistribuido.sistema.sincronizacionCristian;

public interface Impresor {
	void imprimir(String s);
	void imprimirLn(String s);
	void imprimirTiempoActualMaquina(long t);
	void imprimirN(long nTemporal);
}
