package com.emerscience.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class TicketEstudianteActivity extends AppCompatActivity {

    //campo para obtener cantidad de familiares con sintomas
    Integer cantidadPersSintomas=0;

    //Campo para obtener si es el estudiante el que tiene sintomas COVID-19
    String estudiantePresentaSintomas = "";

    //Campo para obtener cedula estudiante como id del familiar
    String cedulaEstudiante = "";

    TextView txtCantidadFamiliaresCovid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ticket_estudiante);
        //Recepcion de parametro cantidadPersonasSintomas y cedula de persona (Estudiante)

        txtCantidadFamiliaresCovid = findViewById(R.id.txtCantidaFamiliaresCovid);

        Bundle parametros = this.getIntent().getExtras();
        if (parametros != null){
            cantidadPersSintomas = Integer.parseInt(parametros.getString("cantidadPersonasSintomas"));
            txtCantidadFamiliaresCovid.setText(String.valueOf(cantidadPersSintomas));
            estudiantePresentaSintomas = parametros.getString("estudiantePresentaSintomas","");
        }


    }

    @Override
    public void onBackPressed() {

    }


    public void irDatosFamiliar(View v) {

        Intent datosFamiliar = new Intent(getApplicationContext(), DatosFamiliaresActivity.class);
        datosFamiliar.putExtra("cantidadPersonasSintomas", cantidadPersSintomas);
        datosFamiliar.putExtra("estudiantePresentaSintomas", estudiantePresentaSintomas);
        startActivity(datosFamiliar);
        finish();
    }
}
