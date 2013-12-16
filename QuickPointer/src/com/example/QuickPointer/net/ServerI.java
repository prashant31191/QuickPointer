package com.example.QuickPointer.net;

import java.net.SocketException;

public interface ServerI {
	public static final int DEFAULT_PORT = 9999;
	public void setPort(int port);
	public void start() throws SocketException;
	public void stop();
	public void setOnDataReceiveListener(OnDataReceiveListener onReceive);
	
	public interface OnDataReceiveListener {
		public void onReceive(Object args);
	}
}
