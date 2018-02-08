package org.usfirst.frc.team470.robot;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class DashboardState {
	
	SmartDashboard dash = new SmartDashboard();
	SensorData sensors = new SensorData();
	
	
	public void updateSmartDashboard() {
		
		SmartDashboard.putNumber("Left Wheel Distance", sensors.getLeftWheelDistance());
		SmartDashboard.putNumber("Right Wheel Distance", sensors.getRightWheelDistance());
		
	}
	
}
