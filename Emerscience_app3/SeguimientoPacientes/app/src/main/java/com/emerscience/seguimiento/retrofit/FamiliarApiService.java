package com.emerscience.seguimiento.retrofit;

import com.emerscience.seguimiento.pojos.Familiar;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface FamiliarApiService {

    @GET("{cedulaEstudiante}")
    Call<List<Familiar>> obtenerFamiliaresPorCedulaEstudiante(@Path("cedulaEstudiante") String cedulaEstudiante, @Header("Authorization") String token);
    @POST("actualizar")
    Call<Boolean> actualizarFamiliar(@Body Familiar familiar, @Header("Authorization") String token);
    @GET("apenom/{apenom}")
    Call<Familiar> obtenerFamiliarPorApeNom(@Path("apenom") String cedulaEstudiante, @Header("Authorization") String token);
    @GET("cedula/{cedulaFamiliar}")
    Call<Familiar> obtenerFamiliarPorCedula(@Path("cedulaFamiliar") String cedulaEstudiante, @Header("Authorization") String token);

}
