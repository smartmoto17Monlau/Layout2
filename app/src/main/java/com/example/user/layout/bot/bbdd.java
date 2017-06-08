package com.example.user.layout.bot;


import android.util.Log;

import com.example.user.layout.sensors.Bluetooth;
import com.example.user.layout.sensors.LocationFollow;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class bbdd  extends Thread{

    static Connection c = null;
    static Statement stmt = null;


    public void run(){
        try {
            connect();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }


        while(true){
           try {
               Thread.sleep(10000);
               insert();
               Log.i("aaa", "10s");
           }catch (InterruptedException e) {
               e.printStackTrace();
           } catch (SQLException e) {
               e.printStackTrace();
           }
        }
    }

    public void connect() throws SQLException, ClassNotFoundException {
        Class.forName("org.postgresql.Driver");
        c = DriverManager.getConnection("jdbc:postgresql://smcdb.ccj5m01jpnyw.eu-west-1.rds.amazonaws.com:5432/smcdata", "monlau", "monlau17");
        //c = DriverManager.getConnection("jdbc:postgresql://172.31.128.103:5432/smc", "monlau", "monlau17");
        if (c != null) {
            System.out.println("Conexion a la bbdd realizada.");
            //insert();
            //System.out.println("Fila insertada.");

        } else {
            System.out.println("Failed to make connection!");
        }
    }

    private static void insert() throws SQLException{
        c.setAutoCommit(false);
        stmt = c.createStatement();
        String date = getCurrentDate();
        System.out.println(date);
        String query = "INSERT INTO public.data("
                + "motoid, speed, maxspeed, avgspeed, battery, motortemp, batterytemp, latitude, longitude, frontalrotation, siderotation, glp, propane, naturalgas, co, butane, hydrogen, methane, co2, amonia, rad, temp, hum, height, pa, fecha) "
                + "VALUES (1, "+Bluetooth.s14+", "+Bluetooth.max+", 60, "+Bluetooth.s13+", "+Bluetooth.s15+", "+Bluetooth.s16+", "+ LocationFollow.latitud+", "+ LocationFollow.longitud+", 15.30, 25.36, 20000, 20, 30, 5, 6, 1, 6, 10, 20, 0.20, 17, 25, 30, 1020, '"+date+"');";//'2016/02/15 13:30:10');";
        stmt.executeUpdate(query);
        stmt.close();
        c.commit();
    }

    private static String getCurrentDate(){
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd hh:mm:ss");
        String formattedDate = df.format(c.getTime());
        return  formattedDate;
    }
}
