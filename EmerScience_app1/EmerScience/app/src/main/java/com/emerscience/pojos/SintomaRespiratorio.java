package com.emerscience.pojos;

public class SintomaRespiratorio {
    private String cedulaFam;
    private String sintomaRespiratorio;

    public SintomaRespiratorio(String cedulaFam, String sintomaRespiratorio) {
        this.cedulaFam = cedulaFam;
        this.sintomaRespiratorio = sintomaRespiratorio;
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
