package carage;

import carage.engine.Asset;


// http://engineeringdotnet.blogspot.co.uk/2010/04/simple-2d-car-physics-in-games.html
// http://de.wikipedia.org/wiki/Fahrwiderstand
// http://www.asawicki.info/Mirror/Car%20Physics%20for%20Games/Car%20Physics%20for%20Games.html

public class Car extends Asset {
	
	// TODO these should be determined on initialization (via constructor)
	public String chassisMeshResource = "vw-polo.obj";
	public String wheelMeshResource   = "vw-polo-wheel.obj";
	
	// Chassis Mesh
	private CarChassis chassis = null;
	// Wheels Mesh
	private CarWheel leftFrontWheel  = null;
	private CarWheel rightFrontWheel = null;
	private CarWheel leftRearWheel   = null;
	private CarWheel rightRearWheel  = null;
	
	// Chassis Texture
	private int chassisTextureId = 0;
	// Wheel Texture
	private int wheelTextureId = 0;
	
	// Wheel offsets (relative positions to chassis' center)
	private float frontAxleOffset = 0.0f;
	private float rearAxleOffset  = 0.0f;
	private float frontWheelClearance = 0.0f;
	private float rearWheelClearance  = 0.0f;
	
	// Has the car been accelerated this tick?
	boolean accelerated = false; 
	boolean decelerated = false;
	
	// TODO some more values that should come from the config file!
	private float weight = 780f;
	private float acceleration = 3.31f;

	public Car() {
		initMeshes();
	}
	
	// TODO have a Car(CarProperties props) {} constructor which initializes a car from a config gile / config object
	
	public Car(float frontAxleOffset, float rearAxleOffset, float frontWheelTrack, float rearWheelTrack) {
		this.frontAxleOffset = frontAxleOffset;
		this.rearAxleOffset  = rearAxleOffset;
		this.frontWheelClearance = frontWheelTrack * 0.5f;
		this.rearWheelClearance  = rearWheelTrack * 0.5f;
		
		initMeshes();
		setWheelPositions();
	}

	/**
	 * Set the wheel's positions as offsets from the car's origin
	 * 
	 * @param frontAxleOffset		The front axis' offset from the car's origin in meters
	 * @param rearAxleOffset		The rear axis' offset from the car's origin in meters
	 * @param frontWheelClearance	The front wheel's distance (left/right) from the car's center 
	 * @param rearWheelClearance	The rear wheel's distance (left/right) from the car's center
	 */
	public void setWheelOffsets(float frontAxleOffset, float rearAxleOffset, float frontWheelClearance, float rearWheelClearance) {
		this.frontAxleOffset = frontAxleOffset;
		this.rearAxleOffset  = rearAxleOffset;
		this.frontWheelClearance = frontWheelClearance;
		this.rearWheelClearance  = rearWheelClearance;
		
		setWheelPositions();
	}
	
	/**
	 * http://en.wikipedia.org/wiki/Wheelbase
	 * 
	 * @return the distance from rear to front axis in meters
	 */
	public float getWheelbase() {
		return (frontAxleOffset + rearAxleOffset);
	}
	
	/**
	 * http://en.wikipedia.org/wiki/Axle_track
	 * 
	 * @return the distance between both front wheels in meters
	 */
	public float getFrontTrack() {
		return (frontWheelClearance * 2);
	}
	
	/**
	 * http://en.wikipedia.org/wiki/Axle_track
	 * 
	 * @return the distance between both rear wheels in meters
	 */
	public float getRearTrack() {
		return (rearWheelClearance * 2);
	}
	
	/**
	 * Indicates if the vehicle is driving forwards, backwards or neither of those.
	 * @return 1 for forward movement, -1 for backwards movement, 0 for else (TODO what does 'else' mean, bitch?)
	 */
	public int getDrivingDirection() {
		float scalar = (direction[0]*velocity[0] + direction[1]*velocity[1] + direction[2]*velocity[2]);
		if (scalar == 0) {
			return 0;
		}
		return (scalar > 0) ? 1 : -1;
	}
	
	public void debugDrivingDirection() {
		System.out.println("Vehicle direction: "+direction[0]+" "+direction[1]+" "+direction[2]);
		System.out.println("Vehicle velocity : "+velocity[0]+" "+velocity[1]+" "+velocity[2]);
	}
	
