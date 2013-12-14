package com.example.QuickPointerApp;

import java.io.IOException;

import com.example.QuickPointerApp.net.UDPServer;

public class ScreenPointerAppV2 {
	public static void main(String[] args) throws IOException{
		int portNumber = UDPServer.DEFAULTPORT;
		
		if (args.length == 0) {
	    	System.out.println("Run with default port "+portNumber);
	    }else if(args.length !=1){
	    	System.err.println("Usage: java EchoServer <port number>");
	        System.exit(1);
	    }else{
	    	portNumber = Integer.parseInt(args[0]);
	    }
		
		//creating and showing this application's GUI.
	 	final QuickPointerPanel qp = new QuickPointerPanel();
	    javax.swing.SwingUtilities.invokeLater(new Runnable() {
	        public void run() {
	            qp.createAndShowGUI();
	        }
	    });
	    
	    final UDPServer server = new UDPServer(portNumber);
	    
		server.setOnDataReceiveListener(new UDPServer.OnDataReceiveListener(){

			@Override
			public void onReceive(Object args) {
				System.out.println("Data received.");
				
				String inputLine = (String) args;
				if (inputLine.startsWith("A")){
	              	int i = inputLine.indexOf(",");
	              	if(i<1){
	              		System.out.println("Error: Wrong msg format \""+inputLine+"\"");
	              	}else{
		                int x = Integer.parseInt(inputLine.substring(1,i));
		                int y = Integer.parseInt(inputLine.substring(i+1));
		                qp.changePosition(x,y);
		                System.out.println("Position change to "+x+","+y);
	              	}
	            }else if(inputLine.equals("END")){
	            	server.stop();
	            	System.exit(0);
	            }
			}
		});
		
		System.out.println("Server starting...");
		server.start();
	}
}
