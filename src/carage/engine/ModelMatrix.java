package carage.engine;

@SuppressWarnings("serial")
public class ModelMatrix extends RenderMatrix {

	public static final String DEFAULT_NAME = "modelMatrix";
	
	public ModelMatrix() {
		super(DEFAULT_NAME);
	}
	
	public ModelMatrix(String name) {
		super(name);
	}
	
}
