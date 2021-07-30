package ec.edu.uce.emerscience.servicios;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.enterprise.context.ApplicationScoped;
import javax.sql.DataSource;

import ec.edu.uce.emerscience.dto.SeguimientoFamiliar;

@ApplicationScoped
public class ServicioSeguimientoImpl implements ServicioSeguimiento {

	static final String INSERT_SEGUIMIENTO = "INSERT INTO seguimiento_familiar (f_ci, seg_fecha, seg_fc_resp, "
			+ "seg_temp, seg_fc_card, seg_sat_oxg, seg_dif_resp, seg_fatiga, seg_dif_hblr, seg_delirio) "
			+ "VALUES (?,?,?,?,?,?,?,?,?,?);";
	// static final String SELECT_SEGUIMIENTO_CI_FECHA = "SELECT * FROM
	// seguimiento_familiar WHERE f_ci = ? ;";//AND seg_fecha = ?;";
	static final String SELECT_SEGUIMIENTO_CI_FECHA = "SELECT * FROM seguimiento_familiar WHERE f_ci = ? AND seg_fecha = ?;";
	static final String SELECT_SEGUIMIENTO_NUMERO = "SELECT count(*) FROM seguimiento_familiar WHERE f_ci = ? ;";
	static final String SELECT_SEGUIMIENTO_BY_CEDULA = "SELECT * FROM seguimiento_familiar WHERE f_ci = ?;";

	private PreparedStatement pstm = null;
	private Connection con = null;

	@Resource(name = "jdbc/conexionDB")
	private DataSource fuente;

	@Override
	public void registrarDiaSeguimiento(SeguimientoFamiliar seguimiento) {
		SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd");
		Date fechaFormato = null;
		Calendar calendar = Calendar.getInstance();
		try {
			fechaFormato = formato.parse(seguimiento.getFechaRegistroSeguimiento());
			calendar.setTime(fechaFormato);
			System.out.println("FECHA OBTENIDA: " + seguimiento.getFechaRegistroSeguimiento());
			Timestamp timeStamp = new Timestamp(calendar.getTimeInMillis());
			con = fuente.getConnection();
			pstm = con.prepareStatement(INSERT_SEGUIMIENTO);
			pstm.setString(1, seguimiento.getCedulaFamiliar());
			pstm.setTimestamp(2, timeStamp);
			pstm.setInt(3, seguimiento.getFrecRespiratoria());
			pstm.setDouble(4, seguimiento.getTemperaturaCorporal());
			pstm.setInt(5, seguimiento.getFrecCardiaca());
			pstm.setInt(6, seguimiento.getSaturacionOxigeno());
			pstm.setString(7, seguimiento.getDificultadRespirar());
			pstm.setString(8, seguimiento.getFatiga());
			pstm.setString(9, seguimiento.getDificultadHablar());
			pstm.setString(10, seguimiento.getDelirio());

			pstm.executeUpdate();

		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			// TODO: handle exception
		} finally {

			try {
				if (pstm != null) {
					pstm.close();
					con.close();
				}
			} catch (SQLException e) {
			}
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
	public SeguimientoFamiliar obtenerSeguimientoCIFecha(String cedulaFam, String fechaRegistro) {
		ResultSet rs = null;
		List<SeguimientoFamiliar> lista = new ArrayList<>();
		try {
			con = fuente.getConnection();
			pstm = con.prepareStatement(SELECT_SEGUIMIENTO_CI_FECHA);
			pstm.setString(1, cedulaFam);
			pstm.setDate(2, java.sql.Date.valueOf(fechaRegistro));
			rs = pstm.executeQuery();
			while (rs.next()) {
				SeguimientoFamiliar seg = new SeguimientoFamiliar();
				seg.setId(rs.getInt(1));
				seg.setCedulaFamiliar(rs.getString(2));
				seg.setFechaRegistroSeguimiento(rs.getTimestamp(3).toString());
				seg.setFrecRespiratoria(rs.getInt(4));
				seg.setTemperaturaCorporal(rs.getDouble(5));
				seg.setFrecCardiaca(rs.getInt(6));
				seg.setSaturacionOxigeno(rs.getInt(7));
				seg.setDificultadRespirar(rs.getString(8));
				seg.setFatiga(rs.getString(9));
				seg.setDificultadHablar(rs.getString(10));
				seg.setDelirio(rs.getString(11));

				lista.add(seg);
			}
			rs.close();
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
			} catch (SQLException e) {
			}
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
		} else {
			return null;
		}
	}

	@Override
	public int obtenerNumeroRegistrosFamiliar(String cedulaFam) {
		ResultSet rs = null;
		int numeroRegistros = 0;
		try {
			con = fuente.getConnection();
			pstm = con.prepareStatement(SELECT_SEGUIMIENTO_NUMERO);
			pstm.setString(1, cedulaFam);
			rs = pstm.executeQuery();
			while (rs.next()) {
				numeroRegistros = (rs.getInt(1));
			}
			rs.close();
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
			} catch (SQLException e) {
			}
			try {
				if (con != null) {
					con.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return numeroRegistros;
	}

	@Override
	public List<SeguimientoFamiliar> obtenerListaSeguimientos(String cedulaFamiliar) {
		ResultSet rs = null;
		List<SeguimientoFamiliar> lista = new ArrayList<>();
		try {
			con = fuente.getConnection();
			pstm = con.prepareStatement(SELECT_SEGUIMIENTO_BY_CEDULA);
			pstm.setString(1, cedulaFamiliar);
			rs = pstm.executeQuery();
			while (rs.next()) {
				SeguimientoFamiliar seg = new SeguimientoFamiliar();
				seg.setId(rs.getInt(1));
				seg.setCedulaFamiliar(rs.getString(2));
				seg.setFechaRegistroSeguimiento(rs.getDate(3).toString());
				seg.setFrecRespiratoria(rs.getInt(4));
				seg.setTemperaturaCorporal(rs.getDouble(5));
				seg.setFrecCardiaca(rs.getInt(6));
				seg.setSaturacionOxigeno(rs.getInt(7));
				seg.setDificultadRespirar(rs.getString(8));
				seg.setFatiga(rs.getString(9));
				seg.setDificultadHablar(rs.getString(10));
				seg.setDelirio(rs.getString(11));
				lista.add(seg);
			}
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {

			try {
				if (pstm != null) {
					pstm.close();
					con.close();
				}
			} catch (SQLException e) {
			}
			try {
				if (con != null) {
					con.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return lista;
	}

}
