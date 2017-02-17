package com.example.user.layout;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.UUID;

/**
 * Created by jordimasmer on 17/02/2017.
 */

public class Bluetooth extends Thread {

    ArrayList<String> currentSensorValue;
    char[] arrayLetras;

    Handler bluetoothIn;
    BluetoothDevice device;

    final int handlerState = 0;
    private BluetoothAdapter btAdapter = null;
    private BluetoothSocket btSocket = null;
    private StringBuilder recDataString = new StringBuilder();

    private InputStream mmInStream;
    //private OutputStream mmOutStream;

    // SPP UUID service - this should work for most devices
    private static final UUID BTMODULEUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    // String for MAC address
    private static String address;

    //variables globales de sensores
    public  static String s0, s1, s2, s3, s4, s5, s6, s7;

    public Bluetooth(String address){
        this.address = address;
        //iniciamos bluetooth socket
        init();
    }
    public Bluetooth(){
    }


    private void init(){
        bluetoothIn = new Handler() {
            public void handleMessage(android.os.Message msg) {

                if (msg.what == handlerState) {                                     //if message is what we want
                    String readMessage = (String) msg.obj;                              // msg.arg1 = bytes from connect thread
                    recDataString.append(readMessage);                                      //keep appending to string until ~
                    int endOfLineIndex = recDataString.indexOf("~");                    // determine the end-of-line
                    if (endOfLineIndex > 0) {                                           // make sure there data before ~
                        String dataInPrint = recDataString.substring(0, endOfLineIndex);    // extract string
                        int dataLength = dataInPrint.length();                          //get length of data received

                        String sensor0 = "";
                        String sensor1 = "";
                        String sensor2 = "";
                        String sensor3 = "";
                        String sensor4 = "";
                        String sensor5 = "";
                        String sensor6 = "";
                        String sensor7 = "";

                        if (recDataString.charAt(0) == '#')       //if it starts with # we know it is what we are looking for
                        {
                            int currentSensor = 0;
                            arrayLetras = dataInPrint.toCharArray();
                            currentSensorValue = new ArrayList<>();
                            for(int i = 0 ; i < dataLength;i++){
                                if(arrayLetras[i] == '#'){

                                }else if(arrayLetras[i] == '+'){
                                    if(currentSensor == 0){
                                        sensor0 = sacarString(currentSensorValue);
                                        currentSensorValue.clear();
                                        currentSensor++;
                                    }else if(currentSensor == 1){
                                        sensor1 = sacarString(currentSensorValue);
                                        currentSensorValue.clear();
                                        currentSensor++;
                                    }else if (currentSensor == 2){
                                        sensor2 = sacarString(currentSensorValue);
                                        currentSensorValue.clear();
                                        currentSensor++;
                                    }else if (currentSensor == 3){
                                        sensor3 = sacarString(currentSensorValue);
                                        currentSensorValue.clear();
                                        currentSensor++;
                                    }else if (currentSensor == 4){
                                        sensor4 = sacarString(currentSensorValue);
                                        currentSensorValue.clear();
                                        currentSensor++;
                                    }else if (currentSensor == 5){
                                        sensor5 = sacarString(currentSensorValue);
                                        currentSensorValue.clear();
                                        currentSensor++;
                                    }else if (currentSensor == 6){
                                        sensor6 = sacarString(currentSensorValue);
                                        currentSensorValue.clear();
                                        currentSensor++;
                                    }else if (currentSensor == 7){
                                        sensor7 = sacarString(currentSensorValue);
                                        currentSensorValue.clear();
                                        currentSensor = 0;
                                    }
                                }else if(arrayLetras[i] == '~'){
                                    currentSensor = 0;
                                    break;
                                }else{
                                    currentSensorValue.add(String.valueOf(arrayLetras[i]));
                                }
                            }
                            s0 = sensor0;
                            s1 = sensor1;
                            s2 = sensor2;
                            s3 = sensor3;
                            s4 = sensor4;
                            s5 = sensor5;
                            s6 = sensor6;
                            s7 = sensor7;
                            //Log.d("MyApp","aaa");
                            //s1.setText(" Sensor 1 = " + sensor1);
                            //s2.setText(" Sensor 2 = " + sensor2);
                            //s3.setText(" Sensor 3 = " + sensor3);
                            //s4.setText(" Sensor 4 = " + sensor4);
                            //s5.setText(" Sensor 5 = " + sensor5);
                            //s6.setText(" Temp = " + sensor6 +"ºC");
                            //s7.setText(" Humidity = " + sensor7+ "%");

                        }
                        recDataString.delete(0, recDataString.length());   //clear all string data
                    }
                }
            }
        };

        btAdapter = BluetoothAdapter.getDefaultAdapter();       // get Bluetooth adapter
        //create device and set the MAC address
        device = btAdapter.getRemoteDevice(address);

        try {
            btSocket = createBluetoothSocket(device);
        } catch (IOException e) {
            //Toast.makeText(getBaseContext(), "Socket creation failed", Toast.LENGTH_LONG).show();
        }
        // Establish the Bluetooth socket connection.
        try
        {
            btSocket.connect();
        } catch (IOException e) {
            try
            {
                btSocket.close();
            } catch (IOException e2)
            {
                //insert code to deal with this
            }
        }
        getInputSocket(btSocket);

    }

    private String sacarString(ArrayList<String> arraylist){
        String texto = "";
        for (String letra : arraylist) {
            texto += letra;
        }
        return texto;
    }

    private BluetoothSocket createBluetoothSocket(BluetoothDevice device) throws IOException {

        return  device.createRfcommSocketToServiceRecord(BTMODULEUUID);
        //creates secure outgoing connecetion with BT device using UUID
    }

    public void run(){
        byte[] buffer = new byte[256];
        int bytes;

        // Keep looping to listen for received messages
        while (true) {
            try {
                bytes = mmInStream.read(buffer);            //read bytes from input buffer
                String readMessage = new String(buffer, 0, bytes);
                // Send the obtained bytes to the UI Activity via handler
                bluetoothIn.obtainMessage(handlerState, bytes, -1, readMessage).sendToTarget();
                Log.d("MyApp","kkk "+s3);
            } catch (IOException e) {
                break;
            }
        }
    }

    private void getInputSocket(BluetoothSocket socket){
        InputStream tmpIn = null;
        //OutputStream tmpOut = null;
        try {
            //Create I/O streams for connection
            tmpIn = socket.getInputStream();
            //tmpOut = socket.getOutputStream();
        } catch (IOException e) { }

        mmInStream = tmpIn;
        //mmOutStream = tmpOut;
    }
}
