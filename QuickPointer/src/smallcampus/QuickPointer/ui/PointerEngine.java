package smallcampus.QuickPointer.ui;

public class PointerEngine {
	public PointerEngine(float f, int mx, int my){
		setSpeed(f);
		
		setBoundary(mx,my);
		
		//default position: center of screen
		pointer = new Pointer(mx/2,my/2);
		
		target = new int[2];
		target[0] = pointer.getCoordinate()[0];
		target[1] = pointer.getCoordinate()[1];
	}
	
	private float speed;
	private int[] boundary = new int[2];
	public void setBoundary(int mx, int my){
		boundary[0] = mx;
		boundary[1] = my;
	}
	
	//Caution may cause the missing of negative component
	private static final int power = 1;
	public void setSpeed(float f){
		this.speed = f;
	}
	
	private Pointer pointer;
	public Pointer getPointer(){return pointer;}
	
	private int[] target;
	public void setTarget(int[] coordinate){
		float vx, vy;
		coordinate[0] = Math.max(0, Math.min(boundary[0], coordinate[0]));
		coordinate[1] = Math.max(0, Math.min(boundary[1], coordinate[1]));
		
		// V = b * S^0.5
		vx = (float) (Math.pow(speed,power) * Math.pow((coordinate[0]- target[0]>=0?1:-1),power-1) * Math.pow(coordinate[0]- target[0],power));
		vy = (float) (Math.pow(speed,power) * Math.pow((coordinate[1]- target[1]>=0?1:-1),power-1) * Math.pow(coordinate[1]- target[1],power));
		
		pointer.addVelocity(vx,vy);
		
		target[0] = coordinate[0];
		target[1] = coordinate[1];
	}
	public void setTarget(float[] coordinatePercentile){
		int[] coordinate = new int[2];
		coordinate[0] = (int) (boundary[0]*coordinatePercentile[0]);
		coordinate[1] = (int) (boundary[1]*coordinatePercentile[1]);
		
		setTarget(coordinate);
	}
	
	public void proceed(int time){
		//calculate deceleration
		int x,y;
		float dx,dy, vx, vy;
		synchronized(pointer){
			x = pointer.getCoordinate()[0];
			y = pointer.getCoordinate()[1];
			vx = pointer.getVelocity()[0];
			vy = pointer.getVelocity()[1];
		}
		
		// A = -b * V^0.5
		dx = (float) (-Math.pow(speed,power) * Math.pow((vx>=0?1:-1),power-1) * Math.pow(vx, power));
		dy = (float) (-Math.pow(speed,power) * Math.pow((vy>=0?1:-1),power-1) * Math.pow(vy, power));
		
		//set new position
		//TODO fixed deceleration component
		x = x+ (int) Math.round(vx*time/1000);
		y = y + (int) Math.round(vy*time/1000);
		
		pointer.setCoordinate(x,y);
		
		//set new velocity
		pointer.setVelocity(vx + dx*time/1000, vy + dy*time/1000);
	}
}
