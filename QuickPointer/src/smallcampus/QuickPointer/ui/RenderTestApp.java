package smallcampus.QuickPointer.ui;

import java.util.Timer;
import java.util.TimerTask;

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
				qp.translateC(100,100);
			}}, 1000, 1000);
	}

}
