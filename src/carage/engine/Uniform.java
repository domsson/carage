package carage.engine;

import lenz.opengl.utils.ShaderProgram;

// wait... I think this doesn't make sense at all! for example, the LightSource object will have
// SEVERAL values that need to be send over INDIVIDUALLY... so, if at all, then those VALUES should
// be their own objects, which can then extend/implement a Uniform object/interface. Hm....
// OR... we just put "toShader" in here and everything else has to be handled internally by the implementing class?
// i dunno...
public interface Uniform {
	
	public void toShader();
	public void toShader(ShaderProgram sp);
	
}
