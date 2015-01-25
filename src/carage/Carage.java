package carage;
import static org.lwjgl.opengl.GL11.GL_BACK;
import static org.lwjgl.opengl.GL11.GL_CCW;
import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_CULL_FACE;
import static org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_TEST;
import static org.lwjgl.opengl.GL11.GL_RENDERER;
import static org.lwjgl.opengl.GL11.GL_VERSION;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glClearColor;
import static org.lwjgl.opengl.GL11.glCullFace;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glFrontFace;
import static org.lwjgl.opengl.GL11.glGetString;
import static org.lwjgl.opengl.GL11.glViewport;
import static org.lwjgl.opengl.GL20.GL_SHADING_LANGUAGE_VERSION;
import static org.lwjgl.opengl.GL20.glUseProgram;

import java.util.ArrayList;

import lenz.opengl.AbstractSimpleBase;
import lenz.opengl.utils.ShaderProgram;

import org.lwjgl.Sys;
import org.lwjgl.input.Keyboard;
import org.lwjgl.util.vector.Vector3f;

import carage.engine.Asset;
import carage.engine.Camera;
import carage.engine.Renderer;
import carage.engine.ShaderAttribute;
import carage.engine.VertexBufferObject;

// http://antongerdelan.net/opengl/
// http://www.opengl-tutorial.org/beginners-tutorials/
// http://wiki.lwjgl.org/wiki/Main_Page
// http://www.codinglabs.net/article_world_view_projection_matrix.aspx
// http://www.gamedev.net/page/resources/_/technical/opengl/the-basics-of-glsl-40-shaders-r2861
public class Carage extends AbstractSimpleBase {
		
	public static final int FPS = 60;
	public static final String DEFAULT_SHADER = "phong";	
	
	public static final Vector3f X_AXIS = new Vector3f(1, 0, 0);
	public static final Vector3f Y_AXIS = new Vector3f(0, 1, 0);
	public static final Vector3f Z_AXIS = new Vector3f(0, 0, 1);
	
	private long lastRender = 0;
	private float delta = 0;
	
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
		
	private ShaderProgram sp;
	private int spId;
	
	private Renderer renderer;
	private Camera camera;
	
	private ArrayList<Asset> assets = null;
	private Car car = null;
	private Asset workshopFloor = null;
	private Asset workshopWalls = null;
	private Asset workshopColumns = null;
	private Asset workshopCeiling = null;
	private Asset cardboardBox = null;

	public static void main(String[] args) {
		new Carage().start();
	}

	@Override
	protected void initOpenGL() {
		printInfo();
		initViewport();
		initShaders();
		initAssets();
		
		camera = new Camera();
		camera.setPosition(-1.8f, 1.8f, 4f);
		camera.setRotation(new Vector3f(-0.25f, -0.4f, 0f));
		
		renderer = new Renderer(sp, WIDTH, HEIGHT, camera);
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
		glClearColor(0.4f, 0.6f, 1.0f, 1.0f);
		glViewport(0, 0, WIDTH, HEIGHT);
		glEnable(GL_DEPTH_TEST);	// Do not render hidden geometry
		glEnable(GL_CULL_FACE);		// Do not render back sides
		glCullFace(GL_BACK);		// cull back face - this is the default
		glFrontFace(GL_CCW);		// GL_CCW for counter clock-wise - default again
	}
	
	private void initShaders() {
		sp = new ShaderProgram(DEFAULT_SHADER);
		String[] attributeLocations = new String[] {ShaderAttribute.POSITION.getName(), ShaderAttribute.COLOR.getName(), ShaderAttribute.TEXTURE.getName(), ShaderAttribute.NORMALS.getName()};
		sp.bindAttributeLocations(attributeLocations);
		
		spId = sp.getId();
		glUseProgram(spId);
	}

	private void initAssets() {
		assets = new ArrayList<>();
		
		car = new Car("vw-polo", "vw-polo-wheel");
		car.printInfo();
		
		workshopFloor = new Asset("workshop-floor");
		workshopWalls = new Asset("workshop-walls");
		workshopColumns = new Asset("workshop-columns");
		workshopCeiling = new Asset("workshop-ceiling");
		
		cardboardBox = new Asset("cardboardbox");
		cardboardBox.setPosition(-3.2f, 0, -3.8f);
		cardboardBox.setRotation(new Vector3f(0f, 35f, 0f));
		
	}
	
	@Override
	protected void render() {
		// Time shizzle
		delta = getDelta();
		
		// User pressed any relevant keys?
		processInput();
		
		// Let our Quad (or Tri or whatever it currently is) move a bit
		moveTestAsset();
		
		// Clear dat screen!
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
        
		// Finally, render our simple test geometry!		
		renderer.renderAssetGroup(car);
		renderer.renderAsset(workshopFloor);
		renderer.renderAsset(workshopWalls);
		renderer.renderAsset(workshopColumns);
		renderer.renderAsset(workshopCeiling);
		renderer.renderAsset(cardboardBox);
	}
	
	private void moveTestAsset() {
		// http://www.opengl-tutorial.org/beginners-tutorials/tutorial-3-matrices/
		// What we want: SCALE, ROTATE, TRANS
		// What we do  : TRANS, ROTATE, SCALE
		
		/*
		float transX = buttonLeft  ? -0.02f * delta : 0;
			  transX = buttonRight ?  0.02f * delta : transX;
			  
		float transY = buttonUp    ?  0.02f * delta : 0;
			  transY = buttonDown  ? -0.02f * delta : transY;
		*/
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
		
	    /*
		car.alterPosition(new Vector3f(transX, transY, transZ));
		car.alterRotation(new Vector3f(rotX, 0, rotZ));
		*/
	    camera.alterPosition(new Vector3f(transX, transY, transZ));
		camera.alterRotation(new Vector3f(rotX, rotY, 0));
		
		car.tick(delta);
		if (buttonLeft) { car.steerLeft(delta);	}
		if (buttonRight) { car.steerRight(delta); }
		if (buttonUp) { car.accelerate(delta); }
		if (buttonDown) { car.decelerate(delta); }
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
		        }
		    }
		}
	}
	
}
