package sistemaDistribuido.visual.exclusionMutuaCentralizado;


import javax.swing.JScrollPane;
import javax.swing.JTextArea;

class AreaTextoConScroll extends JScrollPane{

	private static final long serialVersionUID = 1L;
	private JTextArea areaTexto;
	
	AreaTextoConScroll(){
		setViewportView(areaTexto = new JTextArea(20,10));
	}
	
	public void setEditable(boolean b){
		areaTexto.setEditable(b);
	}

	public void append(String s) {
		areaTexto.append(s);
	}

	public void setText(String s) {
		areaTexto.setText(s);
		
	}
}