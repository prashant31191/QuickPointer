package smallcampus.QuickPointer.android;

import java.util.Timer;
import java.util.TimerTask;

import smallcampus.QuickPointer.util.EventListener;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

public class MySensor {
	private SensorManager mSensorManager;
	
	private float[] mValuesGravity = new float[3];
	private float[] mValuesMagnet = new float[3];
	private float[] matrixR = new float[9];
	
	/**
	 * Storing the result from sensor data
	 * result[0] = Azimuth angle (radiant)
	 * result[1] = Pitch angle (radiant)
	 */
	private float[] result =  new float[2];
	
	/**
	 * Is SensorManager registered
	 */
	boolean isReg = false;
	
	final int interval = SensorManager.SENSOR_DELAY_NORMAL;
	
	/**
	 * time interval for generating sensor results
	 * (millisecond)
	 */
	final int timerInterval = 200;
	
	private Timer calculateTimer ;
	
    private final SensorEventListener mEventListener = new SensorEventListener() {
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
        }

        public void onSensorChanged(SensorEvent event) {
            // Handle the events for which we registered
            switch (event.sensor.getType()) {
                case Sensor.TYPE_GRAVITY:
                	synchronized(mValuesGravity){
                		System.arraycopy(event.values, 0, mValuesGravity, 0, 3);
                	}
                    break;

                case Sensor.TYPE_MAGNETIC_FIELD:
                	synchronized(mValuesMagnet){
                		System.arraycopy(event.values, 0, mValuesMagnet, 0, 3);
                	}
                    break;
            }
        };
    };
	
	public MySensor(SensorManager manager){
		mSensorManager = manager;
	}
		
	private EventListener<float[]> mListener = null;
	/**
	 * listen to sensor result generation
	 * @param mEventListener 
	 * @see timerInterval
	 */
	public void setListener(EventListener<float[]> mEventListener){
		mListener = mEventListener;
	}
	

	
	//---------------------------------------
	/**
	 * for android activity life cycle
	 */
	public void pause()
	{
		if(isReg){
			mSensorManager.unregisterListener(mEventListener);
			calculateTimer.cancel();
			calculateTimer.purge();
			isReg = false;
		}
	}
	
	/**
	 * for android activity life cycle
	 */
	public void resume()
	{
		if(!isReg){
			mSensorManager.registerListener(mEventListener, mSensorManager.getDefaultSensor(Sensor.TYPE_GRAVITY), 
	                interval);
	        mSensorManager.registerListener(mEventListener, mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD), 
	                interval);
	        
	        calculateTimer = new Timer();
			calculateTimer.schedule(new CalculationTask(), 1000,timerInterval);
	        isReg = true;
		}
	}
	
	//----------------------------------------
	
	private class CalculationTask extends TimerTask{
		float[] gravity = new float[3];
		float[] magnet = new float[3];

		@Override
		public void run() {
			if(mListener!=null){
				synchronized(mValuesGravity){
					gravity[0] = mValuesGravity[0];
					gravity[1] = mValuesGravity[1];
					gravity[2] = mValuesGravity[2];
				}
				synchronized(mValuesMagnet){
					magnet[0] = mValuesMagnet[0];
					magnet[1] = mValuesMagnet[1];
					magnet[2] = mValuesMagnet[2];
				}
				
				SensorManager.getRotationMatrix(matrixR, null, gravity, magnet);
				
				//Azimuth angle calculation
				result[0] = (float) Math.atan((matrixR[1]-matrixR[3])/(matrixR[0]+matrixR[4]));
				
				//Pitch angle calculation
				result[1] = (float) Math.asin(-matrixR[7]);		
				
				mListener.perform(result);
			}
		}
	};
    
}
