package alex.labyrinth.visual.movement;

import java.awt.event.KeyEvent;

import alex.geometry.angle.Angle;
import alex.geometry.angle.Degrees;
import alex.geometry.base.Force;
import alex.geometry.base.Vertex;
import alex.geometry.camera.Camera;
import alex.labyrinth.physical.Maze;
import alex.labyrinth.visual.driver.KeyHandler;

public abstract class MoveMode {
	public static double move = 0.5;
	public static Angle turnUD = new Degrees(1.5);
	public static Angle turnLR = new Degrees(1.5);
	
	protected KeyHandler handler;
	
	
	protected MoveMode(KeyHandler handler){
		this.handler = handler;
	}
	
	/**
	 * Return TRUE if a key is in pressed-mode
	 * */
	protected boolean keyDown(int keyCode){
		return handler.keyDown(keyCode);
	}
	
	public Force getForce(Camera camera){
		this.applyTurn(camera);
		return this.getForce(camera.getEye(), camera.getAim());
	}
	
	protected abstract Force getForce(Vertex eye, Vertex aim);
		
	public void applyForce(Maze maze, Camera camera){
		this.applyTurn(camera);
		
		Force cameraForce = this.getForce(camera);
		//don't need to check walls if camera is holding still
		if(cameraForce.asVertex().length() > 0.01){
			Force walls = maze.getForceOn(camera.getEye(), cameraForce);
			if(walls != null){
				camera.translate(cameraForce.asVertex().plus(walls.asVertex()));
			}
			else{
				camera.translate(cameraForce.asVertex());
			}
		}
	}
	
	private void applyTurn(Camera camera){
		if(keyDown(KeyEvent.VK_UP)){
			camera.turnUp(turnUD);
		}
		if(keyDown(KeyEvent.VK_DOWN)){
			camera.turnDown(turnUD);
		}
		if(keyDown(KeyEvent.VK_LEFT)){
			camera.turnLeft(turnLR);
		}
		if(keyDown(KeyEvent.VK_RIGHT)){
			camera.turnRight(turnLR);
		}
	}
}
