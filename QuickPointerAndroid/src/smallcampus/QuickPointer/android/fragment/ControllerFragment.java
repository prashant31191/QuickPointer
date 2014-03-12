package smallcampus.QuickPointer.android.fragment;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import smallcampus.QuickPointer.android.Config;
import smallcampus.QuickPointer.android.ConnectionManager;
import smallcampus.QuickPointer.android.MainActivity;
import smallcampus.QuickPointer.android.QPSensor;
import smallcampus.QuickPointer.android.R;
import smallcampus.QuickPointer.net.BaseClient;
import android.content.Context;
import android.hardware.SensorManager;
import android.util.Log;
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
    
	private QPSensor sensor;
	private BaseClient client;
	
	/**
	 * Determine whether to send the coordinate message when the sensor has new data. 
	 * true when pointer button is pressed.
	 * 
	 */
	final double[] center = new double[2];
	final double thresholdX = Config.POINTER_THRESHOLD_X,
			thresholdY = Config.POINTER_THRESHOLD_Y;
	
    @Override
	protected void setUI(){
    	//Get the connection set up-ed at the ConnectionFragment
    	client = ConnectionManager.getInstance().getConnection();
    	
    	//possible from debug mode
//    	if(client==null){
//    		//create a dummy client
//    		try {
//				client = new QPTcpUdpClient("localhost", Config.DEFAULT_TCP_SERVER_PORT, Config.DEFAULT_UDP_SERVER_PORT);
//			} catch (UnknownHostException e) {
//			} catch (SocketException e) {
//			}
//    	}
		
    	//Setup sensor
		sensor = new QPSensor((SensorManager) getActivity().getSystemService(Context.SENSOR_SERVICE));
		
		final SendTask sendTask = new SendTask();
		Timer sendTimer = new Timer();
		
		final long delay = Config.SEND_DELAY;
		
		sendTimer.schedule(sendTask, 0, delay);
		
		((Button) mView.findViewById(R.id.btn_pointer))
			.setOnTouchListener(new OnTouchListener(){
			@Override
			public boolean onTouch(View v, MotionEvent event) {
		        if(event.getAction() == MotionEvent.ACTION_DOWN) {
		        	float[] result;
					try {
						result = sensor.fastRead();
					
		        	
						result[0] = (float) (90 + result[0]*180/Math.PI);
						result[1] = (float) (result[1]*180/Math.PI);
			        	
			            center[0] = result[0];
			            center[1] = result[1];
			            
	//					Log.d(TAG, "center[0]: " +center[0]);
	//					Log.d(TAG, "center[1]: "+ center[1]);
			        	
						sendTask.sendFlag = true;
					} catch (IOException e) {
						Log.e(TAG, e.getMessage());
						e.printStackTrace();
					}
		         } else if (event.getAction() == MotionEvent.ACTION_UP) {
		            sendTask.sendFlag = false;
		         }
				return false;
			}
			});
		
		((Button) mView.findViewById(R.id.btn_pageup))
			.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View v) {
					client.sendPageUpControl();
					Toast.makeText(getActivity(), 
							"Page up", Toast.LENGTH_SHORT).show();
				}});
		
		((Button) mView.findViewById(R.id.btn_pagedown))
			.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View v) {
					client.sendPageDownControl();
					Toast.makeText(getActivity(), 
							"Page down", Toast.LENGTH_SHORT).show();
				}});	

	}

    @Override
	public int getFragmentLayoutId() {
		return R.layout.fragment_controller;
	}
    
	@Override
	public void onResume() {
		super.onResume();
		if(!client.isConnected()){
			Toast.makeText(getActivity(), "Connection is lost", Toast.LENGTH_LONG).show();	
			MainActivity.getChangeFragmentHandler().changeFragment(IntroductionFragment.id);
		}else{
			sensor.resume();
		}
	}

	@Override
	public void onPause() {
	    super.onPause();
	    sensor.pause();
	    client.disconnect();
	}
	
	@Override
	public void onDestroy(){
		super.onDestroy();
	}
	
	
	class SendTask extends TimerTask{
		public boolean sendFlag = false;
		@Override
		public void run() {
			float[] result;
			if(sendFlag){
				try{
					result = sensor.read();
					
					result[0] = (float) (90 + result[0]*180/Math.PI);
					result[1] = (float) (result[1]*180/Math.PI);
					
	//				Log.d(TAG, "result[0]: " +result[0]);
	//				Log.d(TAG, "result[1]: "+ result[1]);
										
					double adjustment = 0;
					if(center[0]-thresholdX < 0 && result[0]>180+center[0]-thresholdX){
						adjustment = -180;
					}else if(center[0] + thresholdX > 180 && result[0]<center[0]-180 +thresholdX){
						adjustment = 180;
					}
					
					final float tempX = (float) ((result[0] -center[0] + adjustment +thresholdX)/thresholdX/2);
					final float tempY = (float) ((-center[1]+result[1]+thresholdY)/(2*thresholdY));
					
					client.sendCoordinateData(Math.max(Math.min(tempX, 1), 0), Math.max(Math.min(tempY, 1), 0));
				}catch(IOException e){
					Log.e(TAG, e.getMessage());
					e.printStackTrace();
				}
			}
		}
	}
}
