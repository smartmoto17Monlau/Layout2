package com.example.user.layout.bot;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.example.user.layout.sensors.Bluetooth;
import com.example.user.layout.sensors.motionsensors;

/**
 * Created by jordimasmer on 02/05/2017.
 */

public class Avisos extends Thread {

    //variables estaticas
    public static Context context;
    public static boolean bucle = true;
    public static boolean thread = true;
    public static  boolean isON = true;

    //variables globales
    private boolean isBtHelped = true;
    boolean wait = true;
    boolean yes = true;

    //constructor vacio
    public Avisos() {
    }

    public void run(){
        //nos esperamos  segundos siempre al empezar el run
        //para darle tiempo al resto de threads a iniciarse
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        //empezamos bucle infinito
        while (thread){
            //si la bateria vuelve a ser mas de 10, reset al aviso de bateria
            try {
                if(Integer.parseInt(Bluetooth.s5) > 10){
                    isBtHelped = true;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            //empezamos un segundo bucle infinito que se encarga de comprobar alertas
            while (bucle){
                try {
                    /*if(Integer.parseInt(Bluetooth.s13) > 10 && !isBtHelped){
                        isBtHelped = true;
                    }*/
                    //hacemos reset en la variable que determina que input da el usuario en el alertdialog
                    yes = false;
                    //comprobamos si alguna de estas condiciones se cumple
                    if((motionsensors.azimuth >= 160 || motionsensors.azimuth <= 10)
                            || (motionsensors.pitch >= 85 || motionsensors.pitch <= -85)
                            || (Integer.parseInt(Bluetooth.s13) <= 10) && isBtHelped){
                        //si se cumple cambiamos las variables booleanas de control del bucle, espera y reset de aviso de bateria
                        bucle = false;
                        isBtHelped = false;
                        wait = true;

                        //nos esperamos 5 segundo
                        try {
                            Thread.sleep(5000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                        //volvemos a mirar si alguna de las condiciones se cumple despues de los 5s de forma separada
                        //para comprobar que no ha sido un fallo de lectura momentaneo
                        if((motionsensors.azimuth >= 160 || motionsensors.azimuth <= 10) || (motionsensors.pitch >= 85 || motionsensors.pitch <= -85)){
                            //si se cumple la condicion de caida, ejecutamos el handler con el tipo de aviso de caida
                            myHandler.sendEmptyMessage(0);
                        }else{
                            //si despues de 5s no se cumple, hacemos reset de bucle
                            bucle = true;
                            yes = false;
                            wait= false;
                        }
                        if(Integer.parseInt(Bluetooth.s13) <= 10){
                            //si despues de 5s se cumple la condicion, ejecutamos handler del tipo de aviso de bateria
                            //y cambiamos la variable de control a false
                            myHandler.sendEmptyMessage(1);
                            isBtHelped =false;
                        }else{
                            //si despues de 5s no se cumple, hacemos reset de bucle
                            bucle = true;
                            yes = false;
                            wait = false;
                        }

                        //ponemos el bucle en espera hasta que el usuario interactue con el alertdialog
                        while(wait){
                            Thread.sleep(1000);
                            Log.i("Avisos: ", "waiting");
                        }

                        //si la respuesta del usuario es negativa esperamos 5s a volver a comprobar las alertas
                        //si el usuario responde que si, asumimos que no hace falta avisar en un tiempo y esperamos 20s
                        if(!yes){
                            try {
                                Thread.sleep(5000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }else{
                            try {
                                Thread.sleep(20000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                //esperamos siempre medio segundo
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            //esperamos siempre medio segundo si sele del bucle
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    //handler que se encarga de ejecutar la alerta que corresponde en cada caso
    private final Handler myHandler = new Handler() {
        public void handleMessage(Message msg) {
            final int what = msg.what;
            switch(what) {
                case 0: alertaCaida(); break;
                case 1: alertaBateria(); break;
            }
        }
    };

    //alertdialog del tipo caida
    private void alertaCaida(){
        final AlertDialog alert = new AlertDialog.Builder(context)
                .setTitle("Are you ok ? ")
                .setMessage("We detected your position in space is wrong are you ok ?")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        //si el usuario responde afirmativamente se hace reset de booleanos de control
                        bucle = true;
                        yes = true;
                        wait = false;
                        //empezamos una instancia del thred que avisa al bot de discord por tcp
                        TcpAviso tcp = new TcpAviso(0);
                        tcp.start();

                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        //si la respuesta es negativa, no hacemos nada y reseteamos el bucle
                        bucle = true;
                        yes = false;
                        wait = false;


                    }
                })
                .setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {
                        //si la respuesta es cancelada, no hacemos nada y reseteamos el bucle
                        bucle = true;
                        yes = false;
                        wait = false;

                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();

        // declaramos un handler para comprovar el tiempo de espera
        final Handler handler  = new Handler();

        //hacemos un runnable con el codigo que queremos ejecutar si nadie responde al alertdialog en 10s
        final Runnable runnable = new Runnable() {
            @Override
            public void run() {
                //si el mensaje esta en pantalla, lo ocultamos, reseteamos los booleanos y enviamos el aviso igualmente
                if (alert.isShowing()) {
                    alert.dismiss();
                    bucle = true;
                    yes = true;
                    wait = false;
                    TcpAviso tcp = new TcpAviso(0);
                    tcp.start();
                }
            }
        };
        //declaramos un listener que ejecutara el runnable cuando pasen 10s
        alert.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                handler.removeCallbacks(runnable);
            }
        });
        //hacemos que el handler ejecute el runnable despues de 10 de espera
        handler.postDelayed(runnable, 10000);
    }

    //alertdialog de tipo bateria
    private void alertaBateria(){
        final AlertDialog alert = new AlertDialog.Builder(context)
                .setTitle("Low Battery")
                .setMessage("We detected your battery is less than 10%, do you want to send help ?")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        //alert
                        bucle = true;
                        yes = true;
                        isBtHelped = false;
                        wait = false;
                        TcpAviso tcp = new TcpAviso(1);
                        tcp.start();

                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // do nothing
                        bucle = true;
                        isBtHelped = true;
                        yes = false;
                        wait = false;


                    }
                })
                .setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {
                        //When you touch outside of dialog bounds,
                        //the dialog gets canceled and this method executes.
                        bucle = true;
                        isBtHelped = true;
                        yes = false;
                        wait = false;

                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
        // Hide after some seconds
        final Handler handler  = new Handler();
        final Runnable runnable = new Runnable() {
            @Override
            public void run() {
                if (alert.isShowing()) {
                    alert.dismiss();
                    bucle = true;
                    yes = true;
                    wait = false;
                    isBtHelped = false;
                    TcpAviso tcp = new TcpAviso(1);
                    tcp.start();
                }
            }
        };

        alert.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                handler.removeCallbacks(runnable);
            }
        });

        handler.postDelayed(runnable, 10000);
    }
}
