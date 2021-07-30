package com.emerscience.utils;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.emerscience.activity.DatosFamiliaresActivity;
import com.emerscience.pojos.Familiar;
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

public class AsyncTaskInsertarFamiliares extends AsyncTask<List, Void, String> {

    public static boolean resultadoFamiliares;
    Context context;
    public AsyncTaskInsertarFamiliares(Context context) {
        this.context = context;
    }

    @Override
    protected String doInBackground(List... lists) {
        List<Familiar> lista = lists[0];
        Gson gson = new Gson();
        HttpClient httpClient = new DefaultHttpClient();
        HttpResponse responseFam;
        String responseStringFam = "";
        boolean respuestaFam = false;

        try {
            String jsonFam = gson.toJson(lista);
            System.out.println("JSON: " + jsonFam);
            HttpPost httpPostFam = new HttpPost("http://200.12.169.70:8080/api-emerscience/familiares");
            StringEntity seFam = new StringEntity(jsonFam);
            System.out.println("String Entity: " + seFam.toString());
            seFam.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
            httpPostFam.setEntity(seFam);
            httpPostFam.setHeader(HttpHeaders.AUTHORIZATION, Utileria.obtenerToken(context));
            responseFam = httpClient.execute(httpPostFam);

            StatusLine statusLine = responseFam.getStatusLine();

            Log.e("ESTATUS CODE FAMILIARES", String.valueOf(statusLine.getStatusCode()));

            if (statusLine.getStatusCode() == HttpStatus.SC_OK){
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                responseFam.getEntity().writeTo(out);
                responseStringFam = out.toString();
                respuestaFam = gson.fromJson(responseStringFam, Boolean.class);
                Log.i("RESPUESTA FAMILIARES", String.valueOf(respuestaFam));
            }else {
                responseFam.getEntity().getContent().close();
                throw new IOException(statusLine.getReasonPhrase());
            }
        } catch (ClientProtocolException e) {
            //TODO Handle problems..
        } catch (IOException e) {
            //TODO Handle problems..
        }
        resultadoFamiliares = respuestaFam;
        Log.i("RESULTADO FAMILIARES", String.valueOf(resultadoFamiliares));
        return responseStringFam;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);

        new AsyncTaskInsertarSintomas(context).execute(DatosFamiliaresActivity.listaSintomas, DatosFamiliaresActivity.listaCondiciones);
    }
}
