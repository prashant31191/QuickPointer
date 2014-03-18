package smallcampus.QuickPointer;

import java.awt.AWTException;
import java.awt.EventQueue;
import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import smallcampus.QuickPointer.net.BaseServer;
import smallcampus.QuickPointer.net.QPBluetoothServer;
import smallcampus.QuickPointer.net.TCP.QPTcpUdpServer;
import smallcampus.QuickPointer.ui.MainSetupFrame;
import smallcampus.QuickPointer.ui.MainSetupFrame.OnServerSelectListener;
import smallcampus.QuickPointer.ui.PointerEngine;
import smallcampus.QuickPointer.ui.PointerPanel;
import smallcampus.QuickPointer.ui.QuickPointerMainFrame;
import smallcampus.QuickPointer.util.EventListener;

public class QuickPointerApp {
	
	 private static Logger LOGGER = Logger.getLogger("InfoLogging");
	 private static FileHandler fh; 
	 
        public static void main(String[] args) throws IOException{
        	
        	// This block configure the logger with handler and formatter  
            fh = new FileHandler("QP.log"); 
                
            LOGGER.addHandler(fh);
            SimpleFormatter formatter = new SimpleFormatter();  
            fh.setFormatter(formatter);  
        	
        	//Initialize...
        	//final InitializationFrame frame = new InitializationFrame();
    		EventQueue.invokeLater(new Runnable() {
    			public void run() {
    				try {
    					final MainSetupFrame window = new MainSetupFrame();
    					
    		        	//Choose a server type        	
    		        	window.setOnSelectListener(new OnServerSelectListener(){
    						@Override
    						public void serverSelect(int type) {
    							//Setup the server
    							BaseServer server = null;
    							
    				        	switch(type){
    				        	case TYPE_TCP:
    				        		try {
    				        			LOGGER.info("TCP and UDP server are setting up.");
    									server = new QPTcpUdpServer(Config.DEFAULT_TCP_SERVER_PORT,Config.DEFAULT_UDP_SERVER_PORT);
    								} catch (IOException e1) {
    									e1.printStackTrace();
    								}
    				        		break;
    				        	case TYPE_BLUETOOTH:
    				        		LOGGER.info("Bluetooth Server is setting up");
    				        		server = new QPBluetoothServer();
    				        		break;
    				        	}
    				        	
    				        	if(server==null){
    				        		LOGGER.entering("QuickPointerApp", "fail to set up server");
    				        		System.err.println("Cannot initialize the server. Program terminated.");
    				        		System.exit(-1);
    				        	}
    				        	
    				        	//create connection information
    				        	window.showConnectionInfoPanel(server.getHostname());
    				        	
    				        	
    				        	//Create main UI
    				            QuickPointerMainFrame qp = new QuickPointerMainFrame();
    				            final PointerEngine pointer = qp.getPointerEngine();
    				            //disable the pointer before connection
    				            //pointer.setVisible(false);
    				            
    				            //set up action response to coordinate data
    				            server.setOnCoordinateReceiveListener(new EventListener<float[]>(){
    								@Override
    								public void perform(float[] args) {
    									pointer.setTarget(args);
    								}
    				            });
    				            
    				            //set up robot for keyboard keypress simulation
    				            try {
									final Robot robot = new Robot();
									
	    				            //set up action response to control signal
	    				            server.setOnPageUpReceiveListener(new EventListener(){
										@Override
										public void perform(Object args) {
											robot.keyPress(KeyEvent.VK_PAGE_UP);
											robot.keyRelease(KeyEvent.VK_PAGE_UP);
										}
	    				            });
	    				            server.setOnPageDownReceiveListener(new EventListener(){
										@Override
										public void perform(Object args) {
											robot.keyPress(KeyEvent.VK_PAGE_DOWN);
											robot.keyRelease(KeyEvent.VK_PAGE_DOWN);
										}
	    				            });
								} catch (AWTException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
    				            
    				        	//Accept connection and show main UI
    				        	server.setOnConnectionReceiveListener(new EventListener(){
    								@Override
    								public void perform(Object args) {
    									window.showSucessfulCountDown();
    									//pointer.setVisible(true);
    								}
    				        	});

    				            
    				            System.out.println("Starting Server...");
    				            server.start();
    				            
    						}
    		        	});
    					
    				} catch (Exception e) {
    					e.printStackTrace();
    				}
    			}
    		});

        }
}