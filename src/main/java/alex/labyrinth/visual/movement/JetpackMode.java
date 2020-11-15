package alex.labyrinth.visual.movement;

import alex.geometry.angle.rotate.RayRotator;
import alex.geometry.base.Force;
import alex.geometry.base.Vector;
import alex.geometry.base.Vertex;
import alex.labyrinth.visual.driver.KeyHandler;

public class JetpackMode extends MoveMode{
	public JetpackMode(KeyHandler handler){
		super(handler);
	}
	
	@Override
	public Force getForce(Vertex eye, Vertex aim){
		double lr = 0.0;
		double fb = 0.0;
		double ud = -0.05;
		
		if(keyDown('W')){
			fb -= move;
		}
		if(keyDown('S')){
			fb += move;
		}
		if(keyDown('A')){
			lr += move;
		}
		if(keyDown('D')){
			lr -= move;
		}
		if(keyDown(' ')){
			ud += 2.0*move;
		}
		
		RayRotator temp = new RayRotator(eye, new Vertex(aim.x, eye.y, aim.z));
		
		Vector v_lr = new Vector(eye.minus(eye.movedForth(lr, 
				temp.getLeftRight()[0])));
		Vector v_ud = new Vector(eye.translated(new Vertex(0, ud, 0)).minus(eye));
		Vector v_fb = new Vector(eye.minus(eye.movedForth(fb, aim)));
		
		return new Force(v_lr.plus(v_ud).plus(v_fb));
	}
}