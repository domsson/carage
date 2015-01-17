package carage.engine;

import org.lwjgl.util.vector.Vector3f;

public class BoundingBox {
	
	private Vector3f min  = new Vector3f();	// smallest position values (for example: -0.5 for a 'unit cube')
	private Vector3f max  = new Vector3f(); // biggest position values (for example: +0.5 for a 'unit cube')
	private Vector3f size = new Vector3f();	// x=width, y=height, z=length
	
	public BoundingBox(Vector3f min, Vector3f max) {
		this.min = min;
		this.max = max;
		calcSize();
	}
			
	public Vector3f getSize() {
		return new Vector3f(size.getX(), size.getY(), size.getZ());
	}
	
	public float getSizeX() {
		return size.getX();
	}
	
	public float getSizeY() {
		return size.getY();
	}
	
	public float getSizeZ() {
		return size.getZ();
	}
	
	public Vector3f getMinBoundaries() {
		return new Vector3f(min.getX(), min.getY(), min.getZ());
	}
	
	public Vector3f getMaxBoundaries() {
		return new Vector3f(max.getX(), max.getY(), max.getZ());
	}
	
	public float getMinX() {
		return min.getX();
	}
	
	public float getMinY() {
		return min.getY();
	}
	
	public float getMinZ() {
		return min.getZ();
	}
	
	public float getMaxX() {
		return max.getX();
	}
	
	public float getMaxY() {
		return max.getY();
	}
	
	public float getMaxZ() {
		return max.getZ();
	}
	
	public float[] getBoundariesX() {
		return new float[] { getMinX(), getMaxX() };
	}
	
	public float[] getBoundariesY() {
		return new float[] { getMinY(), getMaxY() };
	}
	
	public float[] getBoundariesZ() {
		return new float[] { getMinZ(), getMaxZ() };
	}
		
	private void calcSize() {
		size.setX(max.getX() - min.getX());	// width
		size.setY(max.getY() - min.getY());	// height
		size.setZ(max.getZ() - min.getZ());	// length
	}

}
