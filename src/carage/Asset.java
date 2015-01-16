package carage;

public class Asset {

	private String name = "";
	private int textureId = 0;
	private VertexArrayObject vao = null;
	
	private TextureManager textureManager = TextureManager.getInstance();
	
	public Asset(String resource) {
		this.name = resource;
		loadModel(resource);
		loadTexture(resource);
	}
	
	private void loadModel(String resource) {
		OBJLoader objectLoader = new OBJLoader(resource+".obj");
		objectLoader.debugOutput();
		vao = new VertexArrayObject();

		// Positions
		vao.addVBO(new VertexBufferObject(objectLoader.getExpandedPositions(), 3), ShaderAttribute.POSITION);
		// Unwraps
		if (objectLoader.hasUnwraps()) {
			vao.addVBO(new VertexBufferObject(objectLoader.getExpandedUnwraps(), 2), ShaderAttribute.TEXTURE);
		}
		// Normals
		if (objectLoader.hasNormals()) {
			vao.addVBO(new VertexBufferObject(objectLoader.getExpandedNormals(), 3), ShaderAttribute.NORMALS);
		}
		// Indices
		vao.setIBO(new IndexBufferObject(objectLoader.getIndices()));
	}
	
	private void loadTexture(String resource) {
		textureId = textureManager.getId(resource+".png");
	}
	
	public String getName() {
		return name;
	}
	
	public int getTextureId() {
		return textureId;
	}
	
	public VertexArrayObject getVAO() {
		return vao;
	}
	
}
