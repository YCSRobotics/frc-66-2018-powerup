package org.usfirst.frc.team470.robot;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

import edu.wpi.first.wpilibj.ADXRS450_Gyro;

public class AutoRoutine {
	//Autonomous Routines
	final static int DO_NOTHING           = 0;
	final static int CENTER_SWITCH        = 1;
	final static int LEFT_START_SWITCH    = 2;
	final static int LEFT_START_SCALE     = 3;
	final static int RIGHT_START_SWITCH   = 4;
	final static int RIGHT_START_SCALE    = 5;

	//Autonomous States
    final static int START        			    = 0;
	final static int DELAY_1 					= 1;
    final static int CENTER_TURN_TO_TARGET		= 2;
	final static int MOVE_Y_DISTANCE 			= 3;
	final static int MOVE_X_DISTANCE 			= 4;
	final static int STRAFE_XY_DISTANCE 		= 5;
	final static int STOP						= 255;
	
	public static int selectedAutonRoutine;
	public static int currentAutonState = START;
	
	public static int autonDelayCount = 0;
	
	public static String fms_plate_assignment; 
	
	public AutoRoutine(){
		
	}
	
	public void setSelectedAutonRoutine(int routine){
		selectedAutonRoutine = routine;
	}
	
	public void setFmsPlateAssignment(String assignment){
		fms_plate_assignment = assignment;
	}
	
	public void updateAutoRoutine(){
		
		switch(currentAutonState){
		case START:
			stateActionStart();
			break;
		case CENTER_TURN_TO_TARGET:
			stateActionCenterTurnToTarget();
			break;
		case DELAY_1:
			stateActionDelay1();
		case MOVE_Y_DISTANCE:
			stateActionMoveYDistance();
			break;
		case STOP:
		default:
			stateActionStop();
		}
	}
	
	private void stateActionStart(){
		//Log what the FMS plate assignment is (to argue with FTA)
		System.out.println("The FMS assignment is: " +  fms_plate_assignment);
		
		if(selectedAutonRoutine != DO_NOTHING){
			if(selectedAutonRoutine == CENTER_SWITCH){
				
				/*Drivetrain.zeroGyro();
				Drivetrain.setTurnToTarget(0.5, 15.0);
				currentAutonState = CENTER_TURN_TO_TARGET;*/
				
				if(fms_plate_assignment.charAt(0) == 'L'){
					
					Drivetrain.zeroGyro();
					Drivetrain.setTurnToTarget(-Constants.CenterTurnToSwitchSpeed,
												Constants.CenterTurnToSwitchAngle);
					
					currentAutonState = CENTER_TURN_TO_TARGET;
				}
				else if(fms_plate_assignment.charAt(0) == 'R')
				{
					Drivetrain.zeroGyro();
					
					Drivetrain.setTurnToTarget(Constants.CenterTurnToSwitchSpeed,
							                    Constants.CenterTurnToSwitchAngle);
					
					currentAutonState = CENTER_TURN_TO_TARGET;
				}
				else
				{
					//Not a valid plate assignment
					currentAutonState = STOP;
				}
			}
			else{
				currentAutonState = STOP;
			}
		}
		else{
			currentAutonState = STOP;
		}
	}
	
	private void stateActionCenterTurnToTarget(){
		if(!Drivetrain.isTurning){
			if(selectedAutonRoutine == CENTER_SWITCH){
				setAutonDelay(500);
				currentAutonState = DELAY_1;
			}
			else{
				//currentAutonState = STOP;
			}	
		}
		else{
			//Wait for turn to complete
		}
	}
	
	private void stateActionDelay1(){
		if(autonDelayCount <= 20)
		{
			autonDelayCount = 0;
			Drivetrain.zeroGyro();
			Drivetrain.setMoveDistance(0.0, 72.0, 0.0, 0.5);
			currentAutonState = MOVE_Y_DISTANCE;
		}
		else{
			autonDelayCount = autonDelayCount - 20;
		}
	}
	
	private void stateActionMoveYDistance(){
		if(!Drivetrain.isMovingYDistance){
			currentAutonState = STOP;
		}
		else
		{
			//Do nothing and wait for move to complete
		}
	}
	
	private void stateActionStop(){
		Drivetrain.setMoveDistance(0.0, 0.0, 0.0, 0.0);
	}
	
	private void setAutonDelay(int delay){
		autonDelayCount = delay;
	}
}
