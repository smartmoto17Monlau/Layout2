package com.example.user.layout.bot;

import android.util.Log;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.net.Socket;

/**
 * Created by jordimasmer on 08/05/2017.
 */

public class TcpAviso extends Thread {

    private int tipo;
    private boolean isSent;
    int[] port = new int[]{4, 6, 8, 10, 12};
    int i = 0;

    public TcpAviso(int tipo){
        this.tipo = tipo;
    }

    public void run(){
        isSent = false;
        do{
            try{
                Socket clientSocket = new Socket();
                clientSocket.connect(new InetSocketAddress("172.31.4.8", 9009/*port[i]*/), 1500);
                //clientSocket.connect(new InetSocketAddress("192.168.43.185", 9009/*port[i]*/), 1500);
                //Socket clientSocket = new Socket("172.31.4.8", port[i]);
                DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());
                outToServer.writeInt(tipo);
                clientSocket.close();
                isSent = true;
            }catch(Exception e ){
                e.printStackTrace();
                isSent = false;
                i++;
                if(i == 4){
                    i = 0;
                }
            }
        }while(!isSent);

    }
}
