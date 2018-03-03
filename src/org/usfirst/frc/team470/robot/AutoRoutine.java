package org.usfirst.frc.team470.robot;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

import edu.wpi.first.wpilibj.ADXRS450_Gyro;

public class AutoRoutine {
	//Autonomous Routines
	final static int DO_NOTHING           = 0;
	final static int CENTER_SWITCH        = 1;

<<<<<<< HEAD
=======
	//Autonomous States
    final static int START        			    = 0;
	final static int MOVE_Y_DISTANCE 			= 1;
	final static int MOVE_X_DISTANCE 			= 2;
	final static int STRAFE_XY_DISTANCE 		= 3;
	final static int STOP						= 255;
	
	public static int selectedAutonRoutine;
	public static int currentAutonState = 0;
	
	public static String fms_plate_assignment; 
	
	public AutoRoutine(){
		
	}
	
	public void setSelectedAutonRoutine(int routine){
		selectedAutonRoutine = routine;
	}
	
	public void updateAutoRoutine(){
		switch(currentAutonState){
		
		case START:
			stateActionStart();
			break;
		case MOVE_Y_DISTANCE:
			stateActionMoveYDistance();
			break;
		case STOP:
		default:
			stateActionStop();
	
		}
	}
	
	private void stateActionStart(){
		if(selectedAutonRoutine != DO_NOTHING){
			Drivetrain.setMoveDistance(0.0, 72.0, 0.0, 0.3);
			currentAutonState = MOVE_Y_DISTANCE;
		}
		else{
			currentAutonState = STOP;
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
>>>>>>> be6edb73da053b560a7a53e5fd942ec3eaa9886f
}
