package ec.edu.uce.emerscience.servicios;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.enterprise.context.ApplicationScoped;
import javax.sql.DataSource;

import org.mindrot.jbcrypt.BCrypt;

import ec.edu.uce.emerscience.dto.Usuario;

@ApplicationScoped
public class ServicioUsuarioImpl implements ServicioUsuario{

	static final String SELECT_USUARIO_BY_NICK_CLAVE = "SELECT * FROM usuarios WHERE us_nick = ? AND us_pwd = ? ;";
	static final String INSERT_USUARIO_CON_CLAVE = "INSERT INTO usuarios (us_nick, us_pwd, us_rol, us_usuario_ldap, us_activo) VALUES "
			+ "(?,?,?,?,?);";
	static final String INSERT_USUARIO_SIN_CLAVE = "INSERT INTO usuarios (us_nick, us_rol, us_usuario_ldap, us_activo) VALUES "
			+ "(?,?,?,?);";
	static final String SELECT_USUARIO_BY_NICK = "SELECT * FROM usuarios WHERE us_nick = ? ;";
	static final String SELECT_USUARIO_BY_ID = "SELECT * FROM usuarios WHERE us_id = ? ;";
	static final String SELECT_USUARIOS = "SELECT * FROM usuarios ORDER BY us_nick ASC ;";
	static final String UPDATE_USUARIO_CON_CLAVE = "UPDATE usuarios SET us_nick = ?, us_pwd = ?, us_rol = ?, us_usuario_ldap = ?, "
			+ "us_activo = ? WHERE us_id = ? ;";
	static final String UPDATE_USUARIO_SIN_CLAVE = "UPDATE usuarios SET us_nick = ?, us_rol = ?, us_usuario_ldap = ?, "
			+ "us_activo = ? WHERE us_id = ? ;";
	
	private PreparedStatement pstm = null;
	private Connection con = null;

	@Resource(name = "jdbc/conexionDB")
	private DataSource fuente;
	
