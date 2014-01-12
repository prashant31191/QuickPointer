package com.example.QuickPointer.ui;
import java.awt.Color;
import java.awt.Graphics;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JPanel;

public class QuickPointerMainFrame extends QuickPointerBaseFrame{
	@SuppressWarnings("serial")
	protected class Pointer extends JPanel {
	    protected final int size = 10;
	    protected Color color = Color.red;
	    protected int curX=0,curY=0;
	    protected int x=0,y=0;

	    //private int targetX=0, targetY=0;
	    
	    @Override
	    public void paintComponent(Graphics g) {
	        //super.paintComponent(g);
	        
	    	g.setColor(color);

	        //clear the old point
	        g.clearRect(curX-size, curY-size, curX+size, curY+size);
	        
	        //move half of the distance per frame
	        curX = Math.round(curX+(x-curX)*sensitivity);
	        curY = Math.round(curY+(y-curY)*sensitivity);
	        
	        //draw the new point
	        g.fillOval(curX, curY, size, size);
	        
	    }    
	}
	
	
	protected Pointer p = new Pointer();
    protected final int fps = 25;
	protected final float sensitivity = 0.2f; //0-1
	
	public void setPosition(int x, int y){
		p.x=Math.min(sysDim.width,Math.max(0, x));
		p.y=Math.min(sysDim.height,Math.max(0, y));
		//p.repaint();
	}
	
	/**
	 * Translate (Circular)
	 * @param deltaX
	 * @param deltaY
	 */
	public void translateC(int deltaX, int deltaY){
		p.x = (p.x + deltaX)% sysDim.width;
		p.y = (p.y + deltaY)% sysDim.height;
		//p.repaint();
	}
	
	public void translate(int deltaX, int deltaY){
		p.x = Math.max(p.x+deltaX,sysDim.width);
		p.y = Math.max(p.y+deltaY, sysDim.width);
		//p.repaint();
	}
	
	public void setPositionR(float percentileX, float percentileY){
		p.x = Math.round(sysDim.width*percentileX);
		p.y = Math.round(sysDim.height*percentileY);
		//p.repaint();
	}
	
	@Override
	public void createAndShowGUI(){
		super.createAndShowGUI();

		frame.setGlassPane(p);
		p.setVisible(true);
		
		Timer repaintTimer = new Timer();
		repaintTimer.schedule(new TimerTask(){
			@Override
			public void run() {
				if(p.x!=p.curX||p.y!=p.curY){
					p.repaint();
				}
			}
		},2000,1000/fps);
		
		//TODO BUG Ubuntu cannot clear the point at the first few frames ??
		setPositionR(0.5f,0.5f);
	}

}
