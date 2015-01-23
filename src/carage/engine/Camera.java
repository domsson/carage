package carage.engine;

import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

// TODO implement camera, hue hue hue
public class Camera extends Entity {

	public Camera() {
		// TODO
	}
	
	public void lockTo(Entity entity) {
		// TODO
	}
	
	public void lookAt(Entity entity) {
		// TODO
	}
	
	public void lookAt(Vector3f point) {
		// TODO
	}
	
	public void applyTransformationsToMatrix(Matrix4f modelMatrix) {
		// This is the camera - negate ALL the transformations!
		modelMatrix.translate(new Vector3f(-position.getX(), -position.getY(), -position.getZ()));
		modelMatrix.rotate(-rotation.getX(), new Vector3f(1, 0, 0));
		modelMatrix.rotate(-rotation.getY(), new Vector3f(0, 1, 0));
		modelMatrix.rotate(-rotation.getZ(), new Vector3f(0, 0, 1));
		// modelMatrix.scale(new Vector3f(1, 1, 1));	// TODO obviously...
	}
}
