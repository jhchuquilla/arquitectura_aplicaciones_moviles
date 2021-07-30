package com.emerscience.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.transition.Transition;

public class IntroActivity extends AppCompatActivity {

    public static int DURACION_SPLASH = 2000;
    private Transition transition;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

        new Handler().postDelayed(new Runnable(){
            public void run(){
                Intent mainActivity = new Intent(getApplicationContext(),DeclaracionAceptacion.class);
                startActivity(mainActivity);
                overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
                finish();
            };
        }, 3000);

        }
        else{
            Intent mainActivity = new Intent(getApplicationContext(),DeclaracionAceptacion.class);
            startActivity(mainActivity);
            finish();
        }
    }

}

