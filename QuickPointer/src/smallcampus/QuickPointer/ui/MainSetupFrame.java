package smallcampus.QuickPointer.ui;

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Panel;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

public class MainSetupFrame extends JFrame{

	/**
	 * Create the application.
	 */
	public MainSetupFrame() {
		if(cfHandler==null){
			cfHandler = new CloseFrameHandler(){
				@Override
				public void perform() {
					MainSetupFrame.this.dispose();
				}
			};
		}
		
		initialize();
	}

	MainPanel mainPanel;
	ConnectionInfoPanel connectionPanel;
	
	/**
	 * Initialize the contents of the Frame
	 */
	private void initialize() {
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		
		setBounds(100, 100, 450, 455);
		setLocation(dim.width/2-getSize().width/2, dim.height/2-getSize().height/2);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		getContentPane().setLayout(new BorderLayout(0, 0));
		
		mainPanel = new MainPanel();
		getContentPane().add(mainPanel, BorderLayout.CENTER);
		mainPanel.addOnButtonClickListener(onClick);
		
		Panel panel_2 = new Panel();
		getContentPane().add(panel_2, BorderLayout.SOUTH);
		
		Panel panel_3 = new Panel();
		getContentPane().add(panel_3, BorderLayout.EAST);
		
		Panel panel_4 = new Panel();
		getContentPane().add(panel_4, BorderLayout.WEST);
		
		setVisible(true);
	}
	
	public void showConnectionInfoPanel(String info){
		getContentPane().remove(mainPanel);
		
		connectionPanel = new ConnectionInfoPanel(info);
		
		getContentPane().add(connectionPanel,BorderLayout.CENTER);
		pack();
	}
	
	public void showSucessfulCountDown(){
		if(connectionPanel !=null){
			connectionPanel.showCountDown();
		}
	}
	
	private final ActionListener onClick = new ActionListener(){
		@Override
		public void actionPerformed(ActionEvent e) {
			JButton b = (JButton) e.getSource();
			
			if(onSelectListener!=null && b.getText().toString().equals("TCP")){
				onSelectListener.serverSelect(OnServerSelectListener.TYPE_TCP);
			}else if(onSelectListener!=null){
				onSelectListener.serverSelect(OnServerSelectListener.TYPE_BLUETOOTH);
			}
		}
	};
	
	public interface OnServerSelectListener{
		public static final int TYPE_TCP =0 ;
		public static final int TYPE_BLUETOOTH = 1;
		public void serverSelect(int serverType);
	}
	
	private OnServerSelectListener onSelectListener = null;
	public void setOnSelectListener(OnServerSelectListener listener){
		this.onSelectListener = listener;
	}
	
	static CloseFrameHandler cfHandler;
	public static CloseFrameHandler getCloseFrameHandler(){
		return cfHandler;
	}
	
	interface CloseFrameHandler{
		public void perform();
	}
}
