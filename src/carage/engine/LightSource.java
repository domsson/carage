package carage.engine;

import static org.lwjgl.opengl.GL20.glGetUniformLocation;
import lenz.opengl.utils.ShaderProgram;

// http://www.gamedev.net/page/resources/_/technical/opengl/the-basics-of-glsl-40-shaders-r2861
public class LightSource extends Entity {
	
	public static final float DEFAULT_INTENSITY = 0.5f;
	
	private float intensity; // or should we call it 'brightness'?
	// TODO what else? fall-off? color? something?
	
	public LightSource() {
		setIntensity(DEFAULT_INTENSITY);
	}
	
	public LightSource(float intensity) {
		setIntensity(intensity);
	}
	
	public void setIntensity(float intensity) {
		this.intensity = intensity;
	}
	
	public float getIntensity() {
		return intensity;
	}

	public void toShader() {
		
	}
	
	public void toShader(ShaderProgram shaderProgram) {
		
	}

}
