package ec.edu.uce.emerscience.servicios;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.annotation.Resource;
import javax.enterprise.context.ApplicationScoped;
import javax.sql.DataSource;

import ec.edu.uce.emerscience.dto.Direccion;

@ApplicationScoped
public class ServicioDireccionImpl implements ServicioDireccion{

	static final String INSERTAR_DIRECCION = "INSERT INTO direcciones (es_ci, dir_cod_post, dir_parroquia, dir_canton, "
			+ "dir_provincia, dir_pais, es_alg_ps, es_presen_sint) VALUES (?,?,?,?,?,?,?,?);";
	
	private PreparedStatement pstm = null;
	private Connection con = null;

	@Resource(name = "jdbc/conexionDB")
	private DataSource fuente;
	
	@Override
	public boolean registrarDireccion(Direccion direccion) {
		
		try {
			con = fuente.getConnection();
			pstm = con.prepareStatement(INSERTAR_DIRECCION);
			pstm.setString(1, direccion.getCedulaEstudiante());
			pstm.setString(2, direccion.getCodigoPostal());
			pstm.setString(3, direccion.getParroquia());
			pstm.setString(4, direccion.getCanton());
			pstm.setString(5, direccion.getProvincia());
			pstm.setString(6, direccion.getPais());
			pstm.setString(7, direccion.getAlguienSintomas());
			pstm.setString(8, direccion.getEstudianteSintomas());
			
			pstm.executeUpdate();
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		} finally {
			try {
				if (pstm != null) {
					pstm.close();
					con.close();
				}
				return true;
			} catch (SQLException e) {}
			try {
				if (con != null) {
					con.close();
					return true;
				}
			} catch (SQLException e) {
				e.printStackTrace();
				return false;
			}
		}
	}//FIN METODO REGISTRAR DIRECCION

}
