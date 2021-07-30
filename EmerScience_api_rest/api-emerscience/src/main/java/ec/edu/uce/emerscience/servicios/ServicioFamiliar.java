package ec.edu.uce.emerscience.servicios;

import java.util.List;

import ec.edu.uce.emerscience.dto.Familiar;

public interface ServicioFamiliar {

	List<Familiar> obtenerFamiliares();

	void registrarFamiliar(Familiar familiar);

	List<Familiar> obtenerFamiliarPorCedulaEstudiante(String cedulaEstudiante);
	
	Familiar obtenerFamiliarPorApellidosNombres(String apellidosnombres);
	
	Familiar obtenerFamiliarPorCI(String cedula);

	int eliminarFamiliar(String cedula);

	int actualizarFamiliar(Familiar familiar);
	
	List<String> obtenerCedulasFamiliares();

}
