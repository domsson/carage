package carage;

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
	
	public void setMesh(Mesh mesh) {
		this.mesh = mesh;
	}
	
	public void setPosition(float[] position) {
		this.position = position;
	}
	
	public void setPosition(float x, float y, float z) {
		this.position = new float[] {x, y, z};
	}
	
	public void setDirection(float[] direction) {
		this.direction = direction;
	}
	
	public void setDirection(float x, float y, float z) {
		this.direction = new float[] {x, y, z};
	}
	
	public float[] getPosition() {
		return position;
	}
	
	public float getPositionX() {
		return position[0];
	}
	
	public float getPositionY() {
		return position[1];
	}
	
	public float getPositionZ() {
		return position[2];
	}
	
	public float[] getDirection() {
		return direction;
	}
	
	public float[] getVelocity() {
		return velocity;
	}
	
	public float[] getRotation() {
		// TODO verify if this is correct (see related methods)
		return new float[] {getRotationX(), getRotationY(), getRotationZ()};
	}
	
	public float getRotationX() {
		// TODO verify if this is correct (IT IS NOT)
		// rotation around x axis -> z and y axis relevant, right?
		return (float) Math.toDegrees(Math.atan2(direction[1], direction[2]));
	}
	
	public float getRotationY() {
		// TODO verify if this is correct (IT IS NOT)
		// rotation around y axis -> z and x axis relevant, right?
		return (float) Math.toDegrees(Math.atan2(direction[2], direction[0]));
	}
	
	public float getRotationZ() {
		// TODO verify if this is correct (IT IS NOT)
		// rotation around z axis -> x and y axis relevant, right?
		return (float) Math.toDegrees(Math.atan2(direction[1], direction[0]));
	}
	
	/**
	 * Returns the Entitie's current speed in meters per second
	 * @return this entitie's current speed in m/s
	 */
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
