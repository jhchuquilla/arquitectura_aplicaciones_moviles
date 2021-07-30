package com.emerscience.io;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;

public interface EstudianteApiService {
    @GET("cedulas")
    Call<List<String>> obtenerCedulasEStudiantes(@Header("Authorization") String token);
}
