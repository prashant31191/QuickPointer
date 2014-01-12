package com.example.QuickPointer.net;

import java.io.IOException;

public class QuickPointerClient {
	private TCPClient tcpClient;
	private TCPProtocol protocol;
	private UDPClient udpClient;
	
	public QuickPointerClient(){
		tcpClient = new TCPClient();

		try {
			udpClient = new UDPClient();
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		}
	}
	
	//public TCPClient getTCPClient(){return tcpClient;}
	public UDPClient getUDPClient(){return udpClient;}
	
	public void connect(final String hostname,final int tcpPort,final int udpPort){
		protocol = new TCPProtocol();
		
		//1. Connect TCP server
		tcpClient.setOnConnectedListener(new OnConnectedListener(){
			@Override
			public void onConnected(boolean isConnected) {
				if(isConnected){
					System.out.println("TCP server connected.");
					System.out.println("Requesting UDP service...");
					tcpClient.send(TCPProtocol.startString);
				}else{
					System.err.println("cannot connect to TCP server");
				}
			}
		});
		tcpClient.setOnDataReceiveListener(new OnDataReceiveListener(){
			@Override
			public void onReceive(String msg) {
				switch(protocol.receiveMsg(msg)){
				case START:
					System.out.println("Connecting UDP server...");
					//2. Connect the UDP server
					udpClient.connect(hostname,udpPort);
					break;
				case END:
					break;
				default: break;
				}
			}
		});
		
		tcpClient.connect(hostname, tcpPort);
	}

	public void sendCoordinateMsg(int x, int y) {
		// TODO Auto-generated method stub
		udpClient.send(UDPProtocol.compileCoordinateMsg(x, y));
	}
	
}
