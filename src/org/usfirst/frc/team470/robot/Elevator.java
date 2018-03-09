package org.usfirst.frc.team470.robot;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.LimitSwitchNormal;
import com.ctre.phoenix.motorcontrol.LimitSwitchSource;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

import edu.wpi.first.wpilibj.Joystick;

public class Elevator {
	
	//Initialize Controller
	
	Joystick OperatorController = new Joystick(Constants.OperatorController);
	
	//Initialize Motors
	private static TalonSRX elevatorMotor = Constants.ElevatorMotor;
	
	//Elevator Variables
	public double elevatorPosition;
	public static double commandedPosition;
	
	public static boolean isElevatorZeroed = false;
	public static boolean isManualControl = false;
	public static boolean isPositionControl = false;
	
	public Elevator(){
		//Ramping Lift
		elevatorMotor.configOpenloopRamp(0.15, 10);
		
		//Configure Feedback device - arg0 Feedback type, arg1 PID loop, arg2 Timeout in mS
		elevatorMotor.setSensorPhase(true);
		elevatorMotor.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative, 0, 10);
		elevatorMotor.configForwardLimitSwitchSource(LimitSwitchSource.FeedbackConnector, LimitSwitchNormal.NormallyOpen, 0);
		elevatorMotor.configReverseLimitSwitchSource(LimitSwitchSource.FeedbackConnector, LimitSwitchNormal.NormallyOpen, 0);
		
		//Configure PIDF Terms
		elevatorMotor.selectProfileSlot(0, 0);
		elevatorMotor.config_kP(0, .125, 10);
		elevatorMotor.config_kI(0, 0, 10);
		elevatorMotor.config_kD(0, 0, 10);
		elevatorMotor.config_kF(0, 0, 10);
	}
	
	public void initElevator(){
		//Put any initialization routines here
	}
	
	public void updateElevatorDisabled(){
		if((elevatorMotor.getSensorCollection().isRevLimitSwitchClosed()) 
			&& (!isElevatorZeroed)){
			this.zeroElevatorPosition();
		}
	}
	
	public void updateElevatorAuton(){
		
	}
	
	public void updateElevatorTeleop(){
		
		double elevatorInput = getElevatorInput();
		elevatorPosition = getElevatorPosition();
		
		if((elevatorMotor.getSensorCollection().isRevLimitSwitchClosed()) &&
		   (!isElevatorZeroed)){
			this.zeroElevatorPosition();
		}
		
		if(Math.abs(elevatorInput) > 0.0)
		{
			//Elevator is being controlled by the joystick, Manual Control
			isManualControl = true;
			isPositionControl = false;
			elevatorMotor.set(ControlMode.PercentOutput, getElevatorInput());
		}
		else if ((!isElevatorZeroed)||
				 ((elevatorPosition < Constants.ElevatorPosCtrlThreshold)&&
				  (commandedPosition < Constants.ElevatorPosCtrlThreshold))){ 
			/*Joystick released but relative position is unknown or close to lower limit 
			  so can't move or hold relative position*/
			isManualControl = true;
			isPositionControl = false; 
			elevatorMotor.set(ControlMode.PercentOutput, 0.0);
		}
		else if((isManualControl)){
			//Joystick was just released AND elevator is zeroed, so do position control on current position
			isManualControl = false;
			isPositionControl = true;
			this.goToPosition(elevatorPosition);
			//elevatorMotor.set(ControlMode.Position, elevatorPosition);
		}
		else if (OperatorController.getRawButton(Constants.Startbutton)){
			//Move to Low Carry Position
			isManualControl = false;
			isPositionControl = true;
			this.goToPosition(Constants.LowCarryPosition);
		}
		else if (OperatorController.getRawButton(Constants.ButtonX)){
			//Move to Switch Position
			isManualControl = false;
			isPositionControl = true;
			this.goToPosition(Constants.SwitchPosition);
		}
		else if (OperatorController.getRawButton(Constants.ButtonY)){
			//Move to Low Scale Position
			isManualControl = false;
			isPositionControl = true;
			this.goToPosition(Constants.LowScalePosition);
		}
		else if (OperatorController.getRawButton(Constants.ButtonA)){
			//Move to Mid Scale Position
			isManualControl = false;
			isPositionControl = true;
			this.goToPosition(Constants.MidScalePosition);
		}
		else if (OperatorController.getRawButton(Constants.ButtonB)){
			//Move to High Scale Position
			isManualControl = false;
			isPositionControl = true;
			this.goToPosition(Constants.HighScalePosition);
		}
		else {
			//Do nothing, Position Control already active
		}
		
		
	}
	
	private double getElevatorInput() {
		
		double z;
		z = OperatorController.getRawAxis(Constants.LeftJoyY);
		
		return (Math.abs(z) > Constants.DeadZoneLimit ? -(z) : 0.0);
		
	}
	
	
	public static double getElevatorPosition(){
		return elevatorMotor.getSelectedSensorPosition(0);
	}
	
	public static boolean isLowerLimitPressed(){
		return (elevatorMotor.getSensorCollection().isRevLimitSwitchClosed());
	}
	
	public static boolean isUpperLimitPressed(){
		return (elevatorMotor.getSensorCollection().isFwdLimitSwitchClosed());
	}
	
	public static void goToPosition(double targetPosition){
		//Only call this if elevator position is zeroed!!!!
		if(isElevatorZeroed){
			commandedPosition = targetPosition;
			elevatorMotor.set(ControlMode.Position, targetPosition);
		}
		else{}
	}
	
	public void zeroElevatorPosition(){
		isElevatorZeroed = true;
		elevatorMotor.setSelectedSensorPosition(0, 0, 0);
	}
}
