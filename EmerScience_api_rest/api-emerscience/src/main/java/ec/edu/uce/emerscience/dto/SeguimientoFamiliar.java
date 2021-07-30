package ec.edu.uce.emerscience.dto;

import java.io.Serializable;

public class SeguimientoFamiliar implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private int id;
	private String cedulaFamiliar;
	private String fechaRegistroSeguimiento;
	private int frecRespiratoria;
	private double temperaturaCorporal;
	private int frecCardiaca;
	private int saturacionOxigeno;
	private String dificultadRespirar;
	private String fatiga;
	private String dificultadHablar;
	private String delirio;
	
	public SeguimientoFamiliar() {
		// TODO Auto-generated constructor stub
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getCedulaFamiliar() {
		return cedulaFamiliar;
	}

	public void setCedulaFamiliar(String cedulaFamiliar) {
		this.cedulaFamiliar = cedulaFamiliar;
	}

	public String getFechaRegistroSeguimiento() {
		return fechaRegistroSeguimiento;
	}

	public void setFechaRegistroSeguimiento(String fechaRegistroSeguimiento) {
		this.fechaRegistroSeguimiento = fechaRegistroSeguimiento;
	}

	public int getFrecRespiratoria() {
		return frecRespiratoria;
	}

	public void setFrecRespiratoria(int frecRespiratoria) {
		this.frecRespiratoria = frecRespiratoria;
	}

	public double getTemperaturaCorporal() {
		return temperaturaCorporal;
	}

	public void setTemperaturaCorporal(double temperaturaCorporal) {
		this.temperaturaCorporal = temperaturaCorporal;
	}

	public int getFrecCardiaca() {
		return frecCardiaca;
	}

	public void setFrecCardiaca(int frecCardiaca) {
		this.frecCardiaca = frecCardiaca;
	}

	public int getSaturacionOxigeno() {
		return saturacionOxigeno;
	}

	public void setSaturacionOxigeno(int saturacionOxigeno) {
		this.saturacionOxigeno = saturacionOxigeno;
	}

	public String getDificultadRespirar() {
		return dificultadRespirar;
	}

	public void setDificultadRespirar(String dificultadRespirar) {
		this.dificultadRespirar = dificultadRespirar;
	}

	public String getFatiga() {
		return fatiga;
	}

	public void setFatiga(String fatiga) {
		this.fatiga = fatiga;
	}

	public String getDificultadHablar() {
		return dificultadHablar;
	}

	public void setDificultadHablar(String dificultadHablar) {
		this.dificultadHablar = dificultadHablar;
	}

	public String getDelirio() {
		return delirio;
	}

	public void setDelirio(String delirio) {
		this.delirio = delirio;
	}

}
