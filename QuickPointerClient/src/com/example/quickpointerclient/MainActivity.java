package com.example.quickpointerclient;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;


public class MainActivity extends Activity {

	private Client client;
	private final int port = 9999;
	SeekBar barX, barY;
	
	private SensorManager mSensorManager;
	private Sensor mSensor;
	
	protected TextView textX, textY, textZ;
	protected Button bindBtn;
	private boolean isBinded =false;
	
	protected int systemDimX = 800, systemDimY = 600;
		
	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		barX = (SeekBar)findViewById(R.id.seekBar1);
		barX.setMax(systemDimX);
		barY = (SeekBar)findViewById(R.id.seekBar2);
		barY.setMax(systemDimY);
		final Button connectBtn = (Button) findViewById(R.id.ConnectBtn); 
		textX = (TextView) findViewById(R.id.TextX);
		textY = (TextView) findViewById(R.id.TextY);
		textZ = (TextView) findViewById(R.id.TextZ);
		bindBtn = (Button) findViewById(R.id.bindBtn);
		
		bindBtn.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				if(isBinded){
					isBinded = false;
					bindBtn.setText("Bind");
					unbindSensorWithSeekBar();
				}else{
					isBinded = true;
					bindBtn.setText("Unbind");
					sensorBindListener.mean_azimuth = seListener.azimuth_angle;
					sensorBindListener.mean_pitch = seListener.pitch_angle;
					sensorBindListener.mean_roll = seListener.roll_angle;
					bindSensorWithSeekBar();
				}
			}
		});
		
		connectBtn.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				String hostName = ((EditText) findViewById(R.id.editText1)).getText().toString();
				client = new Client(hostName,port);
				client.onSocketConnectedListener = new Client.OnSocketConnectedListener(){
					@Override
					public void onSocketConnected(int ret) {
						//TODO get system dimension information systemDimX & systemDimY
						if(ret==0){
							showAlert();
						}
					}
				};
				client.connectHost();
			}
			
		});
		
		barX.setOnSeekBarChangeListener(onSeekBarChangeListener);
		barY.setOnSeekBarChangeListener(onSeekBarChangeListener);
		
		mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
		mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION);
	}
	
	//Seekbar event listener
	private OnSeekBarChangeListener onSeekBarChangeListener = new OnSeekBarChangeListener(){
		@Override
		public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {}

		@Override
		public void onStartTrackingTouch(SeekBar seekBar) {}

		@Override
		public void onStopTrackingTouch(SeekBar seekBar) {
			sendCoordinateMessage(barX.getProgress(),barY.getProgress());
		}
	};//End of Seekbar event listener
	
	//Orientation Event listener
	class MySensorEventListener implements SensorEventListener{
		@Override
		public void onAccuracyChanged(Sensor arg0, int arg1) {return;}
		
	    public float azimuth_angle;
	    public float pitch_angle;
	    public float roll_angle;
		@Override
		public void onSensorChanged(SensorEvent event) {
		    azimuth_angle = event.values[0];
		    pitch_angle = event.values[1];
		    roll_angle = event.values[2];
		    
		    textX.setText(String.valueOf(azimuth_angle));
		    textY.setText(String.valueOf(pitch_angle));
		    textZ.setText(String.valueOf(roll_angle));
		}
	}
	private MySensorEventListener seListener = new MySensorEventListener();
	
	class SensorBindListener implements SensorEventListener{
		public float threshold = 8.0f;
		public float mean_azimuth = 0, mean_pitch = 0, mean_roll = 0;
		@Override
		public void onAccuracyChanged(Sensor arg0, int arg1) {return;}
		
		@Override
		public void onSensorChanged(SensorEvent event) {
			// TODO Auto-generated method stub
		    float a = event.values[0];
		    float p = event.values[1];
		    //float r = event.values[2];
		    
		    int x = Math.round((a-mean_azimuth+threshold)/threshold/2*systemDimX);
		    int y = Math.round((p-mean_pitch+threshold)/threshold/2*systemDimY);
		    
		    barX.setProgress(x);
		    barY.setProgress(y);
		    
		    sendCoordinateMessage(x,y);
		}
	}
	private SensorBindListener sensorBindListener = new SensorBindListener();
	
	private boolean isBindRegistered = false;
	public void bindSensorWithSeekBar(){
		if(isBinded && !isBindRegistered){
			isBindRegistered = true;
			mSensorManager.registerListener(sensorBindListener, mSensor, SensorManager.SENSOR_DELAY_NORMAL);
		}
	}
	public void unbindSensorWithSeekBar(){
		if(isBindRegistered){
			isBindRegistered = false;
			mSensorManager.unregisterListener(sensorBindListener);
		}
	}
	//End of orientation event listener
	
	@Override
	protected void onResume() {
		super.onResume();
		mSensorManager.registerListener(seListener, mSensor, 5000000);
		bindSensorWithSeekBar();
	}

	@Override
	protected void onPause() {
	    super.onPause();
	    mSensorManager.unregisterListener(seListener);
	    unbindSensorWithSeekBar();
	}
	  
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	public void sendCoordinateMessage(int x,int y){
		if(client!=null && !client.isError()){
			client.sendMessage("A"+x+","+y);
		}
	}
	public void showAlert(){
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
				this);
 
		// set title
		alertDialogBuilder.setTitle("Connected");
 
		// set dialog message
		//alertDialogBuilder
		//	.setMessage("Click yes to exit!")
		//	.setCancelable(false);
 
		// create alert dialog
		AlertDialog alertDialog = alertDialogBuilder.create();
 
		// show it
		alertDialog.show();	
	}

}
