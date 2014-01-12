package com.example.QuickPointer.ui;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.JFrame;

public class QuickPointerBaseFrame {
	   protected JFrame frame;
	   public Dimension sysDim = Toolkit.getDefaultToolkit().getScreenSize();
	    /**
	     * Create the GUI and show it.  For thread safety,
	     * this method should be invoked from the
	     * event-dispatching thread.
	     */
	    public void createAndShowGUI() {
	        //Create and set up the window.
	        frame = new JFrame("QuickPointer");
	        
	        //hide the menu bar
	        frame.setUndecorated(true);
	        
	        //TODO
	        //frame.setAlwaysOnTop(true);
	        
	        //transparent background
	        frame.setBackground(new Color(0,0,0,0));
	        
	        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	         	 
	        //Display the window.
	        //frame.pack();
	        frame.setSize(sysDim.width,sysDim.height);
	        frame.setVisible(true);
	    }
}