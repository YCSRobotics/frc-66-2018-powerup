package org.usfirst.frc.team470.robot;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.Talon;

public class Intake {

	private static Talon leftIntakeMotor = Constants.IntakeLeftMotor;
	private static Talon rightIntakeMotor = Constants.IntakeRightMotor;
	
	private static Solenoid intakeSolenoid = Constants.IntakeSolenoid;
	
	Joystick OperatorController = new Joystick(Constants.OperatorController);
	
	private boolean isActive = false;
	private boolean isButtonPressed = false;
	
	public void updateIntake() {
		
		intakeButtonStatus();
		
		if (!isActive) {
			
			intakeSolenoid.set(false);
			
		} else {
			
			intakeSolenoid.set(true);
			
		}
		
		//Control intake motors
		if(OperatorController.getRawButton(Constants.ButtonA)){
			
			leftIntakeMotor.set(1.0);
			rightIntakeMotor.set(-1.0);
			
		}else if(OperatorController.getRawButton(Constants.ButtonB)){
			
			leftIntakeMotor.set(-1.0);
			rightIntakeMotor.set(1.0);
			
		}else{
			
			leftIntakeMotor.set(0.0);
			rightIntakeMotor.set(0.0);
		}
		
	}
	
	private void intakeButtonStatus(){
		
		if((OperatorController.getRawButton(Constants.RightBumper)) && (!isButtonPressed)) {
					
			isButtonPressed = true;
					
		    if(isActive) {
		    	
		    	isActive = false;
		    	
			} else {
				
				isActive = true;
				
			}
		    
		} else if (!OperatorController.getRawButton(Constants.RightBumper)){
			
			isButtonPressed = false;
			
		} else {
			//Do nothing, button is still pressed
		}
		
	}
}
