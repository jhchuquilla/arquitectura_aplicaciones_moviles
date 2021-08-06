package com.emerscience.seguimientos.retrofit;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class UsuarioApiAdapter {

    private static UsuarioApiService USUARIO_API_SERVICE;

    public static UsuarioApiService getUsuarioApiService(){
        String baseUrl = "http://200.12.169.70:8080/api-emerscience/usuarios/";

        if (USUARIO_API_SERVICE == null){
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(baseUrl)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            USUARIO_API_SERVICE = retrofit.create(UsuarioApiService.class);
        }
        return USUARIO_API_SERVICE;
    }

}
