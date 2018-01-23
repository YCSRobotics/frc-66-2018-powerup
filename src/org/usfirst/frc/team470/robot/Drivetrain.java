package org.usfirst.frc.team470.robot;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import edu.wpi.first.wpilibj.Joystick;

public class Drivetrain {
	
	//Initialize Controllers
	Joystick DriveController = new Joystick(Constants.DriveController);
	Joystick OperatorController = new Joystick(Constants.OperatorController);
	
	//Initialize Motors
	private static TalonSRX leftMasterMotor = Constants.LeftMasterMotor;
	private static TalonSRX leftSlaveMotor = Constants.LeftSlaveMotor;
	private static TalonSRX rightMasterMotor = Constants.RightMasterMotor;
	private static TalonSRX rightSlaveMotor = Constants.RightSlaveMotor;
	
	
	public Drivetrain() {
		
		//Ramping Right
		leftMasterMotor.configOpenloopRamp(Constants.DriveRampRate, 5);
		leftSlaveMotor.set(ControlMode.Follower, leftMasterMotor.getDeviceID());
		
		//Ramping Right
		rightMasterMotor.configOpenloopRamp(Constants.DriveRampRate, 5);
		rightSlaveMotor.set(ControlMode.Follower, rightMasterMotor.getDeviceID());
		
	}
	
	public void updateDrivetrain() {
		
		leftMasterMotor.set(ControlMode.PercentOutput, DriveController.getRawAxis(Constants.LeftJoyY));
		rightMasterMotor.set(ControlMode.PercentOutput, -DriveController.getRawAxis(Constants.RightJoyY));
		
	}
	
}
