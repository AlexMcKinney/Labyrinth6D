package alex.labyrinth.blueprints.path;

import java.util.ArrayList;
import java.util.Collections;

import alex.labyrinth.blueprints.Cell;

@SuppressWarnings("serial")
public class SimplePath extends ArrayList<Cell>{
	public int stackDepth=0; 
	
	public SimplePath(){
		super();
	}
	
	public Cell getFirst(){
		if(this.isEmpty()){
			return null;
		}
		return this.get(0);
	}
	
	public Cell getLast(){
		if(this.isEmpty()){
			return null;
		}
		return this.get(this.size()-1);
	}
	
	public void reverse(){
		Collections.reverse(this);
	}
	
	public String getSummary(){
		return "["+this.size()+"] "+this.getFirst()+" => "+this.getLast()+" {"+this.stackDepth+"}";
	}
}
