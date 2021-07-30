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

import ec.edu.uce.emerscience.dto.CondicionSalud;
import ec.edu.uce.emerscience.dto.CondicionSaludBd;
import ec.edu.uce.emerscience.dto.CondicionesFamiliares;

@ApplicationScoped
public class ServicioCondicionesImpl implements ServicioCondiciones {

	static final String SELECT_ALL = "SELECT * FROM condiciones;";
	static final String SELECT_CONDICIONES_FAMILIARES_POR_CEDULA_FAMILIAR_CODIGO = "SELECT * FROM condiciones_familiares "
			+ "WHERE f_ci = ? AND cod_cond = ?;";
	static final String SELECT_CONDICIONES_FAMILIARES_POR_CEDULA_FAMILIAR = "SELECT * FROM condiciones_familiares "
			+ "WHERE f_ci = ? ;";
	static final String INSERT_CONDICIONES_FAMILIARES = "INSERT INTO condiciones_familiares VALUES (?,?);";
	
	private PreparedStatement pstm = null;
	private Connection con = null;

	@Resource(name = "jdbc/conexionDB")
	private DataSource fuente;
	
	@Override
	public List<CondicionSaludBd> obtenerCondicionesCatalogo() {
		ResultSet rs = null;
		List<CondicionSaludBd> listaCondiciones = new ArrayList<CondicionSaludBd>();
		try {
			con = fuente.getConnection();
			pstm = con.prepareStatement(SELECT_ALL);
			rs = pstm.executeQuery();

			while (rs.next()) {
				CondicionSaludBd sintoma = new CondicionSaludBd();
				sintoma.setCodigoCondicion(rs.getInt(1));
				sintoma.setNombreCondicion(rs.getString(2));
				listaCondiciones.add(sintoma);
			}
			rs.close();
		}catch (SQLException e) {
			e.printStackTrace();
		}catch (Exception e) {
			// TODO: handle exception
		} finally {
			
			try {
				if (pstm != null) {
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
		return listaCondiciones;
	}
	
	@Override
	public List<CondicionesFamiliares> obtenerCondicionesFamiliaresPorCedula(String cedula){
		ResultSet rs = null;
		List<CondicionesFamiliares> listaCondiciones = new ArrayList<>();
		try {
			con = fuente.getConnection();
			pstm = con.prepareStatement(SELECT_CONDICIONES_FAMILIARES_POR_CEDULA_FAMILIAR);
			pstm.setString(1, cedula);
			rs = pstm.executeQuery();
			while (rs.next()) {
				CondicionesFamiliares condFam = new CondicionesFamiliares();
				condFam.setCedulaFamiliar(rs.getString(1));
				condFam.setCodigoCondicion(rs.getInt(2));
				listaCondiciones.add(condFam);
			}
			rs.close();
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
		if (listaCondiciones.size() > 0) {
			return listaCondiciones;
		}else {
			return null;
		}
	}
	
	@Override
	public List<CondicionSalud> obtenerCondicionSaludPorCedula(String cedula){
		try {
			List<CondicionSaludBd> catalogoCondiciones = obtenerCondicionesCatalogo(); //catalogo de condiciones en BD
			
			//condiciones de familiar por cedula del mismo alamcenadas en tabla de cruce en BD
			List<CondicionesFamiliares> listaCondicionesFam = obtenerCondicionesFamiliaresPorCedula(cedula);
			
			//Lista que se tiene que devolver al cliente
			List<CondicionSalud> lista = new ArrayList<>();
			for (CondicionesFamiliares condicionFam : listaCondicionesFam) {
				for (CondicionSaludBd condicionSaludBd : catalogoCondiciones) {
					if (condicionFam.getCodigoCondicion() == condicionSaludBd.getCodigoCondicion()) {
						CondicionSalud cond = new CondicionSalud();
						cond.setCedulaFam(condicionFam.getCedulaFamiliar());
						cond.setCondicionSalud(condicionSaludBd.getNombreCondicion());
						lista.add(cond);
					}
				}
			}
			if (!lista.isEmpty()) {
				return lista;
			}else {
				return null;
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
			return null;
		}
	}

	@Override
	public void registrarCondicionSalud(List<CondicionSalud> listaCondiciones) {

		try {
			for (CondicionSalud condicion : listaCondiciones) {
				for (CondicionSaludBd condicionBd : obtenerCondicionesCatalogo()) {
					if ((condicion.getCondicionSalud().toUpperCase()).equals(condicionBd.getNombreCondicion())) {
						if (!existeCondicionFamiliar(condicion.getCedulaFam(), condicionBd.getCodigoCondicion())) {
							con = fuente.getConnection();
							pstm = con.prepareStatement(INSERT_CONDICIONES_FAMILIARES);
							pstm.setString(1, condicion.getCedulaFam());
							pstm.setInt(2, condicionBd.getCodigoCondicion());
							pstm.executeUpdate();
						}
					}
				}
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			// TODO: handle exception
		} finally {
			try {
				if (pstm != null) {
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
	
	private boolean existeCondicionFamiliar(String cedulaFamiliar, int codigoCondicion) {
		
		ResultSet rs = null;
		List<CondicionesFamiliares> listaCondiciones = new ArrayList<>();
		try {
			con = fuente.getConnection();
			pstm = con.prepareStatement(SELECT_CONDICIONES_FAMILIARES_POR_CEDULA_FAMILIAR_CODIGO);
			pstm.setString(1, cedulaFamiliar);
			pstm.setInt(2, codigoCondicion);
			rs = pstm.executeQuery();
			while (rs.next()) {
				CondicionesFamiliares condFam = new CondicionesFamiliares();
				condFam.setCedulaFamiliar(rs.getString(1));
				condFam.setCodigoCondicion(rs.getInt(2));
				listaCondiciones.add(condFam);
			}
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}catch (Exception e) {
			// TODO: handle exception
		}finally {
			
			try {
				if (pstm != null) {
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
		if (listaCondiciones.size() > 0) {
			return true;
		}else {
			return false;
		}
		
	}

}
