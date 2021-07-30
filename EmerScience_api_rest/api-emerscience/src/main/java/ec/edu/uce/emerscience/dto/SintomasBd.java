package ec.edu.uce.emerscience.dto;

import java.io.Serializable;

public class SintomasBd implements Serializable {

	private static final long serialVersionUID = 1L;

	private int codigoSintoma;
	private String nombreSintoma;

	public SintomasBd() {
		// TODO Auto-generated constructor stub
	}
	
	public int getCodigoSintoma() {
		return codigoSintoma;
	}

	public void setCodigoSintoma(int codigoSintoma) {
		this.codigoSintoma = codigoSintoma;
	}

	public String getNombreSintoma() {
		return nombreSintoma;
	}

	public void setNombreSintoma(String nombreSintoma) {
		this.nombreSintoma = nombreSintoma;
	}

}
