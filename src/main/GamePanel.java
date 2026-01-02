package main;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import javax.swing.JPanel;

import ai.PathFinder;
import data.SaveLoad;
import entity.Entity;
import entity.EntityGenerator;
import entity.Player;
import environment.EnvironmentManager;
import tile.Map;
import tile.TileManager;
import tile_interactive.InteractiveTile;

@SuppressWarnings("serial")
public class GamePanel extends JPanel implements Runnable{
		
	//SCREEN SETINGS
	final int originalTileSize = 16;  // 16 x 16 Tile
	final int scale = 3; 
	
	public final int tileSize = originalTileSize * scale;  // 48 x48 Tile
	
	public final int maxScreenCol = 20;
	public final int maxScreenRow = 12;
	
//	(Experimental Purpose)
//	public final int maxScreenCol = 22;
//  public final int maxScreenRow = 12;
	
	
	public final int screenWidth = maxScreenCol * tileSize; // 960 pixels
	public final int screenHeight = maxScreenRow * tileSize; //576 pixels
	
	// WORLD SETTINGS
	public int maxWorldCol;
	public int maxWorldRow;
	public final int maxMap = 10;
	public int currentMap = 0;
	
	// FOR FULL SCREEN 
	int screenWidth2 = screenWidth;
	int screenHeight2 = screenHeight;
	 BufferedImage tempScreen;
	 Graphics2D g2;
	 public boolean fullScreenOn = false;
	
	//FPS
	public int FPS = 60;
	int showFPS = 0;
	
	// SYSTEM
	public TileManager tileM = new TileManager(this);
	public KeyHandler keyH = new KeyHandler(this);
	public Sound music = new Sound();
	public Sound se = new Sound();
	public CollisionChecker cChecker = new CollisionChecker(this);
	public AssetSetter aSetter = new AssetSetter(this);
	public EventHandler eHandler = new EventHandler(this);
	public PathFinder pFinder = new PathFinder(this);
	public UI ui = new UI(this);
	Config config = new Config(this);
	EnvironmentManager eManager = new EnvironmentManager(this);
	Map map = new Map(this);
	SaveLoad saveLoad = new SaveLoad(this);
	public EntityGenerator eGenerator = new EntityGenerator(this);
	public CutSceneManager csManager = new CutSceneManager(this);
	Thread gameThread; 
	
	// ENTITY AND OBJECT
	public Player player = new Player(this, keyH);
	public Entity obj[][] = new Entity[maxMap][20];
	public Entity npc[][] = new Entity[maxMap][10];
	public Entity monster[][] = new Entity[maxMap][40];
	public InteractiveTile iTile[][] = new InteractiveTile[maxMap][50];
	public Entity[][] projectileList = new Entity[maxMap][20];
//	public ArrayList<Entity> projectileList = new ArrayList<>();
	public ArrayList<Entity> particleList = new ArrayList<>();
	ArrayList<Entity> entityList = new ArrayList<>();
	
	// GAMESTATE
	public int gameState;
	public final int titleState = 0;
	public final int playState = 1;
	public final int pauseState = 2;
	public final int dialogueState = 3;
	public final int characterState = 4;
	public final int optionsState = 5;
	public final int gameOverState = 6;
	public final int transitionState = 7;
	public final int tradeState = 8; 
	public final int sleepState = 9;
	public final int mapState = 10;
	public final int cutSceneState = 11;

	// others
	public boolean bossBattleOn = false;
	
	// Area
	public int currentArea;
	public int nextArea;
	public final int outSide = 50;
	public final int indoor = 51;
	public final int dungeon = 52;
	
	
	public GamePanel() {
		
		this.setPreferredSize(new Dimension(screenWidth, screenHeight));
		this.setBackground(Color.black);
		this.setDoubleBuffered(true);
		this.addKeyListener(keyH);
		this.setFocusable(true);
	}
	
	
	public void setUpGame() {
		
		aSetter.setObject();
		aSetter.setNPC();
		aSetter.setMonster();
		aSetter.setInteractiveTile();
		eManager.setup();
		
		gameState = titleState;
		currentArea = outSide;
		
		//FullScreen
		tempScreen = new BufferedImage(screenWidth, screenHeight, BufferedImage.TYPE_INT_ARGB);
		g2 = (Graphics2D)tempScreen.getGraphics();
		
		
		if(fullScreenOn == true) {
			setFullScreen();
		}
	}
		
