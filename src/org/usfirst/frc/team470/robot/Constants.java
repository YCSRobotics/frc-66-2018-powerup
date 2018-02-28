package org.usfirst.frc.team470.robot;

import com.ctre.phoenix.motorcontrol.can.TalonSRX;

import edu.wpi.first.wpilibj.ADXRS450_Gyro;
import edu.wpi.first.wpilibj.CounterBase;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.Talon;

/* Only variables that have a fixed (never changing) value should be put here */

public class Constants {
	
	//Motor Configuration
	public static final TalonSRX LeftMasterMotor = new TalonSRX(0);
	public static final TalonSRX LeftSlaveMotor = new TalonSRX(1);
	public static final TalonSRX RightMasterMotor = new TalonSRX(2);
	public static final TalonSRX RightSlaveMotor = new TalonSRX(3);
	public static final TalonSRX OmniBottomMotor = new TalonSRX(4);
	public static final TalonSRX OmniTopMotor = new TalonSRX(5);
	public static final TalonSRX ElevatorMotor = new TalonSRX(6);

	public static final Talon IntakeLeftMotor = new Talon(0);
	public static final Talon IntakeRightMotor = new Talon(1);
	
	public static final Solenoid IntakeSolenoid = new Solenoid(0);
	
	public static final boolean EnableDriveBrake = false;
	public static final double DeadZoneLimit = 0.3;
	public static final double OmniDeadZoneLimit = 0.35;
	
	//Gains
	public static final double SkimGain = 0.15;
	public static final double TurnGain = 1.0;
	public static final double FinesseGain = 0.6;
	public static final double GyroGain = 0.05;
	public static final double LiftMaxSpeed = 1.0;
	public static final double LiftGain = 0.2;
	
	//Motor Reverse
	public static final boolean LeftDriveReversed   = false;
	public static final boolean RightDriveReversed  = true;
	public static final double Finesse = 0.3;
	
	//This is the min ramp rate per CTRE documentation
	public static final double MinTalonRampRate = 1.173; 
	public static final boolean RampEnabled = true;
	
	//This is the time to ramp from 0V to +/-12V in sec
	public static final double DriveRampTime = 0.25; 
	public static final double DriveRampRate = 0.5;
	public static final double OmniRampRate = 0.5;
	
	//Controllers
	public static final int DriveController = 0;
	public static final int OperatorController = 1;
	
	//Math
	public static final double PI = 3.1415962;
	
	//Sensors
	public static final ADXRS450_Gyro Gyro = new ADXRS450_Gyro();
	public static final int LeftUltrasonic = 1;
	public static final double SuppliedVoltage = 5.0;
	public static final double VoltsPerFiveMM = SuppliedVoltage/1024;
	public static final int LeftEncoderA = 0;
	public static final int LeftEncoderB = 1;
	public static final int RightEncoderA = 2;
	public static final int RightEncoderB = 3;
	public static final boolean LeftEncoderReversed = true;
	public static final boolean RightEncoderReversed = false;
	public static final double EncoderPulsesPerRev = 360; //in degrees
	public static final double WheelDiameter = 6.0; //in inches
	public static final double EncoderDistancePerPulse = 
									(WheelDiameter*PI)/(EncoderPulsesPerRev);
	
	public static final Encoder LeftWheelEncoder = new Encoder
			(LeftEncoderA,
            LeftEncoderB,
            LeftEncoderReversed,
            CounterBase.EncodingType.k4X);
	
	public static final Encoder RightWheelEncoder = new Encoder
			(RightEncoderA,
            RightEncoderB,
            RightEncoderReversed,
            CounterBase.EncodingType.k4X);
	
	public static final int CountsPerRevolution = 4096;
	public static final double OmniEncoderInchPerRotation = 1/(CountsPerRevolution/(WheelDiameter*PI));
	
	//Controller Mapping
	public static final int LeftJoyX = 0;
	public static final int LeftJoyY = 1;
	public static final int RightJoyX = 4;
	public static final int RightJoyY = 5;
	public static final int LeftTrigger = 2;
	public static final int RightTrigger = 3;
	public static final int ButtonA = 1;
	public static final int ButtonB = 2;
	public static final int ButtonX = 3;
	public static final int ButtonY = 4;
	public static final int LeftBumper = 5;
	public static final int RightBumper = 6;
	public static final int SelectButton = 7;
	public static final int Startbutton = 8;
	
}