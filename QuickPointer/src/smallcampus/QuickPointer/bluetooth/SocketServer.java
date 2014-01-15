package smallcampus.QuickPointer.bluetooth;

import java.io.DataInputStream;
import java.io.IOException;
 

import javax.bluetooth.BluetoothStateException;
import javax.bluetooth.LocalDevice;
import javax.microedition.io.Connector;
import javax.microedition.io.StreamConnection;
import javax.microedition.io.StreamConnectionNotifier;
 
public class SocketServer {
	
    	static final String serverUUID = "11111111111111111111111111111123";
        
    	private static LocalDevice mLocalDevice;
        private static StreamConnectionNotifier connectionNotifier;
   
        public static void main(String[] args) throws IOException, InterruptedException {
                try
                {
                connectionNotifier =
                    (StreamConnectionNotifier) Connector.open("btspp://localhost:" +
                    serverUUID+";name=BtExample;" +
                    "authenticate=false;encrypt=false;master=false");
                while(true){
                        new SocketServer().start();
                }
                } catch (BluetoothStateException e) {
                        System.out.println("Bluetooth not enabled!\nPlease enable your bluetooth first!");
                }
    }
 
    public SocketServer() throws IOException {
        mLocalDevice = LocalDevice.getLocalDevice();
                System.out.println("accepting on " + mLocalDevice.getBluetoothAddress());
    }
 
    public void start() throws IOException {
   
        StreamConnection streamConnection = connectionNotifier.acceptAndOpen();
        DataInputStream is = streamConnection.openDataInputStream();
 
        byte[] bytes = new byte[1024];
        int r;
        while ((r = is.read(bytes)) > 0) {
        		System.out.print("R:");
                System.out.println(new String(bytes, 0, r));
        }
    }
 
}