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
	
	public Elevator(){
		//Ramping Lift
		elevatorMotor.configOpenloopRamp(0.0, 5);
	}
	
	public void updateElevator(){
		
		double elevatorInput = getElevatorInput();
		
		if (Math.abs(elevatorInput) > Constants.DeadZoneLimit){
			elevatorMotor.set(ControlMode.PercentOutput, getElevatorInput());
		}else { 
			elevatorMotor.set(ControlMode.PercentOutput, 0.0);
		}
		
		elevatorPosition = getElevatorPosition();
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
	
	public void zeroElevatorPosition(){
		elevatorMotor.setSelectedSensorPosition(0, 0, 0);
	}
}
