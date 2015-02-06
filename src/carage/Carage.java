package carage;
import static org.lwjgl.opengl.GL11.GL_BACK;
import static org.lwjgl.opengl.GL11.GL_BLEND;
import static org.lwjgl.opengl.GL11.GL_CCW;
import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_CULL_FACE;
import static org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_TEST;
import static org.lwjgl.opengl.GL11.GL_ONE_MINUS_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_RENDERER;
import static org.lwjgl.opengl.GL11.GL_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_VERSION;
import static org.lwjgl.opengl.GL11.glBlendFunc;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glClearColor;
import static org.lwjgl.opengl.GL11.glCullFace;
import static org.lwjgl.opengl.GL11.glDisable;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glFrontFace;
import static org.lwjgl.opengl.GL11.glGetString;
import static org.lwjgl.opengl.GL11.glViewport;
import static org.lwjgl.opengl.GL20.GL_SHADING_LANGUAGE_VERSION;
import static org.lwjgl.opengl.GL20.glUniform1f;
import static org.lwjgl.opengl.GL20.glUniform1i;

import java.util.ArrayList;
import java.util.Random;

import lenz.opengl.AbstractSimpleBase;
import lenz.opengl.utils.ShaderProgram;
import lenz.opengl.utils.Texture;

import org.lwjgl.Sys;
import org.lwjgl.input.Keyboard;
import org.lwjgl.util.vector.Vector3f;

import carage.engine.Asset;
import carage.engine.LightSource;
import carage.engine.Material;
import carage.engine.PlaneGeometry;
import carage.engine.Renderer;
import carage.engine.ShaderAttribute;
import carage.engine.ShaderManager;

// http://antongerdelan.net/opengl/
// http://www.opengl-tutorial.org/beginners-tutorials/
// http://wiki.lwjgl.org/wiki/Main_Page
// http://www.codinglabs.net/article_world_view_projection_matrix.aspx
// http://www.gamedev.net/page/resources/_/technical/opengl/the-basics-of-glsl-40-shaders-r2861
public class Carage extends AbstractSimpleBase {
	
	public static final String TITLE = "Carage";
	public static final int WIDTH  = 800;
	public static final int HEIGHT = 600;
		
	public static final int TARGET_FPS = 60;
	public static final String DEFAULT_SHADER = "phong";
	
	public static final float CAM_PAN_RIGHT = 65.00f; // Smaller angle = Panning right
	public static final float CAM_PAN_LEFT  =  0.00f; // Bigger angle  = Panning left 
	public static final float CAM_PAN_STEP  =  0.25f;
	
	private long lastRender = 0;
	private float delta = 0;
	@SuppressWarnings("unused")
	private float secondsSinceLastRender = 0;
	
	// TODO Finally implement proper input handling, this is ugly
	private boolean buttonForward  = false;
	private boolean buttonBackward = false;
	private boolean buttonLeft = false;
	private boolean buttonRight = false;
	private boolean buttonRotUp = false;
	private boolean buttonRotDown = false;
	private boolean buttonRotLeft = false;
	private boolean buttonRotRight = false;
	private boolean lightIncrease = false;
	private boolean lightDecrease = false;

	private ShaderManager shaderManager;
	private ShaderProgram gouraudShader;
	private ShaderProgram phongShader;
	private ShaderProgram proceduralShader;
	
	private Renderer renderer;			// This guy is gonna take care of all the rendering
	private SurveillanceCamera camera;	// We'll hand this to the Renderer, he needs it
	private LightSource light;			// Only one single light is currently supported
	
	private Asset cameraOverlay;		// This is where we render our procedural onto
	private ArrayList<Asset> assets = null;
	private Car car = null;
	private Asset hangingBulb = null;
	private Asset slendi = null;
	
	private boolean lightIsOn = true;
	private int lightFlickers = 0;
	private float scanlineTimer = 0f;
	private boolean renderSlendi = false;
	private boolean renderCameraOverlay = true;
	private boolean slendiMode = true;
	
	public static void main(String[] args) {
		new Carage().start(WIDTH, HEIGHT, TITLE);
	}

	@Override
	protected void initOpenGL() {
		printInfo();
		initViewport();
		initShaders();
		initCamera();
		initLightSource();
		initRenderer();
		initAssets();
		lastRender = getTime();
	}
	
	private void printInfo() {
		System.out.println("[Starting "+TITLE+"]");
		System.out.println("Viewport size  : "+WIDTH+" * "+HEIGHT+" px");
		System.out.println("OpenGL Renderer: "+glGetString(GL_RENDERER));
		System.out.println("OpenGL Version : "+glGetString(GL_VERSION));
		System.out.println("GLSL Version   : "+glGetString(GL_SHADING_LANGUAGE_VERSION));
		System.out.println();
	}
		
