package carage;
import static org.lwjgl.opengl.GL11.GL_BACK;
import static org.lwjgl.opengl.GL11.GL_CCW;
import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_CULL_FACE;
import static org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_TEST;
import static org.lwjgl.opengl.GL11.GL_RENDERER;
import static org.lwjgl.opengl.GL11.GL_VERSION;
import static org.lwjgl.opengl.GL11.GL_ONE_MINUS_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_BLEND;
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
import static org.lwjgl.opengl.GL20.glUseProgram;
import static org.lwjgl.opengl.GL20.glUniform1i;
import static org.lwjgl.opengl.GL20.glUniform1f;
import static org.lwjgl.opengl.GL20.glUniform2i;

import java.util.ArrayList;

import lenz.opengl.AbstractSimpleBase;
import lenz.opengl.utils.ShaderProgram;
import lenz.opengl.utils.Texture;

import org.lwjgl.Sys;
import org.lwjgl.input.Keyboard;
import org.lwjgl.util.vector.Vector3f;

import carage.engine.Asset;
import carage.engine.Camera;
import carage.engine.LightSource;
import carage.engine.Material;
import carage.engine.PlaneGeometry;
import carage.engine.Renderer;
import carage.engine.ShaderAttribute;
import carage.engine.ShaderManager;
import carage.engine.VertexBufferObject;

// http://antongerdelan.net/opengl/
// http://www.opengl-tutorial.org/beginners-tutorials/
// http://wiki.lwjgl.org/wiki/Main_Page
// http://www.codinglabs.net/article_world_view_projection_matrix.aspx
// http://www.gamedev.net/page/resources/_/technical/opengl/the-basics-of-glsl-40-shaders-r2861
public class Carage extends AbstractSimpleBase {
		
	public static final int FPS = 60;
	public static final String DEFAULT_SHADER = "phong";	
	
	public static final float CAM_PAN_MIN =  5.0f;
	public static final float CAM_PAN_MAX = 55.0f;
	public static final float CAM_PAN_STEP = 0.25f;
	
	private long lastRender = 0;
	private float delta = 0;
	
	// TODO Finally implement proper input handling, this is ugly
	private boolean buttonUp = false;
	private boolean buttonLeft = false;
	private boolean buttonRight = false;
	private boolean buttonDown = false;
	private boolean buttonZoomIn = false;
	private boolean buttonZoomOut = false;
	private boolean buttonRotUp = false;
	private boolean buttonRotDown = false;
	private boolean buttonRotLeft = false;
	private boolean buttonRotRight = false;
	
	private boolean lightIncrease = false;
	private boolean lightDecrease = false;
	private boolean toggleLight = false;
	
	private boolean testValueUp = false;
	private boolean testValueDown = false;

	// TODO get handling of multiple shaders to work...
	private ShaderManager shaderManager;
	private ShaderProgram phongShader;
	private ShaderProgram proceduralShader;
	
	private Renderer renderer;	// This guy is gonna take care of all the rendering
	private Camera camera;		// We'll hand this to the Renderer, he needs it
	private LightSource light;	// Only one single light is currently supported
	private Asset cameraOverlay;
	
	private boolean camPanningRight = true;
	private int camPausedFor = 0;
	private float scanlineTimer = 0;
	
	private ArrayList<Asset> assets = null;
	private Car car = null;

	public static void main(String[] args) {
		new Carage().start();
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
	}
	
	private void printInfo() {
		System.out.println("[Starting "+TITLE+"]");
		System.out.println("Viewport size  : "+WIDTH+" * "+HEIGHT+" px");
		System.out.println("OpenGL Renderer: "+glGetString(GL_RENDERER));
		System.out.println("OpenGL Version : "+glGetString(GL_VERSION));
		System.out.println("GLSL Version   : "+glGetString(GL_SHADING_LANGUAGE_VERSION));
	}
	
	private void printAssetInfo(Asset asset) {
		VertexBufferObject positionVBO = asset.getVAO().getVBO(ShaderAttribute.POSITION);
		int numVertices = positionVBO.getSize() / positionVBO.getChunkSize();
		int numIndices = asset.getIBO().getSize();
		Vector3f size = asset.getBoundingBox().getSize();
		
		System.out.println("[OBJ Information]");
		System.out.println("Num. of Vertices : "+numVertices);
		System.out.println("Num. of Indices  : "+numIndices);
		System.out.println("Object Dimensions: "+size.getX()+" x "+size.getY()+" x "+size.getZ());
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
		// Pass in the viewport resolution. Do it again if the resolution changes!
		
		proceduralShader.bind();
		glUniform1i(proceduralShader.getUniformLocation("viewportWidth"), WIDTH);
		glUniform1i(proceduralShader.getUniformLocation("viewportHeight"), HEIGHT);
		proceduralShader.unbind();
	}
	
