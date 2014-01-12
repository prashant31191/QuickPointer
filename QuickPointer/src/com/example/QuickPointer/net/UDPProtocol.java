package com.example.QuickPointer.net;

import java.io.IOException;
import java.net.DatagramPacket;

public class UDPProtocol {
	public static final int PACKET_SIZE = 64;
	
	public static String compileCoordinateMsg(int x, int y){
		return "A"+x+","+y;
	}
	public static int[] decompileCoordinateMsg(String msg) throws IOException {
		if(msg.startsWith("A")){
			int i = msg.indexOf(",");
			if(i>1){
				int[] c = new int[2];
				c[0] =Integer.parseInt(msg.substring(1,i));
				c[1] = Integer.parseInt(msg.substring(i+1));
				return c;
			}
		}
		throw new IOException("Cannot decompile coordinate msg");
	}
	
	public static DatagramPacket compilePacket(String msg){
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
		return new DatagramPacket(buf,PACKET_SIZE);
	}
	
	public static DatagramPacket getNewPacket(){
		return new DatagramPacket(new byte[PACKET_SIZE],PACKET_SIZE);
	}
	
	public static String decompilePacket(DatagramPacket packet){
		byte[] buf = packet.getData();
		int firstByte;
		//search for the first byte
		for(firstByte=0;firstByte<UDPProtocol.PACKET_SIZE&& buf[firstByte]==0;firstByte++){}
		//construct the new String
		return new String(buf,firstByte,UDPProtocol.PACKET_SIZE-firstByte);
	}
}
