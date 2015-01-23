package carage.engine;

import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

public class Entity {
	
	public static final Vector3f DEFAULT_POSITION = new Vector3f(0f, 0f, 0f);
	public static final Vector3f DEFAULT_DIRECTION = new Vector3f(-1f, 0f, 0f);
	public static final Vector3f DEFAULT_ROTATION = new Vector3f(0f, 0f, 0f);
	public static final Vector3f DEFAULT_VELOCITY = new Vector3f(0f, 0f, 0f);
	
	// Important to _initialize_ them here already, otherwise the setters will raise a NullPointerException
	protected Vector3f position = new Vector3f(0f, 0f, 0f);
	protected Vector3f direction = new Vector3f(0f, 0f, 0f); // TODO drop this in favor of rotation? Otherwise make sure both are consistent!!!
	protected Vector3f rotation = new Vector3f(0f, 0f, 0f);
	protected Vector3f velocity = new Vector3f(0f, 0f, 0f);
	
	public Entity() {
		setPosition(DEFAULT_POSITION);
		setDirection(DEFAULT_DIRECTION);
		setRotation(DEFAULT_ROTATION);
		setVelocity(DEFAULT_VELOCITY);
	}
	
	public Entity(Vector3f position, Vector3f direction, Vector3f velocity) {
		setPosition(position);
		setDirection(direction);
		setRotation(DEFAULT_ROTATION);// TODO
		setVelocity(velocity);
	}
	
	/**
	 * Set this Entitie's position.
	 * @param position A 3-dimensional vector representing the position
	 */
	public void setPosition(Vector3f position) {
		// Make sure to pass by value, not by reference!
		this.position.x = position.getX();
		this.position.y = position.getY();
		this.position.z = position.getZ();
	}
	
	/**
	 * Set this Entitie's position.
	 * @param x The position's x component
	 * @param y The position's y component
	 * @param z The position's z component
	 */
	public void setPosition(float x, float y, float z) {
		this.position.x = x;
		this.position.y = y;
		this.position.z = z;
	}
	
	/**
	 * Set this Entitie's position.
	 * @param xyz A float array holding the position's x, y and z components
	 */
	public void setPosition(float[] xyz) {
		if (xyz.length < 3) { return; } // TODO throw exception or something?
		this.position.x = xyz[0];
		this.position.y = xyz[1];
		this.position.z = xyz[2];
	}
	
	/**
	 * Set this Entitie's position in x direction.
	 * @param x The entitie's new x position
	 */
	public void setPositionX(float x) {
		this.position.x = x;
	}
	
	/**
	 * Set this Entitie's position in y direction.
	 * @param y The entitie's new y position
	 */
	public void setPositionY(float y) {
		this.position.y = y;
	}
	
	/**
	 * Set this Entitie's position in z direction.
	 * @param z The entitie's new z position
	 */
	public void setPositionZ(float z) {
		this.position.z = z;
	}
	
	/**
	 * Change this Entitie's position by the given amounts.
	 * @param position a 3-dimensional vector holding the changes for the x, y and z position
	 */
	public void alterPosition(Vector3f position) {
		this.position.x += position.getX();
		this.position.y += position.getY();
		this.position.z += position.getZ();
	}
	
	/**
	 * Change this Entitie's position in x direction
	 * @param x The amount that should be added to the x position
	 */
	public void alterPositionX(float x) {
		this.position.x += x;
	}
	
	/**
	 * Change this Entitie's position in y direction
	 * @param y The amount that should be added to the y position
	 */
	public void alterPositionY(float y) {
		this.position.y += y;
	}
	
	/**
	 * Change this Entitie's position in z direction
	 * @param z The amount that should be added to the z position
	 */
	public void alterPositionZ(float z) {
		this.position.z += z;
	}
	
	/**
	 * Set this Entitie's direction. The values will be normalized.
	 * @param direction A 3-dimensional vector representing the direction
	 */
	public void setDirection(Vector3f direction) {
		this.direction = (Vector3f) direction.normalise(); // TODO passed by reference? also, maybe drop direction entirely...
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
		// Make sure to pass by value, not by reference!
		this.rotation.x = rotation.getX();
		this.rotation.y = rotation.getY();
		this.rotation.z = rotation.getZ();
	}
	
