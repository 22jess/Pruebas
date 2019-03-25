/* 	Sistemas Concurrentes y Distribuidos
	Algoritmos de Sincronización de Relojes - Cristian
	CeroCero
*/
package sistemaDistribuido.sistema.sincronizacionCristian;


import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class UtilRed{
	
	private static ByteOrder orden = ByteOrder.BIG_ENDIAN;
	public final static String CODIFICACION = "ISO-8859-1";
	
	public static long toLong(byte[] bytes,int offset){
		return ByteBuffer.wrap(bytes,offset,Long.BYTES).order(orden).getLong();
	}
	
	public static long toLong(byte[] bytes){
		return ByteBuffer.wrap(bytes).order(orden).getLong();
	}
	
	public static int toInt(byte[] bytes,int offset){
		return ByteBuffer.wrap(bytes,offset,Integer.BYTES).order(orden).getInt();
	}
	
	public static int toInt(byte[] bytes){
		return ByteBuffer.wrap(bytes).order(orden).getInt();
	}
	
	public static short toShort(byte[] bytes){
		return ByteBuffer.wrap(bytes).order(orden).getShort();
	}

	public static short toShort(byte[] solicitud, int offset) {
		return ByteBuffer.wrap(solicitud,offset,Short.BYTES).order(orden).getShort();
	}
	
	public static String toString(byte[] bytes,int offset, int numBytes){
		byte[] bytesCadena = new byte[numBytes];
		System.arraycopy(bytes, offset, bytesCadena, 0, numBytes);
		
		try {
			return new String(bytesCadena,CODIFICACION);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static byte[] toBytes(int x){
		return ByteBuffer.allocate(Integer.BYTES).order(orden).putInt(x).array();
	}
	
	public static byte[] toBytes(short x){
		return ByteBuffer.allocate(Short.BYTES).order(orden).putShort(x).array();
	}
	
	public static byte[] toBytes(long x){
		return ByteBuffer.allocate(Long.BYTES).order(orden).putLong(x).array();
	}
}
