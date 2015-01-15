package carage;

import static org.lwjgl.opengl.GL15.GL_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.GL_ELEMENT_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.GL_STATIC_DRAW;
import static org.lwjgl.opengl.GL15.GL_DYNAMIC_DRAW;
import static org.lwjgl.opengl.GL15.GL_STREAM_DRAW;
import static org.lwjgl.opengl.GL15.glBindBuffer;
import static org.lwjgl.opengl.GL15.glBufferData;
import static org.lwjgl.opengl.GL15.glDeleteBuffers;
import static org.lwjgl.opengl.GL15.glGenBuffers;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import org.lwjgl.BufferUtils;

//https://www.khronos.org/opengles/sdk/1.1/docs/man/glBufferData.xml
// https://www.opengl.org/sdk/docs/man3/xhtml/glBufferData.xml
public class BufferObject {
	
	public static int DEFAULT_DRAW_TYPE = GL_STATIC_DRAW;
	public static int[] VALID_BUFFER_TYPES = { GL_ARRAY_BUFFER, GL_ELEMENT_ARRAY_BUFFER };
	public static int[] VALID_DRAW_TYPES = { GL_STATIC_DRAW, GL_DYNAMIC_DRAW, GL_STREAM_DRAW };
	
	protected int id = 0;
	protected int size = 0;
	protected int bufferType = 0;	// Can be GL_ELEMENT_ARRAY_BUFFER (IBO) or GL_ARRAY_BUFFER (VBO) and some more (see link above)
	protected int drawType = 0;		// Can be GL_STATIC_DRAW or GL_DYNAMIC_DRAW or GL_STREAM_DRAW and some more (see link above)
	
	/**
	 * Creates a BufferObject of the specified type and with default draw type.
	 * In order to make use of this BufferObject, its data has to be set via setData().
	 * @param bufferType A valid Buffer Type (see VALID_BUFFER_TYPES for a list of valid types)
	 */
	public BufferObject(int bufferType) {
		setBufferType(bufferType);
		setDrawType(DEFAULT_DRAW_TYPE);
	}
	
	/**
	 * Creates a BufferObject of the specified type and specified draw type.
	 * In order to make use of this BufferObject, its data has to be set via setData().
	 * @param bufferType A valid Buffer Type (see VALID_BUFFER_TYPES for a list of valid types)
	 * @param drawType A valid Draw Type (see VALID_DRAW_TYPES for a list of valid types)
	 */
	public BufferObject(int bufferType, int drawType) {
		setBufferType(bufferType);
		setDrawType(drawType);
	}
	
	/**
	 * Creates a BufferObject from integer data, assuming that it is supposed to be an IBO.
	 * @param data An array of type int
	 */
	public BufferObject(int[] data) {
		// We assume that integer data means that this is supposed to be an IBO
		setBufferType(GL_ELEMENT_ARRAY_BUFFER);
		setDrawType(DEFAULT_DRAW_TYPE);
		createBuffer(data);
	}

	/**
	 * Creates a BufferObject from float data, assuming that it is supposed to be a VBO
	 * @param data An array of type float
	 */
	public BufferObject(float[] data) {
		// We assume that float data means that this is supposed to be a VBO
		setBufferType(GL_ARRAY_BUFFER);
		setDrawType(DEFAULT_DRAW_TYPE);
		createBuffer(data);
	}
	
	/**
	 * Change the type of this Buffer. This will only have an effect if called before
	 * the Buffer's data has been set, otherwise it will do nothing.
	 * @param bufferType A valid Buffer Type (see VALID_BUFFER_TYPES for a list of valid types)
	 */
	public void setBufferType(int bufferType) {
		if (id == 0) {
			this.bufferType = (isValidBufferType(bufferType)) ? bufferType : 0;
		}
	}
	
	/**
	 * Change the draw type of this Buffer. This will only have an effect if called before
	 * the Buffer's data has been set, otherwise it will do nothing.
	 * @param drawType A valid Draw Type (see VALID_DRAW_TYPES for a list of valid types)
	 */
	public void setDrawType(int drawType) {
		if (id == 0) {
			this.drawType = (isValidDrawType(drawType)) ? drawType : DEFAULT_DRAW_TYPE;
		}
	}
	
	/**
	 * Set this buffer's data. This will trigger the actual creation of this Buffer.
	 * Once the data has been set, this Buffer will have an id and can be bound and drawn.
	 * If the data has been set before, this will do nothing.
	 * @param data An array of type int
	 */
	public void setData(int[] data) {
		if (id == 0) {
			createBuffer(data);
		}
	}
	
