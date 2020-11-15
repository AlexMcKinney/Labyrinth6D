package alex.labyrinth.visual.images;

import java.util.ArrayList;
import java.util.List;

import alex.buffer.render.Color;
import alex.buffer.render.triangle.RenderingTriangle;
import alex.geometry.base.Vertex;
import alex.geometry.shape.Cylinder;
import alex.geometry.shape.Rectangle;
import alex.geometry.shape.Shape;

public class OuterWallImage extends WallImage{
	private static final double CYLINDER_RADIUS = 5.0;
	
	private Shape shape;
	private Cylinder c1, c2;
	
	public OuterWallImage(Rectangle rec){
		this.shape = rec;
		rec.setColor(Color.WHITE.mult(0.3+ Math.random()/2.0));
		
		Color[] ends = new Color[]{Color.BLACK};
		Color[] sides = new Color[]{Color.WHITE.mult(0.5), Color.WHITE.mult(0.4)};
		
		Vertex v1 = rec.getVertex(0);
		Vertex v2 = rec.getFarthestFrom(v1);
		this.c1 = new Cylinder(v1, v2, CYLINDER_RADIUS, sides, ends);
		
		Vertex v3 = rec.getVertex(2);
		Vertex v4 = rec.getFarthestFrom(v3);		
		this.c2 = new Cylinder(v3, v4, CYLINDER_RADIUS, sides, ends);
	}

	@Override
	public List<RenderingTriangle> getRenderingTriangles() {
		List<RenderingTriangle> list = new ArrayList<RenderingTriangle>();
		list.addAll(this.shape.getRenderingTriangles());
		list.addAll(this.c1.getRenderingTriangles());
		list.addAll(this.c2.getRenderingTriangles());
		return list;
	}

	@Override
	public OuterWallImage toOuterWall() {
		return this;
	}

}
