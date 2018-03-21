package org.usfirst.frc.team470.robot;

import org.usfirst.frc.team470.robot.Constants;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.NeutralMode;
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
	private static TalonSRX topSlideMotor = Constants.SlideTopMotor;
	private static TalonSRX bottomSlideMotor = Constants.SlideBottomMotor;
	
	//Motor Variables
	private double leftMotorCommand = 0.0;
	private double rightMotorCommand = 0.0;
	private double bottomSlideMotorCommand = 0.0;
	private double topSlideMotorCommand = 0.0;
	
	//Gyro
	private static ADXRS450_Gyro gyro = Constants.Gyro;
	private boolean isDriveStraight = false;
	private static boolean isGyroZeroed = false;
	
	private double driveGain = 1.0;
	private boolean isInverted = false;
	
	public static double targetYThrottle = 0.0;//Fwd/Rwd Throttle
	public static double targetXThrottle = 0.0;//Left/Right Throttle
	public static double targetTurn = 0.0;
	
	private static double finesse = 1.0;
	
	public static double yTargetDistance = 0.0;
	public static double xTargetDistance = 0.0;
	public static double targetAngle = 0.0;
	
	public static boolean isMovingYDistance = false;
	public static boolean isMovingXDistance = false;
	public static boolean isTurning = false;
	
	//Sensors
	static SensorData sensors = new SensorData();
	
	public Drivetrain() {
		
		//Ramping Left
		leftMasterMotor.configOpenloopRamp(Constants.DriveRampRate, 5);
		leftSlaveMotor.set(ControlMode.Follower, leftMasterMotor.getDeviceID());
		
		//Ramping Right
		rightMasterMotor.configOpenloopRamp(Constants.DriveRampRate, 5);
		rightSlaveMotor.set(ControlMode.Follower, rightMasterMotor.getDeviceID());
		
		//Ramping slide
		bottomSlideMotor.configOpenloopRamp(Constants.SlideRampRate, 5);
		topSlideMotor.configOpenloopRamp(Constants.SlideRampRate, 5);
		
		//Calibrate Gyro
		gyro.calibrate();
		
		//Configure strafe encoders
		topSlideMotor.setSensorPhase(true);
		bottomSlideMotor.setSensorPhase(true);
		
		bottomSlideMotor.setSelectedSensorPosition(0, 0, 0);
		topSlideMotor.setSelectedSensorPosition(0, 0, 0);
	}
	
	//Main drivetrain movement code
	public void updateDrivetrainTeleop() {
		
		System.out.println("Cube Distance" + PiMath.getCubeDistance());
		System.out.println("Cube Angle" + PiMath.angleToCube());

		isDriveStraight = isStraightButtonPressed();

		targetYThrottle = getThrottleInput();
		targetXThrottle = getSlideInput();
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
			targetYThrottle = getThrottleInput();
			targetTurn = getTurnInput();
			isGyroZeroed = false;			
		}
	
		setTargetSpeeds(targetYThrottle, targetXThrottle, targetTurn);
		
		if(!isInverted){
			
			leftMasterMotor.set(ControlMode.PercentOutput, (Constants.LeftDriveReversed ? -1:1) * leftMotorCommand * driveGain);
			rightMasterMotor.set(ControlMode.PercentOutput, (Constants.RightDriveReversed ? -1:1) * rightMotorCommand * driveGain);
			
			//update slides
			bottomSlideMotor.set(ControlMode.PercentOutput, bottomSlideMotorCommand);
			topSlideMotor.set(ControlMode.PercentOutput, -topSlideMotorCommand);

			
		} else {
			
			leftMasterMotor.set(ControlMode.PercentOutput, (Constants.RightDriveReversed ? -1:1) * rightMotorCommand * driveGain);
			rightMasterMotor.set(ControlMode.PercentOutput, (Constants.LeftDriveReversed ? -1:1) * leftMotorCommand * driveGain);
		
		}
			
	}
	
	public void updateDrivetrainAuton(){
		double y_distance_error;
		double x_distance_error;
		
		if((isMovingYDistance)||
		   (isMovingXDistance)){
			//Move distance without tracking vision target
			y_distance_error = yTargetDistance - sensors.getMainAvgDistance();
			x_distance_error = xTargetDistance - getStrafeAverageDistance();
			
			if(Math.abs(y_distance_error) <= Constants.TargetDistanceThreshold){
				//Robot has reached target
				targetYThrottle = 0.0;
				isMovingYDistance = false;
			}
		
			if(Math.abs(x_distance_error) <= Constants.TargetDistanceThreshold){
				//Robot has reached target
				targetXThrottle = 0.0;
				isMovingXDistance = false;
			}

			targetTurn = -1*(gyro.getAngle()*Constants.GyroGain);
		}
		else if(isTurning)
		{			
			if(Math.abs(gyro.getAngle()) >= targetAngle)
			{
				targetXThrottle = 0.0;
				targetXThrottle = 0.0;
				targetTurn = 0.0;
				isTurning = false;
			}
			else
			{
				//Do Nothing while turning
			}
		}
		else{
			//No Auton move in progress
			targetYThrottle = 0.0;
			targetXThrottle = 0.0;
			targetTurn = 0.0;
		}
		
		setTargetSpeeds(targetYThrottle, targetXThrottle, targetTurn);
		
		leftMasterMotor.set(ControlMode.PercentOutput, (Constants.LeftDriveReversed ? -1:1) * leftMotorCommand * driveGain);
		rightMasterMotor.set(ControlMode.PercentOutput, (Constants.RightDriveReversed ? -1:1) * rightMotorCommand * driveGain);
		
		//update slides
		bottomSlideMotor.set(ControlMode.PercentOutput, bottomSlideMotorCommand);
		topSlideMotor.set(ControlMode.PercentOutput, -topSlideMotorCommand);
	}
	
	//compute turn gain
	private void setTargetSpeeds(double ythrottle, double xthrottle, double turn){
			
			double t_left;
			double t_right;
			
			//Amp up turn only for large amounts of throttle, already dead zoned
			if(ythrottle > 0) {
				
				turn = turn * (Constants.TurnGain * Math.abs(ythrottle));
				
			} else {
				
				turn = turn * Constants.FinesseGain;
				
			}
	
			
			t_left = ythrottle + turn;
			t_right = ythrottle - turn;
			
			//apply the turn to throttle
			leftMotorCommand = t_left + skim(t_right);
			rightMotorCommand = t_right + skim(t_left);

			//limit the negative and positive motor outputs to finesse
			leftMotorCommand = Math.max(-finesse, (Math.min(leftMotorCommand, finesse)));
			rightMotorCommand = Math.max(-finesse, (Math.min(rightMotorCommand, finesse)));
			
			bottomSlideMotorCommand = Math.max(-Constants.SlidePowerLimit, (Math.min(xthrottle, Constants.SlidePowerLimit)));
			topSlideMotorCommand = Math.max(-Constants.SlidePowerLimit, (Math.min(xthrottle, Constants.SlidePowerLimit)));

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
	
	//calculate left slide throttle
	private double getSlideInput() {
		
		double w;
		w = DriveController.getRawAxis(Constants.LeftJoyX);
		
		return (Math.abs(w) > Constants.SlideDeadZoneLimit ? -(w) : 0.0);
		
	}
		
	//Calculate turn input
	private double getTurnInput() {
		
		double v;
		v = DriveController.getRawAxis(Constants.RightJoyX);
		
		return(v >= 0 ? (v*v):-(v*v));

	}
	
	//is invert button pressed or has been pressed, then toggle invert
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
		
		targetYThrottle = getThrottleInput();		
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

	
	//Finese Mode, Slows Drivetrain down.
	private void getFinesseInput() {
		
		if (!DriveController.getRawButton(Constants.RightBumper)) {
				
			finesse = Constants.Finesse;
				
		} else {
			finesse = 1.0;
		}
		
	}
	
	//gets the bottom and top slide distance. 
	/*public static double getBottomSlideDistance() {
		
		return bottomSlideMotor.getSelectedSensorPosition(0) * Constants.SlideEncoderInchPerRotation;
	
	}
	
	public static double getTopSlideDistance() {
		
		topSlideMotor.setSensorPhase(true);
		return topSlideMotor.getSelectedSensorPosition(0) * 
				Constants.SlideEncoderInchPerRotation;
		
	}*/
	
	public static void setMoveDistance(double yDistance, double yThrottle, double xDistance, double xThrottle){
		
		sensors.resetEncoder();
		topSlideMotor.setSelectedSensorPosition(0, 0, 0);
		bottomSlideMotor.setSelectedSensorPosition(0, 0, 0);
		
		yTargetDistance = yDistance;
		xTargetDistance = xDistance;
		
		enableDrivetrainDynamicBraking(true);
		
		if(Math.abs(yDistance) > Constants.TargetDistanceThreshold)
		{
    		isMovingYDistance = true;
    		targetYThrottle = yThrottle;
		}
    	else
    	{
    		isMovingYDistance = false;
    		targetYThrottle = 0.0;
    	}
		
		if(Math.abs(xDistance) > Constants.TargetDistanceThreshold)
		{
    		isMovingXDistance = true;
    		targetXThrottle = xThrottle;
		}
    	else
    	{
    		isMovingXDistance = false;
    		targetXThrottle = 0.0;
    	}
	}
	
	public static void setTurnToTarget(double turn, double angle){
		isTurning = true;
		targetAngle = Math.abs(angle);//angle must always be positive 
		targetTurn = turn;//Turn power
	}
	
	public static void enableDrivetrainDynamicBraking(boolean enable){
		if(enable){
			leftMasterMotor.setNeutralMode(NeutralMode.Brake);
			leftSlaveMotor.setNeutralMode(NeutralMode.Brake);
			rightMasterMotor.setNeutralMode(NeutralMode.Brake);
			rightSlaveMotor.setNeutralMode(NeutralMode.Brake);
			topSlideMotor.setNeutralMode(NeutralMode.Brake);
			topSlideMotor.setNeutralMode(NeutralMode.Brake);
		}
		else{
			leftMasterMotor.setNeutralMode(NeutralMode.Coast);
			leftSlaveMotor.setNeutralMode(NeutralMode.Coast);
			rightMasterMotor.setNeutralMode(NeutralMode.Coast);
			rightSlaveMotor.setNeutralMode(NeutralMode.Coast);
			topSlideMotor.setNeutralMode(NeutralMode.Coast);
			topSlideMotor.setNeutralMode(NeutralMode.Coast);
		}	
	}
	
	public static double getStrafeAverageDistance(){
		double topdistance = getTopMotorDistance();
		double bottomdistance = getBottomMotorDistance();
		
		return((topdistance+bottomdistance)/2);
	}
	
	public static double getTopMotorDistance(){
		return -1*((topSlideMotor.getSelectedSensorPosition(0)/4096)*Constants.DistancePerRev);
	}
	
	public static double getBottomMotorDistance(){
		return (bottomSlideMotor.getSelectedSensorPosition(0)/4096)*Constants.DistancePerRev;
	}
}
