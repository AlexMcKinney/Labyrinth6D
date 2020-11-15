package alex.labyrinth.visual.movement;

import alex.geometry.base.Force;
import alex.geometry.base.Vector;
import alex.geometry.base.Vertex;
import alex.labyrinth.visual.driver.KeyHandler;

public class SpaceshipMode extends MoveMode{
	public SpaceshipMode(KeyHandler handler){
		super(handler);
	}
	
	@Override
	public Force getForce(Vertex eye, Vertex aim){
		if(keyDown(' ')){
			return new Force(new Vector(eye.movedForth(move, aim).minus(eye)));
		}
		return new Force(0,0,0);
	}
}
