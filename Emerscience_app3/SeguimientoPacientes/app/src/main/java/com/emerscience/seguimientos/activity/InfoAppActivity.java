package com.emerscience.seguimientos.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.transition.Fade;
import android.view.View;
import android.view.animation.DecelerateInterpolator;

public class InfoAppActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_app);
        llegadaTransicion();
    }

    public void ingresar(View view){
        Intent login = new Intent(getApplicationContext(), LoginActivity.class);
        startActivity(login);
    }

    public void llegadaTransicion(){
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
            Fade fadeIn = new Fade(Fade.IN);
            fadeIn.setDuration(IntroActivity.DURACION_SPLASH);
            fadeIn.setInterpolator(new DecelerateInterpolator());
        }
    }

    @Override public void onBackPressed() {
        moveTaskToBack(true);
    }
}
