package smallcampus.QuickPointer.ui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JPanel;

@SuppressWarnings("serial")
public class PointerPanel extends JPanel {
	protected Pointer pointer;
	public Pointer getPointer(){return pointer;}
	public void setPointer(Pointer pointer){
		this.pointer = pointer;
	}
	
    protected final int size = 10;
    public Color color = Color.red;
    
    //Current x,y on the screen
    protected int curX=0,curY=0;
        
    //Width and height of the screen
    protected int width,height;
    
    public PointerPanel(Pointer pointer, int width, int height){
    	setPointer(pointer);
    	this.width = width;
    	this.height = height;
    }
    
	@Override
    public void paintComponent(Graphics g) {
    	
    	g.setColor(color);

    	//set transparent background
    	((Graphics2D) g).setBackground(new Color(0,0,0,0));
        //clear the old point
        g.clearRect(Math.max(0, curX-size), Math.max(0, curY-size), curX+size, curY+size);
        
        curX = pointer.getCoordinate()[0];
        curY = pointer.getCoordinate()[1];
        
        //draw the new point
        g.fillOval(curX, curY, size, size);
    }
    
}