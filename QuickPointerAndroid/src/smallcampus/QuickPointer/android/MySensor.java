package smallcampus.QuickPointer.android;

import java.util.Timer;
import java.util.TimerTask;

import smallcampus.QuickPointer.net.EventListener;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

public class MySensor {
	private SensorManager mSensorManager;
	//private Sensor mSensor;
	
	private float[] mValuesAccel = new float[3];
	private float[] mValuesMagnet = new float[3];
	private float[] matrixR = new float[9];
	private float[] matrixI = new float[9];
	float[] mOrientation = new float[3];
	private float[] result =  new float[2]; // 0 inclination, 1  direction
	
	private Timer calculateTimer ;
	private class CalculationTask extends TimerTask{
		float[] accel = new float[3];
		float[] magnet = new float[3];
		float[] temp = new float[9];		
		@Override
		public void run() {
			if(mListener!=null){
				synchronized(mValuesAccel){
					accel[0] = mValuesAccel[0];
					accel[1] = mValuesAccel[1];
					accel[2] = mValuesAccel[2];
				}
				synchronized(mValuesMagnet){
					magnet[0] = mValuesMagnet[0];
					magnet[1] = mValuesMagnet[1];
					magnet[2] = mValuesMagnet[2];
				}
				
				SensorManager.getRotationMatrix(matrixR, null, accel, magnet);
				//SensorManager.remapCoordinateSystem(temp, SensorManager.AXIS_X, SensorManager.AXIS_Z, matrixR);
				//result[0] = SensorManager.getInclination(matrixI);
				result[1] = SensorManager.getOrientation(matrixR, mOrientation)[0];
				result[0] = (float) Math.atan(mOrientation[0]/Math.sqrt((mOrientation[2]*mOrientation[2]+mOrientation[1]*mOrientation[1])));
				
				mListener.perform(result);
			}
		}
	};
	
	private EventListener<float[]> mListener = null;
	public void setListener(EventListener<float[]> mEventListener){
		mListener = mEventListener;
	}
	
	public MySensor(SensorManager manager){
		mSensorManager = manager;
	}
	
	//---------------------------------------
	boolean isReg = false;
	final int interval = SensorManager.SENSOR_DELAY_NORMAL;
	final int timerInterval = 500;
	
	public void pause()
	{
		if(isReg){
			mSensorManager.unregisterListener(mEventListener);
			calculateTimer.cancel();
			calculateTimer.purge();
			isReg = false;
		}
	}
	
	public void resume()
	{
		if(!isReg){
			mSensorManager.registerListener(mEventListener, mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), 
	                interval);
	        mSensorManager.registerListener(mEventListener, mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD), 
	                interval);
	        
	        calculateTimer = new Timer();
			calculateTimer.schedule(new CalculationTask(), 1000,timerInterval);
	        isReg = true;
		}
	}
	
	//----------------------------------------
	
    final SensorEventListener mEventListener = new SensorEventListener() {
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
        }

        public void onSensorChanged(SensorEvent event) {
            // Handle the events for which we registered
            switch (event.sensor.getType()) {
                case Sensor.TYPE_ACCELEROMETER:
                	synchronized(mValuesAccel){
                		System.arraycopy(event.values, 0, mValuesAccel, 0, 3);
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
    
}
