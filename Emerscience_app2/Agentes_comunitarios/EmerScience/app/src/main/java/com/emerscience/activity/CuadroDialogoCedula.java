package com.emerscience.activity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.emerscience.pojos.Estudiante;
import com.emerscience.utils.AsyncTaskBucarInformante;
import com.emerscience.utils.Utileria;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Objects;

public class CuadroDialogoCedula extends DialogFragment {

    private Context context;

    public CuadroDialogoCedula(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        return crearDialogoCedula();
    }

    private AlertDialog crearDialogoCedula(){

        AlertDialog.Builder builder = new AlertDialog.Builder(Objects.requireNonNull(getActivity()));
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.cuadro_dialogo_cedula, null);
        builder.setView(view);
        builder.setCancelable(false);
        Button aceptar = view.findViewById(R.id.btnAceptar);
        Button cancelar = view.findViewById(R.id.btnCancelar);
        TextInputLayout txtInputCedula = view.findViewById(R.id.txtInputCedulaInformante);

        Objects.requireNonNull(txtInputCedula.getEditText()).addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                Utileria.validarCedula(txtInputCedula, true, null);
            }
        });

        aceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            @SuppressLint({"StaticFieldLeak"})
            public void onClick(View v) {
                if (!Utileria.validarCedula(txtInputCedula, true, null)){
                    Toast.makeText(getContext(), "Campo vacío o valor ingresado no válido", Toast.LENGTH_LONG).show();
                }else {
                    new AsyncTaskBucarInformante(getContext()){
                        @Override
                        protected void onPostExecute(Estudiante estudiante) {
                            if (null != estudiante){
                                Toast.makeText(getContext(),"Búsqueda exitosa", Toast.LENGTH_SHORT).show();
                                MenuActivity.estudiante = estudiante;
                                guardarUsuarioObtenido(estudiante, context);
                                System.out.println("INFORMANTE: " + estudiante.getSexo() + " " + estudiante.getCedula());
                                Intent menuEditar = new Intent(getContext(), MenuEditarActivity.class);
                                menuEditar.putExtra("editar", true);
                                startActivity(menuEditar);
                                dismiss();
                            }else {
                                Toast.makeText(getContext(), "No se encontró usuario", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }.execute(Objects.requireNonNull(txtInputCedula.getEditText()).getText().toString().trim(), MainActivity.usuario);
                    Toast.makeText(getContext(),"Buscando usuario usuario", Toast.LENGTH_SHORT).show();
                }
            }
        });

        cancelar.setOnClickListener(v -> {
            Toast.makeText(getContext(),"Búsqueda Cancelada", Toast.LENGTH_SHORT).show();
            dismiss();
        });
        return builder.create();
    }

    private void guardarUsuarioObtenido(Estudiante estudiante, Context context){
        SharedPreferences preferences = context.getSharedPreferences("UsuarioObtenido", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("apellidos", estudiante.getApellidos());
        editor.putString("nombres", estudiante.getNombres());
        editor.putString("cedula", estudiante.getCedula());
        editor.putString("correo", estudiante.getCorreo());
        editor.commit();
    }
}
