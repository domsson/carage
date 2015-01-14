package lenz.opengl;
import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;

public abstract class AbstractSimpleBase {
	
	public static final String TITLE = "Carage";
	public static final int WIDTH = 800;
	public static final int HEIGHT = 600;

	public void start() {

		try {
			Display.setTitle(TITLE);
			Display.setDisplayMode(new DisplayMode(WIDTH, HEIGHT));
			Display.create();
		} catch (LWJGLException e) {
			throw new RuntimeException("Unable to intialize display", e);
		}

		// hier Initialisieren
		initOpenGL();

		while (!Display.isCloseRequested()) {

			// hier Zeichnen
			render();

			Display.update();
			Display.sync(60);
		}

		Display.destroy();

	}

	protected abstract void initOpenGL();

	protected abstract void render();

}
