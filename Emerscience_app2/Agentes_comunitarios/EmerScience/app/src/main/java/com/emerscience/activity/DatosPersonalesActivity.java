package com.emerscience.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.appcompat.app.ActionBar;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
//import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
//import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.emerscience.io.EstudianteApiAdapter;
import com.emerscience.pojos.Estudiante;
import com.emerscience.utils.AsyncTaskInsertarEstudiante;
import com.emerscience.utils.Utileria;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static java.util.Objects.requireNonNull;

public class DatosPersonalesActivity extends AppCompatActivity implements Callback<List<String>> {

    static public Estudiante estudiante;
    public static List<String> cedulasEstudiantes;
    private boolean editar = false;
    private LinearLayout lyDatosInformante;
    private RadioGroup rbgSexo, rbgSintomas, rbgPropiedadVivienda, rbgBanioCompartido
            , rbgPagoArriendoServiciosBasicos, rbgProblemasComida;
    private RadioButton rbMasculino, rbFemenino, rbSintomasSi, rbSintomasNo, rbPropia, rbArrendada, rbPrestada, rbOtro
            , rbSiComparte, rbNoComparte, rbSiPago, rbNoPago, rbSiProblemaComida, rbNoProblemaComida;
    private TextInputLayout txtInputApellidos, txtInputNombres, txtInputCedula, txtInputEdad,
            txtInputCelular, txtInputFijo, txtInputCorreo, txtInputPersonas, txtInputNumDormitorios
            , txtInputNumPersonasTrabajan, txtInputNumPersonasEmpleoFijo;

    TextView latitud, longitud;
    private Localizacion loc;
    private LocationManager locationManager;
    private String fechaRegistroObtenida = "";
    private boolean tieneFamiliares = false;

    //Botones y selecciones generadas por codigo
    private RadioGroup rbgCont, rbgUsted;
    private RadioButton rbContSi, rbContNo, rbSiUsted, rbNoUsted;
    private EditText etNumPersonasSintomas;

