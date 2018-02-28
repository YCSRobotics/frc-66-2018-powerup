package org.usfirst.frc.team470.robot;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

import edu.wpi.first.wpilibj.Joystick;

public class Elevator {
	
	//Initialize Controller
	
	Joystick OperatorController = new Joystick(Constants.OperatorController);
	
	//Initialize Motors
	private static TalonSRX elevatorMotor = Constants.ElevatorMotor;
	
	//Elevator Variables
	public double elevatorPosition;
	public double commandedPosition;
	
	public boolean isElevatorZeroed = false;
	public boolean isManualControl = false;
	public boolean isPositionControl = false;
	
	public Elevator(){
		//Ramping Lift
		elevatorMotor.configOpenloopRamp(0.0, 5);
	}
	
	public void initElevator(){
		//Put any initialization routines here
	}
	
	public void updateElevator(){
		
		double elevatorInput = getElevatorInput();
		elevatorPosition = getElevatorPosition();
		
		if(Math.abs(elevatorInput) > 0.0)
		{
			//Elevator is being controlled by the joystick, Manual Control
			isManualControl = true;
			isPositionControl = false;
			elevatorMotor.set(ControlMode.PercentOutput, getElevatorInput());
		}
		else if (!isElevatorZeroed) { 
			//Joystick released but relative position is unknown, so can't move to relative position
			isManualControl = true;
			isPositionControl = false; 
			elevatorMotor.set(ControlMode.PercentOutput, 0.0);
		}
		else if((isManualControl)){
			//Joystick was just released AND elevator is zeroed, so do position control on current position
			isManualControl = false;
			isPositionControl = true;
			elevatorMotor.set(ControlMode.Position, elevatorPosition);
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
		
		return (Math.abs(z) > Constants.OmniDeadZoneLimit ? -(z) : 0.0);
		
	}
	
	public static double getElevatorPosition(){
		elevatorMotor.setSensorPhase(true);
		return elevatorMotor.getSelectedSensorPosition(0);
	}
	
	public void goToPosition(double targetPosition){
		//Only call this if elevator position is zeroed!!!!
		elevatorMotor.set(ControlMode.Position, targetPosition);
	}
	
	public void zeroElevatorPosition(){
		elevatorMotor.setSelectedSensorPosition(0, 0, 0);
	}
}
