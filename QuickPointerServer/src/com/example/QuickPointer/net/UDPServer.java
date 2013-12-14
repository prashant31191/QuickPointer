package com.example.QuickPointerApp.net;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class UDPServer {
	protected DatagramSocket socket;
	private static final int PACKETSIZE = 64;
	public static final int DEFAULTPORT = 9999;
	private boolean isStarted = false;
	private Thread receive;
	
	public UDPServer(int port) throws IOException{
		if(port <1 || port>65535){
			System.err.println("UDPServer: port out of range");
			throw new IOException("port out of range");
		}
		socket = new DatagramSocket(port);
	}
	
	public void start(){
		if(!isStarted){
			isStarted = true;
			System.out.println("Server started.");
			
			receive = new Thread(new ReceiveCoordination());
			receive.start();
		}
	}
	
	public void stop(){
		isStarted = false;
		Thread stop = new Thread(new StopSocket());
		stop.start();
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
	public interface OnDataReceiveListener {
		public void onReceive(Object args);
	}
	
	class ReceiveCoordination implements Runnable{
		@Override
		public void run() {
			if(socket.isBound()){
				byte[] buf;
				while(isStarted){		
					try {
						buf = new byte[PACKETSIZE];
						DatagramPacket p = new DatagramPacket(buf,PACKETSIZE);
						
						System.out.println("waiting for packet...");
						
						socket.receive(p); //block
						
						System.out.println("packet received.");
						
						//decompose the packet
						buf = p.getData();
						int firstByte;
						//search for the first byte
						for(firstByte=0;firstByte<PACKETSIZE&& buf[firstByte]==0;firstByte++){}
						
						if(onReceive!=null){
							onReceive.onReceive(new String(buf,firstByte,PACKETSIZE-firstByte));
						}
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}
		
	}
}
