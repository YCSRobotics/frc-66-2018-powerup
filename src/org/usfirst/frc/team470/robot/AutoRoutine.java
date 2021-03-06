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
    final static int START        			      = 0;
	final static int CENTER_SWITCH_DELAY_1 		  = 1;
	final static int CENTER_SWITCH_DELAY_2 		  = 2;
	final static int CENTER_SWITCH_DELAY_3 		  = 3;
    final static int CENTER_TURN_TO_TARGET		  = 4;
	final static int MOVE_Y_DISTANCE_FWD 		  = 5;
	final static int BACKUP				 		  = 6;
	final static int LEFT_RIGHT_DELAY_BEFORE_TURN = 7;
	final static int LEFT_RIGHT_TURN_1			  = 8;
	final static int LEFT_RIGHT_DELAY_AFTER_TURN  = 9;
	final static int CREEP_FWD					  = 10;
	final static int LEFT_RIGHT_EXTEND_INTAKE	  = 11;
	final static int LEFT_RIGHT_EJECT_CUBE  	  = 12;
	final static int STOP						  = 255;
	
	//Target plates
	final static int NONE         = 0;
	final static int LEFT_SWITCH  = 1;
	final static int RIGHT_SWITCH = 2;
	final static int LEFT_SCALE   = 3;
	final static int RIGHT_SCALE  = 4;
	
	public static int selectedAutonRoutine;
	public static int currentAutonState = START;
	public static int targetPlate = NONE;
	
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
		case LEFT_RIGHT_DELAY_BEFORE_TURN:
			stateActionLftRtDelayBeforeTurn();
			break;
		case LEFT_RIGHT_DELAY_AFTER_TURN:
			stateActionLftRtDelayAfterTurn();
			break;
		case CREEP_FWD:
			stateActionCreepFwd();
			break;
		case LEFT_RIGHT_EXTEND_INTAKE:
			stateActionLftRtExtendIntake();
			break;
		case LEFT_RIGHT_EJECT_CUBE:
			stateActionLftRtEjectCube();
			break;
		case LEFT_RIGHT_TURN_1:
			stateActionLftRtTurn1();
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
				if(fms_plate_assignment.charAt(0) == 'L'){
					targetPlate = LEFT_SWITCH;
					Elevator.goToPosition(Constants.SwitchPosition);
					Drivetrain.zeroGyro();
					Drivetrain.setTurnToTarget(-Constants.CenterTurnToSwitchSpeed,
												Constants.CenterTurnToSwitchAngle);
					currentAutonState = CENTER_TURN_TO_TARGET;
				}
				else if(fms_plate_assignment.charAt(0) == 'R')
				{
					targetPlate = RIGHT_SWITCH;
					Elevator.goToPosition(Constants.SwitchPosition);
					Drivetrain.zeroGyro();
					Drivetrain.setTurnToTarget(Constants.CenterTurnToSwitchSpeed,
							                    Constants.CenterTurnToSwitchAngle);
					currentAutonState = CENTER_TURN_TO_TARGET;
				}
				else
				{
					//Not a valid plate assignment
					targetPlate = NONE;
					currentAutonState = STOP;
				}
			}
			else if(selectedAutonRoutine == LEFT_START_SWITCH)
			{
				if(fms_plate_assignment.charAt(0) == 'L'){
					//Left switch is ours - LLL or LRL
					targetPlate = LEFT_SWITCH;
					Drivetrain.zeroGyro();
					Drivetrain.setMoveDistance(Constants.LeftRightSwitchDistance,
												Constants.LeftRightSwitchSpeed);
					currentAutonState = MOVE_Y_DISTANCE_FWD;
				}else if(fms_plate_assignment.charAt(1) == 'L'){
					//Left scale is ours - RLR
					targetPlate = LEFT_SCALE;
					Drivetrain.zeroGyro();
					Drivetrain.setMoveDistance(Constants.LeftRightScaleDistance, 
												Constants.LeftRightScaleSpeed);
					currentAutonState = MOVE_Y_DISTANCE_FWD;
				}else if(fms_plate_assignment.charAt(1) == 'R'){
					//Right switch and scale are ours "RRR"
					//For now, just cross autonomous line
					targetPlate = NONE;
					Drivetrain.zeroGyro();
					Drivetrain.setMoveDistance(Constants.LeftRightAutoLineDistance, 
							     				Constants.LeftRightAutoLineSpeed);
					currentAutonState = MOVE_Y_DISTANCE_FWD;
				}else{
					//Not a valid plate assignment
					currentAutonState = STOP;
				}		
			}
			else if(selectedAutonRoutine == LEFT_START_SCALE)
			{
				if(fms_plate_assignment.charAt(1) == 'L'){
					//Left scale is ours - RLR or LLL
					targetPlate = LEFT_SCALE;
					Drivetrain.zeroGyro();
					Drivetrain.setMoveDistance(Constants.LeftRightScaleDistance, 
												Constants.LeftRightScaleSpeed);
					currentAutonState = MOVE_Y_DISTANCE_FWD;
				}else if(fms_plate_assignment.charAt(0) == 'L'){
					//Left switch is ours - LRL
					targetPlate = LEFT_SWITCH;
					Drivetrain.zeroGyro();
					Drivetrain.setMoveDistance(Constants.LeftRightSwitchDistance,
												Constants.LeftRightSwitchSpeed);
					currentAutonState = MOVE_Y_DISTANCE_FWD;
				}else if(fms_plate_assignment.charAt(1) == 'R'){
					//Right switch and scale are ours "RRR"
					//For now, just cross autonomous line
					targetPlate = NONE;
					Drivetrain.zeroGyro();
					Drivetrain.setMoveDistance(Constants.LeftRightAutoLineDistance, 
												Constants.LeftRightAutoLineSpeed);
					currentAutonState = MOVE_Y_DISTANCE_FWD;
				}else{
					//Not a valid plate assignment
					currentAutonState = STOP;
				}		
			}
			else if(selectedAutonRoutine == RIGHT_START_SWITCH)
			{
				if(fms_plate_assignment.charAt(0) == 'R'){
					//Right switch is ours - RRR or RLR
					targetPlate = RIGHT_SWITCH;
					Drivetrain.zeroGyro();
					Drivetrain.setMoveDistance(Constants.LeftRightSwitchDistance, 
												Constants.LeftRightSwitchSpeed);
					currentAutonState = MOVE_Y_DISTANCE_FWD;
				}else if(fms_plate_assignment.charAt(1) == 'R'){
					//Right scale is ours - LRL
					targetPlate = RIGHT_SCALE;
					Drivetrain.zeroGyro();
					Drivetrain.setMoveDistance(Constants.LeftRightScaleDistance, 
							 					Constants.LeftRightScaleSpeed);
					currentAutonState = MOVE_Y_DISTANCE_FWD;
				}else if(fms_plate_assignment.charAt(1) == 'L'){
					//Left switch and scale are ours - LLL
					//For now, just cross autonomous line
					targetPlate = NONE;
					Drivetrain.zeroGyro();
					Drivetrain.setMoveDistance(Constants.LeftRightAutoLineDistance, 
							  					Constants.LeftRightAutoLineSpeed);
					currentAutonState = MOVE_Y_DISTANCE_FWD;
				}else{
					//Not a valid plate assignment
					currentAutonState = STOP;
				}		
			}
			else if(selectedAutonRoutine == RIGHT_START_SCALE)
			{
				if(fms_plate_assignment.charAt(1) == 'R'){
					//Right scale is ours - LRL or RRR
					targetPlate = RIGHT_SCALE;
					Drivetrain.setMoveDistance(Constants.LeftRightScaleDistance, 
												Constants.LeftRightScaleSpeed);
					currentAutonState = MOVE_Y_DISTANCE_FWD;
				}else if(fms_plate_assignment.charAt(0) == 'R'){
					//Right switch is ours - RLR
					targetPlate = RIGHT_SWITCH;
					Drivetrain.setMoveDistance(Constants.LeftRightSwitchDistance, 
												Constants.LeftRightSwitchSpeed);
					currentAutonState = MOVE_Y_DISTANCE_FWD;
				}else if(fms_plate_assignment.charAt(1) == 'L'){
					//Left switch and scale are ours - LLL
					//For now, just cross autonomous line
					targetPlate = NONE;
					Drivetrain.zeroGyro();
					Drivetrain.setMoveDistance(Constants.LeftRightAutoLineDistance, 0.5);
					currentAutonState = MOVE_Y_DISTANCE_FWD;
				}else{
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
			Drivetrain.setMoveDistance(Constants.CenterSwitchFwdDistance, 
										Constants.CenterSwitchFwdSpeed);
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
			Drivetrain.setMoveDistance(Constants.CenterSwitchRwdDistance, 
										Constants.CenterSwitchRwdSpeed);
			currentAutonState = BACKUP;
		}
		else{
			//Wait for the timer to expire
		}
	}
	
	private void stateActionMoveYDistance(){
		if(!Drivetrain.isMovingYDistance){
			if(selectedAutonRoutine == CENTER_SWITCH)
			{
				Intake.setIntakeSolenoid(true);
				setAutonDelay(1);
				currentAutonState = CENTER_SWITCH_DELAY_2;
			}
			else if (selectedAutonRoutine == LEFT_START_SWITCH)
			{
				if(fms_plate_assignment.charAt(0) == 'L')
				{
					//LLL or LRL
					Elevator.goToPosition(Constants.SwitchPosition);
					setAutonDelay(1);
					currentAutonState = LEFT_RIGHT_DELAY_BEFORE_TURN;
				}
				else if(fms_plate_assignment.charAt(1) == 'L')
				{
					//RLR
					Elevator.goToPosition(Constants.HighScalePosition);
					setAutonDelay(1);
					currentAutonState = LEFT_RIGHT_DELAY_BEFORE_TURN;
				}
				else
				{
					//RRR
					currentAutonState = STOP;
				}
				
			}
			else if (selectedAutonRoutine == LEFT_START_SCALE)
			{
				if(fms_plate_assignment.charAt(1) == 'L')
				{
					//RLR or LLL
					Elevator.goToPosition(Constants.HighScalePosition);
					setAutonDelay(1);
					currentAutonState = LEFT_RIGHT_DELAY_BEFORE_TURN;
				}
				else if(fms_plate_assignment.charAt(0) == 'L')
				{
					//LRL
					Elevator.goToPosition(Constants.SwitchPosition);
					setAutonDelay(1);
					currentAutonState = LEFT_RIGHT_DELAY_BEFORE_TURN;
				}
				else
				{
					//RRR
					currentAutonState = STOP;
				}
				
			}
			else if (selectedAutonRoutine == RIGHT_START_SWITCH)
			{
				if(fms_plate_assignment.charAt(0) == 'R')
				{
					//RRR or RLR
					Elevator.goToPosition(Constants.SwitchPosition);
					setAutonDelay(1);
					currentAutonState = LEFT_RIGHT_DELAY_BEFORE_TURN;
				}
				else if(fms_plate_assignment.charAt(1) == 'R')
				{
					//LRL
					Elevator.goToPosition(Constants.HighScalePosition);
					setAutonDelay(1);
					currentAutonState = LEFT_RIGHT_DELAY_BEFORE_TURN;
				}
				else
				{
					//LLL
					currentAutonState = STOP;
				}
				
			}
			else if (selectedAutonRoutine == RIGHT_START_SCALE)
			{
				if(fms_plate_assignment.charAt(1) == 'R')
				{
					//LRL or RRR
					Elevator.goToPosition(Constants.HighScalePosition);
					setAutonDelay(1);
					currentAutonState = LEFT_RIGHT_DELAY_BEFORE_TURN;
				}
				else if(fms_plate_assignment.charAt(0) == 'R')
				{
					//RLR
					Elevator.goToPosition(Constants.SwitchPosition);
					setAutonDelay(1);
					currentAutonState = LEFT_RIGHT_DELAY_BEFORE_TURN;
				}
				else
				{
					//LLL
					currentAutonState = STOP;
				}
				
			}
			else{
				currentAutonState = STOP;
			}
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
	
	private void stateActionLftRtDelayBeforeTurn(){
		if(timer.get()>= alarmTime)
		{
			if((selectedAutonRoutine == LEFT_START_SWITCH) ||
			   (selectedAutonRoutine == LEFT_START_SCALE)){
				Drivetrain.zeroGyro();
				Drivetrain.setTurnToTarget(Constants.LeftRightTurnSpeed, 
											Constants.LeftRightTurnAngle);
				currentAutonState = LEFT_RIGHT_TURN_1;
			}
			else if((selectedAutonRoutine == RIGHT_START_SWITCH) ||
					(selectedAutonRoutine == RIGHT_START_SCALE)){
				Drivetrain.zeroGyro();
				Drivetrain.setTurnToTarget(-Constants.LeftRightTurnSpeed, 
											Constants.LeftRightTurnAngle);
				currentAutonState = LEFT_RIGHT_TURN_1;
			}else{
				
			}
		}
		else{
			//Wait for the timer to expire
		}
	}
	
	private void stateActionLftRtTurn1(){
		if(!Drivetrain.isTurning){
			setAutonDelay(1);
			currentAutonState = LEFT_RIGHT_DELAY_AFTER_TURN;
		}
		else{
			//Wait for turn to stop
		}
	}
	
	private void stateActionLftRtDelayAfterTurn(){
		if(timer.get()>= alarmTime){
			
			Drivetrain.zeroGyro();
			
			if((targetPlate == LEFT_SWITCH) ||
			   (targetPlate == RIGHT_SWITCH)){
				Drivetrain.setMoveDistance(Constants.LeftRightCreepSwitchDist, 
						   					Constants.LeftRightCreepSpeed);
			}else{
				Drivetrain.setMoveDistance(Constants.LeftRightCreepScaleDist, 
	   					Constants.LeftRightCreepSpeed);
			}
			
			currentAutonState = CREEP_FWD; 
		}
	}
	
	private void stateActionCreepFwd(){
		if(!Drivetrain.isMovingYDistance){
			Intake.setIntakeSolenoid(true);
			setAutonDelay(1);
			currentAutonState = LEFT_RIGHT_EXTEND_INTAKE;
		}
	}
	
	private void stateActionLftRtExtendIntake(){
		if(timer.get()>= alarmTime){
			Intake.setIntakeSpeed(1.0);
			setAutonDelay(1);
			currentAutonState = LEFT_RIGHT_EJECT_CUBE;
		}
	}
	
	private void stateActionLftRtEjectCube(){
		if(timer.get()>= alarmTime){
			Intake.setIntakeSpeed(0.0);
			Drivetrain.enableDrivetrainDynamicBraking(false);
			currentAutonState = STOP;
		}
	}
	
	private void stateActionStop(){
		//Drivetrain.setMoveDistance(0.0, 0.0);
	}
	
	private void setAutonDelay(double delay){
		alarmTime = timer.get() + delay;
	}
}
