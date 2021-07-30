package ec.edu.uce.emerscience.conexionldap;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Hashtable;

import javax.enterprise.context.ApplicationScoped;
import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.InitialDirContext;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;

import ec.edu.uce.emerscience.dto.Estudiante;

@ApplicationScoped
public class ConexionLdap implements ConexionLdapI{
	private String ldapAdServer;
	private String ldapIPServer;
	private String ldapPuerto;
    private String ldapBase;
    private String ldapUsuario;
    private String ldapPassword;

	public ConexionLdap(String ldapIPServer, String ldapPuerto, String ldapBase) {
		this.ldapIPServer = ldapIPServer;
		this.ldapAdServer = "ldap://"+ldapIPServer+":"+ldapPuerto;
		this.ldapBase = ldapBase;
	}
	public String getLdapAdServer() {
		return ldapAdServer;
	}
	public void setLdapAdServer(String ldapAdServer) {
		this.ldapAdServer = ldapAdServer;
	}
	public String getLdapBase() {
		return ldapBase;
	}
	public void setLdapBase(String ldapBase) {
		this.ldapBase = ldapBase;
	}
	public String getLdapUsuario() {
		return ldapUsuario;
	}
	public void setLdapUsuario(String ldapUsuario) {
		this.ldapUsuario = ldapUsuario;
	}
	public String getLdapPassword() {
		return ldapPassword;
	}
	public void setLdapPassword(String ldapPassword) {
		this.ldapPassword = ldapPassword;
	}
    
    public String getLdapIPServer() {
		return ldapIPServer;
	}
	public void setLdapIPServer(String ldapIPServer) {
		this.ldapIPServer = ldapIPServer;
	}
	
	public String getLdapPuerto() {
		return ldapPuerto;
	}
	public void setLdapPuerto(String ldapPuerto) {
		this.ldapPuerto = ldapPuerto;
	}
	
	@Override
	public boolean verificarLoginUsuario(String ldapUsuario, String ldapPassword){
		SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date fecha = new Date();
		String fechaFormato = formato.format(fecha);
		System.out.println(ldapUsuario);
		System.out.println(fechaFormato);
		
		try {
    		String usuario = "UCE\\";    	
        	Hashtable<String, Object> env = new Hashtable<String, Object>();
    	    env.put(Context.SECURITY_AUTHENTICATION, "simple");
    	    if(ldapUsuario != null) {
    	    	usuario = usuario + ldapUsuario.toLowerCase();
    	    	env.put(Context.SECURITY_PRINCIPAL, usuario);
    	    	System.out.println("USUARIO DIFERENTE DE NULL");
    	    }else{
    	    	return false;
    	    }

    	    if(ldapPassword != null) {
    	        env.put(Context.SECURITY_CREDENTIALS, ldapPassword);
    	        System.out.println("PASSWORD DIFERENTE DE NULL");
    	    }else{
    	    	return false;
    	    }

    	    env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
    	    env.put(Context.PROVIDER_URL, ldapAdServer);
            env.put("java.naming.ldap.attributes.binary", "objectSID");
            @SuppressWarnings("unused")
			InitialDirContext ctx = new InitialDirContext(env);
            return true;
		} catch (NamingException e) {
			System.out.println("EXCEPTION: " + e.getMessage());
			return false;
		} 	
    }
	
	@Override
	public Estudiante obtenerAtributos(String ldapUsuario, String ldapPassword) {
		Estudiante est = new Estudiante();
		try {
    		String usuario = "UCE\\";    	
        	Hashtable<String, Object> env = new Hashtable<String, Object>();
    	    env.put(Context.SECURITY_AUTHENTICATION, "simple");
    	    if(ldapUsuario != null) {
    	    	usuario = usuario + ldapUsuario.toLowerCase();
    	    	env.put(Context.SECURITY_PRINCIPAL, usuario);
    	    }else{
    	    	return null;
    	    }

    	    if(ldapPassword != null) {
    	        env.put(Context.SECURITY_CREDENTIALS, ldapPassword);
    	    }else{
    	    	return null;
    	    }

    	    env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
    	    env.put(Context.PROVIDER_URL, ldapAdServer);
            env.put("java.naming.ldap.attributes.binary", "objectSID");
            env.put(Context.REFERRAL, "follow");
            InitialDirContext ctx = new InitialDirContext(env);

            SearchControls ctls = new SearchControls();
            ctls.setSearchScope(SearchControls.SUBTREE_SCOPE);
            
            //Buscamos el objeto usuario mediante un filtro donde se le indica el DC y el respectivo filtro
            NamingEnumeration<SearchResult> answer = ctx.search("dc=uce, dc=edu, dc=ec"
            		, "(&(objectClass=user)(objectCategory=person)(mail="+ ldapUsuario +"@uce.edu.ec))", ctls);
            
            while (answer.hasMore()) {
                SearchResult sr = (SearchResult)answer.next();
                est.setApellidos(sr.getAttributes().get("sn").get().toString());
                est.setNombres(sr.getAttributes().get("givenName").get().toString());
                est.setCedula(sr.getAttributes().get("postalCode").get().toString());
                if (!ldapUsuario.equals("dpruebas")) {
                	if (null != sr.getAttributes().get("streetAddress")) {
                		est.setCorreo(sr.getAttributes().get("streetAddress").get().toString());
					}else {
						est.setCorreo("");
					}
				}else {
					est.setCorreo("");
				}
            }
            
            return est;
		} catch (NamingException e) {
			System.out.println("EXCEPTION: " + e.getMessage());
			e.printStackTrace();
			return null;
		}
		
	}
	
}