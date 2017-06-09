package com.example.user.layout;

import android.os.Handler;
import java.util.TimerTask;

/**
 * Created by jordimasmer on 21/03/2017.
 */

//timer Task que interactua con la clase Marquee
class MyTimerTask extends TimerTask {

    Handler handler = new Handler();
    Marquee marquee;
    public MyTimerTask(Marquee marquee){this.marquee=marquee;}
    public void run() {
        handler.post(new Runnable() {
            public void run() {
                //cada vez que pasamos por el times ejecutamos el metodo Timer Task de la clase marquee
                marquee.taskTimer();
            }});
    }
}
