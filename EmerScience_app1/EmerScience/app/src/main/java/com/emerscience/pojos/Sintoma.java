package com.emerscience.pojos;

public class Sintoma {
    private String cedulaFam;
    private String sintoma;

    public Sintoma(String cedulaFam, String sintoma) {
        this.cedulaFam = cedulaFam;
        this.sintoma = sintoma;
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
