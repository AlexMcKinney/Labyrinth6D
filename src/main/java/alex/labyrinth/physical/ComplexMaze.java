package alex.labyrinth.physical;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import alex.geometry.base.Vertex;
import alex.geometry.shape.Sphere;
import alex.labyrinth.blueprints.Cell;
import alex.labyrinth.blueprints.CellStructure;
import alex.labyrinth.blueprints.path.PathBuilder;
import alex.labyrinth.blueprints.path.SimplePath;
import alex.labyrinth.physical.Maze.Alignment;
import alex.labyrinth.visual.images.ColorStructure;

public class ComplexMaze {
	private static final double TROPHY_SIZE = Room.ROOM_WIDTH / 10.0;
	
	private ColorStructure colors;
	private CellStructure struct;
	
	private Maze maze1, maze2;
	private List<Path> paths = new ArrayList<Path>();
	private Cell start, end;
	

	public ComplexMaze(CellStructure struct, ColorStructure colors){
		this(struct, colors, struct.getCell(0, 0, 0, 0, 0, 0), struct.getCell(struct.x-1, struct.y-1, struct.z-1, struct.u-1, struct.v-1, struct.w-1));
	}
	
	public ComplexMaze(CellStructure struct, ColorStructure colors, Cell start, Cell end){
		this.struct = struct;
		this.colors = colors;
		
		this.start = start;
		this.end = end;
		this.maze1 = new Maze(struct, colors, Alignment.XYZ, start);
		this.maze2 = new Maze(struct, colors, Alignment.UVW, start);
		this.enactPaths();
	}
	
	public void renewMainMaze(Cell player){
		this.maze1 = new Maze(struct, colors, Alignment.XYZ, player);
		this.enactPaths();
	}
	
	public void renewMetaMaze(Cell player){
		this.maze2 = new Maze(struct, colors, Alignment.UVW, player);
		this.enactPaths();
	}
	
	public List<Path> getPaths(){
		return this.paths;
	}
	
	public void printPlayerState(Cell current){
		System.out.print(current+" ");
		for(Path path : getPaths()){
			if(path.isReal()){
				System.out.print(path.indexOf(current)+1+" / "+path.size()+" ");
			}
		}
		System.out.println();
	}
	
	public void openPaths(int real, int fake){
		PathBuilder builder = new PathBuilder(this.struct);
		
		int finished_real=0, finished_fake=0;
		
		while(finished_real < real || finished_fake < fake){
			SimplePath path = builder.attemptPath(start, end);
			String status = finished_real+"/"+real+" "+finished_fake+"/"+fake+" "+new Date();
			//'real' path
			if(end.equals(path.getLast()) && finished_real < real){
				paths.add(new Path(path, true));
				finished_real++;
				status = finished_real+"/"+real+" "+finished_fake+"/"+fake+" "+new Date();
				System.out.println("Real Path "+path.getSummary()+" "+status);
			}
			//'fake' path
			else if(end.equals(path.getLast()) == false && finished_fake < fake){
				paths.add(new Path(path, false));
				finished_fake++;
				status = finished_real+"/"+real+" "+finished_fake+"/"+fake+" "+new Date();
				System.out.println("Fake Path "+path.getSummary()+" "+status);
			}
			else{
				System.out.println("Discard Path "+path.getSummary()+" "+status);
			}
		}
	}
	
	public void enactPaths(){
		for(Path path : paths){
			maze1.openPath(path);
			maze2.openPath(path);
		}
		
		Vertex v1 = maze1.getVertex(this.end);
		Vertex v2 = maze2.getVertex(this.end);
		
		if(v1 != null){
			maze1.addTrophey(new Sphere(v1, TROPHY_SIZE));
		}
		if(v2 != null){
			maze2.addTrophey(new Sphere(v2, TROPHY_SIZE));
		}
	}

	public Maze getMainMaze(){
		return this.maze1;
	}
	
	public Maze getMetaMaze(){
		return this.maze2;
	}
}
