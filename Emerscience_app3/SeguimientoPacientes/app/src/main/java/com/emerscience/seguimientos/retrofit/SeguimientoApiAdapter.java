package com.emerscience.seguimientos.retrofit;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SeguimientoApiAdapter {

    private static SeguimientoApiService SEGUIMIENTO_API_SERVICE;

    public static SeguimientoApiService getSeguimientoApiService(){
        String baseUrl = "http://200.12.169.70:8080/api-emerscience/seguimiento/";

        if (SEGUIMIENTO_API_SERVICE == null) {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(baseUrl)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            SEGUIMIENTO_API_SERVICE = retrofit.create(SeguimientoApiService.class);
        }
        return SEGUIMIENTO_API_SERVICE;
    }

}
