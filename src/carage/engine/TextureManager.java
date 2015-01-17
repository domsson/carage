package carage.engine;

import java.util.HashMap;

import lenz.opengl.utils.Texture;

public final class TextureManager {
		
	private static TextureManager instance = null;
	private HashMap<String, Texture> textures = new HashMap<>();

	private TextureManager() {
		// I'm a Singleton. Get out!
	}
	
	public static TextureManager getInstance() {
		if (instance == null) {
			instance = new TextureManager();
		}
		return instance;
	}
	
	public int load(String resource) {
		// Not yet loaded? Load and return the texture id (or 0)!
		if (!textures.containsKey(resource)) {
			return add(resource);
		}
		// Already there, just return the texture id!
		return textures.get(resource).getId();
	}
	
	public Texture get(String resource) {
		// Not yet loaded? Load!
		if (!textures.containsKey(resource)) {
			add(resource);
		}
		// Still not there? Not available!
		if (!textures.containsKey(resource)) {
			return null;
		}
		// Got it, here you go!
		else {
			return textures.get(resource);
		}
	}
	
	public int getId(String resource) {
		// Not yet loaded? Load!
		if (!textures.containsKey(resource)) {
			add(resource);
		}
		// Still not there? Not available!
		if (!textures.containsKey(resource)) {
			return 0;
		}
		// Got it, here you go!
		else {
			return textures.get(resource).getId();
		}
	}
	
	private int add(String resource) {
		try {
			Texture texture = new Texture(resource);
			textures.put(resource, texture);
			return texture.getId();
		}
		catch (RuntimeException e) {
			return 0;
		}
	}
}
