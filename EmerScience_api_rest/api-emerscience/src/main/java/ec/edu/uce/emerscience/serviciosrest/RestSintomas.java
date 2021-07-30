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

import ec.edu.uce.emerscience.dto.Sintoma;
import ec.edu.uce.emerscience.dto.SintomaRespiratorio;
import ec.edu.uce.emerscience.dto.SintomasBd;
import ec.edu.uce.emerscience.seguridad.Secured;
import ec.edu.uce.emerscience.servicios.ServicioSintoma;

@ApplicationScoped
@Path("sintomas")
public class RestSintomas {
	
	@Inject private ServicioSintoma servicio;
	
	@GET
	@Secured
	@Produces(MediaType.APPLICATION_JSON)
	public List<SintomasBd> obtenerSintomasCatalogo() {
		List<SintomasBd> listSintomasCatalogo = servicio.obtenerSintomasCatalogo();
		return listSintomasCatalogo;
	}
	
	@GET
	@Secured
	@Path("/{cedulaFamiliar}")
	@Produces(MediaType.APPLICATION_JSON)
	public List<Sintoma> obtenerSintomasFamiliarPorCedula(@PathParam("cedulaFamiliar") String cedulaFamiliar) {
		List<Sintoma> listSintomas = servicio.obtenerNombresSintomasFamiliares(cedulaFamiliar);
		return listSintomas;
	}
	
	@POST
	@Secured
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public boolean registrarSintomas(List<Sintoma> listaSintomas) {
		
		if (!listaSintomas.isEmpty()) {
			servicio.registrarSintomas(listaSintomas);
			return true;
		}
		return false;
	}
	
	@POST
	@Secured
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/respiratorios")
	public String registrarSintomasRespiratorios(List<SintomaRespiratorio> listaSintomasRespiratorios) {
		
		if (!listaSintomasRespiratorios.isEmpty()) {
			servicio.registrarSintomasRespiratorios(listaSintomasRespiratorios);
			return "true";
		}
		return "false";
	}
	
}
