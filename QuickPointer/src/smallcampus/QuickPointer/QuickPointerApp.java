package smallcampus.QuickPointer;

import java.io.IOException;

import smallcampus.QuickPointer.net.BaseServer;
import smallcampus.QuickPointer.net.EventListener;
import smallcampus.QuickPointer.net.QPTcpUdpServer;
import smallcampus.QuickPointer.ui.QuickPointerMainFrame;

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
            
            final BaseServer server = new QPTcpUdpServer(Config.DEFAULT_TCP_SERVER_PORT,Config.DEFAULT_UDP_SERVER_PORT);
            
//            final UDPServer server = new UDPServer(Config.DEFAULT_UDP_SERVER_PORT);
                        
            server.setOnCoordinateReceiveListener(new EventListener<int[]>(){
				@Override
				public void perform(int[] args) {
					qp.setPosition(args[0], args[1]);
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