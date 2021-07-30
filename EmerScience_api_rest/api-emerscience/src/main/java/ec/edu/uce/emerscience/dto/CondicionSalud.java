package ec.edu.uce.emerscience.dto;

import java.io.Serializable;

public class CondicionSalud implements Serializable{

	private static final long serialVersionUID = 1L;
	private String cedulaFam;
    private String condicionSalud;

    public CondicionSalud() {
        
    }

    public String getCedulaFam() {
        return cedulaFam;
    }

    public void setCedulaFam(String cedulaFam) {
        this.cedulaFam = cedulaFam;
    }

    public String getCondicionSalud() {
        return condicionSalud;
    }

    public void setCondicionSalud(String condicionSalud) {
        this.condicionSalud = condicionSalud;
    }
	
}
