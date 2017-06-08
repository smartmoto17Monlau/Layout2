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

    //variables globales
    private int tipo;
    private boolean isSent;
    int i = 0;

    //constructor del thread
    public TcpAviso(int tipo){
        //recibe tipo de alerta a enviar al bot
        this.tipo = tipo;
    }

    public void run(){
        //mientras el mensake no se ha enviado, el booleano sera false
        isSent = false;
        do{
            try{
                //cremos nuevo socket de cliente tcp
                Socket clientSocket = new Socket();
                //conectamos con el server tcp (nuestro bot), ponemos 1,5s de timeout
                clientSocket.connect(new InetSocketAddress("172.31.4.8", 9009), 1500);
                //creamos un data output stream
                DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());
                //enviamos el tipo de alerta al bot
                outToServer.writeInt(tipo);
                //cerramos socket de cliente
                clientSocket.close();
                //si se ha enviado, macramos como true el booleano
                isSent = true;
            }catch(Exception e ){
                e.printStackTrace();
                //si no se envia bien, el booleano es false
                isSent = false;
            }
        }while(!isSent);
        //ejecutamos el bucle de envio hasta que cosigamos un envio correcto
    }
}
