package com.example.user.layout;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by jordimasmer on 02/05/2017.
 */

public class Avisos extends Thread {

    public static Context context;
    public static boolean bucle = true;
    public static boolean thread = true;
    boolean yes = true;

    public Avisos(Context context) {
        //this.context = context;
    }

    public void run(){
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        while (thread){
            while (bucle){
                if((motionsensors.azimuth >= 160 || motionsensors.azimuth <= 10)){
                    bucle = false;
                    try {
                        Thread.sleep(5000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    if((motionsensors.azimuth >= 160 || motionsensors.azimuth <= 10)){
                        myHandler.sendEmptyMessage(0);
                    }else{
                        bucle = true;
                        yes = false;
                    }

                    if(!yes){
                        try {
                            Thread.sleep(500);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }else{
                        try {
                            Thread.sleep(5000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Log.i("eeeeeeeeee", "eeeeeeeeeeeee");
            }
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Log.i("aaaaaa", "aaaaaaaaaaaaaaaaaaaaaa");
        }
    }

    private final Handler myHandler = new Handler() {
        public void handleMessage(Message msg) {
            final int what = msg.what;
            switch(what) {
                case 0: alertaCaida(); break;
                case 1: alertaCaida(); break;
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


                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // do nothing
                        bucle = true;
                        yes = false;


                    }
                })
                .setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {
                        //When you touch outside of dialog bounds,
                        //the dialog gets canceled and this method executes.
                        bucle = true;
                        yes = false;

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
