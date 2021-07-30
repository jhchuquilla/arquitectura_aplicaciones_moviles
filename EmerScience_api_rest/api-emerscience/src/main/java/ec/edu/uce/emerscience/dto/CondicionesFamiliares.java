package ec.edu.uce.emerscience.dto;

import java.io.Serializable;

public class CondicionesFamiliares implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private String cedulaFamiliar;
	private int codigoCondicion;
	
	public CondicionesFamiliares() {
		// TODO Auto-generated constructor stub
	}
	public String getCedulaFamiliar() {
		return cedulaFamiliar;
	}

	public void setCedulaFamiliar(String cedulaFamiliar) {
		this.cedulaFamiliar = cedulaFamiliar;
	}

	public int getCodigoCondicion() {
		return codigoCondicion;
	}

	public void setCodigoCondicion(int codigoCondicion) {
		this.codigoCondicion = codigoCondicion;
	}
	
	

}
