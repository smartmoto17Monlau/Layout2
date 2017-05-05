package com.example.user.layout;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;
import android.view.Surface;

/**
 * Created by jordimasmer on 08/03/2017.
 */

public class motionsensors implements SensorEventListener {

    public static float azimuth, pitch, roll;

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {

        /*azimuth = sensorEvent.values[0]*100;
        pitch = sensorEvent.values[1]*100;
        roll = sensorEvent.values[2]*100;
        */
        updateOrientation(sensorEvent.values);
        Log.d("Datos motionsensor",""+azimuth+" "+pitch+" "+roll);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }


    private void updateOrientation(float[] rotationVector) {
        float[] rotationMatrix = new float[9];
        SensorManager.getRotationMatrixFromVector(rotationMatrix, rotationVector);

        final int worldAxisForDeviceAxisX;
        final int worldAxisForDeviceAxisY;

        worldAxisForDeviceAxisX = SensorManager.AXIS_X;
        worldAxisForDeviceAxisY = SensorManager.AXIS_Z;

        float[] adjustedRotationMatrix = new float[9];
        SensorManager.remapCoordinateSystem(rotationMatrix, worldAxisForDeviceAxisX, worldAxisForDeviceAxisY, adjustedRotationMatrix);

        // Transform rotation matrix into azimuth/pitch/roll
        float[] orientation = new float[3];
        SensorManager.getOrientation(adjustedRotationMatrix, orientation);

        // Convert radians to degrees
        roll = orientation[0] * -57;
        pitch = orientation[1] * -57;
        azimuth = orientation[2] * -57;
    }
}
