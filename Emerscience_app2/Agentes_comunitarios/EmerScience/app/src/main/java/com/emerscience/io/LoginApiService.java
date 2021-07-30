package com.emerscience.io;

import com.emerscience.pojos.Estudiante;
import com.emerscience.pojos.Usuario;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface LoginApiService {

    @POST("login")
    Call<Estudiante> login(@Body Usuario usuario);

}
