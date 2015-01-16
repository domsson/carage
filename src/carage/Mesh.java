package carage;

import static org.lwjgl.opengl.GL11.GL_QUADS;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL11.glColor3f;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glTexCoord2f;
import static org.lwjgl.opengl.GL11.glVertex3f;

import java.util.ArrayList;

public class Mesh {
	
	private ArrayList<float[]> vertices = new ArrayList<>();
	private ArrayList<float[]> uvs      = new ArrayList<>();
	private ArrayList<float[]> normals  = new ArrayList<>();
	private ArrayList<int[][]> faces    = new ArrayList<>();
	
	private float geomOffsetX = 0;
	private float geomOffsetY = 0;
	private float geomOffsetZ = 0;
	
	private float uvOffsetU = 0;
	private float uvOffsetV = 0;
	
	private float minX = 0;
	private float maxX = 0;
	private float minY = 0;
	private float maxY = 0;
	private float minZ = 0;
	private float maxZ = 0;
	
	private float width  = 0;
	private float height = 0;
	private float length = 0;

	public Mesh() {
	}
	
	public int getNumberOfVertices() {
		return vertices.size();
	}
	
	public int getNumberOfUVs() {
		return uvs.size();
	}
	
	public int getNumberOfFaces() {
		return faces.size();
	}
	
	public float getWidth() {
		return width;
	}
	
	public float getHeight() {
		return height;
	}
	
	public float getLength() {
		return length;
	}
	
	public float[] getBoundariesX() {
		return new float[] { minX, maxX };
	}
	
	public float[] getBoundariesY() {
		return new float[] { minY, maxY };
	}
	
	public float[] getBoundariesZ() {
		return new float[] { minZ, maxZ };
	}
	
	public float[] getGeometryOffsets() {
		return new float[] { geomOffsetX, geomOffsetY, geomOffsetZ };
	}
	
	public float[] getUVOffsets() {
		return new float[] { uvOffsetU, uvOffsetV };
	}
	
	public void setGeometryOffsets(float offsetX, float offsetY, float offsetZ) {
		geomOffsetX = offsetX;
		geomOffsetY = offsetY;
		geomOffsetZ = offsetZ;
	}
	
	public void setGeometryOffsets(float[] xyz) {
		setGeometryOffsets(xyz[0], xyz[1], xyz[2]);
	}
	
	public void setGeometryOffsetX(float offsetX) {
		geomOffsetX = offsetX;
	}
	
	public void setGeometryOffsetY(float offsetY) {
		geomOffsetY = offsetY;
	}
	
	public void setGeometryOffsetZ(float offsetZ) {
		geomOffsetZ = offsetZ;
	}
	
	public void setUVOffsets(float offsetU, float offsetV) {
		uvOffsetU = offsetU;
		uvOffsetV = offsetV;
	}
	
	public void setUVOffsets(float[] uv) {
		setUVOffsets(uv[0], uv[1]);
	}
	
	public void setUVOffsetU(float offsetU) {
		uvOffsetU = offsetU;
	}
	
	public void setUVOffsetV(float offsetV) {
		uvOffsetV = offsetV;
	}
	
	public void addVertex(float x, float y, float z) {
		vertices.add(new float[] {x, y, z});
		updateBoundingBox(x, y, z);
	}
	
	public void addVertex(float[] xyz) {
		addVertex(xyz[0], xyz[1], xyz[2]);
	}
		
	public void addUV(float u, float v) {
		uvs.add(new float[] {u, v});
	}
	
	public void addUV(float[] uv) {
		addUV(uv[0], uv[1]);
	}
	
	/*
	 * Tri hat drei Punkte (Vertices)
	 * Jeder Vertex hat drei Koordinaten (x,y,z)
	 * Jeder Vertex *kann* zwei UV-Koordinaten haben (u, v)
	 * 
	 * Beispiel (zwei Vertices unwrapped, einer nicht):
	 * 
	 * tri[0] = {x,y,z,u,v}
	 * tri[1] = {x,y,z,u,v}
	 * tri[2] = {x,y,z}
	 * 
	 * ABER: im faces Array speichern wir nur die Indizes
	 * der jeweiligen Vertices/UVs
	 * 
	 * Beispiel (wie oben):
	 * 
	 * tri[0] = {17, 6}; // Vertex #17, UV #6
	 * tri[1] = {18, 7}; // Vertex #18, UV #7
	 * tri[2] = {19};    // Vertex #19, kein UV
	 * 
	 * Den Mix aus Vertex+UV nennen wir "Point":
	 * 
	 * point[] = {17, 6};
	 * point[] = {19};
	 */
	
	public void makeTri(int[] a, int[] b, int[] c) {
		makeTri(new int[][] {a, b, c});
	}

	public void makeTri(int[][] points) {
		// if ((points.length == 3) && (verticesExist(points))) {
		if ((points.length == 3) && pointsExist(points)) {
			faces.add(points);
		}
	}
		
	public void makeQuad(int[] a, int[] b, int[] c, int[] d) {
		makeQuad(new int[][] {a, b, c, d});
	}
	
