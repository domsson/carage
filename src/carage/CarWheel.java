package carage;

import carage.engine.Asset;

public class CarWheel extends Asset {
	
	// spin when driving!
	private int rot = 0;
	private float radius;
	private double circumfence;
	// here, angle should be easier then the direction vector, no?
	private int angle = 0;
	
	private boolean invert = false;
	
	// How far to the left/right can the wheel turn?
	// TODO should come from the wheel's or car's config, right?
	private int maxSteeringAngle = 40;
	
	public CarWheel(Mesh mesh) {
		this.mesh = mesh;
		this.radius = (getMesh().getHeight()/2);
		this.circumfence = Math.PI * getMesh().getHeight();
	}
	
	public void invert() {
		invert = !invert;
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
		rot += amount;
		rot = (rot > 360) ? rot-360 : rot;
		rot = (rot <   0) ? 360-rot : rot;
	}
	
	public void spin(float distance) {
		// TODO rotate exactly as much as needed to "drive" distance meters
	}
	
}
