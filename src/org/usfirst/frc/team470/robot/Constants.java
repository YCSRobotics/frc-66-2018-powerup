package org.usfirst.frc.team470.robot;

import com.ctre.phoenix.motorcontrol.can.TalonSRX;

/* Only variables that have a fixed (never changing) value should be put here */

public class Constants {
	
	//Motor Configuration
	public static final TalonSRX LeftMasterMotor  = new TalonSRX(0);
	public static final TalonSRX LeftSlaveMotor   = new TalonSRX(1);
	public static final TalonSRX RightMasterMotor = new TalonSRX(2);
	public static final TalonSRX RightSlaveMotor  = new TalonSRX(3);
	
	public static final boolean EnableDriveBrake = false;
	
	//This is the min ramp rate per CTRE documentation
	public static final double MinTalonRampRate = 1.173; 
	public static final boolean RampEnabled = true;
	
	//This is the time to ramp from 0V to +/-12V in sec
	public static final double DriveRampTime = 0.25; 
	public static final double DriveRampRate = (0.5);
	
	//Controllers
	public static final int DriveController = 0;
	public static final int OperatorController = 0;
	
	//Controller Mapping
	public static final int LeftJoyX = 0;
	public static final int LeftJoyY = 1;
	public static final int RightJoyX = 4;
	public static final int RightJoyY = 5;
	public static final int LeftTrigger = 2;
	public static final int RightTrigger = 3;
	public static final int ButtonA = 1;
	public static final int ButtonB = 2;
	public static final int ButtonX = 3;
	public static final int ButtonY = 4;
	public static final int LeftBummper = 5;
	public static final int RightBummper = 6;
	
}