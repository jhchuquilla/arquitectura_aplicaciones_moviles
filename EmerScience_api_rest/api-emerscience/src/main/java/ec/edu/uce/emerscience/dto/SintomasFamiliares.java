package ec.edu.uce.emerscience.dto;

import java.io.Serializable;

public class SintomasFamiliares implements Serializable{

	private static final long serialVersionUID = 1L;
	private String cedulaFam;
	private String sintoma;

	public SintomasFamiliares() {
		// TODO Auto-generated constructor stub
	}

	public String getCedulaFam() {
		return cedulaFam;
	}

	public void setCedulaFam(String cedulaFam) {
		this.cedulaFam = cedulaFam;
	}

	public String getSintoma() {
		return sintoma;
	}

	public void setSintoma(String sintoma) {
		this.sintoma = sintoma;
	}
	
}
