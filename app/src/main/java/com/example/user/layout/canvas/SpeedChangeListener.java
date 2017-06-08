package com.example.user.layout.canvas;

//interface que se encarga de actualizar los valores nuevos para los canvas
public interface SpeedChangeListener {
	public void onSpeedChanged(float newSpeedValue);
	public void onBatteryChanged(float newBatValue);
	public void onMotorTempChanged(float newMotorTempValue);
	public void onBatteryTempChanged(float newBatteryTempValue);
}
