package carage.engine;

import java.util.HashMap;

import carage.utils.OBJLoader;

public class GeometryManager {
	
	public static final String GEOMETRY_FORMAT = "obj";
	
	private static GeometryManager instance = null;
	private HashMap<String, Geometry> geometries = new HashMap<>();

	private GeometryManager() {
		// I'm a Singleton. Get out!
	}
	
	public static GeometryManager getInstance() {
		if (instance == null) {
			instance = new GeometryManager();
		}
		return instance;
	}
	
	public Geometry load(String resource) {
		// Not yet loaded? Load and return the geometry (or null)!
		if (!geometries.containsKey(resource)) {
			return add(resource);
		}
		// Already there, just return the geometry!
		return geometries.get(resource);
	}
	
	public Geometry get(String resource) {
		return load(resource);
	}
	
	private Geometry add(String resource) {
		Geometry geometry = loadGeometry(resource);
		geometries.put(resource, geometry);
		return geometry;
	}
	
	// TODO move this (and the following three) over to Geometry itself? Or is this a good place for it?
	private Geometry loadGeometry(String resource) {
		OBJLoader objectLoader = new OBJLoader(resource+"."+GEOMETRY_FORMAT);
		return new Geometry(getVAO(objectLoader), getIBO(objectLoader), getBoundingBox(objectLoader));
	}
	
	private VertexArrayObject getVAO(OBJLoader objectLoader) {
		VertexArrayObject vao = new VertexArrayObject();
		vao.addVBO(new VertexBufferObject(objectLoader.getExpandedPositions(), 3), ShaderAttribute.POSITION);
		if (objectLoader.hasUnwraps()) {
			vao.addVBO(new VertexBufferObject(objectLoader.getExpandedUnwraps(), 2), ShaderAttribute.TEXTURE);
		}
		if (objectLoader.hasNormals()) {
			vao.addVBO(new VertexBufferObject(objectLoader.getExpandedNormals(), 3), ShaderAttribute.NORMALS);
		}
		return vao;
	}
	
	private IndexBufferObject getIBO(OBJLoader objectLoader) {
		return new IndexBufferObject(objectLoader.getIndices());
	}
	
	private BoundingBox getBoundingBox(OBJLoader objectLoader) {
		return new BoundingBox(objectLoader.getMinBoundaries(), objectLoader.getMaxBoundaries());
	}
	
}
