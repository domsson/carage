package carage.engine;

// Normal Matrix = Transponierte und invertierte (Reihenfolge egal) ModelView Matrix!
@SuppressWarnings("serial")
public class NormalMatrix extends RenderMatrix {
	
	public static final String DEFAULT_NAME = "normalMatrix";
	
	private ModelViewMatrix modelViewMatrix = null;
	
	public NormalMatrix() {
		super(DEFAULT_NAME);
	}
	
	public NormalMatrix(ModelViewMatrix modelViewMatrix) {
		super(DEFAULT_NAME);
		setModelViewMatrix(modelViewMatrix);
		setTranspose(true);
	}
	
	public NormalMatrix(String name) {
		super(name);
		setTranspose(true);
	}
	
	public NormalMatrix(String name, ModelViewMatrix modelViewMatrix) {
		super(name);
		setModelViewMatrix(modelViewMatrix);
		setTranspose(true);
	}
	
	public void setModelViewMatrix(ModelViewMatrix modelViewMatrix) {
		// Important: by reference!
		this.modelViewMatrix = modelViewMatrix;
	}
	
	/**
	 * Updates this normal matrix by loading it from the referenced model-view matrix.
	 * If no reference to a model-view matrix has been set, an identity matrix will be used instead.
	 */
	public void update() {
		if (modelViewMatrix == null) {
			setModelViewMatrix((ModelViewMatrix) (new ModelViewMatrix()).setIdentity());
		}
		this.load(modelViewMatrix);
		invert();
	}
	
	/**
	 * Sets the internal reference to the model-view matrix to the provided matrix,
	 * then updates this normal matrix by loading it from the model-view matrix.
	 * @param modelViewMatrix The model-view matrix currently used for rendering
	 */
	public void update(ModelViewMatrix modelViewMatrix) {
		setModelViewMatrix(modelViewMatrix);
		update();
	}
}
