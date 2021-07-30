package com.emerscience.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import com.emerscience.adaptadores.AdaptadorFamiliares;
import com.emerscience.pojos.Familiar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class ListarFamiliaresActivity extends AppCompatActivity {

    private List<Familiar> listaFamiliares;
    private FloatingActionButton fabAgregarFamiliar;
    RecyclerView rvFamiliares;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listar_familiares);

        if (null != MenuEditarActivity.listaFamiliares){
            listaFamiliares = MenuEditarActivity.listaFamiliares;
        }else {
            listaFamiliares = new ArrayList<>();
        }

        rvFamiliares = findViewById(R.id.rvListaFamiliares);
        rvFamiliares.setLayoutManager(new LinearLayoutManager(this));
        fabAgregarFamiliar = findViewById(R.id.fabAgregarFamiliar);

        AdaptadorFamiliares adaptadorFamiliares = new AdaptadorFamiliares(listaFamiliares);
        rvFamiliares.setAdapter(adaptadorFamiliares);

        adaptadorFamiliares.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Familiar fam = new Familiar();
                fam.setApellidosFam(listaFamiliares.get(rvFamiliares.getChildAdapterPosition(v)).getApellidosFam());
                fam.setNombresFam(listaFamiliares.get(rvFamiliares.getChildAdapterPosition(v)).getNombresFam());
                fam.setCedulaFam(listaFamiliares.get(rvFamiliares.getChildAdapterPosition(v)).getCedulaFam());
                fam.setCedulaEstudiante(listaFamiliares.get(rvFamiliares.getChildAdapterPosition(v)).getCedulaEstudiante());
                fam.setEdadFam(listaFamiliares.get(rvFamiliares.getChildAdapterPosition(v)).getEdadFam());
                fam.setSexoFam(listaFamiliares.get(rvFamiliares.getChildAdapterPosition(v)).getSexoFam());
                fam.setSintomasFam(listaFamiliares.get(rvFamiliares.getChildAdapterPosition(v)).getSintomasFam());
                System.out.println("INFOOOO: " + listaFamiliares.get(rvFamiliares.getChildAdapterPosition(v)).getDifRespirarSevera());
                if (listaFamiliares.get(rvFamiliares.getChildAdapterPosition(v)).getDifRespirarSevera() != null){
                    fam.setDifRespirarSevera(listaFamiliares.get(rvFamiliares.getChildAdapterPosition(v)).getDifRespirarSevera());
                }
                fam.setCondicionesFam(listaFamiliares.get(rvFamiliares.getChildAdapterPosition(v)).getCondicionesFam());
                fam.setContactoFamConPersonaCovid(listaFamiliares.get(rvFamiliares.getChildAdapterPosition(v)).getContactoFamConPersonaCovid());
                guardarPreferenciasFamiliarSeleccionado(fam);
                Intent datosFamiliares = new Intent(getApplicationContext(), DatosFamiliaresActivity.class);
                datosFamiliares.putExtra("editar", true);
                datosFamiliares.putExtra("cantidadPersSintomas", 1);
                datosFamiliares.putExtra("estudiantePresentaSintomas", "NO");
                datosFamiliares.putExtra("cedulaEstudiante", fam.getCedulaEstudiante());
                startActivity(datosFamiliares);
            }
        });

        fabAgregarFamiliar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent datosFamiliares = new Intent(getApplicationContext(), DatosFamiliaresActivity.class);
                datosFamiliares.putExtra("editar", false);
                datosFamiliares.putExtra("cantidadPersonasSintomas", 1);
                datosFamiliares.putExtra("estudiantePresentaSintomas", "NO");
                startActivity(datosFamiliares);
            }
        });

    }

    private void guardarPreferenciasFamiliarSeleccionado(Familiar familiar){
        SharedPreferences preferences = getSharedPreferences("FamiliarSeleccionado", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("apellidos", familiar.getApellidosFam());
        editor.putString("nombres", familiar.getNombresFam());
        editor.putString("cedula", familiar.getCedulaFam());
        editor.putString("cedulaEstudiante", familiar.getCedulaEstudiante());
        editor.putInt("edad", familiar.getEdadFam());
        editor.putString("sexo", familiar.getSexoFam());
        editor.putString("sintomas", familiar.getSintomasFam());
        editor.putString("difRespirar", familiar.getDifRespirarSevera());
        editor.putString("condiciones", familiar.getCondicionesFam());
        editor.putString("contactoCovid", familiar.getContactoFamConPersonaCovid());
        editor.apply();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent menuEditar = new Intent(getApplicationContext(), MenuEditarActivity.class);
        startActivity(menuEditar);
    }
}
