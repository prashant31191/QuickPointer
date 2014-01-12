package com.example.QuickPointer.net;

public class TCPProtocol {
	public static final String endString = "END";
	public static final String startString = "START";
	public enum Status {READY, END, START};
	private Status status = Status.READY;
	public Status getStatus(){return status;}
	public Status receiveMsg(String msg){
		if(msg.equals(endString)){
			status = Status.END;
		}else if(msg.equals(startString)){
			status = Status.START;
		}else{
			System.err.println("[Warning] Unknown message.");
		}
		return status;
	}
	
	public String getResponseMsg(){
		switch(status){
		case END:
			return endString;
		case START:
			return startString;
		default: return"";
		}
	}
}
