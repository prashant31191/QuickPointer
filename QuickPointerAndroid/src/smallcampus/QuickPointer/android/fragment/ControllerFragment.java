package smallcampus.QuickPointer.android.fragment;

import smallcampus.QuickPointer.android.ConnectionManager;
import smallcampus.QuickPointer.android.MainActivity;
import smallcampus.QuickPointer.android.MySensor;
import smallcampus.QuickPointer.android.R;
import smallcampus.QuickPointer.net.BaseClient;
import smallcampus.QuickPointer.util.EventListener;
import android.content.Context;
import android.hardware.SensorManager;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.Toast;

public class ControllerFragment extends AbstractFragment{
	
	private static final String TAG = "ControllerFragment";
	
	/**
	 * ID for changing fragment
	 * @see MainActivity.ChangeFragmentHandler
	 */
	public static final int id = 3;
    
	private MySensor mSensor;
	private BaseClient client;
	
	/**
	 * Determine whether to send the coordinate message when the sensor has new data. 
	 * true when pointer button is pressed.
	 * 
	 */
	boolean sendFlag = false;
	final double[] center = new double[2];
	final double[] result = new double[2];
	final double thresholdX = 30,thresholdY = 20;
	
    @Override
	protected void setUI(){
    	//Get the connection set up-ed at the ConnectionFragment
    	client = ConnectionManager.getInstance().getConnection();
    	
		((Button) mView.findViewById(R.id.btn_pointer))
			.setOnTouchListener(new OnTouchListener(){
			@Override
			public boolean onTouch(View v, MotionEvent event) {
		        if(event.getAction() == MotionEvent.ACTION_DOWN) {
		            center[0] = result[0];
		            center[1] = result[1];
		        	sendFlag = true;
		         } else if (event.getAction() == MotionEvent.ACTION_UP) {
		            sendFlag = false;
		         }
				return false;
			}
			});
		
		((Button) mView.findViewById(R.id.btn_pageup))
			.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View v) {
					//TODO page up on click
					Toast.makeText(getActivity(), 
							"btn_page_up", Toast.LENGTH_SHORT).show();
				}});
		
		((Button) mView.findViewById(R.id.btn_pagedown))
			.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View v) {
					//TODO page down on click
					Toast.makeText(getActivity(), 
							"btn_page_down", Toast.LENGTH_SHORT).show();
				}});
		
		//Setup sensor
		mSensor = new MySensor((SensorManager) getActivity().getSystemService(Context.SENSOR_SERVICE));
		mSensor.setListener(new EventListener<float[]>(){
			@Override
			public void perform(float[] args) {
				result[0] = 90 + args[0]*180/Math.PI;
				result[1] = args[1]*180/Math.PI;
								
				//pbx.setProgress((int) result[0]);
				//pby.setProgress((int) -result[1]+90);
				
				if(sendFlag){
					double adjustment = 0;
					if(center[0]-thresholdX < 0 && result[0]>180+center[0]-thresholdX){
						adjustment = -180;
					}else if(center[0] + thresholdX > 180 && result[0]<center[0]-180 +thresholdX){
						adjustment = 180;
					}
					
					final float tempX = (float) ((result[0] -center[0] + adjustment +thresholdX)/thresholdX/2);
					final float tempY = (float) ((-center[1]+result[1]+thresholdY)/(2*thresholdY));
					
					//Log.d(TAG, "SendCoordinateMsg("+Math.max(Math.min(tempX, 1), 0)+","+ Math.max(Math.min(tempY, 1), 0)+")...");
					client.sendCoordinateData(Math.max(Math.min(tempX, 1), 0), Math.max(Math.min(tempY, 1), 0));
				}
			}
		});
	}

    @Override
	public int getFragmentLayoutId() {
		return R.layout.fragment_controller;
	}
    
	@Override
	public void onResume() {
		super.onResume();
		// register the sensor info to the progress bar UI
		mSensor.resume();
	}

	@Override
	public void onPause() {
	    super.onPause();
	    mSensor.pause();
	}
	
	@Override
	public void onDestroy(){
		super.onDestroy();
		client.disconnect();
	}
	
}
