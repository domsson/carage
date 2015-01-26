package carage.engine;

import static org.lwjgl.opengl.GL20.glGetUniformLocation;
import static org.lwjgl.opengl.GL20.glUniform1f;
import static org.lwjgl.opengl.GL20.glUniform3f;
import lenz.opengl.utils.ShaderProgram;

// http://www.gamedev.net/page/resources/_/technical/opengl/the-basics-of-glsl-40-shaders-r2861
public class LightSource extends Entity {
	
	public static final float DEFAULT_INTENSITY =  1f;
	public static final float MINIMUM_INTENSITY =  0f;
	public static final float MAXIMUM_INTENSITY = 10f;
	
	private float intensity; // or should we call it 'brightness'?
	private int intensityLocation = 0;
	private int positionLocation = 0;
	// TODO what else? fall-off? color? something?
	
	private boolean isOn = true;
	
	public LightSource() {
		this(DEFAULT_INTENSITY);
	}
	
	public LightSource(float intensity) {
		super();
		setIntensity(intensity);
	}
	
	public void setIntensity(float intensity) {
		this.intensity = intensity;
		this.intensity = (this.intensity < MINIMUM_INTENSITY) ? MINIMUM_INTENSITY : this.intensity;
		this.intensity = (this.intensity > MAXIMUM_INTENSITY) ? MAXIMUM_INTENSITY : this.intensity;
	}
	
	public float getIntensity() {
		return intensity;
	}
	
	public boolean isOn() {
		return isOn;
	}
	
	public void turnOff() {
		isOn = false;
	}
	
	public void turnOn() {
		isOn = true;
	}
	
	public void toggle() {
		isOn = !isOn;
	}
	
	public void fetchLocations(ShaderProgram shader) {
		positionLocation = glGetUniformLocation(shader.getId(), "lightPosition");
		intensityLocation = glGetUniformLocation(shader.getId(), "lightIntensity");
	}

	public void toShader() {
		glUniform3f(positionLocation, position.x, position.y, position.z);
		glUniform1f(intensityLocation, ((isOn) ? intensity : 0f));
	}
	
	public void toShader(ShaderProgram shaderProgram) {
		// TODO
		/*
		glUseProgram(shaderProgram.getId());
		
		glUseProgram(0);
		*/
	}
	
}
