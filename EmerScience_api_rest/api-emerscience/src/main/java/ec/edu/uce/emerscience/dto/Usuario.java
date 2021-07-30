package ec.edu.uce.emerscience.dto;

import java.io.Serializable;

public class Usuario implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private int id;
	private String username;
	private String password;
	private String rol;
	private boolean usuaioLdap;
	private boolean usuarioActivo;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getRol() {
		return rol;
	}

	public void setRol(String rol) {
		this.rol = rol;
	}

	public boolean isUsuaioLdap() {
		return usuaioLdap;
	}

	public void setUsuaioLdap(boolean usuaioLdap) {
		this.usuaioLdap = usuaioLdap;
	}

	public boolean isUsuarioActivo() {
		return usuarioActivo;
	}

	public void setUsuarioActivo(boolean usuarioActivo) {
		this.usuarioActivo = usuarioActivo;
	}
	
}
