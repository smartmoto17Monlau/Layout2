package com.example.user.layout;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextClock;
import android.widget.TextView;


public class Main2Activity extends AppCompatActivity {

    //variables globales
    ImageButton menu, right, left;
    TextClock clock;
    TextView title, title2, s0, s1, s2, s3, s4, s5, s6, s7, s8, sb0, sb1, sb2, sb3, sb4, sb5, sb6, sb7, sb8;
    Typeface type, fuente;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        referencias();
        listeners();
        setTypeface();

    }

    private void cambiarRight(){
        //creamos un intent que hace referencia al segundo activity
        Intent intent = new Intent(Main2Activity.this, Main4Activity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
    private void cambiarLeft(){
        //creamos un intent que hace referencia al segundo activity
        Intent intent = new Intent(Main2Activity.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
    private void cambiarMenu(){
        //creamos un intent que hace referencia al segundo activity
        Intent intent = new Intent(Main2Activity.this, Main3Activity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
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
        s3 = (TextView) findViewById(R.id.s3);;
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
}
