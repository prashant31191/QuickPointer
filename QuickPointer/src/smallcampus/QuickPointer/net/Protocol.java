package smallcampus.QuickPointer.net;

import java.io.IOException;

public class Protocol {	
	public enum Status {READY, STOP, START, PAGEUP, PAGEDOWN};
	public static String getStatusString(Status status){
		switch(status){
		case STOP:
			return "STOP";
		case START:
			return "START";
		case PAGEUP:
			return "PAGEUP";
		case PAGEDOWN:
			return "PAGEDOWN";
		default:
			break;
		}
		return "";
	}
	
	public static Status getStatus(String status) throws IOException{
		switch(status){
		case "STOP":
			return Status.STOP;
		case "START":
			return Status.START;
		case "PAGEUP":
			return Status.PAGEUP;
		case "PAGEDOWN":
			return Status.PAGEDOWN;
		default:
			return Status.READY;
		}
	}
	
	private Status status = Status.READY;
	public Status getStatus(){return status;}
	
	public Status receiveMsg(String msg) throws IOException{
		if(msg==null){
			throw new IOException("Message cannot be null.");
		}
		
		status = Protocol.getStatus(msg);
		
		return status;
	}
	
	public static String compileCoordinateMsg(float x, float y){
		return "A"+x+","+y;
	}
	
	public static float[] decompileCoordinateMsg(String msg) throws IOException {
		if(msg.startsWith("A")){
			int i = msg.indexOf(",");
			if(i>1){
				float[] c = new float[2];
				c[0] = Float.parseFloat(msg.substring(1,i));
				c[1] = Float.parseFloat(msg.substring(i+1));
				return c;
			}
		}
		throw new IOException("Cannot decompile coordinate msg");
	}
	
	public String getResponseMsg(){
		//TODO
		return "";
	}
	public void reset() {
		// TODO Auto-generated method stub
		status = Status.READY;
	}
}
