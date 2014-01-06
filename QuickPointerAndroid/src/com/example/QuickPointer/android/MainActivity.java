package com.example.QuickPointer.android;

import java.io.IOException;
import java.util.UUID;

import com.example.QuickPointer.net.TCPClient;
import com.example.QuickPointer.net.UDPClient;
import com.example.QuickPointer.Config;
import com.example.quickpointerclient.R;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
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
	
	private static enum Mode{TCP, UDP};
	private Mode mode = Mode.UDP;
	
	private UDPClient client;
	SeekBar barX, barY;
	
	private SensorManager mSensorManager;
	private Sensor mSensor;
	
	protected TextView textX, textY, textZ;
	protected Button bindBtn, connectBtn;
	private boolean isBinded =false;
	
	protected int systemDimX = 800, systemDimY = 600;
	
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
				String hostName = ((EditText) findViewById(R.id.editText1)).getText().toString();
				try {
					client = new UDPClient();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				client.connect(hostName, Config.DEFAULT_UDP_SERVER_PORT);
			}
		});
		
		initializeSeekBarControl();
		
		//initialize bluetooth button and adapter
		initializeBlueTooth();
	    
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
			sendCoordinateMessage(barX.getProgress(),barY.getProgress());
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
		    
		    sendCoordinateMessage(x,y);
		}
	}
	
//End of Seekbar
	
//Bluetooth related
	private static final String TAG_bt = "BlueTooth";
	String dStarted = BluetoothAdapter.ACTION_DISCOVERY_STARTED;
	String dFinished = BluetoothAdapter.ACTION_DISCOVERY_FINISHED;
	BluetoothAdapter bluetooth = BluetoothAdapter.getDefaultAdapter();
	static final String serverUUID = "11111111111111111111111111111123";
	
	// bluetooth connection Button
	protected Button btBtn;
	private void initializeBlueTooth(){
		btBtn.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				// Run the bluetooth setup
				bluetooth.enable();
				if(!bluetooth.isDiscovering()){
					Log.d(TAG_bt,"Start searching other bluetooth devices.");
					bluetooth.startDiscovery();
				}
			}
			
		});
		
	    BroadcastReceiver discoveryResult = new BroadcastReceiver(){
			@Override
			public void onReceive(Context context, Intent intent) {
				Log.d(TAG_bt,"Action founded, start showing the results.");
	            String remoteDeviceName = intent.getStringExtra(BluetoothDevice.EXTRA_NAME);
	            BluetoothDevice remoteDevice;

	            remoteDevice = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);

	            Log.i("@#$%^&*(*&^%$#@#$%^&*(", "WYSWIETLAM");
	            Toast.makeText(getApplicationContext(), "Discovered: " + remoteDeviceName + " address " + remoteDevice.getAddress(), Toast.LENGTH_SHORT).show();

	            try{
	                BluetoothDevice device = bluetooth.getRemoteDevice(remoteDevice.getAddress());
	                BluetoothSocket clientSocket = device.createRfcommSocketToServiceRecord(UUID.fromString(serverUUID));
	                clientSocket.connect();

	            } catch (IOException e) {
	                Log.e(TAG_bt, e.getMessage());
	            }
			}
	    };

	    registerReceiver(discoveryResult, new IntentFilter(BluetoothDevice.ACTION_FOUND));
	}
//End of bluetooth related
	
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
	
	public void sendCoordinateMessage(int x,int y){
		if(client!=null){
			client.send("A"+x+","+y);
		}
	}
}
