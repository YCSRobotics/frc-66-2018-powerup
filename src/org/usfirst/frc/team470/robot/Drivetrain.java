package org.usfirst.frc.team470.robot;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

import edu.wpi.first.wpilibj.ADXRS450_Gyro;
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
	
	//Gyro
	private static ADXRS450_Gyro gyro = Constants.Gyro;
	private boolean isDriveStraight = false;
	private static boolean isGyroZeroed = false;
	
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
		
		//Calibrate Gyro
		gyro.calibrate();
		
	}
	
	//Main drivetrain movement code
	public void updateDrivetrain() {
		
<<<<<<< HEAD
		isDriveStraight = isStraightButtonPressed();
=======
		targetThrottle = getThrottleInput();
		targetTurn = getTurnInput();
		getFinesseInput();
>>>>>>> e7f7374ad51315549365c4c5085f20676f1ab0a1
		
		//zero the gyro if not zeroed
		if( (isDriveStraight) && (!isGyroZeroed) ) {
	
			zeroGyro();
				
		//drive straight
		} else if(isDriveStraight) {
		
			goStraight();
			isDriveStraight = true;
			
		//teleop as normal
		} else {
			
			//Do regular teleop control
			targetThrottle = getThrottleInput();
			targetTurn = getTurnInput();
			isGyroZeroed = false;
			
		}
	
		setTargetSpeeds(targetThrottle, targetTurn);
		
		if(!isInverted){
			
			leftMasterMotor.set(ControlMode.PercentOutput, (Constants.LeftDriveReversed ? -1:1) * leftMotorCommand * driveGain);
			rightMasterMotor.set(ControlMode.PercentOutput, (Constants.RightDriveReversed ? -1:1) * rightMotorCommand * driveGain);
			
<<<<<<< HEAD
			if(isStraightButtonPressed()) {
				
				omniMasterMotor.set(ControlMode.PercentOutput, 0);
				omniSlaveMotor.set(ControlMode.PercentOutput, 0);
				
			} else {
				
				omniMasterMotor.set(ControlMode.PercentOutput, getLeftOmniInput());
				omniSlaveMotor.set(ControlMode.PercentOutput, -getRightOmniInput());
				
			}
=======
			omniMasterMotor.set(ControlMode.PercentOutput, getLeftOmniInput());
			omniSlaveMotor.set(ControlMode.PercentOutput, -getRightOmniInput());
>>>>>>> e7f7374ad51315549365c4c5085f20676f1ab0a1
			
		} else {
			
			leftMasterMotor.set(ControlMode.PercentOutput, (Constants.RightDriveReversed ? -1:1) * rightMotorCommand * driveGain);
			rightMasterMotor.set(ControlMode.PercentOutput, (Constants.LeftDriveReversed ? -1:1) * leftMotorCommand * driveGain);
		
		}
			
	}
	
	//compute turn gain
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
			
<<<<<<< HEAD
			leftMotorCommand = Math.max(-0.5, (Math.min(leftMotorCommand, 0.5)));
			rightMotorCommand = Math.max(-0.5, (Math.min(rightMotorCommand, 0.5)));
=======
			leftMotorCommand = Math.max(-finesse, (Math.min(leftMotorCommand, finesse)));
			rightMotorCommand = Math.max(-finesse, (Math.min(rightMotorCommand, finesse)));
>>>>>>> e7f7374ad51315549365c4c5085f20676f1ab0a1

	}
	
	//normalize the acceleration curve
	private double skim(double v) {
		
		if (v > 1.0) {
			
			return -((v-1.0)*Constants.SkimGain);
			
		} else if (v < -1.0) {
			
			return -((v+1.0)*Constants.SkimGain); 
			
		} else {
			
			return 0;
			
		}
		
	}
	
	//normal throttle input
	private double getThrottleInput() {
		
		double v;
		v = DriveController.getRawAxis(Constants.LeftJoyY);
		//Thumbstick increasing value is toward operator!
		return (Math.abs(v) > Constants.DeadZoneLimit ? -(v) : 0.0);
		
	}
	
	//calculate left omni throttle
	private double getLeftOmniInput( ) {
		
		double w;
		w = DriveController.getRawAxis(Constants.LeftJoyX);
		
		return (Math.abs(w) > Constants.OmniDeadZoneLimit ? -(w) : 0.0);
		
	}
	
	//Calculate right omni throttle
	private double getRightOmniInput( ) {
		
		double z;
		z = DriveController.getRawAxis(Constants.LeftJoyX);
		
		return (Math.abs(z) > Constants.OmniDeadZoneLimit ? -(z) : 0.0);
		
	}
	
	//Calculate turn input
	private double getTurnInput() {
		
		double v;
		v = DriveController.getRawAxis(Constants.RightJoyX);
		
		return(v >= 0 ? (v*v):-(v*v));

	}
	
	//Math to calculate going straight
	private void goStraight() {
		
		targetThrottle = getThrottleInput();		
		targetTurn = -1 * (gyro.getAngle() * Constants.GyroGain);
		
	}
	
	//Is the go straight button pressed?
	private boolean isStraightButtonPressed() {
		
		return(DriveController.getRawButton(Constants.ButtonY));
		
	}

	//zero gyro
	public static void zeroGyro() {
		
		gyro.reset();
		isGyroZeroed = true;
		
	}
	
	private void getFinesseInput() {
		
	if (DriveController.getRawButton(Constants.RightBumper)) {
			
		finesse = 0.5;
			
	} else {
		
		finesse = 1.0;
		
	}
		
	
	}
}
