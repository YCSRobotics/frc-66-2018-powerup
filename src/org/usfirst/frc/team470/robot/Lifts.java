package org.usfirst.frc.team470.robot;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Solenoid;

public class Lifts {

	static Joystick OperatorController = new Joystick(Constants.OperatorController);
	
	static Solenoid leftRelease = Constants.LeftLiftReleaseSolenoid;
	static Solenoid rightRelease = Constants.RightLiftReleaseSolenoid;
	
	private static TalonSRX leftLiftMotor = Constants.LeftLiftMotor;
	private static TalonSRX rightLiftMotor = Constants.RightLiftMotor;
	
	public Lifts(){
		
	}
	
	public void updateLiftsTeleop(){
		
		if(OperatorController.getPOV() == Constants.DPAD_UP){
			leftRelease.set(true);
			rightRelease.set(true);
		}
		else{
			leftRelease.set(false);
			rightRelease.set(false);
		}
		
		if(OperatorController.getPOV() == Constants.DPAD_LEFT){
			leftRelease.set(true);
		}
		else{
			leftRelease.set(false);
		}
		
		if(OperatorController.getPOV() == Constants.DPAD_RIGHT){
			rightRelease.set(true);
		}
		else{
			rightRelease.set(false);
		}
		
		if(OperatorController.getRawAxis(Constants.LeftTrigger) >= Constants.TriggerActiveThreshold){
			leftLiftMotor.set(ControlMode.PercentOutput, -1.0);
		}
		else
		{
			leftLiftMotor.set(ControlMode.PercentOutput, 0.0);
		}
		
		if(OperatorController.getRawAxis(Constants.RightTrigger) >= Constants.TriggerActiveThreshold){
			rightLiftMotor.set(ControlMode.PercentOutput, 1.0);
		}
		else
		{
			rightLiftMotor.set(ControlMode.PercentOutput, 0.0);
		}
		
		if(OperatorController.getPOV() == 180)
		{
			leftLiftMotor.set(ControlMode.PercentOutput, 1.0);
			rightLiftMotor.set(ControlMode.PercentOutput, -1.0);
		}
		else
		{		
			leftLiftMotor.set(ControlMode.PercentOutput, 0.0);
			rightLiftMotor.set(ControlMode.PercentOutput, 0.0);
		}
	}
}
