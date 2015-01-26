package carage.engine;

public class Material {
	
	public static final float DEFAULT_AMBIENT_REFLECTIVITY = 0.1f;
	public static final float DEFAULT_DIFFUSE_REFLECTIVITY = 0.5f;
	public static final float DEFAULT_SPECULAR_REFLECITIVITY = 0.4f;
	public static final int DEFAULT_SPECULAR_HARDNESS = 50;
	
	protected String name = "";				// In case we're going to implement a MaterialManager for material re-use... not used for now.
	
	protected float ambientReflectivity;	// 'ka' in the lecture slides; "ka + kd + ks = 1"
	protected float diffuseReflectivity;	// 'kd' in the lecture slides; "ka + kd + ks = 1"
	protected float specularReflectivity;	// 'ks' in the lecture slides; "ka + kd + ks = 1"
	
	protected int specularHardness;			// 'n'  in the lecture slides; "The value for f is typically chosen to be somewhere between 1 and 200."
	
	// TODO Should the material hold the shader it wants to be rendered with? This is how it works in Blender!
	// TODO diffuseColor, specularColor? Not for the time being, though...	
	
	public Material() {
		this("", DEFAULT_AMBIENT_REFLECTIVITY, DEFAULT_DIFFUSE_REFLECTIVITY, DEFAULT_SPECULAR_REFLECITIVITY, DEFAULT_SPECULAR_HARDNESS);
	}
	
	public Material(String name) {
		this(name, DEFAULT_AMBIENT_REFLECTIVITY, DEFAULT_DIFFUSE_REFLECTIVITY, DEFAULT_SPECULAR_REFLECITIVITY, DEFAULT_SPECULAR_HARDNESS);
	}
	
	public Material(String name, float ambientReflectivity, float diffuseReflectivity, float specularReflectivity, int specularHardness) {
		this.name = name;
		setAmbientReflectivity(ambientReflectivity);
		setDiffuseReflectivity(diffuseReflectivity);
		setSpecularReflectivity(specularReflectivity);
		setSpecularHardness(specularHardness);
	}
	
	public void setAmbientReflectivity(float ambientReflectivity) {
		this.ambientReflectivity = ambientReflectivity;
		normalizeReflectivities();
	}	
	
	public void setDiffuseReflectivity(float diffuseReflectivity) {
		this.diffuseReflectivity = diffuseReflectivity;
		normalizeReflectivities();
	}
	
	public void setSpecularReflectivity(float specularReflectivity) {
		this.specularReflectivity = specularReflectivity;
		normalizeReflectivities();
	}
	
	public void setSpecularHardness(int specularHardness) {
		this.specularHardness = (specularHardness > 1 && specularHardness <= 200) ? specularHardness : DEFAULT_SPECULAR_HARDNESS;
	}
	
	public float getAmbientReflectivity() {
		return ambientReflectivity;
	}

	public float getDiffuseReflectivity() {
		return diffuseReflectivity;
	}

	public float getSpecularReflectivity() {
		return specularReflectivity;
	}

	public int getSpecularHardness() {
		return specularHardness;
	}

	private void normalizeReflectivities() {
		float totalReflectivity = ambientReflectivity + diffuseReflectivity + specularReflectivity;
		if (totalReflectivity != 1) {
			float factor = 1 / totalReflectivity;
			ambientReflectivity = ambientReflectivity * factor;
			diffuseReflectivity = diffuseReflectivity * factor;
			specularReflectivity = specularReflectivity * factor;
		}
	}

}
