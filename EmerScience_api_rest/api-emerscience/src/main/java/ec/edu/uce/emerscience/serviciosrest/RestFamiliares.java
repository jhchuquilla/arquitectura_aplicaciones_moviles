package ec.edu.uce.emerscience.serviciosrest;

import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import ec.edu.uce.emerscience.dto.Familiar;
import ec.edu.uce.emerscience.seguridad.Secured;
import ec.edu.uce.emerscience.servicios.ServicioFamiliar;

@ApplicationScoped
@Path("familiares")
public class RestFamiliares {

	@Inject private ServicioFamiliar servicio;
	
	@GET
	@Secured
	@Produces(MediaType.APPLICATION_JSON)
	public List<Familiar> obtenerFamiliares() {
		List<Familiar> listEst = servicio.obtenerFamiliares();
		return listEst;
	}
	
	@GET
	@Secured
	@Path("/{cedulaEstudiante}")
	@Produces(MediaType.APPLICATION_JSON)
	public List<Familiar> obtenerFamiliaresPorCedulaEstudiante(@PathParam("cedulaEstudiante")String cedulaEstudiante){
		if (!cedulaEstudiante.isEmpty()) {
			return servicio.obtenerFamiliarPorCedulaEstudiante(cedulaEstudiante);
		}
		return null;
	}
	
	@POST
	@Secured
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public boolean registrarFamiliar(List<Familiar> listFamiliar) {
		if(!listFamiliar.isEmpty()) {
			for (Familiar famAux : listFamiliar) {
				servicio.registrarFamiliar(famAux);
			}
			return true;
		}else {
			return false;
		}
	}
	
	@GET
	@Secured
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/apenom/{apellidosnombres}")
	public Familiar obtenerCedulasEstudiantes(@PathParam("apellidosnombres")String apellidosnombres){
		if (!apellidosnombres.isEmpty() || apellidosnombres.length() != 0) {
			return servicio.obtenerFamiliarPorApellidosNombres(apellidosnombres);
		}
		return null;
	}
	
	@GET
	@Secured
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/cedula/{cedula}")
	public Familiar obtenerFamiliarPorCI(@PathParam("cedula")String cedula){
		if (!cedula.isEmpty() || cedula.length() != 0) {
			return servicio.obtenerFamiliarPorCI(cedula);
		}
		return null;
	}
	

	@GET
	@Secured
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/cedulas")
	public List<String> obtenerCedulasEstudiantes(){
		return servicio.obtenerCedulasFamiliares();
	}
	
	@POST
	@Path("/actualizar")
	@Secured
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public boolean actualizarFamiliar(Familiar familiar) {
		int count = 0;
		count = servicio.eliminarFamiliar(familiar.getCedulaFam());
		if (count > 0) {
			servicio.registrarFamiliar(familiar);
			return true;
		}
		return false;
	}
	
}
