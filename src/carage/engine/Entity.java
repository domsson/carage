package carage.engine;

import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

public class Entity {
	
	public static final Vector3f DEFAULT_POSITION = new Vector3f(0f, 0f, 0f);
	public static final Vector3f DEFAULT_ROTATION = new Vector3f(0f, 0f, 0f);
	public static final Vector3f DEFAULT_SCALE    = new Vector3f(1f, 1f, 1f);
	public static final Vector3f DEFAULT_VELOCITY = new Vector3f(0f, 0f, 0f);
	
	// Important to _initialize_ them here already, otherwise the setters will raise a NullPointerException
	protected Vector3f position = new Vector3f(0f, 0f, 0f);
	protected Vector3f rotation = new Vector3f(0f, 0f, 0f);	// TODO is this in degrees or radians? I guess radians? Or leave that to the 'user'?
	protected Vector3f scale    = new Vector3f(0f, 0f, 0f);
	protected Vector3f velocity = new Vector3f(0f, 0f, 0f);
	
	/**
	 * Create a new Entity with default position, rotation, scale and velocity.
	 */
	public Entity() {
		this(DEFAULT_POSITION, DEFAULT_ROTATION, DEFAULT_SCALE, DEFAULT_VELOCITY);
	}
	
	/**
	 * Create a new Entity with the given position.
	 * Rotation, scale and velocity will be initialized with their respective defaults.
	 * @param position The entitie's initial position as 3-dimensional vector
	 */
	public Entity(Vector3f position) {
		this(position, DEFAULT_ROTATION, DEFAULT_SCALE, DEFAULT_VELOCITY);
	}
	
	/**
	 * Create a new Entity with the given position and rotation.
	 * Scale and velocity will be initialized with their respective defaults.
	 * @param position The entitie's initial position as 3-dimensional vector
	 * @param rotation The entitie's initial rotation as 3-dimensional vector
	 */
	public Entity(Vector3f position, Vector3f rotation) {
		this(position, rotation, DEFAULT_SCALE, DEFAULT_VELOCITY);
	}
	
	/**
	 * Create a new Entity with the given position, rotation and scale.
	 * The entitie's velocity will be initialized with its default value.
	 * @param position The entitie's initial position as 3-dimensional vector
	 * @param rotation The entitie's initial rotation as 3-dimensional vector
	 * @param scale    The entitie's initial scale
	 */
	public Entity(Vector3f position, Vector3f rotation, Vector3f scale) {
		this(position, rotation, scale, DEFAULT_VELOCITY);
	}
	
	/**
	 * Create a new Entity and set its initial position, rotation, scale and velocity.
	 * @param position The entitie's initial position as 3-dimensional vector
	 * @param rotation The entitie's initial rotation as 3-dimensional vector
	 * @param scale    The entitie's initial scale
	 * @param velocity The entitie's initial velocity
	 */
	public Entity(Vector3f position, Vector3f rotation, Vector3f scale, Vector3f velocity) {
		setPosition(position);
		setRotation(rotation);
		setScale(scale);
		setVelocity(velocity);
	}
		
	/**
	 * Set this Entitie's position.
	 * This method will be called by all other setPosition(*) convenience methods.
	 * Hence, this one is the one to override if a different behavior for setting the position is required.
	 * @param x The position's x component
	 * @param y The position's y component
	 * @param z The position's z component
	 */
	public void setPosition(float x, float y, float z) {
		position.x = x;
		position.y = y;
		position.z = z;
	}
	
	/**
	 * Set this Entitie's position.
	 * @param xyz A float array holding the position's x, y and z components
	 */
	public void setPosition(float[] xyz) {
		if (xyz.length < 3) { return; } // TODO throw exception or something?
		setPosition(xyz[0], xyz[1], xyz[2]);
	}
	
	/**
	 * Set this Entitie's position.
	 * @param position A 3-dimensional vector representing the position
	 */
	public void setPosition(Vector3f position) {
		setPosition(position.getX(), position.getY(), position.getZ());
	}
	
	/**
	 * Set this Entitie's position in x direction.
	 * @param x The entitie's new x position
	 */
	public void setPositionX(float x) {
		setPosition(x, position.y, position.z);
	}
	
	/**
	 * Set this Entitie's position in y direction.
	 * @param y The entitie's new y position
	 */
	public void setPositionY(float y) {
		setPosition(position.x, y, position.z);
	}
	
	/**
	 * Set this Entitie's position in z direction.
	 * @param z The entitie's new z position
	 */
	public void setPositionZ(float z) {
		setPosition(position.x, position.y, z);
	}
	
