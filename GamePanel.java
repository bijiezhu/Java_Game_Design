package Main;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.event.*;

import javax.swing.JPanel;

import GameState.GameStateManager;

@SuppressWarnings("serial")
public class GamePanel extends JPanel implements Runnable,KeyListener{
	
	//dimensions
	public static final int WIDTH=320;
	public static final int HEIGHT=240;
	public static final int SCALE=2;
	
	//game thread
	private Thread thread;
	private boolean running;
	private int FPS=60;
	private long targetTime=1000/FPS;
	
	//image
	private BufferedImage image;
	private Graphics2D g;
	
	//game state manager
	
	private GameStateManager gsm;
	
	
	public GamePanel(){
		super();//use Jpanel's constructor first
		setPreferredSize(new Dimension(WIDTH*SCALE,HEIGHT*SCALE));
		setFocusable(true);
		requestFocus();
	}
	
	public void addNotify(){
		/*Creates the Panel's peer. 
		The peer allows you to modify the appearance of the panel
		without changing its functionality.*/
		
		super.addNotify();
		//if no thread, creates a new thread
		if(thread==null){
			thread=new Thread(this);
			addKeyListener(this);
			thread.start();
		}
	}
	
	private void init(){
		image=new BufferedImage(
			WIDTH,HEIGHT,
			BufferedImage.TYPE_INT_RGB
		);
		
		g=(Graphics2D) image.getGraphics();
		running=true;
		
		gsm=new GameStateManager();
	}
	//game loop
		public void run(){
			init();//everythings needs to be initialized
			//three timer
			long start;
			long elapsed;
			long wait;
			
			while(running){
				start=System.nanoTime();
				
				update();
				draw();
				drawToScreen();
				
				elapsed=System.nanoTime()-start;
				wait=targetTime-elapsed/1000000;
				if(wait<0) wait=5;
				
				try{
					Thread.sleep(wait);
				}
				catch(Exception e){
					e.printStackTrace();
				}
				
			}
		}
		
		private void update(){
			gsm.update();
		}
		private void draw(){
			gsm.draw(g);
		}
		private void drawToScreen(){
			Graphics g2=getGraphics();
			g2.drawImage(image,0,0,WIDTH*SCALE,HEIGHT*SCALE,null);//draw cavans image
			g2.dispose();
			
		}
		
		public void keyTyped(KeyEvent key){}
		public void keyPressed(KeyEvent key){
			gsm.keyPressed(key.getKeyCode());
		}
		public void keyReleased(KeyEvent key){
			gsm.keyReleased(key.getKeyCode());
		}
		
	}
	
	

