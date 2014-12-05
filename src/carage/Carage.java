package carage;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL20.*;
import java.nio.FloatBuffer;
import org.lwjgl.BufferUtils;

import org.lwjgl.Sys;
import org.lwjgl.input.Keyboard;

import lenz.opengl.AbstractSimpleBase;
import lenz.opengl.utils.ShaderProgram;
import lenz.opengl.utils.Texture;

public class Carage extends AbstractSimpleBase {
		
	public static final int FPS = 60;
	
	// TODO Abstract this away. Re-mapping keys should be easy.
	// Keys for car movement
	private boolean moveLeft  = false;
	private boolean moveRight = false;
	private boolean moveUp    = false;
	private boolean moveDown  = false;
	private boolean moveFwd   = false;
	private boolean moveBack  = false;

	// TODO Abstract this away. Re-mapping keys should be easy.
	// Keys for camera movement
	private boolean camUp    = false;
	private boolean camRight = false;
	private boolean camDown  = false;
	private boolean camLeft  = false;
	private boolean camIn    = false;
	private boolean camOut   = false;
	
	private boolean modify   = false;
	
	// TODO Create a Cam object? (and use it, too?)
	private Entity camera;
	
	private float[] camPos = new float[] {0.0f, 0.4f, 4f};
	private int[]   camRot = new int[] {30, 0, 0};
		
	// Okay, timing and stuff
	private long lastRender = 0;
	
	// TODO what.... what even is this?
	private float angle = 0.0f;
	private float offset = 0.0f;
	private int rot = 0;
	
	private TextureManager textureManager;
	// TODO private MeshManager meshManager;
	
	private WavefrontLoader cardBoardBoxLoader;
	private Mesh cardBoardBox;
	
	private WavefrontLoader carWorkshopLoader;
	private Mesh carWorkshop;
	
	private Car car;
	
	// TODO this solution is ugly as f0ck, too many fields anyway...
	private String[] wheels = new String[] {"vw-polo-wheel", "generic-wheel" };
	private int curWheels = 0;
	
	private ShaderProgram sp;

	public static void main(String[] args) {
		new Carage().start();
	}

	@Override
	protected void initOpenGL() {
		// Print OpenGL Version Info
		System.out.println("OpenGL Version: "+glGetString(GL_VERSION));
		
		// Perspective and Viewport
		glMatrixMode(GL_PROJECTION);
		glFrustum(-(WIDTH/(float)HEIGHT), (WIDTH/(float)HEIGHT), -1, 1, 1.5, 100);
		// TODO glFrustrum raus, OpenGL will:
		// Matrix4f projection = new Matrix4f();
		
		glMatrixMode(GL_MODELVIEW);
		
		// Shading
		glShadeModel(GL_FLAT);
		glClearColor(0.4f, 0.6f, 1.0f, 1.0f);
		
		// Enable depth test and backface culling
		glEnable(GL_DEPTH_TEST);
		glEnable(GL_CULL_FACE);
		
		loadTextures();
		// Textures
		glEnable(GL_TEXTURE_2D);
		
		// Shaders
		sp = new ShaderProgram("phong");
		// glUseProgram(sp.getId());
		
		initFog();
		initMeshes();
		initCamera();
	}
	
	public void initFog() {
		glEnable(GL_FOG);
		FloatBuffer fogColor = BufferUtils.createFloatBuffer(4);
		fogColor.put(0.4f).put(0.6f).put(1.0f).put(1.0f).flip();
		glFog(GL_FOG_COLOR, fogColor);
		glFogi(GL_FOG_MODE, GL_LINEAR);
		glFogf(GL_FOG_START, 5.0f);
		glFogf(GL_FOG_END, 20.0f);
	}
	
	private void initMeshes() {
		carWorkshopLoader = new WavefrontLoader("workshop.obj");
		carWorkshop = carWorkshopLoader.getMesh();
			
		cardBoardBoxLoader = new WavefrontLoader("cardboardbox.obj");
		cardBoardBox = cardBoardBoxLoader.getMesh();
				
		loadCar();
		car.printInfo();
	}
	
	private void initCamera() {
		camera = new Entity();
		camera.setPosition(0.0f, 0.4f, 4.0f);
		// direction should always be a unit vector... who should take care of it?
		camera.setDirection(0.0f, 0.0f, -1.0f);
	}
	
	private void loadTextures() {
		textureManager = TextureManager.getInstance();
		
		textureManager.load("cg.png");
		//textureManager.load("two-floors.png");
		textureManager.load("generic-wheel.png");
		textureManager.load("vw-polo.png");
		textureManager.load("vw-polo-wheel.png");
		textureManager.load("road.png");
		textureManager.load("cardboardbox.png");
	}
	
	private void loadCar() {
		car = new Car(1.15f, 1.23f, 1.3f, 1.3f);
		car.setChassisTexture(textureManager.getId("vw-polo.png"));
		car.setWheelTexture(textureManager.getId("vw-polo-wheel.png"));
	}
	
	private void modifyCar() {
		// TODO there should be one modifyWheels() and one modifyChassis()!
		if (++curWheels>wheels.length-1) {
			curWheels=0;
		}
		car.setWheelMesh(wheels[curWheels]+".obj");
		car.setWheelTexture(textureManager.getId(wheels[curWheels]+".png"));
	}

