package com.example.QuickPointer.android;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Set;
import java.util.UUID;

import com.example.quickpointerclient.R;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;
 
public class BluetoothClient extends Activity {
 
        private static final String TAG = "MyActivity";
        private static final int REQUEST_ENABLE_BT = 0;
        private BluetoothAdapter mBtAdapter;
        private BluetoothSocket socket;
        private Button button;
        
        private static final String uuid = "11111111-1111-1111-1111-111111111123";//"38400000-8cf0-11bd-b23e-10b96e4ef00d"
        private static final String mac = "E8:39:DF:06:DF:AB";
 
        /** Called when the activity is first created. */
        @Override
        public void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);
                setContentView(R.layout.activity_bluetooth);
                Log.d(TAG, "ON CREATE");
        }
       
 
        @Override
        protected void onStart() {
                super.onStart();
                Log.d(TAG, "ON START");
                button = (Button) findViewById(R.id.button1);
                this.establishConnection();
        }
       
 
       
        private void establishConnection() {
                try {
                        mBtAdapter = BluetoothAdapter.getDefaultAdapter();
                        if (mBtAdapter == null) {
                                Toast.makeText(BluetoothClient.this, "Bluetooth not supported", Toast.LENGTH_SHORT).show();
                                Log.d(TAG,"Bluetooth not supported.");
                                finish();
                        }
                       
                } catch (Exception e) {
                        Log.e(TAG, "Error connecting to device", e);
                        Toast.makeText(BluetoothClient.this, "Error connecting to destkop application.", Toast.LENGTH_SHORT).show();
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
                      
                        Thread btThread = new ConnectThread(mBtAdapter.getRemoteDevice(mac));
                        btThread.start();;
                        
                        button.setOnClickListener(new OnClickListener() {
                                public void onClick(View v) {
                                        Log.d(TAG,"In listener button.");
                                        try {
                                                if (socket != null)
                                                        socket.getOutputStream().write("Hello, world!".getBytes());
                                                else
                                                        Toast.makeText(BluetoothClient.this, "Not connected to destkop application.", Toast.LENGTH_SHORT).show();
                                        } catch (IOException e) {
                                                Log.d(TAG, e.toString());
                                                e.printStackTrace();
                                        }
                                }      
                        });
                }
                catch(Exception ex)
                {
                        Log.d(TAG,ex.toString());
                }
        }
 
 
        @Override
        protected void onPause() {
                super.onPause();
                Log.d(TAG, "ON PAUSE");
                try {
                        socket.close();
                } catch (IOException e) {
                        e.printStackTrace();
                }
        }
 
        @Override
        protected void onDestroy() {
                super.onDestroy();
                Log.d(TAG, "ON DESTROY");
                try {
                        socket.close();
                } catch (IOException e) {
                        e.printStackTrace();
                }
        }       
       
        @Override
        protected void onResume() {
                super.onResume();
                Log.d(TAG, "ON RESUME");
        }
       
        @Override
        public void onBackPressed() {
                super.onBackPressed();
                try {
                        socket.close();
                } catch (IOException e) {
                        e.printStackTrace();
                }
        }
        
        private class ConnectThread extends Thread {
            //private final BluetoothSocket mmSocket;
            private final BluetoothDevice mmDevice;
         
            public ConnectThread(BluetoothDevice device) {
                // Use a temporary object that is later assigned to mmSocket,
                // because mmSocket is final
                BluetoothSocket tmp = null;
                mmDevice = device;
         
                // Get a BluetoothSocket to connect with the given BluetoothDevice
                try {
                    // MY_UUID is the app's UUID string, also used by the server code
                	UUID uid = UUID.fromString(uuid);
                	Log.d(TAG,"uuid created");
                    tmp = device.createRfcommSocketToServiceRecord(uid);
                } catch (IOException e) { }
                socket = tmp;
            }
         
            public void run() {
                // Cancel discovery because it will slow down the connection
                mBtAdapter.cancelDiscovery();
         
                try {
                    // Connect the device through the socket. This will block
                    // until it succeeds or throws an exception
                    socket.connect();
                } catch (IOException connectException) {
                    // Unable to connect; close the socket and get out
                    try {
                        socket.close();
                    } catch (IOException closeException) { }
                    return;
                }
         
            }
         
            /** Will cancel an in-progress connection, and close the socket */
            public void cancel() {
                try {
                    socket.close();
                } catch (IOException e) { }
            }
        }
}