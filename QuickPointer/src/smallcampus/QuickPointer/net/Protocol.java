package smallcampus.QuickPointer.net;

import java.io.IOException;

public class Protocol {
	public static final String endString = "END";
	public static final String startString = "START";
	public enum Status {READY, END, START};
	private Status status = Status.READY;
	public Status getStatus(){return status;}
	public Status receiveMsg(String msg) throws IOException{
		if(msg==null){
			throw new IOException("Message cannot be null.");
		}
		
		if(msg.equals(endString)){
			status = Status.END;
		}else if(msg.equals(startString)){
			status = Status.START;
		}else{
			System.err.println("[Warning] Unknown message.");
		}
		return status;
	}
	
	//public static String compileCoordinateMsg(int x, int y){
	//	return "A"+x+","+y;
	//}
	
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
		switch(status){
		case END:
			return endString;
		case START:
			return startString;
		default: return"";
		}
	}
	public void reset() {
		// TODO Auto-generated method stub
		status = Status.READY;
	}
}
