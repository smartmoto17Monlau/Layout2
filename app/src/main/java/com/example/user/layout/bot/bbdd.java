package com.example.user.layout.bot;


import android.util.Log;

import com.example.user.layout.sensors.Bluetooth;
import com.example.user.layout.sensors.LocationFollow;
import com.example.user.layout.sensors.motionsensors;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class bbdd  extends Thread{

    //variables globales
    static Connection c = null;
    static Statement stmt = null;


    public void run(){
        //iniciamos una conexion con nuestra BBDD
        try {
            connect();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        //una vez conectados, generamos bucle infito que ejecuta un insert cada 10s
        while(true){
           try {
               Thread.sleep(10000);
               insert();
           }catch (InterruptedException e) {
               e.printStackTrace();
           } catch (SQLException e) {
               e.printStackTrace();
           }
        }
    }

    //metodo que se encarga de conectar con la BBDD
    public void connect() throws SQLException, ClassNotFoundException {
        Class.forName("org.postgresql.Driver");
        c = DriverManager.getConnection("jdbc:postgresql://smcdb.ccj5m01jpnyw.eu-west-1.rds.amazonaws.com:5432/smcdata", "monlau", "monlau17");
        //c = DriverManager.getConnection("jdbc:postgresql://172.31.128.103:5432/smc", "monlau", "monlau17");

        if (c != null) {
            System.out.println("Conexion a la bbdd realizada.");
        } else {
            System.out.println("Failed to make connection!");
        }
    }

    //metodo que se encarga de ejecutar la sentencia SQL en la BBDD
    private static void insert() throws SQLException{
        c.setAutoCommit(false);
        stmt = c.createStatement();
        String date = getCurrentDate();
        System.out.println(date);
        String query = "INSERT INTO public.data("
                + "motoid, speed, maxspeed, avgspeed, battery, motortemp, batterytemp, latitude, longitude, frontalrotation, siderotation, glp, propane, naturalgas, co, butane, hydrogen, methane, co2, amonia, rad, temp, hum, height, pa, fecha) "
                + "VALUES (1, "+Bluetooth.s14+", "+Bluetooth.max+", 60, "+Bluetooth.s13+", "+Bluetooth.s15+", "+Bluetooth.s16+", "+ LocationFollow.latitud+", "+ LocationFollow.longitud+", "+ motionsensors.azimuth+","+motionsensors.pitch+"," +
                " "+Bluetooth.s4+", "+Bluetooth.s5+", "+Bluetooth.s6+", "+Bluetooth.s8+", "+Bluetooth.s7+", "+Bluetooth.s9+", "+Bluetooth.s10+", "+Bluetooth.s11+", 0, "+Bluetooth.s12+", "+Bluetooth.s0+", "+Bluetooth.s1+", "+Bluetooth.s3+", "+Bluetooth.s2+", '"+date+"');";
        stmt.executeUpdate(query);
        stmt.close();
        c.commit();
    }

    //metodo que devuelve un timestamp de la hora y dia actual
    private static String getCurrentDate(){
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd hh:mm:ss");
        String formattedDate = df.format(c.getTime());
        return  formattedDate;
    }
}
