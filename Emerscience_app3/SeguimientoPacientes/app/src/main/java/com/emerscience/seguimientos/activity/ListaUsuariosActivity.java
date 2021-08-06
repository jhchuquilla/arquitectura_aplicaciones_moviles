package com.emerscience.seguimientos.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;

import com.emerscience.seguimientos.adaptadores.AdaptadorUsuarios;
import com.emerscience.seguimientos.pojos.Usuario;

import java.util.ArrayList;
import java.util.List;

public class ListaUsuariosActivity extends AppCompatActivity {

    private ArrayList<Usuario> arrayListUsuarios;
    private List<Usuario> listaUsuarios;
    private RecyclerView rvListaUsuarios;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_usuarios);

        Bundle bundle = this.getIntent().getBundleExtra("bundArrListUsuarios");
        if (bundle != null){
            arrayListUsuarios = (ArrayList<Usuario>) bundle.getSerializable("arrListUsuarios");
            listaUsuarios = new ArrayList<>(arrayListUsuarios);
        }else {
            arrayListUsuarios = new ArrayList<>();
            listaUsuarios = new ArrayList<>();
        }

        rvListaUsuarios = findViewById(R.id.rvListaUsuarios);
        rvListaUsuarios.setLayoutManager(new LinearLayoutManager(this));

        AdaptadorUsuarios adaptadorUsuarios = new AdaptadorUsuarios(listaUsuarios);
        rvListaUsuarios.setAdapter(adaptadorUsuarios);

        adaptadorUsuarios.setOnClickListener(v -> {
            Usuario usuario = new Usuario();
            usuario.setId(listaUsuarios.get(rvListaUsuarios.getChildAdapterPosition(v)).getId());
            usuario.setUsername(listaUsuarios.get(rvListaUsuarios.getChildAdapterPosition(v)).getUsername());
            usuario.setRol(listaUsuarios.get(rvListaUsuarios.getChildAdapterPosition(v)).getRol());
            usuario.setUsuaioLdap(listaUsuarios.get(rvListaUsuarios.getChildAdapterPosition(v)).isUsuaioLdap());
            usuario.setUsuarioActivo(listaUsuarios.get(rvListaUsuarios.getChildAdapterPosition(v)).isUsuarioActivo());

            Intent crearUsuarioActivity = new Intent(getApplicationContext(), CrearUsuarioActivity.class);
            Bundle bund = new Bundle();
            bund.putSerializable("arrListUsuarios", arrayListUsuarios);
            bund.putSerializable("Usuario", usuario);
            crearUsuarioActivity.putExtra("bundArrListUsuarios", bund);
            crearUsuarioActivity.putExtra("editar", true);
            startActivity(crearUsuarioActivity);
            finish();
        });

    }

    @Override
    public void onBackPressed() {
        Intent menuGestionUsuariosActivity = new Intent(getApplicationContext(), MenuGestionUsuariosActivity.class);
        startActivity(menuGestionUsuariosActivity);
        finish();
    }
}
