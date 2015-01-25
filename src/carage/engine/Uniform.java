package carage.engine;

import lenz.opengl.utils.ShaderProgram;

// wait... I think this doesn't make sense at all! for example, the LightSource object will have
// SEVERAL values that need to be send over INDIVIDUALLY... so, if at all, then those VALUES should
// be their own objects, which can then extend/implement a Uniform object/interface. Hm....
public interface Uniform {
	
	public String getName();
	public int fetchLocation(ShaderProgram sp); // should those be private/internal?
	public int fetchLocation(int spId);  // should those be private/internal?
	public boolean hasLocation();
	public int getLocation();
	public void toShader();
	// public void toShader(ShaderProgram sp); // ..and instead this one should be implemented, which calls the fetchLocations() methods if required?
}
