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

import ec.edu.uce.emerscience.dto.Familiar;

@ApplicationScoped
public class ServicioFamiliarImpl implements ServicioFamiliar {

	static final String SELECT_ALL = "SELECT * FROM familiares;";
	static final String SELECT_BY_ID = "SELECT * FROM familiares WHERE f_ci = ?;";
	static final String SELECT_BY_CED_EST = "SELECT * FROM familiares WHERE es_ci = ?;";
	static final String INSERT_FAMILIAR = "INSERT INTO familiares VALUES (?,?,?,?,?,?,?,?,?,?);";
	
	static final String UPDATE_FAMILIAR= "UPDATE familiares SET f_apellidos = ?, f_nombres = ?, es_ci = ?, f_edad = ?, "
			+ "f_sexo = ?, f_sint = ?, f_dif_rs = ?, f_cond_fam = ?, f_cont_pc = ? WHERE f_ci = ? ;";
	
	static final String DELETE_FAMILIAR = "DELETE FROM familiares WHERE f_ci = ? ;";
	static final String SELECT_CEDULAS_FAMILIARES = "SELECT f_ci from familiares ORDER BY f_ci ASC ;";
	
	static final String SELECT_BY_APE_NOM = "SELECT * FROM familiares WHERE CONCAT(f_apellidos,' ',f_nombres) = ?;";
	private PreparedStatement pstm = null;
	private Connection con = null;

	@Resource(name = "jdbc/conexionDB")
	private DataSource fuente;
	
