package carage;

import org.lwjgl.util.vector.Matrix4f;

@SuppressWarnings("serial")
public class ProjectionMatrix extends Matrix4f {
	
	private int viewportWidth;
	private int viewportHeight;
	private float nearPlane;
	private float farPlane;
	private float fieldOfView;
	
	public ProjectionMatrix(int viewportWidth, int viewportHeight, float nearPlane, float farPlane, float fieldOfView) {
		this.viewportWidth = viewportWidth;
		this.viewportHeight = viewportHeight;
		this.nearPlane = nearPlane;
		this.farPlane = farPlane;
		this.fieldOfView = fieldOfView;		
		
		initMatrix();
	}
	
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
