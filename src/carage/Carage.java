package carage;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL13.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import org.lwjgl.BufferUtils;

import org.lwjgl.Sys;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import lenz.opengl.AbstractSimpleBase;
import lenz.opengl.utils.ShaderProgram;
import lenz.opengl.utils.Texture;

// http://antongerdelan.net/opengl/
// http://www.opengl-tutorial.org/beginners-tutorials/
// http://wiki.lwjgl.org/wiki/Main_Page
public class Carage extends AbstractSimpleBase {
		
	public static final int FPS = 60;
	
	private String shader = "phong";
	
	public static final Vector3f X_AXIS = new Vector3f(1, 0, 0);
	public static final Vector3f Y_AXIS = new Vector3f(0, 1, 0);
	public static final Vector3f Z_AXIS = new Vector3f(0, 0, 1);
	
	// TODO we also want to store their names, can enum help?
	private final int ATTR_LOCATION_GEOMETRY = 0; // in_Position
	private final int ATTR_LOCATION_COLOR    = 1; // in_Color
	private final int ATTR_LOCATION_TEXTURE  = 2; // in_TextureCoord
	private final int ATTR_LOCATION_NORMALS  = 3; // Not yet used

	private long lastRender = 0;
	private float delta = 0;
	
	private boolean buttonUp = false;
	private boolean buttonLeft = false;
	private boolean buttonRight = false;
	private boolean buttonDown = false;
	private boolean buttonZoomIn = false;
	private boolean buttonZoomOut = false;
	
	private TextureManager textureManager;
		
	private ShaderProgram sp;
	private int spId;
	
	private Matrix4f projectionMatrix;
	private Matrix4f viewMatrix;
	private Matrix4f modelMatrix;
	
	private int projectionMatrixLocation;
	private int viewMatrixLocation;
	private int modelMatrixLocation;
	
	private FloatBuffer matrixBuffer;
	
	private int vaoId = 0;
	private int vboId = 0;
	private int indicesVBOId = 0;
	private int indicesLength = 0;

	public static void main(String[] args) {
		new Carage().start();
	}

	@Override
	protected void initOpenGL() {
		printInfo();
		initMatrices();
		initViewport();
		loadTextures(); // can we load textures after shaders or will it break?
		
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
		String[] attributeLocations = new String[] {"in_Position", "in_Color", "in_TextureCoord"};
		sp.bindAttributeLocations(attributeLocations);
		
		spId = sp.getId();
		projectionMatrixLocation = glGetUniformLocation(spId, "projectionMatrix");
		viewMatrixLocation = glGetUniformLocation(spId, "viewMatrix");
		modelMatrixLocation = glGetUniformLocation(spId, "modelMatrix");
		
		glUseProgram(spId);
	}
	
	private void initMatrices() {
		initModelMatrix();
		initViewMatrix();
		initProjectionMatrix();
		initMatrixBuffer();
	}
	
