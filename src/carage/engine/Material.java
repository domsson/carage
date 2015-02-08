package carage.engine;

import static org.lwjgl.opengl.GL20.glUniform1f;
import static org.lwjgl.opengl.GL20.glUniform1i;
import lenz.opengl.utils.ShaderProgram;

public class Material {
	
	public static final float DEFAULT_AMBIENT_REFLECTIVITY = 0.1f;
	public static final float DEFAULT_DIFFUSE_REFLECTIVITY = 0.5f;
	public static final float DEFAULT_SPECULAR_REFLECITIVITY = 0.4f;
	public static final int DEFAULT_SPECULAR_HARDNESS = 50;
	public static final String DEFAULT_SHADER = "phong";
	
	protected String name = "";				// In case we're going to implement a MaterialManager for material re-use... not used for now.
	protected ShaderProgram shader = null;
	
	protected float ambientReflectivity;	// 'ka' in the lecture slides; "ka + kd + ks = 1"
	protected float diffuseReflectivity;	// 'kd' in the lecture slides; "ka + kd + ks = 1"
	protected float specularReflectivity;	// 'ks' in the lecture slides; "ka + kd + ks = 1"
	protected int specularHardness;			// 'n'  in the lecture slides; "The value for f is typically chosen to be somewhere between 1 and 200."
	
	protected int ambientReflectivityLocation  = -1;
	protected int diffuseReflectivityLocation  = -1;
	protected int specularReflectivityLocation = -1;
	protected int specularHardnessLocation     = -1;
	
	// TODO diffuseColor, specularColor? Not for the time being, though...	
	
	public Material() {
		this("", null, DEFAULT_AMBIENT_REFLECTIVITY, DEFAULT_DIFFUSE_REFLECTIVITY, DEFAULT_SPECULAR_REFLECITIVITY, DEFAULT_SPECULAR_HARDNESS);
	}
	
	public Material(String name) {
		this(name, null, DEFAULT_AMBIENT_REFLECTIVITY, DEFAULT_DIFFUSE_REFLECTIVITY, DEFAULT_SPECULAR_REFLECITIVITY, DEFAULT_SPECULAR_HARDNESS);
	}
	
	public Material(String name, ShaderProgram shader) {
		this(name, shader, DEFAULT_AMBIENT_REFLECTIVITY, DEFAULT_DIFFUSE_REFLECTIVITY, DEFAULT_SPECULAR_REFLECITIVITY, DEFAULT_SPECULAR_HARDNESS);
	}
	
	public Material(String name, ShaderProgram shader, float ambientReflectivity, float diffuseReflectivity, float specularReflectivity, int specularHardness) {
		this.name = name;
		if (shader == null) { initShader(DEFAULT_SHADER); }
		else { setShader(shader); }
		setAmbientReflectivity(ambientReflectivity);
		setDiffuseReflectivity(diffuseReflectivity);
		setSpecularReflectivity(specularReflectivity);
		setSpecularHardness(specularHardness);
		normalizeReflectivities();
	}
	
	public String getName() {
		return name;
	}
	
	public void setShader(ShaderProgram shader) {
		this.shader = shader;
	}
	
	public ShaderProgram getShader() {
		return shader;
	}
	
	public int getShaderId() {
		return shader.getId();
	}
	
	public void setAmbientReflectivity(float ambientReflectivity) {
		this.ambientReflectivity = ambientReflectivity;
		// normalizeReflectivities();
	}	
	
	public void setDiffuseReflectivity(float diffuseReflectivity) {
		this.diffuseReflectivity = diffuseReflectivity;
		// normalizeReflectivities();
	}
	
	public void setSpecularReflectivity(float specularReflectivity) {
		this.specularReflectivity = specularReflectivity;
		// normalizeReflectivities();
	}
	
	public void setSpecularHardness(int specularHardness) {
		this.specularHardness = specularHardness;
		this.specularHardness = (specularHardness < 1) ? 1 : this.specularHardness;
		this.specularHardness = (specularHardness > 200) ? 200 : this.specularHardness;
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
	
	public void sendToShader() {
		sendToShader(shader);
	}
	
	public void sendToShader(ShaderProgram shader) {
		update();
		int location = -1;
		if ((location = shader.getUniformLocation("materialAmbientReflectivity")) != -1) {
			glUniform1f(location, ambientReflectivity);
		}
		if ((location = shader.getUniformLocation("materialDiffuseReflectivity")) != -1) {
			glUniform1f(location, diffuseReflectivity);
		}
		if ((location = shader.getUniformLocation("materialSpecularReflectivity")) != -1) {
			glUniform1f(location, specularReflectivity);
		}
		if ((location = shader.getUniformLocation("materialSpecularHardness")) != -1) {
			glUniform1i(location, specularHardness);
		}
	}
	
	private void update() {
		normalizeReflectivities();
	}
	
	private void initShader(String shaderName) {
		ShaderManager shaderManager = ShaderManager.getInstance();
		setShader(shaderManager.get(shaderName));
	}

	private void normalizeReflectivities() {
		float totalReflectivity = ambientReflectivity + diffuseReflectivity + specularReflectivity;
		if (totalReflectivity != 1f) {
			float factor = 1f / totalReflectivity;
			ambientReflectivity = ambientReflectivity * factor;
			diffuseReflectivity = diffuseReflectivity * factor;
			specularReflectivity = specularReflectivity * factor;
		}
	}

}
