package com.example.user.layout;

import android.content.Context;
import android.os.Handler;

import java.util.TimerTask;

/**
 * Created by jordimasmer on 21/03/2017.
 */

class MyTimerTask extends TimerTask {

    Handler handler = new Handler();
    Context context;
    Marquee marquee;
    public MyTimerTask(Marquee marquee){this.marquee=marquee;}
    public void run() {
        handler.post(new Runnable() {
            public void run() {
                marquee.taskTimer();
            }});
    }
}
