package smallcampus.QuickPointer.ui;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Panel;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public class MainPanel extends JPanel {

	private JButton tcpButton, btButton;
	/**
	 * Create the panel.
	 */
	public MainPanel() {
		GridBagLayout gbl_panel = new GridBagLayout();
		gbl_panel.columnWidths = new int[]{428, 0};
		gbl_panel.rowHeights = new int[]{83, 33, 142, 31, 81, 0};
		gbl_panel.columnWeights = new double[]{0.0, Double.MIN_VALUE};
		gbl_panel.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		setLayout(gbl_panel);
		
		Canvas canvas = new Canvas();
		canvas.setBackground(Color.WHITE);
		GridBagConstraints gbc_canvas = new GridBagConstraints();
		gbc_canvas.fill = GridBagConstraints.BOTH;
		gbc_canvas.insets = new Insets(0, 0, 5, 0);
		gbc_canvas.gridx = 0;
		gbc_canvas.gridy = 0;
		gbc_canvas.weighty = .2;
		add(canvas, gbc_canvas);
		
		JLabel lblNewLabel = new JLabel("QuickPointer");
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		GridBagConstraints gbc_lblNewLabel = new GridBagConstraints();
		gbc_lblNewLabel.fill = GridBagConstraints.BOTH;
		gbc_lblNewLabel.insets = new Insets(0, 0, 5, 0);
		gbc_lblNewLabel.gridx = 0;
		gbc_lblNewLabel.gridy = 1;
		gbc_lblNewLabel.weighty = .05;
		add(lblNewLabel, gbc_lblNewLabel);
		
		JLabel lblNewLabel_1 = new JLabel("Introduction xxxxxxxxxxxxxxxxxxxxxx");
		lblNewLabel_1.setVerticalAlignment(SwingConstants.TOP);
		GridBagConstraints gbc_lblNewLabel_1 = new GridBagConstraints();
		gbc_lblNewLabel_1.fill = GridBagConstraints.BOTH;
		gbc_lblNewLabel_1.insets = new Insets(0, 0, 5, 0);
		gbc_lblNewLabel_1.gridx = 0;
		gbc_lblNewLabel_1.gridy = 2;
		gbc_lblNewLabel_1.weighty = .4;
		add(lblNewLabel_1, gbc_lblNewLabel_1);
		
		JLabel lblNewLabel_2 = new JLabel("Choose a connection type:");
		GridBagConstraints gbc_lblNewLabel_2 = new GridBagConstraints();
		gbc_lblNewLabel_2.fill = GridBagConstraints.BOTH;
		gbc_lblNewLabel_2.insets = new Insets(0, 0, 5, 0);
		gbc_lblNewLabel_2.gridx = 0;
		gbc_lblNewLabel_2.gridy = 3;
		gbc_lblNewLabel_2.weighty = .05;
		add(lblNewLabel_2, gbc_lblNewLabel_2);
		
		Panel panel_1 = new Panel();
		GridBagConstraints gbc_panel_1 = new GridBagConstraints();
		gbc_panel_1.fill = GridBagConstraints.BOTH;
		gbc_panel_1.gridx = 0;
		gbc_panel_1.gridy = 4;
		gbc_panel_1.weighty = .3;
		add(panel_1, gbc_panel_1);
		GridBagLayout gbl_panel_1 = new GridBagLayout();
		panel_1.setLayout(gbl_panel_1);
		
		tcpButton = new JButton("TCP");

		GridBagConstraints gbc_btnNewButton = new GridBagConstraints();
		gbc_btnNewButton.gridx = 0;
		gbc_btnNewButton.gridy = 0;
		gbc_btnNewButton.ipadx = 90;
		gbc_btnNewButton.ipady = 28;
		gbc_btnNewButton.weightx = 0.5;
		panel_1.add(tcpButton, gbc_btnNewButton);
		
		btButton = new JButton("Bluetooth");

		GridBagConstraints gbc_btnNewButton_1 = new GridBagConstraints();
		gbc_btnNewButton_1.gridx = 1;
		gbc_btnNewButton_1.gridy = 0;
		gbc_btnNewButton_1.ipadx = 90;
		gbc_btnNewButton_1.ipady = 28;
		gbc_btnNewButton_1.weightx = 0.5;
		panel_1.add(btButton, gbc_btnNewButton_1);
	}
	
	public void addOnButtonClickListener(ActionListener listener){
		btButton.addActionListener(listener);
		tcpButton.addActionListener(listener);
	}

}
