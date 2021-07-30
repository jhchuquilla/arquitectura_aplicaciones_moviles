package ec.edu.uce.emerscience.dto;

import java.io.Serializable;

public class Coordenada implements Serializable {

	private static final long serialVersionUID = 1L;

	private String nombreEstudiante;
	private String apellidoEstudiante;
	private String cedulaEstudiante;
	private String celular;
	private Double latitud;
	private Double longitud;
	private String alguienSintomas;
	private String estudianteSintomas;
	private int personasEnHogar;
	private int personasConSintomas;

	public String getNombreEstudiante() {
		return nombreEstudiante;
	}

	public void setNombreEstudiante(String nombreEstudiante) {
		this.nombreEstudiante = nombreEstudiante;
	}

	public String getApellidoEstudiante() {
		return apellidoEstudiante;
	}

	public void setApellidoEstudiante(String apellidoEstudiante) {
		this.apellidoEstudiante = apellidoEstudiante;
	}

	public String getCedulaEstudiante() {
		return cedulaEstudiante;
	}

	public void setCedulaEstudiante(String cedulaEstudiante) {
		this.cedulaEstudiante = cedulaEstudiante;
	}

	public String getCelular() {
		return celular;
	}

	public void setCelular(String celular) {
		this.celular = celular;
	}

	public Double getLatitud() {
		return latitud;
	}

	public void setLatitud(Double latitud) {
		this.latitud = latitud;
	}

	public Double getLongitud() {
		return longitud;
	}

	public void setLongitud(Double longitud) {
		this.longitud = longitud;
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

	public int getPersonasEnHogar() {
		return personasEnHogar;
	}

	public void setPersonasEnHogar(int personasEnHogar) {
		this.personasEnHogar = personasEnHogar;
	}

	public int getPersonasConSintomas() {
		return personasConSintomas;
	}

	public void setPersonasConSintomas(int personasConSintomas) {
		this.personasConSintomas = personasConSintomas;
	}

	@Override
	public String toString() {
		return "Coordenada [nombreEstudiante=" + nombreEstudiante + ", apellidoEstudiante=" + apellidoEstudiante
				+ ", cedulaEstudiante=" + cedulaEstudiante + ", celular=" + celular + ", latitud=" + latitud
				+ ", longitud=" + longitud + ", alguienSintomas=" + alguienSintomas + ", estudianteSintomas="
				+ estudianteSintomas + ", personasEnHogar=" + personasEnHogar + ", personasConSintomas="
				+ personasConSintomas + "]";
	}

}