	@Override
	protected void render() {
		processInput();
		
		// Cam rotation
		camRot[1] = camLeft  ? camRot[1]+2 : camRot[1];
		camRot[1] = camRight ? camRot[1]-2 : camRot[1];
		
		camRot[1] = (camRot[1] > 360) ? camRot[1]-360 : camRot[1];
		camRot[1] = (camRot[1] <   0) ? 360-camRot[1] : camRot[1];
		
		camRot[0] = camUp   ? camRot[0]+2 : camRot[0];
		camRot[0] = camDown ? camRot[0]-2 : camRot[0];
		
		camRot[0] = (camRot[0] > 360) ? camRot[0]-360 : camRot[0];
		camRot[0] = (camRot[0] <   0) ? 360-camRot[0] : camRot[0];
		
		// Cam position
		if (camIn) {
			camPos[2] -= 0.1;
		}
		if (camOut) {
			camPos[2] += 0.1;
		}
		
		// Car rotation
		rot = moveLeft  ? rot+2 : rot;
		rot = moveRight ? rot-2 : rot;
		
		rot = (rot > 360) ? rot-360 : rot;
		rot = (rot <   0) ? 360-rot : rot;
		
		double rotRad = Math.toRadians(rot);
				
		double xDiff = Math.cos(rotRad+Math.PI) / 10;
		double zDiff = Math.sin(rotRad+Math.PI) / 10;
					
		// TIME SHIZZLE TODO this is basically abandoned code; delta should be used properly!
		float delta = getDelta();
		angle = (angle >= 360) ? 0 : angle + delta;
		offset += 0.05;
		
		// Spin wheels... maybe?
		if (moveFwd) {
			car.accelerate(delta);
		}
		if (moveBack) {
			car.decelerate(delta);
		}
		// Angle wheels... maybe?
		if (moveLeft) {
			car.steerLeft(delta);
		}
		if (moveRight) {
			car.steerRight(delta);
		}
		car.tick(delta);
		
		if (modify) {
			modifyCar();
		}
			
		// Clear screen and z-Buffer
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
				
		glPushMatrix();

			// POSITION THE CAMERA
			// TODO use the camera object!
			glTranslatef(-camPos[0], -camPos[1], -camPos[2]);
			glRotatef(camRot[0], 1, 0, 0);
			glRotatef(camRot[1], 0, 1 ,0);
			glRotatef(camRot[2], 0, 0, 1);
			
			// BOX
			glPushMatrix();
				glTranslatef(-2.5f, 0, -3.6f);
				glRotatef(32, 0, 1, 0);
				
				cardBoardBox.draw(textureManager.getId("cardboardbox.png"));
			glPopMatrix();

			// CAR
			glPushMatrix();
				//glTranslatef(-transX, -transY, -transZ);
				float[] carPos = car.getPosition();
				float[] carDir = car.getDirection();
				// glTranslatef(carPos[0], carPos[1], carPos[2]);
				//glRotatef(angle, 0, 1, 0); // Fucking stop rotating, I'm gonna vomit all over the screen!
				
				car.draw();
			glPopMatrix();
			
			// WORKSHOP
			glPushMatrix();				
				carWorkshop.draw(textureManager.getId("cg.png"));
			glPopMatrix();
					
		glPopMatrix();
	}
			
	/**
	 * Get the time in milliseconds
	 * http://ninjacave.com/lwjglbasics4
	 * 
	 * @return The system time in milliseconds
	 */
	public long getTime() {
		return (Sys.getTime() * 1000) / Sys.getTimerResolution();
	}
	
	private float getDelta() {
		long now = getTime();
		float delta = now - lastRender;
		lastRender = now;
		return (FPS * delta / 1000f);
	}
	
	private void processInput() {
		while (Keyboard.next()) {
		    if (Keyboard.getEventKeyState()) {
		        switch (Keyboard.getEventKey()) {
			        case Keyboard.KEY_A:
			        	moveLeft = true;	
			        	break;
			        case Keyboard.KEY_D:
			        	moveRight = true;	
			        	break;
			        case Keyboard.KEY_W:
			        	moveFwd = true;	
			        	break;
			        case Keyboard.KEY_S:
			        	moveBack = true;	
			        	break;
			        case Keyboard.KEY_O:
			        	moveUp = true;
			        	break;
			        case Keyboard.KEY_L:
			        	moveDown = true;
			        	break;
			        case Keyboard.KEY_UP:
			        	camUp = true;
			        	break;
			        case Keyboard.KEY_RIGHT:
			        	camRight = true;
			        	break;
			        case Keyboard.KEY_DOWN:
			        	camDown = true;
			        	break;
			        case Keyboard.KEY_LEFT:
			        	camLeft = true;
			        	break;
			        case Keyboard.KEY_ADD:
			        	camIn = true;
			        	break;
			        case Keyboard.KEY_SUBTRACT:
			        	camOut = true;
			        	break;
			        case Keyboard.KEY_M:
			        	modify = true;
			        	break;
		        }
		    }
		    else {
		    	switch (Keyboard.getEventKey()) {
			        case Keyboard.KEY_A:
			        	moveLeft = false;	
			        	break;
			        case Keyboard.KEY_D:
			        	moveRight = false;	
			        	break;
			        case Keyboard.KEY_W:
			        	moveFwd = false;
			        	break;
			        case Keyboard.KEY_S:
			        	moveBack = false;	
			        	break;
			        case Keyboard.KEY_O:
			        	moveUp = false;
			        	break;
			        case Keyboard.KEY_L:
			        	moveDown = false;
			        	break;
			        case Keyboard.KEY_UP:
			        	camUp = false;
			        	break;
			        case Keyboard.KEY_RIGHT:
			        	camRight = false;
			        	break;
			        case Keyboard.KEY_DOWN:
			        	camDown = false;
			        	break;
			        case Keyboard.KEY_LEFT:
			        	camLeft = false;
			        	break;
			        case Keyboard.KEY_ADD:
			        	camIn = false;
			        	break;
			        case Keyboard.KEY_SUBTRACT:
			        	camOut = false;
			        	break;
			        case Keyboard.KEY_M:
			        	modify = false;
			        	break;
		    	}
		    }
		}
	}
}
