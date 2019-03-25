package sistemaDistribuido.sistema.exclusionMutuaCentralizado;


public class IdCliente {
	private String ip;
	private int idRegionCritica;
	
	public IdCliente(String ip){
		this.ip = ip;
	}
	

	public int dameIdRegionCritica() {
		return idRegionCritica;
	}
	
	public String dameIp() {
		return ip;
	}
	
	public boolean fijaIdRegionCritica(int id){
		idRegionCritica = id;
		return true;
	}
	
	public String toString(){
		return "Ip: "+ ip + "\t idRegion: " + idRegionCritica;
	}
	
	
	public boolean equals(Object o){
		IdCliente i;
		if(o instanceof IdCliente){
			i = (IdCliente)o;
			return i.dameIp().equals(ip) && i.dameIdRegionCritica() == idRegionCritica;
		}
		return false; 
	}
	
}
