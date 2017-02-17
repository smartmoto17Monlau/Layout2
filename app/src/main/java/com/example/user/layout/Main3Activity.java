package com.example.user.layout;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextClock;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.UUID;

public class Main3Activity extends AppCompatActivity {

    ImageButton main, map, options, exit;
    TextClock clock;
    Typeface type;

    // String for MAC address
    private static String address;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);

        referencias();
        listeners();
        type = Typeface.createFromAsset(getAssets(),"fonts/DS-DIGI.TTF");
        clock.setTypeface(type);

    }

    @Override
    protected void onResume() {
        super.onResume();
        /*
        //Get MAC address from DeviceListActivity via intent
        Intent intent = getIntent();

        //Get the MAC address from the DeviceListActivty via EXTRA
        Bundle bundle = getIntent().getExtras();
        address = bundle.getString("add");
        */
    }

    private void cambiarMain(){
        //creamos un intent que hace referencia al segundo activity
        Intent intent = new Intent(Main3Activity.this, Main4Activity.class);
        intent.putExtra("add", address);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
    private void cambiarMap(){
        //creamos un intent que hace referencia al segundo activity
        Intent intent = new Intent(Main3Activity.this, MainActivity.class);
        intent.putExtra("add", address);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
    private void cambiarData(){
        //creamos un intent que hace referencia al segundo activity
        Intent intent = new Intent(Main3Activity.this, Main2Activity.class);
        intent.putExtra("add", address);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    private void referencias(){
        main = (ImageButton) findViewById(R.id.mainButton);
        map = (ImageButton) findViewById(R.id.mapButton);
        options = (ImageButton) findViewById(R.id.dataButton);
        exit = (ImageButton) findViewById(R.id.exitButton);
        clock =(TextClock) findViewById(R.id.textClock1);
    }


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
