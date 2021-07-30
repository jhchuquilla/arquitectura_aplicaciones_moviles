package ec.edu.uce.emerscience.servicios;

import java.util.List;

import ec.edu.uce.emerscience.dto.Coordenada;
import ec.edu.uce.emerscience.dto.Estudiante;

public interface ServicioEstudiante {

	List<Estudiante> obtenerEstudiantes();
	void registrarEstudiante(Estudiante estudiante);
	Estudiante obtenerEstudiantePorCedula(String cedula);
	Estudiante obtenerEstudiantePorCedulaUsuario(String cedula, String usuario);
	int eliminarEstudiante(String cedula);
	int actualizarEstudiante(Estudiante estudiante);
	List<String> obtenerCedulasEstudiantes();
	List<Coordenada> obtenerCoordenadas();
	Coordenada obtenerCoordenadaPorCI(String cedula);
	Coordenada obtenerCoordPorApeNom(String apellidosNombres);
	boolean obtenerEstudiantePorUsuario(String userName);

}
