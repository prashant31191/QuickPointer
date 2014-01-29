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
	protected final PointerPanel p = new PointerPanel(sysDim.width,sysDim.height);
    protected final int fps = 40;
	
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
		setGlassPane(p);
		p.setVisible(true);
		
		//refresh the position of pointer over time
		Timer repaintTimer = new Timer();
		repaintTimer.schedule(new TimerTask(){
			@Override
			public void run() {
				p.repaint();
			}
		},2000,1000/fps);
    }
    
    public PointerPanel getPointer(){return p;}
}
