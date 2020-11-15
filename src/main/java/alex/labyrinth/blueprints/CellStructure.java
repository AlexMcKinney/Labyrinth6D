package alex.labyrinth.blueprints;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import alex.labyrinth.physical.Maze.Alignment;

public class CellStructure {
	//sizes
	public int x=1, y=1, z=1, u=1, v=1, w=1;
	
	private Map<String, Cell> cells = new HashMap<String, Cell>();
	private Map<String, Side> sides = new HashMap<String, Side>();
	

	public CellStructure(int x, int y, int z) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.setupCells();
	}
	
	public CellStructure(int x, int y, int z, int u, int v, int w) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.u = u;
		this.v = v;
		this.w = w;
		this.setupCells();
	}
	
	public int size(){
		return this.cells.size();
	}
	
	public Map<String, Cell> getCells(){
		return this.cells;
	}
	
	public Map<String, Side> getSides(){
		return this.sides;
	}
	
	private void setupCells(){
		for(int a=0; a < x; a++){
			for(int b=0; b < y; b++){
				for(int c=0; c < z; c++){
					for(int d=0; d < u; d++){
						for(int e=0; e < v; e++){
							for(int f=0; f < w; f++){
								this.addCell(new Cell(a, b, c, d, e, f));
							}
						}
					}
				}
			}
		}
		
		for(int a=0; a < x; a++){
			for(int b=0; b < y; b++){
				for(int c=0; c < z; c++){
					for(int d=0; d < u; d++){
						for(int e=0; e < v; e++){
							for(int f=0; f < w; f++){
								addSide(new Side(Side.X_INDEX, a, b, c, d, e, f));
								addSide(new Side(Side.Y_INDEX, a, b, c, d, e, f));
								addSide(new Side(Side.Z_INDEX, a, b, c, d, e, f));
								
								addSide(new Side(Side.X_INDEX, a+1, b, c, d, e, f));
								addSide(new Side(Side.Y_INDEX, a, b+1, c, d, e, f));
								addSide(new Side(Side.Z_INDEX, a, b, c+1, d, e, f));
								
								addSide(new Side(Side.U_INDEX, a, b, c, d, e, f));
								addSide(new Side(Side.V_INDEX, a, b, c, d, e, f));
								addSide(new Side(Side.W_INDEX, a, b, c, d, e, f));
								
								addSide(new Side(Side.U_INDEX, a, b, c, d+1, e, f));
								addSide(new Side(Side.V_INDEX, a, b, c, d, e+1, f));
								addSide(new Side(Side.W_INDEX, a, b, c, d, e, f+1));
							}
						}
					}
				}
			}
		}
	}
	
	private void addCell(Cell cell){
		this.cells.put(cell.toString(), cell);
	}
	
	public void addSide(Side side){
		this.sides.put(side.toString(), side);
	}
		
	public boolean isOuterSide(Side side, Alignment align){
		switch(align){
		case XYZ: 
			switch(side.a){
			case Side.X_INDEX: return (side.x == 0 || side.x == this.x);
			case Side.Y_INDEX: return (side.y == 0 || side.y == this.y);
			case Side.Z_INDEX: return (side.z == 0 || side.z == this.z);
			}
		case UVW: 
			switch(side.a){
			case Side.U_INDEX: return (side.u == 0 || side.u == this.u);
			case Side.V_INDEX: return (side.v == 0 || side.v == this.v);
			case Side.W_INDEX: return (side.w == 0 || side.w == this.w);
			}
		}
		return (
				side.x == 0 || side.x == this.x ||
				side.y == 0 || side.y == this.y ||
				side.z == 0 || side.z == this.z ||
				side.u == 0 || side.u == this.u ||
				side.v == 0 || side.v == this.v ||
				side.w == 0 || side.w == this.w
				);
	}
	
	public Set<Cell> getAdjacentCells(int x, int y, int z, int u, int v, int w){
		Set<Cell> set = new HashSet<Cell>();
		set.add(this.getCell(x-1, y, z, u, v, w));
		set.add(this.getCell(x+1, y, z, u, v, w));
		set.add(this.getCell(x, y-1, z, u, v, w));
		set.add(this.getCell(x, y+1, z, u, v, w));
		set.add(this.getCell(x, y, z-1, u, v, w));
		set.add(this.getCell(x, y, z+1, u, v, w));
		set.add(this.getCell(x, y, z, u-1, v, w));
		set.add(this.getCell(x, y, z, u+1, v, w));
		set.add(this.getCell(x, y, z, u, v-1, w));
		set.add(this.getCell(x, y, z, u, v+1, w));
		set.add(this.getCell(x, y, z, u, v, w-1));
		set.add(this.getCell(x, y, z, u, v, w+1));
		set.remove(null);
		return set;
	}
	
	public Set<Cell> getAdjacentCells(Cell cell){
		return getAdjacentCells(cell.x, cell.y, cell.z, cell.u, cell.v, cell.w);
	}
	
	public Cell getCell(int x, int y, int z, int u, int v, int w){
		return this.cells.get(Cell.getKey(x, y, z, u, v, w));
	}
	
	
	public Set<Cell> getCellsWhere(Integer x, Integer y, Integer z, Integer u, Integer v, Integer w){
		Set<Cell> cells = new HashSet<Cell>();
		for(Cell cell : this.cells.values()){
			if(allows(cell.x, x) && allows(cell.y, y) && allows(cell.z, z) && allows(cell.u, u) && allows(cell.v, v) && allows(cell.w, w)){
				cells.add(cell);
			}
		}
		return cells;		
	}
	
	private boolean allows(Integer i1, Integer i2){
		if(i1 == null || i2 == null){
			return true;
		}
		return (i1 == i2);
	}
	

	/**
	 * Get the Cell associated with the given back-end Side
	 * should return NULL for outer front edges
	 * */
	public Cell getCellFor(Side side){
		return this.getCell(side.x, side.y, side.z, side.u, side.v, side.w);
	}
	
	public Side getBackSideFor(int axis, Cell to){
		return new Side(axis, to.x, to.y, to.z, to.u, to.v, to.w);
	}
	
	public Side getFrontSideFor(int axis, Cell to){
		int x=0, y=0, z=0, u=0, v=0, w=0;
		switch(axis){
		case Side.X_INDEX: x = 1; break;
		case Side.Y_INDEX: y = 1; break;
		case Side.Z_INDEX: z = 1; break;
		case Side.U_INDEX: u = 1; break;
		case Side.V_INDEX: v = 1; break;
		case Side.W_INDEX: w = 1; break;
		}
		return new Side(axis, to.x+x, to.y+y, to.z+z, to.u+u, to.v+v, to.w+w);
	}
	
	public Side getSideBetween(Cell from, Cell to){
		//assumed to be an adjacent Cell, can be in either order
		if(to.x < from.x){
			return this.getBackSideFor(Side.X_INDEX, from);
		}
		else if(to.x > from.x){
			return this.getBackSideFor(Side.X_INDEX, to);
		}
		else if(to.y < from.y){
			return this.getBackSideFor(Side.Y_INDEX, from);
		}
		else if(to.y > from.y){
			return this.getBackSideFor(Side.Y_INDEX, to);
		}
		else if(to.z < from.z){
			return this.getBackSideFor(Side.Z_INDEX, from);
		}
		else if(to.z > from.z){
			return this.getBackSideFor(Side.Z_INDEX, to);
		}
		else if(to.u < from.u){
			return this.getBackSideFor(Side.U_INDEX, from);
		}
		else if(to.u > from.u){
			return this.getBackSideFor(Side.U_INDEX, to);
		}
		else if(to.v < from.v){
			return this.getBackSideFor(Side.V_INDEX, from);
		}
		else if(to.v > from.v){
			return this.getBackSideFor(Side.V_INDEX, to);
		}
		else if(to.w < from.w){
			return this.getBackSideFor(Side.W_INDEX, from);
		}
		else if(to.w > from.w){
			return this.getBackSideFor(Side.W_INDEX, to);
		}
		else{
			return null;
		}
	}
	
	public Cell getRandomCell(){
		Random rand = new Random();
		return this.getCell(rand.nextInt(this.x), rand.nextInt(this.y), rand.nextInt(this.z), rand.nextInt(this.u), rand.nextInt(this.v), rand.nextInt(this.w));
	}
}
