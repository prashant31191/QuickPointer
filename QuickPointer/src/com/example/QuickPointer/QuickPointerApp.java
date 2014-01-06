package com.example.QuickPointer;

import java.io.IOException;
import java.net.SocketException;

import com.example.QuickPointer.net.OnDataReceiveListener;
import com.example.QuickPointer.net.TCPProtocol;
import com.example.QuickPointer.net.TCPServer;
import com.example.QuickPointer.net.UDPProtocol;
import com.example.QuickPointer.net.UDPServer;
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
            
            final UDPServer server = new UDPServer(Config.DEFAULT_UDP_SERVER_PORT);
            
            server.setOnDataReceiveListener(new OnDataReceiveListener(){
	            @Override
	            public void onReceive(String msg) {
		            int x=0,y=0;
		            UDPProtocol.decompileCoordinateMsg(msg, x, y);
		            qp.setPosition(x, y);
	            }
            });
            
            final TCPServer mServer = new TCPServer(Config.DEFAULT_TCP_SERVER_PORT);
            final TCPProtocol protocol = new TCPProtocol();
            
            mServer.setOnDataReceiveListener(new OnDataReceiveListener(){
				@Override
				public void onReceive(String msg) {
					switch(protocol.receiveMsg(msg)){
						case 0:
							//Terminate this program
							mServer.out.println(protocol.getResponseMsg());
							
							server.stop();
							mServer.stop();
							break;
						case 1:	//start receive coordinates
							try {
								server.start();
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
            
        }
}