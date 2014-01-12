package com.example.QuickPointer.net;

import java.net.SocketException;

public class QuickPointerServer {
	private TCPProtocol protocol;
	private TCPServer tcpServer;
	private UDPServer udpServer;
	
	public QuickPointerServer(int tcpPort,int udpPort){
		tcpServer = new TCPServer(tcpPort);
		tcpServer.setOnDataReceiveListener(onTCPReceiveListener);
		udpServer = new UDPServer(udpPort);
	}
	
	public void setOnCoordinateDataReceiveListener(OnDataReceiveListener onCoorReceive){
		udpServer.setOnDataReceiveListener(onCoorReceive);
	}
	
	private final OnDataReceiveListener onTCPReceiveListener = new OnDataReceiveListener(){
		@Override
		public void onReceive(String msg) {
			if(msg==null){
				return;
			}
			switch(protocol.receiveMsg(msg)){
			case START:
				if(!udpServer.isStarted()){
					//Start the udp server for receiving coordinate data
					try {
						System.out.println("starting UDP server...");
						udpServer.start();
					} catch (SocketException e) {
						e.printStackTrace();
						System.exit(1);
					}
				}
				break;
			case END:
				if(udpServer.isStarted()){
					udpServer.stop();
				}
				tcpServer.stop();
				break;
			default: break;
			}
			System.out.println("Sending response... msg:"+protocol.getResponseMsg());
			tcpServer.send(protocol.getResponseMsg());
		}
	};
	
	public void start(){
		if(!tcpServer.isStarted()){
			protocol = new TCPProtocol();
			System.out.println("starting TCPServer...");
			tcpServer.start();
		}else{
			System.out.println("TCP server is started");
		}
	}
	public void stop(){
		udpServer.stop();
		tcpServer.stop();
	}
}
