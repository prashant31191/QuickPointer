package com.example.QuickPointer.net;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

public class UDPServer implements ServerI{
	protected DatagramSocket socket;
	private boolean isStarted = false;
	private Thread receive;
	int port = ServerI.DEFAULT_PORT;
		
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
			byte[] buf;
			while(isStarted){		
				try {
					buf = new byte[UDPProtocol.PACKET_SIZE];
					DatagramPacket p = new DatagramPacket(buf,UDPProtocol.PACKET_SIZE);
					
					System.out.println("waiting for packet...");
					
					socket.receive(p); //block
					
					System.out.println("packet received.");
					
					//decompose the packet
					buf = p.getData();
					int firstByte;
					//search for the first byte
					for(firstByte=0;firstByte<UDPProtocol.PACKET_SIZE&& buf[firstByte]==0;firstByte++){}
					
					if(onReceive!=null){
						onReceive.onReceive(new String(buf,firstByte,UDPProtocol.PACKET_SIZE-firstByte));
					}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		
	}

	@Override
	public void setPort(int port) {
		this.port = port;
	}
}
