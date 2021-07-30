package com.emerscience.seguimiento.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MenuMapsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_maps);
    }

    public void buscarCoordenada(View v){
        Intent registroActivity = new Intent(getApplicationContext(), RegistroActivity.class);
        registroActivity.putExtra("buscarCoord", true);
        startActivity(registroActivity);
        finish();
    }

    public void mostrarTodasCoordenadas(View v){
        Intent mapsActivity = new Intent(getApplicationContext(), MapsActivity.class);
        mapsActivity.putExtra("buscarCoord", false);
        startActivity(mapsActivity);
        finish();
    }

    @Override
    public void onBackPressed() {
        Intent menuActivity = new Intent(getApplicationContext(), MenuActivity.class);
        startActivity(menuActivity);
        finish();
    }
}
