package main;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.Toolkit;
import java.io.File;
import java.io.IOException;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

import tile.TileManager;



public class GamePanel extends JPanel implements Runnable {
	
	// Screen Settings
	final int originalTileSize = 16; //16x16
	final int scale = 3;
	
	public final int tileSize = originalTileSize * scale; //48x48
	final int maxScreenCol = 22;
	final int maxScreenRow = 28; //16x14
	final int screenWidth = tileSize * maxScreenRow; // (48x14)px
	final int screenHeight = tileSize * maxScreenCol; // (48x16)px
	
	private Image backgroundImage;
	BufferedImage myBackgroundImg;
	
	int FPS = 60;
	
	TileManager tileM = new TileManager(this);
	KeyHandler keyH = new KeyHandler();
	Thread gameThread;
	Random randNum = new Random();
	
	Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	public double maxWidth = screenSize.getWidth();
	public double maxHeight = screenSize.getHeight();
	
	int rand = randNum.nextInt((int) (maxHeight-500));
	int rand2 = randNum.nextInt((int) (maxHeight-500));
	
	int score = 0;
	public int birdX = 100;
	public int birdY = 100;
	int birdJump = 25;
	
	int originalPlane1X = (int) (maxWidth-25);
	int plane1X = (int) (maxWidth+75);
	int plane1Y = rand;
	int plane1Speed = 10;
	int plane1Height = 60;
	int plane1Width = 90;
	
	double centerWidth = maxWidth/2;
	double centerHeight = maxHeight/3;

	int originalPlane2X = (int) (maxWidth-25);
	int plane2X = (int) (maxWidth+75);
	int plane2Y = rand2;
	int plane2Speed = 18;
	int plane2Height = 90;
	int plane2Width = 135;
	
	Color birdColor;

	int counter = 0;
	
	boolean gameIsOver = false;
	boolean isOnTitleScreen = true;
	boolean isOnRetryScreen = false;
	boolean firstAnim = true;
	boolean dayTime = true;
	
	
	public GamePanel(){
		//public GamePanel(String fileName) throws IOException
		//this.backgroundImage = ImageIO.read(new File(fileName));
		this.setPreferredSize(new Dimension(screenWidth, screenHeight));
		this.setBackground(Color.black);
		this.setDoubleBuffered(true);
		this.addKeyListener(keyH);
		this.setFocusable(true);
	}
	
	public void paintComponent(Graphics g) {
		if(!isOnTitleScreen && !isOnRetryScreen) {
		    super.paintComponent(g);

			
		    Graphics2D g2 = (Graphics2D)g;
			g2.drawImage(myBackgroundImg, 0, 0, (int)(maxWidth), (int)(maxHeight), this);
			
			tileM.draw(g2);
			Font curFont = g2.getFont();
			Font newFont = curFont.deriveFont(curFont.getSize() * 2F);

			g2.setFont(newFont);
			g2.drawString(Integer.toString(score), 40, 40);

		    g2.setColor(birdColor);
		    g2.fillRect(birdX, birdY, tileSize, tileSize);
		    
			g2.setColor(Color.white);

		    g2.fillRect(plane1X, plane1Y, plane1Width, plane1Height);
		    
		    if(score>375) {
		    	g2.fillRect(plane2X, plane2Y, plane2Width, plane2Height);
		    	
		    }
		    // Draw the background image.
			
		    g2.drawImage(backgroundImage, 0, 0, this);
		    
		    
		    g2.dispose();
		}
		else if(isOnTitleScreen) {
			super.paintComponent(g);
			
		    Graphics2D g2 = (Graphics2D)g;
			/*
			Font curFont = g2.getFont();
			Font newFont = curFont.deriveFont(curFont.getSize() * 3F);
			g2.setColor(Color.white);
			g2.setFont(newFont);
			g2.drawString("Press Enter to Start", (int)centerWidth, (int)centerHeight);
			*/
		    g2.setColor(Color.black);
		    g2.drawImage(backgroundImage, 0, 0, this);
		}

		else if(isOnRetryScreen) {
			super.paintComponent(g);
			
		    Graphics2D g2 = (Graphics2D)g;
			/* 
			Font curFont = g2.getFont();
			Font newFont = curFont.deriveFont(curFont.getSize() * 3F);
			g2.setColor(Color.white);
			g2.setFont(newFont);
			g2.drawString("Press ESC to Try again", (int)centerWidth, (int)centerHeight);
			*/
		    g2.setColor(Color.black);
		    g2.drawImage(backgroundImage, 0, 0, this);
		}
	  }

	public void startGameThread() {
		
		gameThread = new Thread(this);
		gameThread.start();
	}
	
