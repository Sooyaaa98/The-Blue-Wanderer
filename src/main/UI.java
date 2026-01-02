package main;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import entity.Entity;
import object.Obj_Coin_Bronze;
import object.Obj_Heart;
import object.Obj_Mana;



public class UI {
	
	GamePanel gp;
	Graphics2D g2;
	public Font maruMonica;
	Font purisaB;
	public boolean messageOn = false;
	ArrayList<String> message = new ArrayList<>();
	ArrayList<Integer> messageCounter = new ArrayList<>();
	public boolean gameFinished = false;
	public String currentDialogue = "";
	BufferedImage backgroundTitleImage;
	public int commandNum = 0;
//	public int titleScreenState = 0; // 0 -> the first title screen, 1 -> the second Title Screen, 2 -> 
	public BufferedImage heart_full, heart_half, heart_blank, crystal_full, crystal_blank, coin;
	public int playerSlotCol = 0;
	public int playerSlotRow = 0;
	public int npcSlotCol = 0;
	public int npcSlotRow = 0;
	public int subState = 0;
	int counter = 0;
	public Entity npc;
	int charIndex = 0;
	String combinedText = "";
	
	public UI(GamePanel gp) {
		
		this.gp = gp;
		
		try {
			
			InputStream is = getClass().getResourceAsStream("/font/x12y16pxMaruMonica.ttf");
			maruMonica = Font.createFont(Font.TRUETYPE_FONT, is);
			is = getClass().getResourceAsStream("/font/Purisa Bold.ttf");
			purisaB = Font.createFont(Font.TRUETYPE_FONT, is);
			
		} catch (FontFormatException | IOException e) {
			e.printStackTrace();
		}
		
		
		Entity heart = new Obj_Heart(gp);
		heart_full = heart.image;
		heart_half = heart.image2;
		heart_blank = heart.image3;
		
		Entity crystal = new Obj_Mana(gp);
		crystal_full = crystal.image;
		crystal_blank = crystal.image2;
		
		Entity bronzeCoin = new Obj_Coin_Bronze(gp);
		coin = bronzeCoin.down1;
		
	}
	
	public void addMessage(String text) {
		
		message.add(text);
		messageCounter.add(0);
	}
	
	public void draw(Graphics2D g2) {
		
		this.g2 = g2;
		
		g2.setFont(maruMonica);
//		g2.setFont(purisaB);
		g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		
		
		g2.setColor(Color.white);
		
		if(gp.gameState == gp.titleState) {
			drawTitleState(g2);
		}
		else {
			//PlayState
			if(gp.gameState == gp.playState) {	
				drawPlayerLife(g2);
				drawMonsterLife(g2);
				//UI while playState
				drawMessage();
			}	
			// PauseState
			if(gp.gameState == gp.pauseState) {	
				drawPlayerLife(g2);
				drawPauseState(g2);
			}
			//DialoagueState
			if(gp.gameState == gp.dialogueState) {	
				drawPlayerLife(g2);
				drawDialogueState();
			}
			// Character State
			if(gp.gameState == gp.characterState) {
				drawCharacterState();
				drawInventory(gp.player, true);
			}
			// OptionsS State 
			if(gp.gameState == gp.optionsState) {
				drawOptionsState();
			}
			// Game Over State
			if(gp.gameState == gp.gameOverState) {
				drawGameOverState();
			}
			// Transition State
			if(gp.gameState == gp.transitionState) {
				drawTransition();
			}
			// Transition State
			if(gp.gameState == gp.tradeState) {
				drawTradeScreen();
			}
			// Sleep State
			if(gp.gameState == gp.sleepState) {
				drawSleepScreen();
			}
		}
	}
	
	public void drawOptionsState() {
		
		g2.setColor(Color.white);
		g2.setFont(g2.getFont().deriveFont(32F));
		
		// SUB WINDOW
		int frameX = gp.tileSize*6;
		int frameY = gp.tileSize;
		int frameWidth = gp.tileSize*8;
		int frameHeight = gp.tileSize*10;
		drawSubWindow(frameX, frameY, frameWidth, frameHeight);
		
		switch(subState) {
		case 0 : options_top(frameX, frameY); break;
		case 1 : options_FullScreenNotification(frameX, frameY); break;
		case 2 : options_Control(frameX, frameY); break;
		case 3 : options_endGameConfirmation( frameX,  frameY); break;
		}
		
		gp.keyH.enterPressed = false;
	}
		
