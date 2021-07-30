package com.emerscience.io;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class EstudianteApiAdapter {

    private static EstudianteApiService API_SERVICE;

    public static EstudianteApiService getApiService() {

        String baseUrl = "http://200.12.169.70:8080/api-emerscience/estudiantes/";

        if (API_SERVICE == null) {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(baseUrl)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            API_SERVICE = retrofit.create(EstudianteApiService.class);
        }
        return API_SERVICE;
    }

}
