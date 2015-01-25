package carage.engine;

public enum ShaderAttribute {

	POSITION (0, "in_Position"),
	COLOR    (1, "in_Color"),
	TEXTURE  (2, "in_TextureCoord"),
	NORMALS  (3, "in_Normal");
	
	private int location;
	private String name;
	
	private ShaderAttribute(int location, String name) {
		this.location = location;
		this.name = name;
	}
	
	public int getLocation() {
		return location;
	}
	
	public String getName() {
		return name;
	}
	
}
