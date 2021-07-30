package com.emerscience.seguimiento.pojos;

import java.io.Serializable;

public class Estudiante implements Serializable {

    private String apellidos;
    private String nombres;
    private String cedula;
    private int edad;
    private String sexo;
    private String celular;
    private String telFijo;
    private String correo;
    private int numPersonasHogar;
    private String alguienPresentaSintomas;
    private String estudiantePresentaSintomas;
    private int cantidadPersonasSintomas;
    private String contactoPersonaConCovid;
    private String lng;
    private String lat;
    private String fechaRegistroPersona;
    private String fechaModificacion;
    private String usuario;
    private int cantidadDormitorios;
    private int cantidadPersonasTrabajan;
    private int cantidadPersonasEmpleoFijo;
    private String propiedadVivienda;
    private String banioCompartido;
    private String pagoArriendoServiciosBasicos;
    private String problemasCompraComida;

    private boolean usuarioAD;

    public Estudiante(String fechaRegistroPersona, String fechaModificacion, String lng, String lat
            , String apellidos, String nombres, String cedula, int edad, String sexo, String celular
            , String telFijo, String correo, int numPersonasHogar, String alguienPresentaSintomas
            , String estudiantePresentaSintomas, int cantidadPersonasSintomas, String contactoPersonaConCovid
            , String usuario, int cantidadDormitorios, int cantidadPersonasTrabajan, int cantidadPersonasEmpleoFijo
            , String propiedadVivienda, String banioCompartido, String pagoArriendoServiciosBasicos
            , String problemasCompraComida) {
        this.fechaRegistroPersona = fechaRegistroPersona;
        this.fechaModificacion = fechaModificacion;
        this.lng = lng;
        this.lat = lat;
        this.apellidos = apellidos;
        this.nombres = nombres;
        this.cedula = cedula;
        this.edad = edad;
        this.sexo = sexo;
        this.celular = celular;
        this.telFijo = telFijo;
        this.correo = correo;
        this.numPersonasHogar = numPersonasHogar;
        this.alguienPresentaSintomas = alguienPresentaSintomas;
        this.estudiantePresentaSintomas = estudiantePresentaSintomas;
        this.cantidadPersonasSintomas = cantidadPersonasSintomas;
        this.contactoPersonaConCovid = contactoPersonaConCovid;
        this.usuario = usuario;
        this.cantidadDormitorios = cantidadDormitorios;
        this.cantidadPersonasTrabajan = cantidadPersonasTrabajan;
        this.cantidadPersonasEmpleoFijo = cantidadPersonasEmpleoFijo;
        this.propiedadVivienda = propiedadVivienda;
        this.banioCompartido = banioCompartido;
        this.pagoArriendoServiciosBasicos = pagoArriendoServiciosBasicos;
        this.problemasCompraComida = problemasCompraComida;
    }

    public Estudiante(){

    }

    public String getFechaRegistroPersona() { return fechaRegistroPersona; }

    public void setFechaRegistroPersona(String fechaRegistroPersona) { this.fechaRegistroPersona = fechaRegistroPersona; }

    public String getFechaModificacion() {
        return fechaModificacion;
    }

