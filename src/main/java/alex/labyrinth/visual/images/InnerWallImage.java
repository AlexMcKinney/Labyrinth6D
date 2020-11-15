package alex.labyrinth.visual.images;

import java.util.List;

import alex.buffer.render.Color;
import alex.buffer.render.triangle.RenderingTriangle;
import alex.geometry.base.Vertex;
import alex.geometry.shape.Rectangle;

public class InnerWallImage extends WallImage{
	private Rectangle shape;
	
	
	public InnerWallImage(Vertex v1, Vertex v2, Vertex v3, Vertex v4, Color color){
		this.shape = new Rectangle(v1, v2, v3, v4, color);
	}
	
	@Override
	public OuterWallImage toOuterWall() {
		return new OuterWallImage(shape);
	}
	
	@Override
	public List<RenderingTriangle> getRenderingTriangles() {
		return this.shape.getRenderingTriangles();
	}
}
