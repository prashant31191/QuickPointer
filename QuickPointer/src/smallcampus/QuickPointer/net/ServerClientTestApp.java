package smallcampus.QuickPointer.net;

import java.io.IOException;

import smallcampus.QuickPointer.Config;

public class ServerClientTestApp {	
	public static void main(String[] args) throws IOException {
		BaseServer server = new QPTcpUdpServer(Config.DEFAULT_TCP_SERVER_PORT,Config.DEFAULT_UDP_SERVER_PORT);
		BaseClient client = new QPTcpUdpClient("localhost", Config.DEFAULT_TCP_SERVER_PORT, Config.DEFAULT_UDP_SERVER_PORT);
		final float x =0.5f,y=0.75f;
		
		server.setOnCoordinateReceiveListener(new EventListener<float[]>(){
			@Override
			public void perform(float[] args) {
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
		client.sendCoordinateData(x,y);
		
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {}
		
		System.out.println("terminating program...");
		client.disconnect();
		//server.stop();
	}
}
