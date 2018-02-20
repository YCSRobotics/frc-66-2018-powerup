package org.usfirst.frc.team470.robot;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

import edu.wpi.first.wpilibj.ADXRS450_Gyro;
import edu.wpi.first.wpilibj.Joystick;

public class Drivetrain {
	
	private static boolean isInvertPressed = false;
	//Initialize Controllers
	Joystick DriveController = new Joystick(Constants.DriveController);
	Joystick OperatorController = new Joystick(Constants.OperatorController);
	
	//Initialize Motors
	private static TalonSRX leftMasterMotor = Constants.LeftMasterMotor;
	private static TalonSRX leftSlaveMotor = Constants.LeftSlaveMotor;
	private static TalonSRX rightMasterMotor = Constants.RightMasterMotor;
	private static TalonSRX rightSlaveMotor = Constants.RightSlaveMotor;
	private static TalonSRX topOmniMotor = Constants.OmniTopMotor;
	private static TalonSRX bottomOmniMotor = Constants.OmniBottomMotor;
	private static TalonSRX liftMotor = Constants.LiftMotor;
	
	//Motor Variables
	private double leftMotorCommand = 0.0;
	private double rightMotorCommand = 0.0;
	private double bottomOmniMotorCommand = 0.0;
	private double topOmniMotorCommand = 0.0;
	private double liftMotorCommand = 0.0;
	
	//Gyro
	private static ADXRS450_Gyro gyro = Constants.Gyro;
	private boolean isDriveStraight = false;
	private static boolean isGyroZeroed = false;
	
	private double driveGain = 1.0;
	private boolean isInverted = false;
	
	private static double targetThrottle = 0.0;
	private static double targetTurn = 0.0;
	
	private static double finesse = 1.0;
	
	//Sensors
	SensorData sensors = new SensorData();
	
	public Drivetrain() {
		
		//Ramping Left
		leftMasterMotor.configOpenloopRamp(Constants.DriveRampRate, 5);
		leftSlaveMotor.set(ControlMode.Follower, leftMasterMotor.getDeviceID());
		
		//Ramping Right
		rightMasterMotor.configOpenloopRamp(Constants.DriveRampRate, 5);
		rightSlaveMotor.set(ControlMode.Follower, rightMasterMotor.getDeviceID());
		
		//Ramping Lift
		liftMotor.configOpenloopRamp(Constants.DriveRampRate, 5);
		
		//Ramping Omni
		bottomOmniMotor.configOpenloopRamp(Constants.OmniRampRate, 5);
		topOmniMotor.configOpenloopRamp(Constants.OmniRampRate, 5);
		
		//Calibrate Gyro
		gyro.calibrate();
		
	}
	
	//Main drivetrain movement code
	public void updateDrivetrain() {

		isDriveStraight = isStraightButtonPressed();

		targetThrottle = getThrottleInput();
		targetTurn = getTurnInput();
		getFinesseInput();
		setInvert();

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
		
		liftMotor.set(ControlMode.PercentOutput, liftMotorCommand * Constants.LiftMaxSpeed);
		
		if(!isInverted){
			
			leftMasterMotor.set(ControlMode.PercentOutput, (Constants.LeftDriveReversed ? -1:1) * leftMotorCommand * driveGain);
			rightMasterMotor.set(ControlMode.PercentOutput, (Constants.RightDriveReversed ? -1:1) * rightMotorCommand * driveGain);
			
			//update omnis
			bottomOmniMotor.set(ControlMode.PercentOutput, bottomOmniMotorCommand);
			topOmniMotor.set(ControlMode.PercentOutput, -topOmniMotorCommand);

			
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
			
			//apply the turn to throttle
			leftMotorCommand = t_left + skim(t_right);
			rightMotorCommand = t_right + skim(t_left);

			leftMotorCommand = Math.max(-finesse, (Math.min(leftMotorCommand, finesse)));
			rightMotorCommand = Math.max(-finesse, (Math.min(rightMotorCommand, finesse)));
			
			bottomOmniMotorCommand = Math.max(-finesse, (Math.min(getLeftOmniInput(), finesse)));
			topOmniMotorCommand = Math.max(-finesse, (Math.min(getRightOmniInput(), finesse)));
			liftMotorCommand = Math.max(-finesse, (Math.min(getLiftInput(), finesse)));

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
	private double getLeftOmniInput() {
		
		double w;
		w = DriveController.getRawAxis(Constants.LeftJoyX);
		
		return (Math.abs(w) > Constants.OmniDeadZoneLimit ? -(w) : 0.0);
		
	}
	
	//Calculate right omni throttle
	private double getRightOmniInput() {
		
		double z;
		z = DriveController.getRawAxis(Constants.LeftJoyX);
		
		return (Math.abs(z) > Constants.OmniDeadZoneLimit ? -(z) : 0.0);
		
	}
	
	//Calculate right omni throttle
	private double getLiftInput() {
		
		double z;
		z = OperatorController.getRawAxis(Constants.LeftJoyY);
		
		return (Math.abs(z) > Constants.OmniDeadZoneLimit ? -(z) : 0.0);
		
	}	
	
	//Calculate turn input
	private double getTurnInput() {
		
		double v;
		v = DriveController.getRawAxis(Constants.RightJoyX);
		
		return(v >= 0 ? (v*v):-(v*v));

	}
	
	private void setInvert(){
			
			if((DriveController.getRawButton(Constants.SelectButton)) && (!isInvertPressed)) {
						
				isInvertPressed = true;
						
			    if(isInverted) {
			    	
			    	isInverted = false;
			    	
				} else {
					
					isInverted = true;
					
				}
			    
			} else if (!DriveController.getRawButton(Constants.SelectButton)){
				
				isInvertPressed = false;
				
			} else {
				//Do nothing, button is still pressed
			}
			
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
				
			finesse = Constants.Finesse;
				
		} else {
			
			finesse = 0.3;
			
		}
		
	}
	
	public static double getBottomOmniDistance() {
		
		return bottomOmniMotor.getSelectedSensorPosition(0) * Constants.OmniEncoderInchPerRotation;
	
	}
	
	public static double getTopOmniDistance() {
		
		topOmniMotor.setSensorPhase(true);
		return topOmniMotor.getSelectedSensorPosition(0) * Constants.OmniEncoderInchPerRotation;
		
	}
	
}
