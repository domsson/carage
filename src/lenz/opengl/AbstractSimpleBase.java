package lenz.opengl;
import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.ContextAttribs;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.PixelFormat;

public abstract class AbstractSimpleBase {
	
	public static final String DEFAULT_TITLE = "Game";
	public static final int DEFAULT_WIDTH  = 800;
	public static final int DEFAULT_HEIGHT = 600;

	public void start() {
		start(DEFAULT_WIDTH, DEFAULT_HEIGHT, DEFAULT_TITLE);
	}
	
	public void start(int width, int height) {
		start(width, height, DEFAULT_TITLE);
	}
	
	public void start(int width, int height, String title) {
		try {
			Display.setTitle(title);
			Display.setDisplayMode(new DisplayMode(width, height));
			Display.create();
			// http://stackoverflow.com/questions/18702467/enable-anti-aliasing-with-lwjgl
			// The last value (samples) will get us Antialiasing! But what do the other values do!?
			// Display.create(new PixelFormat(/*Alpha Bits*/2, /*Depth bits*/ 2, /*Stencil bits*/ 0, /*samples*/2));
		} catch (LWJGLException e) {
			throw new RuntimeException("Unable to intialize display", e);
		}

		initOpenGL();

		while (!Display.isCloseRequested()) {
			update();
			render();
			Display.update();
			Display.sync(60);
		}

		Display.destroy();
	}

	protected abstract void initOpenGL();
	protected abstract void update();
	protected abstract void render();
	
}
