package com.example.user.layout;

public interface SpeedChangeListener {
	
	public void onSpeedChanged(float newSpeedValue);
	public void onBatteryChanged(float newBatValue);
	public void onMotorTempChanged(float newMotorTempValue);
	public void onBatteryTempChanged(float newBatteryTempValue);
}