	private void initCamera() {
		camera = new Camera();
		camera.setPosition(-1.8f, 1.8f, 4f);
		camera.setRotation(new Vector3f(-0.25f, -0.4f, 0f));
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
		
		Asset hangingBulb = new Asset("hanging-bulb");
		hangingBulb.setMaterial(new Material("", null, 0.8f, 0.05f, 0.15f, 50));
		hangingBulb.setPosition(0f, 2.0f, 2.0f);
		assets.add(hangingBulb);
		
		Asset cardboardBox = new Asset("cardboardbox");
		cardboardBox.setPosition(-3.2f, 0, -3.8f);
		cardboardBox.setRotation(new Vector3f(0f, 35f, 0f));
		assets.add(cardboardBox);
	}
	
	private void initCameraOverlay() {
		cameraOverlay = new Asset(new PlaneGeometry(), new Texture("cg.png"));
		cameraOverlay.setMaterial(new Material("", proceduralShader));
		cameraOverlay.setPosition(0.0f, 0.0f, 0.11f);
		cameraOverlay.setScale(2f, 2f, 1f);
	}
	
	private void increaseScanlineTimer() {
		scanlineTimer = (scanlineTimer >= 360) ? scanlineTimer - 360f : scanlineTimer + (FPS * delta / 1000f);
		
		/*
		for (int i=0; i<800; ++i) {
			int time = (int) (scanlineTimer % 6);
			int lineNumber = i % 6;

			if (lineNumber == ((0 + time) % 6) || lineNumber == ((3 + time) % 6)) {
				System.out.println("medium green, " + ((0 + time) % 6));
			}
			if (lineNumber == ((4 + time) % 6) || lineNumber == ((5 + time) % 6)) {
				System.out.println("dark green");
			}
		}
		*/
		// medium - light - light - medium - dark - dark - ...
	}
	
	@Override
	protected void render() {
		// Shader "delta"
		increaseScanlineTimer();
		
		// Time shizzle
		delta = getDelta();
		
		// User pressed any relevant keys?
		processInput();
		
		// Move/Rotate/Whatever some Assets according to User Input
		modifyAssets();
		
		// Clear dat screen!
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
        		
		// Send light information to shader (in case it has moved, its intensity changed, ...)
		// TODO Light handling has to be improved... we're changing states (shader program) just to send the uniforms over...
		phongShader.bind();
		light.sendToShader(phongShader);
		
		// This shader doesn't need light, so...
//		glUseProgram(proceduralShader.getId());
//		light.sendToShader(proceduralShader);		
				
		// Finally, render our assets!
		renderer.renderAssetGroup(car);
		for (Asset asset : assets) {
			renderer.renderAsset(asset);
		}
		
		// Procedural Shader needs the delta...
		proceduralShader.bind();
		glUniform1f(proceduralShader.getUniformLocation("shaderTimer"), scanlineTimer);
		renderCameraOverlay();
	}
	
	private void renderCameraOverlay() {
		glEnable(GL_BLEND);
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
		renderer.renderAsset(cameraOverlay);
		glDisable(GL_BLEND);
	}
	
