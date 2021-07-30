package com.emerscience.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class TicketFamiliarActivity extends AppCompatActivity {

    boolean desactivarNuevoForm = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ticket_familiar);

        Bundle parametros = this.getIntent().getExtras();
        if (parametros != null){
            System.out.println("PARAMETROS NO NULOS");
            desactivarNuevoForm = parametros.getBoolean("desactivaNuevoForm");
        }

    }


    public void irMenu(View v) {
        Intent menu = new Intent(getApplicationContext(), MenuActivity.class);
        menu.putExtra("desactivaNuevoForm", true);
        startActivity(menu);
        finish();
    }
}