	private void initViewport() {
		glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
		glViewport(0, 0, WIDTH, HEIGHT);
		glEnable(GL_DEPTH_TEST);	// Do not render hidden geometry
		glEnable(GL_CULL_FACE);		// Do not render back sides
		glCullFace(GL_BACK);		// cull back face - this is the default
		glFrontFace(GL_CCW);		// GL_CCW for counter clock-wise - default again
	}
	
	private void initShaders() {
		shaderManager = ShaderManager.getInstance();
		
		String[] attributeLocations = new String[] {
				ShaderAttribute.POSITION.getName(),
				ShaderAttribute.COLOR.getName(),
				ShaderAttribute.TEXTURE.getName(),
				ShaderAttribute.NORMALS.getName() };
		
		phongShader = shaderManager.get("phong");
		phongShader.bindAttributeLocations(attributeLocations);
		
		proceduralShader = shaderManager.get("procedural");
		proceduralShader.bindAttributeLocations(attributeLocations);
		
		gouraudShader = shaderManager.get("gouraud");
		gouraudShader.bindAttributeLocations(attributeLocations);
		
		// Pass in the viewport resolution. Do it again if the resolution changes!
		proceduralShader.bind();
		glUniform1i(proceduralShader.getUniformLocation("viewportWidth"), WIDTH);
		glUniform1i(proceduralShader.getUniformLocation("viewportHeight"), HEIGHT);
		proceduralShader.unbind();
	}
	
	private void initCamera() {
		camera = new SurveillanceCamera();
		camera.allowPitching();
		camera.setPosition(-2.8f, 2.2f, 4f);
		camera.setRotation(new Vector3f(-0.3f, 0f, 0f));
		camera.activateAutoPanning((float)Math.toRadians(CAM_PAN_LEFT), (float)Math.toRadians(CAM_PAN_RIGHT));
	}
	
	private void initLightSource() {
		light = new LightSource();
		light.setPosition(0f, 1.8f, 2.0f);
		light.setIntensity(1.0f);
	}
	
	private void initRenderer() {
		renderer = new Renderer(WIDTH, HEIGHT, camera);
	}

	private void initAssets() {
		assets = new ArrayList<>();
		initCar();
		initWorkshop();
		initCameraOverlay();
	}
	
	private void initCar() {
		car = new Car("vw-polo", "vw-polo-wheel");
		car.getParentAsset().setMaterial(new Material("", phongShader));
		car.getParentAsset().getMaterial().setSpecularHardness(80);
		car.getParentAsset().getMaterial().setAmbientReflectivity(0.1f);
		car.getParentAsset().getMaterial().setDiffuseReflectivity(1.4f);
		car.getParentAsset().getMaterial().setSpecularReflectivity(0.5f); 
		car.printInfo();
	}
	
	private void initWorkshop() {
		Asset workshopFloor = new Asset("workshop-floor");
		workshopFloor.setMaterial(new Material("", phongShader));
		workshopFloor.getMaterial().setSpecularHardness(40);
		assets.add(workshopFloor);
		
		Asset workshopWalls = new Asset("workshop-walls");
		workshopWalls.setMaterial(new Material("", phongShader));
		workshopWalls.getMaterial().setSpecularHardness(10);
		assets.add(workshopWalls);
		
		Asset workshopColumns = new Asset("workshop-columns");
		workshopColumns.setMaterial(new Material("", phongShader));
		assets.add(workshopColumns);		
		
		Asset workshopCeiling = new Asset("workshop-ceiling");
		workshopCeiling.setMaterial(new Material("", phongShader));
		assets.add(workshopCeiling);
		
		Asset cardboardBox1 = new Asset("cardboardbox");
		cardboardBox1.setPosition(-3.2f, 0f, -3.8f);
		cardboardBox1.setRotation(0f, (float)Math.toRadians(35), 0f);
		assets.add(cardboardBox1);
		
		Asset cardboardBox2 = new Asset("cardboardbox");
		cardboardBox2.setPosition(-4.1f, 0f, -2.6f);
		cardboardBox2.setRotation(0f, (float)Math.toRadians(-15), 0f);
		assets.add(cardboardBox2);
		
		Asset genericWheel1 = new Asset("generic-wheel");
		genericWheel1.setPosition(3.9f, 0.13f, 3.0f);
		genericWheel1.setRotation((float)Math.toRadians(-90), 0f, 0f);
		assets.add(genericWheel1);
		
		Asset genericWheel2 = new Asset("generic-wheel");
		genericWheel2.setPosition(3.8f, 0.39f, 2.95f);
		genericWheel2.setRotation((float)Math.toRadians(-90), 0f, 0f);
		assets.add(genericWheel2);
		
		Asset genericWheel3 = new Asset("generic-wheel");
		genericWheel3.setPosition(3.85f, 0.65f, 2.97f);
		genericWheel3.setRotation((float)Math.toRadians(-90), 0f, 0f);
		assets.add(genericWheel3);
		
		Asset genericWheel4 = new Asset("generic-wheel");
		// genericWheel4.setMaterial(new Material("", gouraudShader));
		genericWheel4.setPosition(-4.6f, 0.3f, 0.9f);
		genericWheel4.setRotation((float)Math.toRadians(20), (float)Math.toRadians(-90), 0f);
		assets.add(genericWheel4);
		
		Asset ladder = new Asset("ladder");
		ladder.setPosition(0.8f, 0f, 4.2f);
		ladder.setRotation((float)Math.toRadians(15), 0f , 0f);
		assets.add(ladder);
				
		hangingBulb = new Asset("hanging-bulb");
		hangingBulb.setMaterial(new Material("", null, 0.8f, 0.05f, 0.15f, 50));
		hangingBulb.setPosition(0f, 2.0f, 2.0f);
		assets.add(hangingBulb);
		
		slendi = new Asset("slendi");
		slendi.setRotation(0f, (float)Math.toRadians(10), 0f);
		slendi.setPosition(1.3f, 0.4f, -0.6f);
	}
	