	public void makeQuad(int[][] points) {
		if ((points.length == 4) && (pointsExist(points))) {
			faces.add(points);
		}
	}
	
	public void makeFace(int[][] points) {
		if (points.length == 3) {
			makeTri(points);
			return;
		}
		if (points.length == 4) {
			makeQuad(points);
		}
	}
	
	// TODO (maybe?)
	public void addFace() {
	}
	
	// TODO (maybe not?)
	public void trisFromVerts() {
		
	}
	
	
	// TODO (maybe not?)
	public void quadsFromVerts() {
		
	}
	
	private void updateBoundingBox(float x, float y, float z) {
		minX = (x < minX) ? x : minX;
		maxX = (x > maxX) ? x : maxX;
		minY = (y < minY) ? y : minY;
		maxY = (y > maxY) ? y : maxY;
		minZ = (z < minZ) ? z : minZ;
		maxZ = (z > maxZ) ? z : maxZ;
		updateDimensions();		
	}
	
	private void updateDimensions() {
		width  = maxX - minX;
		height = maxY - minY;
		length = maxZ - minZ;
	}
	
	private boolean pointsExist(int[][] points) {
		// 3=tri; 4=quad
		int numPoints = points.length;
		
		for (int i=0; i<numPoints; ++i) {
			// 3=not unwrapped; 5=unwrapped
			int numElements = points[i].length;
			
			if (numElements==3) {
				if (!verticesExist(points[i])) {
					return false;
				}
			}
			else if (numElements==5) {
				if (!verticesExist(new int[] {points[i][0], points[i][1], points[i][2]})) {
					return false;
				}
				if (!uvsExist(new int[] {points[i][3], points[i][4]})) {
					return false;
				}
			}
		}
		return true;
	}
	
	private boolean verticesExist(int[] vertices) {
		int numVertsInMesh = this.vertices.size();
		int numVertsProvided = vertices.length;
		int numValidVertsProvided = 0;
		for (int i=0; i<numVertsProvided; ++i) {
			if (vertices[i] < numVertsInMesh) {
				++numValidVertsProvided;
			}
		}
		return (numVertsProvided == numValidVertsProvided);
	}
	
	private boolean uvsExist(int[] uvs) {
		int numUVsInMesh = this.uvs.size();
		int numUVsProvided = uvs.length;
		int numValidUVsProvided = 0;
		for (int i=0; i<numUVsProvided; ++i) {
			if (uvs[i] < numUVsInMesh) {
				++numValidUVsProvided;
			}
		}
		return (numUVsProvided == numValidUVsProvided);
	}
	
	private boolean verticeExists(int vertexNum) {
		return vertexNum < vertices.size();
	}
	
	private boolean uvExists(int uvNum) {
		return uvNum < uvs.size();
	}
	
	public void draw() {
		glBegin(GL_TRIANGLES);
		for (int[][] face : faces) {
			for (int i=0; i<3; ++i) {
				if (face[i].length > 1) {
					float[] uv = uvs.get(face[i][1]);
					glTexCoord2f(uv[0]+uvOffsetU, 1-(uv[1]+uvOffsetV));
				}				
				float[] v  = vertices.get(face[i][0]);
				glVertex3f(v[0]+geomOffsetX, v[1]+geomOffsetY, v[2]+geomOffsetZ);
			}
		}
		glEnd();
	}
	
	public void draw(int textureId) {
		glBindTexture(GL_TEXTURE_2D, textureId);
		draw();
	}
	
	public void drawBoundingBox(float[] color) {
		glBindTexture(GL_TEXTURE_2D, 0);
		glBegin(GL_QUADS);
			glColor3f(color[0], color[1], color[2]);
			// bottom
			glVertex3f(minX, minY, minZ);
			glVertex3f(maxX, minY, minZ);
			glVertex3f(maxX, minY, maxZ);
			glVertex3f(minX, minY, maxZ);
			// front
			glVertex3f(minX, minY, maxZ);
			glVertex3f(maxX, minY, maxZ); 
			glVertex3f(maxX, maxY, maxZ);
			glVertex3f(minX, maxY, maxZ);
			// top
			glVertex3f(minX, maxY, maxZ);
			glVertex3f(maxX, maxY, maxZ);
			glVertex3f(maxX, maxY, minZ);
			glVertex3f(minX, maxY, minZ);
			// back
			glVertex3f(minX, maxY, minZ);
			glVertex3f(maxX, maxY, minZ);
			glVertex3f(maxX, minY, minZ);
			glVertex3f(minX, minY, minZ);
			
			// left
			glVertex3f(minX, minY, minZ);
			glVertex3f(minX, minY, maxZ);
			glVertex3f(minX, maxY, maxZ);
			glVertex3f(minX, maxY, minZ);
			// right
			glVertex3f(maxX, minY, maxZ);
			glVertex3f(maxX, minY, minZ);
			glVertex3f(maxX, maxY, minZ);
			glVertex3f(maxX, maxY, maxZ);
			
		glEnd();
	}
	
}
