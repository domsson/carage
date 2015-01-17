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
	private EnumMap<ShaderAttribute, VertexBufferObject> vbos = new EnumMap<>(ShaderAttribute.class); // TODO This might actually not be needed?

	public VertexArrayObject() {
		generateId();
	}
	
	public void addVBO(VertexBufferObject vbo, ShaderAttribute shaderAttribute) {
		vbos.put(shaderAttribute, vbo);
		bind();
		vbo.bind();
		glVertexAttribPointer(shaderAttribute.getLocation(), vbo.getChunkSize(), GL_FLOAT, false, 0, 0);
		glEnableVertexAttribArray(shaderAttribute.getLocation());	// Enable the new VBO
		vbo.unbind();
		unbind();
	}

	// TODO Why would we want to access the VBOs later on? This might be unnecessary!
	public VertexBufferObject getVBO(ShaderAttribute shaderAttribute) {
		return vbos.get(shaderAttribute);
	}
	
	public void bind() {
		glBindVertexArray(id);
	}
	
	public void unbind() {
		glBindVertexArray(0);
	}
	
	public int getId() {
		return id;
	}
		
	public void delete() {
		glDeleteVertexArrays(id);
	}
		
	private void generateId() {
		id = (id == 0) ? glGenVertexArrays() : id;
	}
	
}
