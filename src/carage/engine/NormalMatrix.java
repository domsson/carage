package carage.engine;

import java.nio.FloatBuffer;
import static org.lwjgl.opengl.GL20.glUniformMatrix4;

import org.lwjgl.util.vector.Matrix4f;

// Normal Matrix = Transponierte und invertierte (Reihenfolge egal) ModelView Matrix!
@SuppressWarnings("serial")
public class NormalMatrix extends RenderMatrix {
	
	public static final String DEFAULT_NAME = "normalMatrix";
	
	private ModelMatrix modelMatrix = null;
	private ViewMatrix viewMatrix = null;
	
	public NormalMatrix() {
		super(DEFAULT_NAME);
	}
	
	public NormalMatrix(ModelMatrix modelMatrix, ViewMatrix viewMatrix) {
		super(DEFAULT_NAME);
		setModelMatrix(modelMatrix);
		setViewMatrix(viewMatrix);
	}
	
	public NormalMatrix(String name) {
		super(name);
	}
	
	public NormalMatrix(String name, ModelMatrix modelMatrix, ViewMatrix viewMatrix) {
		super(name);
		setModelMatrix(modelMatrix);
		setViewMatrix(viewMatrix);
	}
	
	/**
	 * Set the internally referenced model matrix to the provided one.
	 * @param modelMatrix The model matrix currently used for rendering
	 */
	public void setModelMatrix(ModelMatrix modelMatrix) {
		// Important: by reference!
		this.modelMatrix = modelMatrix;
	}
	
	/**
	 * Sets the internally referenced view matrix to the provided one.
	 * @param viewMatrix The view matrix currently used for rendering
	 */
	public void setViewMatrix(ViewMatrix viewMatrix) {
		// Important: by reference!
		this.viewMatrix = viewMatrix;
	}
	
	/**
	 * Sets the internally references model and view matrices to the provided ones.
	 * @param modelMatrix The model matrix currently used for rendering
	 * @param viewMatrix The view matrix currently used for rendering
	 */
	public void setModelAndViewMatrices(ModelMatrix modelMatrix, ViewMatrix viewMatrix) {
		setModelMatrix(modelMatrix);
		setViewMatrix(viewMatrix);
	}
	
	/**
	 * Updates this normal matrix by multiplying the referenced model and view matrices.
	 * If no references to model and/or view matrices have been set, identity matrices will be used instead.
	 */
	public void update() {
//		if (viewMatrix.m00 == 1 && viewMatrix.m11 == 1 && viewMatrix.m22 == 1) { System.out.println("viewmatrix has no rotation/scale!"); }
//		if (modelMatrix.m00 == 1 && modelMatrix.m11 == 1 && modelMatrix.m22 == 1) { System.out.println("modelmatrix has no rotation/scale!"); }
		Matrix4f.mul(((viewMatrix == null) ? new Matrix4f() : viewMatrix), ((modelMatrix == null) ? new Matrix4f() : modelMatrix), this);
		invert();
	}
	
	/**
	 * Sets the internal reference to model and view matrix to the provided matrices,
	 * then updates this normal matrix by multiplying them (see update()).
	 * @param modelMatrix The model matrix currently used for rendering
	 * @param viewMatrix The view matrix currently used for rendering
	 */
	public void update(ModelMatrix modelMatrix, ViewMatrix viewMatrix) {
		setModelMatrix(modelMatrix);
		setViewMatrix(viewMatrix);
		update();
	}
	
	/**
	 * Updates and inverts this matrix, then sends a buffered and transposed version of it to the active OpenGL shader program.
	 */
	public void toShader(FloatBuffer buffer) {
		update();
		store(buffer);
        buffer.flip();
        glUniformMatrix4(location, true, buffer);
		buffer.clear();
	}
}
