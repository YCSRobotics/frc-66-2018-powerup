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
	private static TalonSRX omniMasterMotor = Constants.OmniMasterMotor;
	private static TalonSRX omniSlaveMotor = Constants.OmniSlaveMotor;
	
	//Motor Variables
	private double leftMotorCommand = 0.0;
	private double rightMotorCommand = 0.0;
	
	private double driveGain = 1.0;
	private boolean isInverted = false;
	
	private static double targetThrottle = 0.0;
	private static double targetTurn = 0.0;
	
	private static double finesse = 1.0;
	
	public Drivetrain() {
		
		//Ramping Right
		leftMasterMotor.configOpenloopRamp(Constants.DriveRampRate, 5);
		leftSlaveMotor.set(ControlMode.Follower, leftMasterMotor.getDeviceID());
		
		//Ramping Right
		rightMasterMotor.configOpenloopRamp(Constants.DriveRampRate, 5);
		rightSlaveMotor.set(ControlMode.Follower, rightMasterMotor.getDeviceID());
		
		//Ramping Omni
		omniMasterMotor.configOpenloopRamp(Constants.OmniRampRate, 5);
		omniSlaveMotor.configOpenloopRamp(Constants.OmniRampRate, 5);
		
	}
	
	public void updateDrivetrain() {
		
		targetThrottle = getThrottleInput();
		targetTurn = getTurnInput();
		getFinesseInput();
		
		setTargetSpeeds(targetThrottle, targetTurn);
		
		if(!isInverted){
			
			leftMasterMotor.set(ControlMode.PercentOutput, (Constants.LeftDriveReversed ? -1:1) * leftMotorCommand * driveGain);
			rightMasterMotor.set(ControlMode.PercentOutput, (Constants.RightDriveReversed ? -1:1) * rightMotorCommand * driveGain);
			
			omniMasterMotor.set(ControlMode.PercentOutput, getLeftOmniInput());
			omniSlaveMotor.set(ControlMode.PercentOutput, -getRightOmniInput());
			
		} else {
			
			leftMasterMotor.set(ControlMode.PercentOutput, (Constants.RightDriveReversed ? -1:1) * rightMotorCommand * driveGain);
			rightMasterMotor.set(ControlMode.PercentOutput, (Constants.LeftDriveReversed ? -1:1) * leftMotorCommand * driveGain);
		
		}
			
	}
	
	private void setTargetSpeeds(double throttle, double turn){
			
			double t_left;
			double t_right;
			
			//Amp up turn only for large amounts of throttle, already dead zoned
			if(throttle > 0) {
				
				turn = turn * (Constants.TurnGain * Math.abs(throttle));
				
			} else {
				
				turn = turn * Constants.FinesseGain;
				
			}
	
			
			t_left = throttle + turn;
			t_right = throttle - turn;
			
			leftMotorCommand = t_left + skim(t_right);
			rightMotorCommand = t_right + skim(t_left);
			
			leftMotorCommand = Math.max(-finesse, (Math.min(leftMotorCommand, finesse)));
			rightMotorCommand = Math.max(-finesse, (Math.min(rightMotorCommand, finesse)));

	}
	
	private double skim(double v) {
		
		if (v > 1.0) {
			
			return -((v-1.0)*Constants.SkimGain);
			
		} else if (v < -1.0) {
			
			return -((v+1.0)*Constants.SkimGain); 
			
		} else {
			
			return 0;
			
		}
		
	}
	
	private double getThrottleInput() {
		
		double v;
		v = DriveController.getRawAxis(Constants.LeftJoyY);
		//Thumbstick increasing value is toward operator!
		return (Math.abs(v) > Constants.DeadZoneLimit ? -(v) : 0.0);
		
	}

	private double getLeftOmniInput( ) {
		
		double w;
		w = DriveController.getRawAxis(Constants.LeftJoyX);
		
		return (Math.abs(w) > Constants.OmniDeadZoneLimit ? -(w) : 0.0);
		
	}
	
	private double getRightOmniInput( ) {
		
		double z;
		z = DriveController.getRawAxis(Constants.LeftJoyX);
		
		return (Math.abs(z) > Constants.OmniDeadZoneLimit ? -(z) : 0.0);
		
	}
	
	
	
	private double getTurnInput() {
		
		double v;
		v = DriveController.getRawAxis(Constants.RightJoyX);
		
		return(v >= 0 ? (v*v):-(v*v));

	}
	
	
	private void getFinesseInput() {
		
	if (DriveController.getRawButton(Constants.RightBumper)) {
			
		finesse = 0.5;
			
	} else {
		
		finesse = 1.0;
		
	}
		
	
	}
}
