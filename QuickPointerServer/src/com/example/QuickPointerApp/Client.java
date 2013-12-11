package com.example.QuickPointerApp;


import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;

//Java console client for testing

public class Client{

	public Client(String host,int port){
		setHost(host);
		setPort(port);
	}
	
    protected String hostName=null;
    protected int portNumber=0;
    protected Socket kkSocket = null;
    public PrintWriter out;
    public BufferedReader in;
    public ActionListener messageReceived = null;
    private short errCode = 0;
    //protected String endMessage = "END"; 
    
    public void setHost(String host){hostName= host;}
    public void setPort(int port){portNumber = port;}
    public int getErrorCode(){return errCode;}
    public boolean isError(){return (errCode!=0);}
    
	public void connectHost(){
		if(hostName==null || portNumber<1){
			errCode = 1;
			return;
		}
		if(kkSocket!=null && !kkSocket.isClosed()){
			errCode=2;
			return;
		}
		
        try {
			kkSocket = new Socket(hostName, portNumber);
			// TODO TCP keepalive implementation
	        out = new PrintWriter(kkSocket.getOutputStream(), true);
	        in = new BufferedReader(
	            new InputStreamReader(kkSocket.getInputStream()));
	        
	        //Get welcome msg
	        System.out.println(in.readLine());
		} catch (UnknownHostException e) {
			e.printStackTrace();
			errCode =3;
			return;
		} catch (IOException e) {
			e.printStackTrace();
			errCode=4;
			return;
		}
	}
	
	Thread sendNReceive = null;
	
    public void sendMessage(String msg)
    {
    	if(sendNReceive!=null && sendNReceive.isAlive()){
    		System.out.println("Error: Thread is still running, msg skipeed.");
    	}else{
    		sendNReceive = new Thread(new MessageSendNReceive(msg));
    		sendNReceive.run();
    	}
    }
    public void stop()
    {
    	//TODO not sure if this works
    	sendNReceive.interrupt();

    	out.close();
    	try {
    		in.close();
			this.kkSocket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
	
	class MessageSendNReceive implements Runnable{
		public MessageSendNReceive(String msg){
			fromUser = msg;
		}
		public String fromUser = "";
		@Override
		public void run() {
			out.println(fromUser);
			try {
				messageReceived.actionPerformed(new ActionEvent(this,0,in.readLine()));
			} catch (IOException e) {
				errCode = 5;
				e.printStackTrace();
			}			
		}
	}

}
