package com.example.user.layout.sensors;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;
import android.view.Surface;

/**
 * Created by jordimasmer on 08/03/2017.
 */
//implementamos SensorEventListener, para poder extraer la informacion de los sensores
public class motionsensors implements SensorEventListener {

    //variables globales estaticas donde se guardan los datos cada vez que se actualizan
    public static float azimuth, pitch, roll;

    @Override //cada vez que el valor del sensor cambia ejecutamos lo siguiente
    public void onSensorChanged(SensorEvent sensorEvent) {
        //ejecutamos un metodo para actualizar  datos de inclinacion
        //le pasamos values del sensor event
        updateOrientation(sensorEvent.values);
        //Log.d("Datos motionsensor",""+azimuth+" "+pitch+" "+roll);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }


    private void updateOrientation(float[] rotationVector) {
        //creamos una nueva matriz de floats para guardar los datos
        //del sensor para calcular el angulo posteriomente
        float[] rotationMatrix = new float[9];
        //llenamos el array con el vector que recibimos por parametro
        SensorManager.getRotationMatrixFromVector(rotationMatrix, rotationVector);
        //guardamos los ejes que nos interesan en variables
        final int worldAxisForDeviceAxisX;
        final int worldAxisForDeviceAxisY;
        worldAxisForDeviceAxisX = SensorManager.AXIS_X;
        worldAxisForDeviceAxisY = SensorManager.AXIS_Z;
        //creamos otro vector para almacenar los datos ajustados
        float[] adjustedRotationMatrix = new float[9];
        //hacemos un remap de los valores de rotationMatrix y lo llenamos en adjustedRotationMatrix
        SensorManager.remapCoordinateSystem(rotationMatrix, worldAxisForDeviceAxisX, worldAxisForDeviceAxisY, adjustedRotationMatrix);

        // Transformamos la matriz de rotacion en Azimuth, pitch y roll
        float[] orientation = new float[3];
        SensorManager.getOrientation(adjustedRotationMatrix, orientation);

        // convertimos de radianes a grados
        roll = orientation[0] * -57;
        pitch = orientation[1] * -57;
        azimuth = orientation[2] * -57;
    }
}
