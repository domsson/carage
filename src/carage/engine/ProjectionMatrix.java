package carage.engine;

import org.lwjgl.util.vector.Matrix4f;

@SuppressWarnings("serial")
public class ProjectionMatrix extends RenderMatrix {
	
	public static final String DEFAULT_NAME = "projectionMatrix";
	
	private int viewportWidth;
	private int viewportHeight;
	private float nearPlane;
	private float farPlane;
	private float fieldOfView;
	
	public ProjectionMatrix(String name, int viewportWidth, int viewportHeight, float nearPlane, float farPlane, float fieldOfView) {
		super(name);
		this.viewportWidth = viewportWidth;
		this.viewportHeight = viewportHeight;
		this.nearPlane = nearPlane;
		this.farPlane = farPlane;
		this.fieldOfView = fieldOfView;		
		
		initMatrix();		
	}
	
	public ProjectionMatrix(int viewportWidth, int viewportHeight, float nearPlane, float farPlane, float fieldOfView) {
		super(DEFAULT_NAME);
		this.viewportWidth = viewportWidth;
		this.viewportHeight = viewportHeight;
		this.nearPlane = nearPlane;
		this.farPlane = farPlane;
		this.fieldOfView = fieldOfView;		
		
		initMatrix();
	}
	
	public int getViewportWidth() {
		return viewportWidth;
	}
	
	public int getViewportHeight() {
		return viewportHeight;
	}
	
	public float getNearPlane() {
		return nearPlane;
	}
	
	public float getFarPlane() {
		return farPlane;
	}
	
	public float getFieldOfView() {
		return fieldOfView;
	}
	
	// TODO make it possible to change matrix paramters (FOV, width, height, ...) after creation ('on the fly')
	
	private void initMatrix() {
		// TODO Have a close look at the math here and why/how it works exactly (it's copied from a LWJGL wiki tutorial)
		
		float aspectRatio = (float)viewportWidth / (float)viewportHeight;
		float yScale = (float) (1 / Math.tan(Math.toRadians(fieldOfView * 0.5)));
		float xScale = yScale / aspectRatio;
		float frustumLength = farPlane - nearPlane;
		 
		this.m00 = xScale;
		this.m11 = yScale;
		this.m22 = -((farPlane + nearPlane) / frustumLength);
		this.m23 = -1;
		this.m32 = -((2 * nearPlane * farPlane) / frustumLength);
		this.m33 = 0;
	}

}
