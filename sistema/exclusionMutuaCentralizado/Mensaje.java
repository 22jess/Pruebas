package sistemaDistribuido.sistema.exclusionMutuaCentralizado;


public abstract class Mensaje {
	public abstract int dameIdRegionCritica();
	public abstract int dameCodOperacion();
	public abstract boolean fijaIdRegionCritica(int id);
	public abstract boolean fijaCodOperacion(int codOp);
	public abstract byte[] dameContenido();
}
