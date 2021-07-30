package ec.edu.uce.emerscience.dto;

import java.io.Serializable;

public class Familiar implements Serializable {

	private static final long serialVersionUID = 1L;

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

	/**
	 * @return the apellidosFam
	 */
	public String getApellidosFam() {
		return apellidosFam;
	}

	/**
	 * @param apellidosFam the apellidosFam to set
	 */
	public void setApellidosFam(String apellidosFam) {
		this.apellidosFam = apellidosFam;
	}

	/**
	 * @return the nombresFam
	 */
	public String getNombresFam() {
		return nombresFam;
	}

	/**
	 * @param nombresFam the nombresFam to set
	 */
	public void setNombresFam(String nombresFam) {
		this.nombresFam = nombresFam;
	}

	/**
	 * @return the cedulaFam
	 */
	public String getCedulaFam() {
		return cedulaFam;
	}

	/**
	 * @param cedulaFam the cedulaFam to set
	 */
	public void setCedulaFam(String cedulaFam) {
		this.cedulaFam = cedulaFam;
	}

	/**
	 * @return the cedulaEstudiante
	 */
	public String getCedulaEstudiante() {
		return cedulaEstudiante;
	}

	/**
	 * @param cedulaEstudiante the cedulaEstudiante to set
	 */
	public void setCedulaEstudiante(String cedulaEstudiante) {
		this.cedulaEstudiante = cedulaEstudiante;
	}

	/**
	 * @return the edadFam
	 */
	public int getEdadFam() {
		return edadFam;
	}

	/**
	 * @param edadFam the edadFam to set
	 */
	public void setEdadFam(int edadFam) {
		this.edadFam = edadFam;
	}

	/**
	 * @return the sexoFam
	 */
	public String getSexoFam() {
		return sexoFam;
	}

	/**
	 * @param sexoFam the sexoFam to set
	 */
	public void setSexoFam(String sexoFam) {
		this.sexoFam = sexoFam;
	}

	/**
	 * @return the sintomasFam
	 */
	public String getSintomasFam() {
		return sintomasFam;
	}

	/**
	 * @param sintomasFam the sintomasFam to set
	 */
	public void setSintomasFam(String sintomasFam) {
		this.sintomasFam = sintomasFam;
	}

	/**
	 * @return the difRespirarSevera
	 */
	public String getDifRespirarSevera() {
		return difRespirarSevera;
	}

	/**
	 * @param difRespirarSevera the difRespirarSevera to set
	 */
	public void setDifRespirarSevera(String difRespirarSevera) {
		this.difRespirarSevera = difRespirarSevera;
	}

	/**
	 * @return the condicionesFam
	 */
	public String getCondicionesFam() {
		return condicionesFam;
	}

	/**
	 * @param condicionesFam the condicionesFam to set
	 */
	public void setCondicionesFam(String condicionesFam) {
		this.condicionesFam = condicionesFam;
	}

	/**
	 * @return the contactoFamConPersonaCovid
	 */
	public String getContactoFamConPersonaCovid() {
		return contactoFamConPersonaCovid;
	}

	/**
	 * @param contactoFamConPersonaCovid the contactoFamConPersonaCovid to set
	 */
	public void setContactoFamConPersonaCovid(String contactoFamConPersonaCovid) {
		this.contactoFamConPersonaCovid = contactoFamConPersonaCovid;
	}

}
