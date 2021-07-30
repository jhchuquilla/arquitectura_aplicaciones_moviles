package ec.edu.uce.emerscience.serviciosrest;

import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import ec.edu.uce.emerscience.dto.Usuario;
import ec.edu.uce.emerscience.seguridad.Secured;
import ec.edu.uce.emerscience.servicios.ServicioEstudiante;
import ec.edu.uce.emerscience.servicios.ServicioUsuario;

@ApplicationScoped
@Path("/usuarios")
public class RestUsuarios {

	@Inject private ServicioUsuario servicioUsuarios;
	@Inject private ServicioEstudiante servicioEstudiantes;
	
	@GET
	@Secured
	@Path("/todos")
	@Produces(MediaType.APPLICATION_JSON)
	public List<Usuario> obtenerUsuarios(){
		return servicioUsuarios.buscarTodos();
	}
	
	@POST
	@Secured
	@Path("/nuevo")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public boolean crearUsuario(Usuario usuario) {
		boolean existeUsuarioEstudiante = false;
		if (null != usuario) {
			Usuario usuBd = servicioUsuarios.buscarPorNickName(usuario.getUsername());
			if (null == usuBd) {
				existeUsuarioEstudiante = servicioEstudiantes.obtenerEstudiantePorUsuario(usuario.getUsername());
				if (!existeUsuarioEstudiante) {
					if (!usuario.isUsuaioLdap()) {
						return servicioUsuarios.crearUsuario(usuario);
					}else {
						return false;
					}
				}else {
					if (usuario.isUsuaioLdap()) {
						return servicioUsuarios.crearUsuario(usuario);
					}else {
						return false;
					}
				}
				
			}else {
				return false;
			}
		}else {
			return false;
		}
	}
	
	@PUT
	@Secured
	@Path("/modificar")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public boolean actualizarUsuario(Usuario usuario) {
		int count = 0;
		if (null != usuario) {
			Usuario usuBd = servicioUsuarios.buscarPorId(usuario.getId());
			if (usuBd != null) {
				if (!usuBd.getUsername().equals(usuario.getUsername())) {
					Usuario usAux = servicioUsuarios.buscarPorNickName(usuario.getUsername());
					if (usAux == null) {
						boolean existeUsuarioEstudiante = servicioEstudiantes.obtenerEstudiantePorUsuario(usuario.getUsername());
						if (existeUsuarioEstudiante) {
							if (usuario.isUsuaioLdap()) {
								count = servicioUsuarios.actualizarUsuario(usuario);
							}
						}
					}
				}else {
					boolean existeUsuarioEstudiante = servicioEstudiantes.obtenerEstudiantePorUsuario(usuario.getUsername());
					if (existeUsuarioEstudiante) {
						if (usuario.isUsuaioLdap()) {
							count = servicioUsuarios.actualizarUsuario(usuario);
						}
					}else {
						if (!usuario.isUsuaioLdap()) {
							count = servicioUsuarios.actualizarUsuario(usuario);
						}
					}
				}
			}
		}
		
		if (count > 0) {
			return true;
		}else {
			return false;
		}
	}
	
}
