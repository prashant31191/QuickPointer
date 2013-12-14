package com.example.QuickPointer;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

import com.example.QuickPointer.net.ClientI;
import com.example.QuickPointer.net.TCPClient;
import com.example.QuickPointer.net.UDPClient;


public class ClientConsoleApp {
	public static void main(String[] args) throws IOException {
        String hostName;
        int portNumber;
        
        if (args.length != 3) {
            System.err.println(
                "Usage: java EchoClient <host name> <port number> <0 TCP/1 UDP>");
            System.exit(1);
        }
        
        hostName = args[0];
        portNumber = Integer.parseInt(args[1]);
        int mode = Integer.parseInt(args[1]);
        ClientI client = null;
        BufferedReader stdIn =
                new BufferedReader(new InputStreamReader(System.in));
        
        switch(mode){
        case 0:
        	client = new TCPClient();
        	break;
        case 1:
        	client = new UDPClient();
        	break;
        default:
        	System.err.println("Error mode");
        	System.exit(1);
        }        
        
        client.connect(hostName, portNumber);
        
		String fromUser;
        
        while(!(fromUser = stdIn.readLine()).equals("END")){
        	System.out.println("Client: "+fromUser);
        	client.send(fromUser);
        }
        
        //Ending procedures
        System.out.println("Ending operation.");
        client.send("END");
        client.close();

    }
}

