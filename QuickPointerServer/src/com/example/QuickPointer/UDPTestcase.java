package com.example.QuickPointerApp;

import java.io.IOException;

import com.example.QuickPointerApp.net.UDPClient;
import com.example.QuickPointerApp.net.UDPServer;

public class UDPTestcase {

	/**
	 * @param args
	 * @throws IOException 
	 */
	static UDPServer server;
	
	public static void main(String[] args) throws IOException {
		UDPTestcase instance = new UDPTestcase();
		
		
		UDPClient client = new UDPClient(UDPClient.DEFAULTPORT);
		server = new UDPServer(UDPServer.DEFAULTPORT);
		
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
		
		System.out.println("Server starting");
		server.start();
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			return;
		}
		
		System.out.println("Connecting...");
		client.connect("localhost", UDPServer.DEFAULTPORT);
		System.out.println("Connected.");
		
		System.out.println("Packet sending...");
		client.send(testMsg);
		
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			return;
		}
		server.stop();
		System.out.println("Server stopped. End");
	}

}
