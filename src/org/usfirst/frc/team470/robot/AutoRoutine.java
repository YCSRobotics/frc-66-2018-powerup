package org.usfirst.frc.team470.robot;

import edu.wpi.first.wpilibj.Timer;

public class AutoRoutine {
	//Timer for timed delays
	public Timer timer = new Timer();
	
	//Autonomous Routines
	final static int DO_NOTHING           = 0;
	//Center Start
	final static int CENTER_SWITCH        = 1;
	final static int CENTER_SWITCH_2_CUBE = 2;
	//Left Start
	final static int LEFT_ONLY_SWITCH	  = 3;
	final static int LEFT_ONLY_SCALE	  = 4;
	final static int LEFT_START_SWITCH    = 5;
	final static int LEFT_START_SCALE     = 6;
	final static int LEFT_START_2_CUBE    = 7;
	//Right Start
	final static int RIGHT_ONLY_SWITCH   = 8;
	final static int RIGHT_ONLY_SCALE    = 9;
	final static int RIGHT_START_SWITCH   = 10;
	final static int RIGHT_START_SCALE    = 11;
	final static int RIGHT_START_2_CUBE   = 12;

	//Autonomous States
    final static int START        			       = 0;
	final static int CENTER_SWITCH_EXTEND_INTAKE   = 1;
	final static int CENTER_SWITCH_EJECT_CUBE	   = 2;
	final static int CENTER_STRAFE_DIAGONALLY      = 3;
	final static int CENTER_MOVE_BACKWARD          = 4;
	final static int CENTER_CROSS_SWITCH           = 5;
	final static int CENTER_APPROACH_CUBE		   = 6;
	final static int CENTER_GRAB_CUBE_DELAY 	   = 7;
	final static int CENTER_CLEAR_CUBE_ZONE        = 8;
	final static int MOVE_Y_DISTANCE_FWD 		   = 9;
	final static int BACKUP				 		  = 10;
	final static int LEFT_RIGHT_DELAY_BEFORE_TURN = 11;
	final static int LEFT_RIGHT_TURN_1			  = 12;
	final static int LEFT_RIGHT_DELAY_AFTER_TURN  = 13;
	final static int CREEP_FWD					  = 14;
	final static int LEFT_RIGHT_EXTEND_INTAKE	  = 15;
	final static int LEFT_RIGHT_EJECT_CUBE  	  = 16;
	final static int CROSS_SCALE				  = 17;
	final static int DELAY_AFTER_CROSS_SCALE      = 18;
	final static int TURN_TO_CUBE				  = 19;
	final static int TURN_TO_CUBE_DELAY			  = 20;
	final static int TURN_TO_SCALE				  = 21;
	final static int TURN_TO_SCALE_DELAY		  = 22;
	final static int STOP						  = 255;
	
	//Target plates
	final static int NONE              = 0;
	final static int LEFT_SWITCH       = 1;
	final static int RIGHT_SWITCH      = 2;
	final static int LEFT_SCALE        = 3;
	final static int RIGHT_SCALE       = 4;
	final static int CROSS_LEFT_SCALE  = 5;
	final static int CROSS_RIGHT_SCALE = 6;
	
	public static int selectedAutonRoutine;
	public static int currentAutonState = START;
	public static int targetPlate = NONE;
	
