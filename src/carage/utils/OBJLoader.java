package carage.utils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Scanner;

import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import carage.engine.Vertex;

// http://www.martinreddy.net/gfx/3d/OBJ.spec
public class OBJLoader {
	
	public static final String TOKEN_OBJECT = "o";
	public static final String TOKEN_MATERIAL = "usemtl";
	public static final String TOKEN_GEOMETRY_VERTEX = "v";
	public static final String TOKEN_TEXTURE_VERTEX = "vt";
	public static final String TOKEN_VERTEX_NORMAL = "vn";
	public static final String TOKEN_POINT = "p";
	public static final String TOKEN_LINE = "l";
	public static final String TOKEN_FACE = "f";
	
	private ArrayList<Vector3f> v   = new ArrayList<>();		// geometry vertices
	private ArrayList<Vector2f> vt  = new ArrayList<>();		// texture vertices
	private ArrayList<Vector3f> vn  = new ArrayList<>();		// vertex normals
	
	private ArrayList<Integer>  idx = new ArrayList<>();		// face indices (three of them define a tri) (using the vertexList)
	private ArrayList<Vertex> vertexList = new ArrayList<>();	// the final list of unique vertices
	
	private Vector3f min  = new Vector3f();	// smallest position values
	private Vector3f max  = new Vector3f(); // biggest position values
	private Vector3f size = new Vector3f();	// x=width, y=height, z=length
	
