package smallcampus.QuickPointer.ui;

import java.util.Timer;
import java.util.TimerTask;

public class RenderTestApp {

	public static void main(String[] args) {
		//creating and showing this application's GUI.
	 	final QuickPointerMainFrame qp = new QuickPointerMainFrame();
	    
	    Timer timer = new Timer();
	    timer.schedule(new TimerTask() {
			@Override
			public void run() {
				qp.getPointer().translateC(100,100);
			}}, 1000, 1000);
	}

}
