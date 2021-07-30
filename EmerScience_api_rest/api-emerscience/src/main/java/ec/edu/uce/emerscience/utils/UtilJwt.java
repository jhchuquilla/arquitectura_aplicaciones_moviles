package ec.edu.uce.emerscience.utils;

import java.util.Calendar;
import java.util.Date;

import ec.edu.uce.emerscience.seguridad.RestSecurityFilter;
import io.jsonwebtoken.Header;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

public class UtilJwt {

	public static String generarToken(String username) {
		//Calculamos la fecha de expiración del token
    	Date issueDate = new Date();
    	Calendar calendar = Calendar.getInstance();
    	calendar.setTime(issueDate);
    	calendar.add(Calendar.MINUTE, 30);
        Date expireDate = calendar.getTime();
        
        Jwts.header();
		//Creamos el token
        String jwtToken = Jwts.builder()
        		.setIssuer(username)
                .setSubject(username)
                .setIssuedAt(issueDate)
                .setExpiration(expireDate)
                .signWith(SignatureAlgorithm.HS512, RestSecurityFilter.KEY_BASE_64)
                .setHeaderParam(Header.TYPE, "JWT")
                .compact();
        return jwtToken;
	}
	
}
