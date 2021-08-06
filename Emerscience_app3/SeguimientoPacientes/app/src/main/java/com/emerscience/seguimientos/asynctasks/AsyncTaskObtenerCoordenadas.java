package com.emerscience.seguimientos.asynctasks;

import android.content.Context;
import android.os.AsyncTask;

import com.emerscience.seguimientos.pojos.Coordenada;
import com.emerscience.seguimientos.utils.Utileria;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.apache.http.HttpHeaders;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class AsyncTaskObtenerCoordenadas extends AsyncTask<Void, Void, List<Coordenada>> {

    private Context context;

    public AsyncTaskObtenerCoordenadas(Context context){
        this.context = context;
    }

    @Override
    protected List<Coordenada> doInBackground(Void... voids) {

        Gson gson = new Gson();
        HttpClient httpClient = new DefaultHttpClient();
        HttpResponse response;
        String responseString = "";
        List<Coordenada> listCoordenadas = new ArrayList<>();

        try{
            HttpGet httpGet = new HttpGet("http://200.12.169.70:8080/api-emerscience/" + "estudiantes" + "/coordenadas");
            httpGet.setHeader(HttpHeaders.AUTHORIZATION, Utileria.obtenerToken(context));
            response = httpClient.execute(httpGet);
            StatusLine statusLine = response.getStatusLine();
            if (statusLine.getStatusCode() == HttpStatus.SC_OK){
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                response.getEntity().writeTo(out);
                responseString = out.toString();
                System.out.println(responseString);
                Type listType = new TypeToken<List<Coordenada>>(){}.getType();
                listCoordenadas = gson.fromJson(responseString, listType);
                System.out.println("CANTIDAD DE COORDENADAS" + listCoordenadas.size());
            }else {
                response.getEntity().getContent().close();
                throw new IOException(statusLine.getReasonPhrase());
            }
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return listCoordenadas;
    }

}
