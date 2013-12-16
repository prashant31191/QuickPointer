package com.example.QuickPointer.net;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class TCPServer implements ServerI{
	private int port = ServerI.DEFAULT_PORT;
	private boolean isStarted = false;
	protected ServerSocket serverSocket;
	protected Socket clientSocket;
	public PrintWriter out;
	public BufferedReader in;
	private Thread server;
	
	@Override
	public void start(){
		if(serverSocket==null){
			isStarted = true;
			
			server = new Thread(new ServerStart());
			server.start();
		}
	}
	@Override
	public void setPort(int port) { this.port = port;}
	@Override
	public void stop() {
		if(serverSocket!=null){
			isStarted = false;
			
			Thread stop = new Thread(new StopServer());
			stop.start();
		}
	}
	
	private OnDataReceiveListener onReceive;
	@Override
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
		        PrintWriter out =
		            new PrintWriter(clientSocket.getOutputStream(), true);
		        BufferedReader in = new BufferedReader(
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
			// TODO Auto-generated method stub
			try {
				clientSocket.close();
				serverSocket.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
