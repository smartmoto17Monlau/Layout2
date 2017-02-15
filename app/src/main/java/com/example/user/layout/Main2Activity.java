package com.example.user.layout;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextClock;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.UUID;


public class Main2Activity extends AppCompatActivity {

    //variables globales
    ImageButton menu, right, left;
    TextClock clock;
    TextView title, title2, s0, s1, s2, s3, s4, s5, s6, s7, s8, sb0, sb1, sb2, sb3, sb4, sb5, sb6, sb7, sb8;
    Typeface type, fuente;
    Handler bluetoothIn;

    ArrayList<String> currentSensorValue;
    char[] arrayLetras;

    final int handlerState = 0;
    private BluetoothAdapter btAdapter = null;
    private BluetoothSocket btSocket = null;
    private StringBuilder recDataString = new StringBuilder();

    private Main2Activity.ConnectedThread mConnectedThread;

    // SPP UUID service - this should work for most devices
    private static final UUID BTMODULEUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    // String for MAC address
    private static String address;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        referencias();
        listeners();
        setTypeface();

        bluetoothIn = new Handler() {
            public void handleMessage(android.os.Message msg) {

                if (msg.what == handlerState) {                                     //if message is what we want
                    String readMessage = (String) msg.obj;                              // msg.arg1 = bytes from connect thread
                    recDataString.append(readMessage);                                      //keep appending to string until ~
                    int endOfLineIndex = recDataString.indexOf("~");                    // determine the end-of-line
                    if (endOfLineIndex > 0) {                                           // make sure there data before ~
                        String dataInPrint = recDataString.substring(0, endOfLineIndex);    // extract string
                        //txtString.setText("Data Received = " + dataInPrint);
                        int dataLength = dataInPrint.length();                          //get length of data received
                        //txtStringLength.setText("String Length = " + String.valueOf(dataLength));

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
                                    //txtString.setText("~");
                                    break;
                                }else{
                                    currentSensorValue.add(String.valueOf(arrayLetras[i]));
                                }
                            }
                            s0.setText(" Sensor 0 = " + sensor0 + " ppm");    //update the textviews with sensor values
                            s1.setText(" Sensor 1 = " + sensor1);
                            s2.setText(" Sensor 2 = " + sensor2);
                            s3.setText(" Sensor 3 = " + sensor3);
                            s4.setText(" Sensor 4 = " + sensor4);
                            s5.setText(" Sensor 5 = " + sensor5);
                            s6.setText(" Temp = " + sensor6 +"ºC");
                            s7.setText(" Humidity = " + sensor7+ "%");

                        }
                        recDataString.delete(0, recDataString.length());                    //clear all string data
                        // strIncom =" ";
                        dataInPrint = " ";
                    }
                }
            }
        };
        btAdapter = BluetoothAdapter.getDefaultAdapter();       // get Bluetooth adapter

        checkBTState();

    }

    @Override
    public void onResume() {
        super.onResume();

        //Get MAC address from DeviceListActivity via intent
        Intent intent = getIntent();

        //Get the MAC address from the DeviceListActivty via EXTRA
        Bundle bundle = getIntent().getExtras();
        address = bundle.getString("add");

        //create device and set the MAC address
        BluetoothDevice device = btAdapter.getRemoteDevice(address);

        try {
            btSocket = createBluetoothSocket(device);
        } catch (IOException e) {
            Toast.makeText(getBaseContext(), "Socket creation failed", Toast.LENGTH_LONG).show();
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
        mConnectedThread = new Main2Activity.ConnectedThread(btSocket);
        mConnectedThread.start();

        //I send a character when resuming.beginning transmission to check device is connected
        //If it is not an exception will be thrown in the write method and finish() will be called
        mConnectedThread.write("x");
    }

    @Override
    public void onPause()
    {
        super.onPause();
        try
        {
            //Don't leave Bluetooth sockets open when leaving activity
            btSocket.close();
        } catch (IOException e2) {
            //insert code to deal with this
        }
    }

    //Checks that the Android device Bluetooth is available and prompts to be turned on if off
    private void checkBTState() {
        if(btAdapter==null) {
            Toast.makeText(getBaseContext(), "Device does not support bluetooth", Toast.LENGTH_LONG).show();
        } else {
            if (btAdapter.isEnabled()) {
            } else {
                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBtIntent, 1);
            }
        }
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

    private void cambiarRight(){
        //creamos un intent que hace referencia al segundo activity
        Intent intent = new Intent(Main2Activity.this, Main4Activity.class);
        intent.putExtra("add", address);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
    private void cambiarLeft(){
        //creamos un intent que hace referencia al segundo activity
        Intent intent = new Intent(Main2Activity.this, MainActivity.class);
        intent.putExtra("add", address);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
    private void cambiarMenu(){
        //creamos un intent que hace referencia al segundo activity
        Intent intent = new Intent(Main2Activity.this, Main3Activity.class);
        intent.putExtra("add", address);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
    private void setTypeface(){
        type = Typeface.createFromAsset(getAssets(),"fonts/DS-DIGI.TTF");
        fuente = Typeface.createFromAsset(getAssets(), "fonts/GeosansLight.ttf");;
        title.setTypeface(fuente);
        title2.setTypeface(fuente);
        s0.setTypeface(fuente);
        s1.setTypeface(fuente);
        s2.setTypeface(fuente);
        s3.setTypeface(fuente);
        s4.setTypeface(fuente);
        s5.setTypeface(fuente);
        s6.setTypeface(fuente);
        s7.setTypeface(fuente);
        s8.setTypeface(fuente);
        sb0.setTypeface(fuente);
        sb1.setTypeface(fuente);
        sb2.setTypeface(fuente);
        sb3.setTypeface(fuente);
        sb4.setTypeface(fuente);
        sb5.setTypeface(fuente);
        sb6.setTypeface(fuente);
        sb7.setTypeface(fuente);
        sb8.setTypeface(fuente);
        clock.setTypeface(type);
    }

    private void referencias(){
        right = (ImageButton) findViewById(R.id.cambioDe);
        left = (ImageButton) findViewById(R.id.cambioIz);
        menu = (ImageButton) findViewById(R.id.Menu);
        clock =(TextClock) findViewById(R.id.textClock1);
        title = (TextView) findViewById(R.id.title);
        title2 = (TextView) findViewById(R.id.title2);
        s0 = (TextView) findViewById(R.id.s0);
        s1 = (TextView) findViewById(R.id.s1);
        s2 = (TextView) findViewById(R.id.s2);
        s3 = (TextView) findViewById(R.id.s3);;
        s4 = (TextView) findViewById(R.id.s4);
        s5 = (TextView) findViewById(R.id.s5);
        s6 = (TextView) findViewById(R.id.s6);
        s7 = (TextView) findViewById(R.id.s7);
        s8 = (TextView) findViewById(R.id.s8);
        sb0 = (TextView) findViewById(R.id.bs0);
        sb1 = (TextView) findViewById(R.id.bs1);
        sb2 = (TextView) findViewById(R.id.bs2);
        sb3 = (TextView) findViewById(R.id.bs3);
        sb4 = (TextView) findViewById(R.id.bs4);
        sb5 = (TextView) findViewById(R.id.bs5);
        sb6 = (TextView) findViewById(R.id.bs6);
        sb7 = (TextView) findViewById(R.id.bs7);
        sb8 = (TextView) findViewById(R.id.bs8);
    }


    private void listeners(){
        right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cambiarRight();
            }
        });
        left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cambiarLeft();
            }
        });
        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cambiarMenu();
            }
        });
    }

    //create new class for connect thread
    private class ConnectedThread extends Thread {
        private final InputStream mmInStream;
        private final OutputStream mmOutStream;

        //creation of the connect thread
        public ConnectedThread(BluetoothSocket socket) {
            InputStream tmpIn = null;
            OutputStream tmpOut = null;
            try {
                //Create I/O streams for connection
                tmpIn = socket.getInputStream();
                tmpOut = socket.getOutputStream();
            } catch (IOException e) { }

            mmInStream = tmpIn;
            mmOutStream = tmpOut;
        }

        public void run() {
            byte[] buffer = new byte[256];
            int bytes;

            // Keep looping to listen for received messages
            while (true) {
                try {
                    bytes = mmInStream.read(buffer);            //read bytes from input buffer
                    String readMessage = new String(buffer, 0, bytes);
                    // Send the obtained bytes to the UI Activity via handler
                    bluetoothIn.obtainMessage(handlerState, bytes, -1, readMessage).sendToTarget();
                } catch (IOException e) {
                    break;
                }
            }
        }
        //write method
        public void write(String input) {
            byte[] msgBuffer = input.getBytes();           //converts entered String into bytes
            try {
                mmOutStream.write(msgBuffer);                //write bytes over BT connection via outstream
            } catch (IOException e) {
                //if you cannot write, close the application
                Toast.makeText(getBaseContext(), "Connection Failure", Toast.LENGTH_LONG).show();
                finish();
            }
        }
    }
}