	private void modifyAssets() {
		// http://www.opengl-tutorial.org/beginners-tutorials/tutorial-3-matrices/
		// What we want: SCALE, ROTATE, TRANS
		// What we do  : TRANS, ROTATE, SCALE
		
		float transX = 0f;
		float transY = 0f;
		
		float transZ = buttonZoomIn  ? -0.02f * delta : 0;
			  transZ = buttonZoomOut ?  0.02f * delta : transZ;
			  
		float rotX = buttonRotUp   ?  0.5f * delta : 0;
			  rotX = buttonRotDown ? -0.5f * delta : rotX;
			  
		float rotY = buttonRotLeft  ?  0.5f * delta : 0;
			  rotY = buttonRotRight ? -0.5f * delta : rotY;
			  
	    rotX = (float) Math.toRadians(rotX);
	    rotY = (float) Math.toRadians(rotY);
			
	    camera.alterPosition(new Vector3f(transX, transY, transZ));
		camera.alterRotation(new Vector3f(rotX, rotY, 0));
		
		car.tick(delta);
		if (buttonLeft) { car.steerLeft(delta);	}
		if (buttonRight) { car.steerRight(delta); }
		if (buttonUp) { car.accelerate(delta); }
		if (buttonDown) { car.decelerate(delta); }
		
		if (lightIncrease) { light.setIntensity(light.getIntensity() + 0.1f * delta); }
		if (lightDecrease) { light.setIntensity(light.getIntensity() - 0.1f * delta); }
		if (toggleLight) { light.toggle(); }
		
		Material carBodyMaterial = car.getParentAsset().getMaterial();
		if (testValueUp)   { carBodyMaterial.setSpecularHardness(carBodyMaterial.getSpecularHardness()+1); }
		if (testValueDown) { carBodyMaterial.setSpecularHardness(carBodyMaterial.getSpecularHardness()-1); }
			
		panCam();
	}
	
	private void panCam() {
		float camRotY = camera.getRotationY();
		float camRotYDelta = (float)(Math.toRadians(CAM_PAN_STEP)) * delta;
		float camRotYMin   = (float) Math.toRadians(-CAM_PAN_MAX); // right!
		float camRotYMax   = (float) Math.toRadians(CAM_PAN_MIN);  // left!
		
		if (camPausedFor > 0) {
			--camPausedFor;
		}

		if (camPausedFor > 0) {
			return;
		}
		
		if (camPanningRight) {
			camRotY -= camRotYDelta;
			if (camRotY <= camRotYMin) { // We're at the far right
				camPanningRight = false;
				camPausedFor = 120;
				camRotY = camRotYMin;
			}
		}
		else {
			camRotY += camRotYDelta;
			if (camRotY >= camRotYMax) { // We're at the far left
				camPanningRight = true;
				camPausedFor = 120;
				camRotY = camRotYMax;
			}
		}
		camera.setRotation(camera.getRotationX(), camRotY, camera.getRotationZ());
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
		return (FPS * delta / 1000f);
	}
	
	// TODO Put this in some Input Handler Class(es)
	private void processInput() {
		while (Keyboard.next()) {
		    if (Keyboard.getEventKeyState()) {
		        switch (Keyboard.getEventKey()) {
			        case Keyboard.KEY_A:
			        	buttonLeft = true;	
			        	break;
			        case Keyboard.KEY_D:
			        	buttonRight = true;	
			        	break;
			        case Keyboard.KEY_W:
			        	buttonUp = true;	
			        	break;
			        case Keyboard.KEY_S:
			        	buttonDown = true;	
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
			        case Keyboard.KEY_I:
			        	buttonZoomIn = true;
			        	break;
			        case Keyboard.KEY_K:
			        	buttonZoomOut = true;
			        	break;
			        case Keyboard.KEY_O:
			        	lightIncrease = true;
			        	break;
			        case Keyboard.KEY_L:
			        	lightDecrease = true;
			        	break;
			        case Keyboard.KEY_P:
			        	toggleLight = true;
			        	break;
			        case Keyboard.KEY_N:
			        	testValueUp = true;
			        	break;
			        case Keyboard.KEY_M:
			        	testValueDown = true;
			        	break;
		        }
		    }
		    else {
		    	switch (Keyboard.getEventKey()) {
			        case Keyboard.KEY_A:
			        	buttonLeft = false;	
			        	break;
			        case Keyboard.KEY_D:
			        	buttonRight = false;	
			        	break;
			        case Keyboard.KEY_W:
			        	buttonUp = false;	
			        	break;
			        case Keyboard.KEY_S:
			        	buttonDown = false;	
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
			        case Keyboard.KEY_I:
			        	buttonZoomIn = false;
			        	break;
			        case Keyboard.KEY_K:
			        	buttonZoomOut = false;
			        	break;
			        case Keyboard.KEY_O:
			        	lightIncrease = false;
			        	break;
			        case Keyboard.KEY_L:
			        	lightDecrease = false;
			        	break;
			        case Keyboard.KEY_P:
			        	toggleLight = false;
			        	break;
			        case Keyboard.KEY_N:
			        	testValueUp = false;
			        	break;
			        case Keyboard.KEY_M:
			        	testValueDown = false;
			        	break;
		        }
		    }
		}
	}
	
}
