package ec.edu.uce.emerscience.serviciosrest;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import ec.edu.uce.emerscience.conexionldap.ConexionLdap;
import ec.edu.uce.emerscience.dto.Estudiante;
import ec.edu.uce.emerscience.dto.Usuario;
import ec.edu.uce.emerscience.servicios.ServicioUsuario;
import ec.edu.uce.emerscience.utils.UtilJwt;

@ApplicationScoped
@Path("login")
public class RestLogin {

	private ConexionLdap conexion = new ConexionLdap("uce.edu.ec", "389", "DC=uce,DC=edu,DC=ec");
	
	@Inject private ServicioUsuario servicioUser;
	
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response login(Usuario usuario) {
		boolean verificado = conexion.verificarLoginUsuario(usuario.getUsername(), usuario.getPassword());
		Estudiante estudiante = new Estudiante();
		String token;
		try {
			if (verificado) {
				token = UtilJwt.generarToken(usuario.getUsername());
				estudiante = conexion.obtenerAtributos(usuario.getUsername(), usuario.getPassword());
				estudiante.setUsuarioAD(true);
				return Response.ok(estudiante).header(HttpHeaders.AUTHORIZATION, "Bearer " + token).build();
			}else {
				Usuario us = servicioUser.loginUsuario(usuario.getUsername(), usuario.getPassword());
				boolean correcto = us != null;
				if (correcto) {
					if (us.isUsuarioActivo()) {
						token = UtilJwt.generarToken(usuario.getUsername());
						estudiante.setNombres("");
						estudiante.setApellidos("");
						estudiante.setCedula("");
						estudiante.setCorreo("");
						estudiante.setUsuarioAD(false);
						return Response.ok(estudiante).header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
										.header("Rol", us.getRol()).build();
					}else {
						return Response.status(Response.Status.UNAUTHORIZED).build();
					}
				}
				return Response.status(Response.Status.UNAUTHORIZED).build();
			}
		} catch (Exception e) {
			System.out.println("EXCEPTION IN LOGIN:" + e.getMessage());
			e.printStackTrace();
			return Response.status(Response.Status.UNAUTHORIZED).build();
		}
	}
	
	@POST
	@Path("/ldap")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response loginUsuariosLdap(Usuario usuario) {
		if (usuario != null) {
			Estudiante estudiante = new Estudiante();
			String token;
			Usuario usAux = servicioUser.loginUsuariosLdap(usuario.getUsername());
			boolean correcto = usAux != null;
			if (correcto) {
				if (usAux.isUsuarioActivo()) {
					token = UtilJwt.generarToken(usuario.getUsername());
					estudiante.setNombres("");
					estudiante.setApellidos("");
					estudiante.setCedula("");
					estudiante.setCorreo("");
					estudiante.setUsuarioAD(true);
					return Response.ok(estudiante).header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
									.header("Rol", usAux.getRol()).build();
				}else {
					return Response.status(Response.Status.UNAUTHORIZED).build();
				}
			}
			return Response.status(Response.Status.UNAUTHORIZED).build();
		}
		return Response.status(Response.Status.UNAUTHORIZED).build();
	}
	
}
