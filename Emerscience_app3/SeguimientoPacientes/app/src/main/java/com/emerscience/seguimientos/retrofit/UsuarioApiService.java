package com.emerscience.seguimientos.retrofit;

import com.emerscience.seguimientos.pojos.Usuario;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;

public interface UsuarioApiService {

    @GET("todos")
    Call<List<Usuario>> obtenerUsuarios(@Header("Authorization") String token);
    @POST("nuevo")
    Call<Boolean> crearUsuario(@Body Usuario usuario, @Header("Authorization") String token);
    @PUT("modificar")
    Call<Boolean> actualizarUsuario(@Body Usuario usuario, @Header("Authorization") String token);
    @PUT("cambio/contrasena")
    Call<Boolean> actualizarContrasena(@Body Usuario usuario, @Header("Authorization") String token);

}
