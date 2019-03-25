package sistemaDistribuido.sistema.exclusionMutuaCentralizado;



public interface ProcesadorMensaje {
	public void procesarMensaje(Mensaje mensaje,String ip );
}
