package alex.labyrinth.blueprints.path;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import alex.labyrinth.blueprints.Cell;
import alex.labyrinth.blueprints.CellStructure;

public class PathBuilder{
	//recursion is done in batches of this size
	private static final int STACK_LIMIT = 1000;
	//tries to make a path may fail
	private static final int RETRY_LIMIT = 10;
	
	
	private CellStructure struct;
	
	public PrintStream log;
	
	public PathBuilder(CellStructure struct){
		this.struct = struct;
		
		try {
			this.log = new PrintStream(new FileOutputStream("pathLog.txt"));
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
	
	/**
	 * Recursively generate a random path through the CellStructure from 'start' to 'end'
	 * Calls attemptPath() until it succeeds
	 * */
	public SimplePath buildPath(Cell start, Cell end){
		for(int i=0; i < RETRY_LIMIT; i++){
			SimplePath path = this.attemptPath(start, end);
			if(end.equals(path.getLast())){
				return path;
			}
		}
		return null;
	}
	
	/**
	 * Recursively generate a random path through the CellStructure from 'start' to 'end'
	 * May fail to return a full path if it hits a dead end
	 * */
	public SimplePath attemptPath(Cell start, Cell end){
		List<SimplePath> paths = new ArrayList<SimplePath>();

		List<Cell> ignore = new ArrayList<Cell>();
		
		Cell current = start;
		Cell last = start;
		while(last != null && end.equals(last) == false){
			SimplePath sub = new SimplePath();
			findRandomPath(current, end, ignore, sub, 0);
			sub.reverse();
			println("*sub-path "+sub.getSummary());
			
			last = sub.getLast();
			current = last;
			paths.add(sub);
		}
		
		SimplePath full = compilePath(paths);
		this.log.close();
		return full;
	}
	
	/**
	 * Recursively generate a random path through the CellStructure
	 * from the 'start' towards the 'end'
	 * stops upon finding target "end" or at going too deep 
	 * */
	private void findRandomPath(Cell current, Cell end, List<Cell> ignore, SimplePath answer, int debugDepth){
		//if answer is not empty, then the algorithm was finished in another branch
		if(answer.isEmpty() == false){
			println("terminate findRandomPath "+debugDepth+" to "+end);
			return;
		}
		if(debugDepth >= STACK_LIMIT){
			println("abort findRandomPath "+debugDepth+" to "+end);
			answer.stackDepth = debugDepth;
			return;			
		}
				
		println("findRandomPath "+debugDepth+" "+current+" => "+end);
				
		ignore.add(current);
		if(current.equals(end)){
			answer.add(current);
			answer.stackDepth = debugDepth;
			return;
		}

		List<Cell> validRooms = new ArrayList<Cell>(struct.getAdjacentCells(current));
		validRooms.removeAll(ignore);
		if(validRooms.isEmpty()){
			println("WARNING findRandomPath() dead-end");
			answer.stackDepth = debugDepth;
			return;
		}
		
		Collections.shuffle(validRooms);
		for(Cell next : validRooms){
			findRandomPath(next, end, ignore, answer, debugDepth+1);
		}
		//recursively add path after one is found
		answer.add(current);
	}
	
	
	private static SimplePath compilePath(List<SimplePath> paths) {
		SimplePath full = new SimplePath();
		full.add(paths.get(0).get(0));
		for(SimplePath path : paths){
			full.stackDepth += path.stackDepth;
			for(int i=1; i < path.size(); i++){
				full.add(path.get(i));
			}
		}
		return full;
	}
	
	private void println(Object obj){
		log.println(obj);
	}
}
