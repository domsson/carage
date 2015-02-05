package carage.engine.matrix;

import carage.engine.Camera;

@SuppressWarnings("serial")
public class ViewMatrix extends RenderMatrix {

	public static final String DEFAULT_NAME = "viewMatrix";
	
	private Camera camera = null;
	
	public ViewMatrix() {
		super(DEFAULT_NAME);
	}
	
	public ViewMatrix(Camera camera) {
		super(DEFAULT_NAME);
		setCamera(camera);
	}
	
	public ViewMatrix(String name) {
		super(name);
	}
	
	public ViewMatrix(String name, Camera camera) {
		super(name);
		setCamera(camera);
	}
	
	public void setCamera(Camera camera) {
		// Important: by reference!
		this.camera = camera;
	}
	
	public void update() {
		setIdentity();
		if (camera != null) {
			// http://3dgep.com/understanding-the-view-matrix/
			camera.applyTransformationsToMatrix(this);
			invert(); // The View Matrix is the inverse of the Camera's Transformation Matrix
		}
	}
	
	public void update(Camera camera) {
		setCamera(camera);
		update();
	}
	
}