	public void alterRotation(Vector3f rotation) {
		this.rotation.x += rotation.getX();
		this.rotation.y += rotation.getY();
		this.rotation.z += rotation.getZ();
	}
	
	/**
	 * Set this Entitie's velocity.
	 * @param velocity A 3-dimensional vector representing the velocity
	 */
	public void setVelocity(Vector3f velocity) {
		// Make sure to pass by value, not by reference!
		this.velocity.x = velocity.getX();
		this.velocity.y = velocity.getY();
		this.velocity.z = velocity.getZ();
	}
	
	/**
	 * Set this Entitie's velocity.
	 * @param x The velocitie's x component
	 * @param y The velocitie's y component
	 * @param z The velocitie's z component
	 */
	public void setVelocity(float x, float y, float z) {
		this.velocity.x = x;
		this.velocity.y = y;
		this.velocity.z = z;
	}
	
	/**
	 * Set this Entitie's velocity.
	 * @param xyz A float array holding the velocitie's x, y and z components
	 */
	public void setVelocity(float[] xyz) {
		if (xyz.length < 3) { return; } // TODO throw exception or something?
		this.velocity.x = xyz[0];
		this.velocity.y = xyz[1];
		this.velocity.z = xyz[2];
	}
	
	/**
	 * Get this Entitie's position as 3-dimensional vector.
	 * @return A 3-dimensional vector representing this entitie's position
	 */
	public Vector3f getPosition() {
		return new Vector3f(position.getX(), position.getY(), position.getZ());
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
		return new Vector3f(direction.getX(), direction.getY(), direction.getZ());
	}
	
	/**
	 * Get this Entitie's velocity as 3-dimensional vector.
	 * @return A 3-dimensional vector representing this entitie's velocity
	 */
	public Vector3f getVelocity() {
		return new Vector3f(velocity.getX(), velocity.getY(), velocity.getZ());
	}
	
	/**
	 * Get this Entitie's rotation (in degree) as 3-dimensional vector.
	 * @return A 3-dimensional vector representing this entitie's rotation in degrees
	 */
	public Vector3f getRotation() {
		// TODO  (see related methods)
		// return new Vector3f(getRotationX(), getRotationY(), getRotationZ());
		return new Vector3f(rotation.getX(), rotation.getY(), rotation.getZ());
	}
	
	/**
	 * Get this Entitie's rotation (in degree) around the x-axis.
	 * @return A float value representing this Entitie's rotation around the x-axis in degrees
	 */
	public float getRotationX() {
		// TODO use atan2 and toDegrees
		// return (float) Math.toDegrees(Math.atan2(direction.getY(), direction.getZ()));
		return rotation.getX();
	}
	
	/**
	 * Get this Entitie's rotation (in degree) around the y-axis.
	 * @return A float value representing this Entitie's rotation around the y-axis in degrees
	 */
	public float getRotationY() {
		// TODO use atan2 and toDegrees
		return rotation.getY();
	}
	
	/**
	 * Get this Entitie's rotation (in degree) around the z-axis.
	 * @return A float value representing this Entitie's rotation around the z-axis in degrees
	 */
	public float getRotationZ() {
		// TODO use atan2 and toDegrees
		return rotation.getZ();
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
	public Matrix4f getTransformationMatrix() {		
		Matrix4f transformationMatrix = new Matrix4f();
		applyTransformationsToMatrix(transformationMatrix);
		return transformationMatrix;
	}
	
	public void applyTransformationsToMatrix(Matrix4f modelMatrix) {
		// http://www.opengl-tutorial.org/beginners-tutorials/tutorial-3-matrices/
		// What we want: SCALE, ROTATE, TRANS
		// What we do  : TRANS, ROTATE, SCALE
		
		// TODO is this all we need? is it correct?
		modelMatrix.translate(position);
		modelMatrix.rotate(rotation.getX(), new Vector3f(1, 0, 0));
		modelMatrix.rotate(rotation.getY(), new Vector3f(0, 1, 0));
		modelMatrix.rotate(rotation.getZ(), new Vector3f(0, 0, 1));
		modelMatrix.scale(new Vector3f(1, 1, 1));	// TODO obviously...
	}

}
