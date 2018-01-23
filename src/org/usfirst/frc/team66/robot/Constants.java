package org.usfirst.frc.team66.robot;

import com.ctre.CANTalon;

import edu.wpi.first.wpilibj.Joystick;

/* Only variables that have a fixed (never changing) value should be put here */

public class Constants {
	
	static final Joystick DriveController1 = new Joystick(0);
	static final Joystick OperatorController2 = new Joystick(1);
	
	static final int LEFTJOYX     = 0;
	static final int LEFTJOYY     = 1;
	static final int RIGHTJOYX    = 4;
	static final int RIGHTJOYY    = 5;
	static final int LEFTTRIGGER  = 2;
	static final int RIGHTTRIGGER = 3;
	static final int BUTTONA      = 1;
	static final int BUTTONB      = 2;
	static final int BUTTONX      = 3;
	static final int BUTTONY      = 4;
	static final int LEFTBUMMPER  = 5;
	static final int RIGHTBUMMPER = 6;
	
	//Drive Motors
	 static final CANTalon LEFTMASMOTOR = new CANTalon(0);
	 static final CANTalon LEFTSLVMOTOR = new CANTalon(1);
	 static final CANTalon RIGHTMASMOTOR = new CANTalon(2);
	 static final CANTalon RIGHTSLVMOTOR = new CANTalon(3);
	 static final CANTalon HMOTOR1 = new CANTalon(4);
	 static final CANTalon HMOTOR2 = new CANTalon(5);
	
	//Lift Motor
	private static final CANTalon LIFTMOTOR = new CANTalon(6);
	
	//Intake Motors
	private static final CANTalon LEFTINTAKE = new CANTalon(7);
	private static final CANTalon RIGHTINTAKE = new CANTalon(8);
	
	// Winch Motors
	private static final CANTalon WINCH1 = new CANTalon(9);
	private static final CANTalon WINCH2 = new CANTalon(10);
	

}