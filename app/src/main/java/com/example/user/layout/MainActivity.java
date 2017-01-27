package com.example.user.layout;

import android.Manifest;
import android.content.Intent;
import android.content.SyncStatusObserver;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextClock;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.text.DecimalFormat;


public class MainActivity extends AppCompatActivity {

    //declaracion widgets
    GoogleMap googleMap;
    MapView mapView;
    TextView tvScroll;
    ImageButton cambiarActivity;
    TextClock clock;

    //variables globales
    private double latitud = 0, longitud =0;
    private boolean firstTime = true;


    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        referencias();


        mapView.onCreate(savedInstanceState);
        tvScroll.setSelected(true);

        googleMap = mapView.getMap();
        googleMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        readGPS();
        float zoomLevel = 16;
        googleMap.setMyLocationEnabled(true);
        LatLng  tuLocation = new LatLng(latitud, longitud);
        googleMap.addMarker(new MarkerOptions().position(tuLocation).title("Marker en tu posicion"));
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(tuLocation, zoomLevel));
        listeners();
    }

    private void referencias(){
        mapView = (MapView) findViewById(R.id.mapview);
        tvScroll = (TextView) findViewById(R.id.textViewScroll);
        cambiarActivity = (ImageButton) findViewById(R.id.cambio);

    }

    private void cambiarActivity(){
        //creamos un intent que hace referencia al segundo activity
        /*Intent intent2 = new Intent(MainActivity.this, Main2Activity.class);
        try{
            //enviamos datos al acivity 2
            Bundle bundleColor = new Bundle();
            bundleColor.putString("color", color);
            intent2.putExtras(bundleColor);
        }catch(Exception e){
            e.printStackTrace();
        }
        startActivity(intent2);*/
    }

    private void readGPS() {
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        LocationListener locationListener = new LocationListener() {
            public void onLocationChanged(Location location) {

                double altitude = location.getAltitude(); //en metros
                double latitude = location.getLatitude(); //en grados
                double longitude = location.getLongitude(); //en grados
                double speed = location.getSpeed() * 3600 / 1000; //en km/h
                DecimalFormat df = new DecimalFormat("000.0000");
                //guardamos las ultimas coordenadas en las que hemos estado
                latitud = latitude;
                longitud = longitude;

                if(firstTime){
                    float zoomLevel = 16;
                    LatLng  tuLocation = new LatLng(latitud, longitud);
                    googleMap.addMarker(new MarkerOptions().position(tuLocation).title("Marker en tu posicion"));
                    googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(tuLocation, zoomLevel));
                    firstTime = false;
                }

            }
            public void onStatusChanged(String provider, int status, Bundle extras) {
            }
            public void onProviderEnabled(String provider) {

            }
            public void onProviderDisabled(String provider) {
            }
        };
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Toast toast = Toast.makeText(getApplicationContext(), "no permission", Toast.LENGTH_LONG);
            toast.show();
            //si no tiene permisos se los pedimos por pantalla
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0,
                locationListener);
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0,
                locationListener);
    } // fin de readGPS




    private void listeners(){
        cambiarActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // tvScroll.setText("Temp: 20ºC   Humidity: 40%   Pressure: 100HPa   Temp: 20ºC   Humidity: 40%   Pressure: 100HPa");
            }
        });
    }


}
