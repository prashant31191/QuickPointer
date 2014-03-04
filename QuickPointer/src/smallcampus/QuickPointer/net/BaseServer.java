package smallcampus.QuickPointer.net;

import java.io.IOException;

import smallcampus.QuickPointer.util.EventListener;

public abstract class BaseServer {
	public final Protocol protocol = new Protocol();
	
	protected boolean isStarted = false;
	public abstract void start();
	public boolean isStarted(){return isStarted;}
	public abstract void stop();
	public abstract String getHostname();
	
	protected EventListener<?> onClientConnected;
	public void setOnConnectionReceiveListener(EventListener<?> listener){
		onClientConnected = listener;
	}
		
	protected EventListener<float[]> onCoordinateReceive;
	public void setOnCoordinateReceiveListener(EventListener<float[]> listener){
		onCoordinateReceive = listener;
	}
	
	protected EventListener<?> onStartReceive;
	public void setOnStartReceiveListener(EventListener<?> listener) {
		onStartReceive = listener;
	}

	protected EventListener<?> onStopReceive;
	public void setOnStopReceiveListener(EventListener<?> listener) {
		onStopReceive = listener;
	}
	
	protected EventListener<?> onPageUpReceive;
	public void setOnPageUpReceiveListener(EventListener<?> listener) {
		onPageUpReceive = listener;
	}
	
	protected EventListener<?> onPageDownReceive;
	public void setOnPageDownReceiveListener(EventListener<?> listener) {
		onPageDownReceive = listener;
	}
	
	protected String processMsg(String msg) throws IOException{
		switch(protocol.receiveMsg(msg)){
		case START:
			if(onStartReceive!=null){
				onStartReceive.perform(null);
			}
			break;
		case STOP:
			if(onStopReceive!=null){
				onStopReceive.perform(null);
			}
			break;
		case PAGEUP:
			if(onPageUpReceive!=null){
				onPageUpReceive.perform(null);
			}
			break;
		case PAGEDOWN:
			if(onPageDownReceive!=null){
				onPageDownReceive.perform(null);
			}
			break;
		default:
			break;
		}
		
		return protocol.getResponseMsg();
	}
}
