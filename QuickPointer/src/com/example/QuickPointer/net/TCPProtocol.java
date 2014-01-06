package com.example.QuickPointer.net;

public class TCPProtocol {
	public static final String endString = "END";
	public static final String startString = "START";
	private int status = 1;
	public int getStatus(){return status;}
	public int receiveMsg(String msg){
		if(msg.equals(endString)){
			status = 0;
		}else if(msg.equals(startString)){
			status = 1;
		}else{
			System.err.println("[Warning] Unknown message.");
		}
		return status;
	}
	
	public String getResponseMsg(){
		switch(status){
		case 0:
			return endString;
		case 1:
			return startString;
		}
		return "";
	}
}