	/**
	 * Change this Entitie's position by the given amounts.
	 * This method will be called by all other alterPosition(*) convenience methods.
	 * Hence, this one is the one to override if a different behavior for altering the position is required.
	 * @param x The amount that should be added to the x position
	 * @param y The amount that should be added to the y position
	 * @param z The amount that should be added to the z position
	 */
	public void alterPosition(float x, float y, float z) {
		position.x += x;
		position.y += y;
		position.z += z;
	}
	
	/**
	 * Change this Entitie's position by the given amounts.
	 * @param xyz A float array holding the values to be added to the x, y and z positions
	 */
	public void alterPosition(float[] xyz) {
		if (xyz.length < 3) { return; } // TODO throw exception or something?
		alterPosition(xyz[0], xyz[1], xyz[2]);
	}
	
	/**
	 * Change this Entitie's position by the given amounts.
	 * @param position a 3-dimensional vector holding the changes for the x, y and z position
	 */
	public void alterPosition(Vector3f position) {
		alterPosition(position.getX(), position.getY(), position.getZ());
	}
	
	/**
	 * Change this Entitie's position in x direction
	 * @param x The amount that should be added to the x position
	 */
	public void alterPositionX(float x) {
		alterPosition(x, 0f, 0f);
	}
	
	/**
	 * Change this Entitie's position in y direction
	 * @param y The amount that should be added to the y position
	 */
	public void alterPositionY(float y) {
		alterPosition(0f, y, 0f);
	}
	
	/**
	 * Change this Entitie's position in z direction
	 * @param z The amount that should be added to the z position
	 */
	public void alterPositionZ(float z) {
		alterPosition(0f, 0f, z);
	}
	
	/**
	 * Set this Entitie's rotation.
	 * @param x The entitie's new rotation around the x axis
	 * @param y The entitie's new rotation around the y axis
	 * @param z The entitie's new rotation around the z axis
	 */
	public void setRotation(float x, float y, float z) {
		rotation.x = x;
		rotation.y = y;
		rotation.z = z;
	}
	
	/**
	 * Set this Entitie's rotation.
	 * @param xyz A float array with the entitie's new rotations around x, y and z axis, respectively
	 */
	public void setRotation(float[] xyz) {
		if (xyz.length < 3) { return; } // TODO throw exception or something?
		setRotation(xyz[0], xyz[1], xyz[2]);
	}
	
	/**
	 * Set this Entitie's rotation.
	 * @param rotation A 3-dimensional vector with this entitie's new rotations around x, y and z axis, respectively
	 */
	public void setRotation(Vector3f rotation) {
		// Make sure to pass by value, not by reference!
		setRotation(rotation.getX(), rotation.getY(), rotation.getZ());
	}
	
	/**
	 * Change TODO (description :)
	 * @param x
	 * @param y
	 * @param z
	 */
	public void alterRotation(float x, float y, float z) {
		rotation.x += x;
		rotation.y += y;
		rotation.z += z;
	}
	
	public void alterRotation(float[] xyz) {
		if (xyz.length < 3) { return; } // TODO throw exception or something?
		alterRotation(xyz[0], xyz[1], xyz[2]);
	}
	
	public void alterRotation(Vector3f rotation) {
		alterRotation(rotation.getX(), rotation.getY(), rotation.getZ());
	}
	
	public void setScale(float x, float y, float z) {
		scale.x = x;
		scale.y = y;
		scale.z = z;
	}
	
	public void setScale(float[] xyz) {
		if (xyz.length < 3) { return; } // TODO exception or something
		setScale(xyz[0], xyz[1], xyz[2]);
	}
	
	public void setScale(Vector3f scale) {
		setScale(scale.getX(), scale.getY(), scale.getZ());
	}
	
	public void alterScale(float x, float y, float z) {
		scale.x += x;
		scale.y += y;
		scale.z += z;
	}
	
	public void alterScale(float[] xyz) {
		if (xyz.length < 3) { return; } // TODO exception or something
		alterScale(xyz[0], xyz[1], xyz[2]);
	}
	
	public void alterScale(Vector3f scale) {
		alterScale(scale.getX(), scale.getY(), scale.getZ());
	}
		
	/**
	 * Set this Entitie's velocity.
	 * @param x The velocitie's x component
	 * @param y The velocitie's y component
	 * @param z The velocitie's z component
	 */
	public void setVelocity(float x, float y, float z) {
		velocity.x = x;
		velocity.y = y;
		velocity.z = z;
	}
	
