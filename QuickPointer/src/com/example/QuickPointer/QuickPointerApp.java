package com.example.QuickPointer;

import java.io.IOException;
import com.example.QuickPointer.net.OnDataReceiveListener;
import com.example.QuickPointer.net.QuickPointerServer;
import com.example.QuickPointer.net.UDPProtocol;
import com.example.QuickPointer.ui.QuickPointerMainFrame;

public class QuickPointerApp {
        public static void main(String[] args) throws IOException{
        //        int portNumber = ServerI.DEFAULT_PORT;
                
        //        if (args.length == 0) {
        //            System.out.println("Run with default port "+portNumber);
       //     }else if(args.length !=1){
       //             System.err.println("Usage: java EchoServer <port number>");
       //         System.exit(1);
       //     }else{
       //             portNumber = Integer.parseInt(args[0]);
       //     }
                
            //creating and showing this application's GUI.
            final QuickPointerMainFrame qp = new QuickPointerMainFrame();
            javax.swing.SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    qp.createAndShowGUI();
                }
            });
            
            final QuickPointerServer server = new QuickPointerServer(Config.DEFAULT_TCP_SERVER_PORT,Config.DEFAULT_UDP_SERVER_PORT);
            
//            final UDPServer server = new UDPServer(Config.DEFAULT_UDP_SERVER_PORT);
            
            server.setOnCoordinateDataReceiveListener(new OnDataReceiveListener(){
	            @Override
	            public void onReceive(String msg) {
		            int[] c;
		            try {
						c = UDPProtocol.decompileCoordinateMsg(msg);
			            System.out.println("Setting new position:"+c[0]+","+c[1]);
						qp.setPosition(c[0], c[1]);
					} catch (IOException e) {
						//ignore the packet
					}
	            }
            });
            
            //final TCPServer mServer = new TCPServer(Config.DEFAULT_TCP_SERVER_PORT);
            //final TCPProtocol protocol = new TCPProtocol();
            
            /*mServer.setOnDataReceiveListener(new OnDataReceiveListener(){
				@Override
				public void onReceive(String msg) {
					System.out.println("TCP msg received:"+msg);
					switch(protocol.receiveMsg(msg)){
						case END:
							//Terminate this program TODO
							mServer.send(protocol.getResponseMsg());
							
							server.stop();
							mServer.stop();
							break;
						case START:	//start receive coordinates
							try {
								System.out.println("Starting UDPServer...");
								server.start();
								//TODO
								mServer.send(TCPProtocol.startString);
							} catch (SocketException e) {
								e.printStackTrace();
								System.exit(1);
							}
							break;
						default:
							System.err.println("[Warning] Unknown status.");
							break;
					}
				}
            });
            */
            
            System.out.println("Starting Server...");
            server.start();
        }
}