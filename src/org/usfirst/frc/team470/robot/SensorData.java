package org.usfirst.frc.team470.robot;

import edu.wpi.first.wpilibj.Encoder;

public class SensorData {

		private static Encoder leftEncoder = Constants.LeftWheelEncoder;
		private static Encoder rightEncoder = Constants.RightWheelEncoder;
		
		public SensorData() {
			
			leftEncoder.setDistancePerPulse(Constants.EncoderDistancePerPulse);
			rightEncoder.setDistancePerPulse(Constants.EncoderDistancePerPulse);
		
		}
		
		public double getAvgDistance() {
			
			double ave;		
			ave = (leftEncoder.getDistance() + rightEncoder.getDistance())/2;
			return ave;
			
		}
		
}
