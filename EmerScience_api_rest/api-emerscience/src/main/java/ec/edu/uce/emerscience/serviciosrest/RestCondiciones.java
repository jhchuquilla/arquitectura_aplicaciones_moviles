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

import ec.edu.uce.emerscience.dto.CondicionSalud;
import ec.edu.uce.emerscience.dto.CondicionSaludBd;
import ec.edu.uce.emerscience.seguridad.Secured;
import ec.edu.uce.emerscience.servicios.ServicioCondiciones;

@ApplicationScoped
@Path("condiciones")
public class RestCondiciones {

	@Inject private ServicioCondiciones servicio;
	
	@GET
	@Secured
	@Produces(MediaType.APPLICATION_JSON)
	public List<CondicionSaludBd> obtenerCondicionesCatalogo(){
		return servicio.obtenerCondicionesCatalogo();
	}
	
	@GET
	@Secured
	@Path("/{cedula}")
	@Produces(MediaType.APPLICATION_JSON)
	public List<CondicionSalud> obtenerCondicionSaludPorCedula(@PathParam("cedula") String cedula){
		List<CondicionSalud> listaCondiciones = servicio.obtenerCondicionSaludPorCedula(cedula);
		return listaCondiciones;
	}
	
	@POST
	@Secured
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public String registrarCondicionesFamiliar(List<CondicionSalud> listaCondiciones) {
		if (!listaCondiciones.isEmpty()) {
			servicio.registrarCondicionSalud(listaCondiciones);
			return "true";
		}
		return "false";
	}
	
}
