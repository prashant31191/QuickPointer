package com.example.QuickPointer.net;

public interface ClientI {
	public static final int UDP_PACKET_SIZE = 64;
	
	public void connect(String host, int port);
	public void send(String msg);
	public void setOnConnectedListener(OnConnectedListener onConnected);
	public void close();

	public interface OnConnectedListener {
		public void onConnected(Object args);
	}
}
