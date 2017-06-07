package com.example.user.layout.sensors;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Handler;
import android.util.Log;

import com.example.user.layout.bot.bbdd;

import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.UUID;

import static android.content.Context.SENSOR_SERVICE;

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
    private SensorManager sensorManager = null;
    private motionsensors mtsensors=null;
    private Sensor gyro = null;
    Context mContext;
    private bbdd db;


    private InputStream mmInStream;
    //private OutputStream mmOutStream;

    // SPP UUID service - this should work for most devices
    private static final UUID BTMODULEUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    // String for MAC address
    private static String address;

    //variables globales de sensores
    public  static String s0, s1, s2, s3, s4, s5, s6, s7, s8, s9, s10, s11, s12, s13, s14, s15, s16;
    public static float max = 0;
    public static  boolean isbtOn = false;

    public Bluetooth(String address, Context mContext) throws SQLException, ClassNotFoundException {
        this.address = address;
        this.mContext = mContext;
        //iniciamos bluetooth socket
        init();

        mtsensors = new motionsensors();
        StartSensor();

        LocationFollow loc = new LocationFollow(mContext);
        try{
            db = new bbdd();
            db.start();
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    public Bluetooth(){
    }

    private void StartSensor(){
        sensorManager = (SensorManager) mContext.getSystemService(SENSOR_SERVICE);
        gyro = sensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR);
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
                        String sensor8 = "";
                        String sensor9 = "";
                        String sensor10 = "";
                        String sensor11 = "";
                        String sensor12 = "";
                        String sensor13 = "";
                        String sensor14 = "";
                        String sensor15 = "";
                        String sensor16 = "";

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
                                        //Log.d("0"," ");
                                    }else if(currentSensor == 1){
                                        sensor1 = sacarString(currentSensorValue);
                                        currentSensorValue.clear();
                                        currentSensor++;
                                        //Log.d("1"," ");
                                    }else if (currentSensor == 2){
                                        sensor2 = sacarString(currentSensorValue);
                                        currentSensorValue.clear();
                                        currentSensor++;
                                        //Log.d("2"," ");
                                    }else if (currentSensor == 3){
                                        sensor3 = sacarString(currentSensorValue);
                                        currentSensorValue.clear();
                                        currentSensor++;
                                        //Log.d("3"," ");
                                    }else if (currentSensor == 4){
                                        sensor4 = sacarString(currentSensorValue);
                                        currentSensorValue.clear();
                                        currentSensor++;
                                        //Log.d("4"," ");
                                    }else if (currentSensor == 5){
                                        sensor5 = sacarString(currentSensorValue);
                                        currentSensorValue.clear();
                                        currentSensor++;
                                       //Log.d("5"," ");
                                    }else if (currentSensor == 6){
                                        sensor6 = sacarString(currentSensorValue);
                                        currentSensorValue.clear();
                                        currentSensor++;
                                        //Log.d("6"," ");
                                    }else if (currentSensor == 7){
                                        sensor7 = sacarString(currentSensorValue);
                                        currentSensorValue.clear();
                                        currentSensor++;
                                        //Log.d("7"," ");
                                    }else if (currentSensor == 8){
                                        sensor8 = sacarString(currentSensorValue);
                                        currentSensorValue.clear();
                                        currentSensor++;
                                        //Log.d("8"," ");
                                    }else if (currentSensor == 9){
                                        sensor9 = sacarString(currentSensorValue);
                                        currentSensorValue.clear();
                                        currentSensor++;
                                        //Log.d("9"," ");
                                    }
                                    else if (currentSensor == 10){
                                        sensor10 = sacarString(currentSensorValue);
                                        currentSensorValue.clear();
                                        currentSensor++;
                                        //Log.d("10"," ");
                                    }
                                    else if (currentSensor == 11){
                                        sensor11 = sacarString(currentSensorValue);
                                        currentSensorValue.clear();
                                        currentSensor++;
                                        //Log.d("11"," ");
                                    }
                                    else if (currentSensor == 12){
                                        sensor12 = sacarString(currentSensorValue);
                                        currentSensorValue.clear();
                                        currentSensor++;
                                        //Log.d("12"," ");
                                    }
                                    else if (currentSensor == 13){
                                        sensor13 = sacarString(currentSensorValue);
                                        currentSensorValue.clear();
                                        currentSensor++;
                                        //Log.d("13"," ");
                                    }
                                    else if (currentSensor == 14){
                                        sensor14 = sacarString(currentSensorValue);
                                        currentSensorValue.clear();
                                        currentSensor++;
                                        //Log.d("14"," ");
                                    }else if (currentSensor == 15){
                                        sensor15 = sacarString(currentSensorValue);
                                        currentSensorValue.clear();
                                        currentSensor++;
                                        //Log.d("14"," ");
                                    }else if (currentSensor == 16){
                                        sensor16 = sacarString(currentSensorValue);
                                        currentSensorValue.clear();
                                        currentSensor = 0;
                                        //Log.d("14"," ");
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
                            s8 = sensor8;
                            s9 = sensor9;
                            s10 = sensor10;
                            s11 = sensor11;
                            s12 = sensor12;
                            s13 = sensor13;
                            s14 = sensor14;
                            s15 = sensor15;
                            s16 = sensor16;
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

        sensorManager.registerListener(mtsensors, gyro, SensorManager.SENSOR_DELAY_UI);

        // Keep looping to listen for received messages
        while (true) {
            try {
                bytes = mmInStream.read(buffer);            //read bytes from input buffer
                String readMessage = new String(buffer, 0, bytes);
                // Send the obtained bytes to the UI Activity via handler
                bluetoothIn.obtainMessage(handlerState, bytes, -1, readMessage).sendToTarget();

                Log.d("Datos Arduino","kkk "+s0+ " "+s1+" "+s2+" "+s3+" "+s4+ " "+ s5+" "+s6+ " "+ s7+ " "+ s8+ " "+ s9+ " "+ s10+ " "+ s11+ " "+s12+ " "+s13+ " "+s14+ " "+s15+" "+s16);
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
