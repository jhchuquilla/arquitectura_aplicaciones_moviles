package com.emerscience.seguimiento.retrofit;

import com.emerscience.seguimiento.pojos.Seguimiento;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Path;

public interface SeguimientoApiService {

   /* @GET("{cedulaEstudiante}")
    Call<List<Familiar>> obtenerFamiliaresPorCedulaEstudiante(@Path("cedulaEstudiante") String cedulaEstudiante
            , @Header("Authorization") String token);

*/
   @GET("numeroRegistros/{cedula}")
   Call<Integer> obtenerRegistros(@Path("cedula") String cedula, @Header("Authorization") String token);

   @GET("all/{cedulaFamiliar}")
   Call<List<Seguimiento>> obtenerListaRegistrosSeguimiento(@Path("cedulaFamiliar") String cedulaFamiliar, @Header("Authorization") String token);

}
