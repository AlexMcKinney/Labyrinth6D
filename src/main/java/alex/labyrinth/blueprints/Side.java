package alex.labyrinth.blueprints;

/**
 * This represents a side between cells, defined by index
 *  It corresponds to a "back" side of a Cell at this index
 * */
public class Side implements Comparable<Side>{
	public static final int X_INDEX=0, Y_INDEX=1, Z_INDEX=2, U_INDEX=3, V_INDEX=4, W_INDEX=5;
	
	
	//index
	public int x=0, y=0, z=0, u=0, v=0, w=0;
	//axis
	public int a=0;
	
	
	public Side(int axis, int x, int y, int z, int u, int v, int w){
		this.a = axis;
		this.x = x;
		this.y = y;
		this.z = z;
		this.u = u;
		this.v = v;
		this.w = w;
	}
		
	@Override
	public String toString(){
		return "{"+a+"}["+x+", "+y+", "+z+", "+u+", "+v+", "+w+"]";
	}

	@Override
	public int compareTo(Side arg0) {
		return this.toString().compareTo(arg0.toString());
	}
	
	@Override
	public int hashCode(){
		return this.toString().hashCode();
	}
	
	@Override
	public boolean equals(Object obj){
		return this.toString().equals(obj.toString());
	}
}
