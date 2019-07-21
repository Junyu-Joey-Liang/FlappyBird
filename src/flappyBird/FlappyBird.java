package flappyBird;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.JFrame;
import javax.swing.Timer;

public class FlappyBird implements ActionListener, KeyListener
{
	
	public static FlappyBird fb; // static instance of fb
	public final int WIDTH = 1200, HEIGHT = 800;
	public final int BIRDSIZE = 20;
	public final int COL_SPACE = 300;
	public final int COL_W = 100, COL_W_THICK = 200;
	public final int COL_MIN_H = 50;
	public final int COL_SPEED = 10;
	
	public int ticks,yMotion,score,pastscore,thicken_timer, nextthick,moving_timer;
	public boolean gameOver,started,thicken,moving;
	
	
	public Random rand=new Random();
	
	public Renderer renderer;
	public Rectangle bird;
	public ArrayList<Rectangle> columns;
	
	
	public FlappyBird() 
	{
		JFrame jframe = new JFrame();
		renderer = new Renderer();
		Timer timer = new Timer(20,this); 
		
		jframe.add(renderer);
		jframe.setTitle("Flappy Bird");
		jframe.setSize(WIDTH,HEIGHT);
		jframe.setVisible(true);
		jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		jframe.setResizable(false);
		jframe.addKeyListener(this);
		
		bird = new Rectangle(WIDTH/2-10,HEIGHT/2-10,BIRDSIZE,BIRDSIZE);
		columns = new ArrayList<Rectangle>();
		thicken = false;
		thicken_timer = 0;
		
		addColumn(true);
		addColumn(true);
		addColumn(true);
		addColumn(true);
		
		
		timer.start();
	}
	
	public void addColumn(boolean start) {
		int height = COL_MIN_H + rand.nextInt(300);
		if (thicken==false) {
			if (start) {
				columns.add(new Rectangle(WIDTH+COL_W+columns.size()*COL_SPACE,HEIGHT-height-120,COL_W,height));
				columns.add(new Rectangle(WIDTH+COL_W+(columns.size()-1)*COL_SPACE,0,COL_W,HEIGHT-height-COL_SPACE ));
			}
			else {
				columns.add(new Rectangle(columns.get(columns.size()-1).x+600,HEIGHT-height-120,COL_W,height));
				columns.add(new Rectangle(columns.get(columns.size()-1).x,0,COL_W,HEIGHT-height-COL_SPACE ));
			}
		}
		else {
			if (start) {
				columns.add(new Rectangle(WIDTH+COL_W+columns.size()*COL_SPACE,HEIGHT-height-120,COL_W_THICK,height));
				columns.add(new Rectangle(WIDTH+COL_W+(columns.size()-1)*COL_SPACE,0,COL_W_THICK,HEIGHT-height-COL_SPACE ));
			}
			else {
				columns.add(new Rectangle(columns.get(columns.size()-1).x+600,HEIGHT-height-120,COL_W_THICK,height));
				columns.add(new Rectangle(columns.get(columns.size()-1).x,0,COL_W_THICK,HEIGHT-height-COL_SPACE ));
			}
			thicken = false;
		}
		
	}
	
	public void paintColumn(Graphics g, Rectangle col) {
		
		if (moving) {
			g.setColor(Color.orange);
		}
		else {
			g.setColor(Color.green.darker());
		}
		g.fillRect(col.x, col.y, col.width, col.height);
		
	}
	
	public void jump() {
		
		if (gameOver)
		{
			bird = new Rectangle(WIDTH / 2 - 10, HEIGHT / 2 - 10, 20, 20);
			columns.clear();
			yMotion = 0;
			score = 0;
			thicken = false;
			moving = false;
			thicken_timer = 0;
			moving_timer = 0;
			nextthick = rand.nextInt(150);

			addColumn(true);
			addColumn(true);
			addColumn(true);
			addColumn(true);

			gameOver = false;
		}

		if (!started)
		{
			started = true;
			thicken = false;
			thicken_timer = 0;
			moving_timer = 0;
			moving = false;
		}
		else if (!gameOver)
		{
			if (yMotion > 0)
			{
				yMotion = 0;
			}

			yMotion -= 10;
		}
		
		
	}
	
