package ec.edu.uce.emerscience.serviciosrest;

import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import ec.edu.uce.emerscience.dto.Coordenada;
import ec.edu.uce.emerscience.dto.Estudiante;
import ec.edu.uce.emerscience.seguridad.Secured;
import ec.edu.uce.emerscience.servicios.ServicioEstudiante;
import ec.edu.uce.emerscience.servicios.ServicioFamiliar;

@ApplicationScoped
@Path("estudiantes")
public class RestEstudiantes {
	
	@Inject private ServicioEstudiante servicio;
	@Inject private ServicioFamiliar servicioFam;
	
	@GET
	@Secured
	@Produces(MediaType.APPLICATION_JSON)
	public List<Estudiante> obtenerEstudiantes() {
		List<Estudiante> listEst = servicio.obtenerEstudiantes();
		return listEst;
	}
	
	@POST
	@Secured
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public boolean registrarEstudiante(Estudiante estudiante) {
		if (estudiante != null) {
			System.out.println("LAT: " + estudiante.getLat());
			System.out.println("FEC_MOD: " + estudiante.getFechaModificacion().toString());
			if ((servicio.obtenerEstudiantePorCedula(estudiante.getCedula())) == null) {
				servicio.registrarEstudiante(estudiante);
				return true;
			}else {
				return false;
			}
		}else {
			return false;
		}
	}
	
	@GET
	@Secured
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/{cedula}")
	public Estudiante obtenerEstudiantePorCedula(@PathParam("cedula") String cedula) {
		if (!cedula.isEmpty() || cedula.length() != 0) {
			return servicio.obtenerEstudiantePorCedula(cedula);
		}
		return null;
	}
	
	@GET
	@Secured
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/{cedula}/{usuario}")
	public Estudiante obtenerEstudiantePorCedulaUsuario(@PathParam("cedula") String cedula, @PathParam("usuario") String usuario) {
		if (!cedula.isEmpty() || cedula.length() != 0) {
			return servicio.obtenerEstudiantePorCedulaUsuario(cedula, usuario);
		}
		return null;
	}
	
	@GET
	@Secured
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/cedulas")
	public List<String> obtenerCedulasEstudiantes(){
		List<String> cedulasFamiliares = servicioFam.obtenerCedulasFamiliares();
		List<String> cedulasEstudiantes = servicio.obtenerCedulasEstudiantes();
		List<String> cedulas = new ArrayList<String>();
		cedulas.addAll(cedulasEstudiantes);
		cedulas.addAll(cedulasFamiliares);
		return cedulas;
	}
	
	@PUT
	@Path("/actualizar")
	@Secured
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public boolean actualizarEstudiante(Estudiante estudiante) {
		int count = servicio.actualizarEstudiante(estudiante);
		if (count >= 1) {
			return true;
		}
		return false;
	}
	
	@GET
	@Path("/coordenadas")
	@Secured
	@Produces(MediaType.APPLICATION_JSON)
	public List<Coordenada> obtenerCoordenadas() {
		List<Coordenada> listCoordenadas = servicio.obtenerCoordenadas();
		return listCoordenadas;
	}
	
	@GET
	@Path("/coordenadas/cedula/{cedula}")
	@Secured
	@Produces(MediaType.APPLICATION_JSON)
	public Coordenada obtenerCoordPorCI(@PathParam("cedula") String cedula) {
		return servicio.obtenerCoordenadaPorCI(cedula);
	}
	
	@GET
	@Path("/coordenadas/{apeNom}")
	@Secured
	@Produces(MediaType.APPLICATION_JSON)
	public Coordenada obtenerCoordPorApeNom(@PathParam("apeNom") String apellidosNombres) {
		return servicio.obtenerCoordPorApeNom(apellidosNombres);
	}
	
}
