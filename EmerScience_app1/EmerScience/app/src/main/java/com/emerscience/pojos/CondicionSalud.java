package com.emerscience.pojos;

public class CondicionSalud {

    private String cedulaFam;
    private String condicionSalud;

    public CondicionSalud(String cedulaFam, String condicionSalud) {
        this.cedulaFam = cedulaFam;
        this.condicionSalud = condicionSalud;
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