	private void initCameraOverlay() {
		cameraOverlay = new Asset(new PlaneGeometry(), new Texture("camera-overlay.png"));
		cameraOverlay.setMaterial(new Material("", proceduralShader));
		cameraOverlay.setPosition(0.0f, 0.0f, 0.11f);
		cameraOverlay.setScale(2f, 2f, 1f);
	}
		
	@Override
	protected void update() {
		updateDelta();
		increaseScanlineTimer();
		processInput();
		updateLightSource();
		updateScene();
	}
	
	private void updateDelta() {
		delta = getDelta();
		secondsSinceLastRender = deltaToSeconds(delta);
	}
	
	private void increaseScanlineTimer() {
		scanlineTimer = (scanlineTimer >= 1) ? scanlineTimer - 1 : scanlineTimer + secondsSinceLastRender;
		System.out.println(scanlineTimer);
	}
	
	private void updateLightSource() {
		// Send light information to shader (in case it has moved, its intensity changed, ...)
		// TODO Light handling has to be improved... we're changing states (shader program) just to send the uniforms over...
		/*
		gouraudShader.bind();
		light.sendToShader(gouraudShader);
		*/
		phongShader.bind();
		light.sendToShader(phongShader);
		
	}
	
	private void updateScene() {
		// http://www.opengl-tutorial.org/beginners-tutorials/tutorial-3-matrices/
		// What we want: SCALE, ROTATE, TRANS
		// What we do  : TRANS, ROTATE, SCALE
		
	    adjustCarWheels();
	    adjustLightSource();
		flickerLight(slendiMode);
		adjustBulbAmbient();
		panCam();
		pitchCam();
	}
	
	@Override
	protected void render() {
		clearScreen();
		// Render Car first
		renderer.renderAssetGroup(car);
		// Render Workshop and the props inside of it
		for (Asset asset : assets) {
			renderer.renderAsset(asset);
		}
		renderSlendi();
		// Camera overlay is using a different shader, so let's draw it last
		renderCameraOverlay(); 
	}
	
	private void renderSlendi() {
		if (!renderSlendi) { return; }
		
		renderer.renderAsset(slendi);
	}
	
	private void renderCameraOverlay() {
		if (!renderCameraOverlay) { return; }
		
		// Procedural Shader needs input in order to move the scanlines...
		proceduralShader.bind();
		glUniform1f(proceduralShader.getUniformLocation("scanlineTimer"), scanlineTimer);
		// We have to enable alpha blending, otherwise the overlay would be opaque
		glEnable(GL_BLEND);
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
		renderer.renderAsset(cameraOverlay);
		glDisable(GL_BLEND);
	}
	
	private void clearScreen() {
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
	}
	
	private void flickerLight(boolean slendiMode) {
		if (!lightIsOn) {
			lightFlickers = 0;
			return;
		}
		// TODO make this less ugly and link it with the FPS; maybe even put it into the lamp's class
		Random rand = new Random();
		if (lightFlickers > 0) {
			if (rand.nextFloat() > 0.8) {
				light.toggle();
				--lightFlickers;
				if (slendiMode && lightFlickers > 3 && light.isOn() && rand.nextFloat() > 0.6) {
					renderSlendi = true;
				}
			}
		}
		if (lightFlickers == 1) {
			renderSlendi = false;
		}
		if (lightFlickers == 0) {
			light.turnOn();
		}
		if (rand.nextFloat() > 0.99) {
			lightFlickers += rand.nextInt(8);
		}
	}
	
