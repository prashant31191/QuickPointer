package com.example.QuickPointer.net;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class TCPServer{
	private int port;
	private boolean isStarted = false;
	protected ServerSocket serverSocket;
	protected Socket clientSocket;
	public PrintWriter out;
	public BufferedReader in;
	private Thread server;
	
	public TCPServer(int port){
		setPort(port);
	}
	
	public void start(){
		if(serverSocket==null){
			isStarted = true;
			
			server = new Thread(new ServerStart());
			server.start();
		}
	}
	public void setPort(int port) { this.port = port;}
	
	public void stop() {
		if(serverSocket!=null){
			isStarted = false;
			
			Thread stop = new Thread(new StopServer());
			stop.start();
		}
	}
	
	private OnDataReceiveListener onReceive;
	public void setOnDataReceiveListener(OnDataReceiveListener onReceive) {
		this.onReceive = onReceive;
	}
	
	private class ServerStart implements Runnable{
		@Override
		public void run() {
			try{
				String inputLine;
				
		    	serverSocket = new ServerSocket(port);
		        clientSocket = serverSocket.accept();
		        
		        out =
		            new PrintWriter(clientSocket.getOutputStream(), true);
		        in = new BufferedReader(
		            new InputStreamReader(clientSocket.getInputStream()));
		        
		        while (isStarted && (inputLine = in.readLine()) != null) {
		        	if(onReceive!=null){
		        		onReceive.onReceive(inputLine);
		        	}
		        }
		        
			}catch(IOException e){
				//TODO error handle
				e.printStackTrace();
			}
		}
	}
	
	
	
	private class StopServer implements Runnable{
		@Override
		public void run() {
			try {
				//!! will generate non-handled exception in another thread 
				clientSocket.close();
				serverSocket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
