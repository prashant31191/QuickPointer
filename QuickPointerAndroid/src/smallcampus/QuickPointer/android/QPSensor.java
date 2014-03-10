package smallcampus.QuickPointer.android;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

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
		gravity = new LinkedList<float[]>();
		magnet = new LinkedList<float[]>();
	}
	
	private float[] matrixR = new float[9];
	
	private List<float[]> gravity,magnet;
	private final int maxResults = Config.SENSOR_NUM_FRAMES;// maximum ~= 10 per 0.2 sec
	
	private float[] getMedian(List<float[]> tMagnetList){
		float count;
		float[] temp;
		float[] data_array;
		int[] index_array;
		int i;
		
		Iterator<float[]> it = tMagnetList.iterator();
		data_array = new float[tMagnetList.size()];
		index_array = new int[tMagnetList.size()];
		
		for(i=0;it.hasNext();i++){
			temp = it.next();
			count = temp[0];
			count+= temp[1];
			count+= temp[2];
			
			//copy the data
			data_array[i] = count;
			//set up the index
			index_array[i] = i;
		}
		
		//sort half of the array
		float t;
		int y;
		for(i=0; i<tMagnetList.size()/2; i++){
			int j = tMagnetList.size()-1;
			while(j>i){
				if(data_array[j]<data_array[j-1]){
					t = data_array[j-1];
					y = index_array[j-1];
					data_array[j-1] = data_array[j];
					index_array[j-1] = index_array[j];
					data_array[j] = t;
					index_array[j] = y;
				}
				j--;
			}
		}
		
		//get and return the result
		it = tMagnetList.iterator();
		i = index_array[i];

		while(i>0){
			it.next();
			i--;
		}
		
		return it.next();
	}
	
	public float[] read(){
		float[] result = new float[2];
		float[] tGravity = new float[3], tMagnet = new float[3];
		float[] tg,tm;
		
		//Averaging the gravity component
		if(gravity.size()>=maxResults && magnet.size()>=maxResults){
			List<float[]> tMagnetList = new LinkedList<float[]>();
			//process new data
			for(int i = 0; i<maxResults; i++){
				tg = gravity.remove(0);
				tm = magnet.remove(0);
				
				tGravity[0] += tg[0];
				tGravity[1] += tg[1];
				tGravity[2] += tg[2];
				
				tMagnetList.add(tm);
				//tMagnet[0] += tm[0];
				//tMagnet[1] += tm[1];
				//tMagnet[2] += tm[2];
			}
			
			tGravity[0] = tGravity[0]/maxResults;
			tGravity[1] = tGravity[1]/maxResults;
			tGravity[2] = tGravity[2]/maxResults;
			
//			tMagnet[0] = tMagnet[0]/maxResults;
//			tMagnet[1] = tMagnet[1]/maxResults;
//			tMagnet[2] = tMagnet[2]/maxResults;

			tMagnet = getMedian(tMagnetList);
			
			SensorManager.getRotationMatrix(matrixR, null, tGravity, tMagnet);
		}else{
			Log.d("QPSensor", "Not enough data");
			//use the old data
		}
		
		//Azimuth angle calculation
		result[0] = (float) Math.atan((matrixR[1]-matrixR[3])/(matrixR[0]+matrixR[4]));
		
		//Pitch angle calculation
		result[1] = (float) Math.asin(-matrixR[7]);
		
		return result;
	}
	/**
	 * Is SensorManager registered
	 */
	boolean isReg = false;
			
    private final SensorEventListener mEventListener = new SensorEventListener() {
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
        }

        public void onSensorChanged(SensorEvent event) {
            // Handle the events for which we registered
            switch (event.sensor.getType()) {
                case Sensor.TYPE_GRAVITY:
            		float[] g = new float[3];
               		System.arraycopy(event.values, 0, g, 0, 3);
                	
                	if(gravity.size()>=maxResults){
                		gravity.remove(gravity.size()-1);
                	}
                	gravity.add(g);
                    break;

                case Sensor.TYPE_MAGNETIC_FIELD:
                	float[] m = new float[3];
                	System.arraycopy(event.values, 0, m, 0, 3);
                	
                	if(magnet.size()>=maxResults){
                		magnet.remove(magnet.size()-1);
                	}
                	magnet.add(m);
                    break;
            }
        };
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
