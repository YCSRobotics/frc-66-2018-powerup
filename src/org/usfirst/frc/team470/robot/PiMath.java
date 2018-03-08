package org.usfirst.frc.team470.robot;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class PiMath {
	
	private static double getImageSizeInDeg() {
		
		double nPixels = SmartDashboard.getNumber("CubeWidth", 0);
		
		return (nPixels * Constants.DEG_PER_PIXEL);
		
	}
	
	public static double getCubeDistance() {
		
		double distance;
		double radians = Math.toRadians(getImageSizeInDeg());
		distance = ((5.125)/(Math.tan(radians)));
		
		return distance; //returns distance to target in inches
		
	}
	
	public static double angleToCube(String target) {
				
		double degAngle = ((SmartDashboard.getNumber("CubeCenterOfTarget", 0) - 
				(Constants.CAMERA_WIDTH/2)) * Constants.DEG_PER_PIXEL);
		
		return degAngle; //returns the angle to target
		
	}
	
	public static boolean isValidTargetPresent(){
		
		return(!SmartDashboard.getBoolean("NoContoursFound", true));
		
	}
}

//how to use, simply call the method and you'll have your value