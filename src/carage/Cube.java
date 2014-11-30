package carage;

import static org.lwjgl.opengl.GL11.GL_QUADS;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glColor3f;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glVertex3f;

public class Cube extends Entity {

	public void draw() {
		float xLeft  = -0.5f;
		float xRight =  0.5f;
		float yUp    =  0.5f;
		float yDown  = -0.5f;
		float zNear  =  0.5f;
		float zFar   = -0.5f;
		float[] a = {xLeft, yUp, zNear};
		float[] b = {xLeft, yDown, zNear};
		float[] c = {xRight, yDown, zNear};
		float[] d = {xRight, yUp, zNear};
		float[] e = {xRight, yDown, zFar};
		float[] f = {xRight, yUp, zFar};
		float[] g = {xLeft, yDown, zFar};
		float[] h = {xLeft, yUp, zFar};
		
		glBegin(GL_QUADS);
			glColor3f(defaultColor[0], defaultColor[1], defaultColor[2]);
			// FRONT
			glVertex3f(a[0], a[1], a[2]);
			glVertex3f(b[0], b[1], b[2]);
			glVertex3f(c[0], c[1], c[2]);
			glVertex3f(d[0], d[1], d[2]);
			
			glColor3f(defaultColor[0], defaultColor[1], defaultColor[2]);
			// RIGHT
			glVertex3f(d[0], d[1], d[2]);
			glVertex3f(c[0], c[1], c[2]);
			glVertex3f(e[0], e[1], e[2]);
			glVertex3f(f[0], f[1], f[2]);
			
			glColor3f(defaultColor[0], defaultColor[1], defaultColor[2]);
			// BACK
			glVertex3f(f[0], f[1], f[2]);
			glVertex3f(e[0], e[1], e[2]);
			glVertex3f(g[0], g[1], g[2]);
			glVertex3f(h[0], h[1], h[2]);
			
			glColor3f(defaultColor[0], defaultColor[1], defaultColor[2]);
			// LEFT
			glVertex3f(h[0], h[1], h[2]);
			glVertex3f(g[0], g[1], g[2]);
			glVertex3f(b[0], b[1], b[2]);
			glVertex3f(a[0], a[1], a[2]);
			
			glColor3f(defaultColor[0], defaultColor[1], defaultColor[2]);
			// TOP
			glVertex3f(h[0], h[1], h[2]);
			glVertex3f(a[0], a[1], a[2]);
			glVertex3f(d[0], d[1], d[2]);
			glVertex3f(f[0], f[1], f[2]);
			
			glColor3f(defaultColor[0], defaultColor[1], defaultColor[2]);
			// BOTTOM
			glVertex3f(b[0], b[1], b[2]);
			glVertex3f(g[0], g[1], g[2]);
			glVertex3f(e[0], e[1], e[2]);
			glVertex3f(c[0], c[1], c[2]);
		glEnd();
	}
	
}
