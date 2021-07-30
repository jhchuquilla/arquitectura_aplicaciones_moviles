package com.emerscience.seguimiento.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.emerscience.seguimiento.asynctasks.AsyncTaskInsertarSeguimiento;
import com.emerscience.seguimiento.pojos.Seguimiento;
import com.emerscience.seguimiento.utils.Utileria;
import com.google.android.material.textfield.TextInputLayout;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

import static java.util.Objects.requireNonNull;

public class SeguimientoActivity extends AppCompatActivity {

    private TextInputLayout txtInputRespiratoria, txtInputTemperatura, txtInputOxigeno, txtInputFrecCardiaca;
    private EditText etFrecResp, etTempCorp, etSatOxig, etFrecCard;
    private TextView txtfecha, txtApellidos, txtNombres, txtEdad;
    private RadioGroup rgDifRespirar, rgFatiga, rgDifHablar, rgDelirio;
    private RadioButton rbSiDifRespirar, rbNoDifRespirar, rbSiFatiga, rbNoFatiga, rbSiDfiHablar
            , rbNoDifHablar, rbSiDelirio, rbNoDelirio;
    private Button btnRegistrar;
    private LinearLayout lyGeneralSeguimiento;
    String apellidosFamiliar;
    String nombresFamiliar;
    int edadFamiliar;
    String cedulaFamiliar;
    private boolean reporte = false;
    private Seguimiento seguimiento;
    private ArrayList<Seguimiento> arrayListSeg;
    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seguimiento);
        lyGeneralSeguimiento = findViewById(R.id.lyGeneralSeguimiento);
        txtfecha= findViewById(R.id.txtFecha);
        txtApellidos = findViewById(R.id.txtApellidos);
        txtNombres = findViewById(R.id.txtNombres);
        txtEdad = findViewById(R.id.txtEdad);
        txtInputRespiratoria = findViewById(R.id.txtInputFrecRespiratoria);
        txtInputTemperatura = findViewById(R.id.txtInputTemperatura);
        txtInputFrecCardiaca = findViewById(R.id.txtInputFrecCardiaca);
        txtInputOxigeno = findViewById(R.id.txtInputSatOxigeno);
        etFrecResp = findViewById(R.id.etFrecResp);
        etTempCorp = findViewById(R.id.etTempCorp);
        etSatOxig = findViewById(R.id.etSatOxig);
        etFrecCard = findViewById(R.id.etFrecCard);
        rgDifRespirar = findViewById(R.id.rgDifRespirar);
        rbSiDifRespirar = findViewById(R.id.rbSiDifRespirar);
        rbNoDifRespirar = findViewById(R.id.rbNoDifRespirar);
        rgFatiga = findViewById(R.id.rgFatiga);
        rbSiFatiga = findViewById(R.id.rbSiFatiga);
        rbNoFatiga = findViewById(R.id.rbNoFatiga);
        rgDifHablar = findViewById(R.id.rgDifHablar);
        rbSiDfiHablar = findViewById(R.id.rbSiDifHablar);
        rbNoDifHablar = findViewById(R.id.rbNoDifHablar);
        rgDelirio = findViewById(R.id.rgDelirio);
        rbSiDelirio = findViewById(R.id.rbSiDelirio);
        rbNoDelirio = findViewById(R.id.rbNoDelirio);
        btnRegistrar = findViewById(R.id.btnRegistrarSeguimiento);

        Bundle parametros = this.getIntent().getExtras();
        if (null != parametros){
            apellidosFamiliar = parametros.getString("Apellidos", "");
            nombresFamiliar = parametros.getString("Nombres", "");
            edadFamiliar = parametros.getInt("Edad", 0);
            cedulaFamiliar = parametros.getString("cedulaFamiliar", "");
            reporte = parametros.getBoolean("reporte", false);
        }

        Bundle bundle = this.getIntent().getBundleExtra("bundleSeguimiento");
        if (bundle != null){
            seguimiento = (Seguimiento) bundle.getSerializable("Seguimiento");
        }else {
            seguimiento = new Seguimiento();
        }

        Bundle bundleArrayList = this.getIntent().getBundleExtra("arrSeguimientos");
        if (bundleArrayList != null){
            arrayListSeg = (ArrayList<Seguimiento>) bundleArrayList.getSerializable("arrSeguimientos");
            System.out.println("OBTIENE ARRAY LIST");
        }else{
            arrayListSeg = new ArrayList<>();
        }

        txtApellidos.setText(apellidosFamiliar);
        txtNombres.setText(nombresFamiliar);
        txtEdad.setText(String.valueOf(edadFamiliar));

        if (!reporte){
            setDate(txtfecha);
        }else {
            txtfecha.setText(seguimiento.getFechaRegistroSeguimiento());
//            txtInputRespiratoria.getEditText().setText(seguimiento.getFrecRespiratoria());
//            System.out.println(seguimiento.getFrecRespiratoria());
            btnRegistrar.setText("REGRESAR");
            llenarReporte(seguimiento);
            deshabilitarEdicion();
        }

        lyGeneralSeguimiento.setOnTouchListener((v, event) -> {
            if (getCurrentFocus() != null) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.
                        INPUT_METHOD_SERVICE);
                requireNonNull(imm).hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                getWindow().getDecorView().clearFocus();
            }
            return false;
        });

        requireNonNull(txtInputRespiratoria.getEditText()).addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                Utileria.validarFrecResp(txtInputRespiratoria);
            }
        });

        requireNonNull(txtInputTemperatura.getEditText()).addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                Utileria.validarTemp(txtInputTemperatura);
            }
        });

        requireNonNull(txtInputFrecCardiaca.getEditText()).addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                Utileria.validarFrecCard(txtInputFrecCardiaca);
            }
        });

        requireNonNull(txtInputOxigeno.getEditText()).addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                Utileria.validarSatOxg(txtInputOxigeno);
            }
        });

    }

    @SuppressLint("StaticFieldLeak")
    public void registrarSeguimiento(View view){
        if (reporte){
            Bundle bundle = new Bundle();
            bundle.putSerializable("arrSeguimientos", arrayListSeg);
            Intent listaSeguimientoActivity = new Intent(getApplicationContext(), ListaSeguimientoActivity.class);
            listaSeguimientoActivity.putExtra("Apellidos", requireNonNull(txtApellidos.getText().toString().trim()));
            listaSeguimientoActivity.putExtra("Nombres", requireNonNull(txtNombres.getText().toString().trim()));
            listaSeguimientoActivity.putExtra("Edad", Integer.parseInt(txtEdad.getText().toString().trim()));
            listaSeguimientoActivity.putExtra("cedulaFamiliar", cedulaFamiliar);
            listaSeguimientoActivity.putExtra("reporte", reporte);
            listaSeguimientoActivity.putExtra("arrSeguimientos", bundle);
            startActivity(listaSeguimientoActivity);
        }else {
            if (!Utileria.validarFrecResp(txtInputRespiratoria) | !Utileria.validarTemp(txtInputTemperatura)
                    | !Utileria.validarFrecCard(txtInputFrecCardiaca) | !Utileria.validarSatOxg(txtInputOxigeno)
                    | !Utileria.validarSeleccion(rgDifRespirar, rbNoDifRespirar) | !Utileria.validarSeleccion(rgFatiga, rbNoFatiga)
                    | !Utileria.validarSeleccion(rgDifHablar, rbNoDifHablar) | !Utileria.validarSeleccion(rgDelirio, rbNoDelirio)){
                Toast.makeText(getApplicationContext(), "Existen campos vacíos o con datos incorrectos", Toast.LENGTH_SHORT).show();
            }else {
                Date today = Calendar.getInstance().getTime();//Obtenemos fecha del sistema
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");//Formato para aplicar a la fecha obtenida del sistema
                String date = formatter.format(today);
                String cedula = cedulaFamiliar;

                int frecRespiratoria = Integer.parseInt(Objects.requireNonNull(txtInputRespiratoria.getEditText()).getText().toString().trim());
                double temperatura = Double.parseDouble(Objects.requireNonNull(txtInputTemperatura.getEditText()).getText().toString().trim());
                int frecCardiaca = Integer.parseInt(Objects.requireNonNull(txtInputFrecCardiaca.getEditText()).getText().toString().trim());
                int satOxigeno = Integer.parseInt(Objects.requireNonNull(txtInputOxigeno.getEditText()).getText().toString().trim());

                String dificultadRespirar = "";
                if (rgDifRespirar.getCheckedRadioButtonId() == rbSiDifRespirar.getId()){
                    dificultadRespirar = rbSiDifRespirar.getText().toString().trim().toUpperCase();
                }else if (rgDifRespirar.getCheckedRadioButtonId() == rbNoDifRespirar.getId()){
                    dificultadRespirar = rbNoDifRespirar.getText().toString().trim().toUpperCase();
                }

                String fatiga = "";
                if (rgFatiga.getCheckedRadioButtonId() == rbSiFatiga.getId()){
                    fatiga = rbSiFatiga.getText().toString().trim().toUpperCase();
                }else if (rgFatiga.getCheckedRadioButtonId() == rbNoFatiga.getId()){
                    fatiga = rbNoFatiga.getText().toString().trim().toUpperCase();
                }

                String dificultadHablar = "";
                if (rgDifHablar.getCheckedRadioButtonId() == rbSiDfiHablar.getId()){
                    dificultadHablar = rbSiDfiHablar.getText().toString().trim().toUpperCase();
                }else if (rgDifHablar.getCheckedRadioButtonId() == rbNoDifHablar.getId()){
                    dificultadHablar = rbNoDifHablar.getText().toString().trim().toUpperCase();
                }

                String delirio = "";
                if (rgDelirio.getCheckedRadioButtonId() == rbSiDelirio.getId()){
                    delirio = rbSiDelirio.getText().toString().trim().toUpperCase();
                }else if (rgDelirio.getCheckedRadioButtonId() == rbNoDelirio.getId()){
                    delirio = rbNoDelirio.getText().toString().trim().toUpperCase();
                }

                Seguimiento seguimiento=new Seguimiento(cedula, date, frecRespiratoria, temperatura
                        , frecCardiaca, satOxigeno, dificultadRespirar, fatiga, dificultadHablar, delirio);

                new AsyncTaskInsertarSeguimiento(getApplicationContext()) {
                    @Override
                    protected void onPostExecute(Boolean respuesta) {
                        if (respuesta) {
                            Toast.makeText(getApplicationContext(), "Se ha registrado de manera exitosa", Toast.LENGTH_SHORT).show();

                        } else {
                            Toast.makeText(getApplicationContext(), "Ya cuenta con un registro por el día de hoy", Toast.LENGTH_SHORT).show();
                        }
                    }
                }.execute(seguimiento);
            }
        }
    }

    public void setDate (TextView view){
        Date today = Calendar.getInstance().getTime();//Obtenemos fecha del sistema
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");//Formato para aplicar a la fecha obtenida del sistema
        String date = formatter.format(today);
        view.setText(date);
    }

    public void llenarReporte(Seguimiento seguimiento){
        etFrecResp.setText(String.valueOf(seguimiento.getFrecRespiratoria()));
        etTempCorp.setText(String.valueOf(seguimiento.getTemperaturaCorporal()));
        etFrecCard.setText(String.valueOf(seguimiento.getFrecCardiaca()));
        etSatOxig.setText(String.valueOf(seguimiento.getSaturacionOxigeno()));

        if (seguimiento.getDificultadRespirar().equalsIgnoreCase("SI")){
            rbSiDifRespirar.setChecked(true);
        }else if (seguimiento.getDificultadRespirar().equalsIgnoreCase("NO")){
            rbNoDifRespirar.setChecked(true);
        }

        if (seguimiento.getFatiga().equalsIgnoreCase("SI")){
            rbSiFatiga.setChecked(true);
        }else if (seguimiento.getFatiga().equalsIgnoreCase("NO")){
            rbNoFatiga.setChecked(true);
        }

        if (seguimiento.getDificultadHablar().equalsIgnoreCase("SI")){
            rbSiDfiHablar.setChecked(true);
        }else if (seguimiento.getDificultadRespirar().equalsIgnoreCase("NO")){
            rbNoDifHablar.setChecked(true);
        }

        if (seguimiento.getDelirio().equalsIgnoreCase("SI")){
            rbSiDelirio.setChecked(true);
        }else if (seguimiento.getDificultadRespirar().equalsIgnoreCase("NO")){
            rbNoDelirio.setChecked(true);
        }
    }

    public void deshabilitarEdicion(){
        etFrecResp.setEnabled(false);
        etTempCorp.setEnabled(false);
        etFrecCard.setEnabled(false);
        etSatOxig.setEnabled(false);
        rgDifRespirar.setEnabled(false);
        rbSiDifRespirar.setEnabled(false);
        rbNoDifRespirar.setEnabled(false);
        rgFatiga.setEnabled(false);
        rbSiFatiga.setEnabled(false);
        rbNoFatiga.setEnabled(false);
        rgDifHablar.setEnabled(false);
        rbSiDfiHablar.setEnabled(false);
        rbNoDifHablar.setEnabled(false);
        rgDelirio.setEnabled(false);
        rbSiDelirio.setEnabled(false);
        rbNoDelirio.setEnabled(false);
    }

    @Override
    public void onBackPressed() {

        if (reporte){
            Bundle bundle = new Bundle();
            bundle.putSerializable("arrSeguimientos", arrayListSeg);
            Intent listaSeguimientoActivity = new Intent(getApplicationContext(), ListaSeguimientoActivity.class);
            listaSeguimientoActivity.putExtra("Apellidos", requireNonNull(txtApellidos.getText().toString().trim()));
            listaSeguimientoActivity.putExtra("Nombres", requireNonNull(txtNombres.getText().toString().trim()));
            listaSeguimientoActivity.putExtra("Edad", Integer.parseInt(txtEdad.getText().toString().trim()));
            listaSeguimientoActivity.putExtra("cedulaFamiliar", cedulaFamiliar);
            listaSeguimientoActivity.putExtra("reporte", reporte);
            listaSeguimientoActivity.putExtra("arrSeguimientos", bundle);
            startActivity(listaSeguimientoActivity);
        }else {
            Intent registroActivity = new Intent(getApplicationContext(), RegistroActivity.class);
            startActivity(registroActivity);
            finish();
        }
    }
}
