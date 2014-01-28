package smallcampus.QuickPointer.net;

import java.io.DataInputStream;
import java.io.IOException;

import javax.bluetooth.BluetoothStateException;
import javax.microedition.io.Connector;
import javax.microedition.io.StreamConnection;
import javax.microedition.io.StreamConnectionNotifier;

public class QPBluetoothServer extends BaseServer{
    static final String serverUUID = "11111111111111111111111111111123";
	private StreamConnectionNotifier connectionNotifier;
	private StreamConnection connection;
	
	@Override
	public void start() {
        try {
			//LocalDevice.getLocalDevice().setDiscoverable(DiscoveryAgent.GIAC);
			
			connectionNotifier = (StreamConnectionNotifier) Connector.open("btspp://localhost:"
	                + serverUUID + ";name=BluetoothServerExample");
			
			connection = (StreamConnection) connectionNotifier.acceptAndOpen();
			if(onClientConnected!=null){
				onClientConnected.perform(null);
			}
	        System.out.println("Received OBEX connection ");
	        
	        Thread receive = new Thread(new Runnable(){
				@Override
				public void run() {
			        String fromClient = null;
			        DataInputStream is;
					//DataOutputStream os;
			        
			        Protocol protocol = new Protocol();
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
							switch(protocol.receiveMsg(fromClient)){
							case START:
								if(onStartReceive!=null){
									onStartReceive.perform(null);
								}
								break;
							case END:
								if(onStopReceive!=null){
									onStopReceive.perform(null);
								}
								break;
							default:
								if(fromClient.startsWith("A")){
									onCoordinateReceive.perform(Protocol.decompileCoordinateMsg(fromClient));
								}
								break;
							}
							
							//TODO response to the client
							
							
						}//end of while loop
				        stop();
				        
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
	        });
	        
	        receive.start();
		} catch (BluetoothStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Override
	public void stop() {
		try {
			connection.close();
			connectionNotifier.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
