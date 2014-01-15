package smallcampus.QuickPointer.net;

public abstract class BaseServer {
	public final Protocol protocol = new Protocol();
	
	protected boolean isStarted = false;
	public abstract void start();
	public boolean isStarted(){return isStarted;}
	public abstract void stop();
	
	protected EventListener<?> onClientConnected;
	public void setOnConnectionReceiveListener(EventListener<?> listener){
		onClientConnected = listener;
	}
		
	protected EventListener<int[]> onCoordinateReceive;
	public void setOnCoordinateReceiveListener(EventListener<int[]> listener){
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

}
