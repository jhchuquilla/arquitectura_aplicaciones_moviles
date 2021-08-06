package com.emerscience.seguimientos.activity;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.emerscience.seguimientos.asynctasks.AsyncTaskObtenerCoordenadas;
import com.emerscience.seguimientos.pojos.Coordenada;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnInfoWindowClickListener {

    private GoogleMap mMap;
    private Marker marcador;
    double lat = 0.0;
    double lng = 0.0;
    private ArrayList<Marker> tmpMarcadores = new ArrayList<>();
    private ArrayList<Marker> marcadores = new ArrayList<>();
    private boolean buscarCoord = false;
    private Coordenada coordenada;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        Bundle bundleCoord = this.getIntent().getBundleExtra("bundCoordenada");
        if (bundleCoord != null){
            coordenada = (Coordenada) bundleCoord.getSerializable("coordenada");
        }else{
            coordenada = new Coordenada();
        }

        Bundle parametros = this.getIntent().getExtras();
        if (null != parametros){
            buscarCoord = parametros.getBoolean("buscarCoord", false);
        }

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        mMap.setMyLocationEnabled(true);
        mMap.setOnInfoWindowClickListener(this);
        mMap.setInfoWindowAdapter(new FragmentDatosMarcador(MapsActivity.this));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(-0.176893, -78.481230), 8));
        if (!buscarCoord){
            dibujarCoordenadas();
        } else {
            //Llamar método que dibuja coordenada obtenida en la búsqueda
            dibujarCoordenada();
        }
    }

    private void agregarMarcador(double lat, double lng) {
        LatLng coordenadas = new LatLng(lat, lng);
        CameraUpdate miUbicacion = CameraUpdateFactory.newLatLngZoom(coordenadas, 16);
        if (marcador != null) {
            marcador.remove();
        }
        marcador = mMap.addMarker(new MarkerOptions().position(coordenadas).title("Ubicacion actual")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));
        mMap.animateCamera(miUbicacion);
        mMap.getUiSettings().setZoomControlsEnabled(true);
    }

    private void actualizarUbicacion(Location location) {
        if (location != null) {
            lat = location.getLatitude();
            lng = location.getLongitude();
            agregarMarcador(lat, lng);
        }
    }

    LocationListener locListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            actualizarUbicacion(location);
            Toast.makeText(getApplicationContext(), "Ubicacion actualizada", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }
    };

    @SuppressLint("StaticFieldLeak")
    private void dibujarCoordenadas(){
        new AsyncTaskObtenerCoordenadas(getApplicationContext()){
            @Override
            protected void onPostExecute(List<Coordenada> coordenadas) {

                for (Marker marker: marcadores){
                    marker.remove();
                }

                for (Coordenada coordenada: coordenadas){
                    Double latitud = coordenada.getLatitud();
                    Double longitud = coordenada.getLongitud();
                    if (coordenada.getAlguienSintomas().equalsIgnoreCase("SI")){
                        MarkerOptions markerOptions = new MarkerOptions();
                        markerOptions.position(new LatLng(latitud, longitud));
                        markerOptions.title("DATOS DEL INFORMANTE");
                        markerOptions.snippet(/*"Nombres: " +*/ coordenada.getNombreEstudiante() + "\n"
                                + /*"Apellidos: " + */coordenada.getApellidoEstudiante() + "\n"
                                + /*"Cédula: " + */coordenada.getCedulaEstudiante() + "\n"
                                + /*"Celular: " + */coordenada.getCelular() + "\n"
                                + /*"Personas en el hogar: " + */coordenada.getPersonasEnHogar() + "\n"
                                + /*"Personas con síntomas: " + */coordenada.getPersonasConSintomas());
//                        tmpMarcadores.add(mMap.addMarker(markerOptions));
                        Marker marker = mMap.addMarker(markerOptions);
                        tmpMarcadores.add(marker);
                    }
                }
                marcadores.clear();
                marcadores.addAll(tmpMarcadores);
                Log.i("MARCADORES>>>>>>>>", String.valueOf(marcadores.size()));
                Log.i("MARCADORES TMP>>>>>>>>", String.valueOf(tmpMarcadores.size()));
            }
        }.execute();
    }

    private void dibujarCoordenada(){

        for (Marker marker: marcadores){
            marker.remove();
        }

        Double latitud = coordenada.getLatitud();
        Double longitud = coordenada.getLongitud();
        if (coordenada.getAlguienSintomas().equalsIgnoreCase("SI")){
            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.position(new LatLng(latitud, longitud));
            markerOptions.title("DATOS DEL INFORMANTE");
            markerOptions.snippet(/*"Nombres: " +*/ coordenada.getNombreEstudiante() + "\n"
                    + /*"Apellidos: " + */coordenada.getApellidoEstudiante() + "\n"
                    + /*"Cédula: " + */coordenada.getCedulaEstudiante() + "\n"
                    + /*"Celular: " + */coordenada.getCelular() + "\n"
                    + /*"Personas en el hogar: " + */coordenada.getPersonasEnHogar() + "\n"
                    + /*"Personas con síntomas: " + */coordenada.getPersonasConSintomas());
//                        tmpMarcadores.add(mMap.addMarker(markerOptions));
            Marker marker = mMap.addMarker(markerOptions);
            tmpMarcadores.add(marker);

            marcadores.clear();
            marcadores.addAll(tmpMarcadores);
            Log.i("MARCADORES>>>>>>>>", String.valueOf(marcadores.size()));
            Log.i("MARCADORES TMP>>>>>>>>", String.valueOf(tmpMarcadores.size()));
        }
    }

    @Override
    public void onInfoWindowClick(Marker marker) {
        Log.i("INFO", "ENTRA EVENTO ONINFOWINDOWCLICK");
//        Toast.makeText(getApplicationContext(), marker.getSnippet(),Toast.LENGTH_SHORT).show();
//        FragmentDatosMarcador.newInstance(marker.getTitle(), marker.getSnippet())
//                .show(getSupportFragmentManager(), null);
    }

    @Override
    public void onBackPressed() {
        Intent menuMapsActivity = new Intent(getApplicationContext(), MenuMapsActivity.class);
        startActivity(menuMapsActivity);
        finish();
    }
}
