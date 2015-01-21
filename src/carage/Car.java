package carage;

import org.lwjgl.util.vector.Vector3f;

import carage.engine.AssetGroup;

// http://engineeringdotnet.blogspot.co.uk/2010/04/simple-2d-car-physics-in-games.html
// http://de.wikipedia.org/wiki/Fahrwiderstand
// http://www.asawicki.info/Mirror/Car%20Physics%20for%20Games/Car%20Physics%20for%20Games.html

public class Car extends AssetGroup {
	
	private String chassisResource;
	private String wheelResource;
	
	private AssetConfig config = null;
	
	// Chassis
	private CarChassis chassis = null;
	// Wheels
	private CarWheel leftFrontWheel  = null;
	private CarWheel rightFrontWheel = null;
	private CarWheel leftRearWheel   = null;
	private CarWheel rightRearWheel  = null;
	
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
	
	public Car(String chassisResource, String wheelResource) {
		this.chassisResource = chassisResource;
		this.wheelResource = wheelResource;
		loadConfig();
		initParts();
		initChassisPosition();
		initWheelPositions();
	}
	
	private void loadConfig() {
		config = new AssetConfig(chassisResource);
		
		weight = config.getFloatProperty("weight");
		acceleration = config.getFloatProperty("acceleration");
		frontAxleOffset = config.getFloatProperty("front-axle-offset");
		rearAxleOffset = config.getFloatProperty("rear-axle-offset");
		frontWheelClearance = config.getFloatProperty("front-wheel-track") * 0.5f;
		rearWheelClearance = config.getFloatProperty("rear-wheel-track") * 0.5f;
	}
	
	// TODO have a Car(CarProperties props) {} constructor which initializes a car from a config gile / config object
	
	public Car(float frontAxleOffset, float rearAxleOffset, float frontWheelTrack, float rearWheelTrack) {
		this.chassisResource = "vw-polo"; // TODO !!!
		this.wheelResource = "vw-polo-wheel"; // TODO !!!
		
		this.frontAxleOffset = frontAxleOffset;
		this.rearAxleOffset  = rearAxleOffset;
		this.frontWheelClearance = frontWheelTrack * 0.5f;
		this.rearWheelClearance  = rearWheelTrack * 0.5f;
		
		initParts();
		initWheelPositions();
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
		
		initWheelPositions();
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
	/*
	public int getDrivingDirection() {
		float scalar = (direction[0]*velocity[0] + direction[1]*velocity[1] + direction[2]*velocity[2]);
		if (scalar == 0) {
			return 0;
		}
		return (scalar > 0) ? 1 : -1;
	}
	*/
	
	/**
	 * Set the chassis' texture to the texture with the given id
	 * 
	 * @param chassisTextureId The id of the texture to use for the car's chassis
	 *
	public void setChassisTexture(int chassisTextureId) {
		this.chassisTextureId = chassisTextureId;
	}
	
	/**
	 * Set the texture to use for the wheels to the texture with the given id
	 * 
	 * @param wheelTextureId The id of the texture to use for all four wheels
	 */
	public void setWheelTexture(String wheelTexture) {
		leftFrontWheel.loadTexture(wheelTexture);
		rightFrontWheel.loadTexture(wheelTexture);
		leftRearWheel.loadTexture(wheelTexture);
		rightRearWheel.loadTexture(wheelTexture);
	}
	
	/**
	 * Print some information about the car's geometry to the console
	 */
	public void printInfo() {
		//System.out.println("Verts: " + (chassis.getMesh().getNumberOfVertices() + (leftFrontWheel.getMesh().getNumberOfVertices() * 4)));
		//System.out.println("UVs  : " + (chassis.getMesh().getNumberOfUVs() + (leftFrontWheel.getMesh().getNumberOfUVs() * 4)));
		//System.out.println("Tris : " + (chassis.getMesh().getNumberOfFaces() + (leftFrontWheel.getMesh().getNumberOfFaces() * 4)));
		//System.out.println();
		
		System.out.println("[Car Info]");
		System.out.println("Size       : " + (chassis.getBoundingBox().getWidth() + " x " + chassis.getBoundingBox().getLength() + " x " + chassis.getBoundingBox().getHeight()) + " m");
		System.out.println("Wheelbase  : " + getWheelbase() + " m");
		System.out.println("Front track: " + getFrontTrack() + " m");
		System.out.println("Rear track : " + getFrontTrack() + " m");
		System.out.println("Axle height: " + leftFrontWheel.getRadius() + " m");
		System.out.println("Wheel size : " + (leftFrontWheel.getBoundingBox().getWidth() + " x " + leftFrontWheel.getBoundingBox().getLength() + " x " + leftFrontWheel.getBoundingBox().getHeight()) + " m");
		
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
	private void initParts() {
		// TODO The Car's (or chassis'?) Configuration file (TODO!) should tell where the front is.
		
		// Chassis
		chassis = new CarChassis(chassisResource);
		// Wheels
		leftFrontWheel = new CarWheel(wheelResource);
		rightFrontWheel = new CarWheel(wheelResource);
		rightFrontWheel.invert();
		leftRearWheel = new CarWheel(wheelResource);
		rightRearWheel = new CarWheel(wheelResource);
		rightRearWheel.invert();
		
		addAsset(chassis, "chassis");
		addAsset(leftFrontWheel, "left-front-wheel");
		addAsset(rightFrontWheel, "right-front-wheel");
		addAsset(leftRearWheel, "left-rear-wheel");
		addAsset(rightRearWheel, "right-rear-wheel");
	}
	
	private void initChassisPosition() {
//		System.out.println("Axle height:" + axleHeight);
		chassis.setPosition(new Vector3f(0, leftFrontWheel.getRadius(), 0));
	}
	
	/**
	 * Set the all four wheels' positions relative to the chassis
	 * using the axle offsets and wheel clearance values
	 */
	private void initWheelPositions() {
		// TODO Well, this should take the car's direction (where is the front?) into account! (A "problem" we have everywhere, really)
		
		leftFrontWheel.setPosition(-this.frontAxleOffset, leftFrontWheel.getRadius(), this.frontWheelClearance);		
		rightFrontWheel.setPosition(-this.frontAxleOffset, rightFrontWheel.getRadius(), -this.frontWheelClearance);
		leftRearWheel.setPosition(this.rearAxleOffset, leftRearWheel.getRadius(), this.rearWheelClearance);
		rightRearWheel.setPosition(this.rearAxleOffset, rightRearWheel.getRadius(), -this.rearWheelClearance);
	}

}