	public void startGameThread() {
		
		gameThread = new Thread(this);
		gameThread.start();
	}

	@Override
	public void run() {
		
		// Gameloop:
		
		
		double drawInterval = 1000000000/FPS;
		double delta = 0;
		long lastTime = System.nanoTime();
		long currentTime;
		long timer = 0;
		long drawCount = 0;
		
		
		while(gameThread != null) {
			
			currentTime = System.nanoTime();
			 
			delta += (currentTime - lastTime) / drawInterval;
			timer += (currentTime - lastTime);
			
			lastTime = currentTime;
			
			if(delta >= 1) {
				update();
				drawToTempScreen(); // draw everything to bufferedImage(tempScreen)
				drawToFullScreen(); // draw everything to fullscreen
				delta--;
				
				drawCount++;
			}
			
			if(timer >= 1000000000) {
				showFPS = (int)drawCount;
				drawCount = 0;
				timer = 0;
			}
		}
		
	}
	
	
	
	
	public void update() {
		
		if(gameState == playState) {
			//Player 
			player.update();
			
			//NPC
			for(int i=0; i<npc[1].length; i++) {
				if(npc[currentMap][i] != null) {
					npc[currentMap][i].update();
				}
			}
			
			//mosnter
			for(int i=0; i<monster[1].length; i++) {
				if(monster[currentMap][i] != null) {
					if(monster[currentMap][i].alive == true && monster[currentMap][i].dying == false) {
						monster[currentMap][i].update();
					}
					else if(monster[currentMap][i].alive == false) {
						monster[currentMap][i].checkDrop();
						monster[currentMap][i] = null;
					}
					
				}
			}
			
			// Projectiles
			for(int i=0; i<projectileList[1].length; i++) {
				if(projectileList[currentMap][i] != null) {
					if(projectileList[currentMap][i].alive == true) {
						projectileList[currentMap][i].update();
					}
					else if(projectileList[currentMap][i].alive == false) {
						projectileList[currentMap][i] = null;
					}
					
				}
			}
			// particle list
			for(int i=0; i<particleList.size(); i++) {
				if(particleList.get(i) != null) {
					if(particleList.get(i).alive == true) {
						particleList.get(i).update();
					}
					else if(particleList.get(i).alive == false) {
						particleList.remove(i);
					}
					
				}
			}
			
			// Interactive Tilse
			for(int i=0; i<iTile[1].length; i++) {
				if(iTile[currentMap][i] != null) {
					iTile[currentMap][i].update();
				}
			}
			
			// Lightning:
			eManager.update();
			
		}
		if(gameState == pauseState) {
			// Nothing for now
		}

	}
	
	/* we are first drawing all images to a tempScreen(4:3 ratio) using drawToTempScreen method
	and then stretching that to FULLSCREEN(using drawToFullScreen() method) */
	
