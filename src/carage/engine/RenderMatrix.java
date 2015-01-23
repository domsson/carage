package carage.engine;

import static org.lwjgl.opengl.GL20.glGetUniformLocation;
import static org.lwjgl.opengl.GL20.glUniformMatrix4;

import java.nio.FloatBuffer;

import lenz.opengl.utils.ShaderProgram;

import org.lwjgl.BufferUtils;
import org.lwjgl.util.vector.Matrix4f;

@SuppressWarnings("serial")
public class RenderMatrix extends Matrix4f {

	protected String name = "";
	protected int location = 0;
	
	protected FloatBuffer buffer = null;
	
	public RenderMatrix(String name) {
		this.name = name;
	}
	
	/**
	 * Fetch and return this matrix' uniform location from within the given shader program.
	 * @param sp The ShaderProgram to query for this matrix' uniform location
	 * @return The fetched uniform location or 0 if it couldn't be fetched
	 */
	public int fetchLocation(ShaderProgram sp) {
		return fetchLocation(sp.getId());
	}
	
	/**
	 * Fetch and return this matrix' uniform location from within the shader program with the given ID.
	 * @param sp The ID of the ShaderProgram to query for this matrix' uniform location
	 * @return The fetched uniform location or 0 if it couldn't be fetched
	 */
	public int fetchLocation(int spId) {
		location = glGetUniformLocation(spId, name);
		return location;
	}
	
	/**
	 * Tells whether this Matrix had already acquired a uniform location from a shader program.
	 * @return true if this matrix knows its uniform location, otherwise false
	 */
	public boolean hasLocation() {
		return (location > 0);
	}
	
	/**
	 * Get this matrix' uniform location for the shader program it has previously fetched it from.
	 * @return the uniform location or 0 if none has been fetched yet
	 */
	public int getLocation() {
		return location;
	}
	
	/**
	 * Tells whether this Matrix has (already) created its own buffer.
	 * You can prevent the creation of an internal buffer by always providing an existing buffer when calling toShader(buffer).
	 * @return true if this matrix already created its own buffer, otherwise false
	 */
	public boolean hasBuffer() {
		return (buffer != null);
	}
	
	/**
	 * Sends a buffered version of this matrix to the active OpenGL shader program.
	 * The provided buffer will be used, then cleared, allowing for buffer re-use.
	 */
	public void toShader(FloatBuffer buffer) {
		//glUseProgram(sp);
		store(buffer);
        buffer.flip();
        glUniformMatrix4(location, false, buffer);
		buffer.clear();
		//glUseProgram(0);
	}
	
	/**
	 * Sends a buffered version of this matrix to the active OpenGL shader program.
	 */
	public void toShader() {
		if (!hasBuffer()) {
			initBuffer();
		}
		toShader(buffer);
	}
	
	/**
	 * Create a buffer which will be used whenever this matrix is requested to send itself to the active shader.
	 */
	private void initBuffer() {
		buffer = BufferUtils.createFloatBuffer(16);
	}
	
}
