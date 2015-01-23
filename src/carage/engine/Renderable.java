package carage.engine;

import lenz.opengl.utils.Texture;

import org.lwjgl.util.vector.Matrix4f;

public interface Renderable {

	public VertexArrayObject getVAO();
	public int getVAOId();
	
	public boolean hasIBO();
	public IndexBufferObject getIBO();
	public int getIBOId();
	
	public boolean hasTexture();
	public Texture getTexture();
	public int getTextureId();
	
	public Matrix4f getModelMatrix();
	public void applyTransformationsToMatrix(Matrix4f modelMatrix);
	
}
