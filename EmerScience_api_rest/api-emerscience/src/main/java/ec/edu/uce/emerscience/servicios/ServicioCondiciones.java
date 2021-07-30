package ec.edu.uce.emerscience.servicios;

import java.util.List;

import ec.edu.uce.emerscience.dto.CondicionSalud;
import ec.edu.uce.emerscience.dto.CondicionSaludBd;
import ec.edu.uce.emerscience.dto.CondicionesFamiliares;

public interface ServicioCondiciones {

	List<CondicionSaludBd> obtenerCondicionesCatalogo();
	List<CondicionesFamiliares> obtenerCondicionesFamiliaresPorCedula(String cedula);
	List<CondicionSalud> obtenerCondicionSaludPorCedula(String cedula);
	void registrarCondicionSalud(List<CondicionSalud> listaCondiciones);
	
}
