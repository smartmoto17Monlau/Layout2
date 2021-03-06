package com.example.user.layout.layout;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextClock;

import com.example.user.layout.bot.Avisos;
import com.example.user.layout.sensors.Bluetooth;
import com.example.user.layout.R;

import java.sql.SQLException;

public class Main3Activity extends AppCompatActivity {

    //variables globales
    ImageButton main, map, options, exit;
    TextClock clock;
    Typeface type;
    Context context = this;
    Avisos aviso;
    private static boolean isbtOn;

    // String de la MAC address del bt
    private static String address = "20:16:01:26:18:71";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);
        //traemos referencias
        referencias();
        //declaramos listeners
        listeners();
        //inicializamos y cambiamos la fuente del textclock
        type = Typeface.createFromAsset(getAssets(),"fonts/DS-DIGI.TTF");
        clock.setTypeface(type);

        //si el servicio bt esta encendido
        if(!Bluetooth.isbtOn){
            //creamos e inicializamos el thread que lee el bt del arduino
            Bluetooth btThread = null;
            try {
                btThread = new Bluetooth(address, context);
            } catch (SQLException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            //empezamos el thread
            btThread.start();
            //variable de control para que no se vuelva a iniciar un thread de bt
            Bluetooth.isbtOn = true;
        }

        //cambiamos el oontext del thread de avisos
        Avisos.context = this;

        //empezamos un nuevo thread de avisos solo la primera vez iniciamos esta activity
        if(Avisos.isON){
            Avisos.isON = false;
            aviso = new Avisos();
            aviso.start();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void cambiarMain(){
        //creamos un intent que hace referencia al segundo activity
        Intent intent = new Intent(Main3Activity.this, Main4Activity.class);
        intent.putExtra("add", address);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        Avisos.context = null;
        startActivity(intent);
    }
    private void cambiarMap(){
        //creamos un intent que hace referencia al segundo activity
        Intent intent = new Intent(Main3Activity.this, MainActivity.class);
        intent.putExtra("add", address);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        Avisos.context = null;
        startActivity(intent);
    }
    private void cambiarData(){
        //creamos un intent que hace referencia al segundo activity
        Intent intent = new Intent(Main3Activity.this, Main2Activity.class);
        intent.putExtra("add", address);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        Avisos.context = null;
        startActivity(intent);
    }
    //referencias
    private void referencias(){
        main = (ImageButton) findViewById(R.id.mainButton);
        map = (ImageButton) findViewById(R.id.mapButton);
        options = (ImageButton) findViewById(R.id.dataButton);
        exit = (ImageButton) findViewById(R.id.exitButton);
        clock =(TextClock) findViewById(R.id.textClock1);
    }

    //listeners
    private void listeners(){
        main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cambiarMain();
            }
        });
        map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cambiarMap();
            }
        });
        options.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cambiarData();
            }
        });
        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                System.exit(0);
            }
        });
    }
}
