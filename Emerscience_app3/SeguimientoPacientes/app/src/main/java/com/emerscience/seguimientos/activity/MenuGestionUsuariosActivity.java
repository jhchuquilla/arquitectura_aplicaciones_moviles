package com.emerscience.seguimientos.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.emerscience.seguimientos.pojos.Usuario;
import com.emerscience.seguimientos.retrofit.UsuarioApiAdapter;
import com.emerscience.seguimientos.utils.Utileria;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MenuGestionUsuariosActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_gestion_usuarios);
    }

    public void crearUsuario(View v){
        Intent crearUsuarioActivity = new Intent(getApplicationContext(), CrearUsuarioActivity.class);
        startActivity(crearUsuarioActivity);
        finish();
    }

    public void editarUsuarios(View v){
        Intent listaUsuariosActivity = new Intent(getApplicationContext(), ListaUsuariosActivity.class);
        obtenerListaUsuarios(listaUsuariosActivity);
    }

    public void obtenerListaUsuarios(Intent listaUsuariosActivity){
        Call<List<Usuario>> callUsuarios = UsuarioApiAdapter.getUsuarioApiService().obtenerUsuarios(Utileria.obtenerToken(getApplicationContext()));
        callUsuarios.enqueue(new Callback<List<Usuario>>() {
            @Override
            public void onResponse(Call<List<Usuario>> call, Response<List<Usuario>> response) {
                if (response.isSuccessful()){
                    List<Usuario> listaUsuarios = response.body();
                    if (null != listaUsuarios){
                        Log.i("obtListUs", "Usuarios encontrados: " + listaUsuarios.size());
                        Bundle bund = new Bundle();
                        ArrayList<Usuario> arrayListUsuarios = new ArrayList<>(listaUsuarios);
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

    @Override
    public void onBackPressed() {
        Intent menuActivity = new Intent(getApplicationContext(), MenuActivity.class);
        startActivity(menuActivity);
        finish();
    }
}
