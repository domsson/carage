package carage;

import carage.engine.Asset;

public class CarWheel extends Asset {
	
	// spin when driving!
	private float radius;
	private double circumfence;
	// here, angle should be easier then the direction vector, no?
	private int angle = 0;
	
	private boolean invert = false;
	
	// How far to the left/right can the wheel turn?
	// TODO should come from the wheel's or car's config, right?
	private int maxSteeringAngle = 40;
	
	public CarWheel(String resource) {
		super(resource);
		this.radius = getBoundingBox().getHeight() * 0.5f;
		this.circumfence = Math.PI * getBoundingBox().getHeight();
	}
	
	public void invert() {
		invert = !invert;
	}
	
	public float getRadius() {
		return radius;
	}
	
	public int getMaxAngle() {
		return maxSteeringAngle;
	}
	
	public void setMaxAngle(int maxAngle) {
		// TODO "80" should come from a constant/field, too
		this.maxSteeringAngle = (maxAngle>=-80 && maxAngle<=80) ? maxAngle : this.maxSteeringAngle;
	}
	
	public int getAngle() {
		return angle;
	}
	
	public void setAngle(int angle) {
		this.angle = (angle>=-maxSteeringAngle && angle<=maxSteeringAngle) ? angle : this.angle;
	}
	
	public void spin(int amount) {
		// rotate amount degrees
		rotation.x += amount;
		rotation.x = (rotation.x > 360) ? rotation.x-360 : rotation.x;
		rotation.x = (rotation.x <   0) ? 360-rotation.x : rotation.x;
	}
	
	public void spin(float distance) {
		// TODO rotate exactly as much as needed to "drive" distance meters
	}
	
}
