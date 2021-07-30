package ec.edu.uce.emerscience.servicios;

import java.util.List;

import ec.edu.uce.emerscience.dto.Usuario;

public interface ServicioUsuario {
	Usuario loginUsuario(String nickname, String clave);
	Usuario loginUsuariosLdap(String username);
	boolean crearUsuario(Usuario usuario);
	int actualizarUsuario(Usuario usuario);
	Usuario buscarPorNickName(String nickName);
	Usuario buscarPorId(int id);
	List<Usuario> buscarTodos();
}
