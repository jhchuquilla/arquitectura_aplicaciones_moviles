package ec.edu.uce.emerscience.dto;

import java.io.Serializable;

public class Direccion implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private String cedulaEstudiante;
    private String codigoPostal;
    private String parroquia;
    private String canton;
    private String provincia;
    private String pais;
    private String alguienSintomas;
    private String estudianteSintomas;

    public String getCedulaEstudiante() {
        return cedulaEstudiante;
    }

    public void setCedulaEstudiante(String cedulaEstudiante) {
        this.cedulaEstudiante = cedulaEstudiante;
    }

    public String getCodigoPostal() {
        return codigoPostal;
    }

    public void setCodigoPostal(String codigoPostal) {
        this.codigoPostal = codigoPostal;
    }

    public String getParroquia() {
        return parroquia;
    }

    public void setParroquia(String parroquia) {
        this.parroquia = parroquia;
    }

    public String getCanton() {
        return canton;
    }

    public void setCanton(String canton) {
        this.canton = canton;
    }

    public String getProvincia() {
        return provincia;
    }

    public void setProvincia(String provincia) {
        this.provincia = provincia;
    }

    public String getPais() {
        return pais;
    }

    public void setPais(String pais) {
        this.pais = pais;
    }

    public String getAlguienSintomas() {
        return alguienSintomas;
    }

    public void setAlguienSintomas(String alguienSintomas) {
        this.alguienSintomas = alguienSintomas;
    }

    public String getEstudianteSintomas() {
        return estudianteSintomas;
    }

    public void setEstudianteSintomas(String estudianteSintomas) {
        this.estudianteSintomas = estudianteSintomas;
    }

    @Override
    public String toString() {
        return "Direccion{" +
                "cedulaEstudiante='" + cedulaEstudiante + '\'' +
                ", codigoPostal='" + codigoPostal + '\'' +
                ", parroquia='" + parroquia + '\'' +
                ", canton='" + canton + '\'' +
                ", provincia='" + provincia + '\'' +
                ", pais='" + pais + '\'' +
                ", alguienSintomas='" + alguienSintomas + '\'' +
                ", estudianteSintomas='" + estudianteSintomas + '\'' +
                '}';
    }

}
