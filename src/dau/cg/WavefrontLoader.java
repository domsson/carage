package dau.cg;

import static org.lwjgl.opengl.GL11.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

// http://www.martinreddy.net/gfx/3d/OBJ.spec
public class WavefrontLoader {

	private Mesh mesh = new Mesh();
	
	public WavefrontLoader(String resourceName) {
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
		mesh.addVertex(Float.parseFloat(x), Float.parseFloat(y), Float.parseFloat(z));
	}
	
	private void processTextureVertex(String u, String v) {
		mesh.addUV(Float.parseFloat(u), Float.parseFloat(v));
	}
	
	private void processVertexNormal(String x, String y, String z) {
		
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
	
	// TODO this is ugly... like the rest of this class!
	private void processFace(String p1, String p2, String p3) {
		int[][] points = new int[3][];
		points[0] = convertToPoint(p1);
		points[1] = convertToPoint(p2);
		points[2] = convertToPoint(p3);
		mesh.makeFace(points);
	}
	
	// TODO this is ugly... like the rest of this class!
	private void processFace(String p1, String p2, String p3, String p4) {
		int[][] points = new int[4][];
		points[0] = convertToPoint(p1);
		points[1] = convertToPoint(p2);
		points[2] = convertToPoint(p3);
		points[3] = convertToPoint(p4);
		// TRIANGULATION, yo!
		mesh.makeFace(new int[][] {points[0], points[1], points[2]});
		mesh.makeFace(new int[][] {points[2], points[3], points[0]});
	}
	
	private int[] convertToPoint(String p) {
		if (p.contains("/")) {
			String[] parts = p.split("/");
			return new int[] { Integer.parseInt(parts[0])-1, Integer.parseInt(parts[1])-1 };
		}
		return new int[] { Integer.parseInt(p)-1 };
	}
	
}
