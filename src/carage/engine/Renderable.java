package carage.engine;

import lenz.opengl.utils.ShaderProgram;
import lenz.opengl.utils.Texture;

import org.lwjgl.util.vector.Matrix4f;

public interface Renderable {

	public VertexArrayObject getVAO();
	public int getVAOId();
	
	public boolean hasTexture();
	public Texture getTexture();
	public int getTextureId();
	
	public Matrix4f getTransformationMatrix();
	public void applyTransformationsToMatrix(Matrix4f modelMatrix);
	
	public void setMaterial(Material material);
	public boolean hasMaterial();
	public Material getMaterial();
	
	public ShaderProgram getShader();
	
}
