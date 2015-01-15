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

// http://www.martinreddy.net/gfx/3d/OBJ.spec
public class OBJLoader {

	private Mesh mesh = new Mesh();
	
	private ArrayList<Vector3f> v   = new ArrayList<>();
	private ArrayList<Vector2f> vt  = new ArrayList<>();
	private ArrayList<Vector3f> vn  = new ArrayList<>();
	
	private ArrayList<Integer>  idx = new ArrayList<>();
	private ArrayList<Vertex> vertexList = new ArrayList<>();
	
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
//		mesh.addVertex(Float.parseFloat(x), Float.parseFloat(y), Float.parseFloat(z));
		v.add(new Vector3f(Float.parseFloat(x), Float.parseFloat(y), Float.parseFloat(z)));
	}
	
	private void processTextureVertex(String u, String v) {
//		mesh.addUV(Float.parseFloat(u), Float.parseFloat(v));
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
	
	// TODO this is not what we need... we need to make a set of unique vertices (position AND normals AND unwrap different!)
	private void processFace(String p1, String p2, String p3) {

		Vertex v1 = faceIndicesToVertex(p1);
		Vertex v2 = faceIndicesToVertex(p2);
		Vertex v3 = faceIndicesToVertex(p3);
		Vertex[] vertices = new Vertex[] { v1, v2, v3 };
		
		int vertexIndex;
		for (int i=0; i<3; ++i) {
			vertexIndex = vertexList.indexOf(vertices[i]);
			if (vertexIndex >= 0) {
				idx.add(vertexIndex);
			}
			else {
				idx.add(vertexList.size());
				vertexList.add(vertices[i]);
			}
		}
	}
	
	// TODO see above
	private void processFace(String p1, String p2, String p3, String p4) {		
		processFace(p1, p2, p3);
		processFace(p3, p4, p1);
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
		return arrayListToIntArray(idx);
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
	
	// TODO warum funktioniert die kack toArray Methode nicht (so wie ich will)?
	private float[] arrayListToFloatArray(ArrayList<Float> list) {
		float[] array = new float[list.size()];
		for (int i=0; i<list.size(); ++i) {
			array[i] = list.get(i);
		}
		return array;
	}
	
	// TODO warum funktioniert die kack toArray Methode nicht (so wie ich will)?
	private int[] arrayListToIntArray(ArrayList<Integer> list) {
		int[] array = new int[list.size()];
		for (int i=0; i<list.size(); ++i) {
			array[i] = list.get(i);
		}
		return array;
	}
	
}