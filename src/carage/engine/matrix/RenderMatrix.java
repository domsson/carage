package carage.engine.matrix;

import static org.lwjgl.opengl.GL20.glUniformMatrix4;

import java.nio.FloatBuffer;

import lenz.opengl.utils.ShaderProgram;

import org.lwjgl.BufferUtils;
import org.lwjgl.util.vector.Matrix4f;

@SuppressWarnings("serial")
public class RenderMatrix extends Matrix4f {

	protected String name = "";
	protected int location = -1;
	protected int shaderId = 0;
	
	protected boolean transpose = false;
	
	protected FloatBuffer buffer = null;
	
	public RenderMatrix(String name) {
		this.name = name;
	}
	
	public RenderMatrix(Matrix4f src, String name) {
		super(src);
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
	
	/**
	 * Tells whether this Matrix has (already) created its own buffer.
	 * You can prevent the creation of an internal buffer by always providing an existing buffer when calling toShader(buffer).
	 * @return true if this matrix already created its own buffer, otherwise false
	 */
	public boolean hasBuffer() {
		return (buffer != null);
	}
	
	public void setTranspose(boolean transpose) {
		this.transpose = transpose;
	}
	
	/**
	 * Place to update the matrix. Overwrite this if your matrix extends RenderMatrix
	 * and needs certain operations done before this matrix is being send to the shader.
	 * By default, this does nothing.
	 */
	public void update() {
	
	}
	
	public void sendToShader(ShaderProgram shader, FloatBuffer buffer) {
		update();
		store(buffer);
        buffer.flip();
        glUniformMatrix4(shader.getUniformLocation(name), transpose, buffer);
		buffer.clear();
	}
	
	public void sendToShader(ShaderProgram shader) {
		if (!hasBuffer()) {
			initBuffer();
		}
		sendToShader(shader, buffer);
	}
	
	/**
	 * Create a buffer which will be used whenever this matrix is requested to send itself to the active shader.
	 */
	private void initBuffer() {
		buffer = BufferUtils.createFloatBuffer(16);
	}
	
}