	/**
	 * Set this Entitie's velocity.
	 * @param xyz A float array holding the velocitie's x, y and z components
	 */
	public void setVelocity(float[] xyz) {
		if (xyz.length < 3) { return; } // TODO throw exception or something?
		setVelocity(xyz[0], xyz[1], xyz[2]);
	}
	
	/**
	 * Set this Entitie's velocity.
	 * @param velocity A 3-dimensional vector representing the velocity
	 */
	public void setVelocity(Vector3f velocity) {
		// Make sure to pass by value, not by reference!
		setVelocity(velocity.getX(), velocity.getY(), velocity.getZ());
	}
	
	public void alterVelocity(float x, float y, float z) {
		velocity.x += x;
		velocity.y += y;
		velocity.z += z;
	}
	
	public void alterVelocity(float[] xyz) {
		if (xyz.length < 3) { return; } // TODO exception or something
		alterVelocity(xyz[0], xyz[1], xyz[2]);
	}
	
	public void alterVelocity(Vector3f velocity) {
		alterVelocity(velocity.getX(), velocity.getY(), velocity.getZ());
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
	 *
	public Vector3f getDirection() {
		// TODO calculate normalized direction from the three rotations
	}
	 */
	
	/**
	 * Get this Entitie's rotation (in degree) as 3-dimensional vector.
	 * @return A 3-dimensional vector representing this entitie's rotation in degrees
	 */
	public Vector3f getRotation() {
		return new Vector3f(rotation.getX(), rotation.getY(), rotation.getZ());
	}
	
	/**
	 * Get this Entitie's rotation (in degree) around the x-axis.
	 * @return A float value representing this Entitie's rotation around the x-axis in degrees
	 */
	public float getRotationX() {
		return rotation.getX();
	}
	
	/**
	 * Get this Entitie's rotation (in degree) around the y-axis.
	 * @return A float value representing this Entitie's rotation around the y-axis in degrees
	 */
	public float getRotationY() {
		return rotation.getY();
	}
	
	/**
	 * Get this Entitie's rotation (in degree) around the z-axis.
	 * @return A float value representing this Entitie's rotation around the z-axis in degrees
	 */
	public float getRotationZ() {
		return rotation.getZ();
	}
	
	public Vector3f getScale() {
		return new Vector3f(scale.getX(), scale.getY(), scale.getZ());
	}
	
	public float getScaleX() {
		return scale.getX();
	}
	
	public float getScaleY() {
		return scale.getY();
	}
	
	public float getScaleZ() {
		return scale.getZ();
	}
	
	/**
	 * Get this Entitie's velocity as 3-dimensional vector.
	 * @return A 3-dimensional vector representing this entitie's velocity
	 */
	public Vector3f getVelocity() {
		return new Vector3f(velocity.getX(), velocity.getY(), velocity.getZ());
	}
	
	public float getVelocityX() {
		return velocity.getX();
	}
	
	public float getVelocityY() {
		return velocity.getY();
	}
	
	public float getVelocityZ() {
		return velocity.getZ();
	}
	
	/**
	 * Returns the Entitie's current speed in meters per second.
	 * @return this entitie's current speed in m/s
	 */
	public float getSpeed() {
		return velocity.length();
	}
	
	/**
	 * Creates a transformation matrix according to this Entitie's position, rotation and scale.
	 * @return A Matrix that describes this model's transformations
	 */
	public Matrix4f getTransformationMatrix() {		
		Matrix4f transformationMatrix = new Matrix4f();
		applyTransformationsToMatrix(transformationMatrix);
		return transformationMatrix;
	}
	
	/**
	 * Applies this model's transformations to the given matrix. The supplied matrix will not be reset.
	 * The effect will be the same as multiplying the given matrix with this entitie's transformation matrix.
	 * @param modelMatrix Any 4x4 Matrix
	 */
	public void applyTransformationsToMatrix(Matrix4f modelMatrix) {
		// http://www.opengl-tutorial.org/beginners-tutorials/tutorial-3-matrices/
		// What we want: SCALE, ROTATE, TRANS
		// What we do  : TRANS, ROTATE, SCALE
				
		// http://www.opengl-tutorial.org/intermediate-tutorials/tutorial-17-quaternions/
		// The order of rotation is important, YZX gives the desired result in *most* cases, not all!
		// It seems to do the trick for our purposes. For now, at least. Let's roll with it until we run into problems.
		
		modelMatrix.translate(position);		
		modelMatrix.rotate(rotation.getY(), new Vector3f(0, 1, 0));
		modelMatrix.rotate(rotation.getZ(), new Vector3f(0, 0, 1));
		modelMatrix.rotate(rotation.getX(), new Vector3f(1, 0, 0));
		modelMatrix.scale(scale);
	}

}
