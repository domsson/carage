package carage.engine;

import lenz.opengl.utils.ShaderProgram;
import lenz.opengl.utils.Texture;

public class Asset extends Entity implements Renderable {
	
	// TODO should this go into the TextureManager? But then we could only ever load png files...
	public static final String TEXTURE_FORMAT = "png";
	
	private String name = "";
	//private String resource = ""; // TODO maybe remember the resource name separately? this way, two assets with same resource can have different names
	private Texture texture = null;
	private Geometry geometry = null;
	private Material material = null;
	
	private TextureManager textureManager = TextureManager.getInstance();
	private GeometryManager geometryManager = GeometryManager.getInstance();
	
	public Asset(String resource) {
		super();
		// TODO fail (throw exception?) if resource is empty string
		this.name = resource;
		loadGeometry(resource);
		loadTexture(resource);
	}
	
	public Asset(String resource, Material material) {
		super();
		// TODO fail (throw exception?) if resource is empty string
		this.name = resource;
		loadGeometry(resource);
		loadTexture(resource);
		setMaterial(material);
	}
	
	public Asset(Geometry geometry) {
		super();
		this.geometry = geometry;
	}
	
	public Asset(Geometry geometry, Texture texture) {
		super();
		this.geometry = geometry;
		this.texture = texture;
	}
	
	public Asset(Geometry geometry, Texture texture, Material material) {
		super();
		this.geometry = geometry;
		this.texture = texture;
		this.material = material;
	}
	
	public boolean hasName() {
		return (!name.isEmpty());
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
	
	public void loadTexture(String resource) {
		texture = textureManager.get(resource+"."+TEXTURE_FORMAT);
	}
	
	public void setMaterial(Material material) {
		this.material = material;
	}
	
	public boolean hasMaterial() {
		return (material != null);
	}
	
	public Material getMaterial() {
		return material;
	}
	
	public ShaderProgram getShader() {
		return (hasMaterial()) ? getMaterial().getShader() : null;
	}
	
	public VertexArrayObject getVAO() {
		return geometry.getVAO();
	}
	
	public int getVAOId() {
		return geometry.getVAOId();
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

}
