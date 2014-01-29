package smallcampus.QuickPointer.ui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JPanel;

@SuppressWarnings("serial")
public class PointerPanel extends JPanel {
	
    protected final int size = 10;
    public Color color = Color.red;
    
    //Current x,y on the screen
    protected int curX=0,curY=0;
    //Target x,y
    protected int x=0,y=0;
    
    //Width and height of the screen
    protected int width,height;
    
    public PointerPanel(int width, int height){
    	this.width = width;
    	this.height = height;
    }
    
	protected float sensitivity = 0.1f; //0-1

    @Override
    public void paintComponent(Graphics g) {
    	
    	g.setColor(color);

    	//set transparent background
    	((Graphics2D) g).setBackground(new Color(0,0,0,0));
        //clear the old point
        g.clearRect(Math.max(0, curX-size), Math.max(0, curY-size), curX+size, curY+size);
        
        //move half of the distance per frame
        curX = Math.round(curX+(x-curX)*sensitivity);
        curY = Math.round(curY+(y-curY)*sensitivity);
        
        //draw the new point
        g.fillOval(curX, curY, size, size);
    }
    
    @Override
    public void repaint(){
    	if(x!=curX||y!=curY){
			super.repaint();
		}
    }
    
    public void setSensitivity(float s){
    	this.sensitivity = Math.max(0, Math.min(1, s)); //0 - 1
    }
    
	public void setPosition(int x, int y){
		x = Math.min(width,Math.max(0, x));
		y = Math.min(height,Math.max(0, y));
	}
	
	/**
	 * Translate (Circular)
	 * @param deltaX
	 * @param deltaY
	 */
	public void translateC(int deltaX, int deltaY){
		x = (x + deltaX)% width;
		y = (y + deltaY)% height;
	}
	
	public void translate(int deltaX, int deltaY){
		x = Math.max(x+deltaX,width);
		y = Math.max(y+deltaY, width);
	}
	
	public void setPositionR(float percentileX, float percentileY){
		x = Math.round(width*percentileX);
		y = Math.round(height*percentileY);
	}
}