	private void toggleLight() {
		lightIsOn = !lightIsOn;
		light.toggle();
	}
	
	private void toggleCameraPan() {
		camera.toggleAutoPanning();
	}
	
	private void toggleCameraOverlay() {
		renderCameraOverlay = !renderCameraOverlay;
	}
	
	@SuppressWarnings("unused")
	private void toggleSlendiMode() {
		slendiMode = !slendiMode;
	}
	
	private void adjustLightSource() {
		if (lightIncrease) { light.setIntensity(light.getIntensity() + 0.1f * delta); }
		if (lightDecrease) { light.setIntensity(light.getIntensity() - 0.1f * delta); }
	}
	
	private void adjustCarWheels() {
		if (buttonLeft)     { car.steerLeft(delta); }
		if (buttonRight)    { car.steerRight(delta); }
		if (buttonForward)  { car.accelerate(delta); }
		if (buttonBackward) { car.decelerate(delta); }
		car.tick(delta);
	}
	
	private void adjustBulbAmbient() {
		float ambient = (!light.isOn() || (light.getIntensity() <= 0.1)) ? 0.0f : 0.8f;
		hangingBulb.getMaterial().setAmbientReflectivity(ambient);
	}
	
	private void pitchCam() {
		if (buttonRotLeft)  { camera.panLeft(); }
		if (buttonRotRight) { camera.panRight(); }
		if (buttonRotUp)    { camera.pitchUp(); }
		if (buttonRotDown)  { camera.pitchDown(); }
	}
	
	private void panCam() {
		camera.tick(delta);
	}
			
	/**
	 * Get the time in milliseconds
	 * From: http://ninjacave.com/lwjglbasics4
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
		return (TARGET_FPS * delta / 1000f);
	}
	
	private float deltaToSeconds(float delta) {
		return 1f / TARGET_FPS * delta;
	}
	
	// TODO Put this in some Input Handler Class(es)
	private void processInput() {
		while (Keyboard.next()) {
		    if (Keyboard.getEventKeyState()) { // Keys pressed
		        switch (Keyboard.getEventKey()) {
			        case Keyboard.KEY_A:
			        	buttonLeft = true;	
			        	break;
			        case Keyboard.KEY_D:
			        	buttonRight = true;	
			        	break;
			        case Keyboard.KEY_W:
			        	buttonForward = true;	
			        	break;
			        case Keyboard.KEY_S:
			        	buttonBackward = true;	
			        	break;
			        case Keyboard.KEY_UP:
			        	buttonRotUp = true;
			        	break;
			        case Keyboard.KEY_RIGHT:
			        	buttonRotRight = true;
			        	break;
			        case Keyboard.KEY_DOWN:
			        	buttonRotDown = true;
			        	break;
			        case Keyboard.KEY_LEFT:
			        	buttonRotLeft = true;
			        	break;
			        case Keyboard.KEY_EQUALS:
			        case Keyboard.KEY_ADD:
			        	lightIncrease = true;
			        	break;
			        case Keyboard.KEY_MINUS:
			        case Keyboard.KEY_SUBTRACT:
			        	lightDecrease = true;
			        	break;
		        }
		    }
		    else {	// Keys released
		    	switch (Keyboard.getEventKey()) {
			        case Keyboard.KEY_A:
			        	buttonLeft = false;	
			        	break;
			        case Keyboard.KEY_D:
			        	buttonRight = false;	
			        	break;
			        case Keyboard.KEY_W:
			        	buttonForward = false;	
			        	break;
			        case Keyboard.KEY_S:
			        	buttonBackward = false;	
			        	break;
			        case Keyboard.KEY_UP:
			        	buttonRotUp = false;
			        	break;
			        case Keyboard.KEY_RIGHT:
			        	buttonRotRight = false;
			        	break;
			        case Keyboard.KEY_DOWN:
			        	buttonRotDown = false;
			        	break;
			        case Keyboard.KEY_LEFT:
			        	buttonRotLeft = false;
			        	break;
			        case Keyboard.KEY_EQUALS:
			        case Keyboard.KEY_ADD:
			        	lightIncrease = false;
			        	break;
			        case Keyboard.KEY_MINUS:
			        case Keyboard.KEY_SUBTRACT:
			        	lightDecrease = false;
			        	break;
			        case Keyboard.KEY_L:
			        	toggleLight();
			        	break;
			        case Keyboard.KEY_O:
			        	toggleCameraOverlay();
			        	break;
			        case Keyboard.KEY_P:
			        	toggleCameraPan();
			        	break;
		        }
		    }
		}
	}
	
}
