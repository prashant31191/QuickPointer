package com.example.QuickPointer.net;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

public class UDPServer{
	protected DatagramSocket socket;
	private boolean isStarted = false;
	private Thread receive;
	private int port;
		
	public UDPServer(int port){ this.port = port;}
	
	public boolean isStarted(){return isStarted;}
	public void start() throws SocketException{
		if(socket==null || socket.isClosed()){
			isStarted = true;
			
			socket = new DatagramSocket(port);
			
			receive = new Thread(new ReceiveCoordination());
			receive.start();
		}
	}
	
	public void stop(){
		if(socket!=null){
			isStarted = false;
			Thread stop = new Thread(new StopSocket());
			stop.start();
		}
	}
	
	class StopSocket implements Runnable{
		@Override
		public void run() {
			socket.close();
		}
	}
	
	private OnDataReceiveListener onReceive;
	public void setOnDataReceiveListener(OnDataReceiveListener onReceive){
		this.onReceive = onReceive;
	}
	
	class ReceiveCoordination implements Runnable{
		@Override
		public void run() {
			while(isStarted){		
				try {
					DatagramPacket p = UDPProtocol.getNewPacket();
					
					System.out.println("waiting for packet...");
					
					socket.receive(p); //block
					
					System.out.println("packet received.");
					
					if(onReceive!=null){
						onReceive.onReceive(UDPProtocol.decompilePacket(p));
					}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		
	}

	public void setPort(int port) {
		this.port = port;
	}
}