	private void initProjectionMatrix() {
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
		
	private void loadTextures() {
		textureManager = TextureManager.getInstance();
		
		textureManager.load("cg.png");
		textureManager.load("generic-wheel.png");
		textureManager.load("vw-polo.png");
		textureManager.load("vw-polo-wheel.png");
		textureManager.load("road.png");
		textureManager.load("cardboardbox.png");
	}

	private void initTestMesh() {
		OBJLoader cardboardboxLoader = new OBJLoader("vw-polo.obj");
		cardboardboxLoader.debugOutput();

		float[] cardboardboxVertices = cardboardboxLoader.getExpandedPositions();
		float[] cardboardboxUVs      = cardboardboxLoader.getExpandedUnwraps();
		int[]  cardboardboxIndices   = cardboardboxLoader.getIndices();
		
		/*
		float[] vertices = new float[] {
				-0.5f, -0.5f, -2.5f, // Bottom Left
				 0.5f, -0.5f, -2.5f, // Bottom right
				-0.5f,  0.5f, -2.5f, // Top Left
				 0.5f,  0.5f, -2.5f  // Top Right
		};
		
		float[] colors = new float[] {
				 0.5f,  0.5f,  0.5f,
			     0.5f,  0.5f,  0.5f,
			     0.5f,  0.5f,  0.5f, 
			     0.5f,  0.5f,  0.5f
		};
		
		float[] uvs = new float[] {
				0.0f, 1.0f,
				1.0f, 1.0f,
				0.0f, 0.0f,
				1.0f, 0.0f
		};
		
		byte[] indices = new byte[] {
				0, 1, 2,
				2, 1, 3
		};
		*/
		float[] vertices = cardboardboxVertices;
		float[] uvs = cardboardboxUVs;
		int[] indices = cardboardboxIndices;
		
		IntBuffer indicesBuffer = BufferUtils.createIntBuffer(indices.length);
		indicesBuffer.put(indices);
		indicesBuffer.flip();
		indicesLength = indices.length;

		indicesVBOId = glGenBuffers();
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, indicesVBOId);
		glBufferData(GL_ELEMENT_ARRAY_BUFFER, indicesBuffer, GL_STATIC_DRAW);
			
		VertexArrayObject vao = new VertexArrayObject();
		vaoId = vao.getId();
		/*
		vaoId = glGenVertexArrays();									// generate a VAO and remember its ID
		glBindVertexArray(vaoId);										// bind the freshly created VAO (with ID 'vaoId')
		*/
				
		VertexBufferObject vbo = new VertexBufferObject(vertices);
//		VertexBufferObject colorVBO = new VertexBufferObject(colors);
		VertexBufferObject uvVBO = new VertexBufferObject(uvs);
		/*
		vboId = glGenBuffers();											// generate a VBO and remember its ID
		glBindBuffer(GL_ARRAY_BUFFER, vboId);							// bind the freshly created VBO (with ID 'vboId')
		glBufferData(GL_ARRAY_BUFFER, triangleBuffer, GL_STATIC_DRAW);	// explain to OpenGL how our VBO data is structured
		*/
		
		vao.addVBO(vbo, ShaderAttribute.GEOMETRY.getLocation(), 3);
		VertexBufferObject.unbind();
//		vao.addVBO(colorVBO, ShaderAttribute.COLOR.getLocation(), 3);
//		VertexBufferObject.unbind();
		vao.addVBO(uvVBO, ShaderAttribute.TEXTURE.getLocation(), 2);
		VertexBufferObject.unbind();
		/*
		glVertexAttribPointer(ATTR_LOCATION_GEOMETRY, 3, GL_FLOAT, false, 0, 0); 			
		glEnableVertexAttribArray(ATTR_LOCATION_GEOMETRY);			// Enable VAO's first (0) VBO object (?)
		*/
		
		VertexArrayObject.unbind();
		/*
		glBindBuffer(GL_ARRAY_BUFFER, 0);								// unbind VBO
		glBindVertexArray(0);											// unbind VAO
		*/
		
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
		//glActiveTexture(GL_TEXTURE0); // Why is this (apparently not) necessary?
		glBindTexture(GL_TEXTURE_2D, textureManager.getId("vw-polo.png"));
		
		glBindVertexArray(vaoId);
		glEnableVertexAttribArray(0);
		glEnableVertexAttribArray(1);
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, indicesVBOId);
		glDrawElements(GL_TRIANGLES, indicesLength, GL_UNSIGNED_INT, 0);
		
		// VertexArrayObject.render(vaoId);
	}
	
	private void modifyModelMatrix() {
		// What we want: SCALE, ROTATE, TRANS
		// What we do  : TRANS, ROTATE, SCALE
		float transX = buttonLeft  ? -0.02f * delta : 0f;
			  transX = buttonRight ?  0.02f * delta : transX;
			  
		float transY = buttonUp    ?  0.02f * delta : 0f;
			  transY = buttonDown  ? -0.02f * delta : transY;
			  
		float transZ = buttonZoomIn  ? -0.02f * delta : 0f;
			  transZ = buttonZoomOut ?  0.02f * delta : transZ;
		
		// TODO We need some pushing and popping here, otherwise rotating+translating doesn't work as expected
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
			        	buttonUp = true;
			        	break;
			        case Keyboard.KEY_RIGHT:
			        	buttonRight = true;
			        	break;
			        case Keyboard.KEY_DOWN:
			        	buttonDown = true;
			        	break;
			        case Keyboard.KEY_LEFT:
			        	buttonLeft = true;
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
			        	buttonUp = false;
			        	break;
			        case Keyboard.KEY_RIGHT:
			        	buttonRight = false;
			        	break;
			        case Keyboard.KEY_DOWN:
			        	buttonDown = false;
			        	break;
			        case Keyboard.KEY_LEFT:
			        	buttonLeft = false;
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
