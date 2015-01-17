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
import static org.lwjgl.opengl.GL20.glGetUniformLocation;
import static org.lwjgl.opengl.GL20.glUniformMatrix4;
import static org.lwjgl.opengl.GL20.glUseProgram;

import java.nio.FloatBuffer;

import lenz.opengl.AbstractSimpleBase;
import lenz.opengl.utils.ShaderProgram;

import org.lwjgl.BufferUtils;
import org.lwjgl.Sys;
import org.lwjgl.input.Keyboard;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

import carage.engine.ProjectionMatrix;
import carage.engine.Renderer;
import carage.engine.ShaderAttribute;
import carage.engine.TextureManager;

// http://antongerdelan.net/opengl/
// http://www.opengl-tutorial.org/beginners-tutorials/
// http://wiki.lwjgl.org/wiki/Main_Page
public class Carage extends AbstractSimpleBase {
		
	public static final int FPS = 60;
	
	private String shader = "phong";
	
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
	
	private Matrix4f projectionMatrix;
	private Matrix4f viewMatrix;
	private Matrix4f modelMatrix;
	
	private int projectionMatrixLocation;
	private int viewMatrixLocation;
	private int modelMatrixLocation;
	
	private FloatBuffer matrixBuffer;
	
	private Asset asset = null;

	public static void main(String[] args) {
		new Carage().start();
	}

	@Override
	protected void initOpenGL() {
		printInfo();
		initMatrices();
		initViewport();
		
		initShaders();
		initTestMesh();
	}
	
	private void printInfo() {
		System.out.println("[Starting "+TITLE+"]");
		System.out.println("Viewport size  : "+WIDTH+" * "+HEIGHT+" px");
		System.out.println("OpenGL Renderer: "+glGetString(GL_RENDERER));
		System.out.println("OpenGL Version : "+glGetString(GL_VERSION));
		System.out.println("GLSL Version   : "+glGetString(GL_SHADING_LANGUAGE_VERSION));
	}
	
	private void initViewport() {
		glClearColor(0.4f, 0.6f, 1.0f, 1.0f);
		glViewport(0, 0, WIDTH, HEIGHT);
		glEnable(GL_DEPTH_TEST);
		
		// Do not render back sides
		glEnable(GL_CULL_FACE);
		glCullFace (GL_BACK); // cull back face - this is the default
		glFrontFace (GL_CCW); // GL_CCW for counter clock-wise - default again
		
	}
	
	private void initShaders() {
		sp = new ShaderProgram(shader);
		String[] attributeLocations = new String[] {ShaderAttribute.POSITION.getName(), ShaderAttribute.COLOR.getName(), ShaderAttribute.TEXTURE.getName()};
		sp.bindAttributeLocations(attributeLocations);
		
		spId = sp.getId();
		projectionMatrixLocation = glGetUniformLocation(spId, "projectionMatrix");
		viewMatrixLocation       = glGetUniformLocation(spId, "viewMatrix");
		modelMatrixLocation      = glGetUniformLocation(spId, "modelMatrix");
		
		glUseProgram(spId);
	}
	
	private void initMatrices() {
		initModelMatrix();
		initViewMatrix();
		initProjectionMatrix();
		initMatrixBuffer();
	}
	
	private void initProjectionMatrix() {
		// width, height, near plane, far plane, field of view
		projectionMatrix = new ProjectionMatrix(WIDTH, HEIGHT, 0.1f, 100f, 60f);
	}
	
	private void initViewMatrix() {
		viewMatrix = new Matrix4f();
	}
	
	private void initModelMatrix() {
		modelMatrix = new Matrix4f();
	}
	
	private void initMatrixBuffer() {
		// http://stackoverflow.com/questions/10697161/why-floatbuffer-instead-of-float
		matrixBuffer = BufferUtils.createFloatBuffer(16);
	}

	private void initTestMesh() {
		asset = new Asset("vw-polo");
	}
	
	@Override
	protected void render() {
		// Time shizzle
		delta = getDelta();
		
		// User pressed any relevant keys?
		processInput();
		
		// Let our Quad (or Tri or whatever) move a bit
		modifyModelMatrix();
		
		// Hand over the matrices to the vertex shader
		matricesToShader();
		
		// Clear dat screen!
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
        
		// Finally, render our simple test geometry!		
		Renderer.renderAsset(asset);
	}
	
	private void modifyModelMatrix() {
		// http://www.opengl-tutorial.org/beginners-tutorials/tutorial-3-matrices/
		// What we want: SCALE, ROTATE, TRANS
		// What we do  : TRANS, ROTATE, SCALE
		
		float transX = buttonLeft  ? -0.02f * delta : 0;
			  transX = buttonRight ?  0.02f * delta : transX;
			  
		float transY = buttonUp    ?  0.02f * delta : 0;
			  transY = buttonDown  ? -0.02f * delta : transY;
			  
		float transZ = buttonZoomIn  ? -0.02f * delta : 0;
			  transZ = buttonZoomOut ?  0.02f * delta : transZ;
		
		
		// TODO We need some pushing and popping here, otherwise rotating+translating doesn't work as expected (?)
		modelMatrix.translate(new Vector3f(transX, transY, transZ));
		//modelMatrix.rotate(-delta*0.03f, Z_AXIS);
	}
	
	private void matricesToShader() {
		//glUseProgram(spId);
		
		// Projection Matrix
        projectionMatrix.store(matrixBuffer);
        matrixBuffer.flip();
        glUniformMatrix4(projectionMatrixLocation, false, matrixBuffer);
        
        // View Matrix
        viewMatrix.store(matrixBuffer);
        matrixBuffer.flip();
        glUniformMatrix4(viewMatrixLocation, false, matrixBuffer);
        
        // Model Matrix
        modelMatrix.store(matrixBuffer);
        matrixBuffer.flip();
        glUniformMatrix4(modelMatrixLocation, false, matrixBuffer);

        //glUseProgram(0);
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
