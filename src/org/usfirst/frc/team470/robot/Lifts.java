package org.usfirst.frc.team470.robot;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.Spark;

public class Lifts {

	static Joystick OperatorController = new Joystick(Constants.OperatorController);
	
	static Solenoid releaseSolenoid = Constants.LiftReleaseSolenoid;
	
	private static Spark liftMotor = new Spark(2);
	
	public Lifts(){
		
	}
	
	public void updateLiftsTeleop(){
		
		if(OperatorController.getPOV() == Constants.DPAD_UP){
			releaseSolenoid.set(true);
		}
		else{
			releaseSolenoid.set(false);
		}
		
		if(OperatorController.getRawAxis(Constants.LeftTrigger) >= Constants.TriggerActiveThreshold){
			liftMotor.set(1.0);
		}
		else
		{
			liftMotor.set(0.0);
		}
	}
}
