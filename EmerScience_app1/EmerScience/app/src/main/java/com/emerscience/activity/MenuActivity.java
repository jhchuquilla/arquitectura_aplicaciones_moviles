package com.emerscience.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.text.method.LinkMovementMethod;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MenuActivity extends AppCompatActivity {

    private Button btnLlenarFormularioDatosPersonales, btnFormFamiliar;
    private LinearLayout lyFormFamiliares, lyNuevoFormEst;
    private boolean desactivarNuevoForm = false;
    TextView linkTelesalud, txtTelesalud;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        btnLlenarFormularioDatosPersonales = findViewById(R.id.btn_form_llenar);
        btnFormFamiliar = findViewById(R.id.btnFormFamiliar);
        lyFormFamiliares = findViewById(R.id.lyFormFamiliares);
        lyNuevoFormEst = findViewById(R.id.lyNuevoFormEst);
        txtTelesalud = findViewById(R.id.txtTelesalud);
        linkTelesalud = (TextView) findViewById(R.id.txtLink);
        Toolbar toolbar = findViewById(R.id.toolbarFam);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");

        Bundle parametros = this.getIntent().getExtras();
        if (parametros != null){
            System.out.println("PARAMETROS NO NULOS");
            desactivarNuevoForm = parametros.getBoolean("desactivaNuevoForm");
        }

        txtTelesalud.setVisibility(View.INVISIBLE);
        linkTelesalud.setVisibility(View.INVISIBLE);
        btnFormFamiliar.setVisibility(View.INVISIBLE);
        lyFormFamiliares.setVisibility(View.INVISIBLE);

        if (desactivarNuevoForm){
            txtTelesalud.setVisibility(View.VISIBLE);
            linkTelesalud.setVisibility(View.VISIBLE);
            lyNuevoFormEst.setVisibility(View.INVISIBLE);
            linkTelesalud.setMovementMethod(LinkMovementMethod.getInstance());
        }else {
            txtTelesalud.setVisibility(View.INVISIBLE);
            linkTelesalud.setVisibility(View.INVISIBLE);
        }

        btnLlenarFormularioDatosPersonales.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent datosActivity = new Intent(getApplicationContext(),DatosPersonalesActivity.class);
                startActivity(datosActivity);
            }
        });

        btnFormFamiliar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent datosFamiliar = new Intent(getApplicationContext(), DatosFamiliaresActivity.class);
                startActivity(datosFamiliar);
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
}
