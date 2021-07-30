package ec.edu.uce.emerscience.conexionldap;

import ec.edu.uce.emerscience.dto.Estudiante;

public interface ConexionLdapI {
	
	public boolean verificarLoginUsuario(String ldapUsuario, String ldapPassword);

	Estudiante obtenerAtributos(String ldapUsuario, String ldapPassword);

}
