package com.example.user.layout;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.Socket;

/**
 * Created by jordimasmer on 08/05/2017.
 */

public class TcpAviso extends Thread {

    private int tipo;

    public TcpAviso(int tipo){
        this.tipo = tipo;
    }

    public void run(){
        try{
            Socket clientSocket = new Socket("172.31.4.8", 6);
            DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());
            outToServer.writeInt(tipo);
            clientSocket.close();

        }catch(Exception e ){
            e.printStackTrace();
        }
    }
}