	/**
	 * Set this buffer's data. This will trigger the actual creation of this Buffer.
	 * Once the data has been set, this Buffer will have an id and can be bound and drawn.
	 * If the data has been set before, this will do nothing.
	 * @param data An array of type float
	 */
	public void setData(float[] data) {
		if (id == 0) {
			createBuffer(data);	
		}
	}
	
	/**
	 * Get this Buffer's id/name as registered with OpenGL.
	 * @return This Buffer's id (name)
	 */
	public int getId() {
		return id;
	}
	
	/**
	 * Get this Buffer's data size.
	 * @return This Buffer's data size
	 */
	public int getSize() {
		return size;
	}
	
	/**
	 * Get this Buffer's type.
	 * @return This Buffer's type
	 */
	public int getBufferType() {
		return bufferType;
	}
	
	/**
	 * Get this Buffer's draw type.
	 * @return This Buffer's draw type
	 */
	public int getDrawType() {
		return drawType;
	}
	
	/**
	 * Bind this Buffer in the OpenGL state machine in order to perform actions on/with it.
	 */
	public void bind() {
		glBindBuffer(bufferType, id);
	}
	
	/**
	 * Unbind this Buffer (as well as any other of the same type) from the OpenGL state machine.
	 */
	public void unbind() {
		glBindBuffer(bufferType, 0);
	}
	
	/**
	 * Delete this Buffer from OpenGL. This will not delete this object, just the actual Buffer from OpenGL.
	 */
	public void delete() {
		glDeleteBuffers(id);
	}
	
	/**
	 * Checks if the given Buffer Type is a valid buffer type in the sense that it is
	 * included in the VALID_BUFFER_TYPES array.
	 * @param bufferType An int value representing an OpenGL Buffer Type
	 * @return True if the given value is a valid Buffer Type, otherwise false
	 */
	protected boolean isValidBufferType(int bufferType) {
		for (int i=0; i<VALID_BUFFER_TYPES.length; ++i) {
			if (bufferType == VALID_BUFFER_TYPES[i]) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Checks if the given Draw Type is a valid draw type in the sense that it is
	 * included in the VALID_DRAW_TYPES array.
	 * @param drawType An int value representing an OpenGL Draw Type
	 * @return True if the given value is a valid Draw Type, otherwise false
	 */
	protected boolean isValidDrawType(int drawType) {
		for (int i=0; i<VALID_DRAW_TYPES.length; ++i) {
			if (drawType == VALID_DRAW_TYPES[i]) {
				return true;
			}
		}
		return false;
	}	
	
	/**
	 * Actually creates this Buffer from the given int data.
	 * This will register the buffer with OpenGL and send its data to OpenGL.
	 * Afterwards, this Buffer will have a valid id/name and can be used for drawing.
	 * @param data An array of type int
	 */
	protected void createBuffer(int[] data) {
		size = data.length;
		generateId();
		bind();
		sendBuffer(bufferFromData(data));
		unbind();
	}
	
	/**
	 * Actually creates this Buffer from the given float data.
	 * This will register the buffer with OpenGL and send its data to OpenGL.
	 * Afterwards, this Buffer will have a valid id/name and can be used for drawing.
	 * @param data An array of type float
	 */
	protected void createBuffer(float[] data) {
		size = data.length;
		generateId();
		bind();
		sendBuffer(bufferFromData(data));
		unbind();
	}
	
	/**
	 * Create an IntBuffer object from the given int data Array.
	 * @param data An array of type int; its size should be a multiple of 3
	 * @return An IntBuffer object holding the provided data
	 */
	protected IntBuffer bufferFromData(int[] data) {
		IntBuffer buffer = BufferUtils.createIntBuffer(data.length);
		buffer.put(data);
		buffer.flip();
		return buffer;
	}
	
	/**
	 * Create a FloatBuffer object from the given float dara Array.
	 * @param data An array of type float; its size should be a multiple of 3
	 * @return A FloatBuffer object holding the provided data
	 */
	protected FloatBuffer bufferFromData(float[] data) {
		FloatBuffer buffer = BufferUtils.createFloatBuffer(data.length);
		buffer.put(data);
		buffer.flip();
		return buffer;
	}
	
	/**
	 * Send the actual Buffer to OpenGL, initializing the data store.
	 * @param buffer An IntBuffer holding the actual data
	 */
	protected void sendBuffer(IntBuffer buffer) {
		glBufferData(bufferType, buffer, drawType);
	}
	
	/**
	 * Send the actual Buffer to OpenGL, initializing the data store.
	 * @param buffer A FloatBuffer holding the actual data
	 */
	protected void sendBuffer(FloatBuffer buffer) {
		glBufferData(bufferType, buffer, drawType);
	}
	
	/**
	 * Let OpenGL generate an id/name for this Buffer and remember it.
	 */
	protected void generateId() {
		id = glGenBuffers();
	}
	
}
