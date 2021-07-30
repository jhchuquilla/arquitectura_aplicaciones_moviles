package ec.edu.uce.emerscience.dto;

import java.io.Serializable;

public class SintomaRespiratorio implements Serializable{

	private static final long serialVersionUID = 1L;
	private String cedulaFam;
    private String sintomaRespiratorio;

    public SintomaRespiratorio() {
		// TODO Auto-generated constructor stub
	}

    public String getCedulaFam() {
        return cedulaFam;
    }

    public void setCedulaFam(String cedulaFam) {
        this.cedulaFam = cedulaFam;
    }

    public String getSintomaRespiratorio() {
        return sintomaRespiratorio;
    }

    public void setSintomaRespiratorio(String sintomaRespiratorio) {
        this.sintomaRespiratorio = sintomaRespiratorio;
    }
	
}
