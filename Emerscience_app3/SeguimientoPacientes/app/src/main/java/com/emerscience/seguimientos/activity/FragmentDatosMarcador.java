package com.emerscience.seguimientos.activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

import androidx.appcompat.app.AppCompatActivity;


public class FragmentDatosMarcador  extends AppCompatActivity implements GoogleMap.InfoWindowAdapter {

    private final View mWindow;
    private Context mContext;

    public FragmentDatosMarcador(Context context) {
        this.mContext = context;
        mWindow = LayoutInflater.from(context).inflate(R.layout.fragment_datos_marcador, null);
    }

    public FragmentDatosMarcador() {
        mWindow = LayoutInflater.from(mContext).inflate(R.layout.fragment_datos_marcador, null);
    }

    private void renderWindow(Marker marker, View view){
        String title = marker.getTitle();
        String [] descripcion = marker.getSnippet().split("\n");
        TextView nombres = view.findViewById(R.id.txtNombres);
        TextView apellidos = view.findViewById(R.id.txtApellidos);
        TextView cedula = view.findViewById(R.id.txtCedula);
        TextView celular = view.findViewById(R.id.txtCelular);
        TextView personasCovid = view.findViewById(R.id.txtCantidadEnfermos);
        if (descripcion.length > 0){
            nombres.setText(descripcion[0]);
            apellidos.setText(descripcion[1]);
            cedula.setText(descripcion[2]);
            celular.setText(descripcion[3]);
            personasCovid.setText(descripcion[5]);
        }
    }

    @Override
    public View getInfoWindow(Marker marker) {
        renderWindow(marker, mWindow);
        return mWindow;
    }

    @Override
    public View getInfoContents(Marker marker) {
        renderWindow(marker, mWindow);
        return mWindow;
    }
}
