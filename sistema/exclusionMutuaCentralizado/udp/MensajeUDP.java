package sistemaDistribuido.sistema.exclusionMutuaCentralizado.udp;
import sistemaDistribuido.sistema.exclusionMutuaCentralizado.Mensaje;



public class MensajeUDP extends Mensaje {
	
	private byte[] buffer; 

	public MensajeUDP(byte[] buff) {
		buffer = buff;
	}

	@Override
	public int dameIdRegionCritica() {
		return UtilRed.toInt(buffer,1);
	}

	@Override
	public int dameCodOperacion() {
		// TODO Auto-generated method stub
		 return buffer[0];
	}

	@Override
	public boolean fijaIdRegionCritica(int id) {
		// TODO Auto-generated method stub
		System.arraycopy(UtilRed.toBytes(id), 0, buffer, 1, 4);
		return true;
	}

	@Override
	public boolean fijaCodOperacion(int codOp) {
		// TODO Auto-generated method stub
		buffer[0] = (byte)codOp;
		return true;
	}

	@Override
	public byte[] dameContenido() {
		return buffer;
	}

}
