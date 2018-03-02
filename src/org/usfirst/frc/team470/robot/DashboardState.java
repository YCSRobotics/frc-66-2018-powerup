package org.usfirst.frc.team470.robot;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class DashboardState {
	
	SmartDashboard dash = new SmartDashboard();
	SensorData sensors = new SensorData();
	
	//all information to update the smartdashboard should be put here
	public void updateSmartDashboard() {
		
		SmartDashboard.putNumber("Left Wheel Distance", sensors.getLeftWheelDistance());
		SmartDashboard.putNumber("Right Wheel Distance", sensors.getRightWheelDistance());
		SmartDashboard.putNumber("Average Wheel Distance", sensors.getMainAvgDistance());
		
		SmartDashboard.putNumber("BottomDistance", Drivetrain.getBottomSlideDistance());
		SmartDashboard.putNumber("TopDistance", Drivetrain.getTopSlideDistance());
		
		SmartDashboard.putNumber("Elevator Position", Elevator.getElevatorPosition());
		SmartDashboard.putNumber("Elevator Target Position", Elevator.commandedPosition);
		
		SmartDashboard.putBoolean("Is Position Zeroed", Elevator.isElevatorZeroed);
		SmartDashboard.putBoolean("Manual Control", Elevator.isManualControl);
		SmartDashboard.putBoolean("Position Control", Elevator.isPositionControl);
		SmartDashboard.putBoolean("Lower Limit Active", Elevator.isLowerLimitPressed());
		SmartDashboard.putBoolean("Forward Limit Active", Elevator.isUpperLimitPressed());
		
	}
	
}
