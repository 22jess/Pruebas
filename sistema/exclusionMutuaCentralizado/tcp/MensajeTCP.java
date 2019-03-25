package sistemaDistribuido.sistema.exclusionMutuaCentralizado.tcp;
import sistemaDistribuido.sistema.exclusionMutuaCentralizado.Mensaje;



public class MensajeTCP extends Mensaje {
	private static final String SEPARADOR_CAMPOS = "#";
	private String[] campos;
	
	private StringBuilder sB;
	
	public MensajeTCP(String s){
		this();
		sB.append(s);
	}
	
	public MensajeTCP(){
		sB = new StringBuilder();
	}
	

	@Override
	public int dameIdRegionCritica() {
		
		if(campos == null){
			campos = sB.toString().split(SEPARADOR_CAMPOS);
		}
		return Integer.parseInt(campos[1]);
	}

	@Override
	public int dameCodOperacion() {
		if(campos == null){
			campos = sB.toString().split(SEPARADOR_CAMPOS);
		}
		return Integer.parseInt(campos[0]);
	}

	@Override
	public boolean fijaIdRegionCritica(int id) {
		sB.append(id + SEPARADOR_CAMPOS);
		return true;
	}

	@Override
	public boolean fijaCodOperacion(int codOp) {
		sB.append(codOp + SEPARADOR_CAMPOS);
		return true;
	}

	@Override
	public byte[] dameContenido() {
		// TODO Auto-generated method stub
		return sB.toString().getBytes();
	}
	
	public String toString(){
		return sB.toString();
	}

}
