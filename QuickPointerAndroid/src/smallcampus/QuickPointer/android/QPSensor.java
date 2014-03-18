package smallcampus.QuickPointer.android;

import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;

import smallcampus.QuickPointer.util.EventListener;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;

public class QPSensor {
	private SensorManager mSensorManager;

	public QPSensor(SensorManager manager){
		this();
		mSensorManager = manager;
	}
	
	protected QPSensor(){
		resultBuf = new LinkedList<float[]>();
	}
	
	private static float[] attitude = new float[3];
	
	private final static double RAD2DEG = 180 / Math.PI;
	
	private static final float ALPHA = 0.2f;
	
	//Filter
		public static float[] filter(float[] newValues, float[] previousValues) {
			for(int i = 0; i < newValues.length; i++) {
				previousValues[i] = previousValues[i] + ALPHA * (newValues[i] - previousValues[i]);
			}
			return previousValues;
		}
	
	private Queue<float[]> resultBuf;
	private final int maxResults = Setting.SENSOR_NUM_FRAMES;// maximum ~= 10 per 0.2 sec	
	
	public float[] read() throws IOException{
		float[] result = new float[3]; 
		Arrays.fill(result, 0f);
		
		float[] temp;
		int i;
		
		if(resultBuf.size()<1){
			throw new IOException("Sensor data null");
		}
		
		for(i =0; i<resultBuf.size();i++){
			temp = resultBuf.remove();
			result[0] += temp[0];
			result[1] += temp[1];
			result[2] += temp[2];
		}
		
		result[0] /=i;
		result[1] /=i;
		result[2] /=i;
		
		return result;
	}
	
	/**
	 * Is SensorManager registered
	 */
	boolean isReg = false;
	
	EventListener<float[]> onDataProduced;
	public void setOnDataProducedListener(EventListener<float[]> listener){
		onDataProduced = listener;
	}
	
    private final SensorEventListener mEventListener = new SensorEventListener() {
    	float[] magneticField = new float[3], gravityField = new float[3];
    	float[] matrixR = new float[9];
    	
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
        }

        public void onSensorChanged(SensorEvent event) {
            // Handle the events for which we registered
            switch (event.sensor.getType()) {
                case Sensor.TYPE_GRAVITY:
                	gravityField = filter(event.values.clone(), gravityField);
                	break;
                case Sensor.TYPE_MAGNETIC_FIELD:
                	magneticField = filter(event.values.clone(), magneticField);
                    break;
            }
            
            SensorManager.getRotationMatrix(matrixR, null, gravityField, magneticField);
            
            SensorManager.getOrientation(matrixR, attitude);

			attitude[0] = (float) (attitude[0] * RAD2DEG);
			attitude[1] = (float) (attitude[1] * RAD2DEG);
			attitude[2] = (float) (attitude[2] * RAD2DEG);
			
			//Log.d("QPSensor", "("+attitude[0]+","+attitude[1]+","+attitude[2]+")");
			
			if(resultBuf.size()>maxResults){
				resultBuf.remove();
			}
			
			resultBuf.add(attitude);
        }

    };
	
	//---------------------------------------
	/**
	 * for android activity life cycle
	 */
	public void pause()
	{
		if(isReg){
			mSensorManager.unregisterListener(mEventListener);
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
					SensorManager.SENSOR_DELAY_GAME);
	        mSensorManager.registerListener(mEventListener, mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD), 
	        		SensorManager.SENSOR_DELAY_GAME);
	        isReg = true;
		}
	}
	
	//----------------------------------------
	

}
