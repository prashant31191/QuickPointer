package smallcampus.QuickPointer.android;

import java.io.IOException;
import java.util.UUID;

import smallcampus.QuickPointer.net.BaseClient;
import smallcampus.QuickPointer.net.Protocol;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.util.Log;

public class QPBluetoothClient extends BaseClient {

    private static final String TAG = "BluetoothClient";
    private BluetoothAdapter mBtAdapter;
    private BluetoothSocket socket;
    
    private UUID uuid = UUID.fromString("11111111-1111-1111-1111-111111111123");//"38400000-8cf0-11bd-b23e-10b96e4ef00d"
    private String mac;
    //TODO for testing
    public static final String defaultMac = "E8:39:DF:06:DF:AB";
	
    public QPBluetoothClient(String mac)
    {
    	this.mac = mac;
    }
    
    public QPBluetoothClient(String mac, String uuid){
    	this.uuid = UUID.fromString(uuid);
    	this.mac = mac;
    }
    
    public void setContext(Context context){}
    
	@Override
	public void connect(){
		if(isConnected){
			return;
		}
		
		try {
            mBtAdapter = BluetoothAdapter.getDefaultAdapter();
            if (mBtAdapter == null) {
                    //Toast.makeText(context, "Bluetooth not supported", Toast.LENGTH_SHORT).show();
                    Log.d(TAG,"Bluetooth not supported.");
                    return;
            }
	    } catch (Exception e) {
	            Log.e(TAG, "Error connecting to device", e);
	            //Toast.makeText(context, "Error connecting to destkop application.", Toast.LENGTH_SHORT).show();
	    }
	   
	    try
	    {
	            if (!mBtAdapter.isEnabled()) {
	                mBtAdapter.enable();
	            }
	            while(true){
	                    Log.d(TAG, "INSIDE WHILE STATE ON");
	                    if (mBtAdapter.getState() == BluetoothAdapter.STATE_ON)
	                            break;
	            }
	           
	            boolean result = mBtAdapter.startDiscovery();
	            Log.d(TAG, "Start discovery = " + result);
	           
	            while(true){
	                    Log.d(TAG, "INSIDE WHILE IS DISCOVERING");
	                    if (mBtAdapter.isDiscovering() == true){
	                            mBtAdapter.cancelDiscovery();
	                            break;
	                    }
	            }
	          
	            Thread btThread = new Thread(connect);
	            btThread.start();;
	            
	    }
	    catch(Exception ex)
	    {
	            Log.d(TAG,ex.toString());
	    }
	}

	@Override
	public void disconnect() {
		isConnected = false;
		try {
			socket.getOutputStream().write(-1);
			socket.getOutputStream().flush();
			socket.close();
		} catch (IOException e) {}
	}

	@Override
	public void sendCoordinateData(float x, float y) {
		if(isConnected){
			try {
				socket.getOutputStream().write(Protocol.compileCoordinateMsg(x,y).getBytes());
				socket.getOutputStream().flush();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	@Override
	public void sendStartControl() {
		if(isConnected){
			try {
				socket.getOutputStream().write(Protocol.startString.getBytes());
				socket.getOutputStream().flush();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	@Override
	public void sendStopControl() {
		if(isConnected){
			try {
				socket.getOutputStream().write(Protocol.endString.getBytes());
				socket.getOutputStream().flush();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
    private final Runnable connect = new Runnable(){
			@Override
			public void run() {
				try {
					socket = mBtAdapter.getRemoteDevice(mac).createRfcommSocketToServiceRecord(uuid);
					
					mBtAdapter.cancelDiscovery();
					
					socket.connect(); //block
					
					isConnected = true;
					if(onServerConnected!=null){
						onServerConnected.perform(null);
					}
				} catch (IOException e) {
                 //Toast.makeText(context, "Bluetooth socket error.", Toast.LENGTH_SHORT).show();
					e.printStackTrace();
					try {
						socket.close();
					} catch (IOException e1) {}
				}
			}
     };
}
