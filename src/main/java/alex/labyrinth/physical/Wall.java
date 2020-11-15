package alex.labyrinth.physical;

import java.util.Arrays;
import java.util.List;

import alex.buffer.Bufferable;
import alex.buffer.render.triangle.RenderingTriangle;
import alex.geometry.base.CachedPlane;
import alex.geometry.base.Force;
import alex.geometry.base.Line;
import alex.geometry.base.Vector;
import alex.geometry.base.Vertex;
import alex.geometry.shape.Rectangle;
import alex.geometry.simple.CachedConvexPolygon;
import alex.labyrinth.blueprints.CellStructure;
import alex.labyrinth.blueprints.Side;
import alex.labyrinth.physical.Maze.Alignment;
import alex.labyrinth.visual.images.ColorStructure;
import alex.labyrinth.visual.images.InnerWallImage;
import alex.labyrinth.visual.images.OuterWallImage;
import alex.labyrinth.visual.images.WallImage;

public class Wall implements Bufferable{
	public static double FORCE_DISTANCE = 1.0;
	
	private CachedConvexPolygon polygon;
	private CachedPlane plane;
	
	private Side side;
	private boolean closed = true;
	private WallImage image;
	
	
	public Wall(Side side){
		this.side = side;
	}
	
	public boolean isClosed(){
		return this.closed;
	}
	
	public void setOpen(){
		this.closed = false;
	}
	
	public void setupShape(CellStructure struct, ColorStructure colors, Alignment align){
		Vertex roomCenter = new Vertex(0,0,0);
		
		switch(align){
		case XYZ:
			roomCenter.x = this.side.x * Room.ROOM_WIDTH;
			roomCenter.y = this.side.y * Room.ROOM_WIDTH;
			roomCenter.z = this.side.z * Room.ROOM_WIDTH;
			break;
		case UVW:
			roomCenter.x = this.side.u * Room.ROOM_WIDTH;
			roomCenter.y = this.side.v * Room.ROOM_WIDTH;
			roomCenter.z = this.side.w * Room.ROOM_WIDTH;
			break;
		}
		this.setupShape(struct, colors, align, roomCenter);
	}
	
	public void setupShape(CellStructure struct, ColorStructure colors, Alignment align, Vertex roomCenter){
		Vertex v1=roomCenter.clone(), v2=roomCenter.clone(), v3=roomCenter.clone(), v4=roomCenter.clone();
		
		double dist = Room.ROOM_WIDTH/2;
		double offset = -dist;
		
		switch(side.a){
		case Side.X_INDEX:
		case Side.U_INDEX: 
			v1.translate(offset, -dist, dist);
			v2.translate(offset, dist, dist);
			v3.translate(offset, dist, -dist);
			v4.translate(offset, -dist, -dist);
			break;
		case Side.Y_INDEX:
		case Side.V_INDEX:
			v1.translate(-dist, offset, dist);
			v2.translate(dist, offset, dist);
			v3.translate(dist, offset, -dist);
			v4.translate(-dist, offset, -dist);
			break;
		case Side.Z_INDEX:
		case Side.W_INDEX:
			v1.translate(-dist, dist, offset);
			v2.translate(dist, dist, offset);
			v3.translate(dist, -dist, offset);
			v4.translate(-dist, -dist, offset);
			break;
		}
		
		
		this.polygon = new CachedConvexPolygon(Arrays.asList(v1, v2, v3, v4));
		this.plane = new CachedPlane(v1, v2, v3);
		
		
		if(struct.isOuterSide(this.side, align)){
			this.image = new OuterWallImage(new Rectangle(v1, v2, v3, v4));
		}
		else{
			this.image = new InnerWallImage(v1, v2, v3, v4, colors.getColor(side));
		}
	}
	
	//return a modifier of the initial force
	public Force getForceOn(Vertex v, Force init){
		if(this.closed == false){
			return null;
		}
		//System.out.println("Wall "+this.center+" "+this.plane.getNormalVectorTo(v)+" getForceOn() "+v+" => "+init);
		Vertex to = v.plus(init.asVertex());
		Vertex inter = polygon.getIntersection(new Line(v, to));
		if(inter == null || v.dist(inter) > FORCE_DISTANCE){
			return null;
		}
				
		
		//let point "slide" along wall--only cancel part of vector
		//double h = init.asVertex().length();
		//Angle a = RayRotator.getAngle(v.plus(init.asVertex()), v, inter);
		//cos(a) = d / h
		//double move = h * Math.cos(a.toRadians());
		//System.out.println(move);
		
		double magnitude = Math.abs(init.asVertex().x) + Math.abs(init.asVertex().y) + Math.abs(init.asVertex().z);
		Vector force = this.plane.getNormalVectorTo(v).mult(magnitude);
		//System.out.println(magnitude+" "+force);
		return new Force(force);
	}
	
	public Side getSide(){
		return this.side;
	}

	@Override
	public List<RenderingTriangle> getRenderingTriangles() {
		return this.image.getRenderingTriangles();
	}
	
	@Override
	public String toString(){
		return this.side+" "+this.closed;
	}
}
