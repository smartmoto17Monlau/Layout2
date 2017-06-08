package com.example.user.layout.layout;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextClock;
import android.widget.TextView;

import com.example.user.layout.bot.Avisos;
import com.example.user.layout.bot.TcpAviso;
import com.example.user.layout.sensors.Bluetooth;
import com.example.user.layout.sensors.LocationFollow;
import com.example.user.layout.R;

import java.util.Timer;
import java.util.TimerTask;


public class Main2Activity extends AppCompatActivity {

    //variables globales
    ImageButton menu, right, left, sos;
    TextClock clock;
    TextView title, title2, s0, s1, s2, s3, s4, s5, s6, s7, s8, sb0, sb1, sb2, sb3, sb4, sb5, sb6, sb7, sb8;
    Typeface type, fuente;
    private Main2Activity.refreshUI refresh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        //traemos referencias
        referencias();
        //declaramos listeners
        listeners();
        //cambiamos las fuentes
        setTypeface();
        //cambiamos el context del thread de avisos
        Avisos.context = this;
        //inicializamos y empezamos el thread que actualiza la layout
        refresh = new Main2Activity.refreshUI();
        refresh.start();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause()
    {
        super.onPause();
    }

    private void cambiarRight(){
        //creamos un intent que hace referencia al segundo activity
        Intent intent = new Intent(Main2Activity.this, Main4Activity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        Avisos.context = null;
        startActivity(intent);
    }
    private void cambiarLeft(){
        //creamos un intent que hace referencia al segundo activity
        Intent intent = new Intent(Main2Activity.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        Avisos.context = null;
        startActivity(intent);
    }
    private void cambiarMenu(){
        //creamos un intent que hace referencia al segundo activity
        Intent intent = new Intent(Main2Activity.this, Main3Activity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        Avisos.context = null;
        startActivity(intent);
    }

    //metodo que cambia la fuente de todos los textviews
    private void setTypeface(){
        type = Typeface.createFromAsset(getAssets(),"fonts/DS-DIGI.TTF");
        fuente = Typeface.createFromAsset(getAssets(), "fonts/GeosansLight.ttf");;
        title.setTypeface(fuente);
        title2.setTypeface(fuente);
        s0.setTypeface(fuente);
        s1.setTypeface(fuente);
        s2.setTypeface(fuente);
        s3.setTypeface(fuente);
        s4.setTypeface(fuente);
        s5.setTypeface(fuente);
        s6.setTypeface(fuente);
        s7.setTypeface(fuente);
        s8.setTypeface(fuente);
        sb0.setTypeface(fuente);
        sb1.setTypeface(fuente);
        sb2.setTypeface(fuente);
        sb3.setTypeface(fuente);
        sb4.setTypeface(fuente);
        sb5.setTypeface(fuente);
        sb6.setTypeface(fuente);
        sb7.setTypeface(fuente);
        sb8.setTypeface(fuente);
        clock.setTypeface(type);
    }

    //referencias
    private void referencias(){
        right = (ImageButton) findViewById(R.id.cambioDe);
        left = (ImageButton) findViewById(R.id.cambioIz);
        menu = (ImageButton) findViewById(R.id.Menu);
        clock =(TextClock) findViewById(R.id.textClock1);
        title = (TextView) findViewById(R.id.title);
        title2 = (TextView) findViewById(R.id.title2);
        s0 = (TextView) findViewById(R.id.s0);
        s1 = (TextView) findViewById(R.id.s1);
        s2 = (TextView) findViewById(R.id.s2);
        s3 = (TextView) findViewById(R.id.s3);
        s4 = (TextView) findViewById(R.id.s4);
        s5 = (TextView) findViewById(R.id.s5);
        s6 = (TextView) findViewById(R.id.s6);
        s7 = (TextView) findViewById(R.id.s7);
        s8 = (TextView) findViewById(R.id.s8);
        sb0 = (TextView) findViewById(R.id.bs0);
        sb1 = (TextView) findViewById(R.id.bs1);
        sb2 = (TextView) findViewById(R.id.bs2);
        sb3 = (TextView) findViewById(R.id.bs3);
        sb4 = (TextView) findViewById(R.id.bs4);
        sb5 = (TextView) findViewById(R.id.bs5);
        sb6 = (TextView) findViewById(R.id.bs6);
        sb7 = (TextView) findViewById(R.id.bs7);
        sb8 = (TextView) findViewById(R.id.bs8);
        sos = (ImageButton) findViewById(R.id.sos);
    }

    //listeners del layout
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
                TcpAviso tcp = new TcpAviso(2);
                tcp.start();
            }
        });
    }

    //clase que actualiza los datos que aparecen en la layout
    private class refreshUI extends Thread {
        Timer timer;
        public refreshUI() {

        }

        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable(){
                    public void run() {
                        s0.setText("Temperature: "+ Bluetooth.s0 + " ºC");
                        s1.setText("Humidity: "+Bluetooth.s1 + " %");
                        s2.setText("Pressure: "+Bluetooth.s2 + " hPa");
                        s3.setText("Height: "+Bluetooth.s3 + " m");
                        s4.setText("LPG: "+Bluetooth.s4 + " ppm");
                        s5.setText("Propane: "+Bluetooth.s5+ " ppm");
                        s6.setText("Natural Gas: "+ Bluetooth.s6+ " ppm");
                        s7.setText("Butane: "+Bluetooth.s7+ " ppm");
                        s8.setText("Carbon Monoxide: "+Bluetooth.s8+ " ppm");
                        sb0.setText("Hydrogen: "+Bluetooth.s9+ " ppm");
                        sb1.setText("Methane: "+Bluetooth.s10+ " ppm");
                        sb2.setText("CO2: "+Bluetooth.s11 + " ppm");
                        sb3.setText("Radiation: "+Bluetooth.s12+ " uSv/h");
                        sb4.setText("Battery Temp: "+Bluetooth.s16 + " ºC");
                        sb5.setText("Motor Temp: "+Bluetooth.s15 + " ºC");
                        sb7.setText("Latitude: "+LocationFollow.latitud);
                        sb6.setText("Longitude: "+LocationFollow.longitud);
                        sb8.setText("Speed: "+Bluetooth.s14 +" Km/H");
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
