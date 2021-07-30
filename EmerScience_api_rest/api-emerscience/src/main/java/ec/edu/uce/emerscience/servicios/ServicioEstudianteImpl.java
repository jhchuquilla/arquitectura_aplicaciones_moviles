package ec.edu.uce.emerscience.servicios;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.enterprise.context.ApplicationScoped;
import javax.sql.DataSource;

import ec.edu.uce.emerscience.dto.Coordenada;
import ec.edu.uce.emerscience.dto.Estudiante;
import ec.edu.uce.emerscience.utils.RedondeoUtil;

@ApplicationScoped
public class ServicioEstudianteImpl implements ServicioEstudiante {

	static final String SELECT_ALL = "SELECT * FROM estudiantes;";
	
	static final String SELECT_BY_ID = "SELECT * FROM estudiantes WHERE es_ci = ?;";
	
	static final String SELECT_BY_ID_USERNAME = "SELECT * FROM estudiantes WHERE es_ci = ? AND es_usu = ?;";
	
	static final String INSERT_ESTUDIANTE = "INSERT INTO estudiantes (es_ci, es_apellido, es_nombre, es_edad, es_sexo"
			+ ", es_celular, es_fijo, es_correo, es_num_ph, es_alg_ps, es_presen_sint, es_cant_ps, es_cont_pc, es_lat"
			+ ", es_long, es_fec_reg, es_usu, es_fec_mod, es_cant_drm, es_cant_pt, es_cant_pef, es_prop_viv, es_ban_cmp, es_pag_srv, es_prb_cmd) " 
			+ "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?);";
	
	static final String UPDATE_ESTUDIANTE = "UPDATE estudiantes SET es_apellido = ?, es_nombre = ?, es_edad = ?, es_sexo = ?, "
			+ "es_celular = ?, es_fijo = ?, es_correo = ?, es_num_ph = ?, es_alg_ps = ?, es_presen_sint = ?, es_cant_ps = ?, "
			+ "es_cont_pc = ?, es_lat = ?, es_long = ?, es_fec_reg = ?, es_fec_mod = ?, es_usu = ?, "
			+ "es_cant_drm = ?, es_cant_pt = ?, es_cant_pef = ?, es_prop_viv = ?, es_ban_cmp = ?, es_pag_srv = ?, es_prb_cmd = ? WHERE es_ci = ? ;";
	
	static final String DELETE_ESTUDIANTE = "DELETE FROM estudiantes WHERE es_ci = ? ;";
	
	static final String SELECT_CEDULAS_ESTUDIANTES = "SELECT es_ci from estudiantes ORDER BY es_ci ASC ;";
	
	static final String SELECT_COORDENADAS = "SELECT es_nombre, es_apellido, es_ci, es_celular, es_lat, es_long, "
			+ "es_alg_ps, es_presen_sint, es_num_ph, es_cant_ps FROM estudiantes WHERE es_lat IS NOT NULL "
			+ "ORDER BY es_ci ASC;";
	
	static final String SELECT_COORD_BY_CI = "SELECT es_nombre, es_apellido, es_ci, es_celular, es_lat, es_long, "
			+ "es_alg_ps, es_presen_sint, es_num_ph, es_cant_ps FROM estudiantes WHERE es_lat IS NOT NULL AND es_ci = ? "
			+ "ORDER BY es_ci ASC;";
	
	static final String SELECT_COORD_BY_APE_NOM = "SELECT es_nombre, es_apellido, es_ci, es_celular, es_lat, es_long, "
			+ "es_alg_ps, es_presen_sint, es_num_ph, es_cant_ps FROM estudiantes WHERE es_lat IS NOT NULL AND CONCAT(es_apellido,' ',es_nombre) = ? "
			+ "ORDER BY es_ci ASC;";
	static final String SELECT_USUARIOS_ESTUDIANTES_POR_USUARIO = "SELECT es_usu FROM estudiantes WHERE es_usu = ? ;";
	
	private PreparedStatement pstm = null;
	private Connection con = null;

	@Resource(name = "jdbc/conexionDB")
	private DataSource fuente;

