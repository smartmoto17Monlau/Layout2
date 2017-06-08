package com.example.user.layout.layout;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextClock;

import com.example.user.layout.bot.Avisos;
import com.example.user.layout.bot.TcpAviso;
import com.example.user.layout.sensors.Bluetooth;
import com.example.user.layout.R;
import com.example.user.layout.canvas.Speedometer;

import java.util.Timer;
import java.util.TimerTask;

public class Main4Activity extends AppCompatActivity {

    //variables globales
    ImageButton menu, right, left, sos;
    TextClock clock;
    Typeface type;
    Boolean uiOn = true;
    private Main4Activity.refreshUI refresh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main4);
        //traemos referenciasy declaramos listeners
        referencias();
        listeners();

        //cramos la fuente y la cambiamos en el textclock
        type = Typeface.createFromAsset(getAssets(),"fonts/DS-DIGI.TTF");
        clock.setTypeface(type);

        //cambiamos el context del thread avisos
        Avisos.context = this;
        //inicializamos el canvas
        final Speedometer speedometer = (Speedometer) findViewById(R.id.Speedometer);
        //inicializamos el thread que actualiza la UI y lo empezamos
        refresh = new Main4Activity.refreshUI(speedometer);
        refresh.start();
    }

    @Override
    protected void onResume() {
        super.onResume();
        uiOn = true;
    }
    @Override
    public void onPause()
    {
        super.onPause();
        uiOn = false;
    }

    private void cambiarRight(){
        //creamos un intent que hace referencia al segundo activity
        Intent intent = new Intent(Main4Activity.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        Avisos.context = null;
        startActivity(intent);
    }
    private void cambiarLeft(){
        //creamos un intent que hace referencia al segundo activity
        Intent intent = new Intent(Main4Activity.this, Main2Activity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        Avisos.context = null;
        startActivity(intent);
    }
    private void cambiarMenu(){
        //creamos un intent que hace referencia al segundo activity
        Intent intent = new Intent(Main4Activity.this, Main3Activity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        Avisos.context = null;
        startActivity(intent);
    }

    //referencias
    private void referencias(){
        right = (ImageButton) findViewById(R.id.cambioDe);
        left = (ImageButton) findViewById(R.id.cambioIz);
        menu = (ImageButton) findViewById(R.id.Menu);
        clock =(TextClock) findViewById(R.id.textClock1);
        sos = (ImageButton) findViewById(R.id.sos);
    }

    //listeners
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

    //thread que actualiza los valores de la layout de forma paralela
    private class refreshUI extends Thread {
        Speedometer speedometer;
        Timer timer;
        public refreshUI(Speedometer speedometer) {
            this.speedometer = speedometer;
        }

        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable(){
                    public void run() {
                        try{
                            speedometer.onBatteryChanged(Float.parseFloat(Bluetooth.s13));
                            speedometer.onSpeedChanged(Float.parseFloat(Bluetooth.s14));
                            speedometer.onMotorTempChanged(Float.parseFloat(Bluetooth.s16));
                            speedometer.onBatteryTempChanged(Float.parseFloat(Bluetooth.s15));
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
