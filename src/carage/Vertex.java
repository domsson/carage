package carage;

import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

public class Vertex {

	private Vector3f position = new Vector3f(0, 0, 0);
	//private Vector3f normal   = new Vector3f(1, 0, 0);	// TODO what are good default values?
	private Vector2f unwrap   = new Vector2f(0, 0);
	
	public Vertex() {
		
	}
	
	public Vertex(Vector3f position, Vector2f unwrap) {
		this.position = position;
		this.unwrap = unwrap;
	}
	
	public boolean equals(Object obj) {
		if (!obj.getClass().getName().endsWith("Vertex")) {	return false; }

		Vertex otherVert = (Vertex) obj;
		// Compare position
		Vector3f otherPos = otherVert.getPosition();
		if (position.getX() != otherPos.getX()) { return false; }
		if (position.getY() != otherPos.getY()) { return false; }
		if (position.getZ() != otherPos.getZ()) { return false; }
		// Compare Unwrap
		Vector2f otherUnwrap = otherVert.getUnwrap();
		if (unwrap.getX() != otherUnwrap.getX()) { return false; }
		if (unwrap.getY() != otherUnwrap.getY()) { return false; }
		// All clear
		return true;
	}
	
	public Vertex(float[] position) {
	}
	
	public void setPosition(Vector3f position) {
		this.position = position;
	}
	
	public void setXYZ(float[] xyz) {
		position.x = xyz[0];
		position.y = xyz[1];
		position.z = xyz[2];
	}
	
	public void setUnwrap(Vector2f texture) {
		this.unwrap = texture;
	}
	
	public void setUV(float[] uv) {
		unwrap.x = uv[0];
		unwrap.y = uv[1];
	}
	
	public Vector3f getPosition() {
		return new Vector3f( position.getX(), position.getY(), position.getZ() );
	}
	
	public float[] getXYZ() {
		return new float[] { position.getX(), position.getY(), position.getZ() };
	}
	
	public float[] getXYZW() {
		return new float[] { position.getX(), position.getY(), position.getZ(), 1f };
	}
	
	public Vector2f getUnwrap() {
		return new Vector2f( unwrap.getX(), unwrap.getY() );
	}
	
	public float[] getUV() {
		return new float[] { unwrap.getX(), unwrap.getY() };
	}
	
	public float[] getUVW() {
		return new float[] { unwrap.getX(), unwrap.getY(), 0f };
	}
}
