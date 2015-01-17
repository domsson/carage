package carage.engine;

import lenz.opengl.utils.Texture;

public interface Renderable {

	public VertexArrayObject getVAO();
	public int getVAOId();
	
	public boolean hasIBO();
	public IndexBufferObject getIBO();
	public int getIBOId();
	
	public boolean hasTexture();
	public Texture getTexture();
	public int getTextureId();
	
}
