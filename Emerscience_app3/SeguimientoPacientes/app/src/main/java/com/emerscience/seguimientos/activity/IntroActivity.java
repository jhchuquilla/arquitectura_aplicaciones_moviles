package com.emerscience.seguimientos.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

public class IntroActivity extends AppCompatActivity {

    public static int DURACION_SPLASH = 2000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

            new Handler().postDelayed(new Runnable() {
                public void run() {
                    Intent mainActivity = new Intent(getApplicationContext(), InfoAppActivity.class);
                    startActivity(mainActivity);
                    overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                    finish();
                }
            }, 3000);

        } else {
            Intent mainActivity = new Intent(getApplicationContext(), InfoAppActivity.class);
            startActivity(mainActivity);
            finish();
        }
    }

}

