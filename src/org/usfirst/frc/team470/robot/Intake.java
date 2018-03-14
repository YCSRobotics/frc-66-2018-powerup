package org.usfirst.frc.team470.robot;

import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.Talon;

public class Intake {

	private static Talon leftIntakeMotor = Constants.IntakeLeftMotor;
	private static Talon rightIntakeMotor = Constants.IntakeRightMotor;
	
	private static Solenoid intakeSolenoid = Constants.IntakeSolenoid;
	private static Solenoid intakeOpenCloseSolenoid = Constants.IntakeSolenoidOpenClose;
	
	private static AnalogInput distanceSensor = Constants.DistanceSensor;
	
	Joystick DriveController = new Joystick(Constants.DriveController);
	Joystick OperatorController = new Joystick(Constants.OperatorController);
	
	private static boolean isIntakeSolenoidActive = false;
	private static boolean isIntakeSolenoidOpenCloseActive = false;
	private boolean isButtonPressed = false;
	private boolean isOpenCloseButtonPressed = false;
	private boolean isTriggerPressed = false;
	private boolean isIntakeOpen = false;
	public static double distanceVoltage;
	
	public void intakeInit() {
		
		intakeOpenCloseSolenoid.set(true);
		
	}
	
	public void updateIntake() {
		
		determineIntakeRaiseLowerButtonStatus();
		determineIntakeOpenCloseButtonStatus();
		
		distanceVoltage = distanceSensor.getVoltage();
		
		if(DriveController.getRawAxis(Constants.RightTrigger) >= Constants.TriggerActiveThreshold){
			//Driver Controller Auto Load, overrides Operator Controller
			if(!isTriggerPressed){
				//Trigger was not pressed before, so open intake
				isTriggerPressed = true;
				isIntakeOpen = true;
				isIntakeSolenoidOpenCloseActive = true;
				intakeOpenCloseSolenoid.set(true);
				setIntakeSpeed(1.0);
			}
			else if((isIntakeOpen) &&
					(distanceVoltage > Constants.IntakeCubeDistance)){
				//Trigger is pressed and cube detected, close intake/grip cube
				isIntakeOpen = false;
				intakeOpenCloseSolenoid.set(false);
			}
			else{
				//Intake closed on cube
			}
		}
		else{
			//Trigger no longer pressed, but don't open the intake automatically
			isTriggerPressed = false;
			
			//Handle Operator control of the grip solenoid
			if(!isIntakeSolenoidOpenCloseActive){
				intakeOpenCloseSolenoid.set(false);
			}else {
				intakeOpenCloseSolenoid.set(true);
			}
			
			setIntakeSpeed(getIntakeInput());
		}
		
		//Handle Operator control of the Raise/Lower solenoid
		if (!isIntakeSolenoidActive){
			intakeSolenoid.set(false);
		} else {
			intakeSolenoid.set(true);
		}
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
