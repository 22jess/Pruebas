package sistemaDistribuido.sistema.clienteServidor.modoUsuario.ceroCero;

public enum CodigoOperacion {
	CREAR_ARCHIVO,
	ELIMINAR_ARCHIVO,
	LEER_ARCHIVO,
	ESCRIBIR_ARCHIVO;
	
	public static CodigoOperacion dameTipoMensaje(short s){
		switch(s){
		case 0:	return CREAR_ARCHIVO;
		case 1: return ELIMINAR_ARCHIVO;
		case 2: return LEER_ARCHIVO;
		case 3: return ESCRIBIR_ARCHIVO;
		default:return null; 
		}
	}
}
