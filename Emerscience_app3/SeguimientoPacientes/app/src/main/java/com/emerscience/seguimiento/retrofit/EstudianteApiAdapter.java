package com.emerscience.seguimiento.retrofit;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class EstudianteApiAdapter {

    private static EstudianteApiService ESTUDIANTE_API_SERVICE;

    public static EstudianteApiService getEstudianteApiService(){
        String baseUrl = "http://200.12.169.70:8080/api-emerscience/estudiantes/";

        if (ESTUDIANTE_API_SERVICE == null) {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(baseUrl)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            ESTUDIANTE_API_SERVICE = retrofit.create(EstudianteApiService.class);
        }
        return ESTUDIANTE_API_SERVICE;
    }

}
