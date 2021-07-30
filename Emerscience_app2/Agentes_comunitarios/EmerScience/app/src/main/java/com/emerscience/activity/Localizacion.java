package com.emerscience.activity;

import android.location.Location;
import android.location.LocationListener;
import android.location.LocationProvider;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

public class Localizacion implements LocationListener {
    DatosPersonalesActivity datosPersonalesActivity;
    TextView latitud, longitud, direccion;
    public DatosPersonalesActivity getDatosPersonalesActivity() {
        return datosPersonalesActivity;
    }
    public void setDatosPersonalesActivity(DatosPersonalesActivity datosPersonalesActivity, TextView tvLatitud, TextView tvLongitud) {
        this.datosPersonalesActivity = datosPersonalesActivity;
        this.latitud = tvLatitud;
        this.longitud = tvLongitud;
    }
    @Override
    public void onLocationChanged(Location loc) {
        // Este metodo se ejecuta cada vez que el GPS recibe nuevas coordenadas
        // debido a la deteccion de un cambio de ubicacion
        loc.getLatitude();
        loc.getLongitude();
        String sLatitud = String.valueOf(loc.getLatitude());
        String sLongitud = String.valueOf(loc.getLongitude());
        latitud.setText(sLatitud);
        longitud.setText(sLongitud);
//        this.datosPersonalesActivity.setLocation(loc);
    }

    @Override
    public void onProviderDisabled(String provider) {
        // Este metodo se ejecuta cuando el GPS es desactivado

    }
    @Override
    public void onProviderEnabled(String provider) {
        // Este metodo se ejecuta cuando el GPS es activado

    }
    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        switch (status) {
            case LocationProvider.AVAILABLE:
                Log.d("debug", "LocationProvider.AVAILABLE");
                break;
            case LocationProvider.OUT_OF_SERVICE:
                Log.d("debug", "LocationProvider.OUT_OF_SERVICE");
                break;
            case LocationProvider.TEMPORARILY_UNAVAILABLE:
                Log.d("debug", "LocationProvider.TEMPORARILY_UNAVAILABLE");
                break;
        }
    }
}


