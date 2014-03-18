package smallcampus.QuickPointer.net;

import java.io.DataInputStream;
import java.io.IOException;
import java.util.logging.Logger;

import javax.bluetooth.BluetoothStateException;
import javax.bluetooth.LocalDevice;
import javax.microedition.io.Connector;
import javax.microedition.io.StreamConnection;
import javax.microedition.io.StreamConnectionNotifier;

public class QPBluetoothServer extends BaseServer{
    static final String serverUUID = "11111111111111111111111111111123";
	static final Logger LOGGER = Logger.getLogger("InfoLogging");
    
    private StreamConnectionNotifier connectionNotifier;
	private StreamConnection connection;
	
	@Override
	public void start() {
		Thread start = new Thread(new Runnable(){
			@Override
			public void run() {
		        try {
					//LocalDevice.getLocalDevice().setDiscoverable(DiscoveryAgent.GIAC);
					
		        	LOGGER.info("Starting Bluetooth at btspp://localhost:"
			                + serverUUID + ";name=BluetoothServerExample");
		        	
					connectionNotifier = (StreamConnectionNotifier) Connector.open("btspp://localhost:"
			                + serverUUID + ";name=BluetoothServerExample");
					
					connection = (StreamConnection) connectionNotifier.acceptAndOpen();
					if(onClientConnected!=null){
						onClientConnected.perform(null);
					}
			        LOGGER.info("Received OBEX connection ");
			        
			        Thread receive = new Thread(new Runnable(){
						@Override
						public void run() {
					        String fromClient = null;
					        DataInputStream is;
							//DataOutputStream os;
					        
							try {
								is = connection.openDataInputStream();
								//os = connection.openDataOutputStream();
					        
						        byte[] bytes = new byte[1024];
						        int data;
						        
						        //Receiving messages
						        while ((data = is.read(bytes)) != -1) {
						            fromClient = new String(bytes,0,data);
						            
						            System.out.println("Receive message:" + fromClient);
						            
						            //analyze the input from client
									processMsg(fromClient);
									
									//TODO response to the client
									
									
								}//end of while loop
						        stop();
						        
							} catch (IOException e) {
								LOGGER.info(e.getMessage());
							}
						}
			        });
			        
			        receive.start();
				} catch (BluetoothStateException e) {
					LOGGER.info(e.getMessage());
				}catch (IOException e) {
					// TODO Auto-generated catch block
					LOGGER.info(e.getMessage());
				}
			}
			
		});
		start.start();
	}

	@Override
	public void stop() {
		try {
			connection.close();
			connectionNotifier.close();
		} catch (IOException e) {
			LOGGER.info(e.getMessage());
		}
	}

	@Override
	public String getHostname() {
		try {
			return LocalDevice.getLocalDevice().getBluetoothAddress();
		} catch (BluetoothStateException e) {
			LOGGER.info(e.getMessage());
		}
		return "Bluetooth Error";
	}
}
