package com.example.QuickPointer;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import com.example.QuickPointer.net.OnDataReceiveListener;
import com.example.QuickPointer.net.TCPClient;
import com.example.QuickPointer.net.TCPProtocol;
import com.example.QuickPointer.net.UDPClient;


public class TCPClientConsoleApp {
	public static void main(String[] args) throws IOException {
        String hostName;
        int portNumber;
        
        if (args.length != 2) {
            System.err.println(
                "Usage: java EchoClient <host name> <port number>");
            System.exit(1);
        }
        
        hostName = args[0];
        portNumber = Integer.parseInt(args[1]);
        
        final TCPClient client = new TCPClient();
        final BufferedReader stdIn =
                new BufferedReader(new InputStreamReader(System.in));
        
        client.connect(hostName, portNumber);
        
		client.setOnDataReceiveListener(new OnDataReceiveListener(){
			@Override
			public void onReceive(String msg) {
				System.out.println("Server:"+msg);
				if(msg.equals(TCPProtocol.endString)){
					client.close();
					System.exit(0);
				}else{
					try {
						client.send(stdIn.readLine());
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
			
		});

    }
}

