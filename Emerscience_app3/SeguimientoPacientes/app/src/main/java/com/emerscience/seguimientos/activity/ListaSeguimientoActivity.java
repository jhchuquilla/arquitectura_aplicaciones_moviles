package com.emerscience.seguimientos.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.emerscience.seguimientos.adaptadores.AdaptadorSeguimiento;
import com.emerscience.seguimientos.pojos.Seguimiento;

import java.util.ArrayList;
import java.util.List;

public class ListaSeguimientoActivity extends AppCompatActivity {

    private ArrayList<Seguimiento> arrayListSeg;
    private List<Seguimiento> listaSeguimiento;
    private String nombres, apellidos, cedula;
    private int edad;
    private boolean reporte = false;
    private TextView txtApellidos, txtNombres, txtEdad;
    RecyclerView rvListaSeguimiento;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_seguimiento);

        Bundle bundle = this.getIntent().getBundleExtra("arrSeguimientos");
        if (bundle != null){
            arrayListSeg = (ArrayList<Seguimiento>) bundle.getSerializable("arrSeguimientos");
            listaSeguimiento = new ArrayList<>(arrayListSeg);
            System.out.println(listaSeguimiento.get(0).getFechaRegistroSeguimiento());
        } else {
            arrayListSeg = new ArrayList<>();
            listaSeguimiento = new ArrayList<>();
        }

        Bundle parametros = this.getIntent().getExtras();
        if (parametros != null){
            apellidos = parametros.getString("Apellidos");
            nombres = parametros.getString("Nombres");
            edad = parametros.getInt("Edad");
            cedula = parametros.getString("cedulaFamiliar");
            reporte = parametros.getBoolean("reporte");
        }

        txtApellidos = findViewById(R.id.txtApList);
        txtNombres = findViewById(R.id.txtNombList);
        txtEdad= findViewById(R.id.txtEdadList);
        rvListaSeguimiento = findViewById(R.id.rvListaSeguimiento);
        rvListaSeguimiento.setLayoutManager(new LinearLayoutManager(this));

        txtApellidos.setText(apellidos);
        txtNombres.setText(nombres);
        txtEdad.setText(String.valueOf(edad));

        AdaptadorSeguimiento adaptadorSeguimiento = new AdaptadorSeguimiento(listaSeguimiento);
        rvListaSeguimiento.setAdapter(adaptadorSeguimiento);

        adaptadorSeguimiento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Seguimiento seguimiento = new Seguimiento();
                seguimiento.setFechaRegistroSeguimiento(listaSeguimiento.get(rvListaSeguimiento.getChildAdapterPosition(v)).getFechaRegistroSeguimiento());
                seguimiento.setFrecRespiratoria(listaSeguimiento.get(rvListaSeguimiento.getChildAdapterPosition(v)).getFrecRespiratoria());
                seguimiento.setTemperaturaCorporal(listaSeguimiento.get(rvListaSeguimiento.getChildAdapterPosition(v)).getTemperaturaCorporal());
                seguimiento.setFrecCardiaca(listaSeguimiento.get(rvListaSeguimiento.getChildAdapterPosition(v)).getFrecCardiaca());
                seguimiento.setSaturacionOxigeno(listaSeguimiento.get(rvListaSeguimiento.getChildAdapterPosition(v)).getSaturacionOxigeno());
                seguimiento.setDificultadRespirar(listaSeguimiento.get(rvListaSeguimiento.getChildAdapterPosition(v)).getDificultadRespirar());
                seguimiento.setFatiga(listaSeguimiento.get(rvListaSeguimiento.getChildAdapterPosition(v)).getFatiga());
                seguimiento.setDificultadHablar(listaSeguimiento.get(rvListaSeguimiento.getChildAdapterPosition(v)).getDificultadHablar());
                seguimiento.setDelirio(listaSeguimiento.get(rvListaSeguimiento.getChildAdapterPosition(v)).getDelirio());
                Intent seguimientoActivity = new Intent(getApplicationContext(), SeguimientoActivity.class);

                Bundle bundle = new Bundle();
                bundle.putSerializable("arrSeguimientos", arrayListSeg);
                seguimientoActivity.putExtra("arrSeguimientos", bundle);
                seguimientoActivity.putExtra("Apellidos", apellidos);
                seguimientoActivity.putExtra("Nombres", nombres);
                seguimientoActivity.putExtra("Edad", edad);
                seguimientoActivity.putExtra("cedulaFamiliar", cedula);
                seguimientoActivity.putExtra("reporte", reporte);
                guardarDatosSeguimiento(seguimiento, seguimientoActivity);
                startActivity(seguimientoActivity);
                finish();
            }
        });

    }

    public void guardarDatosSeguimiento(Seguimiento seguimiento, Intent seguimientoActivity){
        Bundle bundle = new Bundle();
        bundle.putSerializable("Seguimiento", seguimiento);
        seguimientoActivity.putExtra("bundleSeguimiento", bundle);

    }

    @Override
    public void onBackPressed() {
        Intent registroActivity = new Intent(getApplicationContext(), RegistroActivity.class);
        registroActivity.putExtra("reporte", reporte);
        startActivity(registroActivity);
        finish();
    }
}