	public void drawToTempScreen() {
		
		// DEBUG
			long drawStart = 0;
			if(keyH.checkDrawTime == true) {
				drawStart = System.nanoTime();
			}
				
				
			if(gameState == titleState) {
				ui.draw(g2);	
			}
			else if(gameState == mapState) {
				map.drawFullMapScreen(g2);
			}
			else {
				// To draw Tile
				tileM.draw(g2);
				
				//Draw Interactive Tile
				for(int i=0; i<iTile[1].length; i++) {
					if(iTile[currentMap][i] != null) {
						iTile[currentMap][i].draw(g2);
					}
				}
						
				entityList.add(player);
					
			
				//   ADD ENTITIIES TO THE LIST
				for(int i=0; i<npc[1].length; i++) {
					if(npc[currentMap][i] != null) {
						entityList.add(npc[currentMap][i]);
					}
				}
					
				for(int i=0; i<obj[1].length; i++) {
					if(obj[currentMap][i] != null) {
						entityList.add(obj[currentMap][i]);
					}
				}
					
				for(int i=0; i<monster[1].length; i++) {
					if(monster[currentMap][i] != null) {
						entityList.add(monster[currentMap][i]);
					}
				}
					
				for(int i=0; i<projectileList[currentMap].length; i++) {
					if(projectileList[currentMap][i] != null) {
						entityList.add(projectileList[currentMap][i]);
					}
				}
				for(int i=0; i<particleList.size(); i++) {
					if(particleList.get(i) != null) {
						entityList.add(particleList.get(i));
					}
				}
			
				
				//sort entityList
				Collections.sort(entityList, new Comparator<Entity>() {
					@Override
					public int compare(Entity e1, Entity e2) {
						int result = Integer.compare(e1.worldY, e2.worldY);
						return result;
					}
				});
					
				//draw ENTITIES
					
				for(int i=0; i<entityList.size(); i++) {
					entityList.get(i).draw(g2);
				}	

				// Empty ENTITIES LIST otherwise entityList will get larger and larger in every loop
				entityList.clear();
				
				// draw Darkness (just like god drew in my life)
				eManager.draw(g2);

				// cutSceneManager
				csManager.draw(g2);
				
				// draw MiniMap
				map.drawMiniMap(g2);
	
				//UI
				ui.draw(g2);
			}
				
				
			// DEBUG
			if(keyH.checkDrawTime == true) {
				long drawEnd = System.nanoTime();
				long passed = drawEnd - drawStart;
				g2.setColor(Color.white);
				g2.setFont(getFont().deriveFont(20F));
				g2.drawString("Draw Time: " + passed, 10 , 400);
				g2.drawString("WorldX: " + player.worldX, 10, 425);
				g2.drawString("WorldY: " + player.worldY, 10, 450);
				g2.drawString("Col: " + ((player.worldX + player.solidArea.x)/tileSize), 10, 475);
				g2.drawString("Row: " + ((player.worldY + player.solidArea.y)/tileSize) , 10, 500);
				g2.drawString("GodModeOn: " + keyH.godModeOn, 10, 525);
				g2.drawString("FPS: " + showFPS, 870, 30);
				
			
				// DEBUG
				String situation = "";
					
				switch(eManager.lightning.dayState) {
				case 0: situation = "Day"; break;
				case 1: situation = "Dusk"; break;
				case 2: situation = "Dawn"; break;
				case 3: situation = "Night"; break;
				}
					
				g2.setColor(Color.white);
				g2.setFont(g2.getFont().deriveFont(20f));
				g2.drawString("Time: " + situation, 850, 550);
					
			}
				
	}
	
	public void drawToFullScreen() {

		Graphics g = getGraphics();
		g.drawImage(tempScreen, 0, 0, screenWidth2, screenHeight2, null);
		g.dispose();
	}
	
	public void setFullScreen() {
		
		// GET LOCAL SCREEN INFROMATION
		GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		GraphicsDevice gd = ge.getDefaultScreenDevice();
		gd.setFullScreenWindow(Main.window);
		
		// get FULL SCREEN WIDTH AND HEIGHT
		screenWidth2 = Main.window.getWidth();
		screenHeight2 = Main.window.getHeight();
	}
	
	public void restartGame(boolean restart) {
		
		stopMusic();
		currentArea = outSide;
		removeTempEntity();
		bossBattleOn = false;
		player.setDefaultPositions();
		player.restoreStatus();
		player.resetCounter();
		aSetter.setNPC();
		aSetter.setMonster();
		
		if(restart == true) {
			player.setDefaultValue();
			aSetter.setObject();
			aSetter.setInteractiveTile();
			eManager.lightning.resetDay();
		}
	}
		
	public void playMusic(int i) {
		music.setFile(i);
		music.play();
		music.loop();	
	}
		
	public void stopMusic() {
		music.stop();
	}
	
	public void playSE(int i) {
		se.setFile(i);
		se.play();
		
	}
	
	public void changeArea() {

		// change Music
		if(nextArea != currentArea) {
			stopMusic();
			if(nextArea == outSide) {
				playMusic(0);
			}
			else if(nextArea == indoor) {
				playMusic(18);
			}
			else if(nextArea == dungeon) {
				playMusic(19);
			}
			
			// to reset the positions of BigRock when player exits dungeon
			aSetter.setNPC();
		}
		
		currentArea = nextArea;
		
		// respawning monster
		aSetter.setMonster();
	}
	
	public void removeTempEntity(){
		for(int mapNum = 0; mapNum < maxMap; mapNum++){
			for(int i=0; i<obj[0].length; i++){
				
				if(obj[mapNum][i] != null && obj[mapNum][i].temp == true){
					obj[mapNum][i] = null;
				}
			}
		}
	}
}
