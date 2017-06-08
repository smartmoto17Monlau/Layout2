package com.example.user.layout.canvas;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.View;

import com.example.user.layout.R;

/**
 * Created by jordimasmer on 13/03/2017.
 */

public class Meters extends View implements SpeedChangeListener { //implementamos la interface que atualiza los datos que necesita el view

    //variables globales
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

    //constructor vacio
    public Meters(Context context) {
        super(context);
    }
    //construcotor de la clase
    public Meters(Context context, AttributeSet attrs) {
        super(context, attrs);
        //creamos un typed array a base del stylable que hemos declarado en el fichero attrs.xml
        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.Meter, 0, 0);
        //cramos fuente y la ponemos por defecto
        type = Typeface.createFromAsset(context.getAssets(),"fonts/GeosansLight.ttf");
        p.setTypeface(type);
        //activamos el antialiasis para que las letras no queden borrosas
        p.setAntiAlias(true);
        //llenamos el array de valores 0
        for(int i = 0; i<9; i++){
            listaY[i] = 0;
        }
    }

    //metodo que se encarga de guardar la nueva velocidad
    //y se asegura que no es mas o menos que el maximo y minimo permitido
    public void setCurrentSpeed(float mCurrentSpeed) {
        if(mCurrentSpeed > this.mMaxSpeed) {
            this.mCurrentSpeed = mMaxSpeed;
        }else if(mCurrentSpeed < 0) {
            this.mCurrentSpeed = 0;
        }else {
            this.mCurrentSpeed = mCurrentSpeed;
        }
        //calculamos el pointer y position
        delatY = (mCurrentSpeed/10)*58.5714f;
        yPointer = yMax - delatY;
        //guardamos el valor del pointer suavizado
        yPointerS = suavizarPointer(yPointer);

    }

    //metodo que se encarga de guardar la nueva bateria
    //y asegurarse que no es mas o menos el el maximo o minimo permitidos
    public void setCurrentBattery(float mCurrentBattery) {
        if(mCurrentBattery > this.mMaxSpeed)
            this.currentBattery = mMaxSpeed;
        else if(mCurrentBattery < 0)
            this.currentBattery = 0;
        else
            this.currentBattery = mCurrentBattery;
    }

    //metodo que se encarga de dibujar el medidor de velocidad
    @Override
    public void onDraw(Canvas canvas){
        //reset de valores
        float h = 810;
        int valor = 0;
        //reset de atributos de la pintura
        p.setColor(yellow);
        p.setTextSize(50);
        p.setStrokeWidth(1);
        //dibujamos las lineas grandes y el texto de graduacion del medidor
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
        //dibujamos la lineas pequeñas
        for(int i=0;i<13;i++){
            canvas.drawLine(0, h2, 35, h2, p);
            h2 -= 58.5714;
        }
        //modificamos las opciones de la pintura y dibujamos el pointer
        p.setColor(Color.RED);
        p.setStrokeWidth(3);
        canvas.drawLine(0, yPointerS, 70, yPointerS, p);
    }

    //suavizamos valor de posicion del pointer haciendo una media de los ultimos 10 valores
    //asi la animacion es mucho mas fluida
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

    //si el valor es nuevo, lo guardamos e invalidamos el canvas
    @Override
    public void onSpeedChanged(float newSpeedValue) {
        this.setCurrentSpeed(newSpeedValue);
        this.invalidate();
    }
    //si el valor es nuevo, lo guardamos e invalidamos el canvas
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
