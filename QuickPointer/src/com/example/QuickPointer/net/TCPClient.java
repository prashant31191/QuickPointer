package com.example.QuickPointer.net;


import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;

//Java console client for testing

public class TCPClient{
	
    protected Socket socket;
    public PrintWriter out;
    public BufferedReader in;
    public ActionListener messageReceived = null;        
	
	Thread sendNReceive;
	
    public void sendMessage(String msg)
    {
    	if(sendNReceive!=null && sendNReceive.isAlive()){
    		//TODO possible bug
    		System.out.println("Error: Thread is still running, msg skipeed.");
    	}else{
    		sendNReceive = new Thread(new MessageSendNReceive(msg));
    		sendNReceive.start();
    	}
    }
    
    public void close()
    {
    	Thread stop = new Thread(new Stop());
    	stop.start();
    }

	public void connect(String host, int port) {
		Thread con = new Thread(new Connect(host,port));
		con.start();
	}
	
	public void send(String msg) {
    	if(sendNReceive!=null && sendNReceive.isAlive()){
    		System.out.println("Error: Thread is still running, msg skipeed.");
    	}else{
    		sendNReceive = new Thread(new MessageSendNReceive(msg));
    		sendNReceive.start();
    	}
	}
	
	OnConnectedListener onConnected;
	public void setOnConnectedListener(OnConnectedListener onConnected) {
		this.onConnected = onConnected;
	}
	
	private class Connect implements Runnable{
		String host;
		int port;
		Connect(String host,int port){this.host=host;this.port=port;}
		@Override
		public void run() {
			try {
				socket = new Socket(InetAddress.getByName(host),port);
		        out = new PrintWriter(socket.getOutputStream(), true);
		        in = new BufferedReader(
		            new InputStreamReader(socket.getInputStream()));
		        
				if(onConnected!=null){
					onConnected.onConnected(true);
				}
			} catch (IOException e) {
				e.printStackTrace();
				if(onConnected!=null){
					onConnected.onConnected(false);
				}
			}
		}
	}
	
	protected OnDataReceiveListener onDataReceiveListener;
	public void setOnDataReceiveListener(OnDataReceiveListener listener){
		this.onDataReceiveListener = listener;
	}
	private class MessageSendNReceive implements Runnable{
		String fromUser;
		
		public MessageSendNReceive(String msg){ fromUser = msg; }
		
		@Override
		public void run() {
			if(socket!=null && out!=null && socket.isConnected()){
				out.println(fromUser);
				
				if(onDataReceiveListener!=null){
					try {
						onDataReceiveListener.onReceive(in.readLine());
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}
	private class Stop implements Runnable{
		@Override
		public void run() {
			try {
				socket.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}			
		}
	}
}
