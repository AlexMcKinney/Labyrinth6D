package alex.labyrinth.physical;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import alex.buffer.BufferMaster;
import alex.buffer.Bufferable;
import alex.geometry.base.Force;
import alex.geometry.base.Vertex;
import alex.geometry.shape.Cylinder;
import alex.geometry.shape.Sphere;
import alex.labyrinth.blueprints.Cell;
import alex.labyrinth.blueprints.CellStructure;
import alex.labyrinth.blueprints.Side;
import alex.labyrinth.visual.images.ColorStructure;

public class Maze {
	private static final double HINT_RADIUS = 1.0;
	private static final double HINT_OPACITY = 0.1;
	
	public static enum Alignment{XYZ, UVW}
	
	private CellStructure struct;
	
	private BufferMaster buffer = new BufferMaster(1000);
	private BufferMaster hintBuffer = new BufferMaster(1000);
	
	private List<Room> rooms = new ArrayList<Room>();
	private Map<String, Wall> walls = new HashMap<String, Wall>();
	
	
	public Maze(CellStructure struct, ColorStructure colors, Alignment align, Cell player){
		this.struct = struct;
		
		switch(align){
		case XYZ:
			for(int x=0; x < struct.x; x++){
				for(int y=0; y < struct.y; y++){
					for(int z=0; z < struct.z; z++){
						Cell cell = struct.getCell(x, y, z, player.u, player.v, player.w);
						List<Wall> list = new ArrayList<Wall>();
						
						//only do back sides unless it is at index 0
						if(x == struct.x-1){
							list.add(new Wall(struct.getFrontSideFor(Side.X_INDEX, cell)));
						}
						if(y == struct.y-1){
							list.add(new Wall(struct.getFrontSideFor(Side.Y_INDEX, cell)));
						}
						if(z == struct.z-1){
							list.add(new Wall(struct.getFrontSideFor(Side.Z_INDEX, cell)));
						}
						
						list.add(new Wall(struct.getBackSideFor(Side.X_INDEX, cell)));
						list.add(new Wall(struct.getBackSideFor(Side.Y_INDEX, cell)));
						list.add(new Wall(struct.getBackSideFor(Side.Z_INDEX, cell)));
						
						this.rooms.add(new Room(cell, align));
						
						for(Wall wall : list){
							this.walls.put(wall.getSide().toString(), wall);
							wall.setupShape(struct, colors, align);
						}
					}
				}
			}
			break;
		case UVW :
			for(int u=0; u < struct.u; u++){
				for(int v=0; v < struct.v; v++){
					for(int w=0; w < struct.w; w++){
						Cell cell = struct.getCell(player.x, player.y, player.z, u, v, w);
						List<Wall> list = new ArrayList<Wall>();
						
						//only do back sides unless it is at index 0
						if(u == struct.u-1){
							list.add(new Wall(struct.getFrontSideFor(Side.U_INDEX, cell)));
						}
						if(v == struct.v-1){
							list.add(new Wall(struct.getFrontSideFor(Side.V_INDEX, cell)));
						}
						if(w == struct.w-1){
							list.add(new Wall(struct.getFrontSideFor(Side.W_INDEX, cell)));
						}
						
						list.add(new Wall(struct.getBackSideFor(Side.U_INDEX, cell)));
						list.add(new Wall(struct.getBackSideFor(Side.V_INDEX, cell)));
						list.add(new Wall(struct.getBackSideFor(Side.W_INDEX, cell)));
						
						this.rooms.add(new Room(cell, align));
						
						for(Wall wall : list){
							this.walls.put(wall.getSide().toString(), wall);
							wall.setupShape(struct, colors, align);
						}
					}
				}
			}
			break;
		}
		
		for(Wall wall : this.walls.values()){
			this.buffer.add(wall);
		}
	}
	
	public void addTrophey(Bufferable trophy){
		this.buffer.add(trophy);
	}
	
	public List<Room> getRooms() {
		return rooms;
	}
	
	public Vertex getStartVertex(){
		return this.rooms.get(0).getCenter();
	}

	public BufferMaster getBuffer() {
		return buffer;
	}
	
	public BufferMaster getHintBuffer() {
		return hintBuffer;
	}
	
	public Vertex getVertex(Cell cell){
		for(Room room : this.rooms){
			if(cell.equals(room.getCell())){
				return room.getCenter();
			}
		}
		return null;
	}
	
	public Cell getCell(Vertex v){
		Room near = this.rooms.get(0);
		double min = Double.MAX_VALUE;
		for(Room r : this.rooms){
			double dist = v.dist(r.getCenter());
			if(dist < min){
				near = r;
				min = dist;
			}
		}
		return near.getCell();
	}
	
	public void openPath(Path path){
		for(int i=0; i < path.size()-1; i++){
			Cell cell = path.get(i);
			Cell next = path.get(i+1);
			
			Side side = this.struct.getSideBetween(cell, next);
			Wall wall = this.walls.get(side.toString());
			if(wall != null){
				wall.setOpen();
				this.buffer.setVisible(wall, false);
			}
			
			if(path.isReal()){
				Vertex v1 = this.getVertex(cell);
				Vertex v2 = this.getVertex(next);
				if(v1 != null){
					Sphere sphere = new Sphere(v1, HINT_RADIUS);
					sphere.setOpacity(HINT_OPACITY);
					this.hintBuffer.add(sphere);
				}
				if(v1 != null && v2 != null){
					Cylinder cy = new Cylinder(v1, v2, HINT_RADIUS);
					cy.setOpacity(HINT_OPACITY);
					this.hintBuffer.add(cy);
					
				}
			}
		}
	}
	
	public Force getForceOn(Vertex v, Force init){
		Force temp = new Force(0,0,0);
		for(Wall wall : this.walls.values()){
			Force sub = wall.getForceOn(v, init);
			if(sub != null){
				temp = temp.plus(sub);
			}
		}
		return temp;
	}
}
