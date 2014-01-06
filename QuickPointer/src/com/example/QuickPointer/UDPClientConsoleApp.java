package com.example.QuickPointer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import com.example.QuickPointer.net.TCPClient;
import com.example.QuickPointer.net.UDPClient;

public class UDPClientConsoleApp {

	public static void main(String[] args) {
        String hostName;
        int portNumber;
        
        if (args.length != 2) {
            System.err.println(
                "Usage: java EchoClient <host name> <port number>");
            System.exit(1);
        }
        
        hostName = args[0];
        portNumber = Integer.parseInt(args[1]);
        
        try {
			UDPClient client = new UDPClient();
	        final BufferedReader stdIn =
	                new BufferedReader(new InputStreamReader(System.in));
	        
	        client.connect(hostName, portNumber);
	        
	        String fromUser;
	        while(!(fromUser=stdIn.readLine()).equals("END")){
	        	client.send(fromUser);
	        }
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		}

	}

}
