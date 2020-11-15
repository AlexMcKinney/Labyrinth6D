package alex.labyrinth.physical;

import alex.geometry.base.Vertex;
import alex.labyrinth.blueprints.Cell;
import alex.labyrinth.physical.Maze.Alignment;

public class Room {
	protected static final double ROOM_WIDTH = 30.0;
	
	private Cell cell;
	private Vertex center; 
	
	
	public Room(Cell cell, Alignment alignment){
		this.cell = cell;
		
		switch(alignment){
		case XYZ: 
			this.center = new Vertex(cell.x * ROOM_WIDTH, cell.y * ROOM_WIDTH, cell.z * ROOM_WIDTH);
			break;
		case UVW:
			this.center = new Vertex(cell.u * ROOM_WIDTH, cell.v * ROOM_WIDTH, cell.w * ROOM_WIDTH);
			break;
		}
	}
	
	public Vertex getCenter(){
		return this.center;
	}
	
	public Cell getCell(){
		return this.cell;
	}
	
	@Override
	public String toString(){
		return this.cell.toString()+" = "+this.center.toString();
	}
}
