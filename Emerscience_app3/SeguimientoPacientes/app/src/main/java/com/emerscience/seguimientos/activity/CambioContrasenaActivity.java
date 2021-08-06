package com.emerscience.seguimientos.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.emerscience.seguimientos.pojos.Usuario;
import com.emerscience.seguimientos.retrofit.UsuarioApiAdapter;
import com.emerscience.seguimientos.utils.Utileria;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CambioContrasenaActivity extends AppCompatActivity {

    private TextInputLayout txtInputCambioContrasena;
    private Usuario usuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cambio_contrasena);

        Bundle bundle = this.getIntent().getBundleExtra("bundleUsuario");
        if (bundle != null){
            usuario = (Usuario) bundle.getSerializable("Usuario");
        }else {
            usuario = new Usuario();
        }

        txtInputCambioContrasena = findViewById(R.id.txtInputCambioContrasena);
    }

    public void cambiarcontrasena(View v){
        if (!Utileria.validarContraseña(txtInputCambioContrasena, false)){
            Toast.makeText(getApplicationContext(), "Debe suministrar una contraseña válida", Toast.LENGTH_SHORT).show();
        }else{
            String contrasena = Objects.requireNonNull(txtInputCambioContrasena.getEditText()).getText().toString().trim();
            usuario.setPassword(contrasena);
            //llamar servicio retrofit para actualizar contraseña
            servicioCambioContrasenaRetrofit(usuario);
        }
    }

    protected void servicioCambioContrasenaRetrofit(Usuario usuario){
        Call<Boolean> callCambioContrasena = UsuarioApiAdapter.getUsuarioApiService().actualizarContrasena(usuario,
                Utileria.obtenerToken(getApplicationContext()));
        callCambioContrasena.enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                if (response.isSuccessful()){
                    boolean contrasenaCambiada = false;
                    contrasenaCambiada = response.body();
                    if (contrasenaCambiada){
                        Toast.makeText(getApplicationContext(), "Su contraseña ha sido actualizada", Toast.LENGTH_SHORT).show();
                        Intent loginActivity = new Intent(getApplicationContext(), LoginActivity.class);
                        startActivity(loginActivity);
                        finish();
                    }else{
                        Toast.makeText(getApplicationContext(), "Su contraseña no ha sido actualizada", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(getApplicationContext(), "No se pudo actualizar contraseña, intente más tarde", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Boolean> call, Throwable t) {

            }
        });
    }

    public void cancelar(View v){
        Intent menuActivity = new Intent(getApplicationContext(), MenuActivity.class);
        startActivity(menuActivity);
        finish();
    }

    @Override
    public void onBackPressed() {
        Intent menuActivity = new Intent(getApplicationContext(), MenuActivity.class);
        startActivity(menuActivity);
        finish();
    }
}
