package carage.engine;

public class Geometry {

	private VertexArrayObject vao = null;
	private IndexBufferObject ibo = null;
	private BoundingBox boundingBox = null;
	
	public Geometry(VertexArrayObject vao) {
		this.vao = vao;
	}
	
	public Geometry(VertexArrayObject vao, IndexBufferObject ibo) {
		this.vao = vao;
		this.ibo = ibo;
	}
	
	public Geometry(VertexArrayObject vao, IndexBufferObject ibo, BoundingBox boundingBox) {
		this.vao = vao;
		this.ibo = ibo;
		this.boundingBox = boundingBox;
	}
	
	public VertexArrayObject getVAO() {
		return vao;
	}
	
	public int getVAOId() {
		return vao.getId();
	}
	
	public void setIBO(IndexBufferObject ibo) {
		this.ibo = ibo;
	}
	
	public boolean hasIBO() {
		return (ibo != null);
	}
	
	public IndexBufferObject getIBO() {
		return ibo;
	}
	
	public int getIBOId() {
		return (hasIBO()) ? ibo.getId() : 0;
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
