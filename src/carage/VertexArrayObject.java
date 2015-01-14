package carage;

import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11.glDrawArrays;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;
import static org.lwjgl.opengl.GL30.glDeleteVertexArrays;

import java.util.HashMap;

import lenz.opengl.utils.Texture;

public class VertexArrayObject {

	private int id = 0;
	
	// TODO make this more generic (add whatever VBO you want, give it whatever name you want)
	// TODO brauchen wir nicht! (nicht?)
	private HashMap<Integer, VertexBufferObject> vbos = new HashMap<>();
	
	private int geometryVBO = -1; // v
	private int colorsVBO   = -1; // vc
	private int normalsVBO  = -1; // vn
	private int textureVBO  = -1; // vt
	private int indicesVBO  = -1; // i
	
	public VertexArrayObject() {
		id = glGenVertexArrays();	// generate a VAO and remember its ID
		glBindVertexArray(id);		// bind the freshly created VAO (with ID 'vaoId')
	}
	
	public void addVBO(VertexBufferObject vbo, int attributeLocation, int chunkSize) {
		vbos.put(attributeLocation, vbo);
		// TODO bind VBO here, maybe?
		vbo.bind();
		glVertexAttribPointer(attributeLocation, chunkSize, GL_FLOAT, false, 0, 0);
		glEnableVertexAttribArray(attributeLocation);	// Enable the new VBO
	}
	
	public VertexBufferObject getVBO(int attributeLocation) {
		return vbos.get(attributeLocation);
	}
	
	public void bind() {
		glBindVertexArray(id);
	}
	
	public static void bind(int id) {
		glBindVertexArray(id);
	}
	
	public static void unbind() {
		glBindVertexArray(0);
	}
	
	public int getId() {
		return id;
	}
	
	public static void delete(int id) {
		glDeleteVertexArrays(id);
	}
	
	public void delete() {
		 glDeleteVertexArrays(id);
	}
	
	// TODO this probably shouldn't be in this class. maybe Entity, Mesh, Object3D, ...?
	public static void render(int vaoId) {
		glBindVertexArray(vaoId);
		glDrawArrays(GL_TRIANGLES, 0, 3);
	    glBindVertexArray(0);
	}
	
	public void render() {
		glBindVertexArray(id);
		glDrawArrays(GL_TRIANGLES, 0, 3);
	    glBindVertexArray(0);
	}
	
}
