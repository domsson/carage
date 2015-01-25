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
	
	private ShaderProgram shader; // TODO should the asset hold the shader it wants to use? probably? 
	
	private Camera camera;

	private ProjectionMatrix projectionMatrix;
	private ViewMatrix viewMatrix;
	private ModelMatrix modelMatrix;
	private NormalMatrix normalMatrix;
	
	private FloatBuffer matrixBuffer;
	
	public Renderer() {
		this.width = DEFAULT_WIDTH;
		this.height = DEFAULT_HEIGHT;
	}
	
	public Renderer(int width, int height) {
		this.width = width;
		this.height = height;
		this.shader = new ShaderProgram(DEFAULT_SHADER);
		initMatrices();
	}
	
	public Renderer(ShaderProgram sp, int width, int height) {
		this.width = width;
		this.height = height;
		this.shader = sp;
		initMatrices();
	}
	
	public Renderer(ShaderProgram sp, int width, int height, Camera camera) {
		this.width = width;
		this.height = height;
		this.shader = sp;
		this.camera = camera;
		initMatrices();
	}
	
	public void setProjectionMatrix(ProjectionMatrix projectionMatrix) {
		this.projectionMatrix = projectionMatrix;
		if (!projectionMatrix.hasLocation()) {
			projectionMatrix.fetchLocation(shader);
		}
	}
	
	public ShaderProgram getShaderProgram() {
		return shader;
	}
	
	public int getShaderProgramId() {
		return shader.getId();
	}
	
	public void renderAsset(Renderable asset) {
		modelMatrix.setIdentity();
		asset.applyTransformationsToMatrix(modelMatrix);
		matricesToShader();
		
		glActiveTexture(GL_TEXTURE0); // Why is this (apparently not) necessary? - Because GL_TEXTURE0 is the default!
		glBindTexture(GL_TEXTURE_2D, asset.getTextureId());
		renderVAO(asset.getVAO(), asset.getIBO());
		glBindTexture(GL_TEXTURE_2D, 0);
	}
	
	public void renderChildAsset(Renderable childAsset, Renderable parentAsset) {
		Matrix4f modelMatrixBackup = (new Matrix4f()).load(modelMatrix); // glPushMatrix()
		
		childAsset.applyTransformationsToMatrix(modelMatrix);
		matricesToShader();
		
		glActiveTexture(GL_TEXTURE0); // Why is this (apparently not) necessary? - Because GL_TEXTURE0 is the default!
		glBindTexture(GL_TEXTURE_2D, childAsset.getTextureId());
		renderVAO(childAsset.getVAO(), childAsset.getIBO());
		glBindTexture(GL_TEXTURE_2D, 0);
		
		modelMatrix.load(modelMatrixBackup); // glPopMatrix();
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
	}
	
	private void initNormalMatrix() {
		normalMatrix = new NormalMatrix(modelMatrix, viewMatrix);
		normalMatrix.fetchLocation(shader);
	}
	
	private void initProjectionMatrix() {
		int viewportWidth = (width > 0) ? width : DEFAULT_WIDTH;
		int viewportHeight = (height > 0) ? height : DEFAULT_HEIGHT;
		projectionMatrix = new ProjectionMatrix(viewportWidth, viewportHeight, DEFAULT_NEAR_PLANE, DEFAULT_FAR_PLANE, DEFAULT_FIELD_OF_VIEW);
		projectionMatrix.fetchLocation(shader);
	}
	
	private void initViewMatrix() {
		viewMatrix = (camera == null) ? new ViewMatrix() : new ViewMatrix(camera);
		viewMatrix.fetchLocation(shader);
	}
	
	private void initModelMatrix() {
		modelMatrix = new ModelMatrix();
		modelMatrix.fetchLocation(shader);
	}
	
	private void initMatrixBuffer() {
		// http://stackoverflow.com/questions/10697161/why-floatbuffer-instead-of-float
		matrixBuffer = BufferUtils.createFloatBuffer(16);
	}
	
	private void matricesToShader() {
		//glUseProgram(spId);
		projectionMatrix.toShader(matrixBuffer); // TODO performance: this only needs to be send _if_ it has changed! how/where to check?
		viewMatrix.toShader(matrixBuffer); // same
		modelMatrix.toShader(matrixBuffer); // same
		normalMatrix.toShader(matrixBuffer); // same
        //glUseProgram(0);
	}

}
