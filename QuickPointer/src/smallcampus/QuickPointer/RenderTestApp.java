package smallcampus.QuickPointer;

import java.util.Timer;
import java.util.TimerTask;

import smallcampus.QuickPointer.ui.PointerEngine;
import smallcampus.QuickPointer.ui.QuickPointerMainFrame;

public class RenderTestApp {

	public static void main(String[] args) {
		//creating and showing this application's GUI.
	 	final QuickPointerMainFrame qp = new QuickPointerMainFrame();
	 	final PointerEngine pe = qp.getPointerEngine();
	    
	 	final float[] coor = new float[2];
		final double[] a = new double[1];
		
		final int[] coor2 = new int[2];
		coor2[0] = 1000;
		coor2[1] = 400;
		
		//pe.setTarget(coor2);
		
		a[0] = 0;
		
	    Timer timer = new Timer();
	    timer.schedule(new TimerTask() {
			@Override
			public void run() 
			{
				coor[0] = 0.5f + (float) (Math.sin(a[0])*0.4f);
				coor[1] = 0.5f + (float) (Math.cos(a[0])*0.4f);
				a[0] += Math.PI*5/180;
						
				pe.setTarget(coor);
			}}, 1000, 100);
			
	}
}