	public void setChassisMesh(String resource) {
		this.chassisMeshResource = resource;
		
		WavefrontLoader chassisLoader = new WavefrontLoader(chassisMeshResource);
		chassis.setMesh(chassisLoader.getMesh());
	}
	
	public void setWheelMesh(String resource) {
		this.wheelMeshResource = resource;
		
		// Left Front Wheel
		WavefrontLoader leftFrontWheelLoader = new WavefrontLoader(wheelMeshResource);
		leftFrontWheel.setMesh(leftFrontWheelLoader.getMesh());
		// Right Front Wheel
		WavefrontLoader rightFrontWheelLoader = new WavefrontLoader(wheelMeshResource);
		rightFrontWheel.setMesh(rightFrontWheelLoader.getMesh());
		// Left Rear Wheel
		WavefrontLoader leftRearWheelLoader = new WavefrontLoader(wheelMeshResource);
		leftRearWheel.setMesh(leftRearWheelLoader.getMesh());
		// Right Rear Wheel
		WavefrontLoader rightRearWheelLoader = new WavefrontLoader(wheelMeshResource);
		rightRearWheel.setMesh(rightRearWheelLoader.getMesh());
	}
	
	/**
	 * Set the chassis' texture to the texture with the given id
	 * 
	 * @param chassisTextureId The id of the texture to use for the car's chassis
	 */
	public void setChassisTexture(int chassisTextureId) {
		this.chassisTextureId = chassisTextureId;
	}
	
	/**
	 * Set the texture to use for the wheels to the texture with the given id
	 * 
	 * @param wheelTextureId The id of the texture to use for all four wheels
	 */
	public void setWheelTexture(int wheelTextureId) {
		this.wheelTextureId = wheelTextureId;
	}
	
	/**
	 * Draw (render) the car on screen
	 */
	public void draw() {
		chassis.draw(chassisTextureId);
		leftFrontWheel.draw(wheelTextureId);
		rightFrontWheel.draw(wheelTextureId);
		leftRearWheel.draw(wheelTextureId);
		rightRearWheel.draw(wheelTextureId);
	}
	
	/**
	 * Print some information about the car's geometry to the console
	 */
	public void printInfo() {
		System.out.println("Verts: " + (chassis.getMesh().getNumberOfVertices() + (leftFrontWheel.getMesh().getNumberOfVertices() * 4)));
		System.out.println("UVs  : " + (chassis.getMesh().getNumberOfUVs() + (leftFrontWheel.getMesh().getNumberOfUVs() * 4)));
		System.out.println("Tris : " + (chassis.getMesh().getNumberOfFaces() + (leftFrontWheel.getMesh().getNumberOfFaces() * 4)));
		System.out.println();
		System.out.println("Size       : " + (chassis.getMesh().getWidth() + " x " + chassis.getMesh().getLength() + " x " + chassis.getMesh().getHeight()) + " m");
		System.out.println("Wheelbase  : " + getWheelbase() + " m");
		System.out.println("Front track: " + getFrontTrack() + " m");
		System.out.println("Rear track : " + getFrontTrack() + " m");
	}
	
	// TODO this belongs elsewhere... we need to finally use the f0ckin Vector3f class!
	private float[] getNormalizedVelocity(float currentSpeed) {
		if (currentSpeed == 0) {
			return direction;
		}
		else {
			return new float[] { (velocity[0] / currentSpeed), (velocity[1] / currentSpeed), (velocity[2] / currentSpeed) };
		}
	}
	
	/**
	 * Trigger the car's calculations.
	 * This method should be called every tick but not before any other methods that can
	 * modify the car's state, like accelerate(), decelerate(), ...
	 * 
	 * @param delta 
	 */
	public void tick(float delta) {
		if (accelerated) { spinWheels( 2); }
		if (decelerated) { spinWheels(-2); }

		accelerated = false;
		decelerated = false;
	}
	
	/**
	 * Cause the car to accelerate this tick.
	 * This is the equivalent of the driver pushing down the gas pedal.
	 * 
	 * @param delta
	 */
	public void accelerate(float delta) {
		accelerated = true;

	}
	
