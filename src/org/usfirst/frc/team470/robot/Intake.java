package org.usfirst.frc.team470.robot;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Solenoid;

public class Intake {

	private static Solenoid intake = Constants.Intake;
	Joystick OperatorController = new Joystick(Constants.OperatorController);
	
	private boolean isActive = false;
	private boolean isButtonPressed = false;
	
	public void updateIntake() {
		
		intakeButtonStatus();
		
		if (!isActive) {
			
			intake.set(false);
			
		} else {
			
			intake.set(true);
			
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
