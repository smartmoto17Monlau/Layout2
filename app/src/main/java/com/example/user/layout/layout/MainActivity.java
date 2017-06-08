package com.example.user.layout.layout;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.BlurMaskFilter;
import android.graphics.EmbossMaskFilter;
import android.graphics.Typeface;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import android.widget.TextClock;
import android.widget.Toast;

import com.example.user.layout.bot.Avisos;
import com.example.user.layout.bot.TcpAviso;
import com.example.user.layout.sensors.Bluetooth;
import com.example.user.layout.canvas.Meters;
import com.example.user.layout.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.sccomponents.widgets.ScGauge;
import com.sccomponents.widgets.ScLinearGauge;
import com.sccomponents.widgets.ScNotches;

import java.text.DecimalFormat;
import java.util.Timer;
import java.util.TimerTask;


public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {

    //declaracion widgets
    GoogleMap googleMap;
    MapView mapView;
    //TextView tvScroll;
    Typeface type;
    ImageButton menu, right, left, sos;
    TextClock clock;

    ScLinearGauge gauge = null;
    ScLinearGauge gauge2 = null;

    //declaramos thread que actualiza
    private MainActivity.refreshUI refresh;

    //variables globales
    private double latitud = 0, longitud = 0;
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

    //metodo que se encarga de cargar el mapa de google
    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;
        googleMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
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
        googleMap.setMyLocationEnabled(true);

        readGPS();
        float zoomLevel = 16;
        LatLng  tuLocation = new LatLng(latitud, longitud);
        googleMap.addMarker(new MarkerOptions().position(tuLocation).title("Marker en tu posicion"));
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(tuLocation, zoomLevel));
        listeners();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //traemos referencias del layout
        referencias();
        //guardamos el context actual en avisos
        Avisos.context = this;
        //creamos las fuentes y las asignamos donde toca
        type = Typeface.createFromAsset(getAssets(), "fonts/GeosansLight.ttf");
        type = Typeface.createFromAsset(getAssets(), "fonts/DS-DIGI.TTF");
        clock.setTypeface(type);
        mapView.onCreate(savedInstanceState);
        //metodo que llama al inicializador del medidor de bateria
        bateria();
        //creamos una instancia del canvas que dibuja el medidor de velocidad
        final Meters meters = (Meters) findViewById(R.id.meter);
        //inicializamos el thread que se encarga de actualizar los datos
        refresh = new MainActivity.refreshUI(gauge, gauge2, meters);
        refresh.start();
        //comprobamos que tenemos permisos de localizacion
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
        //cargamos mapa asyncronamente
        mapView.getMapAsync(this);
        //declaramos listeners
        listeners();
    }

    //referencias
    private void referencias(){
        mapView = (MapView) findViewById(R.id.mapview);
        //tvScroll = (TextView) findViewById(R.id.textViewScroll);
        clock =(TextClock) findViewById(R.id.textClock1);
        right = (ImageButton) findViewById(R.id.cambioDe);
        left = (ImageButton) findViewById(R.id.cambioIz);
        menu = (ImageButton) findViewById(R.id.Menu);
        clock =(TextClock) findViewById(R.id.textClock1);
        sos = (ImageButton) findViewById(R.id.sos);
    }

    //metodo que lee los datos del gps
    private void readGPS() {
        //iniciamos un locationmanager con nuestro servicio de location
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        //cramos un location listener
        LocationListener locationListener = new LocationListener() {
            //si ela posicion se actualiza
            public void onLocationChanged(Location location) {
                //guardamos los datos que proporciona el gps en variables
                double altitude = location.getAltitude(); //en metros
                double latitude = location.getLatitude(); //en grados
                double longitude = location.getLongitude(); //en grados
                double speed = location.getSpeed() * 3600 / 1000; //en km/h
                //creamos el formato digital que queremos que tenga
                DecimalFormat df = new DecimalFormat("000.0000000");
                //guardamos las ultimas coordenadas en las que hemos estado
                latitud = latitude;
                longitud = longitude;
                LatLng  tuLocation = new LatLng(latitud, longitud);
                float zoomLevel = 16;
                //movemos la camara a la posicion actual
                if(firstTime){
                    googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(tuLocation, zoomLevel));
                    firstTime = false;
                }else{
                    googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(tuLocation, zoomLevel));
                }

            }
            public void onStatusChanged(String provider, int status, Bundle extras) {
            }
            public void onProviderEnabled(String provider) {

            }
            public void onProviderDisabled(String provider) {
            }
        };

        //pedimos permisos
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Toast toast = Toast.makeText(getApplicationContext(), "no permission", Toast.LENGTH_LONG);
            toast.show();
            //si no tiene permisos se los pedimos por pantalla
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
    } // fin de readGPS



    private void cambiarRight(){
        //creamos un intent que hace referencia al segundo activity
        Intent intent = new Intent(MainActivity.this, Main2Activity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        Avisos.context = null;
        startActivity(intent);
    }
    private void cambiarLeft(){
        //creamos un intent que hace referencia al segundo activity
        Intent intent = new Intent(MainActivity.this, Main4Activity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        Avisos.context = null;
        startActivity(intent);
    }
    private void cambiarMenu(){
        //creamos un intent que hace referencia al segundo activity
        Intent intent = new Intent(MainActivity.this, Main3Activity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        Avisos.context = null;
        startActivity(intent);
    }

    //listeners de la layout
    private void listeners(){
        right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cambiarRight();
            }
        });
        left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cambiarLeft();
            }
        });
        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cambiarMenu();
            }
        });
        sos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //si el usuario usa el boton de emergencia se envia un aviso manual al bot
                TcpAviso tcp = new TcpAviso(2);
                tcp.start();
            }
        });

    }

    //metodo que inicializa el component visual que muestra la bateria
    //usamos una libreria externa
    private void bateria(){
        // Find the components
        gauge = (ScLinearGauge) this.findViewById(R.id.line);
        assert gauge != null;

        // Remove all features
        gauge.removeAllFeatures();

        // Take in mind that when you tagged a feature after this feature inherit the principal
        // characteristic of the identifier.
        // For example in the case of the BASE_IDENTIFIER the feature notches (always) will be
        // settle as the color and stroke size settle for the base (in xml or via code).

        // Create the base notches.
        ScNotches base = (ScNotches) gauge.addFeature(ScNotches.class);
        base.setTag(ScGauge.BASE_IDENTIFIER);
        base.setCount(20);
        base.setLength(gauge.dipToPixel(18));

        // Note that I will create two progress because to one will add the blur and to the other
        // will be add the emboss effect.

        // Create the progress notches.
        ScNotches progressBlur = (ScNotches) gauge.addFeature(ScNotches.class);
        progressBlur.setTag(ScGauge.PROGRESS_IDENTIFIER);
        progressBlur.setCount(20);
        progressBlur.setLength(gauge.dipToPixel(18));

        // Create the progress notches.
        ScNotches progressEmboss = (ScNotches) gauge.addFeature(ScNotches.class);
        progressEmboss.setTag(ScGauge.PROGRESS_IDENTIFIER);
        progressEmboss.setCount(20);
        progressEmboss.setLength(gauge.dipToPixel(18));

        // Blur filter
        BlurMaskFilter blur = new BlurMaskFilter(5.0f, BlurMaskFilter.Blur.SOLID);
        progressBlur.getPainter().setMaskFilter(blur);

        // Emboss filter
        EmbossMaskFilter emboss = new EmbossMaskFilter(new float[]{0.0f, 1.0f, 0.5f}, 0.8f, 3.0f, 0.5f);
        progressEmboss.getPainter().setMaskFilter(emboss);

        // Set the value
        gauge.setHighValue(75);
    }

    //clase que actualiza los datos de la layout de forma paralela
    private class refreshUI extends Thread {
        ScLinearGauge gauge, gauge2;
        Meters m;
        Timer timer;
        public refreshUI(ScLinearGauge gauge, ScLinearGauge gauge2, Meters m) {
            this.gauge = gauge;
            this.gauge2 = gauge2;
            this.m = m;
        }

        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable(){
                    public void run() {
                        try{
                            gauge.setHighValue(Integer.parseInt(Bluetooth.s13));
                            m.onSpeedChanged(Float.parseFloat(Bluetooth.s14));
                        }catch(Exception e){

                        }
                    }
                });
            }
        };

        public void run() {
            timer = new Timer("MyTimer");//create a new timer
            timer.scheduleAtFixedRate(timerTask, 0, 50);
        }
    }
}
