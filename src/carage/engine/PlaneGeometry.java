package carage.engine;

import org.lwjgl.util.vector.Vector3f;

public class PlaneGeometry extends Geometry {
	
	private VertexBufferObject positionVBO = null;
	private VertexBufferObject normalsVBO = null;
	private VertexBufferObject textureVBO = null;
	
	public PlaneGeometry() {
		buildPositionVBO();
		buildNormalsVBO();
		buildTextureVBO();
		buildVAO();
		buildIBO();
		buildBoundingBox();
	}
	
	private void buildPositionVBO() {
		float[] positions = {
			-0.5f, -0.5f, 0.0f, // 0
			 0.5f, -0.5f, 0.0f,	// 1
			 0.5f,  0.5f, 0.0f,	// 2
			 					// 0
			 					// 2
			-0.5f,  0.5f, 0.0f	// 3
			
		};
		positionVBO = new VertexBufferObject(positions, 3);
	}
	
	private void buildNormalsVBO() {
		float[] normals = {
				0f, 0f, 1f,	// 0
				0f, 0f, 1f,	// 1
				0f, 0f, 1f,	// 2
							// 0
							// 2
				0f, 0f, 1f	// 3
		};
		normalsVBO = new VertexBufferObject(normals, 3);	
	}
	
	private void buildTextureVBO() {
		float[] unwraps = {
				0f, 1f,	// 0
				1f, 1f,	// 1
				1f, 0f,	// 2
						// 0
						// 2
				0f, 0f	// 3
				
		};
		textureVBO = new VertexBufferObject(unwraps, 2);
	}
	
	private void buildIBO() {
		int[] indices = { 0, 1, 2, 0, 2, 3};
		ibo = new IndexBufferObject(indices);
	}
	
	private void buildVAO() {
		vao = new VertexArrayObject();
		vao.addVBO(positionVBO, ShaderAttribute.POSITION);
		vao.addVBO(normalsVBO, ShaderAttribute.NORMALS);
		vao.addVBO(textureVBO, ShaderAttribute.TEXTURE);		
	}
	
	private void buildBoundingBox() {
		Vector3f min = new Vector3f(-0.5f, -0.5f, -0.5f);
		Vector3f max = new Vector3f( 0.5f,  0.5f,  0.5f);
		boundingBox = new BoundingBox(min, max);
	}
	
}
