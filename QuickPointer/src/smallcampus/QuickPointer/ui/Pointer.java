package smallcampus.QuickPointer.ui;

public class Pointer {
	private int[] coordinate = new int[2];
	private float[] velocity = new float[2];
	
	public Pointer(int x, int y){
		this();
		coordinate[0] = x;
		coordinate[1] = y;
	}
	public Pointer(){
		reset();
	}
	
	public int[] getCoordinate(){ return coordinate; }
	public void setCoordinate(int x, int y){
		coordinate[0] = x;
		coordinate[1] = y;
	}
	
	public float[] getVelocity(){ return velocity;}
	public void setVelocity(float vx, float vy){
		velocity[0] = vx;
		velocity[1] = vy;
	}
	
	public void addVelocity(float vx, float vy){
		velocity[0] += vx;
		velocity[1] += vy;
	}
	
	public void reset(){
		//set starting position
		coordinate[0] = 0;
		coordinate[1] = 0;
		
		//set starting velocity
		velocity[0] = 0;
		velocity[1] = 0;
	}
}
