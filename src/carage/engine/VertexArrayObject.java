package carage.engine;

import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glDeleteVertexArrays;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;

import java.util.EnumMap;

public class VertexArrayObject {

	private int id = 0;	
	private EnumMap<ShaderAttribute, VertexBufferObject> vbos = new EnumMap<>(ShaderAttribute.class);

	public VertexArrayObject() {
		generateId();
	}
	
	/**
	 * Add a VBO to this VAO and enable it in the OpenGL state machine.
	 * @param vbo The VBO that you want to add to this VAO
	 * @param shaderAttribute The ShaderAttribute that the VBO belongs to
	 */
	public void addVBO(VertexBufferObject vbo, ShaderAttribute shaderAttribute) {
		vbos.put(shaderAttribute, vbo);
		bind();
		vbo.bind();
		glVertexAttribPointer(shaderAttribute.getLocation(), vbo.getChunkSize(), GL_FLOAT, false, 0, 0);
		glEnableVertexAttribArray(shaderAttribute.getLocation());	// Enable the new VBO
		vbo.unbind();
		unbind();
	}

	/**
	 * Get a VBO that has been previously added to this VAO.
	 * @param shaderAttribute The ShaderAttribute to which the VBO belongs
	 * @return The VBO belonging to the given ShaderAttribute or null
	 */
	public VertexBufferObject getVBO(ShaderAttribute shaderAttribute) {
		return vbos.get(shaderAttribute);
	}
	
	/**
	 * Bind this VAO in the OpenGL state machine.
	 * After binding, you may perform actions on it, for example drawing it.
	 */
	public void bind() {
		glBindVertexArray(id);
	}
	
	/**
	 * Unbind the currently bound VAO from the OpenGL state machine.
	 */
	public static void unbind() {
		glBindVertexArray(0);
	}
	
	/**
	 * Get this VAO's ID as registered with OpenGL.
	 * @return This VAO's ID
	 */
	public int getId() {
		return id;
	}
	
	/**
	 * Delete this VAO from OpenGL / the GPU.
	 * This will not delete this object, but render it useless.
	 */
	public void delete() {
		glDeleteVertexArrays(id);
	}
	
	/**
	 * Request a VAO ID from OpenGL and set it as this VAO's ID.
	 * This will do nothing if this VAO already has an ID.
	 */
	private void generateId() {
		id = (id == 0) ? glGenVertexArrays() : id;
	}
	
}
