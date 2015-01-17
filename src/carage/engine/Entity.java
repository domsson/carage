package carage.engine;

import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

public class Entity {
	
	public static final Vector3f DEFAULT_POSITION = new Vector3f(0f, 0f, 0f);
	public static final Vector3f DEFAULT_DIRECTION = new Vector3f(-1f, 0f, 0f);
	public static final Vector3f DEFAULT_ROTATION = new Vector3f(0f, 0f, 0f);
	public static final Vector3f DEFAULT_VELOCITY = new Vector3f(0f, 0f, 0f);
	
	protected Vector3f position;
	protected Vector3f direction; // TODO drop this in favor of rotation? Otherwise make sure both are consistent!!!
	protected Vector3f rotation; 
	protected Vector3f velocity;
	
	public Entity() {
		setPosition(DEFAULT_POSITION);
		setDirection(DEFAULT_DIRECTION);
		setRotation(DEFAULT_ROTATION);
		setVelocity(DEFAULT_VELOCITY);
	}
	
	public Entity(Vector3f position, Vector3f direction, Vector3f velocity) {
		setPosition(position);
		setDirection(direction);
		setVelocity(velocity);
	}
	
	/**
	 * Set this Entitie's position.
	 * @param position A 3-dimensional vector representing the position
	 */
	public void setPosition(Vector3f position) {
		this.position = position;
	}
	
	/**
	 * Set this Entitie's position.
	 * @param x The position's x component
	 * @param y The position's y component
	 * @param z The position's z component
	 */
	public void setPosition(float x, float y, float z) {
		this.position = new Vector3f(x, y, z);
	}
	
	/**
	 * Set this Entitie's position
	 * @param xyz A float array holding the position's x, y and z components
	 */
	public void setPosition(float[] xyz) {
		if (xyz.length < 3) { return; } // TODO throw exception or something?
		this.position = new Vector3f(xyz[0], xyz[1], xyz[2]);
	}
	
	/**
	 * Set this Entitie's direction. The values will be normalized.
	 * @param direction A 3-dimensional vector representing the direction
	 */
	public void setDirection(Vector3f direction) {
		this.direction = (Vector3f) direction.normalise();
	}
	
	/**
	 * Set this Entitie's direction. The values will be normalized.
	 * @param x The direction's x component
	 * @param y The direction's y component
	 * @param z The direction's z component
	 */
	public void setDirection(float x, float y, float z) {
		this.direction = (Vector3f) (new Vector3f(x, y, z)).normalise();
	}
	
	/**
	 * Set this Entitie's direction. The values will be normalized.
	 * @param xyz A float array holding the direction's x, y and z components
	 */
	public void setDirection(float[] xyz) {
		if (xyz.length < 3) { return; } // TODO throw exception or something?
		this.direction = (Vector3f) (new Vector3f(xyz[0], xyz[1], xyz[2])).normalise();
	}
	
	// TODO drop direction in favor of rotation or synchronize both?!
	public void setRotation(Vector3f rotation) {
		this.rotation = rotation;
	}
	
	/**
	 * Set this Entitie's velocity.
	 * @param velocity A 3-dimensional vector representing the velocity
	 */
	public void setVelocity(Vector3f velocity) {
		this.velocity = velocity;
	}
	
	/**
	 * Set this Entitie's velocity.
	 * @param x The velocitie's x component
	 * @param y The velocitie's y component
	 * @param z The velocitie's z component
	 */
	public void setVelocity(float x, float y, float z) {
		this.velocity = new Vector3f(x, y, z);
	}
	
	/**
	 * Set this Entitie's velocity.
	 * @param xyz A float array holding the velocitie's x, y and z components
	 */
	public void setVelocity(float[] xyz) {
		if (xyz.length < 3) { return; } // TODO throw exception or something?
		this.velocity = new Vector3f(xyz[0], xyz[1], xyz[2]);
	}
	
	/**
	 * Get this Entitie's position as 3-dimensional vector.
	 * @return A 3-dimensional vector representing this entitie's position
	 */
	public Vector3f getPosition() {
		return position;
	}
	
	/**
	 * Get this Entitie's position on the x-axis.
	 * @return A float value representing this Entitie's position on the x-axis 
	 */
	public float getPositionX() {
		return position.getX();
	}
	
	/**
	 * Get this Entitie's position on the y-axis.
	 * @return A float value representing this Entitie's position on the y-axis 
	 */
	public float getPositionY() {
		return position.getY();
	}
	
	/**
	 * Get this Entitie's position on the z-axis.
	 * @return A float value representing this Entitie's position on the z-axis 
	 */
	public float getPositionZ() {
		return position.getZ();
	}
	
	/**
	 * Get this Entitie's direction as normalized 3-dimensional vector.
	 * @return A normalized 3-dimensional vector representing this entitie's direction
	 */
	public Vector3f getDirection() {
		return direction;
	}
	
	/**
	 * Get this Entitie's velocity as 3-dimensional vector.
	 * @return A 3-dimensional vector representing this entitie's velocity
	 */
	public Vector3f getVelocity() {
		return velocity;
	}
	
	/**
	 * Get this Entitie's rotation (in degree) as 3-dimensional vector.
	 * @return A 3-dimensional vector representing this entitie's rotation in degrees
	 */
	public Vector3f getRotation() {
		// TODO  (see related methods)
		// return new Vector3f(getRotationX(), getRotationY(), getRotationZ());
		return rotation;
	}
	
	/**
	 * Get this Entitie's rotation (in degree) around the x-axis.
	 * @return A float value representing this Entitie's rotation around the x-axis in degrees
	 */
	public float getRotationX() {
		// TODO use atan2 and toDegrees
		// return (float) Math.toDegrees(Math.atan2(direction.getY(), direction.getZ()));
		return 0f;
	}
	
	/**
	 * Get this Entitie's rotation (in degree) around the y-axis.
	 * @return A float value representing this Entitie's rotation around the y-axis in degrees
	 */
	public float getRotationY() {
		// TODO use atan2 and toDegrees
		return 0f;
	}
	
	/**
	 * Get this Entitie's rotation (in degree) around the z-axis.
	 * @return A float value representing this Entitie's rotation around the z-axis in degrees
	 */
	public float getRotationZ() {
		// TODO use atan2 and toDegrees
		return 0f;
	}
	
	/**
	 * Returns the Entitie's current speed in meters per second.
	 * @return this entitie's current speed in m/s
	 */
	public float getSpeed() {
		return velocity.length();
	}
	
	/**
	 * 
	 * @return
	 */
	public Matrix4f getModelMatrix() {
		// http://www.opengl-tutorial.org/beginners-tutorials/tutorial-3-matrices/
		// What we want: SCALE, ROTATE, TRANS
		// What we do  : TRANS, ROTATE, SCALE
		
		Matrix4f modelMatrix = new Matrix4f();
		// TODO
		modelMatrix.translate(position);
		modelMatrix.rotate(rotation.getX(), new Vector3f(1, 0, 0));
		modelMatrix.rotate(rotation.getY(), new Vector3f(0, 1, 0));
		modelMatrix.rotate(rotation.getZ(), new Vector3f(0, 0, 1));
		modelMatrix.scale(new Vector3f(1, 1, 1));
		return modelMatrix;
	}

}
