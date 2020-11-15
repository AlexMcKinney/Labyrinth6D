package alex.labyrinth.visual.driver;

import com.jogamp.opengl.util.FPSAnimator;

import java.awt.Cursor;
import java.awt.Frame;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;

import javax.media.opengl.GL;
import javax.media.opengl.GL2;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLCapabilities;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.GLProfile;
import javax.media.opengl.awt.GLCanvas;
import javax.media.opengl.glu.GLU;

import alex.buffer.BufferMaster;
import alex.buffer.render.Color;
import alex.geometry.base.Vertex;
import alex.geometry.camera.Camera;
import alex.geometry.shape.Prism;
import alex.labyrinth.blueprints.Cell;
import alex.labyrinth.physical.ComplexMaze;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class World
		implements GLEventListener, KeyListener, MouseListener, MouseMotionListener {
	
	public static final int FRAME_SPLIT = 10, FRAME_WIDTH = 500, FRAME_HEIGHT = 500;
	
	private static double CAMERA_FAR = 400.0;
		
	private GLU glu = new GLU();
    private FPSAnimator animator;
	
	//buffer for loading renderable images
	private BufferMaster bufferMaster = new BufferMaster(10000);
	
	private Cell player;
	private ComplexMaze maze;
	
	//user's point-of-view
	private Camera mainCamera;
	private Camera metaCamera;
	//camera key-movement handler
	private KeyHandler keyHandler;
	
	//#timesteps
	private int step = 0;
	
	
	public World(ComplexMaze maze, Cell player){
		this.maze = maze;
		this.player = player;
		this.setupMaze();
        
        GLProfile glp = GLProfile.getDefault();
        GLCapabilities caps = new GLCapabilities(glp);
        GLCanvas canvas = new GLCanvas(caps);
        
        canvas.addGLEventListener(this);
        canvas.addKeyListener(this);
        canvas.addMouseListener(this);
        canvas.addMouseMotionListener(this);
        
        Toolkit t = Toolkit.getDefaultToolkit();
        Image i = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
        Cursor noCursor = t.createCustomCursor(i, new Point(0, 0), "none");
        
        Frame frame = new Frame("AWT Window Test");
        frame.setSize(FRAME_WIDTH*2+FRAME_SPLIT*2, FRAME_HEIGHT);
        frame.add(canvas);
        frame.setVisible(true);
        frame.setCursor(noCursor);
        
        frame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });
        
        frame.addMouseMotionListener(this);
        frame.addKeyListener(this);
        frame.addMouseListener(this);
        

        //FPSAnimator(GLAutoDrawable drawable, int fps, boolean scheduleAtFixedRate)
        //animator = new FPSAnimator(canvas, 60, false);
        animator = new FPSAnimator(canvas, 50);
        //animator.add(canvas);
        animator.start();
        
        
	}
	
	private void setupMaze(){
		bufferMaster.add(new Prism(new Vertex(0,0,0), 200.0, Color.makeSpectrum(Color.WHITE, 0.5, 0.48, 0.46, 0.44, 0.42, 0.40)));

		Vertex start = maze.getMainMaze().getStartVertex();
    	mainCamera = new Camera(start.clone(), start.plus(10, 10, 10));
    	mainCamera.setFar(CAMERA_FAR);
    	
    	start = maze.getMetaMaze().getStartVertex();
    	metaCamera = new Camera(start.clone(), start.plus(10, 10, 10));
    	metaCamera.setFar(CAMERA_FAR);
    	
		keyHandler = new KeyHandler(maze, mainCamera, metaCamera);
	}
	
	
	private void timestep(){
		step++;
    	if(keyHandler != null){
    		keyHandler.handleKeys();
    	}

    	if(step == 1){
    		maze.printPlayerState(player);
    	}
    	
    	Cell current = this.keyHandler.getCell();
    	if(player.equals(current) == false){
    		player = current;
    		maze.printPlayerState(player);
    		keyHandler.updateMaze(current);
    	}
	}
	
	@Override
    public void init(GLAutoDrawable drawable) {		
    	//setup drawing parameters
        GL2 gl = drawable.getGL().getGL2();
        
        gl.glEnable(GL2.GL_SCISSOR_TEST);
        
        gl.glEnable(GL2.GL_DEPTH_TEST);
        gl.glEnable(GL2.GL_BLEND);
        gl.glBlendFunc(GL.GL_SRC_ALPHA, GL.GL_ONE_MINUS_SRC_ALPHA);

        gl.glDepthFunc(GL2.GL_LEQUAL);
        gl.glShadeModel(GL2.GL_SMOOTH);
        gl.glHint(GL2.GL_PERSPECTIVE_CORRECTION_HINT, GL2.GL_NICEST);
        
        gl.glClearColor(1.0f, 1.0f, 1.0f, 0.5f);
    }

	/******************************************************************************
	* Standard display() function
	******************************************************************************/
    @Override
    public void display(GLAutoDrawable drawable) {
    	this.timestep();
    	displayMainMaze(drawable);
    	displayMetaMaze(drawable);
    }
        
    public void displayMainMaze(GLAutoDrawable drawable) {

    	GL2 gl = drawable.getGL().getGL2();
    	
    	gl.glScissor(0, 0, FRAME_WIDTH, FRAME_HEIGHT);
    
        //clear screen
    	gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);
    	gl.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
    	
    	//set up view modes
    	gl.glViewport(0, 0, FRAME_WIDTH, FRAME_HEIGHT);
    	
        gl.glMatrixMode(GL2.GL_PROJECTION);
        gl.glLoadIdentity();
        
        mainCamera.runPerspective(glu);
        
        gl.glMatrixMode(GL2.GL_MODELVIEW);
        gl.glLoadIdentity();
        mainCamera.runLookAt(glu);

    	bufferMaster.render(gl);
    	this.maze.getMainMaze().getBuffer().render(gl);
    	if(keyHandler.hintEnabled()){
    		this.maze.getMainMaze().getHintBuffer().render(gl);
    	}
    }
    
    public void displayMetaMaze(GLAutoDrawable drawable) {

    	GL2 gl = drawable.getGL().getGL2();
    
        //clear screen
    	
    	gl.glScissor(FRAME_WIDTH+FRAME_SPLIT, 0, FRAME_WIDTH, FRAME_WIDTH);
    	
    	gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);
    	gl.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
    	
    	//set up view modes
    	gl.glViewport(FRAME_WIDTH+FRAME_SPLIT, 0, FRAME_WIDTH, FRAME_HEIGHT);
    	
        gl.glMatrixMode(GL2.GL_PROJECTION);
        gl.glLoadIdentity();
        
        metaCamera.runPerspective(glu);
        
        gl.glMatrixMode(GL2.GL_MODELVIEW);
        gl.glLoadIdentity();
        metaCamera.runLookAt(glu);

    	bufferMaster.render(gl);
    	this.maze.getMetaMaze().getBuffer().render(gl);
    	if(keyHandler.hintEnabled()){
    		this.maze.getMetaMaze().getHintBuffer().render(gl);
    	}
    }
    
	@Override
	public void dispose(GLAutoDrawable arg0) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void reshape(GLAutoDrawable arg0, int arg1, int arg2, int arg3,
			int arg4) {
	}
	
	
	
	private static void println(Object ob){
		System.out.println(ob);
	}

	@Override
	public void mouseDragged(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseMoved(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseClicked(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void keyPressed(KeyEvent arg0) {
		int code = arg0.getKeyCode();
		
		if(code == KeyEvent.VK_ESCAPE){
			println("Exiting...");
			this.animator.stop();
			System.exit(0);
		}

		keyHandler.tapKey(code);
		keyHandler.setKey(code, true);
	}

	@Override
	public void keyReleased(KeyEvent arg0) {
		keyHandler.setKey(arg0.getKeyCode(), false);
	}

	@Override
	public void keyTyped(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}
}
