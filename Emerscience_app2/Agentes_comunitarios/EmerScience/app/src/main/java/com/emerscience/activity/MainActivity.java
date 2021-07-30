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
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.emerscience.io.LoginApiAdapter;
import com.emerscience.pojos.Estudiante;
import com.emerscience.pojos.Usuario;
import com.google.android.material.snackbar.Snackbar;

import org.apache.http.HttpHeaders;

import java.io.File;
import java.util.List;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    final private int CODE_PERMISSION = 0;
    List<Usuario> listaUsuarios;
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
    }

    public void loginServicio(View view) {
        EditText edtMail = findViewById(R.id.edt_email);
        emailIngresado = edtMail.getText().toString();
        EditText edtContrasena = findViewById(R.id.edt_contrasena);
        contrasenaIngresada = edtContrasena.getText().toString();
        if (emailIngresado.isEmpty() | contrasenaIngresada.isEmpty()) {
            Snackbar.make(view, "Acceso no autorizado, revise credenciales", Snackbar.LENGTH_SHORT).show();
        } else {
            Usuario usuario = new Usuario(emailIngresado, contrasenaIngresada);
            ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
            boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();
            if (isConnected) {
                servicioLoginRetrofit(usuario);
            } else {
                Toast.makeText(getApplicationContext(), "Necesita conectarse a internet para poder acceder"
                        , Toast.LENGTH_SHORT).show();
            }
        }
    }

    protected void servicioLoginRetrofit(Usuario usua){
        Call<Estudiante> callLogin = LoginApiAdapter.getLoginService().login(usua);
        callLogin.enqueue(new Callback<Estudiante>() {
            @Override
            public void onResponse(Call<Estudiante> call, Response<Estudiante> response) {
                if (response.isSuccessful()){
                    Estudiante estudiante;
                    estudiante = response.body();
                    String token = response.headers().get(HttpHeaders.AUTHORIZATION);

                    if (!token.isEmpty()){
                        Log.i("TOKEN", token);
                        guardarUsuarioObtenido(estudiante);
                        guardarToken(token);
                        guardarPreferencias();
                        usuario = usua.getUsername();
                        Toast.makeText(getApplicationContext(), "Se ha ingresado correctamente", Toast.LENGTH_SHORT).show();
                        Intent menuActivity = new Intent(getApplicationContext(), MenuActivity.class);
                        startActivity(menuActivity);

                    }else {
                        Toast.makeText(getApplicationContext(), "Acceso no autorizado, revise credenciales", Toast.LENGTH_SHORT).show();
                    }
                }else {
                    Log.i("HTTP_CE", String.valueOf(response.code()));
                    Toast.makeText(getApplicationContext(),"Acceso no permitido, credenciales incorrectas",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Estudiante> call, Throwable t) {
                Log.e("ErrorLogin", t.getMessage());
            }
        });
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
            Usuario usuario = new Usuario(emailIngresado, contrasenaIngresada);
            ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
            boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();
            if (isConnected) {
                servicioLoginRetrofit(usuario);
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
        //editor.putString("contrasena", contrasenaIngresada);
        editor.commit();
    }

    private void guardarUsuarioObtenido(Estudiante estudiante){
        SharedPreferences preferences = getSharedPreferences("UsuarioObtenido", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("apellidos", estudiante.getApellidos());
        editor.putString("nombres", estudiante.getNombres());
        editor.putString("cedula", estudiante.getCedula());
        editor.putString("correo", estudiante.getCorreo());
        editor.putBoolean("usuarioAD",estudiante.isUsuarioAD());
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
        //int permisoAlmacenamiento = ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (permisoUbicacion != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, CODE_PERMISSION);
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
