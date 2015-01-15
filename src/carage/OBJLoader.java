package carage;

import static org.lwjgl.opengl.GL11.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Scanner;

import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import carage.utils.ArrayUtils;

// http://www.martinreddy.net/gfx/3d/OBJ.spec
public class OBJLoader {

	private Mesh mesh = new Mesh();
	
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
	
	public Mesh getMesh() {
		return mesh;
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
		String[] tokens = line.split(" ");
		
		switch (tokens[0]) {
			case "v":
				// eat: x y z; ignore: w
				processGeometryVertex(tokens[1], tokens[2], tokens[3]); 
				break;
			case "vt":
				// eat: u v; ignore: w
				processTextureVertex(tokens[1], tokens[2]);
				break;
			case "vn":
				// eat: i j k
				processVertexNormal(tokens[1], tokens[2], tokens[3]);
				break;
			case "f":
				// hand over everything but the first element ("f")
				// TODO fetch this from the passed line instead of copying the array?
				processFace(Arrays.copyOfRange(tokens, 1, tokens.length));
				break;
			default:
				// we ran into one of many thing we don't care about (yet)
		}
	}
	
	private void processGeometryVertex(String x, String y, String z) {
		float xValue = Float.parseFloat(x);
		float yValue = Float.parseFloat(y);
		float zValue = Float.parseFloat(z);
		
		v.add(new Vector3f(xValue, yValue, zValue));
		updateBoundingBox(xValue, yValue, zValue);
	}
	
	private void processTextureVertex(String u, String v) {
		vt.add(new Vector2f(Float.parseFloat(u), 1 - Float.parseFloat(v)));
	}
	
	private void processVertexNormal(String x, String y, String z) {
		vn.add(new Vector3f(Float.parseFloat(x), Float.parseFloat(y), Float.parseFloat(z)));
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
		if (vertices.length < 3) { return; } // We need at least 3 vertices. TODO: Better throw an exception here?
		
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
			vnIndex = (parts[1].equals("")) ? vnIndex : Integer.parseInt(parts[1]) - 1;
			vtIndex = Integer.parseInt(parts[2]) - 1;
		}
		
		if (vIndex  >= 0) { vert.setPosition(v.get(vIndex)); }
		if (vnIndex >= 0) { vert.setNormal(vn.get(vnIndex)); }
		if (vtIndex >= 0) { vert.setUnwrap(vt.get(vtIndex)); }
		
		return vert;
	}
	
	public void debugOutput() {
		System.out.println("[OBJ Information]");
		System.out.println("Number of v/vn/vt: "+v.size()+"/"+vn.size()+"/"+vt.size());
		System.out.println("Unique Vertices  : "+vertexList.size());
		System.out.println("Number of Indices: "+idx.size());
		System.out.println("IBO savings      : "+(v.size() - vertexList.size()));
		System.out.println("Object Dimensions: "+size.getX()+" x "+size.getY()+" x "+size.getZ());
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
		size.setX(max.getX()- min.getX());	// width
		size.setY(max.getY()- min.getY());	// height
		size.setZ(max.getZ()- min.getZ());	// length
	}
	
}