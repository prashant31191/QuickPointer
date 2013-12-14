package com.example.QuickPointerApp.net;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class UDPClient {
	protected DatagramSocket socket;
	public static final int DEFAULTPORT = 10000;
	private static final int PACKETSIZE = 64;
	private Thread send;
	private Thread connect;
	
	public UDPClient(int port) throws IOException{
		if(port <1 || port>65535){
			System.err.println("UDPServer: port out of range");
			throw new IOException("port out of range");
		}
		socket = new DatagramSocket(port);
	}
	
	public void connect(String host, int port){
		connect = new Thread(new Connect(host,port));
		connect.start();
	}
	
	public synchronized void send(String msg){
		if(send==null || !send.isAlive()){
			send = new Thread(new SendMessage(msg));
			send.start();
		}
	}
	
	private OnConnectedListener onConnected;
	public void setOnDataReceiveListener(OnConnectedListener onConnected){
		this.onConnected = onConnected;
	}
	public interface OnConnectedListener {
		public void onConnected(Object args);
	}
	
	private class Connect implements Runnable{
		String host;
		int port;
		public Connect(String host, int port){
			this.host = host;
			this.port = port;
		}
		@Override
		public void run() {
			try {
				socket.connect(InetAddress.getByName(host), port);
				if(onConnected!=null){
					onConnected.onConnected(true);
				}
			} catch (UnknownHostException e) {
				e.printStackTrace();
				if(onConnected!=null){
					onConnected.onConnected(false);
				}
			}
		}
	}
	private class SendMessage implements Runnable{
		String msg;
		public SendMessage(String msg){this.msg=msg;}
		
		@Override
		public void run() {
			if(socket.isConnected()){
				try {
					System.out.println("Starting pharsing msg...");
					System.out.println("msg:"+msg);

					byte[] tbuf = msg.getBytes();
					byte[] buf = new byte[PACKETSIZE];

					if(tbuf.length>PACKETSIZE){
						//trim extra bytes
						//TODO error handle
						System.arraycopy(tbuf, 0, buf, 0, PACKETSIZE);
					}else{
						//put the msg on the right side of the byte array
						System.arraycopy(tbuf, 0, buf, PACKETSIZE - tbuf.length, tbuf.length);
					}
										
					DatagramPacket p = new DatagramPacket(buf,PACKETSIZE);
					
					socket.send(p); //block
					
					System.out.println("packet sended. length:"+msg.length());

				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
}
