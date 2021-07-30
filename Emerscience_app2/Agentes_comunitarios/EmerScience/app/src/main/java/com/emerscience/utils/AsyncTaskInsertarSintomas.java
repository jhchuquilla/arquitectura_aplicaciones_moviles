package com.emerscience.utils;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.emerscience.activity.DatosFamiliaresActivity;
import com.emerscience.pojos.CondicionSalud;
import com.emerscience.pojos.Sintoma;
import com.emerscience.pojos.SintomaRespiratorio;
import com.google.gson.Gson;

import org.apache.http.HttpHeaders;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HTTP;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

public class AsyncTaskInsertarSintomas extends AsyncTask<List, Void, String> {

    List<CondicionSalud> listaCondicionesSalud;
    Context context;

    public AsyncTaskInsertarSintomas(Context context) {
        this.context = context;
    }

    @Override
    protected String doInBackground(List... lists) {

        List<Sintoma> listaSintomas = lists[0];
        listaCondicionesSalud = lists[1];
        Gson gson = new Gson();
        HttpClient httpClient = new DefaultHttpClient();
        HttpResponse response;
        String responseString = "";
        boolean respuesta = false;

        try {
            String json = gson.toJson(listaSintomas);
            System.out.println("JSON: " + json);
            HttpPost httpPost = new HttpPost("http://200.12.169.70:8080/api-emerscience/sintomas");
            StringEntity se = new StringEntity(json);
            System.out.println("String Entity: " + se.toString());
            se.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
            httpPost.setEntity(se);
            httpPost.setHeader(HttpHeaders.AUTHORIZATION, Utileria.obtenerToken(context));
            response = httpClient.execute(httpPost);

            StatusLine statusLine = response.getStatusLine();

            if (statusLine.getStatusCode() == HttpStatus.SC_OK){
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                response.getEntity().writeTo(out);
                responseString = out.toString();
                respuesta = gson.fromJson(responseString, Boolean.class);
                Log.i("RESPUESTA SINTOMAS", String.valueOf(respuesta));
            }else {
                response.getEntity().getContent().close();
                throw new IOException(statusLine.getReasonPhrase());
            }
        } catch (ClientProtocolException e) {
            //TODO Handle problems..
        } catch (IOException e) {
            //TODO Handle problems..
        }
        return responseString;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);

        new AsyncTaskInsertarCondicionesFamiliar(context).execute(listaCondicionesSalud);

    }
}
