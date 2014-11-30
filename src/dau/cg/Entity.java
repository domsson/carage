package dau.cg;

public class Entity {
	
	protected float[] position  = {0f, 0f, 0f};
	protected float[] direction = {1f, 0f, 0f};
	protected float[] velocity  = {0f, 0f, 0f}; 
	
	protected Mesh mesh = null;
	
	protected int[] defaultColor = {1, 1, 1};
	
	public Entity() {
		
	}
	
	public Entity(Mesh mesh) {
		this.mesh = mesh;
	}
	
	public Entity(float[] position, float[] direction, float[] velocity) {
		
	}
	
	public void setPosition(float[] position) {
		this.position = position;
	}
	
	public void setPosition(float x, float y, float z) {
		this.position = new float[] {x, y, z};
	}
	
	public float[] getPosition() {
		return position;
	}
	
	public float[] getDirection() {
		return direction;
	}
	
	public float[] getVelocity() {
		return velocity;
	}
	
	public double getSpeed() {
		return Math.sqrt(velocity[0]*velocity[0] + velocity[1]*velocity[1] + velocity[2]*velocity[2]);
	}
		
	public void draw() {
		// TODO take diection into account!
		// Or should that be done in the main class?!
		// In other words: do the "glRotatef" here
		// or let that be done by main class?
		// I'd say main class... BUT there is one prob:
		// the car is made up of several entities and
		// when the car is being drawn, it calls the
		// car parts draw methods. and the wheels, for example,
		// need to be rotated individually, so the CAR has
		// to take care of that, not the main class. therefore,
		// it would be better if the entities took care of
		// rotating themselves, no?
		// but that might be in conflict with the transformations
		// done within the main class... oh boy!
		mesh.draw();
	}
	
	public void draw(int textureId) {
		mesh.draw(textureId);
	}
	
	public Mesh getMesh() {
		return mesh;
	}

}
