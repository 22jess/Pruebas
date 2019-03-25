/* 	Sistemas Concurrentes y Distribuidos
	Algoritmos de Sincronización de Relojes - Cristian
	CeroCero
*/
package sistemaDistribuido.sistema.sincronizacionCristian;

public interface ProcesadorMensajeRed {
	public void procesarMensaje(byte[] buffer, String ip);
	
}
