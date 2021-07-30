package com.emerscience.seguimiento.pojos;

import java.io.Serializable;

public class Familiar implements Serializable {

    private String apellidosFam;
    private String nombresFam;
    private String cedulaFam;
    private String cedulaEstudiante;
    private int edadFam;
    private String sexoFam;
    private String sintomasFam;
    private String difRespirarSevera;
    private String condicionesFam;
    private String contactoFamConPersonaCovid;

    public Familiar() {
    }

    public Familiar(String apellidosFam, String nombresFam, String cedulaFam, String cedulaEstudiante, int edadFam, String sexoFam, String sintomasFam, String difRespirarSevera, String condicionesFam, String contactoFamConPersonaCovid) {
        this.apellidosFam = apellidosFam;
        this.nombresFam = nombresFam;
        this.cedulaFam = cedulaFam;
        this.cedulaEstudiante = cedulaEstudiante;
        this.edadFam = edadFam;
        this.sexoFam = sexoFam;
        this.sintomasFam = sintomasFam;
        this.difRespirarSevera = difRespirarSevera;
        this.condicionesFam = condicionesFam;
        this.contactoFamConPersonaCovid = contactoFamConPersonaCovid;
    }

    public String getApellidosFam() {
        return apellidosFam;
    }

    public void setApellidosFam(String apellidosFam) {
        this.apellidosFam = apellidosFam;
    }

    public String getNombresFam() {
        return nombresFam;
    }

    public void setNombresFam(String nombresFam) {
        this.nombresFam = nombresFam;
    }

    public String getCedulaFam() {
        return cedulaFam;
    }

    public void setCedulaFam(String cedulaFam) {
        this.cedulaFam = cedulaFam;
    }

    public String getCedulaEstudiante() {
        return cedulaEstudiante;
    }

    public void setCedulaEstudiante(String cedulaEstudiante) {
        this.cedulaEstudiante = cedulaEstudiante;
    }

    public int getEdadFam() {
        return edadFam;
    }

    public void setEdadFam(int edadFam) {
        this.edadFam = edadFam;
    }

    public String getSexoFam() {
        return sexoFam;
    }

    public void setSexoFam(String sexoFam) {
        this.sexoFam = sexoFam;
    }

    public String getSintomasFam() {
        return sintomasFam;
    }

    public void setSintomasFam(String sintomasFam) {
        this.sintomasFam = sintomasFam;
    }

    public String getDifRespirarSevera() {
        return difRespirarSevera;
    }

    public void setDifRespirarSevera(String difRespirarSevera) {
        this.difRespirarSevera = difRespirarSevera;
    }

    public String getCondicionesFam() {
        return condicionesFam;
    }

    public void setCondicionesFam(String condicionesFam) {
        this.condicionesFam = condicionesFam;
    }

    public String getContactoFamConPersonaCovid() {
        return contactoFamConPersonaCovid;
    }

    public void setContactoFamConPersonaCovid(String contactoFamConPersonaCovid) {
        this.contactoFamConPersonaCovid = contactoFamConPersonaCovid;
    }
}
