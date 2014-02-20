package smallcampus.QuickPointer.android;

import smallcampus.QuickPointer.net.BaseClient;

public class ConnectionManager {
	private static ConnectionManager instance = null;
	public static ConnectionManager getInstance(){
		if(instance==null){
			instance = new ConnectionManager();
		}
		return instance;
	}
	
	protected ConnectionManager(){}
	
	private BaseClient client;
	public BaseClient getConnection(){
		return client;
	}
	
	public void setConnection(BaseClient client){
		this.client = client;
	}
}
