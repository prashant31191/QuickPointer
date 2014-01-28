package smallcampus.QuickPointer.bluetooth;

import java.io.DataInputStream;
import java.io.IOException;

import javax.bluetooth.DiscoveryAgent;
import javax.bluetooth.LocalDevice;
import javax.microedition.io.Connector;
import javax.microedition.io.StreamConnection;
import javax.microedition.io.StreamConnectionNotifier;

public class OBEXPutServer {

    static final String serverUUID = "11111111111111111111111111111123";

    public static void main(String[] args) throws IOException {

        LocalDevice.getLocalDevice().setDiscoverable(DiscoveryAgent.GIAC);

        StreamConnectionNotifier connectionNotifier = (StreamConnectionNotifier) Connector.open("btspp://localhost:"
                + serverUUID + ";name=BluetoothServerExample");

        StreamConnection connection = (StreamConnection) connectionNotifier.acceptAndOpen();
        System.out.println("Received OBEX connection ");
        
        DataInputStream is = connection.openDataInputStream();
        
        String fromClient = null;
        byte[] bytes = new byte[1024];
        int data;
        while ((data = is.read(bytes)) != -1) {
            fromClient = new String(bytes,0,data);
            System.out.println("got:" + fromClient);
        }
    }
}