/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package org.usfirst.frc.team470.robot;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the build.properties file in the
 * project.
 */
public class Robot extends TimedRobot {
	private static final String kDefaultAuto    	= "Default";
	private static final String kCenterAuto     	= "Center - One Cube";
	private static final String kCenterTwoCube  	= "Center - Two Cubes";
	private static final String kLeftOnlySwitch    	= "Left Only - Switch Priority";
	private static final String kLeftOnlyScale     	= "Left Only - Scale Priority";
	private static final String kLeftSwitchAuto    	= "Left Start - Switch/Scale/Cross Scale";
	private static final String kLeftScaleAuto     	= "Left Start - Scale/Cross Scale";
	private static final String kLeftScaleTwoCube  	= "Left Start - Two Cube Scale";
	private static final String kRightOnlySwitch 	= "Right Only - Switch Priority";
	private static final String kRightOnlyScale  	= "Right Only - Scale Priority";
	private static final String kRightSwitchAuto 	= "Right Start - Switch/Scale/Cross Scale";
	private static final String kRightScaleAuto     = "Right Start - Scale/Cross Scale";
	private static final String kRightScaleTwoCube  = "Right Start - Two Cube Scale";
	
	private String m_autoSelected;
	private SendableChooser<String> m_chooser = new SendableChooser<>();
	
	AutoRoutine autonomous = new AutoRoutine();
	Drivetrain drivetrain = new Drivetrain();
	Elevator elevator = new Elevator();
	DashboardState dashboard = new DashboardState();
	Intake intake = new Intake();
	Lifts lifts = new Lifts();
	

	/**
	 * This function is run when the robot is first started up and should be
	 * used for any initialization code.
	 */
	@Override
	public void robotInit() {
		m_chooser.addDefault("Default Auto", kDefaultAuto);
		//Center Start Auton Options
		m_chooser.addObject("Center - One Cube", kCenterAuto);
		m_chooser.addObject("Center - Two Cubes", kCenterTwoCube);
		//Left Start Auton Options
		m_chooser.addObject("Left Only - Switch Priority", kLeftOnlySwitch);
		m_chooser.addObject("Left Only - Scale Priority", kLeftOnlyScale);
		m_chooser.addObject("Left Start - Switch/Scale/Cross Scale", kLeftSwitchAuto);
		m_chooser.addObject("Left Start - Scale/Cross Scale", kLeftScaleAuto);
		m_chooser.addObject("Left Start - Two Cube Scale", kLeftScaleTwoCube);
		//Right Start Auton Options
		m_chooser.addObject("Right Only - Switch Priority", kRightOnlySwitch);
		m_chooser.addObject("Right Only - Scale Priority", kRightOnlyScale);
		m_chooser.addObject("Right Start - Switch/Scale/Cross Scale", kRightSwitchAuto);
		m_chooser.addObject("Right Start - Scale/Cross Scale", kRightScaleAuto);
		m_chooser.addObject("Right Start - Two Cube Scale", kRightScaleTwoCube);
		
		SmartDashboard.putData("Auto choices", m_chooser);
		
		//elevator.zeroElevatorPosition();
	}

	public void disabledPeriodic(){
		SmartDashboard.putData("Auto choices", m_chooser);
		elevator.updateElevatorDisabled();
		dashboard.updateSmartDashboard();
	}
	/**
	 * This autonomous (along with the chooser code above) shows how to select
	 * between different autonomous modes using the dashboard. The sendable
	 * chooser code works with the Java SmartDashboard. If you prefer the
	 * LabVIEW Dashboard, remove all of the chooser code and uncomment the
	 * getString line to get the auto name from the text box below the Gyro
	 *
	 * <p>You can add additional auto modes by adding additional comparisons to
	 * the switch structure below with additional strings. If using the
	 * SendableChooser make sure to add them to the chooser code above as well.
	 */
	@Override
	public void autonomousInit() {
		m_autoSelected = m_chooser.getSelected();
		//intake.intakeInit();
		//m_autoSelected = kCenterAuto;
		
		autonomous.setFmsPlateAssignment(DriverStation.getInstance().getGameSpecificMessage());
		//m_autoSelected = SmartDashboard.getString("Auto Selector", kDefaultAuto);
		
		System.out.println("Auto selected: " + m_autoSelected);
		
		switch (m_autoSelected) {
		case kCenterAuto:
			// Put custom auto code here
			autonomous.setSelectedAutonRoutine(AutoRoutine.CENTER_SWITCH);
			break;
		case kCenterTwoCube:
			// Put custom auto code here
			autonomous.setSelectedAutonRoutine(AutoRoutine.CENTER_SWITCH_2_CUBE);
			break;
		case kLeftOnlySwitch:
			// Put custom auto code here
			autonomous.setSelectedAutonRoutine(AutoRoutine.LEFT_ONLY_SWITCH);
			break;
		case kLeftOnlyScale:
			// Put custom auto code here
			autonomous.setSelectedAutonRoutine(AutoRoutine.LEFT_ONLY_SCALE);
			break;
		case kLeftSwitchAuto:
			// Put custom auto code here
			autonomous.setSelectedAutonRoutine(AutoRoutine.LEFT_START_SWITCH);
			break;
		case kLeftScaleAuto:
			// Put custom auto code here
			autonomous.setSelectedAutonRoutine(AutoRoutine.LEFT_START_SCALE);
			break;
		case kLeftScaleTwoCube:
			// Put custom auto code here
			autonomous.setSelectedAutonRoutine(AutoRoutine.LEFT_START_2_CUBE);
			break;
		case kRightOnlySwitch:
			// Put custom auto code here
			autonomous.setSelectedAutonRoutine(AutoRoutine.RIGHT_ONLY_SWITCH);
			break;
		case kRightOnlyScale:
			// Put custom auto code here
			autonomous.setSelectedAutonRoutine(AutoRoutine.RIGHT_ONLY_SCALE);
			break;
		case kRightSwitchAuto:
			// Put custom auto code here
			autonomous.setSelectedAutonRoutine(AutoRoutine.RIGHT_START_SWITCH);
			break;
		case kRightScaleAuto:
			// Put custom auto code here
			autonomous.setSelectedAutonRoutine(AutoRoutine.RIGHT_START_SCALE);
			break;
		case kRightScaleTwoCube:
			// Put custom auto code here
			autonomous.setSelectedAutonRoutine(AutoRoutine.RIGHT_START_2_CUBE);
			break;
		case kDefaultAuto:
		default:
			autonomous.setSelectedAutonRoutine(AutoRoutine.DO_NOTHING);
		break;
		}
	}

	/**
	 * This function is called periodically during autonomous.
	 */
	@Override
	public void autonomousPeriodic() {
		
		autonomous.setFmsPlateAssignment(DriverStation.getInstance().getGameSpecificMessage());
		
		autonomous.updateAutoRoutine();
		drivetrain.updateDrivetrainAuton();
		elevator.updateElevatorAuton();
		dashboard.updateSmartDashboard();
	}

	public void teleopInit(){
		//elevator.zeroElevatorPosition();
		//intake.intakeInit();
		Drivetrain.enableDrivetrainDynamicBraking(false);
	}
	/**
	 * This function is called periodically during operator control.
	 */
	@Override
	public void teleopPeriodic() {
		
		intake.updateIntake();
		dashboard.updateSmartDashboard();
		drivetrain.updateDrivetrainTeleop();
		elevator.updateElevatorTeleop();
		lifts.updateLiftsTeleop();
		
	}

	/**
	 * This function is called periodically during test mode.
	 */
	@Override
	public void testPeriodic() {
	}
}
