package carage.engine;

import org.lwjgl.util.vector.Matrix4f;

@SuppressWarnings("serial")
public class ModelViewProjectionMatrix extends RenderMatrix {
	
	public static final String DEFAULT_NAME = "modelViewProjectionMatrix";
	
	private ModelViewMatrix modelViewMatrix = null;
	private ProjectionMatrix projectionMatrix = null;
	
	public ModelViewProjectionMatrix() {
		super(DEFAULT_NAME);
	}
	
	public ModelViewProjectionMatrix(ModelViewMatrix modelViewMatrix, ProjectionMatrix projectionMatrix) {
		super(DEFAULT_NAME);
		setModelViewMatrix(modelViewMatrix);
		setProjectionMatrix(projectionMatrix);
	}
	
	public ModelViewProjectionMatrix(String name) {
		super(name);
	}
	
	public ModelViewProjectionMatrix(String name, ModelViewMatrix modelViewMatrix, ProjectionMatrix projectionMatrix) {
		super(name);
		setModelViewMatrix(modelViewMatrix);
		setProjectionMatrix(projectionMatrix);
	}
	
	/**
	 * Set the internally referenced model matrix to the provided one.
	 * @param modelViewMatrix The model matrix currently used for rendering
	 */
	public void setModelViewMatrix(ModelViewMatrix modelViewMatrix) {
		// Important: by reference!
		this.modelViewMatrix = modelViewMatrix;
	}
	
	/**
	 * Sets the internally referenced view matrix to the provided one.
	 * @param projectionMatrix The view matrix currently used for rendering
	 */
	public void setProjectionMatrix(ProjectionMatrix projectionMatrix) {
		// Important: by reference!
		this.projectionMatrix = projectionMatrix;
	}
	
	/**
	 * Sets the internally references model and view matrices to the provided ones.
	 * @param modelViewMatrix The model matrix currently used for rendering
	 * @param projectionMatrix The view matrix currently used for rendering
	 */
	public void setModelViewAndProjectionMatrices(ModelViewMatrix modelViewMatrix, ProjectionMatrix projectionMatrix) {
		setModelViewMatrix(modelViewMatrix);
		setProjectionMatrix(projectionMatrix);
	}
	
	/**
	 * Updates this normal matrix by multiplying the referenced model and view matrices.
	 * If no references to model and/or view matrices have been set, identity matrices will be used instead.
	 */
	public void update() {
		Matrix4f.mul(((projectionMatrix == null) ? new Matrix4f() : projectionMatrix), ((modelViewMatrix == null) ? new Matrix4f() : modelViewMatrix), this);
	}
	
	/**
	 * Sets the internal reference to model and view matrix to the provided matrices,
	 * then updates this normal matrix by multiplying them (see update()).
	 * @param modelViewMatrix The model matrix currently used for rendering
	 * @param projectionMatrix The view matrix currently used for rendering
	 */
	public void update(ModelViewMatrix modelViewMatrix, ProjectionMatrix projectionMatrix) {
		setModelViewMatrix(modelViewMatrix);
		setProjectionMatrix(projectionMatrix);
		update();
	}
}
