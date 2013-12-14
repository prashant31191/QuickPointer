package com.example.QuickPointerApp;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

import com.example.QuickPointerApp.net.TCPClient;


public class ClientApp {
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
        
        BufferedReader stdIn =
                new BufferedReader(new InputStreamReader(System.in));
        final TCPClient client = new TCPClient(hostName,portNumber);
		client.messageReceived= new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				System.out.println("Server:" + e.getActionCommand());
			}};
		client.connectHost();
		if(client.isError()){
			System.out.println("Error connecting host: errCode = "+client.getErrorCode());
			return;
		}
		
		String fromUser;
        
        while(!(fromUser = stdIn.readLine()).equals("END")){
        	System.out.println("Client: "+fromUser);
        	client.sendMessage(fromUser);
        }
        
        //Ending procedures
        System.out.println("Ending operation.");
        client.sendMessage("END");
        client.stop();
    }
}

