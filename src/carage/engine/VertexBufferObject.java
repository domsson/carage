package carage.engine;


public class VertexBufferObject extends BufferObject {
	
	public static final int DEFAULT_CHUNK_SIZE = 3;	// Standard for Buffers holding vertices, texture vertices or vertex normals
	
	private int chunkSize = 0;

	public VertexBufferObject(float[] data, int chunkSize) {
		super(data);
		setChunkSize(chunkSize);
	}
	
	private void setChunkSize(int chunkSize) {
		this.chunkSize = (chunkSize > 0) ? chunkSize : DEFAULT_CHUNK_SIZE;
	}
	
	public int getChunkSize() {
		return chunkSize;
	}
	
}
