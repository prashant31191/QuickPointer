package com.example.QuickPointer.net.test;

import com.example.QuickPointer.Config;
import com.example.QuickPointer.net.OnDataReceiveListener;
import com.example.QuickPointer.net.TCPClient;
import com.example.QuickPointer.net.TCPServer;

final class TCPTestApp {

	public static void main(String[] args) {
		final TCPClient client = new TCPClient();
		final TCPServer server = new TCPServer(Config.DEFAULT_TCP_SERVER_PORT);
		
		final String testMsg = "t";
		final String responseMsg = "r";
		
		server.setOnDataReceiveListener(new OnDataReceiveListener(){
			@Override
			public void onReceive(String msg) {
				System.out.println("Data Received. msg:\""+msg+"\"");
				if(msg.equals(testMsg)){
					System.out.println("Server Test Result: sucess");
				}else{
					System.out.println("Server Test Result: failure");
				}
				
				server.send(responseMsg);
			}
		});
		
		client.setOnDataReceiveListener(new OnDataReceiveListener(){

			@Override
			public void onReceive(String msg) {
				System.out.println("Data Received. msg:\""+msg+"\"");
				if(msg.equals(responseMsg)){
					System.out.println("Client Test Result: sucess");
				}else{
					System.out.println("Client Test Result: failure");
				}
			}
			
		});
		
		System.out.println("Server starting...");
		server.start();
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			return;
		}
		
		System.out.println("Connecting...");
		client.connect("localhost", Config.DEFAULT_TCP_SERVER_PORT);
		
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			return;
		}
		
		System.out.println("Packet sending...");
		client.send(testMsg);
		
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			return;
		}
		
		server.stop();
		client.close();
		System.out.println("Server stopped. End");
		System.out.println("the SocketExceoption is created in purpose");
	}

}
