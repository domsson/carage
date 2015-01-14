package carage;

import static org.lwjgl.opengl.GL15.GL_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.GL_STATIC_DRAW;
import static org.lwjgl.opengl.GL15.glBindBuffer;
import static org.lwjgl.opengl.GL15.glBufferData;
import static org.lwjgl.opengl.GL15.glDeleteBuffers;
import static org.lwjgl.opengl.GL15.glGenBuffers;

import java.nio.FloatBuffer;
import java.util.ArrayList;

import org.lwjgl.BufferUtils;

// TODO NTH: The option to create an empty VBO, then add data (vertices) one by one (or bulk), then "finalize" to turn data into buffer

public class VertexBufferObject {

	private int id = 0;
	private int size = 0;
	private int drawType = GL_STATIC_DRAW;
	private ArrayList<Float> data = null;
	private FloatBuffer buffer = null;
	
	public VertexBufferObject(float[] data) {
		size = data.length;
		if(size > 0) {
			createBuffer(data);
		}
	}
	
	public VertexBufferObject() {
		data = new ArrayList<Float>();
	}
	
	public void addData(float value) {
		// can't add new data once buffer has been created
		if (buffer != null) {
			// TODO throw exception or otherwise notify about the issue
			return;
		}
		
		data.add(value);
	}
	
	public void createBuffer() {
		if (data.size() > 0) {
			createBuffer(dataAsArray());
		}
	}
	
	private float[] dataAsArray() {
		float[] dataArray = new float[data.size()];
		int i = 0;
		for (float value : data) {
			dataArray[i++] = value;
		}
		return dataArray;
	}
	
	private void createBuffer(float[] data) {
		// http://stackoverflow.com/questions/10697161/why-floatbuffer-instead-of-float
		buffer = BufferUtils.createFloatBuffer(data.length);
		buffer.put(data);
		buffer.flip();
		generateId();
		bind();
	}
	
	private void generateId() {
		id = glGenBuffers();
	}
	
	// TODO RENAME~!
	public void bind() {
		glBindBuffer(GL_ARRAY_BUFFER, id);					// bind the freshly created VBO (with ID 'vboId')
		glBufferData(GL_ARRAY_BUFFER, buffer, drawType);	// explain to OpenGL how our VBO data is structured
	}
	
	public void bind(int differentDrawType) {
		// TODO check if differentDrawType is a valid draw type
		glBindBuffer(GL_ARRAY_BUFFER, id);							// bind the freshly created VBO (with ID 'vboId')
		glBufferData(GL_ARRAY_BUFFER, buffer, differentDrawType);	// explain to OpenGL how our VBO data is structured	
	}
	
	public static void unbind() {
		glBindBuffer(GL_ARRAY_BUFFER, 0);
	}
	
	public void setDrawType(int drawType) {
		// TODO check if drawType is a valid draw type
		this.drawType = drawType;
	}
	
	public int getDrawType() {
		return drawType;
	}
	
	public int getId() {
		return id;
	}
	
	public int getSize() {
		return size;
	}
	
	public void delete() {
		glDeleteBuffers(id);
	}
}
