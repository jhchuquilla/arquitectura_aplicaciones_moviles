package com.emerscience.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.emerscience.io.FamiliarApiAdapter;
import com.emerscience.pojos.Familiar;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MenuEditarActivity extends AppCompatActivity {

    private Button btnEditarEstudiante, btnEditarFamiliares;
    private boolean tieneFamiliares = false;
    private boolean editar = false;
    public static List<Familiar> listaFamiliares;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_editar);

        btnEditarEstudiante = findViewById(R.id.btn_editar_estudiante);
        btnEditarFamiliares = findViewById(R.id.btn_editar_familiar);

        btnEditarFamiliares.setEnabled(false);
        listaFamiliares = new ArrayList<>();

        Bundle parametros = this.getIntent().getExtras();
        if (null != parametros){
            editar = parametros.getBoolean("editar", false);
        }

        habilitarDeshabilitarBtnEditFamiliar(getSharedPreferences("UsuarioObtenido", MODE_PRIVATE)
                .getString("cedula", "")
        );

        btnEditarEstudiante.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent datosEstudiante = new Intent(getApplicationContext(), DatosPersonalesActivity.class);
                datosEstudiante.putExtra("editar", true);
                datosEstudiante.putExtra("tieneFamiliares", tieneFamiliares);
                startActivity(datosEstudiante);
            }
        });

        btnEditarFamiliares.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent menuListaFamiliares = new Intent(getApplicationContext(), ListarFamiliaresActivity.class);
                startActivity(menuListaFamiliares);
            }
        });

    }

    protected void habilitarDeshabilitarBtnEditFamiliar(String cedulaEstudiante){
        Call<List<Familiar>> callFamiliares = FamiliarApiAdapter.getFamiliarApiService()
                .obtenerFamiliaresPorCedulaEstudiante(cedulaEstudiante, getSharedPreferences("token", MODE_PRIVATE)
                        .getString("token","")
                );
        callFamiliares.enqueue(new Callback<List<Familiar>>() {
            @Override
            public void onResponse(Call<List<Familiar>> call, Response<List<Familiar>> response) {
                if (response.isSuccessful()){
                    List<Familiar> lista = response.body();
                    if (null != lista){
                        listaFamiliares = lista;
                        tieneFamiliares = true;
                        btnEditarFamiliares.setEnabled(true);
                    }else {
                        tieneFamiliares = false;
                        btnEditarFamiliares.setEnabled(false);
                    }
                }else {
                    btnEditarFamiliares.setEnabled(false);
                    Log.i("HTTP CODE", String.valueOf(response.code()));
                }
            }

            @Override
            public void onFailure(Call<List<Familiar>> call, Throwable t) {
                Log.e("ErrorGen", t.getMessage());
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent menu = new Intent(getApplicationContext(), MenuActivity.class);
        startActivity(menu);
        finish();
    }
}
