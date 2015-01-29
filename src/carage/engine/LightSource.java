package carage.engine;

import static org.lwjgl.opengl.GL20.glUniform1f;
import static org.lwjgl.opengl.GL20.glUniform3f;
import lenz.opengl.utils.ShaderProgram;

// http://www.gamedev.net/page/resources/_/technical/opengl/the-basics-of-glsl-40-shaders-r2861
public class LightSource extends Entity {
	
	public static final float DEFAULT_INTENSITY =  1f;
	public static final float MINIMUM_INTENSITY =  0f;
	public static final float MAXIMUM_INTENSITY = 10f;
	
	private float intensity; // or should we call it 'brightness'?
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
	
	public void sendToShader(ShaderProgram shader) {
		int location = -1;
		if ((location = shader.getUniformLocation("lightPosition")) != -1) {
			glUniform3f(location, position.x, position.y, position.z);
		}
		if ((location = shader.getUniformLocation("lightIntensity")) != -1) {
			glUniform1f(location, ((isOn) ? intensity : 0f));
		}
	}
	
}
