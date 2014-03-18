package smallcampus.QuickPointer.ui;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JFrame;

@SuppressWarnings("serial")
public final class QuickPointerMainFrame extends JFrame{
	
	private final Dimension sysDim = Toolkit.getDefaultToolkit().getScreenSize();
	protected final PointerEngine pe;
	protected final PointerPanel p;
    protected final int fps = 60;
	
    public QuickPointerMainFrame(){
    	super("QuickPointer");
    	    	
    	//hide the menu bar
        setUndecorated(true);
        
        //TODO
        //frame.setAlwaysOnTop(true);
        
        //transparent background
        setBackground(new Color(0,0,0,0));
        
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        setSize(sysDim.width,sysDim.height);
        setVisible(true);
        
        //set up pointer
    	pe = new PointerEngine(10f,sysDim.width,sysDim.height);
    	p = new PointerPanel(pe.getPointer(),sysDim.width,sysDim.height);

		setGlassPane(p);
		p.setVisible(true);
		
		//refresh the position of pointer over time
		Timer repaintTimer = new Timer();
		repaintTimer.schedule(new TimerTask(){
			@Override
			public void run() {
				pe.proceed(1000/fps);
				p.repaint();
			}
		},2000,1000/fps);
    }
    
    public PointerEngine getPointerEngine(){
    	return pe;
    }
}
