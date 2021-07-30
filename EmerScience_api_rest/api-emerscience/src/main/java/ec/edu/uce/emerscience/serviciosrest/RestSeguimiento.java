package ec.edu.uce.emerscience.serviciosrest;

import java.util.ArrayList;
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
import javax.ws.rs.core.Response;

import ec.edu.uce.emerscience.dto.SeguimientoFamiliar;
import ec.edu.uce.emerscience.seguridad.Secured;
import ec.edu.uce.emerscience.servicios.ServicioSeguimiento;

@ApplicationScoped
@Path("seguimiento")
public class RestSeguimiento {

	@Inject private ServicioSeguimiento servicio;
	
	@POST
	@Secured
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public boolean registrarSeguimiento(SeguimientoFamiliar seguimiento) {
		if (seguimiento != null) {
			if ((servicio.obtenerSeguimientoCIFecha(seguimiento.getCedulaFamiliar(),seguimiento.getFechaRegistroSeguimiento())) == null) {
				//if ((servicio.obtenerSeguimientoCIFecha("1725515538","2020-05-30")) == null) {	
				servicio.registrarDiaSeguimiento(seguimiento);
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
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/numeroRegistros/{cedula}")
	public int numeroRegistros(@PathParam("cedula") String cedula) {
		if (cedula!= null) {
				return servicio.obtenerNumeroRegistrosFamiliar(cedula);
		}else {
			return 0;
		}
	}
	
	@GET
	@Secured
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/all/{cedulaFamiliar}")
	public Response obtenerListaRegistrosSeguimiento(@PathParam("cedulaFamiliar") String cedulaFamiliar) {
		List<SeguimientoFamiliar> list = new ArrayList<>();
		if (cedulaFamiliar != null) {
			list = servicio.obtenerListaSeguimientos(cedulaFamiliar);
			if (list.size() > 0) {
				return Response.ok(list).build();
			}
			return Response.status(Response.Status.NOT_FOUND).entity(list).build();
		}
		return Response.status(Response.Status.BAD_REQUEST).build();
	}
	
}
