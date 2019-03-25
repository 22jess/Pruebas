

package sistemaDistribuido.sistema.clienteServidor.modoUsuario.ceroCero;

public class Mensaje {
	public static final int MAX_TAM_BUFFER = 1024;
	
	
	private byte buffer[];
	private int posBuffer;
	
	public Mensaje(byte[] buffer){
		this.buffer = buffer;
		posBuffer = 8;
	}
	
	public Mensaje(int bytesParaDatos){
		if(bytesParaDatos <= MAX_TAM_BUFFER){
			buffer = new byte[bytesParaDatos];
		}
		posBuffer = 8;
	}
	
	public int dameIdEmisor(){
		return UtilRed.toInt(buffer,0);
	}
	
	public int dameIdReceptor(){
		return UtilRed.toInt(buffer,4);
	}
	
	public CodigoOperacion dameCodOp(){
		return CodigoOperacion.dameTipoMensaje(UtilRed.toShort(buffer,8));
	}
	
	public void agregarDatos(byte[] bytes){
		int n;
		n = bytes.length;
		System.arraycopy(bytes, 0, buffer, posBuffer,n);
		posBuffer+=n;
	}
	
	public void agregarDatos(byte b){
		buffer[posBuffer++] = b;
	}

	public byte[] dameBuffer() {
		return buffer;
	}
	
	public int damePosBuffer(){
		return posBuffer;
	}
	
	byte[] dameBytes(int offset, int numBytes){
		byte[] bytes = new byte[numBytes];
		
		System.arraycopy(buffer, offset, bytes, 0, numBytes);
		
		return bytes;
	}
	
	public boolean fijaCodigoOperacion(CodigoOperacion c){
		short s;
		switch(c){
		case CREAR_ARCHIVO:   	s = 0;		break;
		case ELIMINAR_ARCHIVO:	s = 1;		break;
		case LEER_ARCHIVO:		s = 2;		break;
		case ESCRIBIR_ARCHIVO:	s = 3;		break;
		
		default:				return false;
		}
		System.arraycopy(UtilRed.toBytes(s), 0, buffer, 8, 2);
		posBuffer+=2;
		return true;
	}
	
	byte dameValorByte(int pos){
		return buffer[pos];
	}

	public void fijaIdEmisor(int id) {
		System.arraycopy(UtilRed.toBytes(id), 0, buffer, 0, 4);
		
	}
	
	public void fijaIdReceptor(int id) {
		System.arraycopy(UtilRed.toBytes(id), 0, buffer, 4, 4);
	}
	
	
}
