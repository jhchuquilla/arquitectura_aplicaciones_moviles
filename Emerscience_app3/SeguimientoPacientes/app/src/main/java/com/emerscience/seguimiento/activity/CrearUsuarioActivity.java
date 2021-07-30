package com.emerscience.seguimiento.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.DragEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.emerscience.seguimiento.pojos.Usuario;
import com.emerscience.seguimiento.retrofit.UsuarioApiAdapter;
import com.emerscience.seguimiento.utils.Utileria;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CrearUsuarioActivity extends AppCompatActivity {

    private TextInputLayout txtInputUsuario, txtInputContrasena;
    private TextView txtTituloFormularioUsuario, txtAvisoContrasena;
    private Spinner spinnerRolUsuario;
    private Switch swUsuarioActivo, swTipoUsuario;
    private Button btnAgregar;
    private String rolSeleccionado;
    private boolean esUsuarioLdap = false;
    private boolean usuarioActivo = true;
    private boolean editar = false;
    private Usuario usuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_usuario);

        Bundle bundle = this.getIntent().getBundleExtra("bundArrListUsuarios");
        if (bundle != null){
            usuario = (Usuario) bundle.getSerializable("Usuario");
        }else {
            usuario = new Usuario();
        }

        Bundle parametros = this.getIntent().getExtras();
        if (parametros != null){
            editar = parametros.getBoolean("editar", false);
        }

        txtAvisoContrasena = findViewById(R.id.txtAvisoContrasena);
        txtTituloFormularioUsuario = findViewById(R.id.txtTituloFormularioUsuario);
        txtInputUsuario = findViewById(R.id.txtInputUsuario);
        txtInputContrasena = findViewById(R.id.txtInputContrasena);
        spinnerRolUsuario = findViewById(R.id.spinnerRol);
        swUsuarioActivo = findViewById(R.id.switchUsuarioActivo);
        swTipoUsuario = findViewById(R.id.switchTipoUsuario);
        btnAgregar = findViewById(R.id.btnAgregar);

        ArrayAdapter<CharSequence> adapterRol = ArrayAdapter.createFromResource(getApplicationContext(),
                R.array.rol_usuario, android.R.layout.simple_spinner_item);
        spinnerRolUsuario.setAdapter(adapterRol);

        if (editar){
            txtTituloFormularioUsuario.setText("EDITANDO USUARIO");
            txtInputUsuario.setEnabled(false);
            btnAgregar.setText("ACTUALIZAR");
            txtInputUsuario.getEditText().setText(usuario.getUsername());
            switch (usuario.getRol()){
                case "ROLE_SUPERVISOR":
                    spinnerRolUsuario.setSelection(1);
                    break;
                case "ROLE_VIGILANTE":
                    spinnerRolUsuario.setSelection(2);
                    break;
                default:
                    spinnerRolUsuario.setSelection(0);
            }
            swUsuarioActivo.setChecked(usuario.isUsuarioActivo());
            usuarioActivo = usuario.isUsuarioActivo();
            swTipoUsuario.setChecked(usuario.isUsuaioLdap());
            esUsuarioLdap = usuario.isUsuaioLdap();

            if (esUsuarioLdap) {
                txtAvisoContrasena.setVisibility(View.VISIBLE);
            } else {
                txtAvisoContrasena.setVisibility(View.INVISIBLE);
            }

            swTipoUsuario.setEnabled(false);
            if (esUsuarioLdap){
                txtInputContrasena.setEnabled(false);
            }
        }else{
            btnAgregar.setText("CREAR");
        }

        spinnerRolUsuario.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (!parent.getItemAtPosition(position).toString().equalsIgnoreCase("Seleccione un Rol")){
                    rolSeleccionado = parent.getItemAtPosition(position).toString();
//                    Toast.makeText(parent.getContext(), "Rol: " + rolSeleccionado, Toast.LENGTH_SHORT).show();
                }else {
                    rolSeleccionado = parent.getItemAtPosition(position).toString();
//                    Toast.makeText(parent.getContext(), "Debe Seleccionar un rol", Toast.LENGTH_SHORT).show();
                }
                Log.i("ROL SEL", rolSeleccionado);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        swUsuarioActivo.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (swUsuarioActivo.isChecked()){
                    usuarioActivo = true;
                    Log.i("Usuario Activo", String.valueOf(usuarioActivo));
                }else{
                    usuarioActivo = false;
                    Log.i("Usuario Activo", String.valueOf(usuarioActivo));
                }
            }
        });

        swTipoUsuario.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (swTipoUsuario.isChecked()){
                    txtInputContrasena.setEnabled(false);
                    Objects.requireNonNull(txtInputContrasena.getEditText()).setText("");
                    esUsuarioLdap = true;
                    txtAvisoContrasena.setVisibility(View.VISIBLE);

                    Log.i("Usuario LDAP", String.valueOf(esUsuarioLdap));
                }else{
                    txtInputContrasena.setEnabled(true);
                    esUsuarioLdap = false;
                    txtAvisoContrasena.setVisibility(View.INVISIBLE);
                    Log.i("Usuario LDAP", String.valueOf(esUsuarioLdap));
                }
            }
        });

    }// FIN MÉTODO ONCREATE()

    public void onClickSwitches(View v){
        if (v.getId() == R.id.switchUsuarioActivo){
            if (swUsuarioActivo.isChecked()){
                usuarioActivo = true;
                Log.i("Usuario Activo", String.valueOf(usuarioActivo));
            }else{
                usuarioActivo = false;
                Log.i("Usuario Activo", String.valueOf(usuarioActivo));
            }
        }else if (v.getId() == R.id.switchTipoUsuario){
            if (swTipoUsuario.isChecked()){
                txtInputContrasena.setEnabled(false);
                Objects.requireNonNull(txtInputContrasena.getEditText()).setText("");
                esUsuarioLdap = true;
                txtAvisoContrasena.setVisibility(View.VISIBLE);
                Log.i("Usuario LDAP", String.valueOf(esUsuarioLdap));
            }else{
                txtInputContrasena.setEnabled(true);
                esUsuarioLdap = false;
                txtAvisoContrasena.setVisibility(View.INVISIBLE);
                Log.i("Usuario LDAP", String.valueOf(esUsuarioLdap));
            }
        }
    }

    public void onClickcrearUsuario(View v){
        if (!Utileria.validarUsuario(txtInputUsuario) | rolSeleccionado.equalsIgnoreCase("Seleccione un Rol")){
            Toast.makeText(getApplicationContext(), "Algunos campos no han sido llenados o no son válidos", Toast.LENGTH_SHORT).show();
        }else{
            if (esUsuarioLdap){
                String nickName = Objects.requireNonNull(txtInputUsuario.getEditText()).getText().toString().trim();
                if (rolSeleccionado.equalsIgnoreCase("ROL_SUPERVISOR")){
                    rolSeleccionado = "ROLE_SUPERVISOR";
                }else if (rolSeleccionado.equalsIgnoreCase("ROL_VIGILANTE")){
                    rolSeleccionado = "ROLE_VIGILANTE";
                }

                Usuario usuarioNuevoLdap = new Usuario(nickName, rolSeleccionado, esUsuarioLdap, usuarioActivo);
                if (editar){
                    usuarioNuevoLdap.setId(usuario.getId());
                    usuarioNuevoLdap.setPassword("");
                    //llamar api de actualizacion de datos
                    servicioActualizarUsuario(usuarioNuevoLdap);
                }else{
                    servicioCrearUsuarioRetrofit(usuarioNuevoLdap);
                }
            }else{
                if (!Utileria.validarContraseña(txtInputContrasena, editar)){
                    Toast.makeText(getApplicationContext(), "Algunos campos no han sido llenados o no son válidos", Toast.LENGTH_SHORT).show();
                }else{
                    String nickName = Objects.requireNonNull(txtInputUsuario.getEditText()).getText().toString().trim();
                    String contrasena = Objects.requireNonNull(txtInputContrasena.getEditText()).getText().toString().trim();
                    if (rolSeleccionado.equalsIgnoreCase("ROL_SUPERVISOR")){
                        rolSeleccionado = "ROLE_SUPERVISOR";
                    }else if (rolSeleccionado.equalsIgnoreCase("ROL_VIGILANTE")){
                        rolSeleccionado = "ROLE_VIGILANTE";
                    }

                    Usuario usuarioNuevo = new Usuario(nickName, contrasena, rolSeleccionado, esUsuarioLdap, usuarioActivo);
                    if (editar){
                        usuarioNuevo.setId(usuario.getId());
                        servicioActualizarUsuario(usuarioNuevo);
                    }else {
                        servicioCrearUsuarioRetrofit(usuarioNuevo);
                    }
                }

                /*
                if (editar){
                    String nickName = Objects.requireNonNull(txtInputUsuario.getEditText()).getText().toString().trim();
                    String contrasena = Objects.requireNonNull(txtInputContrasena.getEditText()).getText().toString().trim();
                    Log.i("CONTRASEÑA", contrasena);
                    if (rolSeleccionado.equalsIgnoreCase("ROL_SUPERVISOR")){
                        rolSeleccionado = "ROLE_SUPERVISOR";
                    }else if (rolSeleccionado.equalsIgnoreCase("ROL_VIGILANTE")){
                        rolSeleccionado = "ROLE_VIGILANTE";
                    }

                    Usuario usuarioNuevo = new Usuario(nickName, contrasena, rolSeleccionado, esUsuarioLdap, usuarioActivo);
                    usuarioNuevo.setId(usuario.getId());
                    //llamar api de actualizacion de datos
                    servicioActualizarUsuario(usuarioNuevo);
                }else{
                    if (!Utileria.validarContraseña(txtInputContrasena)){
                        Toast.makeText(getApplicationContext(), "Algunos campos no han sido llenados o no son válidos", Toast.LENGTH_SHORT).show();
                    }else{
                        String nickName = Objects.requireNonNull(txtInputUsuario.getEditText()).getText().toString().trim();
                        String contrasena = Objects.requireNonNull(txtInputContrasena.getEditText()).getText().toString().trim();
                        if (rolSeleccionado.equalsIgnoreCase("ROL_SUPERVISOR")){
                            rolSeleccionado = "ROLE_SUPERVISOR";
                        }else if (rolSeleccionado.equalsIgnoreCase("ROL_VIGILANTE")){
                            rolSeleccionado = "ROLE_VIGILANTE";
                        }

                        Usuario usuarioNuevo = new Usuario(nickName, contrasena, rolSeleccionado, esUsuarioLdap, usuarioActivo);
                        servicioCrearUsuarioRetrofit(usuarioNuevo);
                    }
                }
                */
            }
        }
    }

    public void servicioCrearUsuarioRetrofit(Usuario usuario){
        Call<Boolean> callCrearUsuario = UsuarioApiAdapter.getUsuarioApiService().crearUsuario(usuario,
                Utileria.obtenerToken(getApplicationContext()));
        callCrearUsuario.enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                if (response.isSuccessful()){
                    boolean usuarioCreado = false;
                    usuarioCreado = response.body();
                    if (usuarioCreado){
                        Toast.makeText(getApplicationContext(), "Se creo usuario", Toast.LENGTH_SHORT).show();
                        txtInputUsuario.getEditText().setText("");
                        txtInputContrasena.getEditText().setText("");
                        swUsuarioActivo.setChecked(true);
                        swTipoUsuario.setChecked(false);
                        spinnerRolUsuario.setSelection(0);
                    }else{
                        Toast.makeText(getApplicationContext(), "No se pudo crear usuario, usuario existente en base de datos", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Log.i("RESPONSE", response.message());
                    Log.i("RESP ERROR" ,response.errorBody().toString());
                    Log.i("HTTP CODE", String.valueOf(response.code()));
                    Toast.makeText(getApplicationContext(), "No se pudo crear usuario" , Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Boolean> call, Throwable t) {

            }
        });
    }

    public void servicioActualizarUsuario(Usuario usuario){
        Call<Boolean> callActualizar = UsuarioApiAdapter.getUsuarioApiService().actualizarUsuario(usuario, Utileria.obtenerToken(getApplicationContext()));
        callActualizar.enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                if (response.isSuccessful()){
                    boolean usuarioActualizado = response.body();
                    if (usuarioActualizado){
                        Toast.makeText(getApplicationContext(), "Se actualizó usuario", Toast.LENGTH_SHORT).show();
                        txtInputUsuario.getEditText().setText("");
                        txtInputContrasena.getEditText().setText("");
                        swUsuarioActivo.setChecked(true);
                        swTipoUsuario.setChecked(false);
                        spinnerRolUsuario.setSelection(0);
                        //llamamos api para obtener lista de usuarios actualizada y mostrarla al usuario
                        servicioObtenerUsuarios();
                    }else{
                        Toast.makeText(getApplicationContext(), "No se pudo actualizar usuario , información no válida", Toast.LENGTH_SHORT).show();
                    }
                }else {
                    Log.i("RESPONSE", response.message());
                    Log.i("RESP ERROR" ,response.errorBody().toString());
                    Log.i("HTTP CODE", String.valueOf(response.code()));
                    Toast.makeText(getApplicationContext(), "No se pudo actualizar usuario", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Boolean> call, Throwable t) {

            }
        });
    }

    public void servicioObtenerUsuarios(){
        Call<List<Usuario>> callListaUsuarios = UsuarioApiAdapter.getUsuarioApiService().obtenerUsuarios(Utileria.obtenerToken(getApplicationContext()));
        callListaUsuarios.enqueue(new Callback<List<Usuario>>() {
            @Override
            public void onResponse(Call<List<Usuario>> call, Response<List<Usuario>> response) {
                if (response.isSuccessful()){
                    List<Usuario> listaUsuarios = response.body();
                    if (null != listaUsuarios){
                        Log.i("obtListUs", "Usuarios encontrados: " + listaUsuarios.size());
                        Bundle bund = new Bundle();
                        ArrayList<Usuario> arrayListUsuarios = new ArrayList<>(listaUsuarios);
                        Intent listaUsuariosActivity = new Intent(getApplicationContext(), ListaUsuariosActivity.class);
                        bund.putSerializable("arrListUsuarios",arrayListUsuarios);
                        listaUsuariosActivity.putExtra("bundArrListUsuarios", bund);
                        startActivity(listaUsuariosActivity);
                        finish();
                    }
                }else{
                    Log.i("RESPONSE", response.message());
                    Log.i("RESP ERROR" ,response.errorBody().toString());
                    Log.i("HTTP CODE", String.valueOf(response.code()));
                    Toast.makeText(getApplicationContext(), "No se pudo obtener lista de usuarios, intente más tarde", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Usuario>> call, Throwable t) {

            }
        });
    }

    public void cancelar(View v){
        if (editar){
            servicioObtenerUsuarios();
        }else{
            Intent menuGestionUsuariosActivity = new Intent(getApplicationContext(), MenuGestionUsuariosActivity.class);
            startActivity(menuGestionUsuariosActivity);
            finish();
        }
    }

    @Override
    public void onBackPressed() {
        if (editar){
            servicioObtenerUsuarios();
        }else{
            Intent menuGestionUsuariosActivity = new Intent(getApplicationContext(), MenuGestionUsuariosActivity.class);
            startActivity(menuGestionUsuariosActivity);
            finish();
        }
    }
}
