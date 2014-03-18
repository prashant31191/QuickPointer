package smallcampus.QuickPointer.net;

public final class DummyClient extends BaseClient {

	public DummyClient(){
		this.isConnected = true;
	}
	
	@Override
	public void connect() {}

	@Override
	public void disconnect() {}

	@Override
	public void sendCoordinateData(float x, float y) {	}

	@Override
	public void sendStartControl() {	}

	@Override
	public void sendStopControl() { }

	@Override
	public void sendPageUpControl() {}

	@Override
	public void sendPageDownControl() {}

}
