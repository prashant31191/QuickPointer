package smallcampus.QuickPointer.android;

import java.io.IOException;
import java.net.SocketException;
import java.net.UnknownHostException;

import smallcampus.QuickPointer.Config;
import smallcampus.QuickPointer.net.BaseClient;
import smallcampus.QuickPointer.net.EventListener;
import smallcampus.QuickPointer.net.QPTcpUdpClient;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

public class MainControlActivity extends Activity implements ConnectionDialogFragment.OnClickListener{
	private static final String TAG = "MainActivity";
	
	private BaseClient client;
	ProgressBar pbx, pby, pbz;
	
	private MySensor mSensor;
	//private final SensorData data = new SensorData();

	protected Button btnUp, btnDown, btnPointer;
	boolean sendFlag = false;
	final double[] center = new double[2];
	final double[] result = new double[2];
	final double thresholdX = 45,thresholdY = 45;

	final String dHostName = "192.168.0.103";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main_control);
		
		Log.d(TAG,"onCreate..");
		
		pbx = (ProgressBar) findViewById(R.id.progressBarX);
		pby = (ProgressBar) findViewById(R.id.progressBarY);
		
		pbx.setMax(360); //TODO
		pby.setMax(360);
				
		btnUp = (Button) findViewById(R.id.buttonPageUp);
		btnDown = (Button) findViewById(R.id.buttonPageDown);
		btnPointer = (Button) findViewById(R.id.buttonPointer);
		
		btnPointer.setOnTouchListener(new OnTouchListener(){
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
		
		//disable the button before successfully connect to the server
		btnUp.setEnabled(false);
		btnDown.setEnabled(false);
		btnPointer.setEnabled(false);
		
		//Setup sensor
		mSensor = new MySensor((SensorManager) getSystemService(Context.SENSOR_SERVICE));
		mSensor.setListener(new EventListener<float[]>(){
			@Override
			public void perform(float[] args) {
				result[0] = 180 + args[0]*180/Math.PI;
				result[1] = 180+ args[1]*180/Math.PI;
				
				pbx.setProgress((int) result[0]);
				pby.setProgress((int) result[1]);
				
				if(sendFlag){
					final float tempX = (float) ((-center[1]+result[1]+thresholdX)/(2*thresholdX));
					final float tempY = (float) ((center[0]-result[0]+thresholdY)/(2*thresholdY));
					
					//Log.d(TAG, "SendCoordinateMsg("+Math.max(Math.min(tempX, 1), 0)+","+ Math.max(Math.min(tempY, 1), 0)+")...");
					client.sendCoordinateData(Math.max(Math.min(tempX, 1), 0), Math.max(Math.min(tempY, 1), 0));
				}
			}
		});
		
		//TODO select connection type
		ConnectionDialogFragment fragment = new ConnectionDialogFragment();
		fragment.show(getFragmentManager(), TAG);

	}
	
	private void connect(){
		
		client.setOnServerConnectedListener(new EventListener(){
			@Override
			public void perform(Object args) {
				runOnUiThread(new Runnable() {
				    @Override
				    public void run() {
						//Enable the interface
						btnUp.setEnabled(true);
						btnDown.setEnabled(true);
						btnPointer.setEnabled(true);
						Toast.makeText(MainControlActivity.this, "Server connected", Toast.LENGTH_SHORT).show();
				    }
				});		
			}
		});
		
		client.connect();
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		// register the sensor info to the progress bar UI
		mSensor.resume();
		//mSensorManager.registerListener(seListener, mSensor, 5000000); //0.5 second delay
	}

	@Override
	protected void onPause() {
	    super.onPause();
	    mSensor.pause();
	    //mSensorManager.unregisterListener(seListener);
	}
	
	@Override
	protected void onDestroy(){
		super.onDestroy();
		client.disconnect();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main_control, menu);
		return true;
	}

	@Override
	public void onConnectClick(DialogFragment dialog) {
		EditText host = (EditText) dialog.getDialog().findViewById(R.id.editTextHostname);
		
		try {
			client = new QPTcpUdpClient(host.getText().toString(),Config.DEFAULT_TCP_SERVER_PORT,Config.DEFAULT_UDP_SERVER_PORT);
		} catch (IOException e) {
			Toast.makeText(this, "Fail to connect", Toast.LENGTH_SHORT).show();
			e.printStackTrace();
		}
		
		connect();
	}

	@Override
	public void onBTConnectClick(DialogFragment dialog) {
		EditText host = (EditText) dialog.getDialog().findViewById(R.id.editTextHostname);
		//TODO
		client = new QPBluetoothClient(QPBluetoothClient.defaultMac);
		connect();
	}
}
