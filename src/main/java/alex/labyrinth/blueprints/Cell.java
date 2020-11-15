package alex.labyrinth.blueprints;

public class Cell {
	//index
	public int x=0, y=0, z=0, u=0, v=0, w=0;
	
	
	public Cell(int x, int y, int z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	public Cell(int x, int y, int z, int u, int v, int w) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.u = u;
		this.v = v;
		this.w = w;
	}
	
	@Override
	public boolean equals(Object obj){
		if(obj instanceof Cell){
			return this.equals((Cell)obj);
		}
		return false;
	}
	
	public boolean equals(Cell cell){
		if(cell == null){
			return false;
		}
		return this.toString().equals(cell.toString());
	}
	
	@Override
	public String toString(){
		return getKey(x, y, z, u, v, w);
	}
		
	public static String getKey(int x, int y, int z, int u, int v, int w){
		return "[" + x + ", " + y + ", " + z + ", " + u + ", " + v + ", " + w+"]";
	}
}
