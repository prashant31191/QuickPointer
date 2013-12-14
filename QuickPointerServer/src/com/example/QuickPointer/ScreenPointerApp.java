package com.example.QuickPointerApp;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.Toolkit;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

import javax.swing.*;

public class ScreenPointerApp {
	private static boolean isRunning=false;
	
	/**
	* The HelloWorldApp class implements an application that
	* simply prints "Hello World!" to standard output.
	* @param args the command line arguments
	*/
	public static void main(String[] args) throws IOException{
		//single instance check
		if(isRunning){
			System.out.println("Another instance is running. Program terminated.");
			System.exit(1);
		}else{
			isRunning = true;
		}
		
		int portNumber = 9999;
	    //server side
	    if (args.length == 0) {
	    	System.out.println("Run with default arg: port 9999");
	    }else if(args.length ==1){
	    	portNumber = Integer.parseInt(args[0]);
	    }else{
	    	System.err.println("Usage: java EchoServer <port number>");
	        System.exit(1);
	    }
	 
	    //Schedule a job for the event-dispatching thread:
	    //creating and showing this application's GUI.
	 	final QuickPointerPanel qp = new QuickPointerPanel();
	    javax.swing.SwingUtilities.invokeLater(new Runnable() {
	        public void run() {
	            qp.createAndShowGUI();
	        }
	    });
	    
	    try{ 
	    	ServerSocket serverSocket = new ServerSocket(portNumber);
	        Socket clientSocket = serverSocket.accept();
	        PrintWriter out =
	            new PrintWriter(clientSocket.getOutputStream(), true);
	        BufferedReader in = new BufferedReader(
	            new InputStreamReader(clientSocket.getInputStream()));
	        String inputLine, outputLine;
	        
	        //send dimension information
	        out.println("A"+qp.sysDim.width+","+qp.sysDim.height);
	            
	        while ((inputLine = in.readLine()) != null) {
	            out.println(inputLine);
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
	            	out.println("END");
	              	break;
	            }
	        }
	        
	        out.close();
	        in.close();
	        clientSocket.close();
	        serverSocket.close();
	        
	        System.exit(0);
	            
	    } catch (IOException e) {
	        System.out.println("Exception caught when trying to listen on port "
	            + portNumber + " or listening for a connection");
	        System.out.println(e.getMessage());
	    }
	}
	
}
