package com.emerscience.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import android.os.Environment;
import android.os.Looper;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.emerscience.pojos.Estudiante;
import com.emerscience.pojos.Usuario;
import com.google.gson.Gson;

import org.apache.http.HttpHeaders;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.protocol.HTTP;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.List;
import java.util.ArrayList;
public class MainActivity extends AppCompatActivity {

    final private int CODE_PERMISSION = 0;
    List<Usuario> listaUsuarios;
    boolean usuarioAD = false;
    static String usuario;
    String emailIngresado;
    String contrasenaIngresada;
    boolean registroExistente = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbarFam);
        this.setSupportActionBar(toolbar);
        this.getSupportActionBar().setTitle("");
        solicitarPermisos();
        usuariosGenericos();
//        preferencesLoginWithServiceLdap();
//        preferencesLogin();
    }

    public void loginServicio(View view) {
        EditText edtMail = findViewById(R.id.edt_email);
        emailIngresado = edtMail.getText().toString();
        EditText edtContrasena = findViewById(R.id.edt_contrasena);
        contrasenaIngresada = edtContrasena.getText().toString();
        if (emailIngresado.isEmpty() | contrasenaIngresada.isEmpty()) {
            Toast.makeText(getApplicationContext(), "Usuario y contraseña son campos obligatorios"
                    , Toast.LENGTH_SHORT).show();
            return;
        } else {
            ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
            boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();
            if (isConnected) {
                servicioLoginLdap(emailIngresado, contrasenaIngresada);
            } else {
                Toast.makeText(getApplicationContext(), "Necesita conectarse a internet para poder acceder"
                        , Toast.LENGTH_SHORT).show();
            }
        }
    }

    protected void servicioLoginLdap(final String email, final String pwd) {
        Thread t = new Thread() {
            @Override
            public void run() {
                Looper.prepare(); //For Preparing Message Pool for the child Thread
                Gson gson = new Gson();
                String responseString = "";
                Estudiante estudiante = new Estudiante();
                HttpClient client = new DefaultHttpClient();
                HttpConnectionParams.setConnectionTimeout(client.getParams(), 10000); //Timeout Limit
                HttpResponse response;
                JSONObject json = new JSONObject();
                try {
                    HttpPost post = new HttpPost("http://200.12.169.70:8080/api-emerscience/login");
                    json.put("username", email);
                    json.put("password", pwd);
                    StringEntity se = new StringEntity(json.toString());
                    se.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
                    post.setEntity(se);
                    response = client.execute(post);

                    StatusLine statusLine = response.getStatusLine();

                    /*Checking response */
                    if (statusLine.getStatusCode() == HttpStatus.SC_OK) {
                        String token = response.getFirstHeader(HttpHeaders.AUTHORIZATION).getValue();
                        ByteArrayOutputStream out = new ByteArrayOutputStream();
                        response.getEntity().writeTo(out);
                        responseString = out.toString();
                        estudiante = gson.fromJson(responseString, Estudiante.class);
                        System.out.println(estudiante.getApellidos());
                        System.out.println(estudiante.getNombres());
                        System.out.println(estudiante.getCedula());
                        System.out.println(estudiante.getCorreo());
                        guardarUsuarioObtenido(estudiante);
                        guardarToken(token);

                        if (estudiante.getApellidos() != null){
                            //usuarioAD = true;
                            verificarRegistro();
                            if (registroExistente) {
                                guardarPreferencias();
                                usuario=email;
                                Toast.makeText(getApplicationContext(), "Se ha ingresado correctamente", Toast.LENGTH_SHORT).show();
                                Intent menuActivity = new Intent(getApplicationContext(), MenuActivity.class);
                                startActivity(menuActivity);
                            } else {
                                Toast.makeText(getApplicationContext(), "Usted ya cuenta con un registro anterior, proximamente se habilitará la opción para editar su información", Toast.LENGTH_LONG).show();
                            }

                        }else {
                            Toast.makeText(getApplicationContext(), "Credenciales Incorrectas", Toast.LENGTH_SHORT).show();
                        }
                    }else {
                        Toast.makeText(getApplicationContext(), "Acceso no permitido, credenciales incorrectas",Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Looper.loop(); //Loop in the message queue
            }
        };
        t.start();
    }

    private void preferencesLoginWithServiceLdap() {

        SharedPreferences preferences = getSharedPreferences("credenciales", Context.MODE_PRIVATE);
        emailIngresado = preferences.getString("email", "");
        contrasenaIngresada = preferences.getString("contrasena", "");

        if (emailIngresado.isEmpty() | contrasenaIngresada.isEmpty()) {
            Toast.makeText(getApplicationContext(), "Usuario y contraseña son campos obligatorios"
                    , Toast.LENGTH_SHORT).show();
            return;
        } else {
            ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
            boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();
            if (isConnected) {
                servicioLoginLdap(emailIngresado, contrasenaIngresada);
            } else {
                Toast.makeText(getApplicationContext(), "Necesita conexión a internet para poder acceder"
                        , Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void usuariosGenericos() {
        listaUsuarios = new ArrayList<Usuario>();
        Usuario usuario1 = new Usuario("dpruebas", "Usuario1.2");
        listaUsuarios.add(usuario1);
    }

    private void guardarPreferencias() {
        SharedPreferences preferences = getSharedPreferences("credenciales", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("email", emailIngresado);
        editor.putString("contrasena", contrasenaIngresada);
        editor.commit();
    }

    private void guardarUsuarioObtenido(Estudiante estudiante){
        SharedPreferences preferences = getSharedPreferences("UsuarioObtenido", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("apellidos", estudiante.getApellidos());
        editor.putString("nombres", estudiante.getNombres());
        editor.putString("cedula", estudiante.getCedula());
        editor.putString("correo", estudiante.getCorreo());
        editor.commit();
    }

    private void guardarToken(String token){
        SharedPreferences preferences = getSharedPreferences("token", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("token", token);
        editor.commit();
    }

    private void solicitarPermisos() {
        int permisoUbicacion = ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION);
        int permisoAlmacenamiento = ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (permisoAlmacenamiento != PackageManager.PERMISSION_GRANTED && permisoUbicacion != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.WRITE_EXTERNAL_STORAGE}, CODE_PERMISSION);
            }
        }
    }

    public void verificarRegistro() {
        String file_path = (Environment.getExternalStorageDirectory().toString());
        EditText edtMail = findViewById(R.id.edt_email);
        usuario=edtMail.getText().toString();
        File file = new File(file_path, "log_emer"+usuario);
        if (!file.exists()) {
            registroExistente = true;
        }
    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }
}
