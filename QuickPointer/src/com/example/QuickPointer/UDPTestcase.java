package com.example.QuickPointer;

import java.io.IOException;

import com.example.QuickPointer.net.ClientI;
import com.example.QuickPointer.net.ServerI;
import com.example.QuickPointer.net.UDPClient;
import com.example.QuickPointer.net.UDPServer;

public class UDPTestcase {

	/**
	 * @param args
	 * @throws IOException 
	 */
	static UDPServer server;
	
	public static void main(String[] args) throws IOException {
		UDPTestcase instance = new UDPTestcase();
		
		
		UDPClient client = new UDPClient();
		server = new UDPServer();
		server.setPort(ServerI.DEFAULT_PORT);
		
		final String testMsg = "t";
		
		server.setOnDataReceiveListener(new UDPServer.OnDataReceiveListener(){
			@Override
			public void onReceive(Object args) {
				String msg = (String) args;
				System.out.println("Data Received. msg:\""+msg+"\"");
				if(msg.equals(testMsg)){
					System.out.println("Test Result: sucess");
				}else{
					System.out.println("Test Result: failure");
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
		client.connect("localhost", ServerI.DEFAULT_PORT);
		
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
	}

}
