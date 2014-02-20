package smallcampus.QuickPointer.ui;

import javax.swing.JFrame;
import javax.swing.JPanel;

import java.awt.GridBagLayout;
import java.awt.Canvas;
import java.awt.GridBagConstraints;

import javax.swing.JLabel;

import java.awt.Insets;
import java.util.Timer;
import java.util.TimerTask;

public class ConnectionInfoPanel extends JPanel {

	final JLabel lblWaitingForConnection = new JLabel("Waiting for connection...");
	
	/**
	 * Create the panel.
	 */
	public ConnectionInfoPanel(String hostname) {
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.rowHeights = new int[]{178, 41, 0};
		gridBagLayout.columnWidths = new int[]{157, 179};
		setLayout(gridBagLayout);
		
		Canvas canvas = new Canvas();
		GridBagConstraints gbc_canvas = new GridBagConstraints();
		gbc_canvas.insets = new Insets(0, 0, 5, 0);
		gbc_canvas.gridx = 0;
		gbc_canvas.gridy = 0;
		gbc_canvas.gridwidth = 2;
		add(canvas, gbc_canvas);
		
		JLabel lblYourHostname = new JLabel("Your hostname:");
		GridBagConstraints gbc_lblYourHostname = new GridBagConstraints();
		gbc_lblYourHostname.insets = new Insets(0, 0, 5, 5);
		gbc_lblYourHostname.gridx = 0;
		gbc_lblYourHostname.gridy = 1;
		add(lblYourHostname, gbc_lblYourHostname);
		
		JLabel lblXxxxxxxxxxxxxxxxxxxxxx = new JLabel(hostname);
		GridBagConstraints gbc_lblXxxxxxxxxxxxxxxxxxxxxx = new GridBagConstraints();
		gbc_lblXxxxxxxxxxxxxxxxxxxxxx.insets = new Insets(0, 0, 5, 0);
		gbc_lblXxxxxxxxxxxxxxxxxxxxxx.gridx = 1;
		gbc_lblXxxxxxxxxxxxxxxxxxxxxx.gridy = 1;
		add(lblXxxxxxxxxxxxxxxxxxxxxx, gbc_lblXxxxxxxxxxxxxxxxxxxxxx);
		
		//JLabel lblWaitingForConnection = new JLabel("Waiting for connection...");
		GridBagConstraints gbc_lblWaitingForConnection = new GridBagConstraints();
		gbc_lblWaitingForConnection.gridx = 0;
		gbc_lblWaitingForConnection.gridy = 2;
		gbc_lblWaitingForConnection.gridwidth = 2;
		add(lblWaitingForConnection, gbc_lblWaitingForConnection);

	}

	static Timer timer;
	public void showCountDown() {
		timer = new Timer();
		interval = 3;
		timer.schedule(new TimerTask(){
			@Override
			public void run() {
				lblWaitingForConnection.setText("Connection success. starting in "+interval+" seconds");
				if(interval<=0){
					MainSetupFrame.getCloseFrameHandler().perform();
				}
				setInterval();
			}
		}, 500, 1000);
		
		
	}
	
	private static int interval;
	private static final int setInterval() {
	    if (interval < 0){
	        timer.cancel();
	    }
	    return --interval;
	}

}
