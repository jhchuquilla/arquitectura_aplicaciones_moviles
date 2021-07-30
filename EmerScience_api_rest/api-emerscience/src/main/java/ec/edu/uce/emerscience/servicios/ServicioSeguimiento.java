package ec.edu.uce.emerscience.servicios;

import java.util.List;

import ec.edu.uce.emerscience.dto.SeguimientoFamiliar;

public interface ServicioSeguimiento {

	void registrarDiaSeguimiento(SeguimientoFamiliar seguimiento);
	
	SeguimientoFamiliar obtenerSeguimientoCIFecha(String cedulaFam, String fechaRegistro);
	
	int obtenerNumeroRegistrosFamiliar(String cedulaFam);
	
	List<SeguimientoFamiliar> obtenerListaSeguimientos(String cedulaFamiliar);

}
