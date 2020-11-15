package alex.labyrinth.visual.driver;

import java.awt.event.KeyEvent;
import java.util.HashMap;
import java.util.Map;

import alex.geometry.camera.Camera;
import alex.labyrinth.blueprints.Cell;
import alex.labyrinth.physical.ComplexMaze;
import alex.labyrinth.physical.Maze;
import alex.labyrinth.visual.movement.AirplaneMode;
import alex.labyrinth.visual.movement.HelicopterMode;
import alex.labyrinth.visual.movement.JetpackMode;
import alex.labyrinth.visual.movement.MoveMode;
import alex.labyrinth.visual.movement.SpaceshipMode;


public class KeyHandler {
	public enum MoveMaze{MAIN, META}
	
	//keep track of which keys are pressed--might be holding down multiple
	private Map<Integer, Boolean> keyMap = new HashMap<Integer, Boolean>();
	
	private ComplexMaze maze;
	private Camera mainCamera, metaCamera;
	
	private MoveMode moveMode = new HelicopterMode(this);
	private MoveMaze move = MoveMaze.MAIN;
	
	private boolean showHint = true;
	
	
	public KeyHandler(ComplexMaze maze, Camera mainCamera, Camera metaCamera){
		this.mainCamera = mainCamera;
		this.metaCamera = metaCamera;
		this.maze = maze;
	}
	
	public boolean hintEnabled(){
		return this.showHint;
	}
	
	public void setKey(int key, boolean isPressed){
		this.keyMap.put(key, isPressed);
	}
		
	public void setMoveMode(String moveStr){
		if(moveStr.toLowerCase().equals("airplane")){
			this.moveMode = new AirplaneMode(this);
		}
		else if(moveStr.toLowerCase().equals("helicopter")){
			this.moveMode = new HelicopterMode(this);
		}
		else if(moveStr.toLowerCase().equals("jetpack")){
			this.moveMode = new JetpackMode(this);
		}
		else if(moveStr.toLowerCase().equals("spaceship")){
			this.moveMode = new SpaceshipMode(this);
		}
	}
	
	public Cell getCell(){
		Cell main = this.maze.getMainMaze().getCell(this.mainCamera.getEye());
		Cell meta = this.maze.getMetaMaze().getCell(this.metaCamera.getEye());
		return new Cell(main.x, main.y, main.z, meta.u, meta.v, meta.w);
	}
	
	public void updateMaze(Cell player){
		switch(this.move){
		case MAIN:
			this.maze.renewMetaMaze(player);
			break;
		case META:
			this.maze.renewMainMaze(player);
			break;
		}
	}
	
	/**
	 * Turn off all keys
	 * */
	public void clearKeys(){
		this.keyMap.clear();
	}
	
	/**
	 * Return TRUE if a key is in pressed-mode
	 * */
	public boolean keyDown(int keyCode){
		return (keyMap.get(keyCode) != null && keyMap.get(keyCode) != false);
	}
	
	public void tapKey(int key){
		switch(key){
		case KeyEvent.VK_1:{
			this.moveMode = new AirplaneMode(this);
			break;
		}
		case KeyEvent.VK_2:{
			this.moveMode = new HelicopterMode(this);
			break;
		}
		case KeyEvent.VK_3:{
			this.moveMode = new SpaceshipMode(this);
			break;
		}
		case KeyEvent.VK_4:{
			this.moveMode = new JetpackMode(this);
			break;
		}
		case KeyEvent.VK_ENTER:{
			if(this.move == MoveMaze.MAIN){
				this.move = MoveMaze.META;
			}
			else if(this.move == MoveMaze.META){
				this.move = MoveMaze.MAIN;
			}
			break;
		}
		case KeyEvent.VK_C:{
			this.showHint = !this.showHint;
			break;
		}
		}
	}
	
	/**
	 * Act (mostly camera) based on which keys are being held down
	 * */
	public void handleKeys(){
		if(keyDown(KeyEvent.VK_1)){
			this.moveMode = new AirplaneMode(this);
		}
		if(keyDown(KeyEvent.VK_2)){
			this.moveMode = new HelicopterMode(this);
		}
		if(keyDown(KeyEvent.VK_3)){
			this.moveMode = new JetpackMode(this);
		}
		if(keyDown(KeyEvent.VK_4)){
			this.moveMode = new SpaceshipMode(this);
		}

		if(this.move == MoveMaze.MAIN){
			applyForce(this.maze.getMainMaze(), this.mainCamera);
		}
		if(this.move == MoveMaze.META){
			applyForce(this.maze.getMetaMaze(), this.metaCamera);
		}
	}
	
	private void applyForce(Maze maze, Camera camera){
		this.moveMode.applyForce(maze, camera);
	}
	
	protected static void println(Object ob){
		System.out.println(ob);
	}
}
