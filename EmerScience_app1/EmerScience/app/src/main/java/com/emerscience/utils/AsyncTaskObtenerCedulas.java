package com.emerscience.utils;

import android.content.Context;
import android.os.AsyncTask;

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

public class AsyncTaskObtenerCedulas extends AsyncTask<String, Void, List<String>> {

    Context context;

    public AsyncTaskObtenerCedulas(Context context) {
        this.context = context;
    }

    @Override
    protected List<String> doInBackground(String... params) {
        String path = params[0];
        Gson gson = new Gson();
        HttpClient httpClient = new DefaultHttpClient();
        HttpResponse response;
        String responseString = "";
        List<String> listaCedulas = new ArrayList<String>();

        try {
            HttpGet httpGet = new HttpGet("http://200.12.169.70:8080/api-emerscience/" + path + "/cedulas");
            httpGet.setHeader(HttpHeaders.AUTHORIZATION, Utileria.obtenerToken(context));
            response = httpClient.execute(httpGet);

            StatusLine statusLine = response.getStatusLine();

            if (statusLine.getStatusCode() == HttpStatus.SC_OK){
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                response.getEntity().writeTo(out);
                responseString = out.toString();
                System.out.println(responseString);
                Type listType = new TypeToken<List<String>>(){}.getType();
                listaCedulas = gson.fromJson(responseString, listType);
                System.out.println("CANTIDAD DE CEDULAS" + listaCedulas.size());
            }else {
                response.getEntity().getContent().close();
                throw new IOException(statusLine.getReasonPhrase());
            }
        } catch (ClientProtocolException e) {
            //TODO Handle problems..
        } catch (IOException e) {
            //TODO Handle problems..
        }
        return listaCedulas;
    }

}
