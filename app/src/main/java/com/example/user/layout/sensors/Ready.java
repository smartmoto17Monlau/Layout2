package com.example.user.layout.sensors;

/**
 * Created by jordimasmer on 03/07/2017.
 */

public class Ready extends Thread {

    public static boolean ready = false;


    public void run(){

        while(true){
            try{
                Thread.sleep(500);
            }catch(Exception e){
                e.printStackTrace();
            }

            try{
                if(Bluetooth.ready.equals("0")){
                    ready = false;
                }else if(Bluetooth.ready.equals("1")){
                    ready = true;
                }
            }catch(Exception e){
                e.printStackTrace();
            }
        }
    }
}
