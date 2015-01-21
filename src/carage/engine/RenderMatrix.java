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
	
	public int fetchLocation(ShaderProgram sp) {
		return fetchLocation(sp.getId());
	}
	
	public int fetchLocation(int spId) {
		location = glGetUniformLocation(spId, name);
		return location;
	}
	
	public boolean hasLocation() {
		return (location > 0);
	}
	
	public int getLocation() {
		return location;
	}
	
	public boolean hasBuffer() {
		return (buffer != null);
	}
	
	public void toShader(FloatBuffer buffer) {
//		glUseProgram(sp);
		store(buffer);
        buffer.flip();
        glUniformMatrix4(location, false, buffer);
		buffer.clear();
//		glUseProgram(0);
	}
	
	/*
	public void toShader(FloatBuffer buffer, boolean transpose, boolean invert) {
		if (invert) { invert(); }
		store(buffer);
        buffer.flip();
        glUniformMatrix4(location, transpose, buffer);
		buffer.clear();
	}
	*/
	
	public void toShader() {
		if (!hasBuffer()) {
			initBuffer();
		}
		toShader(buffer);
	}
	
	private void initBuffer() {
		buffer = BufferUtils.createFloatBuffer(16);
	}
	
}
