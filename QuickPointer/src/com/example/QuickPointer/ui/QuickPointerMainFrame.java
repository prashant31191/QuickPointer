package com.example.QuickPointer.ui;
import java.awt.Color;
import java.awt.Graphics;

import javax.swing.JPanel;

public class QuickPointerMainFrame extends QuickPointerBaseFrame{
	@SuppressWarnings("serial")
	class Pointer extends JPanel {
	    public final int size = 10;
	    public Color color = Color.red;
	    private int curX=0,curY=0;
	    private int x=0,y=0;
	    
	    @Override
	    public void paintComponent(Graphics g) {
	        //super.paintComponent(g);
	        
	    	g.setColor(color);

	        //clear the old point
	        g.clearRect(curX-size, curY-size, curX+size, curY+size);
	        
	        //draw the new point
	        g.fillOval(x, y, size, size);
	        
	        //update current position
	        curX=x;
	        curY=y;
	    }    
	}
	
	
	Pointer p = new Pointer();
	
	
	public void setPosition(int x, int y){
		p.x=x;
		p.x=y;
		p.repaint();
	}
	
	/**
	 * Translate (Circular)
	 * @param deltaX
	 * @param deltaY
	 */
	public void translateC(int deltaX, int deltaY){
		p.x = (p.x + deltaX)% sysDim.width;
		p.y = (p.y + deltaY)% sysDim.height;
		p.repaint();
	}
	
	public void translate(int deltaX, int deltaY){
		p.x = Math.max(p.x+deltaX,sysDim.width);
		p.y = Math.max(p.y+deltaY, sysDim.width);
		p.repaint();
	}
	
	public void setPositionR(float percentileX, float percentileY){
		p.x = Math.round(sysDim.width*percentileX);
		p.y = Math.round(sysDim.height*percentileY);
		p.repaint();
	}
	
	@Override
	public void createAndShowGUI(){
		super.createAndShowGUI();

		frame.setGlassPane(p);
		p.setVisible(true);
		
		setPosition(sysDim.width/2,sysDim.height/2);
	}

}