	@Override
	public List<Familiar> obtenerFamiliares() {
		ResultSet rs = null;
		List<Familiar> listaFam = new ArrayList<Familiar>();
		try {
			con = fuente.getConnection();
			pstm = con.prepareStatement(SELECT_ALL);
			rs = pstm.executeQuery();

			while (rs.next()) {
				Familiar fam = new Familiar();
				fam.setCedulaFam(rs.getString(1));
				fam.setApellidosFam(rs.getString(2));
				fam.setNombresFam(rs.getString(3));
				fam.setCedulaEstudiante(rs.getString(4));
				fam.setEdadFam(rs.getInt(5));
				fam.setSexoFam(rs.getString(6));
				listaFam.add(fam);
			}
			rs.close();
		}catch (SQLException e) {
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
		return listaFam;
	}

	@Override
	public void registrarFamiliar(Familiar familiar) {
		try {
			con = fuente.getConnection();
			pstm = con.prepareStatement(INSERT_FAMILIAR);
			pstm.setString(1, familiar.getCedulaFam());
			pstm.setString(2, familiar.getApellidosFam());
			pstm.setString(3, familiar.getNombresFam());
			pstm.setString(4, familiar.getCedulaEstudiante());
			pstm.setInt(5, familiar.getEdadFam());
			pstm.setString(6, familiar.getSexoFam());
			pstm.setString(7, familiar.getSintomasFam());
			pstm.setString(8, familiar.getDifRespirarSevera());
			pstm.setString(9, familiar.getCondicionesFam());
			pstm.setString(10, familiar.getContactoFamConPersonaCovid());
			
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
	public Familiar obtenerFamiliarPorCI(String cedula) {
		ResultSet rs = null;
		List<Familiar> lista = new ArrayList<>();
		try {
			con = fuente.getConnection();
			pstm = con.prepareStatement(SELECT_BY_ID);
			pstm.setString(1, cedula);
			rs = pstm.executeQuery();
			while (rs.next()) {
				Familiar fam = new Familiar();
				fam.setCedulaFam(rs.getString(1));
				fam.setApellidosFam(rs.getString(2));
				fam.setNombresFam(rs.getString(3));
				fam.setCedulaEstudiante(rs.getString(4));
				fam.setEdadFam(rs.getInt(5));
				fam.setSexoFam(rs.getString(6));
				fam.setSintomasFam(rs.getString(7));
				fam.setDifRespirarSevera(rs.getString(8));
				fam.setCondicionesFam(rs.getString(9));
				fam.setContactoFamConPersonaCovid(rs.getString(10));
				lista.add(fam);
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
		if (lista.size() > 0) {
			return lista.get(0);
		}else {
			return null;
		}
	}
	
	@Override
	public Familiar obtenerFamiliarPorApellidosNombres(String apellidosnombres) {
		ResultSet rs = null;
		List<Familiar> lista = new ArrayList<>();
		try {
			con = fuente.getConnection();
			pstm = con.prepareStatement(SELECT_BY_APE_NOM);
			pstm.setString(1, apellidosnombres);
			rs = pstm.executeQuery();
			while (rs.next()) {
				Familiar fam = new Familiar();
				fam.setCedulaFam(rs.getString(1));
				fam.setApellidosFam(rs.getString(2));
				fam.setNombresFam(rs.getString(3));
				fam.setCedulaEstudiante(rs.getString(4));
				fam.setEdadFam(rs.getInt(5));
				fam.setSexoFam(rs.getString(6));
				fam.setSintomasFam(rs.getString(7));
				fam.setDifRespirarSevera(rs.getString(8));
				fam.setCondicionesFam(rs.getString(9));
				fam.setContactoFamConPersonaCovid(rs.getString(10));
				lista.add(fam);
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
		if (lista.size() > 0) {
			return lista.get(0);
		}else {
			return null;
		}
	}

	@Override
	public List<Familiar> obtenerFamiliarPorCedulaEstudiante(String cedulaEstudiante) {
		ResultSet rs = null;
		List<Familiar> lista = new ArrayList<>();
		try {
			con = fuente.getConnection();
			pstm = con.prepareStatement(SELECT_BY_CED_EST);
			pstm.setString(1, cedulaEstudiante);
			rs = pstm.executeQuery();
			while (rs.next()) {
				Familiar fam = new Familiar();
				fam.setCedulaFam(rs.getString(1));
				fam.setApellidosFam(rs.getString(2));
				fam.setNombresFam(rs.getString(3));
				fam.setCedulaEstudiante(rs.getString(4));
				fam.setEdadFam(rs.getInt(5));
				fam.setSexoFam(rs.getString(6));
				fam.setSintomasFam(rs.getString(7));
				fam.setDifRespirarSevera(rs.getString(8));
				fam.setCondicionesFam(rs.getString(9));
				fam.setContactoFamConPersonaCovid(rs.getString(10));
				lista.add(fam);
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
		if (lista.size() > 0) {
			return lista;
		}else {
			return null;
		}
	}

	
	
	
	@Override
	public int eliminarFamiliar(String cedula) {
		int count = 0;
		try {
			con = fuente.getConnection();
			pstm = con.prepareStatement(DELETE_FAMILIAR);
			pstm.setString(1, cedula);
			count = pstm.executeUpdate();
			
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
		
		if (count > 0) {
			return count;
		}else {
			return 0;
		}

	}

	@Override
	public int actualizarFamiliar(Familiar familiar) {
		int count = 0;
		try {
			con = fuente.getConnection();
			pstm = con.prepareStatement(UPDATE_FAMILIAR);
			pstm.setString(1, familiar.getApellidosFam());
			pstm.setString(2, familiar.getNombresFam());
			pstm.setString(3, familiar.getCedulaEstudiante());
			pstm.setInt(4, familiar.getEdadFam());
			pstm.setString(5, familiar.getSexoFam());
			pstm.setString(6, familiar.getSintomasFam());
			pstm.setString(7, familiar.getDifRespirarSevera());
			pstm.setString(8, familiar.getCondicionesFam());
			pstm.setString(9, familiar.getContactoFamConPersonaCovid());
			pstm.setString(10, familiar.getCedulaFam());
			
			count = pstm.executeUpdate();
			
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
	public List<String> obtenerCedulasFamiliares() {
		ResultSet rs = null;
		List<String> listaCedulas = new ArrayList<String>();
		try {
			con = fuente.getConnection();
			pstm = con.prepareStatement(SELECT_CEDULAS_FAMILIARES);
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

}