    public void setFechaModificacion(String fechaModificacion) {
        this.fechaModificacion = fechaModificacion;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public String getNombres() {
        return nombres;
    }

    public void setNombres(String nombres) {
        this.nombres = nombres;
    }

    public String getCedula() {
        return cedula;
    }

    public void setCedula(String cedula) {
        this.cedula = cedula;
    }

    public int getEdad() {
        return edad;
    }

    public void setEdad(int edad) {
        this.edad = edad;
    }

    public String getSexo() {
        return sexo;
    }

    public void setSexo(String sexo) {
        this.sexo = sexo;
    }

    public String getCelular() {
        return celular;
    }

    public void setCelular(String celular) {
        this.celular = celular;
    }

    public String getTelFijo() {
        return telFijo;
    }

    public void setTelFijo(String telFijo) {
        this.telFijo = telFijo;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public int getNumPersonasHogar() {
        return numPersonasHogar;
    }

    public void setNumPersonasHogar(int numPersonasHogar) {
        this.numPersonasHogar = numPersonasHogar;
    }

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getAlguienPresentaSintomas() {
        return alguienPresentaSintomas;
    }

    public void setAlguienPresentaSintomas(String alguienPresentaSintomas) {
        this.alguienPresentaSintomas = alguienPresentaSintomas;
    }

    public String getEstudiantePresentaSintomas() {
        return estudiantePresentaSintomas;
    }

    public void setEstudiantePresentaSintomas(String estudiantePresentaSintomas) {
        this.estudiantePresentaSintomas = estudiantePresentaSintomas;
    }

    public int getCantidadPersonasSintomas() {
        return cantidadPersonasSintomas;
    }

    public void setCantidadPersonasSintomas(int cantidadPersonasSintomas) {
        this.cantidadPersonasSintomas = cantidadPersonasSintomas;
    }

    public String getContactoPersonaConCovid() {
        return contactoPersonaConCovid;
    }

    public void setContactoPersonaConCovid(String contactoPersonaConCovid) {
        this.contactoPersonaConCovid = contactoPersonaConCovid;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public int getCantidadDormitorios() {
        return cantidadDormitorios;
    }

    public void setCantidadDormitorios(int cantidadDormitorios) {
        this.cantidadDormitorios = cantidadDormitorios;
    }

    public int getCantidadPersonasTrabajan() {
        return cantidadPersonasTrabajan;
    }

    public void setCantidadPersonasTrabajan(int cantidadPersonasTrabajan) {
        this.cantidadPersonasTrabajan = cantidadPersonasTrabajan;
    }

    public int getCantidadPersonasEmpleoFijo() {
        return cantidadPersonasEmpleoFijo;
    }

    public void setCantidadPersonasEmpleoFijo(int cantidadPersonasEmpleoFijo) {
        this.cantidadPersonasEmpleoFijo = cantidadPersonasEmpleoFijo;
    }

    public String getPropiedadVivienda() {
        return propiedadVivienda;
    }

    public void setPropiedadVivienda(String propiedadVivienda) {
        this.propiedadVivienda = propiedadVivienda;
    }

    public String getBanioCompartido() {
        return banioCompartido;
    }

    public void setBanioCompartido(String banioCompartido) {
        this.banioCompartido = banioCompartido;
    }

    public String getPagoArriendoServiciosBasicos() {
        return pagoArriendoServiciosBasicos;
    }

    public void setPagoArriendoServiciosBasicos(String pagoArriendoServiciosBasicos) {
        this.pagoArriendoServiciosBasicos = pagoArriendoServiciosBasicos;
    }

    public String getProblemasCompraComida() {
        return problemasCompraComida;
    }

    public void setProblemasCompraComida(String problemasCompraComida) {
        this.problemasCompraComida = problemasCompraComida;
    }

    public boolean isUsuarioAD() {
        return usuarioAD;
    }

    public void setUsuarioAD(boolean usuarioAD) {
        this.usuarioAD = usuarioAD;
    }

    @Override
    public String toString() {
        return "Persona{" +
                "apellidos='" + apellidos + '\'' +
                ", nombres='" + nombres + '\'' +
                ", cedula='" + cedula + '\'' +
                ", edad=" + edad +
                ", sexo='" + sexo + '\'' +
                ", ceulular='" + celular + '\'' +
                ", telFijo='" + telFijo + '\'' +
                ", correo='" + correo + '\'' +
                ", numPersonasHogar=" + numPersonasHogar +
                ", alguienPresentaSintomas='" + alguienPresentaSintomas + '\'' +
                ", cantidadPersonasSintomas=" + cantidadPersonasSintomas +
                ", contactoPersonaConCovid='" + contactoPersonaConCovid + '\'' +
                '}';
    }
}
