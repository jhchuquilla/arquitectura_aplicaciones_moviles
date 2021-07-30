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
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.MotionEvent;
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


public class DatosPersonalesActivity extends AppCompatActivity implements Callback<List<String>> {

    static public Estudiante estudiante;
    public static List<String> cedulasEstudiantes;

    private LinearLayout lyDatosInformante;
    private RadioGroup rbgSexo, rbgSintomas;
    private RadioButton rbMasculino, rbFemenino, rbSintomasSi, rbSintomasNo;
    private TextInputLayout txtInputApellidos, txtInputNombres, txtInputCedula, txtInputEdad,
            txtInputCelular, txtInputFijo, txtInputCorreo, txtInputPersonas;

    TextView latitud, longitud;
    private Localizacion loc;
    private LocationManager locationManager;

    //Botones y selecciones generadas por codigo
    private RadioGroup rbgCont, rbgUsted;
    private RadioButton rbContSi, rbContNo, rbSiUsted, rbNoUsted;
    private EditText etNumPersonasSintomas;

    boolean presionoSintomasNo = false;
    boolean presionoSintomasSi = false;
    boolean presionoContactoSi = false;
    boolean presionoContactoNo = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_datos_personales);

        latitud = findViewById(R.id.tvLatitud);
        longitud = findViewById(R.id.tvLongitud);

        getSupportActionBar().setDisplayOptions(androidx.appcompat.app.ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.toolbar_form_estudiante);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION,}
                    , 1000);
        } else {
            locationStart();
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
        rbMasculino = findViewById(R.id.rbMaculino);
        rbFemenino = findViewById(R.id.rbFemenino);
        rbSintomasSi = findViewById(R.id.rbSintomasSi);
        rbSintomasNo = findViewById(R.id.rbSintomasNo);

        txtInputApellidos = findViewById(R.id.txtInputApellidos);
        txtInputNombres = findViewById(R.id.txtInputNombres);
        txtInputCedula = findViewById(R.id.txtInputCedula);
        txtInputEdad = findViewById(R.id.txtInputEdad);
        txtInputCelular = findViewById(R.id.txtInputCelular);
        txtInputFijo = findViewById(R.id.txtInputTelefono);
        txtInputCorreo = findViewById(R.id.txtInputCorreo);
        txtInputPersonas = findViewById(R.id.txtInputPersonas);

        cargarDatosUsuarioLdap();

        Call<List<String>> call = EstudianteApiAdapter.getApiService().obtenerCedulasEStudiantes(getSharedPreferences("token", MODE_PRIVATE).getString("token",""));
        call.enqueue(this);

        //System.out.println(cedulasEstudiantes.size());

         //Evento que permite ocultar el teclado virtual cuando se presione cualquier parte de la
         //pantalla menos los edit texts
        lyDatosInformante.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (getCurrentFocus() != null) {
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.
                            INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                    getWindow().getDecorView().clearFocus();
                }
                return false;
            }
        });

        //Validacion de campo apellidos en tiempo de escritura
        txtInputApellidos.getEditText().addTextChangedListener(new TextWatcher() {
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
        txtInputNombres.getEditText().addTextChangedListener(new TextWatcher() {
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
        txtInputCedula.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                Utileria.validarCedula(txtInputCedula);
            }
        });

        //Validacion de campo edad en tiempo de escritura
        txtInputEdad.getEditText().addTextChangedListener(new TextWatcher() {
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
        txtInputCelular.getEditText().addTextChangedListener(new TextWatcher() {
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
        txtInputFijo.getEditText().addTextChangedListener(new TextWatcher() {
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

        //Permite mostrar las preguntas segun seleccion del usuario si tiene o no sintomas COVID-19
        rbgSintomas.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
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

                        txtEsUstedPersonaConSintomas.setLayoutParams(new ActionBar.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                        txtEsUstedPersonaConSintomas.setText(R.string.txtEsUstedPersonaConSintomas);
                        txtEsUstedPersonaConSintomas.setTextColor(getResources().getColor(R.color.negro));
                        lyDatosInformante.addView(txtEsUstedPersonaConSintomas);

                        rbgEsUsted.setLayoutParams(new ActionBar.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                        rbgEsUsted.setOrientation(LinearLayout.HORIZONTAL);
//                        rbgEsUsted.setGravity(View.TEXT_ALIGNMENT_CENTER);
                        rbgUsted = rbgEsUsted;
                        lyDatosInformante.addView(rbgEsUsted);

                        rbSiEsUsted.setLayoutParams(new ActionBar.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                        rbNoEsUsted.setLayoutParams(new ActionBar.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                        rbSiEsUsted.setText("Si");
                        rbNoEsUsted.setText("No");
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

                        presionoSintomasNo = false;
                        presionoSintomasSi = true;
                    } else {

                        txtEsUstedPersonaConSintomas.setLayoutParams(new ActionBar.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                        txtEsUstedPersonaConSintomas.setText(R.string.txtEsUstedPersonaConSintomas);
                        txtEsUstedPersonaConSintomas.setTextColor(getResources().getColor(R.color.negro));
                        lyDatosInformante.addView(txtEsUstedPersonaConSintomas);

                        rbgEsUsted.setLayoutParams(new ActionBar.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                        rbgEsUsted.setOrientation(LinearLayout.HORIZONTAL);
//                        rbgEsUsted.setGravity(View.TEXT_ALIGNMENT_CENTER);
                        lyDatosInformante.addView(rbgEsUsted);
                        rbgUsted = rbgEsUsted;

                        rbSiEsUsted.setLayoutParams(new ActionBar.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                        rbNoEsUsted.setLayoutParams(new ActionBar.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                        rbSiEsUsted.setText("Si");
                        rbNoEsUsted.setText("No");
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

                        txtContacto.setLayoutParams(new ActionBar.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                        txtContacto.setText(R.string.txtContacto);
                        txtContacto.setTextColor(getResources().getColor(R.color.negro));
                        lyDatosInformante.addView(txtContacto);

                        rbgContacto.setLayoutParams(new ActionBar.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                        rbgContacto.setOrientation(LinearLayout.HORIZONTAL);
//                        rbgContacto.setGravity(View.TEXT_ALIGNMENT_CENTER);
                        rbgCont = rbgContacto;
                        lyDatosInformante.addView(rbgContacto);

                        rbContactoSi.setLayoutParams(new ActionBar.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                        rbContactoNo.setLayoutParams(new ActionBar.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                        rbContactoSi.setText("Si");
                        rbContactoNo.setText("No");
                        rbContactoSi.setTextColor(getResources().getColor(R.color.negro));
                        rbContactoNo.setTextColor(getResources().getColor(R.color.negro));
                        rbContSi = rbContactoSi;
                        rbContNo = rbContactoNo;
                        rbgContacto.addView(rbContactoSi);
                        rbgContacto.addView(rbContactoNo);
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

                        txtContacto.setLayoutParams(new ActionBar.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                        txtContacto.setText(R.string.txtContacto);
                        txtContacto.setTextColor(getResources().getColor(R.color.negro));
                        lyDatosInformante.addView(txtContacto);

                        rbgContacto.setLayoutParams(new ActionBar.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                        rbgContacto.setOrientation(LinearLayout.HORIZONTAL);
//                        rbgContacto.setGravity(View.TEXT_ALIGNMENT_CENTER);
                        rbgCont = rbgContacto;
                        lyDatosInformante.addView(rbgContacto);

                        rbContactoSi.setLayoutParams(new ActionBar.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                        rbContactoNo.setLayoutParams(new ActionBar.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                        rbContactoSi.setText("Si");
                        rbContactoNo.setText("No");
                        rbContactoSi.setTextColor(getResources().getColor(R.color.negro));
                        rbContactoNo.setTextColor(getResources().getColor(R.color.negro));
                        rbContSi = rbContactoSi;
                        rbContNo = rbContactoNo;
                        rbgContacto.addView(rbContactoSi);
                        rbgContacto.addView(rbContactoNo);
                        presionoSintomasNo = true;
                        presionoSintomasSi = false;
                    }

                }
            }
        });

        //Permite indicar advertencias de si el usuario debe guardar cuarentena o a su vez si no tiene
        //sintomas el y su nucleo familiar se le indica que puede volver a llenar informacion si
        //alguien tuviese COVID-19
        rbgContacto.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == rbContactoSi.getId() && !presionoContactoSi) {
                    if (presionoContactoNo) {
                        lyDatosInformante.removeView(txtLleneDeNuevo);

                        txtCuarentena.setLayoutParams(new ActionBar.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                        txtCuarentena.setText(R.string.txtCuarentena);
                        txtCuarentena.setTextColor(getResources().getColor(R.color.negro));
                        lyDatosInformante.addView(txtCuarentena);
                        presionoContactoSi = true;
                        presionoContactoNo = false;
                    } else {
                        txtCuarentena.setLayoutParams(new ActionBar.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                        txtCuarentena.setText(R.string.txtCuarentena);
                        txtCuarentena.setTextColor(getResources().getColor(R.color.negro));
                        lyDatosInformante.addView(txtCuarentena);
                        presionoContactoSi = true;
                        presionoContactoNo = false;
                    }
                } else if (checkedId == rbContactoNo.getId() && !presionoContactoNo) {
                    if (presionoContactoSi) {
                        lyDatosInformante.removeView(txtCuarentena);

                        txtLleneDeNuevo.setLayoutParams(new ActionBar.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                        txtLleneDeNuevo.setText(R.string.txtLleneDeNuevo);
                        txtLleneDeNuevo.setTextColor(getResources().getColor(R.color.negro));
                        lyDatosInformante.addView(txtLleneDeNuevo);
                        presionoContactoNo = true;
                        presionoContactoSi = false;
                    } else {
                        txtLleneDeNuevo.setLayoutParams(new ActionBar.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                        txtLleneDeNuevo.setText(R.string.txtLleneDeNuevo);
                        txtLleneDeNuevo.setTextColor(getResources().getColor(R.color.negro));
                        lyDatosInformante.addView(txtLleneDeNuevo);
                        presionoContactoNo = true;
                        presionoContactoSi = false;
                    }
                }
            }
        });

    } //fin metodo OnCreate

    //-----------------------------------------VALIDACIONES DE CAMPOS PROPIOS DE FORMULARIO----------------------------------------------------

    public boolean validarNumeroPersonas() {
        String cantidadPersonas = txtInputPersonas.getEditText().getText().toString().trim();
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
                    > (Integer.parseInt(txtInputPersonas.getEditText().getText().toString().trim()))) {
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
    //'------------------------------------------FIN VALIDACIONES---------------------------------------------------------------------

    /**
     * Accion de boton siguiente
     *
     * @param view Vista donde se estará escuchando el evento click
     */
    public void GuardarDatosPersonales(View view) {
        if (!Utileria.validarApellidos(txtInputApellidos) | !Utileria.validarNombres(txtInputNombres)
                | !Utileria.validarCedula(txtInputCedula) | !Utileria.validarEdad(txtInputEdad)
                | !Utileria.validarSexo(rbgSexo, rbFemenino) | !Utileria.validarCelular(txtInputCelular)
                | !Utileria.validarFijo(txtInputFijo) | !Utileria.validarCorreo(txtInputCorreo)
                | !validarNumeroPersonas() | !validarSintomas() | !validarCantidadPersonasSintomas()
                | !validarContactoCovid() | !validarEsUsted()) {
            Toast.makeText(getApplicationContext(), "Lo sentimos, algunos campos están vacíos" +
                    " o no son válidos, revíselos por favor", Toast.LENGTH_LONG).show();
        } else {
            //recoger datos
            String apellidos = Utileria.cleanString(txtInputApellidos.getEditText().getText().toString().trim()).toUpperCase();
            String nombres = Utileria.cleanString(txtInputNombres.getEditText().getText().toString().trim()).toUpperCase();
            String cedula = txtInputCedula.getEditText().getText().toString().trim();
            int edad = Integer.parseInt(txtInputEdad.getEditText().getText().toString().trim());
            String sexo = "";
            if (rbMasculino.isChecked()) {
                sexo = rbMasculino.getText().toString().trim().toUpperCase();
            } else if (rbFemenino.isChecked()) {
                sexo = rbFemenino.getText().toString().trim().toUpperCase();
            }
            String celular = txtInputCelular.getEditText().getText().toString().trim();
            String fijo = "";
            if (!(txtInputFijo.getEditText().getText().toString().trim()).isEmpty()) {
                fijo = txtInputFijo.getEditText().getText().toString().trim();
            }
            String correo = Utileria.cleanString(txtInputCorreo.getEditText().getText().toString().trim());
            int numeroPersonasHogar = Integer.parseInt(txtInputPersonas.getEditText().getText().toString().trim());
            String alguienPresentaSintomas = "";
            if (rbSintomasSi.isChecked()) {
                alguienPresentaSintomas = rbSintomasSi.getText().toString().trim().toUpperCase();
            } else if (rbSintomasNo.isChecked()) {
                alguienPresentaSintomas = rbSintomasNo.getText().toString().trim().toUpperCase();
            }
            int cantidadPersonasSintomas = 0;
            String contactoPersonasCovid = "";
            String estudiantePresentaSintomas = "";

            String lat = "";
            String lng = "";

            lat = latitud.getText().toString().trim();
            lng = longitud.getText().toString().trim();
            System.out.println("LATITUD: " + lat);
            System.out.println("LONGITUD: " + lng);

            Date fecha = new Date();
            SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String fechaConFormato = formato.format(fecha);

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
                estudiante = new Estudiante(fechaConFormato, lng, lat, apellidos, nombres, cedula, edad, sexo, celular, fijo, correo, numeroPersonasHogar, alguienPresentaSintomas, estudiantePresentaSintomas, cantidadPersonasSintomas, contactoPersonasCovid, usuario);


                Toast.makeText(getApplicationContext(), "Datos guardados con éxito" +
                        " proceda a llenar siguiente formulario", Toast.LENGTH_LONG).show();

                locationManager.removeUpdates(loc);
                Intent ticketEstudiante = new Intent(getApplicationContext(), TicketEstudianteActivity.class);
                ticketEstudiante.putExtra("cantidadPersonasSintomas", etNumPersonasSintomas.getText().toString().trim());
                ticketEstudiante.putExtra("estudiantePresentaSintomas", estudiantePresentaSintomas);
                startActivity(ticketEstudiante);
                finish();
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

                //Guarda Datos Persona que no tiene ningún familiar con síntomas Firebase
                estudiante = new Estudiante(fechaConFormato, lng, lat, apellidos, nombres, cedula, edad, sexo, celular, fijo, correo, numeroPersonasHogar, alguienPresentaSintomas, estudiantePresentaSintomas, cantidadPersonasSintomas, contactoPersonasCovid, usuario);
                datosPersona.push().setValue(estudiante);
                generarRegistro();
                new AsyncTaskInsertarEstudiante(getApplicationContext()).execute(estudiante);
                System.out.println("RESPUESTA SERVICIO ESTUDIANTE: " + AsyncTaskInsertarEstudiante.resultado);
                Toast.makeText(getApplicationContext(), "Datos guardados con éxito, " +
                        "gracias por su ayuda", Toast.LENGTH_LONG).show();

                locationManager.removeUpdates(loc);
                Intent menu = new Intent(getApplicationContext(), MenuActivity.class);
                menu.putExtra("desactivaNuevoForm", true);
                startActivity(menu);
                finish();
            }
        }
    }

    /**
     * Permite escuchar cuando el boton de accion de la toolbar es pulsado para permitir
     * regresar al menu principal
     *
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                locationManager.removeUpdates(loc);
                Intent menu = new Intent(getApplicationContext(), MenuActivity.class);
                startActivity(menu);
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Accion de boton regresar del sistema
     */
    @Override
    public void onBackPressed() {
        locationManager.removeUpdates(loc);
        Intent menu = new Intent(getApplicationContext(), MenuActivity.class);
        startActivity(menu);
        finish();
    }

    //Metodos para obtener ubicacion geográfica de informante
    private void locationStart() {
        LocationManager mlocManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Localizacion local = new Localizacion();
        locationManager = mlocManager;
        loc = local;
        local.setDatosPersonalesActivity(this, latitud, longitud);
        final boolean gpsEnabled = mlocManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        if (!gpsEnabled) {
            Intent settingsIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivity(settingsIntent);
        }
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION,}, 1000);
            return;
        }
        mlocManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 10000, 0, (LocationListener) local);
        mlocManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 10000, 0, (LocationListener) local);
        latitud.setText("Localización agregada");
        longitud.setText("");
    }

    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == 1000) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                locationStart();
                return;
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
            }
        }
    }

    /**
     * Permite la carga de datos del usuario que ingreso a la app mediante conexion a servidor LDAP
     */
    public void cargarDatosUsuarioLdap(){
        SharedPreferences preferences = getSharedPreferences("UsuarioObtenido", MODE_PRIVATE);
        txtInputApellidos.getEditText().setText(preferences.getString("apellidos", ""));
        txtInputNombres.getEditText().setText(preferences.getString("nombres", ""));
        txtInputCedula.getEditText().setText(preferences.getString("cedula", ""));
        txtInputCorreo.getEditText().setText(preferences.getString("correo",""));
    }

    @Override
    public void onResponse(Call<List<String>> call, Response<List<String>> response) {
        if (response.isSuccessful()){
            List<String> lista = response.body();
            actualizarListaCedulasEstudiantes(lista);
            cedulasEstudiantes = lista;
            System.out.println("TAMANO: " + cedulasEstudiantes.size());
        }
    }

    @Override
    public void onFailure(Call<List<String>> call, Throwable t) {

    }

    public void actualizarListaCedulasEstudiantes(List<String> listaCedulas){
        cedulasEstudiantes = new ArrayList<String>();
        cedulasEstudiantes = listaCedulas;
        Utileria.validarCedula(txtInputCedula);
    }
}
