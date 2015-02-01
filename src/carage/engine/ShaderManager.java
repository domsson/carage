package carage.engine;

import java.util.HashMap;

import lenz.opengl.utils.ShaderProgram;

public class ShaderManager {
	
public static final String VERTEX_SHADER_FORMAT = "v";
public static final String FRAGMENT_SHADER_FORMAT = "f";
	
	private static ShaderManager instance = null;
	private HashMap<String, ShaderProgram> shaders = new HashMap<>();

	private ShaderManager() {
		// I'm a Singleton. Get out!
	}
	
	public static ShaderManager getInstance() {
		if (instance == null) {
			instance = new ShaderManager();
		}
		return instance;
	}
	
	public ShaderProgram load(String resource) {
		// Not yet loaded? Load and return the shader (or null)!
		if (!shaders.containsKey(resource)) {
			return add(resource);
		}
		// Already there, just return the geometry!
		return shaders.get(resource);
	}
	
	/**
	 * Forwards to load(), as load() will return the loaded ShaderProgram
	 * and only actually load one if it isn't present already.
	 * @param resource
	 * @return
	 */
	public ShaderProgram get(String resource) {
		return load(resource);
	}
	
	private ShaderProgram add(String resource) {
		ShaderProgram shader = new ShaderProgram(resource);
		shaders.put(resource, shader);
		return shader;
	}

}