	/**
	 * Cause the car to decelerate this tick.
	 * This is the equivalent of the driver pushing down the break pedal.
	 * Also, this will cause the car to do 'negative acceleration',
	 * causing the car to move backwards, as if the reverse gear was activated.
	 * 
	 * @param delta
	 */
	public void decelerate(float delta) {
		decelerated = true;

	}
	
	/**
	 * Simulate the driver steering left by turning the front wheels
	 * accordingly and therefore influencing the driving direction
	 * 
	 * @param delta
	 */
	public void steerLeft(float delta) {
		// TODO shouldn't the wheels angle be set according to
		// the car's direction... or something?
		// Or the other way round? In any case, one of those is missing here I guess?
		leftFrontWheel.setAngle(leftFrontWheel.getAngle()+2);
		rightFrontWheel.setAngle(rightFrontWheel.getAngle()+2);
	}
	
	/**
	 * Simulate the driver steering right by turning the front wheels
	 * accordingly and therefore influencing the driving direction
	 * 
	 * @param delta
	 */
	public void steerRight(float delta) {
		leftFrontWheel.setAngle(leftFrontWheel.getAngle()-2);
		rightFrontWheel.setAngle(rightFrontWheel.getAngle()-2);
	}
	
	/**
	 * Spin the wheels by a given angle
	 * @param rot The rotation value (angle in degree)
	 */
	private void spinWheels(int rot) {
		leftFrontWheel.spin(rot);
		rightFrontWheel.spin(rot);
		leftRearWheel.spin(rot);
		rightRearWheel.spin(rot);
	}
	
	/**
	 * Spin the wheels as much as necessary to simulate
	 * that the car drove the given distance in meters
	 * @param distance
	 */
	private void spinWheels(float distance) {
		leftFrontWheel.spin(distance);
		rightFrontWheel.spin(distance);
		leftRearWheel.spin(distance);
		rightRearWheel.spin(distance);
	}
	
	/**
	 * Load/request the meshes that make up this car: chassis and four wheels
	 */
	private void initMeshes() {
		// TODO The Car's (or chassis'?) Configuration file (TODO!) should tell where the front is.
		direction[0] = -1;
		
		// TODO Obviously, it'd be nicer if one WavefrontLoader could be used for all the loading
		// TODO Also, it'd be nicer if once a mesh resource has been loaded, it could be cloned instead of loading it all over
		
		// Chassis
		WavefrontLoader chassisLoader = new WavefrontLoader(chassisMeshResource);
		chassis = new CarChassis(chassisLoader.getMesh());
		// Left Front Wheel
		WavefrontLoader leftFrontWheelLoader = new WavefrontLoader(wheelMeshResource);
		leftFrontWheel = new CarWheel(leftFrontWheelLoader.getMesh());
		// Right Front Wheel
		WavefrontLoader rightFrontWheelLoader = new WavefrontLoader(wheelMeshResource);
		rightFrontWheel = new CarWheel(rightFrontWheelLoader.getMesh());
		rightFrontWheel.invert();
		// Left Rear Wheel
		WavefrontLoader leftRearWheelLoader = new WavefrontLoader(wheelMeshResource);
		leftRearWheel = new CarWheel(leftRearWheelLoader.getMesh());
		// Right Rear Wheel
		WavefrontLoader rightRearWheelLoader = new WavefrontLoader(wheelMeshResource);
		rightRearWheel = new CarWheel(rightRearWheelLoader.getMesh());
		rightRearWheel.invert();
	}
	
	/**
	 * Set the all four wheels' positions relative to the chassis
	 * using the axle offsets and wheel clearance values
	 */
	private void setWheelPositions() {
		// TODO Well, this should take the car's direction (where is the front?) into account! (A "problem" we have everywhere, really)
		
		leftFrontWheel.setPosition(-this.frontAxleOffset, 0, this.frontWheelClearance);		
		rightFrontWheel.setPosition(-this.frontAxleOffset, 0, -this.frontWheelClearance);
		leftRearWheel.setPosition(this.rearAxleOffset, 0, this.rearWheelClearance);
		rightRearWheel.setPosition(this.rearAxleOffset, 0, -this.rearWheelClearance);
	}

}