	@Override
	public List<Estudiante> obtenerEstudiantes() {
		ResultSet rs = null;
		List<Estudiante> listaEst = new ArrayList<Estudiante>();
		try {
			con = fuente.getConnection();
			pstm = con.prepareStatement(SELECT_ALL);
			rs = pstm.executeQuery();

			while (rs.next()) {
				Estudiante est = new Estudiante();
				est.setCedula(rs.getString(1));
				est.setApellidos(rs.getString(2));
				est.setNombres(rs.getString(3));
				est.setEdad(rs.getInt(4));
				est.setSexo(rs.getString(5));
				est.setUsuario(rs.getString(17));
				listaEst.add(est);
			}
			rs.close();
		}catch (SQLException e) {
			System.out.println("SQLException");
			e.printStackTrace();
		}catch (Exception e) {
			// TODO: handle exception
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
		return listaEst;
	}

	@Override
	public void registrarEstudiante(Estudiante estudiante) {
		SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date fechaFormato = null;
		Calendar calendar = Calendar.getInstance();

		try {
			fechaFormato = formato.parse(estudiante.getFechaRegistroPersona());
			calendar.setTime(fechaFormato);
			System.out.println("FECHA OBTENIDA: " + estudiante.getFechaRegistroPersona());
			Timestamp timeStamp = new Timestamp(calendar.getTimeInMillis());
			con = fuente.getConnection();
			pstm = con.prepareStatement(INSERT_ESTUDIANTE);
			pstm.setString(1, estudiante.getCedula());
			pstm.setString(2, estudiante.getApellidos());
			pstm.setString(3, estudiante.getNombres());
			pstm.setInt(4, estudiante.getEdad());
			pstm.setString(5, estudiante.getSexo());
			pstm.setString(6, estudiante.getCelular());
			pstm.setString(7, estudiante.getTelFijo());
			pstm.setString(8, estudiante.getCorreo());
			pstm.setInt(9, estudiante.getNumPersonasHogar());
			pstm.setString(10, estudiante.getAlguienPresentaSintomas());
			pstm.setString(11, estudiante.getEstudiantePresentaSintomas());
			pstm.setInt(12, estudiante.getCantidadPersonasSintomas());
			pstm.setString(13, estudiante.getContactoPersonaConCovid());
			if (estudiante.getLat() == 0.0) {
				pstm.setNull(14, Types.NUMERIC);
				pstm.setNull(15, Types.NUMERIC);
			}else {
				pstm.setDouble(14, RedondeoUtil.redondearDecimales(estudiante.getLat(), 6));
				pstm.setDouble(15, RedondeoUtil.redondearDecimales(estudiante.getLng(), 6));
			}
			
			pstm.setTimestamp(16, timeStamp);
			pstm.setString(17, estudiante.getUsuario());
			pstm.setNull(18, Types.TIMESTAMP);
			
			if (estudiante.getCantidadDormitorios() == 0) {
				pstm.setNull(19, Types.INTEGER);
			}else {
				pstm.setInt(19, estudiante.getCantidadDormitorios());
			}
			
			if (estudiante.getCantidadPersonasTrabajan() == 0) {
				pstm.setNull(20, Types.INTEGER);
			}else {
				pstm.setInt(20, estudiante.getCantidadPersonasTrabajan());
			}
			
			if (estudiante.getCantidadPersonasEmpleoFijo() == 0) {
				pstm.setNull(21, Types.INTEGER);
			}else {
				pstm.setInt(21, estudiante.getCantidadPersonasEmpleoFijo());
			}
			
			if (estudiante.getPropiedadVivienda().isEmpty() || estudiante.getPropiedadVivienda().equals(null)) {
				pstm.setNull(22, Types.VARCHAR);
			}else {
				pstm.setString(22, estudiante.getPropiedadVivienda());
			}
			
			if (estudiante.getBanioCompartido().isEmpty() || estudiante.getBanioCompartido().equals(null)) {
				pstm.setNull(23, Types.VARCHAR);
			}else {
				pstm.setString(23, estudiante.getBanioCompartido());
			}
			
			if (estudiante.getPagoArriendoServiciosBasicos().isEmpty() || estudiante.getPagoArriendoServiciosBasicos().equals(null)) {
				pstm.setNull(24, Types.VARCHAR);
			}else {
				pstm.setString(24, estudiante.getPagoArriendoServiciosBasicos());
			}
			
			if (estudiante.getProblemasCompraComida().isEmpty() || estudiante.getProblemasCompraComida().equals(null)) {
				pstm.setNull(25, Types.VARCHAR);
			}else {
				pstm.setString(25, estudiante.getProblemasCompraComida());
			}
			
			pstm.executeUpdate();
			
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
	}

	@Override
	public Estudiante obtenerEstudiantePorCedula(String cedula) {
		ResultSet rs = null;
		List<Estudiante> lista = new ArrayList<>();
		try {
			con = fuente.getConnection();
			pstm = con.prepareStatement(SELECT_BY_ID);
			pstm.setString(1, cedula);
			rs = pstm.executeQuery();
			while (rs.next()) {
				Estudiante est = new Estudiante();
				est.setCedula(rs.getString(1));
				est.setApellidos(rs.getString(2));
				est.setNombres(rs.getString(3));
				est.setEdad(rs.getInt(4));
				est.setSexo(rs.getString(5));
				est.setCelular(rs.getString(6));
				est.setTelFijo(rs.getString(7));
				est.setCorreo(rs.getString(8));
				est.setNumPersonasHogar(rs.getInt(9));
				est.setAlguienPresentaSintomas(rs.getString(10));
				est.setEstudiantePresentaSintomas(rs.getString(11));
				est.setCantidadPersonasSintomas(rs.getInt(12));
				est.setContactoPersonaConCovid(rs.getString(13));
				est.setLat(rs.getDouble(14));
				est.setLng(rs.getDouble(15));
				est.setFechaRegistroPersona(rs.getTimestamp(16).toString());
				est.setUsuario(rs.getString(17));
				if (null != rs.getTimestamp(18)) {
					est.setFechaModificacion(rs.getTimestamp(18).toString());
				}
				est.setCantidadDormitorios(rs.getInt(19));
				est.setCantidadPersonasTrabajan(rs.getInt(20));
				est.setCantidadPersonasEmpleoFijo(rs.getInt(21));
				if (null != rs.getString(22)) {
					est.setPropiedadVivienda(rs.getString(22));
				}else {
					est.setPropiedadVivienda("");
				}
				if (null != rs.getString(23)) {
					est.setBanioCompartido(rs.getString(23));
				}else {
					est.setBanioCompartido("");
				}
				if (null != rs.getString(24)) {
					est.setPagoArriendoServiciosBasicos(rs.getString(24));
				}else {
					est.setPagoArriendoServiciosBasicos("");
				}
				if (null != rs.getString(25)) {
					est.setProblemasCompraComida(rs.getString(25));
				}else {
					est.setProblemasCompraComida("");
				}
				lista.add(est);
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
	public Estudiante obtenerEstudiantePorCedulaUsuario(String cedula, String usuario) {
		ResultSet rs = null;
		List<Estudiante> lista = new ArrayList<>();
		try {
			con = fuente.getConnection();
			pstm = con.prepareStatement(SELECT_BY_ID_USERNAME);
			pstm.setString(1, cedula);
			pstm.setString(2, usuario);
			rs = pstm.executeQuery();
			while (rs.next()) {
				Estudiante est = new Estudiante();
				est.setCedula(rs.getString(1));
				est.setApellidos(rs.getString(2));
				est.setNombres(rs.getString(3));
				est.setEdad(rs.getInt(4));
				est.setSexo(rs.getString(5));
				est.setCelular(rs.getString(6));
				est.setTelFijo(rs.getString(7));
				est.setCorreo(rs.getString(8));
				est.setNumPersonasHogar(rs.getInt(9));
				est.setAlguienPresentaSintomas(rs.getString(10));
				est.setEstudiantePresentaSintomas(rs.getString(11));
				est.setCantidadPersonasSintomas(rs.getInt(12));
				est.setContactoPersonaConCovid(rs.getString(13));
				est.setLat(rs.getDouble(14));
				est.setLng(rs.getDouble(15));
				est.setFechaRegistroPersona(rs.getTimestamp(16).toString());
				est.setUsuario(rs.getString(17));
				if (null != rs.getTimestamp(18)) {
					est.setFechaModificacion(rs.getTimestamp(18).toString());
				}
				est.setCantidadDormitorios(rs.getInt(19));
				est.setCantidadPersonasTrabajan(rs.getInt(20));
				est.setCantidadPersonasEmpleoFijo(rs.getInt(21));
				if (null != rs.getString(22)) {
					est.setPropiedadVivienda(rs.getString(22));
				}else {
					est.setPropiedadVivienda("");
				}
				if (null != rs.getString(23)) {
					est.setBanioCompartido(rs.getString(23));
				}else {
					est.setBanioCompartido("");
				}
				if (null != rs.getString(24)) {
					est.setPagoArriendoServiciosBasicos(rs.getString(24));
				}else {
					est.setPagoArriendoServiciosBasicos("");
				}
				if (null != rs.getString(25)) {
					est.setProblemasCompraComida(rs.getString(25));
				}else {
					est.setProblemasCompraComida("");
				}
				lista.add(est);
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
	public int eliminarEstudiante(String cedula) {
		int count = 0;
		try {
			con = fuente.getConnection();
			pstm = con.prepareStatement(DELETE_ESTUDIANTE);
			pstm.setString(1, cedula);
			count = pstm.executeUpdate();
			
		} catch (SQLException e) {
			e.printStackTrace();
		}catch (Exception e) {
			// TODO: handle exception
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
		
		if (count > 0) {
			return count;
		}else {
			return 0;
		}
		
	}

	@Override
	public int actualizarEstudiante(Estudiante estudiante) {
		int count = 0;
		SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date fechaFormato = null;
		Date fechaModFormato = null;
		Calendar calendar = Calendar.getInstance();
	
		try {
			fechaFormato = formato.parse(estudiante.getFechaRegistroPersona());
			calendar.setTime(fechaFormato);
			System.out.println("FECHA OBTENIDA: " + estudiante.getFechaRegistroPersona());
			Timestamp timeStamp = new Timestamp(calendar.getTimeInMillis());
			con = fuente.getConnection();
			pstm = con.prepareStatement(UPDATE_ESTUDIANTE);
			pstm.setString(1, estudiante.getApellidos());
			pstm.setString(2, estudiante.getNombres());
			pstm.setInt(3, estudiante.getEdad());
			pstm.setString(4, estudiante.getSexo());
			pstm.setString(5, estudiante.getCelular());
			pstm.setString(6, estudiante.getTelFijo());
			pstm.setString(7, estudiante.getCorreo());
			pstm.setInt(8, estudiante.getNumPersonasHogar());
			pstm.setString(9, estudiante.getAlguienPresentaSintomas());
			pstm.setString(10, estudiante.getEstudiantePresentaSintomas());
			pstm.setInt(11, estudiante.getCantidadPersonasSintomas());
			pstm.setString(12, estudiante.getContactoPersonaConCovid());
			pstm.setDouble(13, estudiante.getLat());
			pstm.setDouble(14, estudiante.getLng());
			pstm.setTimestamp(15, timeStamp);
			fechaModFormato = formato.parse(estudiante.getFechaModificacion());
			calendar.setTime(fechaModFormato);
			System.out.println("FECHA MODIFICACION: " + estudiante.getFechaRegistroPersona());
			Timestamp timeStampMod = new Timestamp(calendar.getTimeInMillis());
			pstm.setTimestamp(16, timeStampMod);
			pstm.setString(17, estudiante.getUsuario());
			pstm.setInt(18, estudiante.getCantidadDormitorios());
			pstm.setInt(19, estudiante.getCantidadPersonasTrabajan());
			pstm.setInt(20, estudiante.getCantidadPersonasEmpleoFijo());
			pstm.setString(21, estudiante.getPropiedadVivienda());
			pstm.setString(22, estudiante.getBanioCompartido());
			pstm.setString(23, estudiante.getPagoArriendoServiciosBasicos());
			pstm.setString(24, estudiante.getProblemasCompraComida());
			pstm.setString(25, estudiante.getCedula());
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
	public List<String> obtenerCedulasEstudiantes() {
		ResultSet rs = null;
		List<String> listaCedulas = new ArrayList<String>();
		try {
			con = fuente.getConnection();
			pstm = con.prepareStatement(SELECT_CEDULAS_ESTUDIANTES);
			rs = pstm.executeQuery();

			while (rs.next()) {
				listaCedulas.add(rs.getString(1));
			}
			rs.close();
		}catch (SQLException e) {
			System.out.println("SQLException");
			e.printStackTrace();
		}catch (Exception e) {
			// TODO: handle exception
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
		return listaCedulas;
	}

	@Override
	public List<Coordenada> obtenerCoordenadas() {
		ResultSet rs = null;
		List<Coordenada> listCoordenadas = new ArrayList<Coordenada>();
		try {
			con = fuente.getConnection();
			pstm = con.prepareStatement(SELECT_COORDENADAS);
			rs = pstm.executeQuery();

			while (rs.next()) {
				Coordenada coord = new Coordenada();
				coord.setNombreEstudiante(rs.getString(1));
				coord.setApellidoEstudiante(rs.getString(2));
				coord.setCedulaEstudiante(rs.getString(3));
				coord.setCelular(rs.getString(4));
				coord.setLatitud(rs.getDouble(5));
				coord.setLongitud(rs.getDouble(6));
				coord.setAlguienSintomas(rs.getString(7));
				coord.setEstudianteSintomas(rs.getString(8));
				coord.setPersonasEnHogar(rs.getInt(9));
				coord.setPersonasConSintomas(rs.getInt(10));
				listCoordenadas.add(coord);
			}
			rs.close();
		}catch (SQLException e) {
			System.out.println("SQLException");
			e.printStackTrace();
		}catch (Exception e) {
			// TODO: handle exception
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
		return listCoordenadas;
	}
	
	@Override
	public Coordenada obtenerCoordenadaPorCI(String cedula) {
		ResultSet rs = null;
		List<Coordenada> listaCoord = new ArrayList<Coordenada>();
		try {
			con = fuente.getConnection();
			pstm = con.prepareStatement(SELECT_COORD_BY_CI);
			pstm.setString(1, cedula);
			rs = pstm.executeQuery();

			while (rs.next()) {
				Coordenada coord = new Coordenada();
				coord.setNombreEstudiante(rs.getString(1));
				coord.setApellidoEstudiante(rs.getString(2));
				coord.setCedulaEstudiante(rs.getString(3));
				coord.setCelular(rs.getString(4));
				coord.setLatitud(rs.getDouble(5));
				coord.setLongitud(rs.getDouble(6));
				coord.setAlguienSintomas(rs.getString(7));
				coord.setEstudianteSintomas(rs.getString(8));
				coord.setPersonasEnHogar(rs.getInt(9));
				coord.setPersonasConSintomas(rs.getInt(10));
				listaCoord.add(coord);
			}
			rs.close();
		}catch (SQLException e) {
			System.out.println("SQLException");
			e.printStackTrace();
		}catch (Exception e) {
			// TODO: handle exception
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
		if (listaCoord.size() > 0) {
			System.out.println("Coordenada obtenida de: " + listaCoord.get(0).getApellidoEstudiante());
			return listaCoord.get(0);
		}else {
			System.out.println("LISTA VACÍA");
			return null;
		}
	}
	
	@Override
	public Coordenada obtenerCoordPorApeNom(String apellidosNombres) {
		ResultSet rs = null;
		List<Coordenada> listaCoord = new ArrayList<Coordenada>();
		try {
			con = fuente.getConnection();
			pstm = con.prepareStatement(SELECT_COORD_BY_APE_NOM);
			pstm.setString(1, apellidosNombres);
			rs = pstm.executeQuery();

			while (rs.next()) {
				Coordenada coord = new Coordenada();
				coord.setNombreEstudiante(rs.getString(1));
				coord.setApellidoEstudiante(rs.getString(2));
				coord.setCedulaEstudiante(rs.getString(3));
				coord.setCelular(rs.getString(4));
				coord.setLatitud(rs.getDouble(5));
				coord.setLongitud(rs.getDouble(6));
				coord.setAlguienSintomas(rs.getString(7));
				coord.setEstudianteSintomas(rs.getString(8));
				coord.setPersonasEnHogar(rs.getInt(9));
				coord.setPersonasConSintomas(rs.getInt(10));
				listaCoord.add(coord);
			}
			rs.close();
		}catch (SQLException e) {
			System.out.println("SQLException");
			e.printStackTrace();
		}catch (Exception e) {
			// TODO: handle exception
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
		if (listaCoord.size() > 0) {
			return listaCoord.get(0);
		}else {
			return null;
		}
	}
	
	@Override
	public boolean obtenerEstudiantePorUsuario(String userName) {
		ResultSet rs = null;
		List<String> lista = new ArrayList<>();
		try {
			con = fuente.getConnection();
			pstm = con.prepareStatement(SELECT_USUARIOS_ESTUDIANTES_POR_USUARIO);
			pstm.setString(1, userName);
			rs = pstm.executeQuery();

			while (rs.next()) {
				lista.add(rs.getString(1));
			}
			rs.close();
		}catch (SQLException e) {
			System.out.println("SQLException");
			e.printStackTrace();
		}catch (Exception e) {
			// TODO: handle exception
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
		if (lista.size() > 0) {
			return true;
		}else {
			return false;
		}
	}

}
