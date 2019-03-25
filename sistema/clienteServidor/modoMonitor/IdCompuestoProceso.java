/* 	Sistemas Concurrentes y Distribuidos
	Modelo Cliente/Servidor - Mecanismo de Comunicación Básico
	CeroCero
*/
package sistemaDistribuido.sistema.clienteServidor.modoMonitor;

public class IdCompuestoProceso implements ParMaquinaProceso{
	private String ip;
	private int id;
	
	public IdCompuestoProceso(String ip, int id){
		this.ip = ip;
		this.id = id;
	}

	public String dameIP() {
		return ip;
	}

	public int dameID() {
		return id;
	}
}
