package com.example.user.layout;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.View;

import com.example.user.layout.sensors.Bluetooth;

import java.util.Timer;

/**
 * Created by jordimasmer on 21/03/2017.
 */

public class Marquee extends View {
    //variables globales
    private Timer timer=null ;
    private MyTimerTask task;
    private int interval = 20;

    Paint p = new Paint();
    Typeface type;
    private int yellow = Color.argb(255, 191, 187, 107);
    private int textColor = Color.argb(255, 174, 174, 174);

    int pos1x, pos2x, pos3x, pos4x, pos5x;
    int borde = -400;
    int inicio = 2030;

    //constructor vacio
    public Marquee(Context context) {
        super(context);
    }

    //constructor de Marquee
    public Marquee(Context context, AttributeSet attrs) {
        super(context, attrs);

        //guardamos en un typedArray los datos que hemos declarado en el stylable del archivo attrs.xml
        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.Marquee, 0, 0);
        //inicializamos la fuente
        type = Typeface.createFromAsset(context.getAssets(),"fonts/GeosansLight.ttf");

        //aplicamos opciones de dibujado a la pintura
        p.setTypeface(type);
        p.setAntiAlias(true);
        p.setColor(textColor);
        p.setTextSize(80);

        //establecemos las pociciones iniciales de estos puntos
        pos1x = 10;
        pos2x = pos1x+450;
        pos3x = pos2x+400;
        pos4x = pos3x+500;
        pos5x = pos4x+450;

        //empezamos timer de la animacion
        startTimer();
    }

    @Override //metodo que dibuja la animacion en pantalla
    public void onDraw(Canvas canvas){
        //dibujamos texto en scroll lateral en sus posiciones correspondientes
        canvas.drawText("Temp: "+ Bluetooth.s0+"ºC", pos1x, 80, p);
        canvas.drawText("Hum: "+Bluetooth.s1+"%", pos2x, 80, p);
        canvas.drawText("PA: "+Bluetooth.s2+"HPa", pos3x, 80, p);
        canvas.drawText("CO: "+Bluetooth.s8+"ppm", pos4x, 80, p);
        canvas.drawText("RAD: "+Bluetooth.s12+"uSv", pos5x, 80, p);
        this.invalidate();
    }

    //metodo que se ejecuta cada vez que el timer se reinicia
    public void taskTimer(){
        mover();
        reposicionar();
        this.invalidate();
    }

    //actualizamos los datos de posicion
    private void mover(){
        pos1x -= 1;
        pos2x -= 1;
        pos3x -= 1;
        pos4x -= 1;
        pos5x -= 1;
    }

    //reposicionamiento de los textos para que giren en bucle
    private void reposicionar(){
        if(pos1x < borde){
            pos1x = inicio;
        }
        if(pos2x < borde){
            pos2x = inicio;
        }
        if(pos3x < borde){
            pos3x = inicio;
        }
        if(pos4x < borde){
            pos4x = inicio;
        }
        if(pos5x < borde){
            pos5x = inicio;
        }
    }

    //metodo que empieza el timer a 20ms de intervalo refresco
    private void startTimer(){
        task=new MyTimerTask(this);
        timer= new Timer("miTimer");
        timer.schedule(task, 0, interval);
    }
    //metodo para parar el timer
    private void stopTimer(){
        timer.cancel();
        timer=null; task=null;
    }

}
