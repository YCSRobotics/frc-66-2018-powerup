package org.usfirst.frc.team470.robot;

import edu.wpi.first.wpilibj.Encoder;

public class SensorData {

		private static Encoder leftEncoder = Constants.LeftWheelEncoder;
		private static Encoder rightEncoder = Constants.RightWheelEncoder;
		
		//set the how much distance is travelled per pulse
		public SensorData() {
			
			leftEncoder.setDistancePerPulse(Constants.EncoderDistancePerPulse);
			rightEncoder.setDistancePerPulse(Constants.EncoderDistancePerPulse);
		
		}
		
		public double getLeftWheelDistance() {
			
			return leftEncoder.getDistance();
			
		}
		
		public double getRightWheelDistance() {
			
			return rightEncoder.getDistance();
			
		}
		
		public double getMainAvgDistance() {
			
			double ave;		
			ave = (leftEncoder.getDistance() + rightEncoder.getDistance())/2;
			return ave;
			
		}
		
		public void resetEncoder() {
			
			leftEncoder.reset();
			rightEncoder.reset();
			
		}
		
}
