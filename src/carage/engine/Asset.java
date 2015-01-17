package carage.engine;

import lenz.opengl.utils.Texture;

public class Asset extends Entity implements Renderable {
	
	// TODO should this go into the TextureManager? But then we could only ever load png files...
	public static final String TEXTURE_FORMAT = "png";
	
	private String name = "";
	private Texture texture = null;
	private Geometry geometry = null;
	
	private TextureManager textureManager = TextureManager.getInstance();
	private GeometryManager geometryManager = GeometryManager.getInstance();
	
	public Asset(String resource) {
		super();
		// TODO fail (throw exception?) if resource is empty string
		this.name = resource;
		loadGeometry(resource);
		setTexture(resource);
	}
	
	public String getName() {
		return name;
	}
		
	public boolean hasTexture() {
		return (texture != null);
	}
	
	public Texture getTexture() {
		return textureManager.get(name);
	}
	
	public int getTextureId() {
		return texture.getId();
	}
	
	public VertexArrayObject getVAO() {
		return geometry.getVAO();
	}
	
	public int getVAOId() {
		return geometry.getVAOId();
	}
	
	public boolean hasIBO() {
		return geometry.hasIBO();
	}
	
	public IndexBufferObject getIBO() {
		return geometry.getIBO();
	}
	
	public int getIBOId() {
		return geometry.getIBOId();
	}
	
	public boolean hasBoundingBox() {
		return geometry.hasBoundingBox();
	}
	
	public BoundingBox getBoundingBox() {
		return geometry.getBoundingBox();
	}
	
	private void loadGeometry(String resource) {
		geometry = geometryManager.get(resource);
	}
	
	private void setTexture(String resource) {
		texture = textureManager.get(resource+"."+TEXTURE_FORMAT);
	}

}
