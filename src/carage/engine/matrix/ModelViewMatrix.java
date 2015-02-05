package carage.engine.matrix;

import org.lwjgl.util.vector.Matrix4f;


@SuppressWarnings("serial")
public class ModelViewMatrix extends RenderMatrix {
	
	public static final String DEFAULT_NAME = "modelViewMatrix";
	
	private ModelMatrix modelMatrix = null;
	private ViewMatrix viewMatrix = null;
	
	public ModelViewMatrix() {
		super(DEFAULT_NAME);
	}
	
	public ModelViewMatrix(ModelMatrix modelMatrix, ViewMatrix viewMatrix) {
		super(DEFAULT_NAME);
		setModelMatrix(modelMatrix);
		setViewMatrix(viewMatrix);
	}
	
	public ModelViewMatrix(String name) {
		super(name);
	}
	
	public ModelViewMatrix(String name, ModelMatrix modelMatrix, ViewMatrix viewMatrix) {
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
		Matrix4f.mul(((viewMatrix == null) ? new Matrix4f() : viewMatrix), ((modelMatrix == null) ? new Matrix4f() : modelMatrix), this);
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
}