	@Override
	public void run() {
		
		double Interval = 1000000000/FPS; // 1billion nanoseconds = 1 second
		double nextDrawTime = System.nanoTime() + Interval; // When to draw
		while(gameThread != null) {
			
			// UPDATE: update character positions etc
			update();
			
			// DRAW/BLIT: blit updated screen with information
			repaint();
			
			
			
			try {
				double timeLeft = nextDrawTime - System.nanoTime();
				timeLeft /= 10000000; //Convert duration of sleep from nano to miliseconds
				
				if(timeLeft < 0) {
					timeLeft = 0;
				} // Check for a bad error
				
				Thread.sleep((long) timeLeft); // Sleep for a duration
				
				nextDrawTime += Interval;
				
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		
	}
	
	public void jump(){
		birdY -= birdJump;
	}

	public void update() {
		if(!isOnTitleScreen && !isOnRetryScreen && counter > 150) {
			if(!gameIsOver){
				if(keyH.upPressed == true) {
					if(birdY >= maxHeight - 200) {
						//end the game
						gameIsOver = true;
						;
						
					}
					
					else if(birdY < 0) {
						//height limit
						gameIsOver = true;
					}
					else{
						jump();

						
						
					}
				}
				//bird gravity
				birdY += 6;
			}

			else if(gameIsOver){
				;
				//dead bird gravity
				birdY += 12;
			}

			if(((birdX > plane1X-plane1Width/2) && (birdX < plane1X + plane1Width/2)) && ((birdY > plane1Y-plane1Height/2) && (birdY < plane1Y+plane1Height/2))){
				gameIsOver = true;
			}

			if(score>375){
				if(((birdX > plane2X-plane2Width/2) && (birdX < plane2X + plane2Width/2)) && ((birdY > plane2Y-plane2Height/2) && (birdY < plane2Y+plane2Height/2))){
					gameIsOver = true;
				}
			}

			if((plane2Y > plane1Y-135) && (plane2Y < plane1Y+135) && (plane2X > plane1X)) {
				   
				rand2 = randNum.nextInt((int) (maxHeight-240));
	    		plane2Y = rand2;
	    		
	    	}  


			// plane constant movement
			plane1X -= plane1Speed;
			plane2X -= plane2Speed;
			
			//
			if(plane1X < 0 || plane1X > maxWidth) {
				plane1X = originalPlane1X;
				rand = randNum.nextInt((int) (maxHeight-180));
				plane1Y = rand;
				
			}
			
			if(plane2X < 0 || plane2X > maxWidth) {
				plane2X = originalPlane2X;
				rand = randNum.nextInt((int) (maxHeight-180));
				plane2Y = rand;
				
			}
			
			if(birdY >= maxHeight) {
				//end the game
				gameIsOver = true;
				isOnRetryScreen = true;
				
			}
			
			

			if(counter % 90 == 0 && gameIsOver == false){
				score += 20;
			}

			if(counter % 30 == 0 && gameIsOver == false && firstAnim){
				// change to 1st 
				birdColor = Color.white;
				firstAnim = false;
				System.out.println("accessed 1");
			}
			else if(counter % 30 == 0 && gameIsOver == false && !firstAnim){
				// change to 2nd
				birdColor = Color.yellow;
				firstAnim = true;
				System.out.println("accessed 2");
			}

			if(counter%2500==0 && counter!=0){
				dayTime = !dayTime;
			}

			else if(gameIsOver == true) {
				;
			}
			System.out.println(score);
		}
		
		else if(isOnTitleScreen) {
			if(keyH.enterPressed == true) {
				isOnTitleScreen = false;
				isOnRetryScreen = false;
				gameIsOver = false;

			}
		}

		else if(isOnRetryScreen) {
			birdX = 100;
			birdY = 100;

			rand = randNum.nextInt((int) (maxHeight-240));
			rand2 = randNum.nextInt((int) (maxHeight-240));
			
			score = 0;
			
			birdX = 100;
			birdY = 100;
			birdJump = 25;
			
			originalPlane1X = (int) (maxWidth-25);
			plane1X = (int) (maxWidth-25);
			plane1Y = rand;
			plane1Speed = 10;
			plane1Height = 60;
			plane1Width = 90;
			
			originalPlane2X = (int) (maxWidth-25);
			plane2X = (int) (maxWidth-25);
			plane2Y = rand2;
			plane2Speed = 18;
			plane2Height = 90;
			plane2Width = 135;
			
			counter = 0;
	
			
			if(keyH.escPressed == true) {
				isOnRetryScreen = false;
				gameIsOver = false;
				
			}
		}
		counter+=1;
		
		
	}

}