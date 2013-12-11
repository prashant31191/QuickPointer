package com.example.quickpointerclient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.EventListener;

import android.net.wifi.p2p.WifiP2pManager.ActionListener;
import android.os.AsyncTask;
import android.util.EventLog.Event;

// testing

public class Client{
	public int i =0 ;
	
	public interface OnMsgReceivedEventListener{
		public void onMsgReceived(String Msg);
	}
	public interface OnSocketConnectedListener{
		public void onSocketConnected(int ret);
	}

	public Client(String host,int port){
		setHost(host);
		setPort(port);
	}
	
    protected String hostName=null;
    protected int portNumber=0;
    protected Socket kkSocket = null;
    public PrintWriter out;
    public BufferedReader in;
    public OnMsgReceivedEventListener onMsgReceivedEventListener = null;
    public OnSocketConnectedListener onSocketConnectedListener = null;
    private short errCode = 0;
    
    public void setHost(String host){hostName= host;}
    public void setPort(int port){portNumber = port;}
    public int getErrorCode(){return errCode;}
    public boolean isError(){return (errCode!=0);}
    
	public void connectHost(){
		if(hostName==null || portNumber<1){
			errCode = 1;
			return;
		}
		if(kkSocket!=null && !kkSocket.isClosed()){
			errCode=2;
			return;
		}
		AsyncTask socketConnection = new AsyncTask(){
			@Override
			protected Object doInBackground(Object... arg0) {
		        try {
					kkSocket = new Socket(hostName, portNumber);
					// TODO TCP keepalive implementation
			        out = new PrintWriter(kkSocket.getOutputStream(), true);
			        in = new BufferedReader(
			            new InputStreamReader(kkSocket.getInputStream()));
			        
			        String systemDimension = in.readLine();
			        
			        //if(onSocketConnectedListener!=null){
			        	//onSocketConnectedListener.onSocketConnected(0);
			        //}
				} catch (UnknownHostException e) {
					e.printStackTrace();
					errCode =3;
				} catch (IOException e) {
					e.printStackTrace();
					errCode=4;
				}
		        return null;
			}
		};
		socketConnection.execute();
		
	}
	
    public void sendMessage(String msg)
    {
		AsyncTask socketConnection = new AsyncTask(){
			@Override
			protected Object doInBackground(Object... args) {
		        if(args.length==1){//only 1 msg is sent at a time
    				//try {	
    		        	out.println((String) args[0]);
    				
    					//onMsgReceivedEventListener.onMsgReceived(in.readLine());
    				//} catch (IOException e) {
    				//	errCode = 5;
    				//	e.printStackTrace();
    				//}			
		        }
		        return null;
			}
		};
		socketConnection.execute(msg);
    }
    
    public void stop()
    {
    	//TODO not sure if this works
    	//sendNReceive.interrupt();

    	out.close();
    	try {
    		in.close();
			this.kkSocket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
	

	class MessageSendNReceive implements Runnable{
		public MessageSendNReceive(String msg){
			fromUser = msg;
		}
		public String fromUser = "";
		@Override
		public void run() {
		}
	}

}
