package com.emerscience.seguimiento.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.emerscience.seguimiento.pojos.Coordenada;
import com.emerscience.seguimiento.pojos.Familiar;
import com.emerscience.seguimiento.pojos.Seguimiento;
import com.emerscience.seguimiento.retrofit.EstudianteApiAdapter;
import com.emerscience.seguimiento.retrofit.FamiliarApiAdapter;
import com.emerscience.seguimiento.retrofit.SeguimientoApiAdapter;
import com.emerscience.seguimiento.utils.Utileria;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static java.util.Objects.requireNonNull;

public class RegistroActivity extends AppCompatActivity {

    RadioButton checkNombre, checkCedula;
    RadioGroup grupoOpciones;
    Familiar fami;
    LinearLayout linearLayout;
    EditText edtNombre;
    EditText edtCedula;
    private TextView txtTitulo;
    private boolean reporte = false, buscarCoord = false;
    @SuppressLint("WrongViewCast")

    @Override
    public void onStop() {
        super.onStop();
        View view = getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            requireNonNull(imm).hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        // imm.hideSoftInputFromWindow(e.getWindowToken(), 0);
        View view = getCurrentFocus();
        if (view != null) {
            requireNonNull(imm).hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        View view = getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            requireNonNull(imm).hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

        Bundle parametros = this.getIntent().getExtras();
        if (null != parametros){
            reporte = parametros.getBoolean("reporte", false);
            buscarCoord = parametros.getBoolean("buscarCoord", false);
        }

        checkNombre=findViewById(R.id.radioNombre);
        linearLayout=findViewById(R.id.layoutRegistroTotal);
        checkCedula=findViewById(R.id.radioCedula);
        grupoOpciones=findViewById(R.id.grupo_Opciones);
        edtNombre = findViewById(R.id.edt_nombre);
        edtCedula = findViewById(R.id.edt_Cedula);
        txtTitulo = findViewById(R.id.txtTitulo);
        checkCedula.setChecked(true);
        edtCedula.requestFocus();

        if (reporte){
            txtTitulo.setText("BUSCAR REGISTROS SEGUIMIENTO");
        }else {
            if (buscarCoord){
                txtTitulo.setText("BUSCAR COORDENADA");
            }else {
                txtTitulo.setText("INGRESAR NUEVO REGISTRO");
            }
        }

        cambiarEstado(edtNombre,false);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        grupoOpciones.setOnCheckedChangeListener((group, checkedId) -> {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            requireNonNull(imm).hideSoftInputFromWindow(checkNombre.getWindowToken(), 0);
            imm.hideSoftInputFromWindow(checkCedula.getWindowToken(), 0);

            if (checkNombre.isChecked()) {
                imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
                cambiarEstado(edtNombre,true);
                cambiarEstado(edtCedula,false);
                edtNombre.requestFocus();
                edtCedula.getText().clear();

            } else {
                imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
                cambiarEstado(edtNombre,false);
                cambiarEstado(edtCedula,true);
                edtCedula.requestFocus();
                edtNombre.getText().clear();
            }
        });

        linearLayout.setOnTouchListener((v, event) -> {
            if (getCurrentFocus() != null) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.
                        INPUT_METHOD_SERVICE);
                requireNonNull(imm).hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                getWindow().getDecorView().clearFocus();
            }
            return false;
        });

