package smallcampus.QuickPointer.net.test;

import java.io.IOException;

import smallcampus.QuickPointer.Config;
import smallcampus.QuickPointer.net.BaseClient;
import smallcampus.QuickPointer.net.BaseServer;
import smallcampus.QuickPointer.net.EventListener;
import smallcampus.QuickPointer.net.QPTcpUdpClient;
import smallcampus.QuickPointer.net.QPTcpUdpServer;

public class ServerClientTestApp {	
	public static void main(String[] args) throws IOException {
		BaseServer server = new QPTcpUdpServer(Config.DEFAULT_TCP_SERVER_PORT,Config.DEFAULT_UDP_SERVER_PORT);
		BaseClient client = new QPTcpUdpClient("localhost", Config.DEFAULT_TCP_SERVER_PORT, Config.DEFAULT_UDP_SERVER_PORT);
		final int x =100,y=110;
		
		server.setOnCoordinateReceiveListener(new EventListener<int[]>(){
			@Override
			public void perform(int[] args) {
				if(args[0]==x&&args[1]==y){
					System.out.println("Test result: success");
				}else{
					System.out.println("Test result: failure x="+args[0]+" y="+args[1]);
				}
			}
		});
		
		server.start();
		client.connect();
		
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {}
		
		System.out.println("sending coordinate data...");
		client.sendCoordinateData((float)x,(float)y);
		
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {}
		
		System.out.println("terminating program...");
		client.disconnect();
		server.stop();
	}
}
