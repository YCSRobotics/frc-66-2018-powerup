package org.usfirst.frc.team470.robot;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.Talon;

public class Intake {

	private static Talon leftIntakeMotor = Constants.IntakeLeftMotor;
	private static Talon rightIntakeMotor = Constants.IntakeRightMotor;
	
	private static Solenoid intakeSolenoid = Constants.IntakeSolenoid;
	private static Solenoid intakeOpenCloseSolenoid = Constants.IntakeSolenoidOpenClose;
	
	Joystick OperatorController = new Joystick(Constants.OperatorController);
	
	private static boolean isIntakeSolenoidActive = false;
	private static boolean isIntakeSolenoidOpenCloseActive = false;
	private boolean isButtonPressed = false;
	private boolean isOpenCloseButtonPressed = false;
	
	public void intakeInit() {
		
		intakeOpenCloseSolenoid.set(true);
		
	}
	
	public void updateIntake() {
		
		determineIntakeRaiseLowerButtonStatus();
		determineIntakeOpenCloseButtonStatus();
		
		if (!isIntakeSolenoidActive) {
			
			intakeSolenoid.set(false);
			
		} else {
			
			intakeSolenoid.set(true);
			
		}
		
		if (!isIntakeSolenoidOpenCloseActive) {
			
			intakeOpenCloseSolenoid.set(false);
			
		} else {
			
			intakeOpenCloseSolenoid.set(true);
			
		}
		
		setIntakeSpeed(getIntakeInput());	
	}
	
	private void determineIntakeRaiseLowerButtonStatus(){
		
		if((OperatorController.getRawButton(Constants.LeftBumper)) && (!isOpenCloseButtonPressed)) {
					
			isOpenCloseButtonPressed = true;
					
		    if(isIntakeSolenoidActive) {
		    	
		    	isIntakeSolenoidActive = false;
		    	
			} else {
				
				isIntakeSolenoidActive = true;
				
			}
		    
		} else if (!OperatorController.getRawButton(Constants.LeftBumper)){
			
			isOpenCloseButtonPressed = false;
			
		} else {
			//Do nothing, button is still pressed
		}
		
	}
	
	private void determineIntakeOpenCloseButtonStatus(){
		
		if((OperatorController.getRawButton(Constants.RightBumper)) && (!isButtonPressed)) {
					
			isButtonPressed = true;
					
		    if(isIntakeSolenoidOpenCloseActive) {
		    	
		    	isIntakeSolenoidOpenCloseActive = false;
		    	
			} else {
				
				isIntakeSolenoidOpenCloseActive = true;
				
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
		
		return (Math.abs(z) > Constants.DeadZoneLimit ? -(z) : -0.15);
		
	}
	
	public static void setIntakeSpeed(double speed){
		leftIntakeMotor.set(speed+0.05);
		rightIntakeMotor.set(-speed);
	}
	
	public static void setIntakeSolenoid(boolean command){
		isIntakeSolenoidActive = command;
		intakeSolenoid.set(command);
	}
}
