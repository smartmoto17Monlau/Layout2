package com.example.user.layout;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.graphics.Typeface;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextClock;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;

public class Main4Activity extends AppCompatActivity {

    ImageButton menu, right, left;
    TextClock clock;
    Typeface type;
    Boolean uiOn = true;

    private motionsensors mtsensors=null;
    private SensorManager sensorManager = null;
    private Sensor gyro = null;

    private Main4Activity.refreshUI refresh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main4);

        referencias();
        listeners();

        type = Typeface.createFromAsset(getAssets(),"fonts/DS-DIGI.TTF");
        clock.setTypeface(type);

        final Speedometer speedometer = (Speedometer) findViewById(R.id.Speedometer);
        mtsensors = new motionsensors();
        StartSensor();
        refresh = new Main4Activity.refreshUI(speedometer);
        refresh.start();
    }

    @Override
    protected void onResume() {
        super.onResume();
        uiOn = true;
        // enviar los cambios del sensor a la vista Gview
        sensorManager.registerListener(mtsensors, gyro, SensorManager.SENSOR_DELAY_UI);
        //mConnectedThread = new Main4Activity.ConnectedThread(btSocket);
        //mConnectedThread.start();
    }
    @Override
    public void onPause()
    {
        super.onPause();
        sensorManager.unregisterListener(mtsensors); //Dejar de enviar los cambios del sensor a gview
        uiOn = false;
    }

    private void StartSensor(){
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        gyro = sensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR);
    }

    private void cambiarRight(){
        //creamos un intent que hace referencia al segundo activity
        Intent intent = new Intent(Main4Activity.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
    private void cambiarLeft(){
        //creamos un intent que hace referencia al segundo activity
        Intent intent = new Intent(Main4Activity.this, Main2Activity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
    private void cambiarMenu(){
        //creamos un intent que hace referencia al segundo activity
        Intent intent = new Intent(Main4Activity.this, Main3Activity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    private void referencias(){
        right = (ImageButton) findViewById(R.id.cambioDe);
        left = (ImageButton) findViewById(R.id.cambioIz);
        menu = (ImageButton) findViewById(R.id.Menu);
        clock =(TextClock) findViewById(R.id.textClock1);
    }


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

    }

    //create new class for connect thread
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
                            speedometer.onSpeedChanged(Float.parseFloat(Bluetooth.s7));
                            //speedometer.onSpeedChanged(555);
                            speedometer.onBatteryChanged(Float.parseFloat(Bluetooth.s7));
                            //speedometer.onBatteryChanged(60);
                            speedometer.onBatteryTempChanged(Float.parseFloat(Bluetooth.s3));
                            speedometer.onMotorTempChanged(Float.parseFloat(Bluetooth.s5));

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
