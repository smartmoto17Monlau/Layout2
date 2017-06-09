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

    //variables globales
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

    // SPP UUID service - Este UUID deberia funcionar en la mayoria de dispositivos
    private static final UUID BTMODULEUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    // String de la  MAC address
    private static String address;

    //variables globales de sensores
    public  static String s0, s1, s2, s3, s4, s5, s6, s7, s8, s9, s10, s11, s12, s13, s14, s15, s16;
    public static float max = 0;
    public static  boolean isbtOn = false;

    //constructor de Bluetooth
    public Bluetooth(String address, Context mContext) throws SQLException, ClassNotFoundException {
        this.address = address;
        this.mContext = mContext;

        //iniciamos bluetooth socket y el handler
        init();

        //inicializamos motionsensors y empezamos a medir
        mtsensors = new motionsensors();
        StartSensor();

        //inicializamos LocationFollow y empezamos a medir
        LocationFollow loc = new LocationFollow(mContext);
        //inicializamos la clase que gestiona la BBDD y la iniciamos
        try{
            db = new bbdd();
            db.start();
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    //constructor vacio
    public Bluetooth(){
    }

    //metodo que empieza a registrar el valor de los sensores de la clase motionsensors
    private void StartSensor(){
        sensorManager = (SensorManager) mContext.getSystemService(SENSOR_SERVICE);
        //usamos el sensor de tipo Rotation Vector
        gyro = sensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR);
    }

    //metodo que inicaliza la conexion Bt y crea el handler que gestiona la recepcion de los datos
    private void init(){
        //declaramos un handler para que gestione el guardado de los valores recibidos en
        //su correspondiente variable
        bluetoothIn = new Handler() {
            public void handleMessage(android.os.Message msg) {
                if (msg.what == handlerState) {                                         //si el mensage es lo que queremos
                    String readMessage = (String) msg.obj;                              // msg.arg1 = bytes del thread conectado
                    recDataString.append(readMessage);                                  //hacemos append hasta que encuentra ~
                    int endOfLineIndex = recDataString.indexOf("~");                    // determinamos el endOfLine
                    if (endOfLineIndex > 0) {                                           // nos aseguramos que recibimos datos antes del ~
                        String dataInPrint = recDataString.substring(0, endOfLineIndex);// extraemos string
                        int dataLength = dataInPrint.length();                          //guardamos el tamaño de los datos recibidos

                        //variables intermedias que almacenaras temporalmente el valor recibido
                        //que corresponde a cada variable
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

                        if (recDataString.charAt(0) == '#')  //si empieza por # sabemos que es lo que estamos buscando
                        {
                            int currentSensor = 0;  //creamos un cursor
                            arrayLetras = dataInPrint.toCharArray(); //guardamos todos los chars del mesage recibido en un array de chars
                            currentSensorValue = new ArrayList<>(); //arraylist que guarda temporalment los  chars de cada sensor

                            for(int i = 0 ; i < dataLength;i++){ //bucle que itera el array de chars
                                if(arrayLetras[i] == '#'){       //si el char es # no hacemos nada

                                }else if(arrayLetras[i] == '+'){ //si el char es un +, significa que hemos llegado al final del valor
                                    if(currentSensor == 0){      //y guardamos los chars que estan el arraylist temporal en su variable correspontiente
                                        sensor0 = sacarString(currentSensorValue); //generamos un String a partir del arraylist que almacena los datos temporales
                                        currentSensorValue.clear(); //borramos los datos temporales
                                        currentSensor++; //aumentamos el cursor de el sensor para que se guarde en la siguiente variablke
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
                                        currentSensor++;
                                    }else if (currentSensor == 8){
                                        sensor8 = sacarString(currentSensorValue);
                                        currentSensorValue.clear();
                                        currentSensor++;
                                    }else if (currentSensor == 9){
                                        sensor9 = sacarString(currentSensorValue);
                                        currentSensorValue.clear();
                                        currentSensor++;
                                    }else if (currentSensor == 10){
                                        sensor10 = sacarString(currentSensorValue);
                                        currentSensorValue.clear();
                                        currentSensor++;
                                    }else if (currentSensor == 11){
                                        sensor11 = sacarString(currentSensorValue);
                                        currentSensorValue.clear();
                                        currentSensor++;
                                    }else if (currentSensor == 12){
                                        sensor12 = sacarString(currentSensorValue);
                                        currentSensorValue.clear();
                                        currentSensor++;
                                    }else if (currentSensor == 13){
                                        sensor13 = sacarString(currentSensorValue);
                                        currentSensorValue.clear();
                                        currentSensor++;
                                    }else if (currentSensor == 14){
                                        sensor14 = sacarString(currentSensorValue);
                                        currentSensorValue.clear();
                                        currentSensor++;
                                    }else if (currentSensor == 15){
                                        sensor15 = sacarString(currentSensorValue);
                                        currentSensorValue.clear();
                                        currentSensor++;
                                    }else if (currentSensor == 16){
                                        sensor16 = sacarString(currentSensorValue);
                                        currentSensorValue.clear();
                                        // como hemos llegado a la ultima variable, volvemos a empezar el bucle de guardado
                                        currentSensor = 0;
                                    }
                                }else if(arrayLetras[i] == '~'){ //si el char es ~, finalizamos el bucle d eguardado
                                    currentSensor = 0;
                                    break;
                                }else{
                                    // si ninguno de los casos anteriores se cumple, el char es una letra normal
                                    // y la añadimos al arraylist de chars temprales
                                    currentSensorValue.add(String.valueOf(arrayLetras[i]));
                                }
                            }
                            //guardamos los valores que hemos recibido en esta trama en las variables globales del programa
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
                        recDataString.delete(0, recDataString.length());   //borramos los datos que hemos recibido
                    }
                }
            }
        };

        btAdapter = BluetoothAdapter.getDefaultAdapter(); // get Bluetooth adapter
        //creamos device y le asignamos una MAC adress
        device = btAdapter.getRemoteDevice(address);

        try { //creamos el BluetoothSocket a partir del device
            btSocket = createBluetoothSocket(device);
        } catch (IOException e) {
        }

        // Establecemos conexion con elbluetooth socket
        try {
            btSocket.connect();
        } catch (IOException e) {
            try {
                //si no podemos conectar, cerramos el socket
                btSocket.close();
            } catch (IOException e2){
            }
        }
        getInputSocket(btSocket); //accedemos al input socket del socket que hemos creado

    }

    //metodo que recibe un arraylist y devuelve un string con los chars que tenia almacenados
    private String sacarString(ArrayList<String> arraylist){
        String texto = "";
        for (String letra : arraylist) {
            texto += letra;
        }
        return texto;
    }

    //metodo que crea el bluetooth socket al recivir el device
    private BluetoothSocket createBluetoothSocket(BluetoothDevice device) throws IOException {

        return  device.createRfcommSocketToServiceRecord(BTMODULEUUID);
        //crea una conexion segura con el BT usando el UUID
    }

    //inicio del run
    public void run(){
        //creamos un array de bytes para hacer de buffer
        byte[] buffer = new byte[256];
        int bytes;

        //registramos los sensores de motionsensors
        sensorManager.registerListener(mtsensors, gyro, SensorManager.SENSOR_DELAY_UI);

        // Bucle infinito que va recibiendo los mensages entrantes
        while (true) {
            try {
                bytes = mmInStream.read(buffer);   //leemos los bytes del input buffer
                String readMessage = new String(buffer, 0, bytes);
                // Enviamos los bytes obtenidos a la UI usando un handler
                bluetoothIn.obtainMessage(handlerState, bytes, -1, readMessage).sendToTarget();
                //Log.d("Datos Arduino","listado de datos"+s0+ " "+s1+" "+s2+" "+s3+" "+s4+ " "+ s5+" "+s6+ " "+ s7+ " "+ s8+ " "+ s9+ " "+ s10+ " "+ s11+ " "+s12+ " "+s13+ " "+s14+ " "+s15+" "+s16);
            } catch (IOException e) {
                break;
            }
        }
    }

    //metodo que
    private void getInputSocket(BluetoothSocket socket){
        InputStream tmpIn = null;
        try {
            //Creamos I/O stream para las conexiones
            tmpIn = socket.getInputStream();
        } catch (IOException e) { }
        //guardamos el inputStream obtenido en una variable global
        mmInStream = tmpIn;
    }
}