    boolean presionoSintomasNo = false;
    boolean presionoSintomasSi = false;
    boolean presionoContactoSi = false;
    boolean presionoContactoNo = false;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_datos_personales);

        latitud = findViewById(R.id.tvLatitud);
        longitud = findViewById(R.id.tvLongitud);

        requireNonNull(getSupportActionBar()).setDisplayOptions(androidx.appcompat.app.ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.toolbar_form_estudiante);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Bundle parametros = this.getIntent().getExtras();
        if (null != parametros){
            editar = parametros.getBoolean("editar");
            if (editar){
                tieneFamiliares = parametros.getBoolean("tieneFamiliares");
            }
        }

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION,}
                    , 1000);
        } else {
            if (!editar) {
                locationStart();
            }
        }

        //Botones, textos y selecciones generadas por codigo
        final TextView txtContacto = new TextView(getApplicationContext());
        final RadioGroup rbgContacto = new RadioGroup(getApplicationContext());
        final RadioButton rbContactoSi = new RadioButton(getApplicationContext());
        final RadioButton rbContactoNo = new RadioButton(getApplicationContext());

        final TextView txtCuarentena = new TextView(getApplicationContext());
        final TextView txtLleneDeNuevo = new TextView(getApplicationContext());

        final TextView txtEsUstedPersonaConSintomas = new TextView(getApplicationContext());
        final RadioGroup rbgEsUsted = new RadioGroup(getApplicationContext());
        final RadioButton rbSiEsUsted = new RadioButton(getApplicationContext());
        final RadioButton rbNoEsUsted = new RadioButton(getApplicationContext());

        final TextView txtPersonasConSintomas = new TextView(getApplicationContext());
        final TextView txtCantidadPersonasSintomas = new TextView(getApplicationContext());
        final EditText etCantidadPersonasSintomas = new EditText(getApplicationContext());

        //Botones, textos y selecciones generados mediante xml
        lyDatosInformante = findViewById(R.id.lyDatosInformante);
        rbgSintomas = findViewById(R.id.rbgSintomas);
        rbgSexo = findViewById(R.id.rbgSexo);
        rbgPropiedadVivienda = findViewById(R.id.rbgPropiedadVivienda);
        rbgBanioCompartido = findViewById(R.id.rbgBanioCompartido);
        rbgPagoArriendoServiciosBasicos = findViewById(R.id.rbgPagoArriendoServiciosBasicos);
        rbgProblemasComida = findViewById(R.id.rbgProblemasComida);

        rbMasculino = findViewById(R.id.rbMaculino);
        rbFemenino = findViewById(R.id.rbFemenino);
        rbSintomasSi = findViewById(R.id.rbSintomasSi);
        rbSintomasNo = findViewById(R.id.rbSintomasNo);
        rbPropia = findViewById(R.id.rbPropia);
        rbArrendada = findViewById(R.id.rbArrendada);
        rbPrestada = findViewById(R.id.rbPrestada);
        rbOtro = findViewById(R.id.rbOtro);
        rbSiComparte = findViewById(R.id.rbSiComparte);
        rbNoComparte = findViewById(R.id.rbNoComparte);
        rbSiPago = findViewById(R.id.rbSiPago);
        rbNoPago = findViewById(R.id.rbNoPago);
        rbSiProblemaComida = findViewById(R.id.rbSiProblemaComida);
        rbNoProblemaComida = findViewById(R.id.rbNoProblemaComida);

        txtInputApellidos = findViewById(R.id.txtInputApellidos);
        txtInputNombres = findViewById(R.id.txtInputNombres);
        txtInputCedula = findViewById(R.id.txtInputCedula);
        txtInputEdad = findViewById(R.id.txtInputEdad);
        txtInputCelular = findViewById(R.id.txtInputCelular);
        txtInputFijo = findViewById(R.id.txtInputTelefono);
        txtInputCorreo = findViewById(R.id.txtInputCorreo);
        txtInputPersonas = findViewById(R.id.txtInputPersonas);
        txtInputNumDormitorios = findViewById(R.id.txtInputNumDormitorios);
        txtInputNumPersonasTrabajan = findViewById(R.id.txtInputNumPersonasTrabajan);
        txtInputNumPersonasEmpleoFijo = findViewById(R.id.txtInputNumPersonasEmpleoFijo);

        if (!editar){
            if (!MainActivity.usuario.equalsIgnoreCase("dpruebas")){
                cargarDatosUsuarioLdap();
            }
            Call<List<String>> call = EstudianteApiAdapter.getApiService().obtenerCedulasEStudiantes(getSharedPreferences("token", MODE_PRIVATE).getString("token",""));
            call.enqueue(this);
        }else {
            cargarDatosEstudianteEditar(txtContacto, rbgContacto, rbContactoSi, rbContactoNo, txtCuarentena, txtLleneDeNuevo
                    , txtEsUstedPersonaConSintomas, rbgEsUsted, rbSiEsUsted, rbNoEsUsted, txtPersonasConSintomas
                    , txtCantidadPersonasSintomas, etCantidadPersonasSintomas);
            requireNonNull(txtInputCedula.getEditText()).setEnabled(false);
            Call<List<String>> call = EstudianteApiAdapter.getApiService().obtenerCedulasEStudiantes(getSharedPreferences("token", MODE_PRIVATE).getString("token",""));
            call.enqueue(this);
        }

         //Evento que permite ocultar el teclado virtual cuando se presione cualquier parte de la
         //pantalla menos los edit texts
        lyDatosInformante.setOnTouchListener((v, event) -> {
            if (getCurrentFocus() != null) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.
                        INPUT_METHOD_SERVICE);
                requireNonNull(imm).hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                getWindow().getDecorView().clearFocus();
            }
            return false;
        });

        //Validacion de campo apellidos en tiempo de escritura
        requireNonNull(txtInputApellidos.getEditText()).addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                Utileria.validarApellidos(txtInputApellidos);
            }
        });

        //Validacion de campo nombres en tiempo de escritura
        requireNonNull(txtInputNombres.getEditText()).addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                Utileria.validarNombres(txtInputNombres);
            }
        });

        //Permite validar numero de cedula luego de que termina de escribir el usuario en dicho campo
        requireNonNull(txtInputCedula.getEditText()).addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                Utileria.validarCedula(txtInputCedula, editar, cedulasEstudiantes);
            }
        });

        //Validacion de campo edad en tiempo de escritura
        requireNonNull(txtInputEdad.getEditText()).addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                Utileria.validarEdad(txtInputEdad);
            }
        });

        //Validacion de campo celular en tiempo de escritura
        requireNonNull(txtInputCelular.getEditText()).addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                Utileria.validarCelular(txtInputCelular);
            }
        });

        //Validacion de campo telf. fijo en tiempo de escritura
        requireNonNull(txtInputFijo.getEditText()).addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                Utileria.validarFijo(txtInputFijo);
            }
        });

        requireNonNull(txtInputNumDormitorios.getEditText()).addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                validarNumeroDormitorios();
            }
        });

        requireNonNull(txtInputNumPersonasTrabajan.getEditText()).addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                validarNumeroPersonasTrabajan();
            }
        });

        requireNonNull(txtInputNumPersonasEmpleoFijo.getEditText()).addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                validarNumeroPersonasTrabajoFijo();
            }
        });

        //Permite mostrar las preguntas segun seleccion del usuario si tiene o no sintomas COVID-19
        rbgSintomas.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.rbSintomasSi && !presionoSintomasSi) {

                if (presionoSintomasNo) {
                    rbContNo.setError(null);
                    rbContSi = null;
                    rbContNo = null;
                    rbgContacto.clearCheck();
                    rbgEsUsted.clearCheck();
                    if (rbContactoSi.getParent() != null) {
                        ((ViewGroup) rbContactoSi.getParent()).removeView(rbContactoSi);
                    }
                    if (rbContactoNo.getParent() != null) {
                        ((ViewGroup) rbContactoNo.getParent()).removeView(rbContactoNo);
                    }
                    if (txtLleneDeNuevo.getParent() != null) {
                        ((ViewGroup) txtLleneDeNuevo.getParent()).removeView(txtLleneDeNuevo);
                    }
                    if (txtCuarentena.getParent() != null) {
                        ((ViewGroup) txtCuarentena.getParent()).removeView(txtCuarentena);
                    }
                    if (txtPersonasConSintomas.getParent() != null) {
                        ((ViewGroup) txtPersonasConSintomas.getParent()).removeView(txtPersonasConSintomas);
                    }
                    if (txtCantidadPersonasSintomas.getParent() != null) {
                        ((ViewGroup) txtCantidadPersonasSintomas.getParent()).removeView(txtCantidadPersonasSintomas);
                    }
                    rbgCont = null;
                    lyDatosInformante.removeView(rbgContacto);
                    lyDatosInformante.removeView(txtContacto);

                    mostrarOpcionesSintomasSi(txtEsUstedPersonaConSintomas, rbgEsUsted, rbSiEsUsted, rbNoEsUsted
                            , txtPersonasConSintomas, txtCantidadPersonasSintomas, etCantidadPersonasSintomas);

                    presionoSintomasNo = false;
                    presionoSintomasSi = true;
                } else {

                    mostrarOpcionesSintomasSi(txtEsUstedPersonaConSintomas, rbgEsUsted, rbSiEsUsted, rbNoEsUsted
                            , txtPersonasConSintomas, txtCantidadPersonasSintomas, etCantidadPersonasSintomas);

                    presionoSintomasNo = false;
                    presionoSintomasSi = true;
                }

            } else if (checkedId == R.id.rbSintomasNo && !presionoSintomasNo) {

                if (presionoSintomasSi) {
                    etCantidadPersonasSintomas.setText(null);
                    etCantidadPersonasSintomas.setError(null);
                    rbgEsUsted.clearCheck();

                    if (rbSiEsUsted.getParent() != null) {
                        ((ViewGroup) rbSiEsUsted.getParent()).removeView(rbSiEsUsted);
                    }
                    if (rbNoEsUsted.getParent() != null) {
                        ((ViewGroup) rbNoEsUsted.getParent()).removeView(rbNoEsUsted);
                    }
                    if (rbgEsUsted.getParent() != null) {
                        ((ViewGroup) rbgEsUsted.getParent()).removeView(rbgEsUsted);
                    }
                    if (txtEsUstedPersonaConSintomas.getParent() != null) {
                        ((ViewGroup) txtEsUstedPersonaConSintomas.getParent()).removeView(txtEsUstedPersonaConSintomas);
                    }

                    lyDatosInformante.removeView(rbgEsUsted);
                    rbgEsUsted.removeView(rbSiEsUsted);
                    rbgEsUsted.removeView(rbNoEsUsted);
                    lyDatosInformante.removeView(txtEsUstedPersonaConSintomas);

                    rbgUsted = null;
                    rbSiUsted = null;
                    rbNoUsted = null;

                    if (rbContactoSi.getParent() != null) {
                        ((ViewGroup) rbContactoSi.getParent()).removeView(rbContactoSi);
                    }
                    if (rbContactoNo.getParent() != null) {
                        ((ViewGroup) rbContactoNo.getParent()).removeView(rbContactoNo);
                    }
                    if (rbgContacto.getParent() != null) {
                        ((ViewGroup) rbgContacto.getParent()).removeView(rbgContacto);
                    }
                    if (txtLleneDeNuevo.getParent() != null) {
                        ((ViewGroup) txtLleneDeNuevo.getParent()).removeView(txtLleneDeNuevo);
                    }
                    if (txtCuarentena.getParent() != null) {
                        ((ViewGroup) txtCuarentena.getParent()).removeView(txtCuarentena);
                    }
                    if (txtPersonasConSintomas.getParent() != null) {
                        ((ViewGroup) txtPersonasConSintomas.getParent()).removeView(txtPersonasConSintomas);
                    }
                    if (txtCantidadPersonasSintomas.getParent() != null) {
                        ((ViewGroup) txtCantidadPersonasSintomas.getParent()).removeView(txtCantidadPersonasSintomas);
                    }

                    lyDatosInformante.removeView(txtContacto);
                    lyDatosInformante.removeView(etCantidadPersonasSintomas);
                    etNumPersonasSintomas = null;
                    mostrarOpcionesSintomasNo(txtContacto, rbgContacto, rbContactoSi, rbContactoNo);
                    presionoSintomasNo = true;
                    presionoSintomasSi = false;
                } else {
                    if (rbContactoSi.getParent() != null) {
                        ((ViewGroup) rbContactoSi.getParent()).removeView(rbContactoSi);
                    }
                    if (rbContactoNo.getParent() != null) {
                        ((ViewGroup) rbContactoNo.getParent()).removeView(rbContactoNo);
                    }

                    lyDatosInformante.removeView(rbgContacto);
                    lyDatosInformante.removeView(txtContacto);
                    mostrarOpcionesSintomasNo(txtContacto, rbgContacto, rbContactoSi, rbContactoNo);
                    presionoSintomasNo = true;
                    presionoSintomasSi = false;
                }

            }
        });

        //Permite indicar advertencias de si el usuario debe guardar cuarentena o a su vez si no tiene
        //sintomas el y su nucleo familiar se le indica que puede volver a llenar informacion si
        //alguien tuviese COVID-19
        rbgContacto.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == rbContactoSi.getId() && !presionoContactoSi) {
                if (presionoContactoNo) {
                    lyDatosInformante.removeView(txtLleneDeNuevo);
                    mostrarTextoCuarentena(txtCuarentena);
                    presionoContactoSi = true;
                    presionoContactoNo = false;
                } else {
                    mostrarTextoCuarentena(txtCuarentena);
                    presionoContactoSi = true;
                    presionoContactoNo = false;
                }
            } else if (checkedId == rbContactoNo.getId() && !presionoContactoNo) {
                if (presionoContactoSi) {
                    lyDatosInformante.removeView(txtCuarentena);
                    mostrarTextoLlenarDeNuevo(txtLleneDeNuevo);
                    presionoContactoNo = true;
                    presionoContactoSi = false;
                } else {
                    mostrarTextoLlenarDeNuevo(txtLleneDeNuevo);
                    presionoContactoNo = true;
                    presionoContactoSi = false;
                }
            }
        });

    } //fin metodo OnCreate

    //-----------------------------------------VALIDACIONES DE CAMPOS PROPIOS DE FORMULARIO----------------------------------------------------

    public boolean validarNumeroPersonas() {
        String cantidadPersonas = requireNonNull(txtInputPersonas.getEditText()).getText().toString().trim();
        if (cantidadPersonas.isEmpty()) {
            txtInputPersonas.setError("Campo requerido");
            return false;
        } else if (Integer.parseInt(cantidadPersonas) > 23) {
            txtInputPersonas.setError("Máximo permitido 23");
            return false;
        } else {
            txtInputPersonas.setError(null);
            return true;
        }
    }

    private boolean validarNumeroDormitorios(){
        String numeroDormitorios = requireNonNull(txtInputNumDormitorios.getEditText()).getText().toString().trim();
        if (numeroDormitorios.isEmpty()){
            txtInputNumDormitorios.setError("Campo requerido");
            return false;
        } else if (Integer.parseInt(numeroDormitorios) > 20){
            txtInputNumDormitorios.setError("Máximo permitido 20");
            return false;
        } else if (Integer.parseInt(numeroDormitorios) < 0){
            txtInputNumDormitorios.setError("No se permite cantidades negativas");
            return false;
        } else {
            txtInputNumDormitorios.setError(null);
            return true;
        }
    }

    private boolean validarNumeroPersonasTrabajan(){
        String numPersonasTrabajan = requireNonNull(txtInputNumPersonasTrabajan.getEditText()).getText().toString().trim();
        String numPersonasHogar;
        if (requireNonNull(txtInputPersonas.getEditText()).getText().toString().trim().isEmpty()){
            numPersonasHogar = "0";
        } else {
            numPersonasHogar = txtInputPersonas.getEditText().getText().toString().trim();
        }
        if (numPersonasTrabajan.isEmpty()){
            txtInputNumPersonasTrabajan.setError("Campo requerido");
            return false;
        } else if (Integer.parseInt(numPersonasTrabajan) > Integer.parseInt(numPersonasHogar)){
            txtInputNumPersonasTrabajan.setError("Cantidad ingresada no puede ser mayor que el número de personas en el hogar");
            return false;
        } else if (Integer.parseInt(numPersonasTrabajan) < 0){
            txtInputNumPersonasTrabajan.setError("No se permite cantidades negativas");
            return false;
        } else {
            txtInputNumPersonasTrabajan.setError(null);
            return true;
        }
    }

    private boolean validarNumeroPersonasTrabajoFijo(){
        String numeroPersonasTrabajoFijo = requireNonNull(txtInputNumPersonasEmpleoFijo.getEditText()).getText().toString().trim();
        int numPersonasTrabajan;
        int numPersonasHogar;

        if (requireNonNull(txtInputPersonas.getEditText()).getText().toString().trim().isEmpty()){
            numPersonasHogar = 0;
        } else {
            numPersonasHogar = Integer.parseInt(txtInputPersonas.getEditText().getText().toString().trim());
        }

        if (requireNonNull(txtInputNumPersonasTrabajan.getEditText()).getText().toString().trim().isEmpty()){
            numPersonasTrabajan = 0;
        }else {
            numPersonasTrabajan = Integer.parseInt(txtInputNumPersonasTrabajan.getEditText().getText().toString().trim());
        }

        if (numeroPersonasTrabajoFijo.isEmpty()){
            txtInputNumPersonasEmpleoFijo.setError("Campo requerido");
            return false;
        } else if (Integer.parseInt(numeroPersonasTrabajoFijo) > numPersonasTrabajan){
            txtInputNumPersonasEmpleoFijo.setError("Cantidad ingresada no puede ser mayor que cantidad de personas que trabajan");
            return false;
        } else if (Integer.parseInt(numeroPersonasTrabajoFijo) > numPersonasHogar) {
            txtInputNumPersonasEmpleoFijo.setError("Cantidad ingresada no puede ser mayor que cantidad de personas en el hogar");
            return false;
        } else if (Integer.parseInt(numeroPersonasTrabajoFijo) < 0) {
            txtInputNumPersonasEmpleoFijo.setError("No se permite cantidades negativas");
            return false;
        } else {
            txtInputNumPersonasEmpleoFijo.setError(null);
            return true;
        }
    }

    @SuppressLint("ResourceType")
    public boolean validarSintomas() {
        if (rbgSintomas.getCheckedRadioButtonId() <= 0) {
            rbSintomasNo.setError("Seleccione una opción");
            return false;
        } else {
            rbSintomasNo.setError(null);
            return true;
        }
    }

    @SuppressLint("ResourceType")
    public boolean validarEsUsted() {
        if (presionoSintomasSi) {
            if (rbgUsted.getCheckedRadioButtonId() <= 0) {
                rbNoUsted.setError("Seleccione una opción");
                return false;
            } else {
                rbNoUsted.setError(null);
                return true;
            }
        } else {
            return true;
        }
    }

    public boolean validarCantidadPersonasSintomas() {
        if (presionoSintomasSi) {
            if ((etNumPersonasSintomas.getText().toString().trim()).isEmpty()) {
                etNumPersonasSintomas.setError("Campo requerido");
                return false;
            } else if ((Integer.parseInt(etNumPersonasSintomas.getText().toString().trim()))
                    > (Integer.parseInt(requireNonNull(txtInputPersonas.getEditText()).getText().toString().trim()))) {
                etNumPersonasSintomas.setError("Valor ingresado no puede ser mayor que numero de personas en su hogar");
                return false;
            } else if ((Integer.parseInt(etNumPersonasSintomas.getText().toString().trim())) <= 0) {
                etNumPersonasSintomas.setError("Valor no puede ser menor o igual a 0");
                return false;
            } else {
                etNumPersonasSintomas.setError(null);
                return true;
            }
        } else {
            return true;
        }
    }

    @SuppressLint("ResourceType")
    public boolean validarContactoCovid() {
        if (presionoSintomasNo) {
            if (rbgCont.getCheckedRadioButtonId() <= 0) {
                rbContNo.setError("Seleccione una opción");
                return false;
            } else {
                rbContNo.setError(null);
                return true;
            }
        } else {
            return true;
        }
    }

    @SuppressLint("ResourceType")
    private boolean validarPropiedadVivienda(){
        if (rbgPropiedadVivienda.getCheckedRadioButtonId() <= 0){
            rbOtro.setError("Seleccione una opción");
            return false;
        } else {
            rbOtro.setError(null);
            return true;
        }
    }

    @SuppressLint("ResourceType")
    private boolean validarBanoCompartido(){
        if (rbgBanioCompartido.getCheckedRadioButtonId() <= 0){
            rbNoComparte.setError("Seleccione una opción");
            return false;
        } else {
            rbNoComparte.setError(null);
            return true;
        }
    }

    @SuppressLint("ResourceType")
    private boolean validarPagoArriendoServicios(){
        if (rbgPagoArriendoServiciosBasicos.getCheckedRadioButtonId() <= 0){
            rbNoPago.setError("Seleccione una opción");
            return false;
        } else {
            rbNoPago.setError(null);
            return true;
        }
    }

    @SuppressLint("ResourceType")
    private boolean validarProblemasComida(){
        if (rbgProblemasComida.getCheckedRadioButtonId() <= 0){
            rbNoProblemaComida.setError("Seleccione una opcion");
            return false;
        } else {
            rbNoProblemaComida.setError(null);
            return true;
        }
    }

    //'------------------------------------------FIN VALIDACIONES---------------------------------------------------------------------

    /**
     * Accion de boton siguiente
     *
     * @param view Vista donde se estará escuchando el evento click
     */
    public void GuardarDatosPersonales(View view) {
        if (!Utileria.validarApellidos(txtInputApellidos) | !Utileria.validarNombres(txtInputNombres)
                | !Utileria.validarCedula(txtInputCedula, editar, cedulasEstudiantes) | !Utileria.validarEdad(txtInputEdad)
                | !Utileria.validarSexo(rbgSexo, rbFemenino) | !Utileria.validarCelular(txtInputCelular)
                | !Utileria.validarFijo(txtInputFijo) | !Utileria.validarCorreo(txtInputCorreo)
                | !validarNumeroPersonas() | !validarSintomas() | !validarCantidadPersonasSintomas()
                | !validarContactoCovid() | !validarEsUsted() | !validarNumeroDormitorios() | !validarNumeroPersonasTrabajan()
                | !validarNumeroPersonasTrabajoFijo() | !validarPropiedadVivienda() | !validarBanoCompartido()
                | !validarPagoArriendoServicios() | !validarProblemasComida()) {
            Toast.makeText(getApplicationContext(), "Lo sentimos, algunos campos están vacíos" +
                    " o no son válidos, revíselos por favor", Toast.LENGTH_LONG).show();
        } else {
            //recoger datos
            String apellidos = Utileria.cleanString(requireNonNull(txtInputApellidos.getEditText()).getText().toString().trim()).toUpperCase();
            String nombres = Utileria.cleanString(requireNonNull(txtInputNombres.getEditText()).getText().toString().trim()).toUpperCase();
            String cedula = requireNonNull(txtInputCedula.getEditText()).getText().toString().trim();
            int edad = Integer.parseInt(requireNonNull(txtInputEdad.getEditText()).getText().toString().trim());
            String sexo = "";
            if (rbMasculino.isChecked()) {
                sexo = rbMasculino.getText().toString().trim().toUpperCase();
            } else if (rbFemenino.isChecked()) {
                sexo = rbFemenino.getText().toString().trim().toUpperCase();
            }
            String celular = requireNonNull(txtInputCelular.getEditText()).getText().toString().trim();
            String fijo = "";
            if (!(requireNonNull(txtInputFijo.getEditText()).getText().toString().trim()).isEmpty()) {
                fijo = txtInputFijo.getEditText().getText().toString().trim();
            }
            String correo = Utileria.cleanString(requireNonNull(txtInputCorreo.getEditText()).getText().toString().trim());
            int numeroPersonasHogar = Integer.parseInt(requireNonNull(txtInputPersonas.getEditText()).getText().toString().trim());
            String alguienPresentaSintomas = "";
            if (rbSintomasSi.isChecked()) {
                alguienPresentaSintomas = rbSintomasSi.getText().toString().trim().toUpperCase();
            } else if (rbSintomasNo.isChecked()) {
                alguienPresentaSintomas = rbSintomasNo.getText().toString().trim().toUpperCase();
            }

            int numeroDormitorios = Integer.parseInt(requireNonNull(txtInputNumDormitorios.getEditText()).getText().toString().trim());
            int numeroPersonasTrabajan = Integer.parseInt(requireNonNull(txtInputNumPersonasTrabajan.getEditText()).getText().toString().trim());
            int numeroPersonasTrabajoFijo = Integer.parseInt(requireNonNull(txtInputNumPersonasEmpleoFijo.getEditText()).getText().toString().trim());

            String propiedadVivienda = "";
            if (rbPropia.isChecked()){
                propiedadVivienda = rbPropia.getText().toString().trim().toUpperCase();
            } else if (rbArrendada.isChecked()) {
                propiedadVivienda = rbArrendada.getText().toString().trim().toUpperCase();
            } else if (rbPrestada.isChecked()) {
                propiedadVivienda = rbPrestada.getText().toString().trim().toUpperCase();
            } else if (rbOtro.isChecked()) {
                propiedadVivienda = rbOtro.getText().toString().trim().toUpperCase();
            }

            String banoCompartido = "";
            if (rbSiComparte.isChecked()){
                banoCompartido = rbSiComparte.getText().toString().trim().toUpperCase();
            } else if (rbNoComparte.isChecked()){
                banoCompartido = rbNoComparte.getText().toString().trim().toUpperCase();
            }

            String pagoArriendoServicios = "";
            if (rbSiPago.isChecked()) {
                pagoArriendoServicios = rbSiPago.getText().toString().trim().toUpperCase();
            } else if (rbNoPago.isChecked()){
                pagoArriendoServicios = rbNoPago.getText().toString().trim().toUpperCase();
            }

            String problemasComida = "";
            if (rbSiProblemaComida.isChecked()){
                problemasComida = rbSiProblemaComida.getText().toString().trim().toUpperCase();
            } else if (rbNoProblemaComida.isChecked()) {
                problemasComida = rbNoProblemaComida.getText().toString().trim().toUpperCase();
            }

            int cantidadPersonasSintomas = 0;
            String contactoPersonasCovid = "";
            String estudiantePresentaSintomas = "";

            String lat;
            String lng;

            lat = latitud.getText().toString().trim();
            lng = longitud.getText().toString().trim();
            System.out.println("LATITUD: " + lat);
            System.out.println("LONGITUD: " + lng);

            Date fecha = new Date();
            SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String fechaConFormato = formato.format(fecha);
            String fechaModConFormato = "";
            if (editar){
                Date fechaMod = new Date();
                fechaConFormato = fechaRegistroObtenida;
                fechaModConFormato = formato.format(fechaMod);
            }

            SharedPreferences prefEstudiante = getSharedPreferences("credenciales", MODE_PRIVATE);
            String usuario = prefEstudiante.getString("email","");

            if (presionoSintomasSi) {

                //Recoger datos de si es el estudiante el que tiene sintomas COVID-19
                if (rbSiUsted.isChecked()) {
                    estudiantePresentaSintomas = rbSiUsted.getText().toString().trim().toUpperCase();
                } else if (rbNoUsted.isChecked()) {
                    estudiantePresentaSintomas = rbNoUsted.getText().toString().trim().toUpperCase();
                }

                //recoger dato cantidad de familiares con sintomas covid
                cantidadPersonasSintomas = Integer.parseInt(etNumPersonasSintomas.getText().toString().trim());
                System.out.println("CANTIDAD PERSONAS SINTOMAS: " + etNumPersonasSintomas.getText().toString());
                /*Inicializa y establece la referencia
                FirebaseDatabase db = FirebaseDatabase.getInstance();
                DatabaseReference datosPersona = db.getReference("estudiantes");
                */

                SharedPreferences preferencias = getSharedPreferences("datosPersona", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = preferencias.edit();
                editor.putString("apellidos", apellidos);
                editor.putString("nombres", nombres);
                editor.putString("cedula", cedula);
                editor.putInt("edad", edad);
                editor.putString("sexo", sexo.toUpperCase());
                editor.putString("celular", celular);
                editor.putString("latitud", lat);
                editor.putString("longitud", lng);
                editor.putString("fijo", fijo);
                editor.putString("correo", correo);
                editor.putInt("numeroPersonasHogar", numeroPersonasHogar);
                editor.putString("alguienPresentaSintomas", alguienPresentaSintomas);
                editor.putString("estudiantePresentaSintomas", estudiantePresentaSintomas);
                editor.putInt("cantidadPersonasSintomas", cantidadPersonasSintomas);
                editor.putString("contactoPersonasCovid", contactoPersonasCovid);
                editor.commit();

                /*Guarda DatosPersona Firebase
                Persona persona = new Persona(new Date(), lng, lat, apellidos,nombres,cedula,edad,sexo,celular,fijo,correo,numeroPersonasHogar,presentaSintomas,cantidadPersonasSintomas,contactoPersonasCovid);
                datosPersona.push().setValue(persona);*/
                estudiante = new Estudiante(fechaConFormato, fechaModConFormato, lng, lat, apellidos
                        , nombres, cedula, edad, sexo, celular, fijo, correo, numeroPersonasHogar
                        , alguienPresentaSintomas, estudiantePresentaSintomas, cantidadPersonasSintomas
                        , contactoPersonasCovid, usuario, numeroDormitorios, numeroPersonasTrabajan
                        , numeroPersonasTrabajoFijo, propiedadVivienda, banoCompartido, pagoArriendoServicios
                        , problemasComida);

                if (!editar){
                    Toast.makeText(getApplicationContext(), "Datos guardados con éxito" +
                            " proceda a llenar siguiente formulario", Toast.LENGTH_LONG).show();

                    locationManager.removeUpdates(loc);
                    Intent ticketEstudiante = new Intent(getApplicationContext(), TicketEstudianteActivity.class);
                    ticketEstudiante.putExtra("cantidadPersonasSintomas", etNumPersonasSintomas.getText().toString().trim());
                    ticketEstudiante.putExtra("estudiantePresentaSintomas", estudiantePresentaSintomas);
                    startActivity(ticketEstudiante);
                    finish();
                }else {
                    if (tieneFamiliares){
                        System.out.println("ENTRA A OPCION LISTAR FAMILIARES");
                        //Llamar menu lista de familiares
                        estudiante.setFechaRegistroPersona(fechaRegistroObtenida);
                        editarEstudiante(estudiante);
                        Intent activityListaFamiliares = new Intent(getApplicationContext(), ListarFamiliaresActivity.class);
                        startActivity(activityListaFamiliares);
                    }else {
                        System.out.println("ENTRA A OPCION FORMULARIO FAMILIARES");
                        //Llamar Formulario ingreso nuevo familiar
                        estudiante.setFechaRegistroPersona(fechaRegistroObtenida);
                        Intent datosFamiliar = new Intent(getApplicationContext(), DatosFamiliaresActivity.class);
                        datosFamiliar.putExtra("cantidadPersonasSintomas", Integer.parseInt(etNumPersonasSintomas.getText().toString().trim()));
                        datosFamiliar.putExtra("estudiantePresentaSintomas", estudiantePresentaSintomas);
                        //datosFamiliar.putExtra("editar", editar);
                        startActivity(datosFamiliar);
                    }
                }


            } else {
                //recoger datos contacto con persona diagnosticada de covid
                if (rbContSi.isChecked()) {
                    contactoPersonasCovid = rbContSi.getText().toString().trim().toUpperCase();
                } else if (rbContNo.isChecked()) {
                    contactoPersonasCovid = rbContNo.getText().toString().trim().toUpperCase();
                }

                //Inicializa y establece la referencia
                FirebaseDatabase db = FirebaseDatabase.getInstance();
                DatabaseReference datosPersona = db.getReference("estudiantes");

                estudiantePresentaSintomas = "NO";

                SharedPreferences preferencias = getSharedPreferences("datosPersona", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = preferencias.edit();
                editor.putString("apellidos", apellidos);
                editor.putString("nombres", nombres);
                editor.putString("cedula", cedula);
                editor.putInt("edad", edad);
                editor.putString("sexo", sexo.toUpperCase());
                editor.putString("celular", celular);
                editor.putString("fijo", fijo);
                editor.putString("correo", correo);
                editor.putString("latitud", lat);
                editor.putString("longitud", lng);
                editor.putInt("numeroPersonasHogar", numeroPersonasHogar);
                editor.putString("alguienPresentaSintomas", alguienPresentaSintomas);
                editor.putString("estudiantePresentaSintomas", estudiantePresentaSintomas);
                editor.putInt("cantidadPersonasSintomas", cantidadPersonasSintomas);
                editor.putString("contactoPersonasCovid", contactoPersonasCovid);
                editor.commit();

                if (!editar){
                    //Guarda Datos Persona que no tiene ningún familiar con síntomas Firebase
                    estudiante = new Estudiante(fechaConFormato, fechaModConFormato, lng, lat, apellidos
                            , nombres, cedula, edad, sexo, celular, fijo, correo, numeroPersonasHogar
                            , alguienPresentaSintomas, estudiantePresentaSintomas, cantidadPersonasSintomas
                            , contactoPersonasCovid, usuario, numeroDormitorios, numeroPersonasTrabajan
                            , numeroPersonasTrabajoFijo, propiedadVivienda, banoCompartido, pagoArriendoServicios
                            , problemasComida);
                    datosPersona.push().setValue(estudiante);
                    generarRegistro();
                    new AsyncTaskInsertarEstudiante(getApplicationContext()).execute(estudiante);
                    System.out.println("RESPUESTA SERVICIO ESTUDIANTE: " + AsyncTaskInsertarEstudiante.resultado);
                    Toast.makeText(getApplicationContext(), "Datos guardados con éxito, " +
                            "gracias por su ayuda", Toast.LENGTH_LONG).show();

                    locationManager.removeUpdates(loc);
                } else {
                    //Edita Datos Persona que no tiene ningún familiar con síntomas
                    estudiante = new Estudiante(fechaConFormato, fechaModConFormato, lng, lat, apellidos
                            , nombres, cedula, edad, sexo, celular, fijo, correo, numeroPersonasHogar
                            , alguienPresentaSintomas, estudiantePresentaSintomas, cantidadPersonasSintomas
                            , contactoPersonasCovid, usuario, numeroDormitorios, numeroPersonasTrabajan
                            , numeroPersonasTrabajoFijo, propiedadVivienda, banoCompartido, pagoArriendoServicios
                            , problemasComida);
                    editarEstudiante(estudiante);
                }
                Intent menu = new Intent(getApplicationContext(), MenuActivity.class);
                menu.putExtra("desactivaNuevoForm", true);
                menu.putExtra("habilitarEditar", true);
                startActivity(menu);
                finish();
            }
        }
    }

    /**
     * Permite escuchar cuando el boton de accion de la toolbar es pulsado para permitir
     * regresar al menu principal
     *
     * @param item MenuItem
     * @return true or false
     */
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            if (!editar) {
                locationManager.removeUpdates(loc);
                Intent menu = new Intent(getApplicationContext(), MenuActivity.class);
                startActivity(menu);
                this.finish();
            }else {
                Intent menuEditar = new Intent(getApplicationContext(), MenuEditarActivity.class);
                startActivity(menuEditar);
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Accion de boton regresar del sistema
     */
    @Override
    public void onBackPressed() {
        if (!editar){
            locationManager.removeUpdates(loc);
            Intent menu = new Intent(getApplicationContext(), MenuActivity.class);
            startActivity(menu);
            finish();
        }else {
            Intent menuEditar = new Intent(getApplicationContext(), MenuEditarActivity.class);
            startActivity(menuEditar);
        }

    }

    //Metodos para obtener ubicacion geográfica de informante
    private void locationStart() {
        LocationManager mlocManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Localizacion local = new Localizacion();
        locationManager = mlocManager;
        loc = local;
        local.setDatosPersonalesActivity(this, latitud, longitud);
        final boolean gpsEnabled = requireNonNull(mlocManager).isProviderEnabled(LocationManager.GPS_PROVIDER);
        if (!gpsEnabled) {
            Intent settingsIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivity(settingsIntent);
        }
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION,}, 1000);
            return;
        }
        mlocManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 10000, 0, local);
        mlocManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 10000, 0, local);
