package com.emerscience.utils;

import android.content.Context;
import android.os.AsyncTask;

import com.emerscience.pojos.Estudiante;
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

public class AsyncTaskBucarInformante extends AsyncTask<String, Void, Estudiante> {

    private Context context;

    public AsyncTaskBucarInformante(Context context) {
        this.context = context;
    }

    @Override
    protected Estudiante doInBackground(String... strings) {
        String cedula = strings[0];
        String usuario = strings[1];
        Gson gson = new Gson();
        HttpClient httpClient = new DefaultHttpClient();
        HttpResponse response;
        String responseString = "";
        Estudiante est = new Estudiante();
        try {
            HttpGet httpGet = new HttpGet("http://200.12.169.70:8080/api-emerscience/estudiantes/" + cedula + "/" + usuario);
            httpGet.setHeader(HttpHeaders.AUTHORIZATION, Utileria.obtenerToken(context));
            response = httpClient.execute(httpGet);

            StatusLine statusLine = response.getStatusLine();

            if (statusLine.getStatusCode() == HttpStatus.SC_OK){
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                response.getEntity().writeTo(out);
                responseString = out.toString();
                System.out.println(responseString);
                est = gson.fromJson(responseString, Estudiante.class);
            }else {
                if (statusLine.getStatusCode() == HttpStatus.SC_NO_CONTENT){
                    est = null;
                }else {
                    response.getEntity().getContent().close();
                    throw new IOException(statusLine.getReasonPhrase());
                }
            }
        } catch (ClientProtocolException e) {
            //TODO Handle problems..
        } catch (IOException e) {
            //TODO Handle problems..
        }

        return est;
    }
}
