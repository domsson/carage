package carage.engine;

import java.nio.FloatBuffer;
import static org.lwjgl.opengl.GL20.glUniformMatrix4;

import org.lwjgl.util.vector.Matrix4f;

// NORMAL MATRIX = MODELVIEW MATRIX transponiert, invertiert (reihenfolge ist wurst)
public class NormalMatrix extends RenderMatrix {
	
	public static final String DEFAULT_NAME = "normalMatrix";
	
	public NormalMatrix() {
		super(DEFAULT_NAME);
	}
	
	public NormalMatrix(String name) {
		super(name);
	}
	
	public void loadFromModelAndViewMatrices(Matrix4f modelMatrix, Matrix4f viewMatrix) {
		Matrix4f.mul(viewMatrix, modelMatrix, this);
	}
	
	public void toShader(FloatBuffer buffer) {
		invert();
		store(buffer);
        buffer.flip();
        glUniformMatrix4(location, true, buffer);
		buffer.clear();
	}
}
