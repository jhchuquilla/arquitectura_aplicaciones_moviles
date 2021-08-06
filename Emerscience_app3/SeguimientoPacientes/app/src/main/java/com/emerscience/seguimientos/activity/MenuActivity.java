package com.emerscience.seguimientos.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.emerscience.seguimientos.pojos.Usuario;

public class MenuActivity extends AppCompatActivity {

    private String rol;
    private LinearLayout lyMaps, lyGestUsuarios;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        lyMaps = findViewById(R.id.lyMaps);
        lyMaps.setVisibility(View.INVISIBLE);

        lyGestUsuarios = findViewById(R.id.lyGestUsuarios);
        lyGestUsuarios.setVisibility(View.INVISIBLE);

        Toolbar toolbar = findViewById(R.id.toolbarFam);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");

        rol = obtenerPreferenciasRol();
        if (rol != null){
            if (rol.equalsIgnoreCase("ROLE_SUPERVISOR")){
                lyMaps.setVisibility(View.VISIBLE);
                lyGestUsuarios.setVisibility(View.VISIBLE);
            }else if (rol.equalsIgnoreCase("ROLE_VIGILANTE")){
                lyMaps.setVisibility(View.INVISIBLE);
                lyGestUsuarios.setVisibility(View.INVISIBLE);
            }
        }
    }

    public void seguimiento(View v) {
        Intent registroActivity = new Intent(getApplicationContext(),RegistroActivity.class);
        registroActivity.putExtra("reporte", false);
        startActivity(registroActivity);
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        SharedPreferences preferences = getSharedPreferences("UsuarioObtenido", Context.MODE_PRIVATE);
        boolean usuarioLdap = preferences.getBoolean("usuarioAD", false);
        // Inflate the menu; this adds items to the action bar if it is present.
        if (usuarioLdap){
            getMenuInflater().inflate(R.menu.menu_main, menu);
        }else {
            getMenuInflater().inflate(R.menu.menu_main_cambio_contrasena, menu);
        }
        return true;
    }

    public void menuMaps(View v){
        Intent menuMaps = new Intent(getApplicationContext(), MenuMapsActivity.class);
        startActivity(menuMaps);
    }

    public void mostrarReporte(View v){
        Intent registroActivity = new Intent(getApplicationContext(), RegistroActivity.class);
        registroActivity.putExtra("reporte", true);
        startActivity(registroActivity);
        finish();
    }

    public void menuGestionUsuarios(View v){
        Intent menuGestionUsuariosActivity = new Intent(getApplicationContext(), MenuGestionUsuariosActivity.class);
        startActivity(menuGestionUsuariosActivity);
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_cerrar_sesion) {
            cerrarSesion(null);
            return true;
        }else if (id == R.id.action_cambiar_password){
            //lanza activity para cambio de constraseña de usuarios no ldap
            SharedPreferences preferences = getSharedPreferences("credenciales", Context.MODE_PRIVATE);
            Usuario usuario = new Usuario();
            usuario.setUsername(preferences.getString("email", ""));
            usuario.setRol(preferences.getString("rol", ""));
            usuario.setUsuarioActivo(true);
            usuario.setUsuaioLdap(false);
            Bundle bund = new Bundle();
            bund.putSerializable("Usuario", usuario);
            Intent cambioContrasenaActivity = new Intent(getApplicationContext(), CambioContrasenaActivity.class);
            cambioContrasenaActivity.putExtra("bundleUsuario", bund);
            startActivity(cambioContrasenaActivity);
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressLint("NewApi")
    public void cerrarAplicacion(View v) {
        finishAffinity();
    }

    public void cambiarContrasena(View v){

    }

    public void cerrarSesion(View v){
        SharedPreferences preferences = getSharedPreferences("credenciales", Context.MODE_PRIVATE);
        preferences.edit().clear().apply();
        Intent mainActivity = new Intent(this, LoginActivity.class);
        startActivity(mainActivity);
    }

    @Override
    public void onBackPressed() {
        Toast.makeText(getApplicationContext(), "Por favor cierre sesión en el menú de la esquina superior derecha",Toast.LENGTH_SHORT).show();
    }

    public String obtenerPreferenciasRol(){
        SharedPreferences preferences = getSharedPreferences("credenciales", Context.MODE_PRIVATE);
        return preferences.getString("rol", "");
    }

}
