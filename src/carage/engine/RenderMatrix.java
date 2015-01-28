package carage.engine;

import static org.lwjgl.opengl.GL20.glGetUniformLocation;
import static org.lwjgl.opengl.GL20.glUniformMatrix4;
import static org.lwjgl.opengl.GL20.glUseProgram;

import java.nio.FloatBuffer;

import lenz.opengl.utils.ShaderProgram;

import org.lwjgl.BufferUtils;
import org.lwjgl.util.vector.Matrix4f;

@SuppressWarnings("serial")
public class RenderMatrix extends Matrix4f {

	protected String name = "";
	protected int location = -1;
	protected int shaderId = 0;
	
	protected FloatBuffer buffer = null;
	
	public RenderMatrix(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
	
	/**
	 * Fetch and return this matrix' uniform location from within the shader program with the given ID.
	 * @param sp The ID of the ShaderProgram to query for this matrix' uniform location
	 * @return The fetched uniform location or -1 if it couldn't be fetched
	 */
	public int fetchLocation(int shaderId) {
		location = glGetUniformLocation(shaderId, name);
		if (location != -1) { this.shaderId = shaderId; }
		return location;
	}
	
	/**
	 * Fetch and return this matrix' uniform location from within the given shader program.
	 * @param sp The ShaderProgram to query for this matrix' uniform location
	 * @return The fetched uniform location or -1 if it couldn't be fetched
	 */
	public int fetchLocation(ShaderProgram shader) {
		return fetchLocation(shader.getId());
	}
	
	/**
	 * Triggers a re-fetch of this matrix' uniform location if the given shader
	 * is different from the shader used previously, otherwise does nothing.
	 * @param shader The Shader Program to be used with this matrix afterwards
	 * @return The fetched uniform location or -1 if it couldn't be fetched
	 */
	public int updateLocation(ShaderProgram shader) {
		return (shaderId == shader.getId()) ? location : fetchLocation(shader);
	}
	
	/**
	 * Tells whether this Matrix had already acquired a uniform location from a shader program.
	 * @return true if this matrix knows its uniform location, otherwise false
	 */
	public boolean hasLocation() {
		return (location != -1);
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
	 * Place to update the matrix. Overwrite this if your matrix extends RenderMatrix
	 * and needs certain operations done before this matrix is being send to the shader.
	 * By default, this does nothing.
	 */
	public void update() {
	
	}
	
	/**
	 * Sends a buffered version of this matrix to the active OpenGL shader program.
	 * The provided buffer will be used, then cleared, allowing for buffer re-use.
	 */
	public void sendToShader(FloatBuffer buffer) {
		update();
		store(buffer);
        buffer.flip();
        glUniformMatrix4(location, false, buffer);
		buffer.clear();
	}
		
	/**
	 * Sends a buffered version of this matrix to the active OpenGL shader program.
	 */
	public void sendToShader() {
		if (!hasBuffer()) {
			initBuffer();
		}
		sendToShader(buffer);
	}
	
	/**
	 * Create a buffer which will be used whenever this matrix is requested to send itself to the active shader.
	 */
	private void initBuffer() {
		buffer = BufferUtils.createFloatBuffer(16);
	}
	
}