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

import ec.edu.uce.emerscience.dto.Sintoma;
import ec.edu.uce.emerscience.dto.SintomaRespiratorio;
import ec.edu.uce.emerscience.dto.SintomasBd;
import ec.edu.uce.emerscience.dto.SintomasFamiliares;
import ec.edu.uce.emerscience.dto.SintomasFamiliaresBd;

@ApplicationScoped
public class ServicioSintomaImpl implements ServicioSintoma {

	static final String SELECT_ALL = "SELECT * FROM sintomas;";
	
	static final String SELECT_SINTOMAS_FAMILIARES_POR_CEDULA_FAMILIAR_CODIGO = "SELECT * FROM sintomas_familiares "
			+ "WHERE f_ci = ? AND cod_sint = ?;";
	
	static final String SELECT_SINTOMAS_FAMILIARES_POR_CEDULA = "SELECT * FROM sintomas_familiares "
			+ "WHERE f_ci = ? ;";
	
	static final String INSERT_SINTOMAS_FAMILIARES = "INSERT INTO sintomas_familiares VALUES (?,?);";
	static final String UPDATE_SINTOMAS_FAMILIARES = "UPDATE sintomas_familiares SET cod_sint = ? WHERE f_ci = ?";
	
	private PreparedStatement pstm = null;
	private Connection con = null;

	@Resource(name = "jdbc/conexionDB")
	private DataSource fuente;
	
	@Override
	public List<SintomasBd> obtenerSintomasCatalogo() {
		ResultSet rs = null;
		List<SintomasBd> listaSintomas = new ArrayList<SintomasBd>();
		try {
			con = fuente.getConnection();
			pstm = con.prepareStatement(SELECT_ALL);
			rs = pstm.executeQuery();

			while (rs.next()) {
				SintomasBd sintoma = new SintomasBd();
				sintoma.setCodigoSintoma(rs.getInt(1));
				sintoma.setNombreSintoma(rs.getString(2));
				listaSintomas.add(sintoma);
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
		return listaSintomas;
	}
	
	@Override
	public List<SintomasFamiliaresBd> obtenerSintomasFamiliarPorCedula(String cedulaFamiliar) {
		ResultSet rs = null;
		List<SintomasFamiliaresBd> listaSintomas = new ArrayList<>();
		try {
			con = fuente.getConnection();
			pstm = con.prepareStatement(SELECT_SINTOMAS_FAMILIARES_POR_CEDULA);
			pstm.setString(1, cedulaFamiliar);
			rs = pstm.executeQuery();
			while (rs.next()) {
				SintomasFamiliaresBd sintFam = new SintomasFamiliaresBd();
				sintFam.setCedulaFamiliar(rs.getString(1));
				sintFam.setCodigoSintoma(rs.getInt(2));
				listaSintomas.add(sintFam);
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
		if (listaSintomas.size() > 0) {
			return listaSintomas;
		}else {
			return null;
		}
		
	}
	
	@Override
	public List<Sintoma> obtenerNombresSintomasFamiliares(String cedula){
		
		try {
			List<SintomasBd> listaSintomasCatalogo = obtenerSintomasCatalogo();
			List<SintomasFamiliaresBd> listaSintomasFam = obtenerSintomasFamiliarPorCedula(cedula);
			List<Sintoma> lista = new ArrayList<>();
			for (SintomasFamiliaresBd sintomaFamiliarBd : listaSintomasFam) {
				for (SintomasBd sintomaBd : listaSintomasCatalogo) {
					if (sintomaFamiliarBd.getCodigoSintoma() == sintomaBd.getCodigoSintoma()) {
						Sintoma sintoma = new Sintoma();
						sintoma.setCedulaFam(sintomaFamiliarBd.getCedulaFamiliar());
						sintoma.setSintoma(sintomaBd.getNombreSintoma());
						lista.add(sintoma);
						break;
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
	public void registrarSintomas(List<Sintoma> sintomas) {
		
		try {
			for (Sintoma sintoma : sintomas) {
				for (SintomasBd sintomaBd : obtenerSintomasCatalogo()) {
					if ((sintoma.getSintoma().toUpperCase()).equals(sintomaBd.getNombreSintoma())) {
						if (!existeSintomaFamiliar(sintoma.getCedulaFam(), sintomaBd.getCodigoSintoma())) {
							con = fuente.getConnection();
							pstm = con.prepareStatement(INSERT_SINTOMAS_FAMILIARES);
							pstm.setString(1, sintoma.getCedulaFam());
							pstm.setInt(2, sintomaBd.getCodigoSintoma());
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
	public void registrarSintomasRespiratorios(List<SintomaRespiratorio> listaSintomasRespiratorios) {
		
		try {
			for (SintomaRespiratorio sintomaResp : listaSintomasRespiratorios) {
				
				for (SintomasBd sintomaBd : obtenerSintomasCatalogo()) {
					
					if ((sintomaResp.getSintomaRespiratorio().toUpperCase()).equals(sintomaBd.getNombreSintoma())) {
						if (!existeSintomaFamiliar(sintomaResp.getCedulaFam(), sintomaBd.getCodigoSintoma())) {
							con = fuente.getConnection();
							pstm = con.prepareStatement(INSERT_SINTOMAS_FAMILIARES);
							pstm.setString(1, sintomaResp.getCedulaFam());
							pstm.setInt(2, sintomaBd.getCodigoSintoma());
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
	
	private boolean existeSintomaFamiliar(String cedulaFamiliar, int codigoSintoma) {
		
		ResultSet rs = null;
		List<SintomasFamiliaresBd> listaSintomas = new ArrayList<>();
		try {
			con = fuente.getConnection();
			pstm = con.prepareStatement(SELECT_SINTOMAS_FAMILIARES_POR_CEDULA_FAMILIAR_CODIGO);
			pstm.setString(1, cedulaFamiliar);
			pstm.setInt(2, codigoSintoma);
			rs = pstm.executeQuery();
			while (rs.next()) {
				SintomasFamiliaresBd sintFam = new SintomasFamiliaresBd();
				sintFam.setCedulaFamiliar(rs.getString(1));
				sintFam.setCodigoSintoma(rs.getInt(2));
				listaSintomas.add(sintFam);
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
		if (listaSintomas.size() > 0) {
			return true;
		}else {
			return false;
		}
		
	}

	@Override
	public void actualizarSintomasFamiliares(List<SintomasFamiliares> listaSintomas) {
		try {
			for (SintomasFamiliares sintomaFamiliar : listaSintomas) {
				for (SintomasBd sintomaBd : obtenerSintomasCatalogo()) {
					if ((sintomaFamiliar.getSintoma().toUpperCase()).equals(sintomaBd.getNombreSintoma())) {
						
						con = fuente.getConnection();
						pstm = con.prepareStatement(UPDATE_SINTOMAS_FAMILIARES);
						pstm.setInt(1, sintomaBd.getCodigoSintoma());
						pstm.setString(2, sintomaFamiliar.getCedulaFam());
						pstm.executeUpdate();
						
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

}
