package com.example.QuickPointer.net;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class UDPClient implements ClientI{
	protected DatagramSocket socket;
	private Thread send;
	private Thread connect;
	
	public UDPClient() throws IOException{
		//random available socket
		socket = new DatagramSocket();
	}
	
	@Override
	public void connect(String host, int port){
		connect = new Thread(new Connect(host,port));
		connect.start();
	}
	
	@Override
	public synchronized void send(String msg){
		if(send==null || !send.isAlive()){
			send = new Thread(new SendMessage(msg));
			send.start();
		}
	}
	
	private OnConnectedListener onConnected;
	@Override
	public void setOnConnectedListener(OnConnectedListener onConnected){
		this.onConnected = onConnected;
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
					byte[] buf = new byte[UDPProtocol.PACKET_SIZE];

					if(tbuf.length>UDPProtocol.PACKET_SIZE){
						//trim extra bytes
						//TODO error handle
						System.arraycopy(tbuf, 0, buf, 0, UDPProtocol.PACKET_SIZE);
					}else{
						//put the msg on the right side of the byte array
						System.arraycopy(tbuf, 0, buf, UDPProtocol.PACKET_SIZE - tbuf.length, tbuf.length);
					}
										
					DatagramPacket p = new DatagramPacket(buf,UDPProtocol.PACKET_SIZE);
					
					socket.send(p); //block
					
					System.out.println("packet sended. length:"+msg.length());

				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
	
	public void close(){
		Thread stop = new Thread(new Stop());
		stop.start();
	}
	private class Stop implements Runnable{
		@Override
		public void run() {
			socket.close();			
		}
	}
}
