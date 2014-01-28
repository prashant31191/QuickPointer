package smallcampus.QuickPointer.net;


public abstract class BaseClient {
	public final Protocol protocol = new Protocol();
	
	protected boolean isConnected = false;
	
	public abstract void connect();
	public abstract void disconnect();
	public boolean isConnected(){return isConnected;}
	
	public abstract void sendCoordinateData(float x, float y);
	public abstract void sendStartControl();
	public abstract void sendStopControl();
	
	protected EventListener<String> onControlReceive;
	public void setOnControlReceiveListener(EventListener<String> listener) {
		onControlReceive = listener;
	}

	protected EventListener<?> onServerConnected;
	public void setOnServerConnectedListener(EventListener<?> listener) {
		onServerConnected = listener;
	}
}