	public static int autonDelayCount = 0;
	private static int cubeCount = 0;
	
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
		case CENTER_STRAFE_DIAGONALLY:
			stateActionStrafeDiagonally();
			break;
		case CENTER_SWITCH_EXTEND_INTAKE:
			stateActionCenSwPivotIntakeDelay();
			break;
		case CENTER_SWITCH_EJECT_CUBE:
			stateActionCenSwEjectCubeDelay();
			break;
		case CENTER_MOVE_BACKWARD:
			stateActionCenMoveBackward();
			break;
		case CENTER_CROSS_SWITCH:
			stateActionCenCrossSwitch();
			break;
		case CENTER_APPROACH_CUBE:
			stateActionCenApproachCube();
			break;
		case CENTER_GRAB_CUBE_DELAY:
			stateActionCenGrabCubeDelay();
			break;
		case CENTER_CLEAR_CUBE_ZONE:
			stateActionCenClearCubeZone();
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
		case CROSS_SCALE:
			stateActionCrossScale();
			break;
		case DELAY_AFTER_CROSS_SCALE:
			stateActionDelayAfterCrossScale();
			break;
		case BACKUP:
			stateActionBackup();
			break;
		case TURN_TO_CUBE:
			stateActionTurnToCube();
			break;
		case TURN_TO_CUBE_DELAY:
			stateActionTurnToCubeDelay();
			break;
		case TURN_TO_SCALE:
			stateActionTurnToScale();
			break;
		case TURN_TO_SCALE_DELAY:
			stateActionTurnToScaleDelay();
			break;
		case STOP:
		default:
			stateActionStop();
		}
	}
	
	private void stateActionTurnToScaleDelay() {
		// TODO Auto-generated method stub
		
	}

	private void stateActionTurnToScale() {
		// TODO Auto-generated method stub
		
	}

	private void stateActionTurnToCubeDelay() {
		// TODO Auto-generated method stub
		
	}

	private void stateActionTurnToCube() {
		if(!Drivetrain.isTurning){
			currentAutonState = STOP;
		}
		else
		{
			//Wait for turn to complete
		}
		
	}

	private void stateActionCenClearCubeZone() {
		if(!Drivetrain.isMovingXDistance){
			if(targetPlate == LEFT_SWITCH){
				Drivetrain.setMoveDistance(Constants.Center2ndStrafeYDistance,
										   Constants.Center2ndStrafeYSpeed,
						   				   Constants.Center2ndStrafeXDistance,
						                   Constants.Center2ndStrafeXSpeed);
				currentAutonState = CENTER_STRAFE_DIAGONALLY;
			}
			else
			{
				//targetPlate == RIGHT_SWITCH
				Drivetrain.setMoveDistance(Constants.Center2ndStrafeYDistance,
						   Constants.Center2ndStrafeYSpeed,
		   				   -Constants.Center2ndStrafeXDistance,
		                   -Constants.Center2ndStrafeXSpeed);
				currentAutonState = CENTER_STRAFE_DIAGONALLY;
			}
		}
		else{
			//Wait for it to stop moving
		}
		
	}

	private void stateActionCenGrabCubeDelay() {
		if(timer.get()>= alarmTime){
			
			Intake.setIntakeSpeed(-0.15);
			Elevator.goToPosition(Constants.SwitchPosition);
			
			if(targetPlate == LEFT_SWITCH){
				Drivetrain.setMoveDistance(0,0,
						   				   Constants.CenterClearCubeZoneDist,
						                   Constants.CenterClearCubeZoneSpeed);
				currentAutonState = CENTER_CLEAR_CUBE_ZONE;
			}
			else
			{
				//targetPlate == RIGHT_SWITCH
				Drivetrain.setMoveDistance(0,0,
							   			   -Constants.CenterClearCubeZoneDist,
							               -Constants.CenterClearCubeZoneSpeed);
				currentAutonState = CENTER_CLEAR_CUBE_ZONE;
			}	
		}
		else{
			//Wait for timer to expire
		}
		
	}

	private void stateActionCenApproachCube() {
		if(!Drivetrain.isMovingYDistance){
			Intake.setIntakeOpenSolenoid(false);
			setAutonDelay(.75);
			currentAutonState = CENTER_GRAB_CUBE_DELAY;
		}
		else{
			//Wait for it to stop moving
		}
		
	}

	private void stateActionCenCrossSwitch() {
		if(!Drivetrain.isMovingXDistance)
		{
			Intake.setIntakeSpeed(-1.0);
			Drivetrain.setMoveDistance(Constants.CenterApproachCubeDist, 
					        		   Constants.CenterApproachCubeSpeed, 
					        		   0,0);
			currentAutonState = CENTER_APPROACH_CUBE;
		}
		else
		{
			//Wait for it to stop moving
		}
		
	}

	private void stateActionCenMoveBackward() {
		if(!Drivetrain.isMovingYDistance){
			
			Elevator.goToPosition(Constants.PickupPosition);
			Intake.setIntakeOpenSolenoid(true);
			
			if(targetPlate == LEFT_SWITCH){
				Drivetrain.setMoveDistance(0,0, 
										   -Constants.CenterCrossSwitchDist,
						                   -Constants.CenterCrossSwitchSpeed);
				currentAutonState = CENTER_CROSS_SWITCH;
			}
			else
			{
				Drivetrain.setMoveDistance(0,0, 
						   				   Constants.CenterCrossSwitchDist,
						   				   Constants.CenterCrossSwitchSpeed);
				currentAutonState = CENTER_CROSS_SWITCH;
			}
		}
		else{
			//Wait for it to stop moving
		}
		
	}

	private void stateActionDelayAfterCrossScale() {
		if(timer.get()>= alarmTime){
			Drivetrain.setMoveDistance(Constants.CrossScaleCreepDistance, 
						   			   Constants.LeftRightCreepSpeed,
									   0,0);
			currentAutonState = CREEP_FWD;
		}
	}

	private void stateActionCrossScale() {
		if(!Drivetrain.isMovingXDistance){
			Elevator.goToPosition(Constants.HighScalePosition);
			setAutonDelay(1);
			currentAutonState = DELAY_AFTER_CROSS_SCALE;
		}
		else
		{
			//Wait for stop moving
		}
		
	}

	private void stateActionStrafeDiagonally() {
		if((!Drivetrain.isMovingYDistance) &&
		   (!Drivetrain.isMovingXDistance))
		{
			if(cubeCount == 0)
			{
				//Delivering 1st Cube
				Drivetrain.setMoveDistance(Constants.CenterSwitchFwdDistance, 
										   Constants.CenterSwitchFwdSpeed,
										   0,0);
			    currentAutonState = MOVE_Y_DISTANCE_FWD;
			}
			else
			{
				//Second Cube
				Intake.setIntakeSpeed(1.0);
				setAutonDelay(1);
				currentAutonState = CENTER_SWITCH_EJECT_CUBE;
			}
		}
		else
		{
			//Wait for strafe to finish
		}
		
	}

	private void stateActionStart(){
		//Log what the FMS plate assignment is (to argue with FTA)
		System.out.println("The received FMS assignment is: " +  fms_plate_assignment);
		
		if(selectedAutonRoutine != DO_NOTHING){
			if((selectedAutonRoutine == CENTER_SWITCH)||
			   (selectedAutonRoutine == CENTER_SWITCH_2_CUBE)){	
				if(fms_plate_assignment.charAt(0) == 'L'){
					targetPlate = LEFT_SWITCH;
					Elevator.goToPosition(Constants.SwitchPosition);
					Drivetrain.zeroGyro();
					Drivetrain.setMoveDistance(Constants.CenterStrafeYDistance, 
											   Constants.CenterStrafeYSpeed, 
											   Constants.CenterStrafeXDistanceLeft, 
											   Constants.CenterStrafeXSpeed);
					currentAutonState = CENTER_STRAFE_DIAGONALLY;
				}
				else if(fms_plate_assignment.charAt(0) == 'R')
				{
					targetPlate = RIGHT_SWITCH;
					Elevator.goToPosition(Constants.SwitchPosition);
					Drivetrain.zeroGyro();
					Drivetrain.setMoveDistance(Constants.CenterStrafeYDistance, 
							   				   Constants.CenterStrafeYSpeed, 
							   				   -Constants.CenterStrafeXDistanceRight, 
							   				   -Constants.CenterStrafeXSpeed);
					currentAutonState = CENTER_STRAFE_DIAGONALLY;
				}
				else
				{
					//Not a valid plate assignment
					targetPlate = NONE;
					currentAutonState = STOP;
				}
			}
			else if(selectedAutonRoutine == LEFT_ONLY_SWITCH)
			{
				if(fms_plate_assignment.charAt(0) == 'L'){
					//Left switch is ours - LLL or LRL
					targetPlate = LEFT_SWITCH;
					Drivetrain.zeroGyro();
					Drivetrain.setMoveDistance(Constants.LeftRightSwitchDistance,
												Constants.LeftRightSwitchSpeed,
												0,0);
					currentAutonState = MOVE_Y_DISTANCE_FWD;
				}else if(fms_plate_assignment.charAt(1) == 'L'){
					//Left scale is ours - RLR
					targetPlate = LEFT_SCALE;
					Drivetrain.zeroGyro();
					Drivetrain.setMoveDistance(Constants.LeftRightScaleDistance, 
												Constants.LeftRightScaleSpeed,
												0,0);
					currentAutonState = MOVE_Y_DISTANCE_FWD;
				}else if(fms_plate_assignment.charAt(1) == 'R'){
					//Right switch and scale are ours - RRR
					targetPlate = NONE;
					Drivetrain.zeroGyro();
					Drivetrain.setMoveDistance(Constants.LeftRightAutoLineDistance,
												Constants.LeftRightAutoLineSpeed,
												0,0);
					currentAutonState = MOVE_Y_DISTANCE_FWD;
				}else{
					//Not a valid plate assignment
					currentAutonState = STOP;
				}		
			}
			else if(selectedAutonRoutine == LEFT_ONLY_SCALE)
			{
				if(fms_plate_assignment.charAt(1) == 'L'){
					//Left scale is ours - LLL or RLR
					targetPlate = LEFT_SCALE;
					Drivetrain.zeroGyro();
					Drivetrain.setMoveDistance(Constants.LeftRightScaleDistance, 
												Constants.LeftRightScaleSpeed,
												0,0);
					currentAutonState = MOVE_Y_DISTANCE_FWD;
				}else if(fms_plate_assignment.charAt(0) == 'L'){
					//Left switch is ours - LRL
					targetPlate = LEFT_SWITCH;
					Drivetrain.zeroGyro();
					Drivetrain.setMoveDistance(Constants.LeftRightSwitchDistance,
												Constants.LeftRightSwitchSpeed,
												0,0);
					currentAutonState = MOVE_Y_DISTANCE_FWD;
				}else if(fms_plate_assignment.charAt(1) == 'R'){
					//Right switch and scale are ours - RRR
					targetPlate = NONE;
					Drivetrain.zeroGyro();
					Drivetrain.setMoveDistance(Constants.LeftRightAutoLineDistance,
												Constants.LeftRightAutoLineSpeed,
												0,0);
					currentAutonState = MOVE_Y_DISTANCE_FWD;
				}else{
					//Not a valid plate assignment
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
												Constants.LeftRightSwitchSpeed,
												0,0);
					currentAutonState = MOVE_Y_DISTANCE_FWD;
				}else if(fms_plate_assignment.charAt(1) == 'L'){
					//Left scale is ours - RLR
					targetPlate = LEFT_SCALE;
					Drivetrain.zeroGyro();
					Drivetrain.setMoveDistance(Constants.LeftRightScaleDistance, 
												Constants.LeftRightScaleSpeed,
												0,0);
					currentAutonState = MOVE_Y_DISTANCE_FWD;
				}else if(fms_plate_assignment.charAt(1) == 'R'){
					//Right switch and scale are ours - RRR
					targetPlate = CROSS_RIGHT_SCALE;
					Drivetrain.zeroGyro();
					Drivetrain.setMoveDistance(Constants.LeftRightCrossScaleYDistance, 
							     				Constants.LeftRightCrossScaleYSpeed,
												0,0);
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
												Constants.LeftRightScaleSpeed,
												0,0);
					currentAutonState = MOVE_Y_DISTANCE_FWD;
				}
				else if(fms_plate_assignment.charAt(1) == 'R'){
					//Right scale is ours - LRL or RRR
					targetPlate = CROSS_RIGHT_SCALE;
					Drivetrain.zeroGyro();
					Drivetrain.setMoveDistance(Constants.LeftRightCrossScaleYDistance, 
		     								   Constants.LeftRightCrossScaleYSpeed,
		     								   0,0);
					currentAutonState = MOVE_Y_DISTANCE_FWD;
				}else{
					//Not a valid plate assignment
					currentAutonState = STOP;
				}		
			}
			else if(selectedAutonRoutine == LEFT_START_2_CUBE)
			{
				if(fms_plate_assignment.charAt(1) == 'L'){
					//Left scale is ours - RLR or LLL
					targetPlate = LEFT_SCALE;
					Drivetrain.zeroGyro();
					Drivetrain.setMoveDistance(Constants.LeftRightCrossScaleYDistance, 
							   				   Constants.LeftRightCrossScaleYSpeed,
							   				   0,0);
					currentAutonState = MOVE_Y_DISTANCE_FWD;
				}
				else if(fms_plate_assignment.charAt(1) == 'R'){
					//Right scale is ours - LRL or RRR
					targetPlate = CROSS_RIGHT_SCALE;
					Drivetrain.zeroGyro();
					Drivetrain.setMoveDistance(Constants.LeftRightCrossScaleYDistance, 
		     								   Constants.LeftRightCrossScaleYSpeed,
		     								   0,0);
					currentAutonState = MOVE_Y_DISTANCE_FWD;
				}else{
					//Not a valid plate assignment
					currentAutonState = STOP;
				}		
			}
			else if(selectedAutonRoutine == RIGHT_ONLY_SWITCH)
			{
				if(fms_plate_assignment.charAt(0) == 'R'){
					//Right switch is ours - RRR or RLR
					targetPlate = RIGHT_SWITCH;
					Drivetrain.zeroGyro();
					Drivetrain.setMoveDistance(Constants.LeftRightSwitchDistance, 
												Constants.LeftRightSwitchSpeed,
												0,0);
					currentAutonState = MOVE_Y_DISTANCE_FWD;
				}else if(fms_plate_assignment.charAt(1) == 'R'){
					//Right scale is ours - LRL
					targetPlate = RIGHT_SCALE;
					Drivetrain.zeroGyro();
					Drivetrain.setMoveDistance(Constants.LeftRightScaleDistance, 
							 					Constants.LeftRightScaleSpeed,
												0,0);
					currentAutonState = MOVE_Y_DISTANCE_FWD;
				}else if(fms_plate_assignment.charAt(1) == 'L'){
					//Left switch and scale are ours - LLL
					targetPlate = NONE;
					Drivetrain.zeroGyro();
					Drivetrain.setMoveDistance(Constants.LeftRightAutoLineDistance,
												Constants.LeftRightAutoLineSpeed,
												0,0);
					currentAutonState = MOVE_Y_DISTANCE_FWD;
				}else{
					//Not a valid plate assignment
					currentAutonState = STOP;
				}		
			}
			else if(selectedAutonRoutine == RIGHT_ONLY_SCALE)
			{
				if(fms_plate_assignment.charAt(1) == 'R'){
					//Right scale is ours - LRL or RRR
					targetPlate = RIGHT_SCALE;
					Drivetrain.zeroGyro();
					Drivetrain.setMoveDistance(Constants.LeftRightScaleDistance, 
							 					Constants.LeftRightScaleSpeed,
												0,0);
					currentAutonState = MOVE_Y_DISTANCE_FWD;
				}else if(fms_plate_assignment.charAt(0) == 'R'){
					//Right switch is ours - RLR
					targetPlate = RIGHT_SWITCH;
					Drivetrain.zeroGyro();
					Drivetrain.setMoveDistance(Constants.LeftRightSwitchDistance, 
												Constants.LeftRightSwitchSpeed,
												0,0);
					currentAutonState = MOVE_Y_DISTANCE_FWD;
				}else if(fms_plate_assignment.charAt(1) == 'L'){
					//Left switch and scale are ours - LLL
					targetPlate = NONE;
					Drivetrain.zeroGyro();
					Drivetrain.setMoveDistance(Constants.LeftRightAutoLineDistance,
												Constants.LeftRightAutoLineSpeed,
												0,0);
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
												Constants.LeftRightSwitchSpeed,
												0,0);
					currentAutonState = MOVE_Y_DISTANCE_FWD;
				}else if(fms_plate_assignment.charAt(1) == 'R'){
					//Right scale is ours - LRL
					targetPlate = RIGHT_SCALE;
					Drivetrain.zeroGyro();
					Drivetrain.setMoveDistance(Constants.LeftRightScaleDistance, 
							 					Constants.LeftRightScaleSpeed,
												0,0);
					currentAutonState = MOVE_Y_DISTANCE_FWD;
				}else if(fms_plate_assignment.charAt(1) == 'L'){
					//Left switch and scale are ours - LLL
					//For now, just cross autonomous line
					targetPlate = CROSS_LEFT_SCALE;
					Drivetrain.zeroGyro();
					Drivetrain.setMoveDistance(Constants.LeftRightCrossScaleYDistance, 
		     				                   Constants.LeftRightCrossScaleYSpeed,
							                   0,0);
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
					Drivetrain.zeroGyro();
					Drivetrain.setMoveDistance(Constants.LeftRightScaleDistance, 
												Constants.LeftRightScaleSpeed,
												0,0);
					currentAutonState = MOVE_Y_DISTANCE_FWD;
				}
				else if(fms_plate_assignment.charAt(1) == 'L'){
					//Left switch and scale are ours - LLL
					//For now, just cross autonomous line
					targetPlate = CROSS_LEFT_SCALE;
					Drivetrain.zeroGyro();
					Drivetrain.setMoveDistance(Constants.LeftRightCrossScaleYDistance, 
		     				                   Constants.LeftRightCrossScaleYSpeed,
							                   0,0);
					currentAutonState = MOVE_Y_DISTANCE_FWD;
				}else{
					//Not a valid plate assignment
					currentAutonState = STOP;
				}		
			}
			else if(selectedAutonRoutine == RIGHT_START_2_CUBE)
			{
				if(fms_plate_assignment.charAt(1) == 'R'){
					//Right scale is ours - LRL or RRR
					targetPlate = RIGHT_SCALE;
					Drivetrain.zeroGyro();
					Drivetrain.setMoveDistance(Constants.LeftRightCrossScaleYDistance, 
			                   				   Constants.LeftRightCrossScaleYSpeed,
			                   				   0,0);
					currentAutonState = MOVE_Y_DISTANCE_FWD;
				}
				else if(fms_plate_assignment.charAt(1) == 'L'){
					//Left switch and scale are ours - LLL
					//For now, just cross autonomous line
					targetPlate = CROSS_LEFT_SCALE;
					Drivetrain.zeroGyro();
					Drivetrain.setMoveDistance(Constants.LeftRightCrossScaleYDistance, 
		     				                   Constants.LeftRightCrossScaleYSpeed,
							                   0,0);
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
	
	private void stateActionCenSwPivotIntakeDelay(){
		if(timer.get()>= alarmTime)
		{
			Intake.setIntakeSpeed(1.0);
			setAutonDelay(0.5);
			currentAutonState = CENTER_SWITCH_EJECT_CUBE;
		}
		else{
			//Wait for the timer to expire
		}
	}
	
	private void stateActionCenSwEjectCubeDelay(){
		if(timer.get()>= alarmTime)
		{
			//Increment the cube count
			cubeCount++;
			
			if((selectedAutonRoutine == CENTER_SWITCH_2_CUBE) &&
			   (cubeCount < 2)){
				//Go get 2nd Cube
				Intake.setIntakeSpeed(0.0);
				Drivetrain.setMoveDistance(Constants.CenterSwitchRwdDistance, 
										   Constants.CenterSwitchRwdSpeed,
				                            0,0);
				currentAutonState = CENTER_MOVE_BACKWARD;
			}
			else
			{
				//Done
				Intake.setIntakeSpeed(0.0);
				currentAutonState = STOP;
			}
		}
		else{
			//Wait for the timer to expire
		}
	}
	
	private void stateActionMoveYDistance(){
		if(!Drivetrain.isMovingYDistance){
			if((selectedAutonRoutine == CENTER_SWITCH)||
			   (selectedAutonRoutine == CENTER_SWITCH_2_CUBE))
			{
				Intake.setIntakeSolenoid(true);
				setAutonDelay(0.5);
				currentAutonState = CENTER_SWITCH_EXTEND_INTAKE;
			}
			else if ((selectedAutonRoutine == LEFT_ONLY_SWITCH) ||
					 (selectedAutonRoutine == LEFT_ONLY_SCALE)  ||
					 (selectedAutonRoutine == LEFT_START_SWITCH)||
					 (selectedAutonRoutine == LEFT_START_SCALE))
			{
				if(targetPlate == LEFT_SWITCH)
				{
					//LLL or LRL
					Elevator.goToPosition(Constants.SwitchPosition);
					setAutonDelay(0.5);
					currentAutonState = LEFT_RIGHT_DELAY_BEFORE_TURN;
				}
				else if(targetPlate == LEFT_SCALE)
				{
					//RLR
					Elevator.goToPosition(Constants.HighScalePosition);
					setAutonDelay(0.5);
					currentAutonState = LEFT_RIGHT_DELAY_BEFORE_TURN;
				}
				else if(targetPlate == CROSS_RIGHT_SCALE)
				{
					//RRR
					Drivetrain.setMoveDistance(0,0, 
											   -Constants.LeftRightCrossScaleXDistance, 
											   -Constants.LeftRightCrossScaleXSpeed);
					currentAutonState = CROSS_SCALE;
					//currentAutonState = STOP;
				}
				else
				{
					currentAutonState = STOP;
				}
				
			}
			else if (selectedAutonRoutine == LEFT_START_2_CUBE)
			{
				if(targetPlate == LEFT_SCALE)
				{
					//LLL or RLR
					Drivetrain.setMoveDistance(0,0, 
							   				-36.0, 
							   				-Constants.LeftRightCrossScaleXSpeed);
					currentAutonState = CROSS_SCALE;
				}
				else if(targetPlate == CROSS_RIGHT_SCALE)
				{
					//LRL or RRR
					Drivetrain.setMoveDistance(0,0, 
											   -Constants.LeftRightCrossScaleXDistance, 
											   -Constants.LeftRightCrossScaleXSpeed);
					currentAutonState = CROSS_SCALE;
					//currentAutonState = STOP;
				}
				else
				{
					currentAutonState = STOP;
				}
				
			}
			else if ((selectedAutonRoutine == RIGHT_ONLY_SWITCH) ||
					 (selectedAutonRoutine == RIGHT_ONLY_SCALE)  ||
					 (selectedAutonRoutine == RIGHT_START_SWITCH) ||
					 (selectedAutonRoutine == RIGHT_START_SCALE))
			{
				if(targetPlate == RIGHT_SWITCH)
				{
					//RRR or RLR
					Elevator.goToPosition(Constants.SwitchPosition);
					setAutonDelay(0.5);
					currentAutonState = LEFT_RIGHT_DELAY_BEFORE_TURN;
				}
				else if(targetPlate == RIGHT_SCALE)
				{
					//LRL
					Elevator.goToPosition(Constants.HighScalePosition);
					setAutonDelay(0.5);
					currentAutonState = LEFT_RIGHT_DELAY_BEFORE_TURN;
				}
				else if(targetPlate == CROSS_LEFT_SCALE)
				{
					//LLL
					Drivetrain.setMoveDistance(0,0, 
											   Constants.LeftRightCrossScaleXDistance, 
											   Constants.LeftRightCrossScaleXSpeed);
					currentAutonState = CROSS_SCALE;
					//currentAutonState = STOP;
				}
				else
				{
					currentAutonState = STOP;
				}
				
			}
			else if (selectedAutonRoutine == RIGHT_START_2_CUBE)
			{
				if(targetPlate == RIGHT_SCALE)
				{
					//LRL or RRR
					Drivetrain.setMoveDistance(0,0, 
							   				   36.0, 
							                   Constants.LeftRightCrossScaleXSpeed);
					currentAutonState = CROSS_SCALE;
				}
				else if(targetPlate == CROSS_LEFT_SCALE)
				{
					//LLL or RLR
					Drivetrain.setMoveDistance(0,0, 
											   Constants.LeftRightCrossScaleXDistance, 
											   Constants.LeftRightCrossScaleXSpeed);
					currentAutonState = CROSS_SCALE;
					//currentAutonState = STOP;
				}
				else
				{
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
			if((targetPlate == LEFT_SCALE) ||
			   (targetPlate == CROSS_LEFT_SCALE)){
				Drivetrain.setTurnToTarget(-0.6, 160);
				currentAutonState = TURN_TO_CUBE;
			}
			else if((targetPlate == RIGHT_SCALE) ||
					(targetPlate == CROSS_RIGHT_SCALE)){
				//Right scale 
				Drivetrain.setTurnToTarget(0.6, 160);
				currentAutonState = TURN_TO_CUBE;
			}
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
			setAutonDelay(0.5);
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
						   					Constants.LeftRightCreepSpeed,
											0,0);
			}else{
				Drivetrain.setMoveDistance(Constants.LeftRightCreepScaleDist, 
	   										Constants.LeftRightCreepSpeed,
											0,0);
			}
			
			currentAutonState = CREEP_FWD; 
		}
	}
	
	private void stateActionCreepFwd(){
		if(!Drivetrain.isMovingYDistance){
			Intake.setIntakeSolenoid(true);
			setAutonDelay(0.5);
			currentAutonState = LEFT_RIGHT_EXTEND_INTAKE;
		}
	}
	
	private void stateActionLftRtExtendIntake(){
		if(timer.get()>= alarmTime){
			if((targetPlate == CROSS_RIGHT_SCALE)||
			   (targetPlate == CROSS_LEFT_SCALE)){
				Intake.setIntakeSpeed(0.5);
			}
			else
			{
				Intake.setIntakeSpeed(1.0);
			}
			
			setAutonDelay(1);
			currentAutonState = LEFT_RIGHT_EJECT_CUBE;
		}
	}
	
	private void stateActionLftRtEjectCube(){
		if(timer.get()>= alarmTime){
			Intake.setIntakeSpeed(0.0);
			
			cubeCount++;
			
			if(((selectedAutonRoutine == LEFT_START_2_CUBE)||
				(selectedAutonRoutine == RIGHT_START_2_CUBE)) &&
			    (cubeCount == 1)){
					Drivetrain.setMoveDistance(-12.0, 
											   -0.3, 
											    0,0);
					currentAutonState = BACKUP;
			}
			else
			{
				Drivetrain.enableDrivetrainDynamicBraking(false);
				currentAutonState = STOP;
			}
		}
		else
		{
			//Wait for timeout
		}
	}
	
	private void stateActionStop(){
		//Drivetrain.setMoveDistance(0.0, 0.0);
	}
	
	private void setAutonDelay(double delay){
		alarmTime = timer.get() + delay;
	}
}
