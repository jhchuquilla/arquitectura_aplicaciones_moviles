package com.emerscience.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.ActionBar;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.emerscience.pojos.CondicionSalud;
import com.emerscience.pojos.Familiar;
import com.emerscience.pojos.Sintoma;
import com.emerscience.pojos.SintomaRespiratorio;
import com.emerscience.utils.AsyncTaskInsertarEstudiante;
import com.emerscience.utils.AsyncTaskObtenerCedulas;
import com.emerscience.utils.Utileria;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class DatosFamiliaresActivity extends AppCompatActivity {

    int contadorFormulario = 1;

    public static List<String> numCedulas;
    public static List<Familiar> listaFamiliares= new ArrayList<Familiar>();
    public static List<Sintoma> listaSintomas=new ArrayList<Sintoma>();
    public static List<CondicionSalud> listaCondiciones=new ArrayList<CondicionSalud>();
    public static List<SintomaRespiratorio> listaSintomasRespiratorios=new ArrayList<SintomaRespiratorio>();

    boolean presionoDificultadRespirar = false;
    boolean presionoDifHablar = false;
    boolean presionoFaltaAire = true;
    boolean presionaDolorToracixo = false;
    boolean presionoNinguna = false;
    boolean presionoContactoSiFam = false, presionoContactoNoFam = false, presionoContactoDescFam = false;

    private TextInputLayout txtInputApellidosFam, txtInputNombresFam, txtInputCedulaFamiliar, txtInputEdadFamiliar;
    private RadioButton rbMasculinoFam, rbFemeninoFam, rbContactoSiFam, rbContactoNoFam, rbContactoDescFam;
    private CheckBox chbFiebre, chbTos, chbGarganta, chbDiarrea, chbCabeza, chbNasal, chbPerdOlfato, chbPerdGusto,
            chbDifRespirar, chbConjuntivitis ,chbMalGeneral;
    private CheckBox chbMayor60, chbDiabetes, chbHipertension, chbEnfCorazon, chbEnfPulmonar, chbEnfRinones,
            chbEnfHigado, chbCancer, chbInmunoPresion, chbEnfAutInmunes, chbNingunoCondiciones;
    private LinearLayout lySintomas, lyDatosFamiliares, lyMensajes;
    private RadioGroup rbgSexoFam, rbgContactoFam;
    private ScrollView scroll;

    //vinculos para checkboxes generados por codigo
    private CheckBox chbDifHab, chbFaltAire, chbDolToracico, chbNingDifResp;

    //campo para obtener cantidad de familiares con sintomas
    Integer cantidadPersSintomas;

    //Campo para recibir si el estudiante es el que presenta sintomas COVID-19
    boolean estudiantePresentaSintomas = false;

    //Campo para obtener cedula estudiante como id del familiar
    String cedulaEstudiante = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_datos_familiares);
        numCedulas= new ArrayList<>();
        getSupportActionBar().setDisplayOptions(androidx.appcompat.app.ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.toolbar_form_familiar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        final TextView txtPregunta7Fam = new TextView(getApplicationContext());
        final CheckBox chbDifHablar = new CheckBox(getApplicationContext());
        final CheckBox chbFaltaAire = new CheckBox(getApplicationContext());
        final CheckBox chbDolorToracico = new CheckBox(getApplicationContext());
        final CheckBox chbNinguna = new CheckBox(getApplicationContext());

        final TextView txtCaso1 = new TextView(getApplicationContext());
        final TextView txtCaso2 = new TextView(getApplicationContext());
        final TextView txtCaso3 = new TextView(getApplicationContext());
        final TextView txtCaso4 = new TextView(getApplicationContext());
        final TextView txtCaso5 = new TextView(getApplicationContext());

        rbgSexoFam = findViewById(R.id.rbgSexoFam);
        rbgContactoFam = findViewById(R.id.rbgContactoFam);
        lySintomas = findViewById(R.id.lySintomas);
        lyDatosFamiliares = findViewById(R.id.lyDatosFamiliares);
        lyMensajes = findViewById(R.id.lyMensajes);
        txtInputApellidosFam = findViewById(R.id.txtInputApellidosFam);
        txtInputNombresFam = findViewById(R.id.txtInputNombresFam);
        txtInputCedulaFamiliar = findViewById(R.id.txtInputCedulaFamiliar);
        txtInputEdadFamiliar = findViewById(R.id.txtInputEdadFamiliar);
        rbMasculinoFam = findViewById(R.id.rbMasculinoFam);
        rbFemeninoFam = findViewById(R.id.rbFemeninoFam);
        rbContactoSiFam = findViewById(R.id.rbContactoSiFam);
        rbContactoNoFam = findViewById(R.id.rbContactoNoFam);
        rbContactoDescFam = findViewById(R.id.rbContactoDescFam);
        chbFiebre = findViewById(R.id.chbFiebre);
        chbTos = findViewById(R.id.chbTos);
        chbGarganta = findViewById(R.id.chbGarganta);
        chbDiarrea = findViewById(R.id.chbDiarrea);
        chbCabeza = findViewById(R.id.chbCabeza);
        chbNasal = findViewById(R.id.chbNasal);
        chbPerdOlfato = findViewById(R.id.chbPerdOlfato);
        chbPerdGusto = findViewById(R.id.chbPerdGusto);
        chbDifRespirar = findViewById(R.id.chbDifRespirar);
        chbConjuntivitis = findViewById(R.id.chbConjuntivitis);
        chbMalGeneral = findViewById(R.id.chbMalGeneral);

        chbMayor60 = findViewById(R.id.chbMayor60);
        chbDiabetes = findViewById(R.id.chbDiabetes);
        chbHipertension = findViewById(R.id.chbHipertension);
        chbEnfCorazon = findViewById(R.id.chbEnfCorazon);
        chbEnfPulmonar = findViewById(R.id.chbEnfPulmonar);
        chbEnfRinones = findViewById(R.id.chbEnfRinones);
        chbEnfHigado = findViewById(R.id.chbEnfHigado);
        chbCancer = findViewById(R.id.chbCancer);
        chbInmunoPresion = findViewById(R.id.chbInmunoPresion);
        chbEnfAutInmunes = findViewById(R.id.chbEnfAutInmunes);
        chbNingunoCondiciones = findViewById(R.id.chbNingunoCondiciones);
        scroll = findViewById(R.id.scroll);

        lyMensajes.removeAllViews();

        //Recepcion de parametro cantidadPersonasSintomas y cedula de persona (Estudiante)
        SharedPreferences preferences = getSharedPreferences("datosPersona", Context.MODE_PRIVATE);

        Bundle parametros = this.getIntent().getExtras();
        if (parametros != null){
            cedulaEstudiante = preferences.getString("cedula", "");
            cantidadPersSintomas = parametros.getInt("cantidadPersonasSintomas");
            estudiantePresentaSintomas = (parametros.getString("estudiantePresentaSintomas", "")).equals("SI");
        }

        if (estudiantePresentaSintomas){
            SharedPreferences preferenciasEstudiante = getSharedPreferences("datosPersona", MODE_PRIVATE);
            txtInputApellidosFam.getEditText().setText(preferenciasEstudiante.getString("apellidos", ""));
            txtInputNombresFam.getEditText().setText(preferenciasEstudiante.getString("nombres", ""));
            txtInputCedulaFamiliar.getEditText().setText(preferenciasEstudiante.getString("cedula", ""));
            txtInputEdadFamiliar.getEditText().setText(String.valueOf(preferenciasEstudiante.getInt("edad",0)));
            if ((preferenciasEstudiante.getString("sexo","")).equals("MASCULINO")){
                rbMasculinoFam.setChecked(true);
            }else {
                rbFemeninoFam.setChecked(true);
            }
            if ((preferenciasEstudiante.getInt("edad", 0)) > 60){
                chbMayor60.setEnabled(false);
                chbMayor60.setChecked(true);
                chbNingunoCondiciones.setEnabled(false);
            }else {
                chbMayor60.setEnabled(false);
                chbMayor60.setChecked(false);
                chbNingunoCondiciones.setEnabled(true);
            }
        }

        //Obtencion de cedulas de familiares de base de datos para controlar la duplicacion de datos
        new AsyncTaskObtenerCedulas(getApplicationContext()){
            @Override
            protected void onPostExecute(List<String> strings) {
                if (!strings.isEmpty()){
                    actualizarListaCedulasFamiliares(strings);
                }
            }
        }.execute("familiares");

        lyDatosFamiliares.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (getCurrentFocus() != null){
                    InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                    getWindow().getDecorView().clearFocus();
                }
                return false;
            }
        });

        //Permite validar numero de cedula luego de que termina de escribir el usuario en dicho campo
        txtInputCedulaFamiliar.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                Utileria.validarCedula(txtInputCedulaFamiliar);
            }
        });

        //Permite controlar si se marca o no automaticamente la condicion Mayor de 60 años
        txtInputEdadFamiliar.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { }

            @Override
            public void afterTextChanged(Editable s) {
                if (Utileria.validarEdad(txtInputEdadFamiliar)){
                    if ((Integer.parseInt(txtInputEdadFamiliar.getEditText().getText().toString().trim())) > 60){
                        chbMayor60.setEnabled(false);
                        chbMayor60.setChecked(true);
                        chbNingunoCondiciones.setEnabled(false);
                    }else {
                        chbMayor60.setEnabled(false);
                        chbMayor60.setChecked(false);
                        chbNingunoCondiciones.setEnabled(true);
                    }
                }
            }
        });

        chbDifRespirar.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked && !presionoDificultadRespirar){
                    //lyMensajes.removeAllViews();
                    txtPregunta7Fam.setLayoutParams(new ActionBar.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                    txtPregunta7Fam.setText(R.string.txtPregunta7Fam);
                    txtPregunta7Fam.setTextColor(getResources().getColor(R.color.colorPreguntasFormFam));
                    txtPregunta7Fam.setTextSize(20);
                    lySintomas.addView(txtPregunta7Fam);

                    chbDifHablar.setLayoutParams(new ActionBar.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                    chbDifHablar.setText(R.string.chbDifHablar);
                    chbDifHablar.setTextColor(getResources().getColor(R.color.colorCheckBoxes));
                    chbDifHablar.setTextSize(16);
                    chbDifHab = chbDifHablar;
                    lySintomas.addView(chbDifHablar);

                    chbFaltaAire.setLayoutParams(new ActionBar.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                    chbFaltaAire.setText(R.string.chbFaltaAire);
                    chbFaltaAire.setTextColor(getResources().getColor(R.color.colorCheckBoxes));
                    chbFaltaAire.setTextSize(16);
                    chbFaltAire = chbFaltaAire;
                    lySintomas.addView(chbFaltaAire);

                    chbDolorToracico.setLayoutParams(new ActionBar.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                    chbDolorToracico.setText(R.string.chbDolorToracico);
                    chbDolorToracico.setTextColor(getResources().getColor(R.color.colorCheckBoxes));
                    chbDolorToracico.setTextSize(16);
                    chbDolToracico = chbDolorToracico;
                    lySintomas.addView(chbDolorToracico);

                    chbNinguna.setLayoutParams(new ActionBar.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                    chbNinguna.setText(R.string.chbNinguna);
                    chbNinguna.setTextColor(getResources().getColor(R.color.colorCheckBoxes));
                    chbNinguna.setTextSize(16);
                    chbNingDifResp = chbNinguna;
                    lySintomas.addView(chbNinguna);
                    presionoDificultadRespirar = true;

                }else {
                    chbDifHablar.setChecked(false);
                    chbFaltaAire.setChecked(false);
                    chbDolorToracico.setChecked(false);
                    chbNinguna.setChecked(false);

                    chbDifHab = null;
                    chbFaltAire = null;
                    chbDolToracico = null;
                    chbNingDifResp = null;

                    lySintomas.removeView(txtPregunta7Fam);
                    lySintomas.removeView(chbDifHablar);
                    lySintomas.removeView(chbFaltaAire);
                    lySintomas.removeView(chbDolorToracico);
                    lySintomas.removeView(chbNinguna);

                    lyMensajes.removeAllViews();

                    presionoDificultadRespirar = false;
                }
            }
        });

        rbgContactoFam.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.rbContactoSiFam && !presionoContactoSiFam){

                    if (presionoContactoNoFam){
                        lyMensajes.removeAllViews();

                        if (chbFiebre.isChecked() || chbTos.isChecked() || chbGarganta.isChecked()
                                || chbDiarrea.isChecked() || chbCabeza.isChecked() || chbNasal.isChecked()
                                || chbPerdOlfato.isChecked() || chbPerdGusto.isChecked()
                                ||chbConjuntivitis.isChecked() || chbMalGeneral.isChecked()) {
                            if (!chbDifRespirar.isChecked()) {
                                if (chbNingunoCondiciones.isChecked()){
                                    txtCaso2.setLayoutParams(new ActionBar.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                                    txtCaso2.setText(R.string.txtCaso2);
                                    txtCaso2.setTextColor(getResources().getColor(R.color.colorComuniquese));
                                    txtCaso2.setTextSize(17);
                                    txtCaso2.setGravity(Gravity.CENTER);
                                    lyMensajes.addView(txtCaso2);
                                    presionoContactoSiFam = true;
                                }else if (chbMayor60.isChecked() || chbDiabetes.isChecked()
                                        ||chbHipertension.isChecked() || chbEnfCorazon.isChecked()
                                        ||chbEnfPulmonar.isChecked() || chbEnfRinones.isChecked()
                                        ||chbEnfHigado.isChecked() || chbCancer.isChecked()
                                        ||chbInmunoPresion.isChecked() || chbEnfAutInmunes.isChecked()){
                                    txtCaso2.setLayoutParams(new ActionBar.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                                    txtCaso2.setText(R.string.txtCaso2);
                                    txtCaso2.setTextColor(getResources().getColor(R.color.colorComuniquese));
                                    txtCaso2.setTextSize(17);
                                    txtCaso2.setGravity(Gravity.CENTER);
                                    lyMensajes.addView(txtCaso2);
                                    presionoContactoSiFam = true;
                                }
                            }else {
                                if (chbDifHablar.isChecked() || chbFaltaAire.isChecked() || chbDolorToracico.isChecked()) {
                                    txtCaso5.setLayoutParams(new ActionBar.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                                    txtCaso5.setText(R.string.txtCaso5);
                                    txtCaso5.setTextColor(getResources().getColor(R.color.colorAtencionUrgente));
                                    txtCaso5.setTextSize(17);
                                    txtCaso5.setGravity(Gravity.CENTER);
                                    lyMensajes.addView(txtCaso5);
                                    presionoContactoSiFam = true;
                                }else if (chbNinguna.isChecked()){
                                    txtCaso4.setLayoutParams(new ActionBar.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                                    txtCaso4.setText(R.string.txtCaso4);
                                    txtCaso4.setTextColor(getResources().getColor(R.color.colorComuniquese));
                                    txtCaso4.setTextSize(17);
                                    txtCaso4.setGravity(Gravity.CENTER);
                                    lyMensajes.addView(txtCaso4);
                                    presionoContactoSiFam = true;
                                }
                            }
                        }else if (chbDifRespirar.isChecked()){
                            if (chbDifHablar.isChecked() || chbFaltaAire.isChecked() || chbDolorToracico.isChecked()){
                                txtCaso5.setLayoutParams(new ActionBar.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                                txtCaso5.setText(R.string.txtCaso5);
                                txtCaso5.setTextColor(getResources().getColor(R.color.colorAtencionUrgente));
                                txtCaso5.setTextSize(17);
                                txtCaso5.setGravity(Gravity.CENTER);
                                lyMensajes.addView(txtCaso5);
                                presionoContactoSiFam = true;
                            }else if (chbNinguna.isChecked()){
                                if (chbMayor60.isChecked() || chbDiabetes.isChecked()
                                        ||chbHipertension.isChecked() || chbEnfCorazon.isChecked()
                                        ||chbEnfPulmonar.isChecked() || chbEnfRinones.isChecked()
                                        ||chbEnfHigado.isChecked() || chbCancer.isChecked()
                                        ||chbInmunoPresion.isChecked() || chbEnfAutInmunes.isChecked()){
                                    txtCaso5.setLayoutParams(new ActionBar.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                                    txtCaso5.setText(R.string.txtCaso5);
                                    txtCaso5.setTextColor(getResources().getColor(R.color.colorAtencionUrgente));
                                    txtCaso5.setTextSize(17);
                                    txtCaso5.setGravity(Gravity.CENTER);
                                    lyMensajes.addView(txtCaso5);
                                    presionoContactoSiFam = true;
                                }else if (chbNingunoCondiciones.isChecked()){
                                    txtCaso4.setLayoutParams(new ActionBar.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                                    txtCaso4.setText(R.string.txtCaso4);
                                    txtCaso4.setTextColor(getResources().getColor(R.color.colorComuniquese));
                                    txtCaso4.setTextSize(17);
                                    txtCaso4.setGravity(Gravity.CENTER);
                                    lyMensajes.addView(txtCaso4);
                                    presionoContactoSiFam = true;
                                }
                            }
                        }

                        presionoContactoNoFam = false;
                        presionoContactoSiFam = true;

                    }else if(presionoContactoDescFam){
                        lyMensajes.removeAllViews();

                        if (chbFiebre.isChecked() || chbTos.isChecked() || chbGarganta.isChecked()
                                || chbDiarrea.isChecked() || chbCabeza.isChecked() || chbNasal.isChecked()
                                || chbPerdOlfato.isChecked() || chbPerdGusto.isChecked()
                                ||chbConjuntivitis.isChecked() || chbMalGeneral.isChecked()) {
                            if (!chbDifRespirar.isChecked()) {
                                if (chbNingunoCondiciones.isChecked()){
                                    txtCaso2.setLayoutParams(new ActionBar.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                                    txtCaso2.setText(R.string.txtCaso2);
                                    txtCaso2.setTextColor(getResources().getColor(R.color.colorComuniquese));
                                    txtCaso2.setTextSize(17);
                                    txtCaso2.setGravity(Gravity.CENTER);
                                    lyMensajes.addView(txtCaso2);
                                    presionoContactoSiFam = true;
                                }else if (chbMayor60.isChecked() || chbDiabetes.isChecked()
                                        ||chbHipertension.isChecked() || chbEnfCorazon.isChecked()
                                        ||chbEnfPulmonar.isChecked() || chbEnfRinones.isChecked()
                                        ||chbEnfHigado.isChecked() || chbCancer.isChecked()
                                        ||chbInmunoPresion.isChecked() || chbEnfAutInmunes.isChecked()){
                                    txtCaso2.setLayoutParams(new ActionBar.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                                    txtCaso2.setText(R.string.txtCaso2);
                                    txtCaso2.setTextColor(getResources().getColor(R.color.colorComuniquese));
                                    txtCaso2.setTextSize(17);
                                    txtCaso2.setGravity(Gravity.CENTER);
                                    lyMensajes.addView(txtCaso2);
                                    presionoContactoSiFam = true;
                                }
                            }else {
                                if (chbDifHablar.isChecked() || chbFaltaAire.isChecked() || chbDolorToracico.isChecked()) {
                                    txtCaso5.setLayoutParams(new ActionBar.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                                    txtCaso5.setText(R.string.txtCaso5);
                                    txtCaso5.setTextColor(getResources().getColor(R.color.colorAtencionUrgente));
                                    txtCaso5.setTextSize(17);
                                    txtCaso5.setGravity(Gravity.CENTER);
                                    lyMensajes.addView(txtCaso5);
                                    presionoContactoSiFam = true;
                                }
                            }
                        }else if (chbDifRespirar.isChecked()){
                            if (chbDifHablar.isChecked() || chbFaltaAire.isChecked() || chbDolorToracico.isChecked()){
                                txtCaso5.setLayoutParams(new ActionBar.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                                txtCaso5.setText(R.string.txtCaso5);
                                txtCaso5.setTextColor(getResources().getColor(R.color.colorAtencionUrgente));
                                txtCaso5.setTextSize(17);
                                txtCaso5.setGravity(Gravity.CENTER);
                                lyMensajes.addView(txtCaso5);
                                presionoContactoSiFam = true;
                            }else if (chbNinguna.isChecked()){
                                if (chbMayor60.isChecked() || chbDiabetes.isChecked()
                                        ||chbHipertension.isChecked() || chbEnfCorazon.isChecked()
                                        ||chbEnfPulmonar.isChecked() || chbEnfRinones.isChecked()
                                        ||chbEnfHigado.isChecked() || chbCancer.isChecked()
                                        ||chbInmunoPresion.isChecked() || chbEnfAutInmunes.isChecked()){
                                    txtCaso5.setLayoutParams(new ActionBar.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                                    txtCaso5.setText(R.string.txtCaso5);
                                    txtCaso5.setTextColor(getResources().getColor(R.color.colorAtencionUrgente));
                                    txtCaso5.setTextSize(17);
                                    txtCaso5.setGravity(Gravity.CENTER);
                                    lyMensajes.addView(txtCaso5);
                                    presionoContactoSiFam = true;
                                }else if (chbNingunoCondiciones.isChecked()){
                                    txtCaso4.setLayoutParams(new ActionBar.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                                    txtCaso4.setText(R.string.txtCaso4);
                                    txtCaso4.setTextColor(getResources().getColor(R.color.colorComuniquese));
                                    txtCaso4.setTextSize(17);
                                    txtCaso4.setGravity(Gravity.CENTER);
                                    lyMensajes.addView(txtCaso4);
                                    presionoContactoSiFam = true;
                                }
                            }
                        }

                        presionoContactoDescFam = false;
                        presionoContactoSiFam = true;

                    }else {

                        if (chbFiebre.isChecked() || chbTos.isChecked() || chbGarganta.isChecked()
                                || chbDiarrea.isChecked() || chbCabeza.isChecked() || chbNasal.isChecked()
                                || chbPerdOlfato.isChecked() || chbPerdGusto.isChecked()
                                ||chbConjuntivitis.isChecked() || chbMalGeneral.isChecked()) {
                            if (!chbDifRespirar.isChecked()) {
                                if (chbNingunoCondiciones.isChecked()){
                                    txtCaso2.setLayoutParams(new ActionBar.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                                    txtCaso2.setText(R.string.txtCaso2);
                                    txtCaso2.setTextColor(getResources().getColor(R.color.colorComuniquese));
                                    txtCaso2.setTextSize(17);
                                    txtCaso2.setGravity(Gravity.CENTER);
                                    lyMensajes.addView(txtCaso2);
                                    presionoContactoSiFam = true;
                                }else if (chbMayor60.isChecked() || chbDiabetes.isChecked()
                                        ||chbHipertension.isChecked() || chbEnfCorazon.isChecked()
                                        ||chbEnfPulmonar.isChecked() || chbEnfRinones.isChecked()
                                        ||chbEnfHigado.isChecked() || chbCancer.isChecked()
                                        ||chbInmunoPresion.isChecked() || chbEnfAutInmunes.isChecked()){
                                    txtCaso2.setLayoutParams(new ActionBar.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                                    txtCaso2.setText(R.string.txtCaso2);
                                    txtCaso2.setTextColor(getResources().getColor(R.color.colorComuniquese));
                                    txtCaso2.setTextSize(17);
                                    txtCaso2.setGravity(Gravity.CENTER);
                                    lyMensajes.addView(txtCaso2);
                                    presionoContactoSiFam = true;
                                }
                            }else {
                                if (chbDifHablar.isChecked() || chbFaltaAire.isChecked() || chbDolorToracico.isChecked()) {
                                    txtCaso5.setLayoutParams(new ActionBar.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                                    txtCaso5.setText(R.string.txtCaso5);
                                    txtCaso5.setTextColor(getResources().getColor(R.color.colorAtencionUrgente));
                                    txtCaso5.setTextSize(17);
                                    txtCaso5.setGravity(Gravity.CENTER);
                                    lyMensajes.addView(txtCaso5);
                                    presionoContactoSiFam = true;
                                }
                            }
                        }else if (chbDifRespirar.isChecked()){
                            if (chbDifHablar.isChecked() || chbFaltaAire.isChecked() || chbDolorToracico.isChecked()){
                                txtCaso5.setLayoutParams(new ActionBar.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                                txtCaso5.setText(R.string.txtCaso5);
                                txtCaso5.setTextColor(getResources().getColor(R.color.colorAtencionUrgente));
                                txtCaso5.setTextSize(17);
                                txtCaso5.setGravity(Gravity.CENTER);
                                lyMensajes.addView(txtCaso5);
                                presionoContactoSiFam = true;
                            }else if (chbNinguna.isChecked()){
                                if (chbMayor60.isChecked() || chbDiabetes.isChecked()
                                        ||chbHipertension.isChecked() || chbEnfCorazon.isChecked()
                                        ||chbEnfPulmonar.isChecked() || chbEnfRinones.isChecked()
                                        ||chbEnfHigado.isChecked() || chbCancer.isChecked()
                                        ||chbInmunoPresion.isChecked() || chbEnfAutInmunes.isChecked()){
                                    txtCaso5.setLayoutParams(new ActionBar.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                                    txtCaso5.setText(R.string.txtCaso5);
                                    txtCaso5.setTextColor(getResources().getColor(R.color.colorAtencionUrgente));
                                    txtCaso5.setTextSize(17);
                                    txtCaso5.setGravity(Gravity.CENTER);
                                    lyMensajes.addView(txtCaso5);
                                    presionoContactoSiFam = true;
                                }else if (chbNingunoCondiciones.isChecked()){
                                    txtCaso4.setLayoutParams(new ActionBar.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                                    txtCaso4.setText(R.string.txtCaso4);
                                    txtCaso4.setTextColor(getResources().getColor(R.color.colorComuniquese));
                                    txtCaso4.setTextSize(17);
                                    txtCaso4.setGravity(Gravity.CENTER);
                                    lyMensajes.addView(txtCaso4);
                                    presionoContactoSiFam = true;
                                }
                            }
                        }
                        presionoContactoSiFam = true;
                    }
                }else if (checkedId == R.id.rbContactoNoFam && !presionoContactoNoFam){

                    if (presionoContactoSiFam){
                        lyMensajes.removeAllViews();

                        if (chbFiebre.isChecked() || chbTos.isChecked() || chbGarganta.isChecked()
                                || chbDiarrea.isChecked() || chbCabeza.isChecked() || chbNasal.isChecked()
                                || chbPerdOlfato.isChecked() || chbPerdGusto.isChecked()
                                ||chbConjuntivitis.isChecked() || chbMalGeneral.isChecked()){
                            if (!chbDifRespirar.isChecked()){
                                if (chbNingunoCondiciones.isChecked()){
                                    txtCaso1.setLayoutParams(new ActionBar.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                                    txtCaso1.setText(R.string.txtCaso1);
                                    txtCaso1.setTextColor(getResources().getColor(R.color.colorNoRequiereHospitalizacion));
                                    txtCaso1.setTextSize(17);
                                    txtCaso1.setGravity(Gravity.CENTER);
                                    lyMensajes.addView(txtCaso1);
                                    presionoContactoNoFam = true;
                                }else if (chbMayor60.isChecked() || chbDiabetes.isChecked()
                                        ||chbHipertension.isChecked() || chbEnfCorazon.isChecked()
                                        ||chbEnfPulmonar.isChecked() || chbEnfRinones.isChecked()
                                        ||chbEnfHigado.isChecked() || chbCancer.isChecked()
                                        ||chbInmunoPresion.isChecked() || chbEnfAutInmunes.isChecked()){
                                    txtCaso3.setLayoutParams(new ActionBar.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                                    txtCaso3.setText(R.string.txtCaso3);
                                    txtCaso3.setTextColor(getResources().getColor(R.color.colorComuniquese));
                                    txtCaso3.setTextSize(17);
                                    txtCaso3.setGravity(Gravity.CENTER);
                                    lyMensajes.addView(txtCaso3);
                                    presionoContactoNoFam = true;
                                }
                            }else {
                                if (chbDifHablar.isChecked() || chbFaltaAire.isChecked() || chbDolorToracico.isChecked()){
                                    txtCaso5.setLayoutParams(new ActionBar.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                                    txtCaso5.setText(R.string.txtCaso5);
                                    txtCaso5.setTextColor(getResources().getColor(R.color.colorAtencionUrgente));
                                    txtCaso5.setTextSize(17);
                                    txtCaso5.setGravity(Gravity.CENTER);
                                    lyMensajes.addView(txtCaso5);
                                    presionoContactoNoFam = true;
                                }else if (chbNinguna.isChecked()){
                                    txtCaso4.setLayoutParams(new ActionBar.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                                    txtCaso4.setText(R.string.txtCaso4);
                                    txtCaso4.setTextColor(getResources().getColor(R.color.colorComuniquese));
                                    txtCaso4.setTextSize(17);
                                    txtCaso4.setGravity(Gravity.CENTER);
                                    lyMensajes.addView(txtCaso4);
                                    presionoContactoNoFam = true;
                                }
                            }
                        }else if (chbDifRespirar.isChecked()){
                            if (chbDifHablar.isChecked() || chbFaltaAire.isChecked() || chbDolorToracico.isChecked()){
                                txtCaso5.setLayoutParams(new ActionBar.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                                txtCaso5.setText(R.string.txtCaso5);
                                txtCaso5.setTextColor(getResources().getColor(R.color.colorAtencionUrgente));
                                txtCaso5.setTextSize(17);
                                txtCaso5.setGravity(Gravity.CENTER);
                                lyMensajes.addView(txtCaso5);
                                presionoContactoNoFam = true;
                            }else if (chbNinguna.isChecked()){
                                txtCaso4.setLayoutParams(new ActionBar.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                                txtCaso4.setText(R.string.txtCaso4);
                                txtCaso4.setTextColor(getResources().getColor(R.color.colorComuniquese));
                                txtCaso4.setTextSize(17);
                                txtCaso4.setGravity(Gravity.CENTER);
                                lyMensajes.addView(txtCaso4);
                                presionoContactoNoFam = true;
                            }
                        }

                        presionoContactoSiFam = false;
                        presionoContactoNoFam = true;
                    }else if (presionoContactoDescFam){
                        lyMensajes.removeAllViews();

                        if (chbFiebre.isChecked() || chbTos.isChecked() || chbGarganta.isChecked()
                                || chbDiarrea.isChecked() || chbCabeza.isChecked() || chbNasal.isChecked()
                                || chbPerdOlfato.isChecked() || chbPerdGusto.isChecked()
                                ||chbConjuntivitis.isChecked() || chbMalGeneral.isChecked()){
                            if (!chbDifRespirar.isChecked()){
                                if (chbNingunoCondiciones.isChecked()){
                                    txtCaso1.setLayoutParams(new ActionBar.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                                    txtCaso1.setText(R.string.txtCaso1);
                                    txtCaso1.setTextColor(getResources().getColor(R.color.colorNoRequiereHospitalizacion));
                                    txtCaso1.setTextSize(17);
                                    txtCaso1.setGravity(Gravity.CENTER);
                                    lyMensajes.addView(txtCaso1);
                                    presionoContactoNoFam = true;
                                }else if (chbMayor60.isChecked() || chbDiabetes.isChecked()
                                        ||chbHipertension.isChecked() || chbEnfCorazon.isChecked()
                                        ||chbEnfPulmonar.isChecked() || chbEnfRinones.isChecked()
                                        ||chbEnfHigado.isChecked() || chbCancer.isChecked()
                                        ||chbInmunoPresion.isChecked() || chbEnfAutInmunes.isChecked()){
                                    txtCaso3.setLayoutParams(new ActionBar.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                                    txtCaso3.setText(R.string.txtCaso3);
                                    txtCaso3.setTextColor(getResources().getColor(R.color.colorComuniquese));
                                    txtCaso3.setTextSize(17);
                                    txtCaso3.setGravity(Gravity.CENTER);
                                    lyMensajes.addView(txtCaso3);
                                    presionoContactoNoFam = true;
                                }
                            }else {
                                if (chbDifHablar.isChecked() || chbFaltaAire.isChecked() || chbDolorToracico.isChecked()){
                                    txtCaso5.setLayoutParams(new ActionBar.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                                    txtCaso5.setText(R.string.txtCaso5);
                                    txtCaso5.setTextColor(getResources().getColor(R.color.colorAtencionUrgente));
                                    txtCaso5.setTextSize(17);
                                    txtCaso5.setGravity(Gravity.CENTER);
                                    lyMensajes.addView(txtCaso5);
                                    presionoContactoNoFam = true;
                                }else if (chbNinguna.isChecked()){
                                    txtCaso4.setLayoutParams(new ActionBar.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                                    txtCaso4.setText(R.string.txtCaso4);
                                    txtCaso4.setTextColor(getResources().getColor(R.color.colorComuniquese));
                                    txtCaso4.setTextSize(17);
                                    txtCaso4.setGravity(Gravity.CENTER);
                                    lyMensajes.addView(txtCaso4);
                                    presionoContactoNoFam = true;
                                }
                            }
                        }else if (chbDifRespirar.isChecked()){
                            if (chbDifHablar.isChecked() || chbFaltaAire.isChecked() || chbDolorToracico.isChecked()){
                                txtCaso5.setLayoutParams(new ActionBar.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                                txtCaso5.setText(R.string.txtCaso5);
                                txtCaso5.setTextColor(getResources().getColor(R.color.colorAtencionUrgente));
                                txtCaso5.setTextSize(17);
                                txtCaso5.setGravity(Gravity.CENTER);
                                lyMensajes.addView(txtCaso5);
                                presionoContactoNoFam = true;
                            }else if (chbNinguna.isChecked()){
                                txtCaso4.setLayoutParams(new ActionBar.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                                txtCaso4.setText(R.string.txtCaso4);
                                txtCaso4.setTextColor(getResources().getColor(R.color.colorComuniquese));
                                txtCaso4.setTextSize(17);
                                txtCaso4.setGravity(Gravity.CENTER);
                                lyMensajes.addView(txtCaso4);
                                presionoContactoNoFam = true;
                            }
                        }

                        presionoContactoDescFam = false;
                        presionoContactoNoFam = true;
                    }else {
                        if (chbFiebre.isChecked() || chbTos.isChecked() || chbGarganta.isChecked()
                                || chbDiarrea.isChecked() || chbCabeza.isChecked() || chbNasal.isChecked()
                                || chbPerdOlfato.isChecked() || chbPerdGusto.isChecked()
                                ||chbConjuntivitis.isChecked() || chbMalGeneral.isChecked()){
                            if (!chbDifRespirar.isChecked()){
                                if (chbNingunoCondiciones.isChecked()){
                                    txtCaso1.setLayoutParams(new ActionBar.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                                    txtCaso1.setText(R.string.txtCaso1);
                                    txtCaso1.setTextColor(getResources().getColor(R.color.colorNoRequiereHospitalizacion));
                                    txtCaso1.setTextSize(17);
                                    txtCaso1.setGravity(Gravity.CENTER);
                                    lyMensajes.addView(txtCaso1);
                                    presionoContactoNoFam = true;
                                }else if (chbMayor60.isChecked() || chbDiabetes.isChecked()
                                        ||chbHipertension.isChecked() || chbEnfCorazon.isChecked()
                                        ||chbEnfPulmonar.isChecked() || chbEnfRinones.isChecked()
                                        ||chbEnfHigado.isChecked() || chbCancer.isChecked()
                                        ||chbInmunoPresion.isChecked() || chbEnfAutInmunes.isChecked()){
                                    txtCaso3.setLayoutParams(new ActionBar.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                                    txtCaso3.setText(R.string.txtCaso3);
                                    txtCaso3.setTextColor(getResources().getColor(R.color.colorComuniquese));
                                    txtCaso3.setTextSize(17);
                                    txtCaso3.setGravity(Gravity.CENTER);
                                    lyMensajes.addView(txtCaso3);
                                    presionoContactoNoFam = true;
                                }
                            }else {
                                if (chbDifHablar.isChecked() || chbFaltaAire.isChecked() || chbDolorToracico.isChecked()){
                                    txtCaso5.setLayoutParams(new ActionBar.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                                    txtCaso5.setText(R.string.txtCaso5);
                                    txtCaso5.setTextColor(getResources().getColor(R.color.colorAtencionUrgente));
                                    txtCaso5.setTextSize(17);
                                    txtCaso5.setGravity(Gravity.CENTER);
                                    lyMensajes.addView(txtCaso5);
                                    presionoContactoNoFam = true;
                                }else if (chbNinguna.isChecked()){
                                    txtCaso4.setLayoutParams(new ActionBar.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                                    txtCaso4.setText(R.string.txtCaso4);
                                    txtCaso4.setTextColor(getResources().getColor(R.color.colorComuniquese));
                                    txtCaso4.setTextSize(17);
                                    txtCaso4.setGravity(Gravity.CENTER);
                                    lyMensajes.addView(txtCaso4);
                                    presionoContactoNoFam = true;
                                }
                            }
                        }else if (chbDifRespirar.isChecked()){
                            if (chbDifHablar.isChecked() || chbFaltaAire.isChecked() || chbDolorToracico.isChecked()){
                                txtCaso5.setLayoutParams(new ActionBar.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                                txtCaso5.setText(R.string.txtCaso5);
                                txtCaso5.setTextColor(getResources().getColor(R.color.colorAtencionUrgente));
                                txtCaso5.setTextSize(17);
                                txtCaso5.setGravity(Gravity.CENTER);
                                lyMensajes.addView(txtCaso5);
                                presionoContactoNoFam = true;
                            }else if (chbNinguna.isChecked()){
                                txtCaso4.setLayoutParams(new ActionBar.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                                txtCaso4.setText(R.string.txtCaso4);
                                txtCaso4.setTextColor(getResources().getColor(R.color.colorComuniquese));
                                txtCaso4.setTextSize(17);
                                txtCaso4.setGravity(Gravity.CENTER);
                                lyMensajes.addView(txtCaso4);
                                presionoContactoNoFam = true;
                            }
                        }
                    }
                }else if (checkedId == R.id.rbContactoDescFam && !presionoContactoDescFam){

                    if (presionoContactoSiFam){
                        lyMensajes.removeAllViews();

                        if (chbFiebre.isChecked() || chbTos.isChecked() || chbGarganta.isChecked()
                                || chbDiarrea.isChecked() || chbCabeza.isChecked() || chbNasal.isChecked()
                                || chbPerdOlfato.isChecked() || chbPerdGusto.isChecked()
                                ||chbConjuntivitis.isChecked() || chbMalGeneral.isChecked()){
                            if (!chbDifRespirar.isChecked()){
                                if (chbNingunoCondiciones.isChecked()){
                                    txtCaso1.setLayoutParams(new ActionBar.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                                    txtCaso1.setText(R.string.txtCaso1);
                                    txtCaso1.setTextColor(getResources().getColor(R.color.colorNoRequiereHospitalizacion));
                                    txtCaso1.setTextSize(17);
                                    txtCaso1.setGravity(Gravity.CENTER);
                                    lyMensajes.addView(txtCaso1);
                                    presionoContactoDescFam = true;
                                }else if (chbMayor60.isChecked() || chbDiabetes.isChecked()
                                        ||chbHipertension.isChecked() || chbEnfCorazon.isChecked()
                                        ||chbEnfPulmonar.isChecked() || chbEnfRinones.isChecked()
                                        ||chbEnfHigado.isChecked() || chbCancer.isChecked()
                                        ||chbInmunoPresion.isChecked() || chbEnfAutInmunes.isChecked()){
                                    txtCaso3.setLayoutParams(new ActionBar.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                                    txtCaso3.setText(R.string.txtCaso3);
                                    txtCaso3.setTextColor(getResources().getColor(R.color.colorComuniquese));
                                    txtCaso3.setTextSize(17);
                                    txtCaso3.setGravity(Gravity.CENTER);
                                    lyMensajes.addView(txtCaso3);
                                    presionoContactoDescFam = true;
                                }
                            }else {
                                if (chbDifHablar.isChecked() || chbFaltaAire.isChecked() || chbDolorToracico.isChecked()){
                                    txtCaso5.setLayoutParams(new ActionBar.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                                    txtCaso5.setText(R.string.txtCaso5);
                                    txtCaso5.setTextColor(getResources().getColor(R.color.colorAtencionUrgente));
                                    txtCaso5.setTextSize(17);
                                    txtCaso5.setGravity(Gravity.CENTER);
                                    lyMensajes.addView(txtCaso5);
                                    presionoContactoDescFam = true;
                                }else if (chbNinguna.isChecked()){
                                    txtCaso4.setLayoutParams(new ActionBar.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                                    txtCaso4.setText(R.string.txtCaso4);
                                    txtCaso4.setTextColor(getResources().getColor(R.color.colorComuniquese));
                                    txtCaso4.setTextSize(17);
                                    txtCaso4.setGravity(Gravity.CENTER);
                                    lyMensajes.addView(txtCaso4);
                                    presionoContactoDescFam = true;
                                }
                            }
                        }else if (chbDifRespirar.isChecked()){
                            if (chbDifHablar.isChecked() || chbFaltaAire.isChecked() || chbDolorToracico.isChecked()){
                                txtCaso5.setLayoutParams(new ActionBar.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                                txtCaso5.setText(R.string.txtCaso5);
                                txtCaso5.setTextColor(getResources().getColor(R.color.colorAtencionUrgente));
                                txtCaso5.setTextSize(17);
                                txtCaso5.setGravity(Gravity.CENTER);
                                lyMensajes.addView(txtCaso5);
                                presionoContactoDescFam = true;
                            }else if (chbNinguna.isChecked()){
                                txtCaso4.setLayoutParams(new ActionBar.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                                txtCaso4.setText(R.string.txtCaso4);
                                txtCaso4.setTextColor(getResources().getColor(R.color.colorComuniquese));
                                txtCaso4.setTextSize(17);
                                txtCaso4.setGravity(Gravity.CENTER);
                                lyMensajes.addView(txtCaso4);
                                presionoContactoDescFam = true;
                            }
                        }

                        presionoContactoSiFam = false;
                        presionoContactoDescFam = true;
                    }else if (presionoContactoNoFam){
                        lyMensajes.removeAllViews();
                        if (chbFiebre.isChecked() || chbTos.isChecked() || chbGarganta.isChecked()
                                || chbDiarrea.isChecked() || chbCabeza.isChecked() || chbNasal.isChecked()
                                || chbPerdOlfato.isChecked() || chbPerdGusto.isChecked()
                                ||chbConjuntivitis.isChecked() || chbMalGeneral.isChecked()){
                            if (!chbDifRespirar.isChecked()){
                                if (chbNingunoCondiciones.isChecked()){
                                    txtCaso1.setLayoutParams(new ActionBar.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                                    txtCaso1.setText(R.string.txtCaso1);
                                    txtCaso1.setTextColor(getResources().getColor(R.color.colorNoRequiereHospitalizacion));
                                    txtCaso1.setTextSize(17);
                                    txtCaso1.setGravity(Gravity.CENTER);
                                    lyMensajes.addView(txtCaso1);
                                    presionoContactoDescFam = true;
                                }else if (chbMayor60.isChecked() || chbDiabetes.isChecked()
                                        ||chbHipertension.isChecked() || chbEnfCorazon.isChecked()
                                        ||chbEnfPulmonar.isChecked() || chbEnfRinones.isChecked()
                                        ||chbEnfHigado.isChecked() || chbCancer.isChecked()
                                        ||chbInmunoPresion.isChecked() || chbEnfAutInmunes.isChecked()){
                                    txtCaso3.setLayoutParams(new ActionBar.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                                    txtCaso3.setText(R.string.txtCaso3);
                                    txtCaso3.setTextColor(getResources().getColor(R.color.colorComuniquese));
                                    txtCaso3.setTextSize(17);
                                    txtCaso3.setGravity(Gravity.CENTER);
                                    lyMensajes.addView(txtCaso3);
                                    presionoContactoDescFam = true;
                                }
                            }else {
                                if (chbDifHablar.isChecked() || chbFaltaAire.isChecked() || chbDolorToracico.isChecked()){
                                    txtCaso5.setLayoutParams(new ActionBar.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                                    txtCaso5.setText(R.string.txtCaso5);
                                    txtCaso5.setTextColor(getResources().getColor(R.color.colorAtencionUrgente));
                                    txtCaso5.setTextSize(17);
                                    txtCaso5.setGravity(Gravity.CENTER);
                                    lyMensajes.addView(txtCaso5);
                                    presionoContactoDescFam = true;
                                }else if (chbNinguna.isChecked()){
                                    txtCaso4.setLayoutParams(new ActionBar.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                                    txtCaso4.setText(R.string.txtCaso4);
                                    txtCaso4.setTextColor(getResources().getColor(R.color.colorComuniquese));
                                    txtCaso4.setTextSize(17);
                                    txtCaso4.setGravity(Gravity.CENTER);
                                    lyMensajes.addView(txtCaso4);
                                    presionoContactoDescFam = true;
                                }
                            }
                        }else if (chbDifRespirar.isChecked()){
                            if (chbDifHablar.isChecked() || chbFaltaAire.isChecked() || chbDolorToracico.isChecked()){
                                txtCaso5.setLayoutParams(new ActionBar.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                                txtCaso5.setText(R.string.txtCaso5);
                                txtCaso5.setTextColor(getResources().getColor(R.color.colorAtencionUrgente));
                                txtCaso5.setTextSize(17);
                                txtCaso5.setGravity(Gravity.CENTER);
                                lyMensajes.addView(txtCaso5);
                                presionoContactoDescFam = true;
                            }else if (chbNinguna.isChecked()){
                                txtCaso4.setLayoutParams(new ActionBar.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                                txtCaso4.setText(R.string.txtCaso4);
                                txtCaso4.setTextColor(getResources().getColor(R.color.colorComuniquese));
                                txtCaso4.setTextSize(17);
                                txtCaso4.setGravity(Gravity.CENTER);
                                lyMensajes.addView(txtCaso4);
                                presionoContactoDescFam = true;
                            }
                        }
                        presionoContactoNoFam = false;
                        presionoContactoDescFam = true;
                    }else {
                        if (chbFiebre.isChecked() || chbTos.isChecked() || chbGarganta.isChecked()
                                || chbDiarrea.isChecked() || chbCabeza.isChecked() || chbNasal.isChecked()
                                || chbPerdOlfato.isChecked() || chbPerdGusto.isChecked()
                                ||chbConjuntivitis.isChecked() || chbMalGeneral.isChecked()){
                            if (!chbDifRespirar.isChecked()){
                                if (chbNingunoCondiciones.isChecked()){
                                    txtCaso1.setLayoutParams(new ActionBar.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                                    txtCaso1.setText(R.string.txtCaso1);
                                    txtCaso1.setTextColor(getResources().getColor(R.color.colorNoRequiereHospitalizacion));
                                    txtCaso1.setTextSize(17);
                                    txtCaso1.setGravity(Gravity.CENTER);
                                    lyMensajes.addView(txtCaso1);
                                    presionoContactoDescFam = true;
                                }else if (chbMayor60.isChecked() || chbDiabetes.isChecked()
                                        ||chbHipertension.isChecked() || chbEnfCorazon.isChecked()
                                        ||chbEnfPulmonar.isChecked() || chbEnfRinones.isChecked()
                                        ||chbEnfHigado.isChecked() || chbCancer.isChecked()
                                        ||chbInmunoPresion.isChecked() || chbEnfAutInmunes.isChecked()){
                                    txtCaso3.setLayoutParams(new ActionBar.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                                    txtCaso3.setText(R.string.txtCaso3);
                                    txtCaso3.setTextColor(getResources().getColor(R.color.colorComuniquese));
                                    txtCaso3.setTextSize(17);
                                    txtCaso3.setGravity(Gravity.CENTER);
                                    lyMensajes.addView(txtCaso3);
                                    presionoContactoDescFam = true;
                                }
                            }else {
                                if (chbDifHablar.isChecked() || chbFaltaAire.isChecked() || chbDolorToracico.isChecked()){
                                    txtCaso5.setLayoutParams(new ActionBar.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                                    txtCaso5.setText(R.string.txtCaso5);
                                    txtCaso5.setTextColor(getResources().getColor(R.color.colorAtencionUrgente));
                                    txtCaso5.setTextSize(17);
                                    txtCaso5.setGravity(Gravity.CENTER);
                                    lyMensajes.addView(txtCaso5);
                                    presionoContactoDescFam = true;
                                }else if (chbNinguna.isChecked()){
                                    txtCaso4.setLayoutParams(new ActionBar.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                                    txtCaso4.setText(R.string.txtCaso4);
                                    txtCaso4.setTextColor(getResources().getColor(R.color.colorComuniquese));
                                    txtCaso4.setTextSize(17);
                                    txtCaso4.setGravity(Gravity.CENTER);
                                    lyMensajes.addView(txtCaso4);
                                    presionoContactoDescFam = true;
                                }
                            }
                        }else if (chbDifRespirar.isChecked()){
                            if (chbDifHablar.isChecked() || chbFaltaAire.isChecked() || chbDolorToracico.isChecked()){
                                txtCaso5.setLayoutParams(new ActionBar.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                                txtCaso5.setText(R.string.txtCaso5);
                                txtCaso5.setTextColor(getResources().getColor(R.color.colorAtencionUrgente));
                                txtCaso5.setTextSize(17);
                                txtCaso5.setGravity(Gravity.CENTER);
                                lyMensajes.addView(txtCaso5);
                                presionoContactoDescFam = true;
                            }else if (chbNinguna.isChecked()){
                                txtCaso4.setLayoutParams(new ActionBar.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                                txtCaso4.setText(R.string.txtCaso4);
                                txtCaso4.setTextColor(getResources().getColor(R.color.colorComuniquese));
                                txtCaso4.setTextSize(17);
                                txtCaso4.setGravity(Gravity.CENTER);
                                lyMensajes.addView(txtCaso4);
                                presionoContactoDescFam = true;
                            }
                        }
                    }
                }
            }
        });

///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

        chbNinguna.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    chbDifHablar.setEnabled(false);
                    chbFaltaAire.setEnabled(false);
                    chbDolorToracico.setEnabled(false);
                    if (chbDifHablar.isChecked() || chbDolorToracico.isChecked() || chbFaltaAire.isChecked()){
                        chbDifHablar.setChecked(false);
                        chbFaltaAire.setChecked(false);
                        chbDolorToracico.setChecked(false);
                    }
                    presionoNinguna = true;
                }else{
//                    rbgContactoFam.removeView(txtComuniqueProfesional);
                    lyMensajes.removeAllViews();
                    chbDifHablar.setEnabled(true);
                    chbFaltaAire.setEnabled(true);
                    chbDolorToracico.setEnabled(true);
                    presionoNinguna = false;
                }
            }
        });

        chbNingunoCondiciones.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    chbMayor60.setEnabled(false);
                    chbMayor60.setChecked(false);

                    chbDiabetes.setEnabled(false);
                    chbDiabetes.setChecked(false);

                    chbHipertension.setEnabled(false);
                    chbHipertension.setChecked(false);

                    chbEnfCorazon.setEnabled(false);
                    chbEnfCorazon.setChecked(false);

                    chbEnfPulmonar.setEnabled(false);
                    chbEnfPulmonar.setChecked(false);

                    chbEnfRinones.setEnabled(false);
                    chbEnfRinones.setChecked(false);

                    chbEnfHigado.setEnabled(false);
                    chbEnfHigado.setChecked(false);

                    chbCancer.setEnabled(false);
                    chbCancer.setChecked(false);

                    chbInmunoPresion.setEnabled(false);
                    chbInmunoPresion.setChecked(false);

                    chbEnfAutInmunes.setEnabled(false);
                    chbEnfAutInmunes.setChecked(false);

                }else{
                    chbMayor60.setEnabled(false);
                    chbDiabetes.setEnabled(true);
                    chbHipertension.setEnabled(true);
                    chbEnfCorazon.setEnabled(true);
                    chbEnfPulmonar.setEnabled(true);
                    chbEnfRinones.setEnabled(true);
                    chbEnfHigado.setEnabled(true);
                    chbCancer.setEnabled(true);
                    chbInmunoPresion.setEnabled(true);
                    chbEnfAutInmunes.setEnabled(true);
                }
            }
        });

    } //FIN METODO ONCREATE

    //***********************************VALIDACIONES CAMPOS DE TEXTO Y CHECKBOXES************************************************

    public boolean validarSeleccionSintomas(){
        int contador = 0;
        if (chbFiebre.isChecked() || chbTos.isChecked() || chbGarganta.isChecked() || chbDiarrea.isChecked()
                || chbCabeza.isChecked() || chbNasal.isChecked() || chbPerdOlfato.isChecked()
                || chbPerdGusto.isChecked() || chbDifRespirar.isChecked() || chbConjuntivitis.isChecked()
                || chbMalGeneral.isChecked()){
            contador ++;
        }
        if (contador > 0){
            chbMalGeneral.setError(null);
            return true;
        }else {
            chbMalGeneral.setError("Debe seleccionar al menos una opcióm");
            return false;
        }
    }

    public boolean validarSeleccionCondicionesFamiliar(){
        int contador = 0;
        if (chbMayor60.isChecked() || chbDiabetes.isChecked() || chbHipertension.isChecked()
                || chbEnfCorazon.isChecked() || chbEnfPulmonar.isChecked()
                || chbEnfRinones.isChecked() || chbEnfHigado.isChecked() || chbCancer.isChecked()
                || chbInmunoPresion.isChecked() || chbEnfAutInmunes.isChecked() || chbNingunoCondiciones.isChecked()){
            contador ++;
        }
        if (contador > 0){
            chbNingunoCondiciones.setError(null);
            return true;
        } else {
            chbNingunoCondiciones.setError("Debe seleccionar al menos una opción");
            return false;
        }
    }

    @SuppressLint("ResourceType")
    public boolean validarSeleccionPresentaSintomas(){
        if (rbgContactoFam.getCheckedRadioButtonId() <= 0){
            rbContactoDescFam.setError("Debe seleccionar una opción");
            return false;
        } else {
            rbContactoDescFam.setError(null);
            return true;
        }
    }
    //**********************************FIN DE VALIDACIONES DE CAMPOS DE TEXTO Y CHECKBOXES****************************************



    /**
     * Metodo para guardar datos del familiar
     * @param view vista para escuchar el evento onclick del boton guardar
     */
    public void guardarDatos(View view){
        if (!Utileria.validarApellidos(txtInputApellidosFam) | !Utileria.validarNombres(txtInputNombresFam)
                | !Utileria.validarCedula(txtInputCedulaFamiliar) | !Utileria.validarEdad(txtInputEdadFamiliar)
                | !Utileria.validarSexo(rbgSexoFam, rbFemeninoFam) | !validarSeleccionSintomas()
                | !validarSeleccionCondicionesFamiliar() | !validarSeleccionPresentaSintomas()){

            Toast.makeText(getApplicationContext(), "Lo sentimos, algunos campos o selecciones " +
                    "están vacíos o no son validos sus datos, revíselos por favor" , Toast.LENGTH_LONG).show();

        }else {

            //recoger datos
            String apellidosFam = Utileria.cleanString(txtInputApellidosFam.getEditText().getText().toString().trim().toUpperCase());
            String nombresFam = Utileria.cleanString(txtInputNombresFam.getEditText().getText().toString().trim().toUpperCase());
            String cedulaFam = txtInputCedulaFamiliar.getEditText().getText().toString().trim();
            int edadFam = Integer.parseInt(txtInputEdadFamiliar.getEditText().getText().toString().trim());

            String sexoFam = "";
            if (rbMasculinoFam.isChecked()){
                sexoFam = Utileria.cleanString(rbMasculinoFam.getText().toString().trim());
            }else if (rbFemeninoFam.isChecked()){
                sexoFam = Utileria.cleanString(rbFemeninoFam.getText().toString().trim());
            }

            String sintomasFam = "";
            String difRespirarSevera = "";
            if (chbFiebre.isChecked()){
                sintomasFam += Utileria.cleanString(chbFiebre.getText().toString().trim()) + " - ";
                listaSintomas.add(new Sintoma(cedulaFam,Utileria.cleanString(chbFiebre.getText().toString().trim())));
            }
            if (chbTos.isChecked()){
                sintomasFam += Utileria.cleanString(chbTos.getText().toString().trim()) + " - ";
                listaSintomas.add(new Sintoma(cedulaFam,chbTos.getText().toString().trim()));
            }
            if(chbGarganta.isChecked()){
                sintomasFam += Utileria.cleanString(chbGarganta.getText().toString().trim()) + " - ";
                listaSintomas.add(new Sintoma(cedulaFam,Utileria.cleanString(chbGarganta.getText().toString().trim())));
            }
            if (chbDiarrea.isChecked()){
                sintomasFam += Utileria.cleanString(chbDiarrea.getText().toString().trim()) + " - ";
                listaSintomas.add(new Sintoma(cedulaFam,Utileria.cleanString(chbDiarrea.getText().toString().trim())));
            }
            if (chbCabeza.isChecked()){
                sintomasFam += Utileria.cleanString(chbCabeza.getText().toString().trim()) + " - ";
                listaSintomas.add(new Sintoma(cedulaFam,Utileria.cleanString(chbCabeza.getText().toString().trim())));
            }
            if(chbNasal.isChecked()){
                sintomasFam += Utileria.cleanString(chbNasal.getText().toString().trim()) + " - ";
                listaSintomas.add(new Sintoma(cedulaFam,Utileria.cleanString(chbNasal.getText().toString().trim())));
            }
            if (chbPerdOlfato.isChecked()){
                sintomasFam += Utileria.cleanString(chbPerdOlfato.getText().toString().trim()) + " - ";
                listaSintomas.add(new Sintoma(cedulaFam,Utileria.cleanString(chbPerdOlfato.getText().toString().trim())));
            }
            if (chbPerdGusto.isChecked()){
                sintomasFam += Utileria.cleanString(chbPerdGusto.getText().toString().trim()) + " - ";
                listaSintomas.add(new Sintoma(cedulaFam,Utileria.cleanString(chbPerdGusto.getText().toString().trim())));
            }
            if (chbDifRespirar.isChecked()){
                sintomasFam += Utileria.cleanString(chbDifRespirar.getText().toString().trim()) + " - ";
                listaSintomas.add(new Sintoma(cedulaFam,Utileria.cleanString(chbDifRespirar.getText().toString().trim())));
                if (chbDifHab.isChecked()){
                    difRespirarSevera += Utileria.cleanString(chbDifHab.getText().toString().trim()) + " - ";
                    listaSintomas.add(new Sintoma(cedulaFam,Utileria.cleanString(chbDifHab.getText().toString().trim())));
                }
                if (chbFaltAire.isChecked()){
                    difRespirarSevera += Utileria.cleanString(chbFaltAire.getText().toString().trim()) + " - ";
                    listaSintomas.add(new Sintoma(cedulaFam,Utileria.cleanString(chbFaltAire.getText().toString().trim())));
                }
                if (chbDolToracico.isChecked()){
                    difRespirarSevera += Utileria.cleanString(chbDolToracico.getText().toString().trim()) + " - ";
                    listaSintomas.add(new Sintoma(cedulaFam,Utileria.cleanString(chbDolToracico.getText().toString().trim())));
                }
                if (chbNingDifResp.isChecked()){
                    difRespirarSevera += Utileria.cleanString(chbNingDifResp.getText().toString().trim()) + " - ";
                    listaSintomas.add(new Sintoma(cedulaFam,Utileria.cleanString(chbNingDifResp.getText().toString().trim())));
                }
            }
            if(chbConjuntivitis.isChecked()){
                sintomasFam += Utileria.cleanString(chbConjuntivitis.getText().toString().trim()) + " - ";
                listaSintomas.add(new Sintoma(cedulaFam,Utileria.cleanString(chbConjuntivitis.getText().toString().trim())));
            }
            if (chbMalGeneral.isChecked()){
                sintomasFam += Utileria.cleanString(chbMalGeneral.getText().toString().trim()) + " - ";
                listaSintomas.add(new Sintoma(cedulaFam,Utileria.cleanString(chbMalGeneral.getText().toString().trim())));
            }

            String condicionesFam = "";
            if (chbMayor60.isChecked()){
                condicionesFam += Utileria.cleanString(chbMayor60.getText().toString().trim()) + " - ";
                listaCondiciones.add(new CondicionSalud(cedulaFam,Utileria.cleanString(chbMayor60.getText().toString().trim())));
            }
            if (chbDiabetes.isChecked()){
                condicionesFam += Utileria.cleanString(chbDiabetes.getText().toString().trim()) + " - ";
                listaCondiciones.add(new CondicionSalud(cedulaFam,Utileria.cleanString(chbDiabetes.getText().toString().trim())));
            }
            if(chbHipertension.isChecked()){
                condicionesFam += Utileria.cleanString(chbHipertension.getText().toString().trim()) + " - ";
                listaCondiciones.add(new CondicionSalud(cedulaFam,Utileria.cleanString(chbHipertension.getText().toString().trim())));
            }
            if (chbEnfCorazon.isChecked()){
                condicionesFam += Utileria.cleanString(chbEnfCorazon.getText().toString().trim()) + " - ";
                listaCondiciones.add(new CondicionSalud(cedulaFam,Utileria.cleanString(chbEnfCorazon.getText().toString().trim())));
            }
            if (chbEnfPulmonar.isChecked()){
                condicionesFam += Utileria.cleanString(chbEnfPulmonar.getText().toString().trim()) + " - ";
                listaCondiciones.add(new CondicionSalud(cedulaFam,Utileria.cleanString(chbEnfPulmonar.getText().toString().trim())));
            }
            if(chbEnfRinones.isChecked()){
                condicionesFam += Utileria.cleanString(chbEnfRinones.getText().toString().trim()) + " - ";
                listaCondiciones.add(new CondicionSalud(cedulaFam,Utileria.cleanString(chbEnfRinones.getText().toString().trim())));
            }
            if (chbEnfHigado.isChecked()){
                condicionesFam += Utileria.cleanString(chbEnfHigado.getText().toString().trim()) + " - ";
                listaCondiciones.add(new CondicionSalud(cedulaFam,Utileria.cleanString(chbEnfHigado.getText().toString().trim())));
            }
            if (chbCancer.isChecked()){
                condicionesFam += Utileria.cleanString(chbCancer.getText().toString().trim()) + " - ";
                listaCondiciones.add(new CondicionSalud(cedulaFam,Utileria.cleanString(chbCancer.getText().toString().trim())));
            }
            if (chbInmunoPresion.isChecked()){
                condicionesFam += Utileria.cleanString(chbInmunoPresion.getText().toString().trim()) + " - ";
                listaCondiciones.add(new CondicionSalud(cedulaFam,Utileria.cleanString(chbInmunoPresion.getText().toString().trim())));
            }
            if(chbEnfAutInmunes.isChecked()){
                condicionesFam += Utileria.cleanString(chbEnfAutInmunes.getText().toString().trim()) + " - ";
                listaCondiciones.add(new CondicionSalud(cedulaFam,Utileria.cleanString(chbEnfAutInmunes.getText().toString().trim())));
            }
            if (chbNingunoCondiciones.isChecked()){
                condicionesFam += Utileria.cleanString(chbNingunoCondiciones.getText().toString().trim()) + " - ";
                listaCondiciones.add(new CondicionSalud(cedulaFam,Utileria.cleanString(chbNingunoCondiciones.getText().toString().trim())));
            }

            String contactoFamConPersonaCovid = "";
            if (rbContactoSiFam.isChecked()){
                contactoFamConPersonaCovid = rbContactoSiFam.getText().toString().trim();
            }else if (rbContactoNoFam.isChecked()){
                contactoFamConPersonaCovid = rbContactoNoFam.getText().toString().trim();
            }else if (rbContactoDescFam.isChecked()){
                contactoFamConPersonaCovid = rbContactoDescFam.getText().toString().trim();
            }

            if (contadorFormulario < cantidadPersSintomas){

                SharedPreferences preferencias = getSharedPreferences("datosFamiliar "+ contadorFormulario,Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = preferencias.edit();
                editor.putString("apellidos", apellidosFam);
                editor.putString("nombres", nombresFam);
                editor.putString("cedula", cedulaFam);
                editor.putString("cedulaEstudiante", cedulaEstudiante);
                editor.putInt("edad", edadFam);
                editor.putString("sexo", sexoFam);
                editor.putString("sintomas", sintomasFam.toUpperCase());
                editor.putString("dificultadRespirarSevera",difRespirarSevera.toUpperCase());
                editor.putString("condiciones", condicionesFam.toUpperCase());
                editor.putString("contactoConPersonaCovid", contactoFamConPersonaCovid.toUpperCase());
                editor.commit();

                //Guarda Datos Familiar en la lista
                Familiar familiar = new Familiar(apellidosFam,nombresFam,cedulaFam,cedulaEstudiante,edadFam,sexoFam,sintomasFam,difRespirarSevera,condicionesFam,contactoFamConPersonaCovid);
                listaFamiliares.add(familiar);

                //Guarda cedula en la lista para validación
                numCedulas.add(cedulaFam);

                txtInputApellidosFam.getEditText().setText(null);
                txtInputNombresFam.getEditText().setText(null);
                txtInputCedulaFamiliar.getEditText().setText(null);
                txtInputEdadFamiliar.getEditText().setText(null);
                rbgSexoFam.clearCheck();
                borrarCheckBoxesSintomas();
                borrarCheckBoxesCondiciones();
                borrarCheckBoxesDifRespirar();
                borrarRadioButtonContactoCovid();

                Toast.makeText(getApplicationContext(), "Datos familiar "+ contadorFormulario +
                        " guardado con éxito, LLENAR formulario para el SIGUIENTE familiar", Toast.LENGTH_LONG).show();
                contadorFormulario ++;
                txtInputApellidosFam.setFocusable(true);

                lyMensajes.removeAllViews();
                presionoContactoSiFam = false;
                presionoContactoNoFam = false;
                presionoContactoDescFam = false;

                scroll.scrollTo(0,0);

            }else {

                //Inicializa y establece la referencia
                FirebaseDatabase db = FirebaseDatabase.getInstance();
                DatabaseReference datosPersona = db.getReference("estudiantes");
                DatabaseReference datosFamiliar = db.getReference("familiares");
                DatabaseReference datosSintomas = db.getReference("sintomas");
                DatabaseReference datosSintomasRespiratorios = db.getReference("sintomasRespiratorios");
                DatabaseReference datosCondiciones = db.getReference("condiciones");

                SharedPreferences preferencias = getSharedPreferences("datosFamiliar "+ contadorFormulario,Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = preferencias.edit();
                editor.putString("apellidos", apellidosFam);
                editor.putString("nombres", nombresFam);
                editor.putString("cedula", cedulaFam);
                editor.putString("cedulaEstudiante", cedulaEstudiante);
                editor.putInt("edad", edadFam);
                editor.putString("sexo", sexoFam);
                editor.putString("sintomas", sintomasFam.toUpperCase());
                editor.putString("dificultadRespirarSevera",difRespirarSevera.toUpperCase());
                editor.putString("condiciones", condicionesFam.toUpperCase());
                editor.putString("contactoConPersonaCovid", contactoFamConPersonaCovid.toUpperCase());
                editor.commit();

                Familiar familiar = new Familiar(apellidosFam,nombresFam,cedulaFam,cedulaEstudiante,edadFam,sexoFam,sintomasFam,difRespirarSevera,condicionesFam,contactoFamConPersonaCovid);
                //Guarda el último registro de familiar
                listaFamiliares.add(familiar);

                //Guarda Datos del estudiante en Firebase
                datosPersona.push().setValue(DatosPersonalesActivity.estudiante);
                //Guarda la lista de familiares en Firebase
                for(int i=0;i<listaFamiliares.size();i++){
                    datosFamiliar.push().setValue(listaFamiliares.get(i));
                }
                //Guarda la lista de sintomas en Firebase
                for(int i=0;i<listaSintomas.size();i++){
                    datosSintomas.push().setValue(listaSintomas.get(i));
                }
                //Guarda la lista de sintomas respiratorios en Firebase
                for(int i=0;i<listaSintomasRespiratorios.size();i++){
                    datosSintomasRespiratorios.push().setValue(listaSintomasRespiratorios.get(i));
                }
                //Guarda la lista de condiciones en Firebase
                for(int i=0;i<listaCondiciones.size();i++){
                    datosCondiciones.push().setValue(listaCondiciones.get(i));
                }
                //Se genera el archivo flag de registro
                generarRegistro();

                //tarea en background que guarda los datos del estudiante en la base de datos postgres
                new AsyncTaskInsertarEstudiante(getApplicationContext()).execute(DatosPersonalesActivity.estudiante);

                //Guarda cedula en la lista para validación
                numCedulas.add(cedulaFam);

                Toast.makeText(getApplicationContext(), "Datos familiar " + contadorFormulario +
                        " guardado con éxito, gracias por su colaboración", Toast.LENGTH_LONG).show();
                /*Intent menu = new Intent(getApplicationContext(), TicketFamiliarActivity.class);
                menu.putExtra("desactivaNuevoForm", true);
                startActivity(menu);
                finish();*/

                Intent menu = new Intent(getApplicationContext(), MenuActivity.class);
                menu.putExtra("desactivaNuevoForm", true);
                startActivity(menu);
                finish();
            }
        }
    }

    /**
     * Boton que abre el navegador para telemedicina CEDIA
     * @param view parametro vista que permite escuchar evento onclick del boton telemedicina
     */
    public void telemedicina(View view){

        if (!Utileria.validarApellidos(txtInputApellidosFam) | !Utileria.validarNombres(txtInputNombresFam)
                | !Utileria.validarCedula(txtInputCedulaFamiliar)){

            Toast.makeText(getApplicationContext(), "Lo sentimos, algunos campos o selecciones " +
                    "están vacíos o no son validos sus datos, revíselos por favor" , Toast.LENGTH_LONG).show();

        }else {

            //recoger datos
            String apellidosFam = txtInputApellidosFam.getEditText().getText().toString().trim().toUpperCase();
            String nombresFam = txtInputNombresFam.getEditText().getText().toString().trim().toUpperCase();
            String cedulaFam = txtInputCedulaFamiliar.getEditText().getText().toString().trim();
            SharedPreferences preferences = getApplicationContext().getSharedPreferences("datosPersona", MODE_PRIVATE);
            String celular = preferences.getString("celular", "");
            String email = preferences.getString("correo","");
            String lat;
            if (preferences.getString("latitud", "").length() <= 15){
                lat = preferences.getString("latitud", "");
            }else {
                lat = preferences.getString("latitud", "").substring(0,15);
            }

            String lng;
            if (preferences.getString("longitud", "").length() <= 15){
                lng = preferences.getString("longitud", "");
            }else {
                lng = preferences.getString("longitud", "").substring(0,15);
            }

            String institutionId = "5e9efb80b0644b5399e37274";
            String url = "https://consultas.telesalud.ec/patient/new?name="+ apellidosFam + "%20" + nombresFam +
                    "&phone=" + celular +"&email="+ email + "&dni=" + cedulaFam + "&lat=" + lat +
                    "&lon=" + lng +"&institutionId=" + institutionId;

            Uri uri = Uri.parse(url);
            Intent tele = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(tele);
        }

    }

    public void borrarCheckBoxesSintomas(){
        chbFiebre.setChecked(false);
        chbTos.setChecked(false);
        chbGarganta.setChecked(false);
        chbDiarrea.setChecked(false);
        chbCabeza.setChecked(false);
        chbNasal.setChecked(false);
        chbPerdOlfato.setChecked(false);
        chbPerdGusto.setChecked(false);
        chbDifRespirar.setChecked(false);
        chbConjuntivitis.setChecked(false);
        chbMalGeneral.setChecked(false);
    }

    public void borrarCheckBoxesCondiciones(){
        chbMayor60.setChecked(false);
        chbDiabetes.setChecked(false);
        chbHipertension.setChecked(false);
        chbEnfCorazon.setChecked(false);
        chbEnfPulmonar.setChecked(false);
        chbEnfRinones.setChecked(false);
        chbEnfHigado.setChecked(false);
        chbCancer.setChecked(false);
        chbInmunoPresion.setChecked(false);
        chbEnfAutInmunes.setChecked(false);
        chbNingunoCondiciones.setChecked(false);
    }

    public void borrarCheckBoxesDifRespirar(){
        if (presionoDificultadRespirar){
            chbDifHab.setChecked(false);
            chbFaltAire.setChecked(false);
            chbDolToracico.setChecked(false);
            chbNingDifResp.setChecked(false);
        }
    }

    public void borrarRadioButtonContactoCovid(){
        rbgContactoFam.clearCheck();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                Intent menu = new Intent(getApplicationContext(), MenuActivity.class);
                startActivity(menu);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        Intent menu = new Intent(getApplicationContext(), MenuActivity.class);
        startActivity(menu);
    }

    public void generarRegistro(){
        String file_path = (Environment.getExternalStorageDirectory().toString());
        File file = new File(file_path, "log_emer"+MainActivity.usuario);
        if (!file.exists()){
            try {
                file.createNewFile();
            }catch(Exception e){
            }}
    }

    public void actualizarListaCedulasFamiliares(List<String> lista){
        numCedulas = lista;
        Utileria.validarCedula(txtInputCedulaFamiliar);
    }

}
