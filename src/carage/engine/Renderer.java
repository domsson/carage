package carage.engine;

import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_INT;
import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL11.glDrawArrays;
import static org.lwjgl.opengl.GL11.glDrawElements;
import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13.glActiveTexture;
import static org.lwjgl.opengl.GL30.glBindVertexArray;

import java.nio.FloatBuffer;

import lenz.opengl.utils.ShaderProgram;

import org.lwjgl.BufferUtils;
import org.lwjgl.util.vector.Matrix4f;

// TODO make this static or singleton or something? i guess? or maybe not?!
public class Renderer {
	
	public static final String DEFAULT_SHADER = "phong";
	public static final int DEFAULT_WIDTH = 800;
	public static final int DEFAULT_HEIGHT = 600;
	public static final float DEFAULT_NEAR_PLANE = 0.1f;
	public static final float DEFAULT_FAR_PLANE = 100f;
	public static final float DEFAULT_FIELD_OF_VIEW = 60f;
	
	private int width;
	private int height;
	
	private Camera camera;

	private ProjectionMatrix projectionMatrix;
	private ViewMatrix viewMatrix;
	private ModelMatrix modelMatrix;
	private NormalMatrix normalMatrix;
	private RenderMatrix modelViewMatrix; // new
	private RenderMatrix modelViewProjectionMatrix; // new
	
	private ShaderManager shaderManager = ShaderManager.getInstance();
	private FloatBuffer matrixBuffer;
	
	public Renderer() {
		this.width = DEFAULT_WIDTH;
		this.height = DEFAULT_HEIGHT;
	}
	
	public Renderer(int width, int height) {
		this.width = width;
		this.height = height;
		initMatrices();
	}
	
	
	public Renderer(int width, int height, Camera camera) {
		this.width = width;
		this.height = height;
		this.camera = camera;
		initMatrices();
	}
	
	public void setProjectionMatrix(ProjectionMatrix projectionMatrix) {
		this.projectionMatrix = projectionMatrix;
	}
	
	private void prepareAssetRendering(Renderable asset) {
		asset.applyTransformationsToMatrix(modelMatrix);	// Fill ModelMatrix with Asset transforms
		ensureAssetHasMaterial(asset);						// If Asset has no Material: assign default
		ShaderProgram assetShader = asset.getShader();		// Get the Shader from the Asset's Material
		assetShader.bind();									// Set the Shader as active in the state machine
		sendMatricesToShader(assetShader);					// Send the Matrices as Uniforms to the Shader
		asset.getMaterial().sendToShader();					// Send the Material's properties to the Shader
	}
	
	private void performAssetRendering(Renderable asset) {
		glActiveTexture(GL_TEXTURE0);						// This is the default and we're okay with it
		glBindTexture(GL_TEXTURE_2D, asset.getTextureId());	// Bind the Asset's Texture to the state machine
		renderVAO(asset.getVAO(), asset.getIBO());			// Render the Asset's Geometry via VAO & IBO
		glBindTexture(GL_TEXTURE_2D, 0);					// Unbind the Texture to have a clean state
	}
	
	public void renderAsset(Renderable asset) {
		modelMatrix.setIdentity();
		prepareAssetRendering(asset);
		performAssetRendering(asset);		
		// glUseProgram(0);
	}
	
	public void renderChildAsset(Renderable childAsset, Renderable parentAsset) {
		Matrix4f modelMatrixBackup = (new Matrix4f()).load(modelMatrix); 	// glPushMatrix
		prepareAssetRendering(childAsset);
		performAssetRendering(childAsset);
		// glUseProgram(0);
		modelMatrix.load(modelMatrixBackup);								// glPopMatrix
	}
	
	public void renderAssetGroup(AssetGroup assetGroup) {
		Asset parentAsset = assetGroup.getParentAsset();
		renderAsset(parentAsset);
		
		Asset[] childAssets = assetGroup.getAllChildAssets();
		for (Asset childAsset : childAssets) {
			renderChildAsset(childAsset, parentAsset);
		}
	}
	
	public static void renderVAO(int vaoId) {
		glBindVertexArray(vaoId);
		glDrawArrays(GL_TRIANGLES, 0, 3);
		glBindVertexArray(0);
	}
	
	public static void renderVAO(VertexArrayObject vao) {
		renderVAO(vao.getId());
	}
	
	public static void renderVAO(VertexArrayObject vao, IndexBufferObject ibo) {
		if (ibo == null) {
			renderVAO(vao);
			return;
		}
		vao.bind();
		ibo.bind();
		glDrawElements(GL_TRIANGLES, ibo.getSize(), GL_UNSIGNED_INT, 0);
		ibo.unbind();
		vao.unbind();
	}
	
	private void initMatrices() {
		initModelMatrix();
		initViewMatrix();
		initProjectionMatrix();
		initNormalMatrix(); // needs model and view matrix, hence has to be called after their initialization
		initMatrixBuffer();
		// new
		modelViewMatrix = new RenderMatrix("modelViewMatrix"); // new
		modelViewProjectionMatrix = new RenderMatrix("modelViewProjectionMatrix"); // new
	}
	
	private void initNormalMatrix() {
		normalMatrix = new NormalMatrix(modelMatrix, viewMatrix);
	}
	
	private void initProjectionMatrix() {
		int viewportWidth  = (width > 0)  ? width  : DEFAULT_WIDTH;
		int viewportHeight = (height > 0) ? height : DEFAULT_HEIGHT;
		projectionMatrix = new ProjectionMatrix(viewportWidth, viewportHeight, DEFAULT_NEAR_PLANE, DEFAULT_FAR_PLANE, DEFAULT_FIELD_OF_VIEW);
	}
	
	private void initViewMatrix() {
		viewMatrix = (camera == null) ? new ViewMatrix() : new ViewMatrix(camera);
	}
	
	private void initModelMatrix() {
		modelMatrix = new ModelMatrix();
	}
	
	private void initMatrixBuffer() {
		// http://stackoverflow.com/questions/10697161/why-floatbuffer-instead-of-float
		matrixBuffer = BufferUtils.createFloatBuffer(16);
	}
		
	private void sendMatricesToShader(ShaderProgram shader) {
		// projectionMatrix.sendToShader(shader, matrixBuffer); // TODO performance: this only needs to be send _if_ it has changed! how/where to check?
		viewMatrix.sendToShader(shader, matrixBuffer); // same
		modelMatrix.sendToShader(shader, matrixBuffer); // same
		
		Matrix4f.mul(viewMatrix, modelMatrix, modelViewMatrix); // new
		modelViewMatrix.sendToShader(shader, matrixBuffer); // new
		Matrix4f.mul(projectionMatrix, modelViewMatrix, modelViewProjectionMatrix); // new
		modelViewProjectionMatrix.sendToShader(shader, matrixBuffer); // new
		
		normalMatrix.sendToShader(shader, matrixBuffer); // same
	}
		
	private void ensureAssetHasMaterial(Renderable asset) {
		if (asset.hasMaterial()) { return; }
		asset.setMaterial(new Material("", shaderManager.get(DEFAULT_SHADER)));
	}

}
