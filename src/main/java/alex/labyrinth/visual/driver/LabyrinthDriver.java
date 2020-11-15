package alex.labyrinth.visual.driver;

import java.util.Date;

import alex.labyrinth.blueprints.Cell;
import alex.labyrinth.blueprints.CellStructure;
import alex.labyrinth.physical.ComplexMaze;
import alex.labyrinth.visual.images.ColorStructure;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class LabyrinthDriver{	
	
	public static void main(String[] args){
		System.out.println("Welcome to Labyrinth!");
		
		int size = 3;
		int x=size, y=size, z=size, u=size, v=size, w=size;
		int real = 1;
		int fake = 3;
		
		System.out.println("Enter dimensions <x> <y> <z> <u> <v> <w>:");
		System.out.println(x+" "+y+" "+z+" "+u+" "+v+" "+w+" ["+(x*y*z*u*v*w)+"]");
		System.out.println("Enter pathing <real> <fake>:");
		System.out.println(real+" "+fake);
		
		CellStructure struct = new CellStructure(x, y, z, u, v, w);
		ColorStructure colors = new ColorStructure(struct);
		
		ComplexMaze maze = new ComplexMaze(struct, colors);
		
		System.out.println("generating paths..." +new Date());
		
		Date start = new Date();
		maze.openPaths(real, fake);
		System.out.println("("+(new Date().getTime() - start.getTime())/1000+" seconds)");
				
		maze.enactPaths();
		
		System.out.println("visualizing world...");
		Cell player = new Cell(0,0,0,0,0,0);
		new World(maze, player);
		
	}
}
