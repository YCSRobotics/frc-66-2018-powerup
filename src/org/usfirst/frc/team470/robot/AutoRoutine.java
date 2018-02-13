package org.usfirst.frc.team470.robot;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

import edu.wpi.first.wpilibj.ADXRS450_Gyro;

public class AutoRoutine {

	//put the modules for autonomous programs here. 
	//the goal is to keep things portable
	
	//Motors
	private static TalonSRX leftMasterMotor = Constants.LeftMasterMotor;
	private static TalonSRX leftSlaveMotor  = Constants.LeftSlaveMotor;
	private static TalonSRX rightMasterMotor = Constants.RightMasterMotor;
	private static TalonSRX rightSlaveMotor = Constants.RightSlaveMotor;
	private static TalonSRX rightOmniMotor = Constants.OmniMasterMotor;
	private static TalonSRX leftOmniMotor  = Constants.OmniSlaveMotor;
	
	//Sensor Information
	private static ADXRS450_Gyro gyro = Constants.Gyro;
	private static boolean isGyroZero = false;
	
	private static boolean stopRobot = false;
	
	private static double targetTurn = 0.0;
	private static double turn = 0.0;
	private static double t_left = 0.0;
	private static double t_right = 0.0;
	
	SensorData sensor = new SensorData();
	
	public AutoRoutine() {
		
				//Ramping Left
				leftMasterMotor.configOpenloopRamp(Constants.DriveRampRate, 5);
				leftSlaveMotor.set(ControlMode.Follower, leftMasterMotor.getDeviceID());
				
				//Ramping Right
				rightMasterMotor.configOpenloopRamp(Constants.DriveRampRate, 5);
				rightSlaveMotor.set(ControlMode.Follower, rightMasterMotor.getDeviceID());
				
				//Ramping Omni
				leftOmniMotor.configOpenloopRamp(Constants.OmniRampRate, 5);
				rightOmniMotor.configOpenloopRamp(Constants.OmniRampRate, 5);
				
				//Calibrate Gyro
				gyro.calibrate();
		
	}
	
	public void goStraight(double distance, double throttle) {
		
		stopRobot = false;
		
		System.out.println("Go Straight Stop Robot =" + stopRobot);
		
		while (stopRobot == false) {
			//if the gyro is zeroed, and stop robot has not been triggered 
			if(isGyroZero) {
					
				targetTurn = -1 * (gyro.getAngle() * Constants.GyroGain);
				
				turn =  targetTurn;
				
				turn = turn * (Constants.TurnGain * Math.abs(throttle));
				
				t_left = throttle + turn;
				t_right = throttle - turn;
				
				if(distance >= sensor.getAvgDistance()) {
					
					leftMasterMotor.set(ControlMode.PercentOutput, t_left);
					rightMasterMotor.set(ControlMode.PercentOutput, -t_right);
					
				} else {
				
					stopRobot = true;
					leftMasterMotor.set(ControlMode.PercentOutput, 0);
					rightMasterMotor.set(ControlMode.PercentOutput, 0);
					return;
					
				}
						
			} else {
				
				gyro.reset();
				isGyroZero = true;
				
			}
			
		}
		
		sensor.resetEncoder();
		isGyroZero = false;
		
	}
	
	public void turnAngle(double targetAngle, double throttle) {
		
		stopRobot = false;
		
		System.out.println("Not enterin loop 1");
		
		if (isGyroZero) {
			
			System.out.println("It not entering loop");
			System.out.println(stopRobot);
			
			while (stopRobot == false) {
				
				System.out.println("Moving Robot");
				if (targetAngle > 0) {
					
					while (gyro.getAngle() < targetAngle + 3) {
						
						leftMasterMotor.set(ControlMode.PercentOutput, throttle);
						
					}
					
					
				} else {
					
					while (gyro.getAngle() > targetAngle - 3) {
						
						rightMasterMotor.set(ControlMode.PercentOutput, -throttle);
						
					}
					
				}
				
				stopRobot = true;
				
			}
			
		} else {
			
			gyro.reset();
			isGyroZero = true;
			
		}
		
		sensor.resetEncoder();
		isGyroZero = false;
		
	}
	
	public void stopRobot() {
		
		while (true) {
			
			//intentional infinite loop
			
		}
		
	}
	
	public void test() {
		
		System.out.println("Going Straight");
		goStraight(50, 0.25);
		System.out.println("Turning");
		turnAngle(45, 0.3);
		goStraight(-50, 0.25);
		stopRobot();
		
	}
	
}