//        latitud.setText("");
//        longitud.setText("");
    }

    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == 1000) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                locationStart();
            }
        }
    }

    public void generarRegistro() {
        String file_path = (Environment.getExternalStorageDirectory().toString());
        File file = new File(file_path, "log_emer" + MainActivity.usuario);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (Exception e) {
                Log.e("ErrorGenReg", requireNonNull(e.getMessage()));
            }
        }
    }

    /**
     * Permite la carga de datos del usuario que ingreso a la app mediante conexion a servidor LDAP
     */
    public void cargarDatosUsuarioLdap(){
        SharedPreferences preferences = getSharedPreferences("UsuarioObtenido", MODE_PRIVATE);
        requireNonNull(txtInputApellidos.getEditText()).setText(preferences.getString("apellidos", ""));
        requireNonNull(txtInputNombres.getEditText()).setText(preferences.getString("nombres", ""));
        requireNonNull(txtInputCedula.getEditText()).setText(preferences.getString("cedula", ""));
        requireNonNull(txtInputCorreo.getEditText()).setText(preferences.getString("correo",""));
    }

    /**
     * Obtiene los datos del Estudiante cuando se ha presionado la opción editar datos estudiante
     */
    public void cargarDatosEstudianteEditar(final TextView txtContacto, final RadioGroup rbgContacto
            , final RadioButton rbContactoSi, final RadioButton rbContactoNo, final TextView txtCuarentena
            , final TextView txtLleneDeNuevo, final TextView txtEsUstedPersonaConSintomas
            , final RadioGroup rbgEsUsted, final RadioButton rbSiEsUsted, final RadioButton rbNoEsUsted
            , final TextView txtPersonasConSintomas, final TextView txtCantidadPersonasSintomas
            , final EditText etCantidadPersonasSintomas){
        Estudiante estTemp = MenuActivity.estudiante;
        latitud.setText(estTemp.getLat());
        longitud.setText(estTemp.getLng());
        fechaRegistroObtenida = estTemp.getFechaRegistroPersona();
        requireNonNull(txtInputApellidos.getEditText()).setText(estTemp.getApellidos());
        requireNonNull(txtInputNombres.getEditText()).setText(estTemp.getNombres());
        requireNonNull(txtInputCedula.getEditText()).setText(estTemp.getCedula());
        requireNonNull(txtInputEdad.getEditText()).setText(String.valueOf(estTemp.getEdad()));
        if (estTemp.getSexo().equalsIgnoreCase("MASCULINO")){
            rbMasculino.setChecked(true);
        }else {
            rbFemenino.setChecked(true);
        }
        requireNonNull(txtInputCelular.getEditText()).setText(estTemp.getCelular());
        requireNonNull(txtInputFijo.getEditText()).setText(estTemp.getTelFijo());
        requireNonNull(txtInputCorreo.getEditText()).setText(estTemp.getCorreo());
        requireNonNull(txtInputPersonas.getEditText()).setText(String.valueOf(estTemp.getNumPersonasHogar()));
        requireNonNull(txtInputNumDormitorios.getEditText()).setText(String.valueOf(estTemp.getCantidadDormitorios()));
        requireNonNull(txtInputNumPersonasTrabajan.getEditText()).setText(String.valueOf(estTemp.getCantidadPersonasTrabajan()));
        requireNonNull(txtInputNumPersonasEmpleoFijo.getEditText()).setText(String.valueOf(estTemp.getCantidadPersonasEmpleoFijo()));

        String propiedadVivienda = estTemp.getPropiedadVivienda();
        System.out.println("TIPO VIVIENDA: " + propiedadVivienda);
        if (propiedadVivienda.equalsIgnoreCase("PROPIA")){
            rbgPropiedadVivienda.check(rbPropia.getId());
        } else if (propiedadVivienda.equalsIgnoreCase("ARRENDADA")){
            rbgPropiedadVivienda.check(rbArrendada.getId());
        }else if (propiedadVivienda.equalsIgnoreCase("PRESTADA")){
            rbgPropiedadVivienda.check(rbPrestada.getId());
        }else if (propiedadVivienda.equalsIgnoreCase("OTRO")){
            rbgPropiedadVivienda.check(rbOtro.getId());
        }

        String banoCompartido = estTemp.getBanioCompartido();
        if (banoCompartido.equalsIgnoreCase("SI")){
            rbgBanioCompartido.check(rbSiComparte.getId());
        } else if (banoCompartido.equalsIgnoreCase("NO")){
            rbgBanioCompartido.check(rbNoComparte.getId());
        }

        String pagoArriendoServicios = estTemp.getPagoArriendoServiciosBasicos();
        if (pagoArriendoServicios.equalsIgnoreCase("SI")){
            rbgPagoArriendoServiciosBasicos.check(rbSiPago.getId());
        } else if (banoCompartido.equalsIgnoreCase("NO")){
            rbgPagoArriendoServiciosBasicos.check(rbNoPago.getId());
        }

        String problemaComida = estTemp.getProblemasCompraComida();
        if (problemaComida.equalsIgnoreCase("SI")){
            rbgProblemasComida.check(rbSiProblemaComida.getId());
        } else if (problemaComida.equalsIgnoreCase("NO")){
            rbgProblemasComida.check(rbNoProblemaComida.getId());
        }

        if (estTemp.getAlguienPresentaSintomas().equalsIgnoreCase("NO")){
            mostrarOpcionesSintomasNo(txtContacto, rbgContacto, rbContactoSi, rbContactoNo);
            presionoSintomasNo = true;
            presionoSintomasSi = false;
            rbgSintomas.check(rbSintomasNo.getId());
            if (estTemp.getContactoPersonaConCovid().equalsIgnoreCase("NO")){
                rbgCont.check(rbContNo.getId());
                mostrarTextoLlenarDeNuevo(txtLleneDeNuevo);
                presionoContactoNo = true;
                presionoContactoSi = false;
            }else {
                rbgCont.check(rbContSi.getId());
                mostrarTextoCuarentena(txtCuarentena);
                presionoContactoNo = false;
                presionoContactoSi = true;
            }
        }else {
            mostrarOpcionesSintomasSi(txtEsUstedPersonaConSintomas, rbgEsUsted, rbSiEsUsted, rbNoEsUsted
                    , txtPersonasConSintomas, txtCantidadPersonasSintomas, etCantidadPersonasSintomas);
            presionoSintomasNo = false;
            presionoSintomasSi = true;
            rbgSintomas.check(rbSintomasSi.getId());
            if (estTemp.getEstudiantePresentaSintomas().equalsIgnoreCase("SI")){
                rbgUsted.check(rbSiUsted.getId());
            }else {
                rbgUsted.check(rbNoUsted.getId());
            }
            etNumPersonasSintomas.setText(String.valueOf(estTemp.getCantidadPersonasSintomas()));
        }
    }

    /**
     * Realiza la edicion de datos del estudiante en la base de datos mediante un proceso en background
     * @param estudiante Objeto Estudiante que se desea actualizar
     */
    private void editarEstudiante(Estudiante estudiante){
        Call<Boolean> callEditEstudiante = EstudianteApiAdapter.getApiService().actualizarEstudiante(
                getSharedPreferences("token", MODE_PRIVATE).getString("token",""), estudiante);
        callEditEstudiante.enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                if (response.isSuccessful()){
                    boolean resultado = response.body();
                    if (resultado){
                        Toast.makeText(getApplicationContext(), "Datos editados satisfactoriamente", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<Boolean> call, Throwable t) {
                Log.e("ErrEditEst", requireNonNull(t.getMessage()));
            }
        });
    }

    @Override
    public void onResponse(Call<List<String>> call, Response<List<String>> response) {
        if (response.isSuccessful()){
            List<String> lista = response.body();
            actualizarListaCedulasEstudiantes(lista);
//            cedulasEstudiantes = lista;
            System.out.println("TAMANO: " + cedulasEstudiantes.size());
        }
    }

    @Override
    public void onFailure(Call<List<String>> call, Throwable t) {

    }

    public void actualizarListaCedulasEstudiantes(List<String> listaCedulas){
        cedulasEstudiantes = new ArrayList<>();
        cedulasEstudiantes = listaCedulas;
//        if (editar){
//            cedulasEstudiantes.remove(getSharedPreferences("UsuarioObtenido", MODE_PRIVATE).getString("cedula", ""));
//        }
        Utileria.validarCedula(txtInputCedula, editar, cedulasEstudiantes);
    }

    /**
     * Permite mostrar preguntas si el usuario selecciona si tiene síntomas
     * @param txtEsUstedPersonaConSintomas TextView que contiene texto de pregunta si el estudiante tiene síntomas
     * @param rbgEsUsted RadioGroup que contiene las opciones de la pregunta
     * @param rbSiEsUsted RadioButton que permite obtener respuesta del usuario
     * @param rbNoEsUsted RadioButton que permite obtener respuesta del usuario
     * @param txtPersonasConSintomas TextView que contiene texto de la pregunta
     * @param txtCantidadPersonasSintomas TextView que contiene texto de siguiente pregunta
     * @param etCantidadPersonasSintomas EditText que permite ingresar una respuesta al usuario
     */
    private void mostrarOpcionesSintomasSi(final TextView txtEsUstedPersonaConSintomas, final RadioGroup rbgEsUsted
            , final RadioButton rbSiEsUsted, final RadioButton rbNoEsUsted, final TextView txtPersonasConSintomas
            , final TextView txtCantidadPersonasSintomas, final EditText etCantidadPersonasSintomas){
        txtEsUstedPersonaConSintomas.setLayoutParams(new ActionBar.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        txtEsUstedPersonaConSintomas.setText(R.string.txtEsUstedPersonaConSintomas);
        txtEsUstedPersonaConSintomas.setTextColor(getResources().getColor(R.color.negro));
        lyDatosInformante.addView(txtEsUstedPersonaConSintomas);

        rbgEsUsted.setLayoutParams(new ActionBar.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        rbgEsUsted.setOrientation(LinearLayout.HORIZONTAL);
        rbgUsted = rbgEsUsted;
        lyDatosInformante.addView(rbgEsUsted);

        rbSiEsUsted.setLayoutParams(new ActionBar.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        rbNoEsUsted.setLayoutParams(new ActionBar.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        rbSiEsUsted.setText(R.string.rbSi);
        rbNoEsUsted.setText(R.string.rbNo);
        rbSiEsUsted.setTextColor(getResources().getColor(R.color.negro));
        rbNoEsUsted.setTextColor(getResources().getColor(R.color.negro));
        rbSiUsted = rbSiEsUsted;
        rbNoUsted = rbNoEsUsted;
        rbgEsUsted.addView(rbSiEsUsted);
        rbgEsUsted.addView(rbNoEsUsted);

        txtPersonasConSintomas.setLayoutParams(new ActionBar.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        txtPersonasConSintomas.setText(R.string.txtPersonasConSintomas);
        txtPersonasConSintomas.setTextColor(getResources().getColor(R.color.azul_app));
        txtPersonasConSintomas.setAllCaps(true);
        txtPersonasConSintomas.setTextSize(16);
        lyDatosInformante.addView(txtPersonasConSintomas);

        txtCantidadPersonasSintomas.setLayoutParams(new ActionBar.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        txtCantidadPersonasSintomas.setText(R.string.txtCantidadPersonasConSintomas);
        txtCantidadPersonasSintomas.setTextColor(getResources().getColor(R.color.negro));
        lyDatosInformante.addView(txtCantidadPersonasSintomas);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.width = 200;

        etCantidadPersonasSintomas.setLayoutParams(params);
        etCantidadPersonasSintomas.setInputType(InputType.TYPE_CLASS_NUMBER);
        etCantidadPersonasSintomas.setHint(R.string.etCantidadPersonasSintomas);
        etCantidadPersonasSintomas.setEms(10);
        etCantidadPersonasSintomas.setTextColor(getResources().getColor(R.color.negro));
        etNumPersonasSintomas = etCantidadPersonasSintomas;
        lyDatosInformante.addView(etCantidadPersonasSintomas);
    }

    /**
     * Permite mostrar preguntas si el usuario selecciona que no tiene síntomas
     * @param txtContacto TextView
     * @param rbgContacto RadioGroup
     * @param rbContactoSi RadioButton
     * @param rbContactoNo RadioButton
     */
    private void mostrarOpcionesSintomasNo(final TextView txtContacto, final RadioGroup rbgContacto
            , final RadioButton rbContactoSi, final RadioButton rbContactoNo){
        txtContacto.setLayoutParams(new ActionBar.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        txtContacto.setText(R.string.txtContacto);
        txtContacto.setTextColor(getResources().getColor(R.color.negro));
        lyDatosInformante.addView(txtContacto);

        rbgContacto.setLayoutParams(new ActionBar.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        rbgContacto.setOrientation(LinearLayout.HORIZONTAL);
        rbgCont = rbgContacto;
        lyDatosInformante.addView(rbgContacto);

        rbContactoSi.setLayoutParams(new ActionBar.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        rbContactoNo.setLayoutParams(new ActionBar.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        rbContactoSi.setText(R.string.rbSi);
        rbContactoNo.setText(R.string.rbNo);
        rbContactoSi.setTextColor(getResources().getColor(R.color.negro));
        rbContactoNo.setTextColor(getResources().getColor(R.color.negro));
        rbContSi = rbContactoSi;
        rbContNo = rbContactoNo;
        rbgContacto.addView(rbContactoSi);
        rbgContacto.addView(rbContactoNo);
    }

    /**
     * Permite mostrar texto de advertencia al usuario si ha tenido contacto con alguna persona
     * con COVID-19
     * @param txtCuarentena TextView
     */
    private void mostrarTextoCuarentena(final TextView txtCuarentena){
        txtCuarentena.setLayoutParams(new ActionBar.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        txtCuarentena.setText(R.string.txtCuarentena);
        txtCuarentena.setTextColor(getResources().getColor(R.color.negro));
        lyDatosInformante.addView(txtCuarentena);
    }

    /**
     * Permite mostrar texto que le dice al usuario que debe llenar de nuevo sus datos si en algún
     * momento tiene síntomas de COVID-19
     * @param txtLleneDeNuevo TextView
     */
    private void mostrarTextoLlenarDeNuevo(final TextView txtLleneDeNuevo){
        txtLleneDeNuevo.setLayoutParams(new ActionBar.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        txtLleneDeNuevo.setText(R.string.txtLleneDeNuevo);
        txtLleneDeNuevo.setTextColor(getResources().getColor(R.color.negro));
        lyDatosInformante.addView(txtLleneDeNuevo);
    }

}
