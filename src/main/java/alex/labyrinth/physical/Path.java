package alex.labyrinth.physical;

import java.util.ArrayList;
import java.util.List;

import alex.labyrinth.blueprints.Cell;

@SuppressWarnings("serial")
public class Path extends ArrayList<Cell>{
	private boolean real = true;
	
	public Path(){
		super();
	}
	
	public Path(List<Cell> path){
		super(path);
	}
	
	boolean isReal(){
		return this.real;
	}
	
	public Path(List<Cell> path, boolean real){
		super(path);
		this.real = real;
	}
	
	public static List<Path> convert(List<List<Cell>> paths, boolean real){
		List<Path> list = new ArrayList<Path>();
		for(List<Cell> temp : paths){
			list.add(new Path(temp, real));
		}
		return list;
	}
}
