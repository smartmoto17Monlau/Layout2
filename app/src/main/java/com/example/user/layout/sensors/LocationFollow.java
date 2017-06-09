package com.example.user.layout.sensors;

import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;


import java.text.DecimalFormat;

import static android.content.Context.LOCATION_SERVICE;

public class LocationFollow {

    //variables globales
    public static double latitud = 0, longitud = 0;
    private Context context;

    //cosntructor de LocationFollow
    public LocationFollow(Context context) {
        //recibe context y empieza a leer del GPS sus valores
        this.context = context;
        readGPS();
    }

    private void readGPS() {
        //inicializamos locationManager y su listener
        LocationManager locationManager = (LocationManager) context.getSystemService(LOCATION_SERVICE);
        LocationListener locationListener = new LocationListener() {
            public void onLocationChanged(Location location) {
                //extraemos la variables que nos interesa
                double latitude = location.getLatitude(); //en grados
                double longitude = location.getLongitude(); //en grados
                double speed = location.getSpeed() * 3600 / 1000; //en km/h
                DecimalFormat df = new DecimalFormat("000.0000000");
                //guardamos las ultimas coordenadas en las que hemos estado
                latitud = latitude;
                longitud = longitude;
                //Log.i("GPS", "Latitud:" +latitud+", Longitud "+ longitud);
            }

            public void onStatusChanged(String provider, int status, Bundle extras) {
            }

            public void onProviderEnabled(String provider) {

            }

            public void onProviderDisabled(String provider) {
            }
        };

        //comprobamos si se dispone de permisos para usar el GPS
        if (ActivityCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        //empezamos a pedir datos de posicion a locationManager desde GPS o Internet
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 10, 1, locationListener);
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 10, 1, locationListener);    } // fin de readGPS
}
