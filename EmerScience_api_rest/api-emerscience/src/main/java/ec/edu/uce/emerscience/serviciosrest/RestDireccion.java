package ec.edu.uce.emerscience.serviciosrest;

import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import ec.edu.uce.emerscience.dto.Direccion;
import ec.edu.uce.emerscience.servicios.ServicioDireccion;

@ApplicationScoped
@Path("direcciones")
public class RestDireccion {
	
	@Inject private ServicioDireccion servicio;
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response registrarDireccion(List<Direccion> listaDirecciones) {
		boolean guardado = false;
		if (!listaDirecciones.isEmpty()) {
			for (Direccion direccion : listaDirecciones) {
				guardado = servicio.registrarDireccion(direccion);
				if (guardado) {
					continue;
				}else {
					break;
				}
			}
			if (guardado) {
				return Response.status(Response.Status.CREATED).entity(true).build();
			}else {
				return Response.status(Response.Status.NOT_MODIFIED).entity(false).build();
			}
		}else {
			return Response.status(Response.Status.NOT_MODIFIED).entity(false).build();
		}
	}

}
