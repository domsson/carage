package carage;

import carage.engine.Asset;

public class CarWheel extends Asset {
	
	public static final float TWO_PI = (float) (2 * Math.PI);
	
	// spin when driving!
	private float radius;
	private double circumfence;
	
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
	
	public float getAngle() {
		return rotation.getY();
	}
	
	public void setAngle(int angleDegrees) {
		setAngle((float) Math.toRadians(angleDegrees));
	}
	
	public void setAngle(float angleRadians) {
		float maxSteeringAngleRadians = (float) Math.toRadians(maxSteeringAngle);
		rotation.y = (angleRadians>=-maxSteeringAngleRadians && angleRadians<=maxSteeringAngleRadians) ? angleRadians : rotation.y;
	}
	
	public void spin(int amountDegrees) {
		// rotate amount degrees
		float amount = (float) Math.toRadians(amountDegrees);
		rotation.z += amount;
		rotation.z = (rotation.z > TWO_PI) ? rotation.z-TWO_PI : rotation.z;
		rotation.z = (rotation.z <      0) ? TWO_PI-rotation.z : rotation.z;
	}
	
	public void spin(float distance) {
		// TODO rotate exactly as much as needed to "drive" distance meters
	}
		
}
