package smallcampus.QuickPointer;

import java.io.IOException;

import smallcampus.QuickPointer.net.BaseServer;
import smallcampus.QuickPointer.net.EventListener;
import smallcampus.QuickPointer.net.QPBluetoothServer;
import smallcampus.QuickPointer.net.QPTcpUdpServer;
import smallcampus.QuickPointer.ui.InitializationFrame;
import smallcampus.QuickPointer.ui.InitializationFrame.OnServerSelectListener;
import smallcampus.QuickPointer.ui.PointerPanel;
import smallcampus.QuickPointer.ui.QuickPointerMainFrame;

public class QuickPointerApp {
        public static void main(String[] args) throws IOException{
        	
        	//Initialize...
        	final InitializationFrame frame = new InitializationFrame();
        	
        	//Choose a server type        	
        	frame.setOnSelectListener(new OnServerSelectListener(){
				@Override
				public void serverSelect(int type) {
					frame.dispose();
					
					//Setup the server
					BaseServer server = null;
					
		        	switch(type){
		        	case TYPE_TCP:
		        		try {
							server = new QPTcpUdpServer(Config.DEFAULT_TCP_SERVER_PORT,Config.DEFAULT_UDP_SERVER_PORT);
						} catch (IOException e1) {
							e1.printStackTrace();
						}
		        		break;
		        	case TYPE_BLUETOOTH:
		        		server = new QPBluetoothServer();
		        		break;
		        	}
		        	
		        	if(server==null){
		        		System.err.println("Cannot start the server. Program terminated.");
		        		System.exit(-1);
		        	}
		        	
		        	//create connection information
		        	
		        	//waiting for connection
		        	
		        	//Connection accepted, show main UI
		            QuickPointerMainFrame qp = new QuickPointerMainFrame();
		            final PointerPanel pointer = qp.getPointer();    
		            
		            server.setOnCoordinateReceiveListener(new EventListener<float[]>(){
						@Override
						public void perform(float[] args) {
							pointer.setPositionR(args[0], args[1]);
						}
		            });
		            
		            System.out.println("Starting Server...");
		            server.start();
				}
        	});
        	
        	frame.showServerTypeSelect();
        }
}