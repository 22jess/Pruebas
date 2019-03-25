
package sistemaDistribuido.visual.clienteServidor.ceroCero;

public enum OperacionArchivo {
	CREAR,
	ELIMINAR,
	LEER,
	ESCRIBIR;
	
	public OperacionArchivo castOperacion(String s){
		switch(s){
		case "CREAR":		return CREAR;
		case "ELIMINAR":	return ELIMINAR;
		case "LEER":		return LEER;
		case "ESCRIBIR":	return ESCRIBIR;
		default: 			return null;
		}
	}
	
}
