package ec.edu.uce.emerscience.dto;

import java.io.Serializable;

public class CondicionSaludBd implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private int codigoCondicion;
	private String nombreCondicion;

	public CondicionSaludBd() {
		// TODO Auto-generated constructor stub
	}
	
	public int getCodigoCondicion() {
		return codigoCondicion;
	}

	public void setCodigoCondicion(int codigoCondicion) {
		this.codigoCondicion = codigoCondicion;
	}

	public String getNombreCondicion() {
		return nombreCondicion;
	}

	public void setNombreCondicion(String nombreCondicion) {
		this.nombreCondicion = nombreCondicion;
	}
	
}