	public void repaint(Graphics g) 
	{	
		if (moving) g.setColor(Color.black);
		else g.setColor(Color.cyan);
		g.fillRect(0, 0, WIDTH, HEIGHT);
		
		g.setColor(Color.orange);
		g.fillRect(0, HEIGHT-120, WIDTH, 120);
		
		g.setColor(Color.green);
		g.fillRect(0, HEIGHT-120, WIDTH, 20);
		
		if (moving) g.setColor(Color.red.brighter().brighter());
		else g.setColor(Color.red);
 		g.fillRect(bird.x, bird.y, bird.width, bird.height);
 		
 		for (Rectangle col : columns) {
 			paintColumn(g,col);
 		}
 		
 		g.setColor(Color.WHITE);
 		g.setFont(new Font("Arial",1,100));
 		
 		if (!started) {
 			g.drawString("CLICK TO START",  120, HEIGHT / 2 - 50);
 		}
 		
 		if (gameOver) {
 			g.drawString("GAME OVER",  100, HEIGHT / 2 - 50);
 			g.drawString("Score: "+ Integer.toString(pastscore),100, HEIGHT/2 +70);
 			
 		}
 		
 		if(!gameOver && started) {
			g.drawString(String.valueOf(score), WIDTH / 2 - 25, 100);
		}
 		
 		
	}
	
	@Override
	public void actionPerformed(ActionEvent e) 
	{	
		ticks++;
		thicken_timer++;
		moving_timer++;
		
		if (thicken_timer >= nextthick) {
			thicken = true;
			nextthick = rand.nextInt(150);
			thicken_timer=0;
		}
		
		if (moving_timer>750) {
			moving = false;
			moving_timer = 0;
		}
		else if(moving_timer>550) {
			moving = true;
		}
	
		if (started) {
		
			
			if (!moving) {
				for (int i=0;i<columns.size();i++) {
					Rectangle col = columns.get(i);
					col.x-=COL_SPEED;
				}
			}
			else {
				for (int i=0;i<columns.size();i++) {
					Rectangle col = columns.get(i);
					col.x-=(COL_SPEED+3);
					
				}
			}
			
			for (int i = 0; i < columns.size(); i++){
				Rectangle column = columns.get(i);
				if (column.x + column.width < 0){
					columns.remove(column);
					if (column.y == 0) addColumn(false);
				}
			}
	
			
			if (ticks%2==0 && yMotion<15) {yMotion+=2;} //gravity
			bird.y+=yMotion;
			
			for (Rectangle col: columns) {
				
				//add score
				if (col.y == 0 && bird.x + bird.width / 2 > col.x + col.width / 2 - 10 && bird.x + bird.width / 2 < col.x + col.width / 2 + 10) score++;
				
				if (col.intersects(bird)) {
					bird.x = col.x-bird.width;
					pastscore = score;
					gameOver = true;
				}
			}
			
			if (bird.y+yMotion>HEIGHT-120 || bird.y<0) {
				bird.y = HEIGHT - 120 - bird.height;
				pastscore = score;
				gameOver = true;
			}
			
			if (gameOver) {
				bird.y = HEIGHT - 120 - bird.height;
			}
			
		}
		
		renderer.repaint(); 	
	}
	

	
	public static void main (String[] args) 
	{	
		fb = new FlappyBird();	
	}

	@Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub
		int key = e.getKeyCode();
		if (key == KeyEvent.VK_SPACE) {
	        jump();
	    }
		
	}

	@Override
	public void keyReleased(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
		
	}

	@Override
	public void keyTyped(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	


}
