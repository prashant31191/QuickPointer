package com.example.QuickPointer.net.test;

import java.io.IOException;

import com.example.QuickPointer.Config;
import com.example.QuickPointer.net.OnDataReceiveListener;
import com.example.QuickPointer.net.QuickPointerClient;
import com.example.QuickPointer.net.QuickPointerServer;
import com.example.QuickPointer.net.UDPProtocol;

public class ServerClientTestApp {	
	public static void main(String[] args) throws IOException {
		QuickPointerServer server = new QuickPointerServer(Config.DEFAULT_TCP_SERVER_PORT,Config.DEFAULT_UDP_SERVER_PORT);
		QuickPointerClient client = new QuickPointerClient();
		final int x =100,y=110;
		
		server.setOnCoordinateDataReceiveListener(new OnDataReceiveListener(){
			@Override
			public void onReceive(String msg) {
				int[] c;
				System.out.println("Server receive coordinates:"+msg);
				try {
					c = UDPProtocol.decompileCoordinateMsg(msg);
					if(c[0]==x&&c[1]==y){
						System.out.println("Test result: success");
					}else{
						System.out.println("Test result: failure x="+c[0]+" y="+c[1]);
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});
		
		server.start();
		client.connect("localhost", Config.DEFAULT_TCP_SERVER_PORT, Config.DEFAULT_UDP_SERVER_PORT);
		
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {}
		
		client.sendCoordinateMsg(x,y);
		
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {}
		
		server.stop();
	}
}
