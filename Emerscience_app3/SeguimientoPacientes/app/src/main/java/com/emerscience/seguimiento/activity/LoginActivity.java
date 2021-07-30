package com.emerscience.seguimiento.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.emerscience.seguimiento.pojos.Estudiante;
import com.emerscience.seguimiento.pojos.Usuario;
import com.emerscience.seguimiento.retrofit.LoginApiAdapter;
import com.google.android.material.snackbar.Snackbar;

import org.apache.http.HttpHeaders;

import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    static String usuario;
    String emailIngresado;
    String contrasenaIngresada;
    private String rol;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Toolbar toolbar = findViewById(R.id.toolbarFam);
        this.setSupportActionBar(toolbar);
        Objects.requireNonNull(this.getSupportActionBar()).setTitle("");
        solicitarPermisos();
    }

    public void loginServicio(View view) {
        EditText edtMail = findViewById(R.id.edt_email);
        emailIngresado = edtMail.getText().toString();
        EditText edtContrasena = findViewById(R.id.edt_contrasena);
        contrasenaIngresada = edtContrasena.getText().toString();
        if (emailIngresado.isEmpty() | contrasenaIngresada.isEmpty()) {
            Snackbar.make(view, "Usuario y contraseña son campos obligatorios", Snackbar.LENGTH_SHORT).show();
        } else {
            Usuario usuario = new Usuario(emailIngresado, contrasenaIngresada);
            ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo activeNetwork = Objects.requireNonNull(cm).getActiveNetworkInfo();
            boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();
            if (isConnected) {
                servicioLoginRetrofit(usuario);
            } else {
                Toast.makeText(getApplicationContext(), "Necesita conectarse a internet para poder acceder"
                        , Toast.LENGTH_SHORT).show();
            }
        }
    }

    protected void servicioLoginRetrofit(final Usuario usua){
        Call<Estudiante> callLogin = LoginApiAdapter.getLoginService().login(usua);
        callLogin.enqueue(new Callback<Estudiante>() {
            @Override
            public void onResponse(Call<Estudiante> call, Response<Estudiante> response) {
                if (response.isSuccessful()){
                    Estudiante estudiante;
                    estudiante = response.body();
                    String token = response.headers().get(HttpHeaders.AUTHORIZATION);

                    assert token != null;
                    if (!token.isEmpty()){
                        if(!estudiante.isUsuarioAD()){
                            guardarUsuarioObtenido(Objects.requireNonNull(estudiante));
                            rol = response.headers().get("Rol");
                            guardarToken(token);
                            guardarPreferencias();
                            usuario = usua.getUsername();
                            Toast.makeText(getApplicationContext(), "Se ha ingresado correctamente", Toast.LENGTH_SHORT).show();
                            // Intent menuActivity = new Intent(getApplicationContext(), RegistroActivity.class);
                            Intent menuActivity = new Intent(getApplicationContext(), MenuActivity.class);
                            startActivity(menuActivity);
                        }else{
                            servicioLoginUsuariosLdap(usua);
                        }

                    }else {
                        Toast.makeText(getApplicationContext(), "Acceso no autorizado, token no válido", Toast.LENGTH_SHORT).show();
                    }
                }else {
                    Log.i("HTTP_CE", String.valueOf(response.code()));
                    Toast.makeText(getApplicationContext(),"Acceso no permitido, credenciales incorrectas o usuario inactivo",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Estudiante> call, Throwable t) {
                Log.e("ErrorLogin", Objects.requireNonNull(t.getMessage()));
            }
        });
    }

    protected void servicioLoginUsuariosLdap(Usuario usuario){
        Call<Estudiante> callLoginUsLdap = LoginApiAdapter.getLoginService().loginUsuariosLdap(usuario);
        callLoginUsLdap.enqueue(new Callback<Estudiante>() {
            @Override
            public void onResponse(Call<Estudiante> call, Response<Estudiante> response) {
                if (response.isSuccessful()){
                    Estudiante estudiante;
                    estudiante = response.body();
                    String token = response.headers().get(HttpHeaders.AUTHORIZATION);

                    assert token != null;
                    if (!token.isEmpty()){
                        guardarUsuarioObtenido(Objects.requireNonNull(estudiante));
                        rol = response.headers().get("Rol");
                        guardarToken(token);
                        guardarPreferencias();
                        Toast.makeText(getApplicationContext(), "Se ha ingresado correctamente", Toast.LENGTH_SHORT).show();
                        Intent menuActivity = new Intent(getApplicationContext(), MenuActivity.class);
                        startActivity(menuActivity);
                    }else{
                        Toast.makeText(getApplicationContext(), "Acceso no autorizado, token no válido", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Log.i("HTTP_CE", String.valueOf(response.code()));
                    Toast.makeText(getApplicationContext(),"Acceso no permitido, credenciales incorrectas o usuario inactivo",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Estudiante> call, Throwable t) {
                Log.e("ErrorLogin", Objects.requireNonNull(t.getMessage()));
            }
        });
    }

    private void guardarPreferencias() {
        SharedPreferences preferences = getSharedPreferences("credenciales", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("email", emailIngresado);
        //editor.putString("contrasena", contrasenaIngresada);
        editor.putString("rol", rol);
        editor.apply();
    }

    private void guardarUsuarioObtenido(Estudiante estudiante){
        SharedPreferences preferences = getSharedPreferences("UsuarioObtenido", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("apellidos", estudiante.getApellidos());
        editor.putString("nombres", estudiante.getNombres());
        editor.putString("cedula", estudiante.getCedula());
        editor.putString("correo", estudiante.getCorreo());
        editor.putBoolean("usuarioAD",estudiante.isUsuarioAD());
        editor.apply();
    }

    private void guardarToken(String token){
        SharedPreferences preferences = getSharedPreferences("token", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("token", token);
        editor.apply();
    }

    private void solicitarPermisos() {
        int permisoUbicacion = ContextCompat.checkSelfPermission(LoginActivity.this, Manifest.permission.ACCESS_FINE_LOCATION);
        //int permisoAlmacenamiento = ContextCompat.checkSelfPermission(LoginActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (permisoUbicacion != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                int CODE_PERMISSION = 0;
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, CODE_PERMISSION);
            }
        }
    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }
}
