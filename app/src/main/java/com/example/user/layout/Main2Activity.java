package com.example.user.layout;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;


public class Main2Activity extends AppCompatActivity {

    //variables globales
    ImageButton cambiarActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        referencias();
        listeners();
    }

    private void cambiarActivity(){
        //creamos un intent que hace referencia al segundo activity
        Intent intent = new Intent(Main2Activity.this, MainActivity.class);
        startActivity(intent);
    }

    private void referencias(){
        cambiarActivity = (ImageButton) findViewById(R.id.cambio);

    }


    private void listeners(){
        cambiarActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // tvScroll.setText("Temp: 20ºC   Humidity: 40%   Pressure: 100HPa   Temp: 20ºC   Humidity: 40%   Pressure: 100HPa");
                cambiarActivity();
            }
        });
    }

}
