package com.example.QuickPointerApp;
import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.geom.Ellipse2D;

import javax.swing.JPanel;
import javax.swing.JComponent;

public class QuickPointerPanel extends QuickPointer{
	class Pointer extends JPanel {
	    public int size = 10;
	    private Graphics2D g2d;
	    public int curX=0,curY=0;
	    
	    private void doDrawing(Graphics g) {
	    	g2d = (Graphics2D) g;
	        
	        g2d.setColor(Color.red);

	        RenderingHints rh = new RenderingHints(RenderingHints.KEY_ANTIALIASING,
	                RenderingHints.VALUE_ANTIALIAS_ON);

	        rh.put(RenderingHints.KEY_RENDERING,
	               RenderingHints.VALUE_RENDER_QUALITY);

	        g2d.setRenderingHints(rh);
	        
	        //g2d.setComposite(AlphaComposite.getInstance(
	        //        AlphaComposite.SRC_OVER, 0.3f));

	        g2d.fillOval(curX, curY, size, size); 
	   } 

	    @Override
	    public void paintComponent(Graphics g) {
	        super.paintComponent(g);
	        doDrawing(g);
	    }    
	}
	
	
	Pointer p = new Pointer();
	
	
	public void changePosition(int x, int y){
		//p.removeAll();
		p.curX=x;
		p.curY=y;
		p.repaint();
	}
	
	@Override
	public void createAndShowGUI(){
		super.createAndShowGUI();

		frame.setGlassPane(p);
		p.setOpaque(false);
		p.setVisible(true);
		
		changePosition(sysDim.width/2,sysDim.height/2);
	}

}
