package com.emerscience.seguimiento.retrofit;

import com.emerscience.seguimiento.pojos.Estudiante;
import com.emerscience.seguimiento.pojos.Usuario;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface LoginApiService {

    @POST("login")
    Call<Estudiante> login(@Body Usuario usuario);

    @POST("login/ldap")
    Call<Estudiante> loginUsuariosLdap(@Body Usuario usuario);

}
