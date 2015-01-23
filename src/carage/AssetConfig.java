package carage;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

@SuppressWarnings("serial")
public class AssetConfig extends Properties {
	
	public static final String ASSET_DIRECTORY = "/res/meshes/"; 
	public static final String FILE_EXTENSION = "properties";
	
	public AssetConfig() {
		
	}
	
	public AssetConfig(String resource) {
		loadProperties(resource);
	}
	
	public void loadProperties(String resource) {
		// TODO use a Reader instead so we can support UTF-8 files (way nicer than ISO 8859-1)
		InputStream input = createInputStreamFromResourceName(resource);
		
		if (input != null) {
			try {
				load(input);
				input.close();
			} catch (IOException e) {
				System.err.println("[ERROR] Could not load AssetConfig "+ fullResourceName(resource));
			}
		}
	}
	
	public float getFloatProperty(String key) {
		return Float.parseFloat(getProperty(key));
	}
	
	public float getFloatProperty(String key, float defaultValue) {
		String prop = getProperty(key);
		return (prop == null) ? defaultValue : Float.parseFloat(prop);
	}
	
	private InputStream createInputStreamFromResourceName(String resource) {
		return getClass().getResourceAsStream(fullResourceName(resource));
	}
	
	private String fullResourceName(String resource) {
		return (resource.startsWith("/")) ? (resource + "." + FILE_EXTENSION) : (ASSET_DIRECTORY + resource + "." + FILE_EXTENSION);
	}
	
}
