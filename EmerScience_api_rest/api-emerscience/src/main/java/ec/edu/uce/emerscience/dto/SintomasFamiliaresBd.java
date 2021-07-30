package ec.edu.uce.emerscience.dto;

import java.io.Serializable;

public class SintomasFamiliaresBd implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private String cedulaFamiliar;
	private int codigoSintoma;

	public SintomasFamiliaresBd() {
		// TODO Auto-generated constructor stub
	}
	
	public String getCedulaFamiliar() {
		return cedulaFamiliar;
	}

	public void setCedulaFamiliar(String cedulaFamiliar) {
		this.cedulaFamiliar = cedulaFamiliar;
	}

	public int getCodigoSintoma() {
		return codigoSintoma;
	}

	public void setCodigoSintoma(int codigoSintoma) {
		this.codigoSintoma = codigoSintoma;
	}
	
	
}
