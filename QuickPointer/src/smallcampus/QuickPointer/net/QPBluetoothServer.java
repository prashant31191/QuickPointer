package smallcampus.QuickPointer.net;

import java.io.IOException;
import java.io.InputStream;

import javax.bluetooth.BluetoothStateException;
import javax.bluetooth.DiscoveryAgent;
import javax.bluetooth.LocalDevice;
import javax.microedition.io.Connector;
import javax.obex.HeaderSet;
import javax.obex.Operation;
import javax.obex.ResponseCodes;
import javax.obex.ServerRequestHandler;
import javax.obex.SessionNotifier;

public class QPBluetoothServer extends BaseServer{
	static QPBluetoothServer instance;
	public static QPBluetoothServer getInstance(){
		if(instance!=null){
			return instance;
		}
		return new QPBluetoothServer();
	}
	
	protected QPBluetoothServer(){}
	
    static final String serverUUID = "11111111111111111111111111111123";
	private SessionNotifier serverConnection;
	
	@Override
	public void start() {
        try {
			LocalDevice.getLocalDevice().setDiscoverable(DiscoveryAgent.GIAC);
			
			serverConnection = (SessionNotifier) Connector.open("btgoep://localhost:"
			        + serverUUID + ";name=ObexExample");
	        int count = 0;
	        while(count < 2) {
	            RequestHandler handler = new RequestHandler();
	            serverConnection.acceptAndOpen(handler);
	            System.out.println("Received OBEX connection " + (++count));
	        }
			
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
			serverConnection.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
    private static class RequestHandler extends ServerRequestHandler {

        public int onPut(Operation op) {
            try {
                HeaderSet hs = op.getReceivedHeaders();
                String name = (String) hs.getHeader(HeaderSet.NAME);
                if (name != null) {
                    System.out.println("put name:" + name);
                }

                InputStream is = op.openInputStream();

                StringBuffer buf = new StringBuffer();
                int data;
                while ((data = is.read()) != -1) {
                    buf.append((char) data);
                }

//                if(QPBluetoothServer.getInstance().onDataReceive!=null){
//                	QPBluetoothServer.getInstance().onDataReceive.perform(buf.toString());
//                }
                
                System.out.println("got:" + buf.toString());

                op.close();
                return ResponseCodes.OBEX_HTTP_OK;
            } catch (IOException e) {
                e.printStackTrace();
                return ResponseCodes.OBEX_HTTP_UNAVAILABLE;
            }
        }
    }
}
