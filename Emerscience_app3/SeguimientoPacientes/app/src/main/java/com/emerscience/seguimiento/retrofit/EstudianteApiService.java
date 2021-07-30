package com.emerscience.seguimiento.retrofit;

import com.emerscience.seguimiento.pojos.Coordenada;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Path;

public interface EstudianteApiService {

    @GET("coordenadas/cedula/{cedula}")
    Call<Coordenada> obtenerCoordPorCI(@Path("cedula") String cedula, @Header("Authorization") String token);

    @GET("coordenadas/{apeNom}")
    Call<Coordenada> obtenerCoordPorApeNom(@Path("apeNom") String apellidosNombres, @Header("Authorization") String token);

}
