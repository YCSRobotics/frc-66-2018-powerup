package org.usfirst.frc.team470.robot;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.Talon;

public class Intake {

	private static Talon leftIntakeMotor = Constants.IntakeLeftMotor;
	private static Talon rightIntakeMotor = Constants.IntakeRightMotor;
	
	private static Solenoid intakeSolenoid = Constants.IntakeSolenoid;
	
	Joystick OperatorController = new Joystick(Constants.OperatorController);
	
	private static boolean isIntakeSolenoidActive = false;
	private boolean isButtonPressed = false;
	
	public void updateIntake() {
		
		determineIntakeRaiseLowerButtonStatus();
		
		if (!isIntakeSolenoidActive) {
			
			intakeSolenoid.set(false);
			
		} else {
			
			intakeSolenoid.set(true);
			
		}
		
		//Control intake motors
		/*if(OperatorController.getRawButton(Constants.ButtonA)){
			
			leftIntakeMotor.set(1.0);
			rightIntakeMotor.set(-1.0);
			
		}else if(OperatorController.getRawButton(Constants.ButtonB)){
			
			leftIntakeMotor.set(-1.0);
			rightIntakeMotor.set(1.0);
			
		}else{
			
			leftIntakeMotor.set(0.0);
			rightIntakeMotor.set(0.0);
		}*/
		
		setIntakeSpeed(getIntakeInput());	
	}
	
	private void determineIntakeRaiseLowerButtonStatus(){
		
		if((OperatorController.getRawButton(Constants.RightBumper)) && (!isButtonPressed)) {
					
			isButtonPressed = true;
					
		    if(isIntakeSolenoidActive) {
		    	
		    	isIntakeSolenoidActive = false;
		    	
			} else {
				
				isIntakeSolenoidActive = true;
				
			}
		    
		} else if (!OperatorController.getRawButton(Constants.RightBumper)){
			
			isButtonPressed = false;
			
		} else {
			//Do nothing, button is still pressed
		}
		
	}
	
	private double getIntakeInput() {
		
		double z;
		z = OperatorController.getRawAxis(Constants.RightJoyY);
		
		return (Math.abs(z) > Constants.DeadZoneLimit ? -(z) : 0.0);
		
	}
	
	public static void setIntakeSpeed(double speed){
		leftIntakeMotor.set(speed);
		rightIntakeMotor.set(-speed);
	}
	
	public static void setIntakeSolenoid(boolean command){
		isIntakeSolenoidActive = command;
		intakeSolenoid.set(command);
	}
}
