package com.example.QuickPointer.android;

import java.io.IOException;
import java.net.SocketException;
import java.net.UnknownHostException;

import smallcampus.QuickPointer.Config;
import smallcampus.QuickPointer.net.BaseClient;
import smallcampus.QuickPointer.net.EventListener;
import smallcampus.QuickPointer.net.QPTcpUdpClient;

import com.example.quickpointerclient.R;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends Activity {

	private static final String TAG = "MainActivity";
		
	private BaseClient client;
	SeekBar barX, barY;
	
	private SensorManager mSensorManager;
	private Sensor mSensor;
	
	protected TextView textX, textY, textZ;
	protected Button bindBtn, connectBtn, btBtn;
	private boolean isBinded =false;
	
	protected int systemDimX = 800, systemDimY = 600;
	
	String hostName;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		Log.d(TAG,"onCreate..");
		
		barX = (SeekBar)findViewById(R.id.seekBar1);
		barY = (SeekBar)findViewById(R.id.seekBar2);
		connectBtn = (Button) findViewById(R.id.ConnectBtn); 
		textX = (TextView) findViewById(R.id.TextX);
		textY = (TextView) findViewById(R.id.TextY);
		textZ = (TextView) findViewById(R.id.TextZ);
		bindBtn = (Button) findViewById(R.id.bindBtn);
		btBtn = (Button) findViewById(R.id.blueToothBtn);
		
		connectBtn.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				Log.d(TAG, "on Connect Button click");
				hostName = ((EditText) findViewById(R.id.editText1)).getText().toString();
				//client.getTCPClient().setOnConnectedListener(onTCPConnect);
				try {
					client = new QPTcpUdpClient(hostName, Config.DEFAULT_TCP_SERVER_PORT,Config.DEFAULT_UDP_SERVER_PORT);
					
					client.setOnServerConnectedListener(new EventListener(){
						@Override
						public void perform(Object args) {
							Toast.makeText(MainActivity.this, "Connected", Toast.LENGTH_SHORT).show();
						}
					});
					
					client.connect();
					
				} catch (UnknownHostException e1) {
					Toast.makeText(MainActivity.this, "Unknown host", Toast.LENGTH_SHORT).show();
				} catch (SocketException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		
		initializeSeekBarControl();
		
		//initialize bluetooth button
		btBtn.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				client = new QPBluetoothClient();
				
				((QPBluetoothClient) client).setContext(MainActivity.this);
				
				client.setOnServerConnectedListener(new EventListener(){
					@Override
					public void perform(Object args) {
						Toast.makeText(MainActivity.this, "Bluetooth connected", Toast.LENGTH_SHORT).show();
					}
				});
				
				client.connect();
			}
		});
	    
	}// end of onCreate
		
	
//Seekbar - coordinate control by orientation sensor
	//change seekbar attributes by sensor
	private SensorBindListener sensorBindListener = new SensorBindListener();
	
	// Show x,y,z results on a textview
	private MySensorEventListener seListener = new MySensorEventListener();
	
	//send cooridinate information to server from seekbar result
	private OnSeekBarChangeListener onSeekBarChangeListener = new OnSeekBarChangeListener(){
		@Override
		public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {}

		@Override
		public void onStartTrackingTouch(SeekBar seekBar) {}

		@Override
		public void onStopTrackingTouch(SeekBar seekBar) {
			client.sendCoordinateData(barX.getProgress(),barY.getProgress());
		}
	};
	
	private void initializeSeekBarControl(){
		barX.setMax(systemDimX);
		barY.setMax(systemDimY);
		barX.setOnSeekBarChangeListener(onSeekBarChangeListener);
		barY.setOnSeekBarChangeListener(onSeekBarChangeListener);
		
		mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
		mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION);
		
		//bind the seekbar control to the client socket
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
	}
	
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
		    
		    client.sendCoordinateData(x, y);
		}
	}
	
//End of Seekbar
		
//Orientation Event listener

	
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
}