	public void drawPauseState(Graphics2D g2) {
		
		g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 80F));
		String text = "PAUSE";
		int x  = getXforCentredText(text);
		int y = gp.screenHeight/2;
		
		
		g2.drawString(text, x, y);
	}
	
	public void drawDialogueState() {
		
		//Window
		int x = gp.tileSize*3;
		int y = gp.tileSize/2;
		int width = gp.screenWidth - (gp.tileSize*6);
		int height = gp.tileSize*4;
		
	
		drawSubWindow(x,y,width,height);
		
		g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 33F));
		x += gp.tileSize;
		y += gp.tileSize;
		
		
		if(npc.dialogues[npc.dialogueSet][npc.dialogueIndex] != null) {
			
//			If u want to display all the dialogue at a time, enable(uncomment) the below line AND comment out the next if statement
//			currentDialogue = npc.dialogues[npc.dialogueSet][npc.dialogueIndex];
			
//			The next if block animate the dialogue text and makes it to appear bit by bit
			char characters[] = npc.dialogues[npc.dialogueSet][npc.dialogueIndex].toCharArray();

			if(charIndex < characters.length) {
				
				gp.playSE(17);
				String s = String.valueOf(characters[charIndex]);
				combinedText = combinedText + s;
				currentDialogue = combinedText;
				
				charIndex++;
			}
			
			if(gp.keyH.enterPressed == true) {
				
				charIndex = 0;
				combinedText = "";
				
				if(gp.gameState == gp.dialogueState || gp.gameState == gp.cutSceneState) {
					npc.dialogueIndex++;
					gp.keyH.enterPressed = false;
				}
			}
			
		}
		else { // IF no text is in the array
				npc.dialogueIndex = 0;
				if(gp.gameState == gp.dialogueState) 
					gp.gameState = gp.playState;

				else if(gp.gameState == gp.cutSceneState)
					gp.csManager.scenePhase++;
		}
		
		
		for(String line : currentDialogue.split("\n")) {
			g2.drawString(line, x, y);
			y += 40;
		}
		
	}
	
	public void drawTitleState(Graphics2D g2) {
		
		//draw Tile in background
		UtilityTool utility = new UtilityTool();
		
		try {
			
			backgroundTitleImage = ImageIO.read(getClass().getResourceAsStream("/tiles/002.png"));
			backgroundTitleImage = utility.scaledImage(backgroundTitleImage, gp.tileSize, gp.tileSize);
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
		int worldCol = 0;
		int worldRow = 0;
		while(worldCol <= gp.maxScreenCol && worldRow <=gp.maxScreenRow) {
			int screenX = worldCol * gp.tileSize ;
			int screenY = worldRow * gp.tileSize ;
			
			g2.drawImage(backgroundTitleImage, screenX, screenY, null);
			
			worldCol++;
			
			if(worldCol == gp.maxScreenCol) {
				worldRow++;
				worldCol = 0;
			}
		}
		
			//Title Name
			g2.setFont(g2.getFont().deriveFont(Font.BOLD, 80F));
			String titleName = "Blue Boy Adventure";
			int x = getXforCentredText(titleName);
			int y = gp.tileSize*3;
			//Shadow Color
			g2.setColor(Color.blue);
			g2.drawString(titleName, x+4, y+4);
			// Main Color
			g2.setColor(Color.white);
			g2.drawString(titleName, x, y);
			
			//Player Image
			x = gp.screenWidth/2 - (gp.tileSize*2/2);
			y += gp.tileSize;
			g2.drawImage(gp.player.down2, x-20, y, gp.tileSize*2, gp.tileSize*2, null);
			
			
			//Menu 
			g2.setFont(g2.getFont().deriveFont(Font.BOLD, 40F));
			
			String newGame = "New Game";
			x =  getXforCentredText(newGame);
			y += gp.tileSize*3.5;
			g2.drawString(newGame, x, y);
			if(commandNum == 0) {
				g2.drawString(">", x-gp.tileSize, y);
			}
			
			String loadGame = "Load Game";
			x =  getXforCentredText(loadGame);
			y += gp.tileSize;
			g2.drawString(loadGame, x, y);
			if(commandNum == 1) {
				g2.drawString(">", x-gp.tileSize, y);
			}
			
			String quit = "Quit";
			x =  getXforCentredText(quit);
			y += gp.tileSize;
			g2.drawString(quit, x, y);
			if(commandNum == 2) {
				g2.drawString(">", x-gp.tileSize, y);
			}
		
//		else if(titleScreenState == 1) {
//			
//			g2.setColor(Color.white);
//			g2.setFont(g2.getFont().deriveFont( 42F));
//			
//			//CLASS SELECTION STATE
//			String text = "Select Your Class!";
//			int x = getXforCentredText(text);
//			int y = gp.tileSize*3;
//			g2.drawString(text, x , y);
//			
//			
//			text ="Fighter";
//			x = getXforCentredText(text);
//			y +=  gp.tileSize*3;
//			g2.drawString(text, x , y);
//			if(commandNum == 0) {
//				g2.drawString(">", x-gp.tileSize, y);
//			}
//
//			text ="Theif";
//			x = getXforCentredText(text);
//			y +=  gp.tileSize;
//			g2.drawString(text, x , y);
//			if(commandNum == 1) {
//				g2.drawString(">", x-gp.tileSize, y);
//			}		
//
//			text ="Sorcerer";
//			x = getXforCentredText(text);
//			y +=  gp.tileSize;
//			g2.drawString(text, x , y);
//			if(commandNum == 2) {
//				g2.drawString(">", x-gp.tileSize, y);
//			}
//
//			text ="Back";
//			x = getXforCentredText(text);
//			y +=  gp.tileSize*2;
//			g2.drawString(text, x , y);
//			if(commandNum == 3) {
//				g2.drawString(">", x-gp.tileSize, y);
//			}
//		}
	}
	
	public void drawCharacterState() {
		
		// Create a frame for character State
		int frameX = gp.tileSize*2;
		int frameY= gp.tileSize;
		int frameWidth = gp.tileSize*5;
		int frameHeight = gp.tileSize*10;
		
		drawSubWindow(frameX, frameY, frameWidth, frameHeight);
		
		// TEXT
		g2.setColor(Color.white);
		g2.setFont(g2.getFont().deriveFont(32F));
		
		int textX = frameX + 20;
		int textY = frameY + gp.tileSize;
		final int lineHeight = 35;
		
		// Names
		
		g2.drawString("Level", textX, textY);
		textY += lineHeight;
		g2.drawString("Life", textX, textY);
		textY += lineHeight;
		g2.drawString("Mana", textX, textY);
		textY += lineHeight;
		g2.drawString("Strength", textX, textY);
		textY += lineHeight;
		g2.drawString("Dexterity", textX, textY);
		textY += lineHeight;
		g2.drawString("Attack", textX, textY);
		textY += lineHeight;
		g2.drawString("Defense", textX, textY);
		textY += lineHeight;
		g2.drawString("Exp", textX, textY);
		textY += lineHeight;
		g2.drawString("Next Level", textX, textY);
		textY += lineHeight;
		g2.drawString("Coin", textX, textY);
		textY += lineHeight + 10;
		g2.drawString("Weapon", textX, textY);
		textY += lineHeight + 15;
		g2.drawString("Shield", textX, textY);
		textY += lineHeight;
		
		
		// VALUES
		
		int tailX = (frameX + frameWidth) - 30;
		// Reset textY
		textY = frameY + gp.tileSize;
		String value;
		
		value = String.valueOf(gp.player.level);
		textX = getXforAlignToRightText(value, tailX);
		g2.drawString(value, textX, textY);
		textY += lineHeight;
		
		value = String.valueOf(gp.player.life + "/" + gp.player.maxLife);
		textX = getXforAlignToRightText(value, tailX);
		g2.drawString(value, textX, textY);
		textY += lineHeight;
		
		value = String.valueOf(gp.player.mana + "/" + gp.player.maxMana);
		textX = getXforAlignToRightText(value, tailX);
		g2.drawString(value, textX, textY);
		textY += lineHeight;
		
		value = String.valueOf(gp.player.strength);
		textX = getXforAlignToRightText(value, tailX);
		g2.drawString(value, textX, textY);
		textY += lineHeight;
		
		value = String.valueOf(gp.player.dexterity);
		textX = getXforAlignToRightText(value, tailX);
		g2.drawString(value, textX, textY);
		textY += lineHeight;
		
		value = String.valueOf(gp.player.attack);
		textX = getXforAlignToRightText(value, tailX);
		g2.drawString(value, textX, textY);
		textY += lineHeight;
		
		value = String.valueOf(gp.player.defense);
		textX = getXforAlignToRightText(value, tailX);
		g2.drawString(value, textX, textY);
		textY += lineHeight;
		
		value = String.valueOf(gp.player.exp);
		textX = getXforAlignToRightText(value, tailX);
		g2.drawString(value, textX, textY);
		textY += lineHeight;
		
		value = String.valueOf(gp.player.nextLevelExp);
		textX = getXforAlignToRightText(value, tailX);
		g2.drawString(value, textX, textY);
		textY += lineHeight;
		
		value = String.valueOf(gp.player.coin);
		textX = getXforAlignToRightText(value, tailX);
		g2.drawString(value, textX, textY);
		textY += lineHeight;
		
		g2.drawImage(gp.player.currentWeapon.down1, tailX - gp.tileSize, textY - 24, null);
		textY += gp.tileSize;
		
		g2.drawImage(gp.player.currentShield.down1, tailX - gp.tileSize, textY - 24, null);
		
	}
	
	public void drawGameOverState() {
		g2.setColor(new Color(0,0,0, 150));
		g2.fillRect(0, 0, gp.screenWidth, gp.screenHeight);
		
		int x;
		int y;
		String text;
		g2.setFont(g2.getFont().deriveFont(Font.BOLD, 110f));
		
		text = "Game Over";
		
		// Shadow
		g2.setColor(Color.black);
		x = getXforCentredText(text);
		y = gp.tileSize * 4;
		g2.drawString(text, x, y);
		
		// Main Text
		g2.setColor(Color.white);
		g2.drawString(text, x-4, y-4);
		
		// Retry
		g2.setFont(g2.getFont().deriveFont(50f));
		text = "Retry";
		x = getXforCentredText(text);
		y += gp.tileSize * 4;
		g2.drawString(text, x, y);
		if(commandNum == 0) {
			g2.drawString(">", x - 40, y);
		}
		
		
		// Return to Main Title (Quit)
		text = "Quit";
		x = getXforCentredText(text);
		y += 55;
		g2.drawString(text, x, y);
		if(commandNum == 1) {
			g2.drawString(">", x - 40, y);
		}
		
		
	}
	
	public void drawSleepScreen() {
		
		counter++;
		
		if(counter < 120) {
			gp.eManager.lightning.filterAlpha += 0.01f;
			
			if(gp.eManager.lightning.filterAlpha > 1f) {
				gp.eManager.lightning.filterAlpha = 1f;
			}
		}
		if(counter >= 120) {
			gp.eManager.lightning.filterAlpha -= 0.01f;
			
			if(gp.eManager.lightning.filterAlpha < 0f) {
				gp.eManager.lightning.filterAlpha = 0f;
				gp.eManager.lightning.dayCounter = 0;
				gp.eManager.lightning.dayState = gp.eManager.lightning.day;
				gp.gameState = gp.playState;
				gp.player.getImage();
				counter = 0;
			}
		}
	}
	
	public void options_top(int frameX, int frameY) {
		int textX;
		int textY;
		
		// TITLE
		String text = "Options";
		textX = getXforCentredText(text);
		textY =  frameY + gp.tileSize;
		g2.drawString(text, textX, textY);
		
		
		// FULL SCREEN ON/OFF
		textX = frameX + gp.tileSize;
		textY += gp.tileSize*2;
		g2.drawString("Full Screen", textX, textY);
		if(commandNum == 0) {
			g2.drawString(">", textX - 25, textY);
			if(gp.keyH.enterPressed == true) {
				if(gp.fullScreenOn == false) gp.fullScreenOn = true;
				else if(gp.fullScreenOn == true) gp.fullScreenOn = false;
				subState = 1;
			}
		}
		
		// MUSIC
		textY += gp.tileSize;
		g2.drawString("Music", textX, textY);
		if(commandNum == 1) {
			g2.drawString(">", textX - 25, textY);
		}
		
		// SE
		textY += gp.tileSize;
		g2.drawString("SE", textX, textY);
		if(commandNum == 2) {
			g2.drawString(">", textX - 25, textY);
		}
		
		// CONTROL
		textY += gp.tileSize;
		g2.drawString("Control", textX, textY);
		if(commandNum == 3) {
			g2.drawString(">", textX - 25, textY);
			if(gp.keyH.enterPressed == true) {
				subState = 2;
				commandNum = 0;
			}
		}
		
		// END GAME
		textY += gp.tileSize;
		g2.drawString("End Game", textX, textY);
		if(commandNum == 4) {
			g2.drawString(">", textX - 25, textY);
			if(gp.keyH.enterPressed == true) {
				subState = 3;
				commandNum = 0;
			}
		}
		
		// BACK
		textY += gp.tileSize*2;
		g2.drawString("BACK", textX, textY);
		if(commandNum == 5) {
			g2.drawString(">", textX - 25, textY);
			if(gp.keyH.enterPressed == true) {
				gp.gameState = gp.playState;
				commandNum = 0;
			}
		}
		
		
		// FULL SCREEN CHECK BOX
		textX = frameX + (int) (gp.tileSize*4.5);
		textY = frameY + gp.tileSize*2 + 24;
		g2.setStroke(new BasicStroke(3));
		g2.drawRect(textX, textY, 24, 24);
		if(gp.fullScreenOn == true) {
			g2.fillRect(textX, textY, 24, 24);
		}
		
		// MUSIC SLIDER
		textY += gp.tileSize;
		g2.drawRect(textX, textY, 120, 24);   // 120/5 = 24;
		int volumeWidth = 24 * gp.music.volumeScale;
		g2.fillRect(textX, textY, volumeWidth, 24);
		
		// Sound Effects SLIDER
		textY += gp.tileSize;
		g2.drawRect(textX, textY, 120, 24); // 120/5 = 24;
		volumeWidth = 24 * gp.se.volumeScale;
		g2.fillRect(textX, textY, volumeWidth, 24);
		
		
		
		// SAVE ABOVE SETTINGS TO CONFIG FILE (IMP)
		gp.config.saveConfig();
	}
	
	public void options_FullScreenNotification(int frameX, int frameY) {
		
		int textX = frameX + gp.tileSize;
		int textY = frameY + gp.tileSize*3;
		
		currentDialogue = "You need to restart\nthe game to see \nthe changes!";
		
		for(String line : currentDialogue.split("\n")) {
			g2.drawString(line, textX, textY);
			textY += 40;
		}
		
		// BACK
		textY = frameY + gp.tileSize*9;
		g2.drawString("Back", textX, textY);
		if(commandNum == 0) {
			g2.drawString(">", textX - 25, textY);
			if(gp.keyH.enterPressed == true) {
				subState = 0;
			}
		}
	}
	
	public void options_Control(int frameX, int frameY) {
		
		int textX;
		int textY;
		
		// TITLE
		String text = "Control";
		textX = getXforCentredText(text);
		textY = frameY + gp.tileSize;
		g2.drawString(text, textX, textY);
		
		textX = frameX + gp.tileSize;
		textY += gp.tileSize;
		g2.drawString("Move", textX, textY); textY += gp.tileSize;
		g2.drawString("Confirm/Attack", textX, textY); textY += gp.tileSize;
		g2.drawString("Shoot/Cast", textX, textY); textY += gp.tileSize;
		g2.drawString("Character Screen", textX, textY); textY += gp.tileSize;
		g2.drawString("Pause", textX, textY); textY += gp.tileSize;
		g2.drawString("Options", textX, textY); textY += gp.tileSize;
		
		textX = frameX + gp.tileSize*6;
		textY = frameY + gp.tileSize*2;
		g2.drawString("WASD", textX, textY); textY += gp.tileSize;
		g2.drawString("ENTER", textX, textY); textY += gp.tileSize;
		g2.drawString("F", textX, textY); textY += gp.tileSize;
		g2.drawString("C", textX, textY); textY += gp.tileSize;
		g2.drawString("P", textX, textY); textY += gp.tileSize;
		g2.drawString("ESC", textX, textY); textY += gp.tileSize;
		

		// BACK
		textX = frameX + gp.tileSize;
		textY = frameY + gp.tileSize*9;
		g2.drawString("Back", textX, textY);
		if(commandNum == 0) {
		g2.drawString(">", textX - 25, textY);
		if(gp.keyH.enterPressed == true) {
				subState = 0;
				commandNum = 3;
			}
		}
	}
	
	public void options_endGameConfirmation(int frameX , int frameY) {
		
		int textX = frameX + gp.tileSize;
		int textY = frameY + gp.tileSize*3;
		
		currentDialogue = "Quit the Game and\n return to Title Screen?";
		
		for(String line : currentDialogue.split("\n")) {
			g2.drawString(line, textX, textY);
			textY += 40;
		}
		
		// YES
		String text = "Yes";
		textX = getXforCentredText(text);
		textY += gp.tileSize*3;
		g2.drawString(text, textX, textY);
		if(commandNum == 0) {
			g2.drawString(">", textX - 25, textY);
			if(gp.keyH.enterPressed == true) {
				subState = 0;
				gp.gameState = gp.titleState;
				gp.stopMusic();
				gp.restartGame(true);
			}
		}
		
		// No
		 text = "No";
		textX = getXforCentredText(text);
		textY += gp.tileSize;
		g2.drawString(text, textX, textY);
		if(commandNum == 1) {
			g2.drawString(">", textX - 25, textY);
			if(gp.keyH.enterPressed == true) {
				subState = 0;
				commandNum = 4;;
			}
		}
	}
	
	public void drawTransition() {
		
		counter++;
		g2.setColor(new Color(0,0,0,counter*5));
		g2.fillRect(0, 0, gp.screenWidth, gp.screenHeight);
		
		if(counter == 50) {
			counter = 0;
			gp.gameState = gp.playState;
			gp.currentMap = gp.eHandler.tempMap;
			gp.player.worldX = gp.tileSize * gp.eHandler.tempCol;
			gp.player.worldY = gp.tileSize * gp.eHandler.tempRow;
			gp.eHandler.previousEventX = gp.player.worldX;
			gp.eHandler.previousEventY = gp.player.worldY;
			gp.changeArea();
		}
	}
	
	public void drawMonsterLife(Graphics2D g2) {
		
		for(int i=0; i<gp.monster[0].length; i++) {
			
			Entity monster = gp.monster[gp.currentMap][i];
			
			if(monster != null && monster.inCamera() == true) {
				
				// Monster HP bar
				if(monster.hpBarOn == true && monster.boss == false) {
								
					double oneScale = (double) gp.tileSize/monster.maxLife;
					double hpBarValue = oneScale * monster.life;
								
					g2.setColor(new Color(35,35,35));
					g2.fillRect(monster.getScreenX()-1, monster.getScreenY() - 16, gp.tileSize+2, 12);
								
					g2.setColor(new Color(255,0,30));
					g2.fillRect(monster.getScreenX(), monster.getScreenY() - 15, (int)hpBarValue, 10);
								
					monster.hpBarCounter++;
								
					if(monster.hpBarCounter > gp.FPS*10) {
						monster.hpBarCounter = 0;
						monster.hpBarOn = false;
					}
				}
				else if(monster.boss == true) {
					
					double oneScale = (double) gp.tileSize * 8/monster.maxLife;
					double hpBarValue = oneScale * monster.life;
					
					int x = gp.screenWidth/2 - gp.tileSize * 4;
					int y = gp.tileSize * 11;
								
					g2.setColor(new Color(35,35,35));
					g2.fillRect(x, y , gp.tileSize * 8 + 2, 22);
								
					g2.setColor(new Color(255,0,30));
					g2.fillRect(x , y,  (int)hpBarValue, 20);
								
					// Boss Name
					g2.setFont(g2.getFont().deriveFont(Font.BOLD, 24f));
					g2.setColor(Color.white);
					g2.drawString(monster.name, x + 4, y - 10);
				}
			}
		}
					
	}
	
	public void drawPlayerLife(Graphics2D g2) {
		
		int x = gp.tileSize/2;
		int y = gp.tileSize/2;
		int i = 0;
		int iconSize = 32;
		
		
		// Display Max Life
		while(i < gp.player.maxLife/2) {
			
			g2.drawImage(heart_blank, x, y, iconSize, iconSize,  null);
			i++;
			x += iconSize;
			
			if(i % 8 == 0) {
				x = gp.tileSize/2;
				y += iconSize;
			}
		}
		
		// Reset
		x = gp.tileSize/2;
		y = gp.tileSize/2;
		i = 0;
		
		// Display Player's current Life
		while(i < gp.player.life) {
			
			g2.drawImage(heart_half, x, y, iconSize, iconSize,  null);
			i++;
			
			if(i < gp.player.life) {
				g2.drawImage(heart_full, x, y, iconSize, iconSize,  null);
			}
			i++;
			x += iconSize;
			
			if(i % 16 == 0) {
				x = gp.tileSize/2;
				y += iconSize;
			}
		}
		
		// Draw MAX MANA
		x = gp.tileSize/2 - 5;
		y = (int) (gp.tileSize * 1.75);
		i = 0;
		
		while(i < gp.player.maxMana) {
			g2.drawImage(crystal_blank, x, y, iconSize, iconSize, null);
			x += 35;
			i++;
		}
		
		// Draw  MANA
		x = gp.tileSize/2 - 5;
		y = (int) (gp.tileSize * 1.75);
		i = 0;
				
		while(i < gp.player.mana) {
			g2.drawImage(crystal_full, x, y, iconSize, iconSize, null);
			x += 35;
			i++;
		}
	}
	
	public void drawMessage() {
		
		int messageX = gp.tileSize;
		int messageY = gp.tileSize * 4;
		g2.setFont(g2.getFont().deriveFont(Font.BOLD, 32F));
		
		for(int i=0; i<message.size(); i++) {
			
			if(message.get(i) != null) {
				

				g2.setColor(Color.black);
				g2.drawString(message.get(i), messageX+2, messageY+2);
				
				g2.setColor(Color.white);
				g2.drawString(message.get(i), messageX, messageY);
				
				int counter = messageCounter.get(i) + 1;
				messageCounter.set(i, counter);
				messageY += 50;
				
				if(messageCounter.get(i) > 100) {
					message.remove(i);
					messageCounter.remove(i);
				}
			}
		}
		
	}
	
	public void drawInventory(Entity entity, boolean cursor) {
		
		// FRAME
		int frameX = 0;
		int frameY = 0;
		int frameWidth = 0;
		int frameHeight = 0;
		int slotCol = 0;
		int slotRow = 0;
		
		if(entity == gp.player) {
			// FRAME
			 frameX = gp.tileSize*12;
			 frameY = gp.tileSize;
			 frameWidth = gp.tileSize*6;
			 frameHeight = gp.tileSize*5;
			 slotCol = playerSlotCol;
			 slotRow = playerSlotRow;
		}
		else {
			// FRAME
			 frameX = gp.tileSize * 2;
			 frameY = gp.tileSize;
			 frameWidth = gp.tileSize*6;
			 frameHeight = gp.tileSize*5;
			 slotCol = npcSlotCol;
			 slotRow = npcSlotRow;
		}
		
		
		
		drawSubWindow(frameX, frameY, frameWidth, frameHeight);
		
		// SLOT
		final int slotXStart = frameX + 20;
		final int slotYStart = frameY + 20;
		int slotX = slotXStart;
		int slotY = slotYStart;
		int slotSize = gp.tileSize + 3;
		
		// DRAW PLAYER'S ITEMS
		for(int i = 0; i<entity.inventory.size(); i++) {
			
			// EQUIPPED CURSOR
			if(entity.inventory.get(i) == entity.currentWeapon ||
					entity.inventory.get(i) == entity.currentShield ||
					entity.inventory.get(i) == entity.currentLight) {
				
					g2.setColor(new Color(240,190,90));
					g2.fillRoundRect(slotX, slotY, gp.tileSize, gp.tileSize, 10, 10);
			}
						
			g2.drawImage(entity.inventory.get(i).down1, slotX, slotY, null);
			
			// Display AMOUNT OF AN ITEM 
			if(entity == gp.player && entity.inventory.get(i).amount > 1) {
				int amountX;
				int amountY;
				
				g2.setFont(g2.getFont().deriveFont(32F));
				String s = "" + entity.inventory.get(i).amount;
				
				amountX = getXforAlignToRightText(s, slotX + 44);
				amountY = slotY + gp.tileSize;
				
				//SHADOW
				g2.setColor(new Color(60,60,60));
				g2.drawString(s, amountX, amountY);
				// NUMBER
				g2.setColor(Color.white);
				g2.drawString(s, amountX-3, amountY-3);
			}
			
			slotX += slotSize;
			
			if(i == 4 || i == 9 || i == 14) {
				
				slotX = slotXStart;
				slotY += slotSize;
			}
			
		}
		
		
		//CURSOR
		if(cursor == true) {
			
			int cursorX = slotXStart + (slotSize * slotCol);
			int cursorY = slotYStart + (slotSize * slotRow);
			int cursorWidth = gp.tileSize;
			int curSorHeight = gp.tileSize;
			// DRAW CURSOR
			g2.setColor(Color.white);
			g2.setStroke(new BasicStroke(1));
			g2.drawRoundRect(cursorX, cursorY, cursorWidth, curSorHeight, 10, 10);
			
			
			// DESCRIPTION WINDOW
			int dFrameX = frameX;
			int dFrameY = frameY + frameHeight ;
			int dFrameWidth = frameWidth;
			int dFrameHeight = gp.tileSize * 3;
			
			
			//DRAW DESCRIPTION TEXT
			int textX = dFrameX + 20;
			int textY = dFrameY + gp.tileSize;
			g2.setFont(g2.getFont().deriveFont(28F));
			
			int itemIndex = getItemIndexOnSlot(slotCol, slotRow);
			
			if(itemIndex < entity.inventory.size()) {
				
				drawSubWindow(dFrameX, dFrameY, dFrameWidth, dFrameHeight);
				
				for(String line : entity.inventory.get(itemIndex).description.split("\n")) {
					g2.drawString(line, textX, textY);
					textY += 32;
				}
			}
		}
		
	}
	
	public void drawTradeScreen() {
		
		switch(subState) {
		case 0: trade_select(); break;
		case 1: trade_buy(); break;
		case 2: trade_sell(); break;
		}
		gp.keyH.enterPressed = false;
	}
	
	public void trade_select() {
		npc.dialogueSet = 0;
		
		drawDialogueState();
		
		// Window for displaying items
		int x = gp.tileSize * 15;
		int y = gp.tileSize *4;
		int width = gp.tileSize * 3;
		int height = (int) (gp.tileSize * 3.5);
		drawSubWindow(x,y,width, height);
		
		// Draw Texts
		x += gp.tileSize;
		y += gp.tileSize;
		g2.drawString("Buy", x, y);
		if(commandNum == 0) {
			g2.drawString(">", x - 24, y);
			if(gp.keyH.enterPressed == true) {
				subState = 1;
			}
		}
		
		y += gp.tileSize;
		g2.drawString("Sell", x, y);
		if(commandNum == 1) {
			g2.drawString(">", x - 24, y);
			if(gp.keyH.enterPressed == true) {
				subState = 2;
			}
		}
		
		y += gp.tileSize;
		g2.drawString("Leave", x, y);
		if(commandNum == 2) {
			g2.drawString(">", x - 24, y);
			if(gp.keyH.enterPressed == true) {
				npc.startDialogue(npc, 1);
			}
		}
	
	}
	
	public void trade_buy() {
		
		// Draw Player's Inventory
		drawInventory(gp.player, false);
		
		// Draw Npc's Inventory
		drawInventory(npc, true);
		
		// Draw Hint Window
		int x = gp.tileSize * 2;
		int y = gp.tileSize * 9;
		int width = gp.tileSize * 6;
		int height = gp.tileSize * 2;
		drawSubWindow(x, y, width, height);
		g2.drawString("[ESC] Back", x + 24, y + 60);
		
		// Draw Player's  Coin
		x = gp.tileSize * 12;
		y = gp.tileSize * 9;
		width = gp.tileSize * 6;
		height = gp.tileSize * 2;
		drawSubWindow(x, y, width, height);
		g2.drawString("Your coins: " + gp.player.coin, x + 24, y + 60);
		
		// Draw Price Window
		int itemIndex = getItemIndexOnSlot(npcSlotCol, npcSlotRow);
		
		if(itemIndex < npc.inventory.size()) {
			 x =  (int) (gp.tileSize * 5.5);
			 y = (int) (gp.tileSize * 5.5);
			 width = (int) (gp.tileSize * 2.5);
			 height = (gp.tileSize );
			 drawSubWindow(x,y,width,height);
			 g2.drawImage(coin, x + 10, y + 8, 32, 32, null );
			 
			 int price = npc.inventory.get(itemIndex).price;
			 String text = "" + price;
			 x = getXforAlignToRightText(text, gp.tileSize * 8 - 20);
			 g2.drawString(text, x , y + 34);
			 
			 // Buy an Item
			 if(gp.keyH.enterPressed == true) {
				 if(npc.inventory.get(itemIndex).price > gp.player.coin ) {
					 subState = 0;
					 npc.startDialogue(npc, 2);
				 }
				 else {
					 if(gp.player.canObtainItem(npc.inventory.get(itemIndex)) == true) {
						 gp.player.coin -= npc.inventory.get(itemIndex).price;
					 }
					 else {
						 subState = 0;
						 gp.gameState = gp.dialogueState;
						 npc.startDialogue(npc, 3);
//						 drawDialogueState();
					 }
				 }
//				
			 }
			
		}	
	}
	
	public void trade_sell() {
		
		drawInventory(gp.player, true);
		
		int x;
		int y;
		int width;
		int height;
		
		// Draw Hint Window
		x = gp.tileSize * 2;
		y = gp.tileSize * 9;
		width = gp.tileSize * 6;
		height = gp.tileSize * 2;
		drawSubWindow(x, y, width, height);
		g2.drawString("[ESC] Back", x + 24, y + 60);
				
		// Draw Player's  Coin
		x = gp.tileSize * 12;
		y = gp.tileSize * 9;
		width = gp.tileSize * 6;
		height = gp.tileSize * 2;
		drawSubWindow(x, y, width, height);
		g2.drawString("Your coins: " + gp.player.coin, x + 24, y + 60);
				
		// Draw Price Window
		int itemIndex = getItemIndexOnSlot(playerSlotCol, playerSlotRow);
				
		if(itemIndex < gp.player.inventory.size()) {
			x =  (int) (gp.tileSize * 15.5);
			y = (int) (gp.tileSize * 5.5);
			width = (int) (gp.tileSize * 2.5);
			height = (gp.tileSize );
			drawSubWindow(x,y,width,height);
			g2.drawImage(coin, x + 10, y + 8, 32, 32, null );
					 
			int price = gp.player.inventory.get(itemIndex).price/2;
			String text = "" + price;
			x = getXforAlignToRightText(text, gp.tileSize * 18 - 20);
			g2.drawString(text, x , y + 34);
					 
			// Sell an Item
			if(gp.keyH.enterPressed == true) {
				if(gp.player.inventory.get(itemIndex) == gp.player.currentWeapon ||
					gp.player.inventory.get(itemIndex) == gp.player.currentShield) {
						commandNum = 0;
						subState = 0;
						npc.startDialogue(npc, 4);
				}
				else {
					if(gp.player.inventory.get(itemIndex).amount > 1) {
						gp.player.inventory.get(itemIndex).amount--;
					}
					else {
						gp.player.inventory.remove(itemIndex);
					}
					gp.player.coin += price;
				}
			}
					
		}	
		
	}
	
	public int getItemIndexOnSlot(int slotCol, int slotRow) {
		int itemIndex = slotCol + (slotRow * 5);
		return itemIndex;
	}
	
	public void drawSubWindow(int x, int y, int width, int height) {
		
		Color c = new Color(0,0,0, 200);  // black Color
		
		g2.setColor(c);
		g2.fillRoundRect(x, y, width, height, 35, 35);
		
		c = new Color(255,255, 255);
		g2.setColor(c);
		g2.setStroke(new BasicStroke(5));
		g2.drawRoundRect(x+5, y+5, width-10, height-10, 35, 35);
		
		
	}
	
	public int getXforAlignToRightText(String text, int tailX) {
		int length = (int) g2.getFontMetrics().getStringBounds(text, g2).getWidth();
		int x = tailX - length;
		return x;
	}
	
	public int getXforCentredText(String text) {
		int length = (int) g2.getFontMetrics().getStringBounds(text, g2).getWidth();
		return gp.screenWidth/2 - length/2;
	}
 	
}






