package com.example.user.layout;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;

/**
 * Created by jordimasmer on 13/03/2017.
 */

public class Meters extends View implements SpeedChangeListener {

    private float mMaxSpeed = 130;
    private float yPointer=810;
    private float yPointerS;
    private int yMax=810;
    private float delatY = 0;
    private float mCurrentSpeed;
    private float currentBattery;
    private float mCurrentBatteryTemp;
    float[] listaY = new float[10];
    int index = 0;

    Paint p = new Paint();
    Typeface type;
    private int yellow = Color.argb(255, 191, 187, 107);


    public Meters(Context context) {
        super(context);
    }

    public Meters(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.Meter, 0, 0);
        type = Typeface.createFromAsset(context.getAssets(),"fonts/GeosansLight.ttf");
        p.setTypeface(type);
        p.setAntiAlias(true);

        for(int i = 0; i<9; i++){
            listaY[i] = 0;
        }
    }


    public void setCurrentSpeed(float mCurrentSpeed) {
        if(mCurrentSpeed > this.mMaxSpeed) {
            this.mCurrentSpeed = mMaxSpeed;
        }else if(mCurrentSpeed < 0) {
            this.mCurrentSpeed = 0;
        }else {
            this.mCurrentSpeed = mCurrentSpeed;
        }
        //calc pointer y position
        delatY = (mCurrentSpeed/10)*58.5714f;
        yPointer = yMax - delatY;

        yPointerS = suavizarPointer(yPointer);

    }

    public void setCurrentBattery(float mCurrentBattery) {
        if(mCurrentBattery > this.mMaxSpeed)
            this.currentBattery = mMaxSpeed;
        else if(mCurrentBattery < 0)
            this.currentBattery = 0;
        else
            this.currentBattery = mCurrentBattery;
    }

    @Override
    public void onDraw(Canvas canvas){
        float h = 810;
        int valor = 0;
        p.setColor(yellow);
        p.setTextSize(50);
        p.setStrokeWidth(1);
        for(int i=0;i<14;i++){
            canvas.drawLine(0, h, 70, h, p);

            if(i < 10 &&  i != 0){
                canvas.drawText("  "+valor, 75, h+15, p);
            }else if(i != 0){
                canvas.drawText(valor+"", 75, h+15, p);
            }
            h -= 58.5714;
            valor += 10;
        }
        float h2 = 810 - 29.2857f;
        for(int i=0;i<13;i++){
            canvas.drawLine(0, h2, 35, h2, p);
            h2 -= 58.5714;
        }
        p.setColor(Color.RED);
        p.setStrokeWidth(3);
        canvas.drawLine(0, yPointerS, 70, yPointerS, p);
    }
    private float suavizarPointer(float yPointer){
        float suavizado = 0;
        if(index < 9){
            listaY[index] = yPointer;
            index++;
        }else{
            listaY[index] = yPointer;
            index = 0;
        }

        for (int i = 0; i< listaY.length; i++){
            suavizado += listaY[i];
        }
        suavizado = suavizado/listaY.length;
        return suavizado;
    }


    @Override
    public void onSpeedChanged(float newSpeedValue) {
        this.setCurrentSpeed(newSpeedValue);
        this.invalidate();
    }
    @Override
    public void onBatteryChanged(float newBatValue) {
        this.setCurrentBattery(newBatValue);
        this.invalidate();
    }

    @Override
    public void onMotorTempChanged(float newMotorTempValue) {

    }

    @Override
    public void onBatteryTempChanged(float newBatteryTempValue) {

    }
}
