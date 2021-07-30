package ec.edu.uce.emerscience.servicios;

import java.util.List;

import ec.edu.uce.emerscience.dto.Sintoma;
import ec.edu.uce.emerscience.dto.SintomaRespiratorio;
import ec.edu.uce.emerscience.dto.SintomasBd;
import ec.edu.uce.emerscience.dto.SintomasFamiliares;
import ec.edu.uce.emerscience.dto.SintomasFamiliaresBd;

public interface ServicioSintoma {

	List<SintomasBd> obtenerSintomasCatalogo();
	void registrarSintomas(List<Sintoma> sintomas);
	void registrarSintomasRespiratorios(List<SintomaRespiratorio> listaSintomasRespiratorios);
	
	List<SintomasFamiliaresBd> obtenerSintomasFamiliarPorCedula(String cedulaFamiliar);
	
	List<Sintoma> obtenerNombresSintomasFamiliares(String cedula);
	
	void actualizarSintomasFamiliares(List<SintomasFamiliares> listaSintomas);
	
}
