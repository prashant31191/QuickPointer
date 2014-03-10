package smallcampus.QuickPointer.android;
import android.hardware.SensorManager;


public final class Config {
	//Time delay interval of sending coordinate msg 
	public static final int SEND_DELAY = 100;
	public static final int SENSOR_DELAY = SensorManager.SENSOR_DELAY_GAME;
	
	//Max ~= 10 for SEND_DELAY = 200ms, DELAY_NORMAL
	//Max = 5 for SEND_DELAY =100ms, DELAY_GAME
	public static final int SENSOR_NUM_FRAMES = 5; 
	
	public static final int POINTER_THRESHOLD_X = 40;
	public static final int POINTER_THRESHOLD_Y = 20;
	
}
