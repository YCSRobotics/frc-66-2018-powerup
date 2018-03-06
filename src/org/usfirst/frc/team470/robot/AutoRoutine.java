package org.usfirst.frc.team470.robot;

import edu.wpi.first.wpilibj.Timer;

public class AutoRoutine {
	//Timer for timed delays
	public Timer timer = new Timer();
	
	//Autonomous Routines
	final static int DO_NOTHING           = 0;
	final static int CENTER_SWITCH        = 1;
	final static int LEFT_START_SWITCH    = 2;
	final static int LEFT_START_SCALE     = 3;
	final static int RIGHT_START_SWITCH   = 4;
	final static int RIGHT_START_SCALE    = 5;

	//Autonomous States
    final static int START        			    = 0;
	final static int CENTER_SWITCH_DELAY_1 		= 1;
	final static int CENTER_SWITCH_DELAY_2 		= 2;
	final static int CENTER_SWITCH_DELAY_3 		= 3;
    final static int CENTER_TURN_TO_TARGET		= 4;
	final static int MOVE_Y_DISTANCE_FWD 		= 5;
	final static int BACKUP				 		= 6;
	final static int STOP						= 255;
	
	public static int selectedAutonRoutine;
	public static int currentAutonState = START;
	
	public static int autonDelayCount = 0;
	
	private double alarmTime;
	
	public static String fms_plate_assignment; 
	
	public AutoRoutine(){
		timer.start();
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
		case CENTER_SWITCH_DELAY_1:
			stateActionCenSwDelay1();
			break;
		case CENTER_SWITCH_DELAY_2:
			stateActionCenSwDelay2();
			break;
		case CENTER_SWITCH_DELAY_3:
			stateActionCenSwDelay3();
			break;
		case MOVE_Y_DISTANCE_FWD:
			stateActionMoveYDistance();
			break;
		case BACKUP:
			stateActionBackup();
			break;
		case STOP:
		default:
			stateActionStop();
		}
	}
	
	private void stateActionStart(){
		//Log what the FMS plate assignment is (to argue with FTA)
		System.out.println("The received FMS assignment is: " +  fms_plate_assignment);
		
		if(selectedAutonRoutine != DO_NOTHING){
			if(selectedAutonRoutine == CENTER_SWITCH){
				
				/*Drivetrain.zeroGyro();
				Drivetrain.setTurnToTarget(0.5, 15.0);
				currentAutonState = CENTER_TURN_TO_TARGET;*/
				
				if(fms_plate_assignment.charAt(0) == 'L'){
					
					Elevator.goToPosition(Constants.SwitchPosition);
					Drivetrain.zeroGyro();
					Drivetrain.setTurnToTarget(-Constants.CenterTurnToSwitchSpeed,
												Constants.CenterTurnToSwitchAngle);
					
					currentAutonState = CENTER_TURN_TO_TARGET;
				}
				else if(fms_plate_assignment.charAt(0) == 'R')
				{
					Elevator.goToPosition(Constants.SwitchPosition);
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
				setAutonDelay(0.5);
				currentAutonState = CENTER_SWITCH_DELAY_1;
			}
			else{
				//currentAutonState = STOP;
			}	
		}
		else{
			//Wait for turn to complete
		}
	}
	
	private void stateActionCenSwDelay1(){
		if(timer.get()>= alarmTime)
		{
			autonDelayCount = 0;
			Drivetrain.zeroGyro();
			Drivetrain.setMoveDistance(110.0, 0.5);
			currentAutonState = MOVE_Y_DISTANCE_FWD;
		}
		else{
			//Wait for the timer to expire
		}
	}
	
	private void stateActionCenSwDelay2(){
		if(timer.get()>= alarmTime)
		{
			Intake.setIntakeSpeed(1.0);
			setAutonDelay(1);
			currentAutonState = CENTER_SWITCH_DELAY_3;
		}
		else{
			//Wait for the timer to expire
		}
	}
	
	private void stateActionCenSwDelay3(){
		if(timer.get()>= alarmTime)
		{
			Intake.setIntakeSpeed(0.0);
			Drivetrain.setMoveDistance(-100, -0.5);
			currentAutonState = BACKUP;
		}
		else{
			//Wait for the timer to expire
		}
	}
	
	private void stateActionMoveYDistance(){
		if(!Drivetrain.isMovingYDistance){
			Intake.setIntakeSolenoid(true);
			setAutonDelay(1);
			currentAutonState = CENTER_SWITCH_DELAY_2;
		}
		else
		{
			//Do nothing and wait for move to complete
		}
	}
	
	private void stateActionBackup(){
		if(!Drivetrain.isMovingYDistance){
			currentAutonState = STOP;
		}
		else
		{
			//Do nothing and wait for move to complete
		}
	}
	
	private void stateActionStop(){
		//Drivetrain.setMoveDistance(0.0, 0.0);
	}
	
	private void setAutonDelay(double delay){
		alarmTime = timer.get() + delay;
	}
}
