package com.example.QuickPointer;

import java.util.Timer;
import java.util.TimerTask;

import com.example.QuickPointer.ui.QuickPointerMainFrame;

public class RenderTestApp {

	public static void main(String[] args) {
		//creating and showing this application's GUI.
	 	final QuickPointerMainFrame qp = new QuickPointerMainFrame();
	    javax.swing.SwingUtilities.invokeLater(new Runnable() {
	        public void run() {
	            qp.createAndShowGUI();
	        }
	    });
	    
	    Timer timer = new Timer();
	    timer.schedule(new TimerTask() {
			@Override
			public void run() {
				qp.translateC(1,1);
			}}, 1000, 100);
	}

}
