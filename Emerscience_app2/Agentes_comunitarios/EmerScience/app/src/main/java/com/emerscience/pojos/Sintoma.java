package com.emerscience.pojos;

import java.io.Serializable;

public class Sintoma implements Serializable {
    private String cedulaFam;
    private String sintoma;

    public Sintoma(String cedulaFam, String sintoma) {
        this.cedulaFam = cedulaFam;
        this.sintoma = sintoma;
    }

    public Sintoma() {
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
