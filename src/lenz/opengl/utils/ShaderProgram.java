package lenz.opengl.utils;

import static org.lwjgl.opengl.GL11.GL_FALSE;
import static org.lwjgl.opengl.GL20.GL_COMPILE_STATUS;
import static org.lwjgl.opengl.GL20.GL_FRAGMENT_SHADER;
import static org.lwjgl.opengl.GL20.GL_INFO_LOG_LENGTH;
import static org.lwjgl.opengl.GL20.GL_LINK_STATUS;
import static org.lwjgl.opengl.GL20.GL_VERTEX_SHADER;
import static org.lwjgl.opengl.GL20.glAttachShader;
import static org.lwjgl.opengl.GL20.glBindAttribLocation;
import static org.lwjgl.opengl.GL20.glCompileShader;
import static org.lwjgl.opengl.GL20.glCreateProgram;
import static org.lwjgl.opengl.GL20.glCreateShader;
import static org.lwjgl.opengl.GL20.glGetProgramInfoLog;
import static org.lwjgl.opengl.GL20.glGetProgrami;
import static org.lwjgl.opengl.GL20.glGetShaderInfoLog;
import static org.lwjgl.opengl.GL20.glGetShaderi;
import static org.lwjgl.opengl.GL20.glGetUniformLocation;
import static org.lwjgl.opengl.GL20.glLinkProgram;
import static org.lwjgl.opengl.GL20.glShaderSource;
import static org.lwjgl.opengl.GL20.glUseProgram;
import static org.lwjgl.opengl.GL32.GL_GEOMETRY_SHADER;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Scanner;

public class ShaderProgram {
	
	private int id;
	private HashMap<String, Integer> uniformLocations = new HashMap<>();

	public ShaderProgram(String resourceNameWithoutExtension) {
		this(resourceNameWithoutExtension + ".v", resourceNameWithoutExtension + ".g", resourceNameWithoutExtension + ".f");
	}

	public ShaderProgram(String vertexResourceName, String fragmentResourceName) {
		this(vertexResourceName, null, fragmentResourceName);
	}

	public ShaderProgram(String vertexResourceName, String geometryResourceName, String fragmentResourceName) {
		id = glCreateProgram();
		compileFromSourceAndAttach(vertexResourceName, GL_VERTEX_SHADER);
		compileFromSourceAndAttach(fragmentResourceName, GL_FRAGMENT_SHADER);
		compileFromSourceAndAttach(geometryResourceName, GL_GEOMETRY_SHADER);

		glLinkProgram(id);
		if (glGetProgrami(id, GL_LINK_STATUS) == GL_FALSE) {
			throw new RuntimeException(glGetProgramInfoLog(id, glGetProgrami(id, GL_INFO_LOG_LENGTH)));
		}
	}

	public int getId() {
		return id;
	}
	
	public void bind() {
		glUseProgram(id);
	}
	
	public void unbind() {
		glUseProgram(0);
	}

	private InputStream getInputStreamFromResourceName(String resourceName) {
		return getClass().getResourceAsStream("/res/shaders/" + resourceName);
	}

	private void compileFromSourceAndAttach(String resourceName, int type) {
		InputStream inputStreamFromResourceName = getInputStreamFromResourceName(resourceName);
		if (inputStreamFromResourceName == null) {
			if (type != GL_GEOMETRY_SHADER) {
				throw new RuntimeException("Shader source file " + resourceName + " not found!");
			}
			return;
		}
		try (Scanner in = new Scanner(inputStreamFromResourceName)) {
			String source = in.useDelimiter("\\A").next();
			int shaderId = glCreateShader(type);
			glShaderSource(shaderId, source);
			glCompileShader(shaderId);

			String compileLog = glGetShaderInfoLog(shaderId, glGetShaderi(shaderId, GL_INFO_LOG_LENGTH));
			if (glGetShaderi(shaderId, GL_COMPILE_STATUS) == GL_FALSE) {
				throw new RuntimeException("Shader " + resourceName + " not compiled: " + compileLog);
			}
			if (!compileLog.isEmpty()) {
				System.err.println(resourceName + ": " + compileLog);
			}

			glAttachShader(id, shaderId);
		}
	}

	// http://stackoverflow.com/questions/3158730/java-3-dots-in-parameters
	public void bindAttributeLocations(String... variableNames) {
		int i = 0;
		for (String var : variableNames) {
			glBindAttribLocation(id, i, var);
			++i;
		}
		glLinkProgram(id);
		if (glGetProgrami(id, GL_LINK_STATUS) == GL_FALSE) {
			throw new RuntimeException(glGetProgramInfoLog(id, glGetProgrami(id, GL_INFO_LOG_LENGTH)));
		}
	}
	
	public int getUniformLocation(String name) {
		if (!uniformLocations.containsKey(name)) {
			return fetchUniformLocation(name);
		}
		return uniformLocations.get(name);
	}
	
	private int fetchUniformLocation(String name) {
		int location = glGetUniformLocation(this.id, name);
		uniformLocations.put(name, location);
		return location;
	}
}
