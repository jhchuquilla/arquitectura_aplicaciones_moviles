package com.emerscience.io;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LoginApiAdapter {

    private static LoginApiService LOGIN_SERVICE;

    /**
     * Obtiene una instancia de LoginApiService
     * @return intancia LoginApiService
     */
    public static LoginApiService getLoginService(){
        String baseUrl = "http://200.12.169.70:8080/api-emerscience/";

        if (LOGIN_SERVICE == null) {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(baseUrl)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            LOGIN_SERVICE = retrofit.create(LoginApiService.class);
        }
        return LOGIN_SERVICE;
    }

}
