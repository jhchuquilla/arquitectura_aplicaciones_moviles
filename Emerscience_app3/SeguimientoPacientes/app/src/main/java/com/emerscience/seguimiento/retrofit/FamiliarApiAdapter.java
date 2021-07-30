package com.emerscience.seguimiento.retrofit;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class FamiliarApiAdapter {

    private static FamiliarApiService FAMILIAR_API_SERVICE;

    public static FamiliarApiService getFamiliarApiService(){
        String baseUrl = "http://200.12.169.70:8080/api-emerscience/familiares/";

        if (FAMILIAR_API_SERVICE == null) {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(baseUrl)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            FAMILIAR_API_SERVICE = retrofit.create(FamiliarApiService.class);
        }
        return FAMILIAR_API_SERVICE;
    }

}
