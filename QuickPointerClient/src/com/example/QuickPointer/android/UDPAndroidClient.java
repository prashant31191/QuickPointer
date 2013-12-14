package com.example.quickpointerclient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;

import android.os.AsyncTask;
import android.util.Log;

public class UDPAndroidClient {
	private static final String TAG = "UDP client";
	
	protected String host;
	protected int port;
	protected DatagramSocket socket = new DatagramSocket();
	
	protected void setHost(String host){this.host= host;}
	protected void setPort(int port){this.port = port;};
	
	public UDPAndroidClient(String host, int port) throws IOException{
		setHost(host);
		setPort(port);
		
		if(host==null || host.length()<=0 || port<1 || port>65535){
			Log.e(TAG, "host and port error! host:"+host+"|port:"+port);
			throw new IOException("Host and port error");
		}
		
		AsyncTask connect = new SocketConnection();
		connect.execute();
		
	}
	
	private OnConnectedListener onConnected;
	public void setOnConnectedListener(OnConnectedListener onConnected){
		this.onConnected = onConnected;
	}
	public interface OnConnectedListener{
		public void onSocketConnected(boolean rtn);
	}
	
	public void send(String msg) throws IOException{
		if(socket.isConnected()){
			byte[] tbuf = msg.getBytes();
			byte[] buf = new byte[256];
			int i;
			for(i=0;i<256 && i<tbuf.length;i++){
				buf[i] = tbuf[i];
			}
			for(;i<256;i++){
				buf[i]=0;
			}
			
			socket.send(new DatagramPacket(buf,256));
			Log.d(TAG, "Packet sended. Length:"+msg.length());
		}
	}
	
	class SocketConnection extends AsyncTask{

		@Override
		protected Object doInBackground(Object... arg0) {
			try {
				socket.connect(InetAddress.getByName(host),port);
				Log.d(TAG, "Host connected.");
				if(onConnected!=null){
					onConnected.onSocketConnected(true);
				}
			} catch (UnknownHostException e) {
				Log.e(TAG,e.getMessage());
				if(onConnected!=null){
					onConnected.onSocketConnected(false);
				}
			}
			return null;
		}
	}
}
