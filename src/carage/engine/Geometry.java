package carage.engine;

public class Geometry {

	protected VertexArrayObject vao = null;
	protected BoundingBox boundingBox = null;
	
	// This is dangerous... and only here for PlaneGeometry, CubeGeometry and so on
	public Geometry() {
	}
	
	public Geometry(VertexArrayObject vao) {
		this.vao = vao;
	}
	
	public Geometry(VertexArrayObject vao, BoundingBox boundingBox) {
		this.vao = vao;
		this.boundingBox = boundingBox;
	}
		
	// Dangeroooooous
	public void setVAO(VertexArrayObject vao) {
		this.vao = vao;
	}
	
	public boolean hasVAO() {
		return (vao != null);
	}
	
	public VertexArrayObject getVAO() {
		return vao;
	}
	
	public int getVAOId() {
		return vao.getId();
	}
	
	public void setBoundingBox(BoundingBox boundingBox) {
		this.boundingBox = boundingBox;
	}
	
	public boolean hasBoundingBox() {
		return (boundingBox != null);
	}
	
	public BoundingBox getBoundingBox() {
		return boundingBox;
	}
	
}