        edtCedula.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                Utileria.validarCedula(edtCedula);
            }
        });

    }

    private void cambiarEstado(EditText editText, boolean estado) {
        editText.setFocusable(estado);
        editText.setEnabled(estado);
        editText.setCursorVisible(estado);
        editText.setFocusableInTouchMode(estado);
    }

    public void buscar(View view) {
        String parametro;
        if (checkNombre.isChecked()) {
            if (!Utileria.validarNombres(edtNombre)){
                Toast.makeText(getApplicationContext(), "Campo nombre vacío o con datos incorrectos", Toast.LENGTH_SHORT).show();
            }else {
                parametro= Utileria.cleanString(edtNombre.getText().toString().trim()).toUpperCase();
                if (!reporte){
                    if (buscarCoord){
                        //Llamar api para obtener coordenada por apellidos y nombres y pasar datos a MapsActivity
                        buscarCoordPorApeNom(parametro);
                        Toast.makeText(getApplicationContext(), "Buscando coordenadas", Toast.LENGTH_SHORT).show();
                    }else {
                        //Se busca datos por apellidos y nombres para llenar un nuevo registro de seguimiento
                        buscarFamiliarApeNom(parametro);
                    }
                }else {
                    Toast.makeText(getApplicationContext(), "Buscando registros seguimiento", Toast.LENGTH_SHORT).show();
                    buscarSeguimientoFamiliarPorApeNom(parametro);
                }
            }
        }else{
            if (!Utileria.validarCedula(edtCedula)){
                Toast.makeText(getApplicationContext(), "Campo cédula vacío o con datos incorrectos", Toast.LENGTH_SHORT).show();
            }else{
                parametro=edtCedula.getText().toString();
                if (!reporte){
                    if (buscarCoord){
                        //Llamar api para obtener coordenada por Cedula de identidad y pasar datos a MapsActivity
                        buscarCoordPorCI(parametro);
                        Toast.makeText(getApplicationContext(), "Buscando coordenadas", Toast.LENGTH_SHORT).show();
                    }else {
                        //Se busca datos por cédula de identidad para llenar un nuevo registro de seguimiento
                        buscarFamiliarCedula(parametro);
                    }
                }else {
                    Toast.makeText(getApplicationContext(), "Buscando registros seguimiento", Toast.LENGTH_SHORT).show();
                    buscarSeguimientoFamiliarPorCedula(parametro);
                }
            }
        }
    }

    protected void buscarFamiliarApeNom(String nombreFamiliar){
        Call<Familiar> callFamiliares = FamiliarApiAdapter.getFamiliarApiService()
                .obtenerFamiliarPorApeNom(nombreFamiliar, getSharedPreferences("token", MODE_PRIVATE)
                        .getString("token","")
                );
        callFamiliares.enqueue(new Callback<Familiar>() {
            @Override
            public void onResponse(Call<Familiar> call, Response<Familiar> response) {
                if (response.isSuccessful()){
                    Familiar familiar = response.body();
                    if (null != familiar){
                        fami=familiar;
                        System.out.println("Se ha encontrado");
                        Intent seguimientoActivity = new Intent(getApplicationContext(),SeguimientoActivity.class);
                        seguimientoActivity.putExtra("Apellidos", familiar.getApellidosFam());
                        seguimientoActivity.putExtra("Nombres", familiar.getNombresFam());
                        seguimientoActivity.putExtra("Edad", familiar.getEdadFam());
                        seguimientoActivity.putExtra("cedulaFamiliar", familiar.getCedulaFam());
                        startActivity(seguimientoActivity);
                        finish();
                    }else {
                        Toast.makeText(getApplicationContext(), "No se encontro información con los" +
                                " datos ingresados", Toast.LENGTH_SHORT).show();
                    }
                }else {
                    Log.i("RESPONSE", response.message());
                    Log.i("RESP ERROR" ,response.errorBody().toString());
                    Log.i("HTTP CODE", String.valueOf(response.code()));
                }
            }

            @Override
            public void onFailure(Call<Familiar> call, Throwable t) {
                Log.e("ErrorGen", requireNonNull(t.getMessage()));
            }
        });

    }

    protected void buscarFamiliarCedula(String cedulaFamiliar){
        Call<Familiar> callFamiliares = FamiliarApiAdapter.getFamiliarApiService()
                .obtenerFamiliarPorCedula(cedulaFamiliar, getSharedPreferences("token", MODE_PRIVATE)
                        .getString("token","")
                );
        callFamiliares.enqueue(new Callback<Familiar>() {
            @Override
            public void onResponse(Call<Familiar> call, Response<Familiar> response) {
                if (response.isSuccessful()){
                    Familiar familiar = response.body();
                    if (null != familiar){
                        fami=familiar;
                        Log.i("buscarFamiliarCedula" ,"Se ha encontrado");
                        Intent seguimientoActivity = new Intent(getApplicationContext(),SeguimientoActivity.class);
                        seguimientoActivity.putExtra("Apellidos", familiar.getApellidosFam());
                        seguimientoActivity.putExtra("Nombres", familiar.getNombresFam());
                        seguimientoActivity.putExtra("Edad", familiar.getEdadFam());
                        seguimientoActivity.putExtra("cedulaFamiliar", familiar.getCedulaFam());
                        startActivity(seguimientoActivity);
                        finish();
                    }else {
                        Toast.makeText(getApplicationContext(), "No se encontro datos para CI: "
                                + cedulaFamiliar, Toast.LENGTH_SHORT).show();
                    }
                }else {
                    Log.i("RESPONSE", response.message());
                    Log.i("RESP ERROR" ,response.errorBody().toString());
                    Log.i("HTTP CODE", String.valueOf(response.code()));
                }
            }

            @Override
            public void onFailure(Call<Familiar> call, Throwable t) {
                Log.e("ErrorGen", t.getMessage());
            }
        });

    }

    protected void buscarSeguimientoFamiliarPorApeNom(String nombreFamiliar){
        Call<Familiar> callFamiliares = FamiliarApiAdapter.getFamiliarApiService()
                .obtenerFamiliarPorApeNom(nombreFamiliar, getSharedPreferences("token", MODE_PRIVATE)
                        .getString("token","")
                );
        callFamiliares.enqueue(new Callback<Familiar>() {
            @Override
            public void onResponse(Call<Familiar> call, Response<Familiar> response) {
                if (response.isSuccessful()){
                    Familiar familiar = response.body();
                    if (null != familiar){
                        fami=familiar;
                        System.out.println("Se ha encontrado");
                        Intent listaSeguimientoActivity = new Intent(getApplicationContext(),ListaSeguimientoActivity.class);
                        listaSeguimientoActivity.putExtra("Apellidos", familiar.getApellidosFam());
                        listaSeguimientoActivity.putExtra("Nombres", familiar.getNombresFam());
                        listaSeguimientoActivity.putExtra("Edad", familiar.getEdadFam());
                        listaSeguimientoActivity.putExtra("cedulaFamiliar", familiar.getCedulaFam());
                        listaSeguimientoActivity.putExtra("reporte", reporte);
                        obtenerListaRegistrosSeguimientoCedula(familiar.getCedulaFam(), listaSeguimientoActivity);
                        finish();
                    }else {
                        Toast.makeText(getApplicationContext(), "No se encontro información con los" +
                                " datos ingresados", Toast.LENGTH_SHORT).show();
                    }
                }else {
                    Log.i("RESPONSE", response.message());
                    Log.i("RESP ERROR" ,response.errorBody().toString());
                    Log.i("HTTP CODE", String.valueOf(response.code()));
                }
            }

            @Override
            public void onFailure(Call<Familiar> call, Throwable t) {
                Log.e("ErrorGen", requireNonNull(t.getMessage()));
            }
        });

    }

    protected void buscarSeguimientoFamiliarPorCedula(String cedulaFamiliar){
        Call<Familiar> callFamiliares = FamiliarApiAdapter.getFamiliarApiService()
                .obtenerFamiliarPorCedula(cedulaFamiliar, getSharedPreferences("token", MODE_PRIVATE)
                        .getString("token","")
                );
        callFamiliares.enqueue(new Callback<Familiar>() {
            @Override
            public void onResponse(Call<Familiar> call, Response<Familiar> response) {
                if (response.isSuccessful()){
                    Familiar familiar = response.body();
                    if (null != familiar){
                        fami=familiar;
                        Log.i("buscarSegFamiliarCedula" ,"Se ha encontrado");
                        Intent listaSeguimientoActivity = new Intent(getApplicationContext(),ListaSeguimientoActivity.class);
                        listaSeguimientoActivity.putExtra("Apellidos", familiar.getApellidosFam());
                        listaSeguimientoActivity.putExtra("Nombres", familiar.getNombresFam());
                        listaSeguimientoActivity.putExtra("Edad", familiar.getEdadFam());
                        listaSeguimientoActivity.putExtra("cedulaFamiliar", familiar.getCedulaFam());
                        listaSeguimientoActivity.putExtra("reporte", reporte);
                        obtenerListaRegistrosSeguimientoCedula(cedulaFamiliar, listaSeguimientoActivity);
                    }else {
                        Toast.makeText(getApplicationContext(), "No se encontro datos para CI: "
                                + cedulaFamiliar, Toast.LENGTH_SHORT).show();
                    }
                }else {
                    Log.i("RESPONSE", response.message());
                    Log.i("RESP ERROR" ,response.errorBody().toString());
                    Log.i("HTTP CODE", String.valueOf(response.code()));
                }
            }

            @Override
            public void onFailure(Call<Familiar> call, Throwable t) {
                Log.e("ErrorGen", t.getMessage());
            }
        });

    }

    protected void obtenerListaRegistrosSeguimientoCedula(String cedulaFamiliar, Intent listaSeguimientoActivity){
        Call<List<Seguimiento>> callSeguimiento = SeguimientoApiAdapter.getSeguimientoApiService()
                .obtenerListaRegistrosSeguimiento(cedulaFamiliar, getSharedPreferences("token", MODE_PRIVATE)
                        .getString("token","")
                );
        callSeguimiento.enqueue(new Callback<List<Seguimiento>>() {
            @Override
            public void onResponse(Call<List<Seguimiento>> call, Response<List<Seguimiento>> response) {
                if (response.isSuccessful()) {
                    List<Seguimiento> listSeguimientos = response.body();
                    if (null != listSeguimientos) {
                        Log.i("obtRegSegCed" ,"Se ha encontrado");
                        Bundle bund = new Bundle();
                        ArrayList<Seguimiento> arrSeguimientos = new ArrayList<>(listSeguimientos);
                        bund.putSerializable("arrSeguimientos", arrSeguimientos);
                        listaSeguimientoActivity.putExtra("arrSeguimientos", bund);
                        startActivity(listaSeguimientoActivity);
                        finish();
                    } else {
                        Toast.makeText(getApplicationContext(), "No se encontro registros", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Log.i("RESPONSE", response.message());
                    Log.i("RESP ERROR" ,response.errorBody().toString());
                    Log.i("HTTP CODE", String.valueOf(response.code()));
                    Toast.makeText(getApplicationContext(), "No se encontro registros" , Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<List<Seguimiento>> call, Throwable t) {

            }
        });
    }

    protected void buscarCoordPorApeNom(String apellidosNombres){
        Call<Coordenada> callCoordenada = EstudianteApiAdapter.getEstudianteApiService()
                .obtenerCoordPorApeNom(apellidosNombres, getSharedPreferences("token", MODE_PRIVATE)
                        .getString("token", ""));
        callCoordenada.enqueue(new Callback<Coordenada>() {
            @Override
            public void onResponse(Call<Coordenada> call, Response<Coordenada> response) {
                if (response.isSuccessful()){
                    Coordenada coordenada = response.body();
                    if (null != coordenada){
                        Bundle bund = new Bundle();
                        bund.putSerializable("coordenada", coordenada);
                        Intent mapsActivity = new Intent(getApplicationContext(), MapsActivity.class);
                        mapsActivity.putExtra("bundCoordenada", bund);
                        mapsActivity.putExtra("buscarCoord", buscarCoord);
                        startActivity(mapsActivity);
                        finish();
                    }else{
                        Toast.makeText(getApplicationContext(), "No se encontro coordenada", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Log.i("RESPONSE", response.message());
                    Log.i("RESP ERROR" ,response.errorBody().toString());
                    Log.i("HTTP CODE", String.valueOf(response.code()));
                    Toast.makeText(getApplicationContext(), "No se encontro coordenada" , Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Coordenada> call, Throwable t) {

            }
        });
    }

    protected void buscarCoordPorCI(String cedula){
        Call<Coordenada> callCoordenada = EstudianteApiAdapter.getEstudianteApiService()
                .obtenerCoordPorCI(cedula, getSharedPreferences("token", MODE_PRIVATE)
                        .getString("token", ""));
        callCoordenada.enqueue(new Callback<Coordenada>() {
            @Override
            public void onResponse(Call<Coordenada> call, Response<Coordenada> response) {
                if (response.isSuccessful()){
                    Coordenada coordenada = response.body();
                    if (null != coordenada){
                        Bundle bund = new Bundle();
                        bund.putSerializable("coordenada", coordenada);
                        Intent mapsActivity = new Intent(getApplicationContext(), MapsActivity.class);
                        mapsActivity.putExtra("bundCoordenada", bund);
                        mapsActivity.putExtra("buscarCoord", buscarCoord);
                        startActivity(mapsActivity);
                        finish();
                    }else{
                        Toast.makeText(getApplicationContext(), "No se encontro coordenada", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Log.i("RESPONSE", response.message());
                    Log.i("RESP ERROR" ,response.errorBody().toString());
                    Log.i("HTTP CODE", String.valueOf(response.code()));
                    Toast.makeText(getApplicationContext(), "No se encontro coordenada" , Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Coordenada> call, Throwable t) {

            }
        });
    }

    protected void obtenerNumero(String cedulaFamiliar) {
        Call<Integer> callSeguimiento= SeguimientoApiAdapter.getSeguimientoApiService()
                .obtenerRegistros(cedulaFamiliar, getSharedPreferences("token", MODE_PRIVATE)
                        .getString("token", "")
                );
        callSeguimiento.enqueue(new Callback<Integer>() {
            @Override
            public void onResponse(Call<Integer> call, Response<Integer> response) {
                if (response.isSuccessful()) {
                    Integer numero = response.body();
                    if (null != numero) {
                        System.out.println("Se ha encontrado");
                        Toast.makeText(getApplicationContext(), "El usuario cuenta con "+ numero + " registros.", Toast.LENGTH_SHORT).show();

                    } else {
                        System.out.println("No se encontró");
                    }
                } else {
                    System.out.println(response.message());
                    System.out.println(response.errorBody().toString());
                    System.out.println("Error");
                    Log.i("HTTP CODE", String.valueOf(response.code()));
                }
            }

            @Override
            public void onFailure(Call<Integer> call, Throwable t) {
                Log.e("ErrorGen", t.getMessage());
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (buscarCoord){
            Intent menuMapsActivity = new Intent(getApplicationContext(), MenuMapsActivity.class);
            startActivity(menuMapsActivity);
            finish();
        }else {
            Intent menuActivity = new Intent(getApplicationContext(), MenuActivity.class);
            startActivity(menuActivity);
            finish();
        }
    }
}
