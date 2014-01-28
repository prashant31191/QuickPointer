package smallcampus.QuickPointer.net;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.ServerSocket;
import java.net.Socket;

public final class QPTcpUdpServer extends BaseServer {
	private ServerSocket serverSocket;
	
	private Socket clientSocket;
	private PrintWriter out;
	private BufferedReader in;
	
	private DatagramSocket socket;
		
	Thread udpReceiveThread, tcpReceiveThread;
	
	public QPTcpUdpServer(int tcpPort, int udpPort) throws IOException{	
		serverSocket = new ServerSocket(tcpPort);
		socket = new DatagramSocket(udpPort);
	}
	
	@Override
	public void start() {
		if(!isStarted()){
			isStarted = true;
			new Thread(serverStart).start();
		}
	}

	@Override
	public void stop() {
		if(isStarted()){
			isStarted = false;
			new Thread(serverStop).start();
		}
	}

	private final Runnable serverStart = new Runnable(){
		@Override
		public void run() {
			try{
		        clientSocket = serverSocket.accept();
		        System.out.println("TCP Connection accepted");
		        
		        out =
		            new PrintWriter(clientSocket.getOutputStream(), true);
		        in = new BufferedReader(
		            new InputStreamReader(clientSocket.getInputStream()));
		        
		        if(onClientConnected!=null){
		        	onClientConnected.perform(null);
		        }
		        
		        tcpReceiveThread = new Thread(receiveControl);
		        tcpReceiveThread.start();
		        udpReceiveThread = new Thread(receiveCoordination);
		        udpReceiveThread.start();
		        
			}catch(IOException e){
				//TODO error handle
				e.printStackTrace();
			}
		}
	};
	
	private final Runnable serverStop = new Runnable(){
		@Override
		public void run() {
			socket.close();

			try {
				clientSocket.close();
				serverSocket.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	};
	
	private final Runnable receiveControl = new Runnable(){
		@Override
		public void run() {
			String fromClient;
			while(isStarted){
				try {
					fromClient = in.readLine();
					if(fromClient==null){
						stop();
						break;
					}
					
					switch(protocol.receiveMsg(fromClient)){
					case START:
						if(onStartReceive!=null){
							onStartReceive.perform(null);
						}
						break;
					case END:
						if(onStopReceive!=null){
							onStopReceive.perform(null);
						}
						break;
					default:
						break;
					}
					
					//send response
					out.println(protocol.getResponseMsg());
					
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	};
	
	private Runnable receiveCoordination =new Runnable(){
		@Override
		public void run() {
			while(isStarted){		
				try {
					DatagramPacket p = UDPProtocol.getNewPacket();
					
					System.out.println("waiting for packet...");
					socket.receive(p); //block
										
					if(onCoordinateReceive!=null){
						onCoordinateReceive.perform(Protocol.decompileCoordinateMsg((UDPProtocol.decompilePacket(p))));
					}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	};
}
