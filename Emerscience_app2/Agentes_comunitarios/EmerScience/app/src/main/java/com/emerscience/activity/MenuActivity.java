package com.emerscience.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;

import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.emerscience.io.EstudianteApiAdapter;
import com.emerscience.pojos.Estudiante;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MenuActivity extends AppCompatActivity {

    private Button btnLlenarFormularioDatosPersonales, btnMenuEditar;
    private LinearLayout lyMenuEditar, lyNuevoFormEst;
    private boolean desactivarNuevoForm = false;
    private boolean habilitarEditar = false;
    public static Estudiante estudiante;
    TextView linkTelesalud, txtTelesalud;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        btnLlenarFormularioDatosPersonales = findViewById(R.id.btn_form_llenar);
        btnMenuEditar = findViewById(R.id.btnMenuEditar);
        lyMenuEditar = findViewById(R.id.lyMenuEditar);
        lyNuevoFormEst = findViewById(R.id.lyNuevoFormEst);
        txtTelesalud = findViewById(R.id.txtTelesalud);
        linkTelesalud = (TextView) findViewById(R.id.txtLink);
        Toolbar toolbar = findViewById(R.id.toolbarFam);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");

        Bundle parametros = this.getIntent().getExtras();
        if (parametros != null){
            habilitarEditar = parametros.getBoolean("habilitarEditar");
        }

        if (getSharedPreferences("UsuarioObtenido", MODE_PRIVATE).getBoolean("usuarioAD", false)){
            if (MainActivity.usuario.equalsIgnoreCase("dpruebas")){
                btnLlenarFormularioDatosPersonales.setEnabled(true);
                btnMenuEditar.setEnabled(false);
                btnMenuEditar.setEnabled(true);
                txtTelesalud.setVisibility(View.INVISIBLE);
                linkTelesalud.setVisibility(View.INVISIBLE);
            }else {
                btnLlenarFormularioDatosPersonales.setEnabled(false);
                btnMenuEditar.setEnabled(false);
                txtTelesalud.setVisibility(View.INVISIBLE);
                linkTelesalud.setVisibility(View.INVISIBLE);
                estudiante = new Estudiante();
                obtenerEstudiante(getSharedPreferences("UsuarioObtenido", Context.MODE_PRIVATE)
                        .getString("cedula", ""));
            }
        }else {
            btnLlenarFormularioDatosPersonales.setEnabled(true);
            btnMenuEditar.setEnabled(false);
            btnMenuEditar.setEnabled(true);
            txtTelesalud.setVisibility(View.INVISIBLE);
            linkTelesalud.setVisibility(View.INVISIBLE);
        }

        btnLlenarFormularioDatosPersonales.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent datosActivity = new Intent(getApplicationContext(),DatosPersonalesActivity.class);
                startActivity(datosActivity);
                finish();
            }
        });

        btnMenuEditar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (getSharedPreferences("UsuarioObtenido", MODE_PRIVATE).getBoolean("usuarioAD", false)){
                    if (MainActivity.usuario.equalsIgnoreCase("dpruebas")){
                        FragmentManager fragmentManager = getSupportFragmentManager();
                        new CuadroDialogoCedula(getApplicationContext()).show(fragmentManager, "DialogoCedula");
                    }else {
                        Intent menuEditar = new Intent(getApplicationContext(), MenuEditarActivity.class);
                        menuEditar.putExtra("editar", true);
                        startActivity(menuEditar);
                        finish();
                    }
                }else {
                    FragmentManager fragmentManager = getSupportFragmentManager();
                    new CuadroDialogoCedula(getApplicationContext()).show(fragmentManager, "DialogoCedula");
                }
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
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
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressLint("NewApi")
    public void cerrarAplicacion(View v) {
        finishAffinity();
    }

    public void cerrarSesion(View v){
        SharedPreferences preferences = getSharedPreferences("credenciales", Context.MODE_PRIVATE);
        preferences.edit().clear().commit();
        Intent mainActivity = new Intent(this, MainActivity.class);
        startActivity(mainActivity);
    }

    @Override
    public void onBackPressed() {
        Toast.makeText(getApplicationContext(), "Por favor cierre sesión en el menú de la esquina superior derecha",Toast.LENGTH_SHORT).show();
    }

    protected void obtenerEstudiante(String cedula){
        Call<Estudiante> estudianteCall = EstudianteApiAdapter.getApiService().obtenerEstudiantePorCedula(cedula
                , getSharedPreferences("token", MODE_PRIVATE).getString("token", ""));
        estudianteCall.enqueue(new Callback<Estudiante>() {
            @Override
            public void onResponse(Call<Estudiante> call, Response<Estudiante> response) {
                if (response.isSuccessful()){
                    Estudiante est;
                    est = response.body();
                    if (null != est){
                        estudiante = est;
                        System.out.println("Fecha Mod: " + estudiante.getFechaModificacion());
                        btnMenuEditar.setEnabled(true);
                        btnLlenarFormularioDatosPersonales.setEnabled(false);
                        txtTelesalud.setVisibility(View.VISIBLE);
                        linkTelesalud.setVisibility(View.VISIBLE);
                        linkTelesalud.setMovementMethod(LinkMovementMethod.getInstance());
                    }else {
                        btnLlenarFormularioDatosPersonales.setEnabled(true);
                        btnMenuEditar.setEnabled(false);
                    }
                }else {
                    Log.e("ERROR HTTP", String.valueOf(response.code()));
                }
            }

            @Override
            public void onFailure(Call<Estudiante> call, Throwable t) {

            }
        });
    }

}
