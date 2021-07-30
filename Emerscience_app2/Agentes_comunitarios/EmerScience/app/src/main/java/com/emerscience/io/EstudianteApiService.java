package com.emerscience.io;

import com.emerscience.pojos.Estudiante;
import com.emerscience.pojos.Familiar;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface EstudianteApiService {
    @GET("cedulas")
    Call<List<String>> obtenerCedulasEStudiantes(@Header("Authorization") String token);

    @GET("{cedula}")
    Call<Estudiante> obtenerEstudiantePorCedula(@Path("cedula")String cedula, @Header("Authorization") String token);

    @GET("{cedulaEstudiante}")
    Call<List<Familiar>> obtenerFamiliaresPorCedulaEstudiante(@Path("cedulaEstudiante")String cedulaEstudiante, @Header("Authorization") String token);

    @PUT("actualizar")
    Call<Boolean> actualizarEstudiante(@Header("Authorization") String token, @Body Estudiante estudiante);
}
