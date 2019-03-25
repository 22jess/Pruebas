/* 
	Actividad 2: Integración de Clientes y Servidores
	   
	CuatroUno
	CeroCero
	SeisDos
	
*/
package sistemaDistribuido.visual.clienteServidor;

public class Integrante{
	private String nickname;
	private int idServidor;
	
	
	public Integrante(String nickname, int idServidor){
		this.nickname = nickname;
		this.idServidor = idServidor;
	}
	
	public String dameNickname(){
		return nickname;
	}
	
	public int dameIdServidor(){
		return idServidor;
	}
}
