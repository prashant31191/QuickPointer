package smallcampus.QuickPointer.ui;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class InitializationFrame extends JFrame {
	public static final String bluetoothString = "bluetooth";
	public static final String tcpString = "tcpip";
	
	public InitializationFrame(){
		super("QuickPointer init");
		
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		this.setLocation(dim.width/2-this.getSize().width/2, dim.height/2-this.getSize().height/2);
		
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        //setSize(500, 500);
		setVisible(true);
	}

	JButton bButton, tButton;
	public void showServerTypeSelect() {
		JPanel panel = new JPanel();
		bButton = new JButton("Bluetooth");
		tButton = new JButton("TCP/IP");
		
		bButton.addActionListener(onClick);
		tButton.addActionListener(onClick);
		
		panel.setLayout(new BoxLayout(panel,BoxLayout.Y_AXIS));
		panel.add(new JLabel("Select a server type:"));
		panel.add(tButton);
		panel.add(bButton);
		
		panel.setPreferredSize(new Dimension(200,100));
		panel.setVisible(true);
		
		getContentPane().add(panel);
		pack();
	}
	
	private final ActionListener onClick = new ActionListener(){
		@Override
		public void actionPerformed(ActionEvent e) {
			if(onSelectListener==null){
				return;
			}
			
			JButton b = (JButton) e.getSource();
			if(b.equals(tButton)){
				onSelectListener.serverSelect(OnServerSelectListener.TYPE_TCP);
			}else{
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
	
}
