package ec.edu.uce.emerscience.seguridad;

import java.io.IOException;
import java.security.Key;

import javax.annotation.Priority;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;
import javax.xml.bind.DatatypeConverter;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.impl.crypto.MacProvider;

@Provider
@Secured
@Priority(Priorities.AUTHENTICATION)
public class RestSecurityFilter implements ContainerRequestFilter{
	
	public static final Key KEY = MacProvider.generateKey() ;
	public static final String KEY_BASE_64 = DatatypeConverter.printBase64Binary(KEY.getEncoded()); 

	@Override
	public void filter(ContainerRequestContext requestContext) throws IOException {
		
		//Recuperamos la cabecera HTTP Authorization de la peticion enviada por el cliente
		String authorizationHeader = requestContext.getHeaderString(HttpHeaders.AUTHORIZATION);
		
		try {
			//Extraemos el token de la cabecera de peticion HTTP
			String token = authorizationHeader.substring("Bearer".length()).trim();
			
			Jwts.parser().setSigningKey(KEY_BASE_64).parseClaimsJws(token);
			
		} catch (Exception e) {
			//En caso de que la petición llegue sin token en la cabecera http, se devuelve Status de no autorizado
			requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED).build());
		}
		
	}
	
}
