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

    public static Context context;
    public static boolean bucle = true;
    public static boolean thread = true;
    public static  boolean isON = true;
    public static String thName = "a3";
    private boolean isBtHelped = true;
    boolean wait = true;
    boolean yes = true;

    public Avisos() {
        //this.context = context;
    }

    public void run(){
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        while (thread){
            /*try {
                if(Integer.parseInt(Bluetooth.s5) > 10){
                    isBtHelped = true;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }*/
            while (bucle){
                try {
                    /*if(Integer.parseInt(Bluetooth.s13) > 10 && !isBtHelped){
                        isBtHelped = true;
                    }*/
                    yes = false;
                    if((motionsensors.azimuth >= 160 || motionsensors.azimuth <= 10)
                            || (motionsensors.pitch >= 85 || motionsensors.pitch <= -85)
                            || (Integer.parseInt(Bluetooth.s13) <= 10) && isBtHelped){
                        bucle = false;
                        isBtHelped =false;;
                        wait = true;
                        //isBtHelped =false;
                        try {
                            Thread.sleep(5000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                        if((motionsensors.azimuth >= 160 || motionsensors.azimuth <= 10) || (motionsensors.pitch >= 85 || motionsensors.pitch <= -85)){
                            myHandler.sendEmptyMessage(0);
                        }else{
                            bucle = true;
                            yes = false;
                            wait= false;
                            //break;
                        }
                        if(Integer.parseInt(Bluetooth.s13) <= 10){
                            myHandler.sendEmptyMessage(1);
                            isBtHelped =false;
                        }else{
                            bucle = true;
                            yes = false;
                            wait = false;
                        }

                        while(wait){
                            Thread.sleep(1000);
                            Log.i("aaa: ", "waiting");
                        }

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

                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                //Log.i("eeeeeeeeee", "eeeeeeeeeeeee");
            }
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            //Log.i("aaaaaa", "aaaaaaaaaaaaaaaaaaaaaa");
        }
    }

    private final Handler myHandler = new Handler() {
        public void handleMessage(Message msg) {
            final int what = msg.what;
            switch(what) {
                case 0: alertaCaida(); break;
                case 1: alertaBateria(); break;
            }
        }
    };

    private void alertaCaida(){
        final AlertDialog alert = new AlertDialog.Builder(context)
                .setTitle("Are you ok ? ")
                .setMessage("We detected you position in space is wrong are you ok ?")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        //alert
                        bucle = true;
                        yes = true;
                        wait = false;
                        TcpAviso tcp = new TcpAviso(0);
                        tcp.start();

                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // do nothing
                        bucle = true;
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
                    TcpAviso tcp = new TcpAviso(0);
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