	public OBJLoader(String resourceName) {
		try {
			parseOBJ(new File(".").getCanonicalPath() + "/src/res/meshes/"+resourceName); // TODO make this more... uhm, dynamic?
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void parseOBJ(String resourceName) throws IOException {
	    Path path = Paths.get(resourceName);
	    try (Scanner scanner = new Scanner(path, "UTF-8")) {
			while (scanner.hasNextLine()){
				parseLine(scanner.nextLine());
			}
	    }
	}
	
	private void parseLine(String line) {
		// String[] tokens = line.split(" ");
		int firstSpace = line.indexOf(" ");
		String dataType = line.substring(0, firstSpace);
		String[] data = line.substring(firstSpace+1, line.length()).split(" ");
		
		switch (dataType) {
			case TOKEN_GEOMETRY_VERTEX:
				// v x y z w (w is optional defaults to 1.0)
				processGeometryVertex(data);
				break;
			case TOKEN_TEXTURE_VERTEX:
				// vt u v w (w is optional)
				processTextureVertex(data);
				break;
			case TOKEN_VERTEX_NORMAL:
				// vn i j k
				processVertexNormal(data);
				break;
			case TOKEN_FACE:
				// f v OR f v/vt OR f v//vn OR f v/vt/vn (3 or more times)
				processFace(data);
				break;
			default:
				// we ran into one of many thing we don't care about (yet)
		}
	}
	
	private void processGeometryVertex(String[] data) {
		if (data.length < 3) { return; } // TODO throw exception or otherwise indicate malformed data?
		processGeometryVertex(Float.parseFloat(data[0]), Float.parseFloat(data[1]), Float.parseFloat(data[2]));
	}
	
	private void processGeometryVertex(float x, float y, float z) {		
		v.add(new Vector3f(x, y, z));
		updateBoundingBox(x, y, z);
	}
	
	private void processTextureVertex(String[] data) {
		if (data.length < 2) { return; } // TODO throw exception or otherwise indicate malformed data?
		processTextureVertex(Float.parseFloat(data[0]), Float.parseFloat(data[1]));
	}
	
	private void processTextureVertex(float u, float v) {
		vt.add(new Vector2f(u, 1-v));
	}

	private void processVertexNormal(String[] data) {
		if (data.length < 3) { return; } // TODO throw exception or otherwise indicate malformed data?
		processVertexNormal(Float.parseFloat(data[0]), Float.parseFloat(data[1]), Float.parseFloat(data[2]));
	}
	
	private void processVertexNormal(float x, float y, float z) {
		vn.add(new Vector3f(x, y, z));
	}

	private void processFace(String[] polyPoints) {
		int numPoints = polyPoints.length;
		// tri
		if (numPoints==3) {
			processFace(polyPoints[0], polyPoints[1], polyPoints[2]);
		}
		// quad
		else if (numPoints==4) {
			processFace(polyPoints[0], polyPoints[1], polyPoints[2], polyPoints[3]);
		}
	}
	
	private void processFace(String p1, String p2, String p3) {
		// Make vertices out of those index strings and feed them to processTri()
		processTri(new Vertex[] { faceIndicesToVertex(p1), faceIndicesToVertex(p2), faceIndicesToVertex(p3) });		
	}
	
	private void processFace(String p1, String p2, String p3, String p4) {
		// Triangulation
		processFace(p1, p2, p3);
		processFace(p3, p4, p1);
	}
	
	private void processTri(Vertex[] vertices) {
		if (vertices.length < 3) { return; } // TODO throw exception or otherwise indicate malformed data?
		
		int vertexIndex;
		for (int i=0; i<3; ++i) {
			vertexIndex = vertexList.indexOf(vertices[i]); // Search the vertexList for this vertex
			if (vertexIndex >= 0) {	// If it was found: remember its index
				idx.add(vertexIndex);
			}
			else { // If it wasn't found: add it and remember its index
				idx.add(vertexList.size());
				vertexList.add(vertices[i]);
			}
		}
	}
	
	private Vertex faceIndicesToVertex(String f) {
		Vertex vert = new Vertex();
		int vIndex  = -1;
		int vnIndex = -1;
		int vtIndex = -1;
		
		String[] parts = f.split("/");
		int numParts = parts.length;
		if (numParts == 1) {
			vIndex = Integer.parseInt(parts[0]) - 1;
		}
		else if (numParts == 2) {
			vIndex = Integer.parseInt(parts[0]) - 1;
			vtIndex = Integer.parseInt(parts[1]) - 1;
		}
		else if (numParts == 3) {
			vIndex = Integer.parseInt(parts[0]) - 1;
			vtIndex = (parts[1].equals("")) ? vtIndex : Integer.parseInt(parts[1]) - 1;
			vnIndex = Integer.parseInt(parts[2]) - 1;
		}
		
		if (vIndex  >= 0) { vert.setPosition(v.get(vIndex)); }
		if (vtIndex >= 0) { vert.setUnwrap(vt.get(vtIndex)); }
		if (vnIndex >= 0) { vert.setNormal(vn.get(vnIndex)); }
		
		return vert;
	}
	
	public boolean hasNormals() {
		return (vn.size() > 0);
	}
	
	public boolean hasUnwraps() {
		return (vt.size() > 0);
	}
	
	public int numberOfPositions() {
		return v.size();
	}
	
	public int numberOfUnwraps() {
		return vt.size();
	}
	
	public int numberOfNormals() {
		return vn.size();
	}
	
	public int numberOfIndices() {
		return idx.size();
	}
	
	public int numberOfUniqueVertices() {
		return vertexList.size();
	}
	
	public Vector3f getSize() {
		return new Vector3f(size.getX(), size.getY(), size.getZ());
	}
	
	public Vector3f getMinBoundaries() {
		return new Vector3f(min.getX(), min.getY(), min.getZ());
	}
	
	public Vector3f getMaxBoundaries() {
		return new Vector3f(max.getX(), max.getY(), max.getZ());
	}
	
	public Vector3f[] getPositions() {
		Vector3f[] positions = new Vector3f[v.size()];
		return (Vector3f[]) v.toArray(positions);
	}
	
	public Vector2f[] getUnwraps() {
		Vector2f[] unwraps = new Vector2f[vt.size()];
		return (Vector2f[]) vt.toArray(unwraps);
	}
	
	public Vector3f[] getNormals() {
		Vector3f[] normals = new Vector3f[vn.size()];
		return (Vector3f[]) vn.toArray(normals);
	}
	
	public int[] getIndices() {
		return ArrayUtils.arrayListToIntArray(idx, 0);
	}
	
	public float[] getExpandedPositions() {
		float[] positions = new float[vertexList.size() * 3];
		
		for (int i=0; i<vertexList.size(); ++i) {
			int pos = 3 * i;
			positions[pos]   = vertexList.get(i).getPosition().getX();
			positions[pos+1] = vertexList.get(i).getPosition().getY();
			positions[pos+2] = vertexList.get(i).getPosition().getZ();
		}
		
		return positions;
	}
	
	public float[] getExpandedUnwraps() {
		float[] unwraps = new float[vertexList.size() * 2];
		
		for (int i=0; i<vertexList.size(); ++i) {
			int pos = 2 * i;
			unwraps[pos]   = vertexList.get(i).getUnwrap().getX();
			unwraps[pos+1] = vertexList.get(i).getUnwrap().getY();
		}
		
		return unwraps;
	}
	
	public float[] getExpandedNormals() {
		float[] normals = new float[vertexList.size() * 3];
		
		for (int i=0; i<vertexList.size(); ++i) {
			int pos = 3 * i;
			normals[pos]   = vertexList.get(i).getNormal().getX();
			normals[pos+1] = vertexList.get(i).getNormal().getY();
			normals[pos+2] = vertexList.get(i).getNormal().getZ();
		}
		
		return normals;
	}
	
	private void updateBoundingBox(float x, float y, float z) {
		min.setX((x < min.getX()) ? x : min.getX());
		min.setY((y < min.getY()) ? y : min.getY());
		min.setZ((z < min.getZ()) ? z : min.getZ());
		
		max.setX((x > max.getX()) ? x : max.getX());
		max.setY((y > max.getY()) ? y : max.getY());
		max.setZ((z > max.getZ()) ? z : max.getZ());
		
		updateDimensions();		
	}
	
	private void updateDimensions() {
		size.setX(max.getX() - min.getX());	// width
		size.setY(max.getY() - min.getY());	// height
		size.setZ(max.getZ() - min.getZ());	// length
	}
	
}