package com.emerscience.seguimientos.asynctasks;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.emerscience.seguimientos.pojos.Seguimiento;
import com.emerscience.seguimientos.utils.Utileria;
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

public class AsyncTaskInsertarSeguimiento extends AsyncTask<Object, Void, Boolean> {

    Context context;

    public AsyncTaskInsertarSeguimiento(Context context){
        this.context = context;
    }

    public static boolean resultado;

    @Override
    protected Boolean doInBackground(Object... objects) {

        Seguimiento est = (Seguimiento) objects[0];
        Gson gson = new Gson();
        HttpClient httpClient = new DefaultHttpClient();
        HttpResponse response;
        String responseString = "";
        boolean respuesta = false;
        try {
            String json = gson.toJson(est);
            System.out.println("JSON: " + json);
            HttpPost httpPost = new HttpPost("http://200.12.169.70:8080/api-emerscience/seguimiento");
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
                Log.i("RESPUESTA SEGUIMIENTO", String.valueOf(respuesta));
                if(respuesta){
                }else{
                }
            }else {
                response.getEntity().getContent().close();
                throw new IOException(statusLine.getReasonPhrase());
            }
        } catch (ClientProtocolException e) {
            //TODO Handle problems..
        } catch (IOException e) {
            //TODO Handle problems..
        }
        resultado = respuesta;
        Log.i("RESULTADO SEGUIMIENTO", String.valueOf(resultado));
        return respuesta;
    }

    @Override
    protected void onPostExecute(Boolean s) {
        super.onPostExecute(s);
    }
}