	@Override
	public Usuario loginUsuario(String nickname, String clave) {
		ResultSet rs = null;
		List<Usuario> lista = new ArrayList<>();
		try {
			con = fuente.getConnection();
			pstm = con.prepareStatement(SELECT_USUARIO_BY_NICK);
			pstm.setString(1, nickname);
			rs = pstm.executeQuery();
			while (rs.next()) {
				Usuario us = new Usuario();
				us.setUsername(rs.getString(2));
				us.setPassword(rs.getString(3));
				us.setRol(rs.getString(4));
				us.setUsuaioLdap(rs.getBoolean(5));
				us.setUsuarioActivo(rs.getBoolean(6));
				if (BCrypt.checkpw(clave, us.getPassword())) {
					lista.add(us);
				}
			}
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}catch (Exception e) {
			e.printStackTrace();
		}finally {
			
			try {
				if (pstm != null) {
					pstm.close();
					con.close();
				}
			} catch (SQLException e) {}
			try {
				if (con != null) {
					con.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		if (lista.size() > 0) {
			return lista.get(0);
		}else {
			return null;
		}
	}
	
	@Override
	public Usuario loginUsuariosLdap(String username) {
		ResultSet rs = null;
		List<Usuario> lista = new ArrayList<>();
		try {
			con = fuente.getConnection();
			pstm = con.prepareStatement(SELECT_USUARIO_BY_NICK);
			pstm.setString(1, username);
			rs = pstm.executeQuery();
			while (rs.next()) {
				Usuario us = new Usuario();
				us.setUsername(rs.getString(2));
				//us.setPassword(rs.getString(3));
				us.setRol(rs.getString(4));
				us.setUsuaioLdap(rs.getBoolean(5));
				us.setUsuarioActivo(rs.getBoolean(6));
				lista.add(us);
			}
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}catch (Exception e) {
			e.printStackTrace();
		}finally {
			
			try {
				if (pstm != null) {
					pstm.close();
					con.close();
				}
			} catch (SQLException e) {}
			try {
				if (con != null) {
					con.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		if (lista.size() > 0) {
			return lista.get(0);
		}else {
			return null;
		}
	}
	
	@Override
	public boolean crearUsuario(Usuario usuario) {
		int count = 0;
		try {
			con = fuente.getConnection();
			if (!usuario.isUsuaioLdap()) {
				pstm = con.prepareStatement(INSERT_USUARIO_CON_CLAVE);
				pstm.setString(1, usuario.getUsername());
				pstm.setString(2, BCrypt.hashpw(usuario.getPassword(), BCrypt.gensalt(10)));
				pstm.setString(3, usuario.getRol());
				pstm.setBoolean(4, usuario.isUsuaioLdap());
				pstm.setBoolean(5, usuario.isUsuarioActivo());
			}else {
				pstm = con.prepareStatement(INSERT_USUARIO_SIN_CLAVE);
				pstm.setString(1, usuario.getUsername());
				pstm.setString(2, usuario.getRol());
				pstm.setBoolean(3, usuario.isUsuaioLdap());
				pstm.setBoolean(4, usuario.isUsuarioActivo());
			}
			count = pstm.executeUpdate();
			
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (pstm != null) {
					pstm.close();
					con.close();
				}
			} catch (SQLException e) {}
			try {
				if (con != null) {
					con.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		if (count > 0) {
			return true;
		}else {
			return false;
		}
	}
	
	@Override
	public int actualizarUsuario(Usuario usuario) {
		int count = 0;
		try {
			con = fuente.getConnection();
			if (!usuario.getPassword().isEmpty()) {
				pstm = con.prepareStatement(UPDATE_USUARIO_CON_CLAVE);
				pstm.setString(1, usuario.getUsername());
				pstm.setString(2, BCrypt.hashpw(usuario.getPassword(), BCrypt.gensalt(10)));
				pstm.setString(3, usuario.getRol());
				pstm.setBoolean(4, usuario.isUsuaioLdap());
				pstm.setBoolean(5, usuario.isUsuarioActivo());
				pstm.setInt(6, usuario.getId());
			}else {
				pstm = con.prepareStatement(UPDATE_USUARIO_SIN_CLAVE);
				pstm.setString(1, usuario.getUsername());
				pstm.setString(2, usuario.getRol());
				pstm.setBoolean(3, usuario.isUsuaioLdap());
				pstm.setBoolean(4, usuario.isUsuarioActivo());
				pstm.setInt(5, usuario.getId());
			}
			count = pstm.executeUpdate();
			
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (pstm != null) {
					pstm.close();
					con.close();
				}
			} catch (SQLException e) {}
			try {
				if (con != null) {
					con.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return count;
	}
	
	@Override
	public Usuario buscarPorNickName(String nickName) {
		ResultSet rs = null;
		List<Usuario> lista = new ArrayList<>();
		try {
			con = fuente.getConnection();
			pstm = con.prepareStatement(SELECT_USUARIO_BY_NICK);
			pstm.setString(1, nickName);
			rs = pstm.executeQuery();
			while (rs.next()) {
				Usuario us = new Usuario();
				us.setId(rs.getInt(1));
				us.setUsername(rs.getString(2));
				us.setRol(rs.getString(4));
				us.setUsuaioLdap(rs.getBoolean(5));
				us.setUsuarioActivo(rs.getBoolean(6));
				lista.add(us);
			}
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}catch (Exception e) {
			e.printStackTrace();
		}finally {
			
			try {
				if (pstm != null) {
					pstm.close();
					con.close();
				}
			} catch (SQLException e) {}
			try {
				if (con != null) {
					con.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		if (lista.size() > 0) {
			return lista.get(0);
		}else {
			return null;
		}
	}
	@Override
	public Usuario buscarPorId(int id) {
		ResultSet rs = null;
		List<Usuario> lista = new ArrayList<>();
		try {
			con = fuente.getConnection();
			pstm = con.prepareStatement(SELECT_USUARIO_BY_ID);
			pstm.setInt(1, id);
			rs = pstm.executeQuery();
			while (rs.next()) {
				Usuario us = new Usuario();
				us.setId(rs.getInt(1));
				us.setUsername(rs.getString(2));
				us.setRol(rs.getString(4));
				us.setUsuaioLdap(rs.getBoolean(5));
				us.setUsuarioActivo(rs.getBoolean(6));
				lista.add(us);
			}
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}catch (Exception e) {
			e.printStackTrace();
		}finally {
			
			try {
				if (pstm != null) {
					pstm.close();
					con.close();
				}
			} catch (SQLException e) {}
			try {
				if (con != null) {
					con.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		if (lista.size() > 0) {
			return lista.get(0);
		}else {
			return null;
		}
	}
	@Override
	public List<Usuario> buscarTodos() {
		ResultSet rs = null;
		List<Usuario> lista = new ArrayList<>();
		try {
			con = fuente.getConnection();
			pstm = con.prepareStatement(SELECT_USUARIOS);
			rs = pstm.executeQuery();
			while (rs.next()) {
				Usuario us = new Usuario();
				us.setId(rs.getInt(1));
				us.setUsername(rs.getString(2));
				//us.setPassword(rs.getString(3));
				us.setRol(rs.getString(4));
				us.setUsuaioLdap(rs.getBoolean(5));
				us.setUsuarioActivo(rs.getBoolean(6));
				lista.add(us);
			}
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}catch (Exception e) {
			e.printStackTrace();
		}finally {
			
			try {
				if (pstm != null) {
					pstm.close();
					con.close();
				}
			} catch (SQLException e) {}
			try {
				if (con != null) {
					con.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		if (lista.size() > 0) {
			return lista;
		}else {
			return null;
		}
	}

}
