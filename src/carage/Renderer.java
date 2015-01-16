package carage;

import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_INT;
import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL11.glDrawArrays;
import static org.lwjgl.opengl.GL11.glDrawElements;
import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13.glActiveTexture;
import static org.lwjgl.opengl.GL30.glBindVertexArray;

// TODO make this static or singleton or something?
public class Renderer {
		
	public static void renderAsset(Asset asset) {
		glActiveTexture(GL_TEXTURE0); // Why is this (apparently not) necessary? - Because GL_TEXTURE0 is the default!
		glBindTexture(GL_TEXTURE_2D, asset.getTextureId());
		renderVAO(asset.getVAO());
		glBindTexture(GL_TEXTURE_2D, 0);
	}
	
	public static void renderVAO(VertexArrayObject vao) {
		if (vao.hasIBO()) {
			renderVAO(vao, vao.getIBO());
		}
		else {
			renderVAO(vao.getId());
		}
	}
	
	public static void renderVAO(int vaoId) {
		glBindVertexArray(vaoId);
		glDrawArrays(GL_TRIANGLES, 0, 3);
		glBindVertexArray(0);
	}
	
	public static void renderVAO(VertexArrayObject vao, IndexBufferObject ibo) {
		vao.bind();
		ibo.bind();
		glDrawElements(GL_TRIANGLES, ibo.getSize(), GL_UNSIGNED_INT, 0);
		ibo.unbind();
		vao.unbind();
	}

}
