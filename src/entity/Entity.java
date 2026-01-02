package entity;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import javax.imageio.ImageIO;

import main.GamePanel;
import main.UtilityTool;

public class Entity {
	
	GamePanel gp;
	public BufferedImage up1, up2, down1, down2, left1, left2, right1, right2;
	public BufferedImage attackUp1, attackUp2, attackdown1, attackdown2, attackleft1, attackleft2, attackright1, attackright2,
	guardDown, guardUp, guardLeft, guardRight ;
	public BufferedImage image, image2, image3;
	public Rectangle solidArea = new Rectangle(0,0,48,48);
	public Rectangle attackArea = new Rectangle(0,0,0,0);
	public int solidAreaDefaultX, solidAreaDefaultY;
	public String dialogues[][] = new String[20][20]; 
	public boolean collision = false;
	public Entity attacker;
	public Entity linkedEntity;
	
	
	//STATE
	public int worldX, worldY;
	public String direction = "down";
	public int spriteNum = 1;
	public int dialogueIndex = 0;
	public int dialogueSet = 0;
	public boolean collisionOn = false;
	public boolean invincible = false;
	public boolean attacking = false;
	public boolean alive = true;
	public boolean dying = false;
	public boolean hpBarOn = false;
	public boolean knockBack = false;
	public String knockBackDirection;
	public boolean guarding = false;
	public boolean transparent = false;
	public boolean offBalance = false;
	public Entity loot;
	public boolean opened = false;
	public boolean inRage = false;
	public boolean boss = false;
	public boolean sleep = false;
	public boolean temp = false; // used to place object during boss Fight
	public boolean drawing = true;
	
	
	//COUNTER
	public int spriteCounter = 0;
	public int actionLockCounter = 0;
	public int invincibleCounter = 0;
	public int shotAvailaibleCounter = 0;
	public int knockBackCounter = 0;
	public int standCounter = 0;
	 int dyingCounter = 0;
	 public int hpBarCounter = 0;
	public int guardCounter = 0;
	public int offBalanceCounter = 0;
	
	//CHARACTER ATTRIBUTES
	public int maxLife;
	public int life;
	public int defaultSpeed = 4;
	public String name;
	public int maxMana;
	public int mana;
	public int speed;
	public int level;
	public int ammo;
	public int strength;
	public int dexterity;
	public int attack;
	public int defense; 
	public int exp;
	public int nextLevelExp;
	public int coin;
	public int motion1_duration;
	public int motion2_duration;
	public Entity currentWeapon;
	public Entity currentShield;
	public Entity currentLight;
	public Projectile projectile;
	public boolean onPath;
	public int changedSpeedAfterEquippingBoots; 
	
	// ITEM ATTRIBUTES
	public ArrayList<Entity> inventory = new ArrayList<>();
	public final int maxInventorySize = 20;
	public int value;
	public int attackValue;
	public int defenseValue;
	public String description = "";
	public int useCost;
	public int price;
	public int knockBackPower = 0;
	public boolean stackable = false;
	public int amount = 1;
	public int lightRadius;
	
	// TYPE
	public int type; // 0-> player,  1-> npc,  2-> monster
	public final int type_player = 0;
	public final int type_npc = 1;
	public final int type_monster = 2;
	public final int type_sword = 3;
	public final int type_axe = 4;
	public final int type_shield = 5;
	public final int type_consumables = 6;
	public final int type_pickupOnly = 7;
	public final int type_obstacle = 8;
	public final int type_light = 9;
	public final int type_pickaxe = 10;
	
	
	
	public Entity(GamePanel gp) {
		this.gp = gp;
	}
	
	public void draw(Graphics2D g2) {
		
		BufferedImage image = null;
		
		// We draw only those entites which are inside the frame of the screen 
		// CAMERA(from the POV of player):
		if(inCamera() == true)
		{
			
			int tempScreenX = getScreenX();
			int tempScreenY = getScreenY();
		

			switch(direction){
				case "up":
					if(attacking == false) {
						if(spriteNum == 1){ image = up1; }
						if(spriteNum == 2){ image = up2; }
					}
					 if(attacking == true) {
						 tempScreenY = getScreenY() - up1.getHeight();
						if(spriteNum == 1){ image = attackUp1; }
						if(spriteNum == 2){ image = attackUp2; }
					}
					break;
				case "down":
					if(attacking == false) {
						if(spriteNum == 1){ image = down1; }
						if(spriteNum == 2){ image = down2; }
					}
					 if(attacking == true) {
						if(spriteNum == 1){ image = attackdown1; }
						if(spriteNum == 2){ image = attackdown2; }
					}
					break;
				case "left":
					if(attacking == false) {
						if(spriteNum == 1){ image = left1; }
						if(spriteNum == 2){ image = left2; }
					}
					 if(attacking == true) {
						 tempScreenX = getScreenX() - left1.getWidth();
						if(spriteNum == 1){ image = attackleft1; }
						if(spriteNum == 2){ image = attackleft2; }
					}
					break;
				case "right":
					if(attacking == false) {
						if(spriteNum == 1){ image = right1; }
						if(spriteNum == 2){ image = right2; }
					}
					 if(attacking == true) {
						if(spriteNum == 1){ image = attackright1; }
						if(spriteNum == 2){ image = attackright2; }
					}
					break;
				}
			
			if(invincible == true) {
				hpBarOn = true;
				hpBarCounter = 0;
				changeAlpha(g2, 0.4f);
			}
			
			if(dying == true) {
				dyingAnimation(g2);
			}
			
			g2.drawImage(image, tempScreenX, tempScreenY, null);
			changeAlpha(g2, 1f);
			
		}
	}
	
	public void checkCollision() {
		collisionOn = false;
		gp.cChecker.checkTile(this);
		gp.cChecker.checkObject(this, false);
		gp.cChecker.checkEntity(this, gp.npc);
		gp.cChecker.checkEntity(this, gp.monster);
		gp.cChecker.checkEntity(this, gp.iTile);
		boolean contactPlayer  =  gp.cChecker.checkPlayer(this);
		
		if(this.type == type_monster && contactPlayer == true) {
			damagePlayer(attack);
		}
		
	}
	
	public void update() {
		if(sleep == false){
			if(knockBack == true) {
				checkCollision();
				if(collisionOn == true) {
					knockBackCounter = 0;
					knockBack = false;
					speed = defaultSpeed;
				}
				else if(collisionOn == false) {
					switch(knockBackDirection) {
						case"up":   worldY -= speed; break;
						case"down":	worldY += speed; break;
						case"left":	worldX -= speed; break;
						case"right":worldX += speed; break;
					}
				}
				
				knockBackCounter++;
				if(knockBackCounter == 10) {
					knockBackCounter = 0;
					knockBack = false;
					speed = defaultSpeed;
				}
			}
			else if(attacking == true){
				attacking();
			}
			else {
				setAction();
				checkCollision();
				// IF COLLISION IS FALSE, PLAYER CAN MOVE
				if(collisionOn == false ) {
					switch(direction) {
						case"up":   worldY -= speed; break;
						case"down":	worldY += speed; break;
						case"left":	worldX -= speed; break;
						case"right":worldX += speed; break;
					}
				} 
	
				spriteCounter++;
				if(spriteCounter > 12){
					if(spriteNum == 1){
						spriteNum = 2;
					}
					else if(spriteNum == 2){
						spriteNum = 1;
					}
					spriteCounter = 0;
				}
			}
				
			if(invincible == true) {
				invincibleCounter++;
				if(invincibleCounter > (gp.FPS/2 + 10)) {
					invincible = false;
					invincibleCounter = 0;
				}
			}
			if(shotAvailaibleCounter < 120) {
				shotAvailaibleCounter++;
			}		
			if(offBalance == true) {
				offBalanceCounter++;
				if(offBalanceCounter > 60) {
					offBalance = false;
					offBalanceCounter = 0;
				}
			
			}
		}
		
	}
	
	public void attacking() {
		
		spriteCounter++;
		
		if(spriteCounter <= motion1_duration) {
			spriteNum = 1;
		}
		if(spriteCounter > motion1_duration && spriteCounter <= motion2_duration) {
			spriteNum = 2;
			
			//sace current worldx, worldy, solidArea; 
			int currentX = worldX;
			int currentY = worldY;
			int solidAreaWidth = solidArea.width;
			int solidAreaHeight = solidArea.height;
			
			//Adjust player's worldX and worldY for the attackArea;
			
			switch(direction) {
			case"up":	worldY -= attackArea.height; break;
			case"down": worldY += attackArea.height; break;
			case"left": worldX -= attackArea.width; break;
			case"right": worldX += attackArea.width; break;
			}
			
			//attackArea becomes solidArea
			solidArea.width = attackArea.width;
			solidArea.height = attackArea.height;
			
			if(type == type_monster){
				if(gp.cChecker.checkPlayer(this) == true){
					damagePlayer(attack);
				}
			}
			else{ // Player:

				//Check monster collision with the updated worldX,worldY and solidArea 
				int monsterIndex = gp.cChecker.checkEntity(this, gp.monster);
				gp.player.damageMonster(monsterIndex, this,  attack, currentWeapon.knockBackPower);   // damage the monster if the weapon hits the monster
			
				int iTileIndex = gp.cChecker.checkEntity(this, gp.iTile);
				gp.player.damageInteractiveTile(iTileIndex);
			
				// damage Projectile
				int projectileIndex = gp.cChecker.checkEntity(this, gp.projectileList);
				gp.player.damageProjectile(projectileIndex);
			}

			//After checking the collision restore the data(worldx, worldY and SolidArea)
			worldX = currentX;
			worldY = currentY;
			solidArea.width = solidAreaWidth;
			solidArea.height = solidAreaHeight;
			
		}
		if(spriteCounter > motion2_duration) {
			spriteNum = 1;
			spriteCounter = 0;
			attacking = false;
		}
		
	}

	public boolean inCamera() {
		
		// return whether the object is in the frame of Camera(Player's POV) or not
		
		boolean inCamera = false;
		
		if( worldX + gp.tileSize * 5 > gp.player.worldX - gp.player.screenX &&
				worldX - gp.tileSize < gp.player.worldX + gp.player.screenX &&
				worldY + gp.tileSize * 5 > gp.player.worldY - gp.player.screenY &&
				worldY - gp.tileSize < gp.player.worldY + gp.player.screenY ) {
			
			inCamera = true;
		}
		
		return inCamera;
	}
	
	public void checkAttackOrNot(int rate, int straight, int horizontal){

		boolean targetInRange = false;
		int xDis = getXDistance(gp.player);
		int yDis = getYDistance(gp.player);

		switch(direction){
			case"up": 
				if(gp.player.getCentreY() < getCentreY() && yDis < straight && xDis < horizontal)
					targetInRange = true;
				break;
			case"down": 
				if(gp.player.getCentreY() > getCentreY() && yDis < straight && xDis < horizontal)
					targetInRange = true;
				break;
			case"left": 
				if(gp.player.getCentreX() < getCentreX() && xDis < straight && yDis < horizontal)
					targetInRange = true;
				break;
			case"right":
				if(gp.player.getCentreX() > getCentreX() && xDis < straight && yDis < horizontal)
					targetInRange = true;
				break;
		}

		if(targetInRange == true){
			//check if it initiates an attack
			int i = new Random().nextInt(rate);
			if(i == 0){
				attacking = true;
				spriteNum = 1;
				spriteCounter = 0;
				shotAvailaibleCounter = 0;
			}
		}

	}

	public int getScreenX(){ return worldX - gp.player.worldX + gp.player.screenX; }
	public int getScreenY() {return worldY - gp.player.worldY + gp.player.screenY; }
	public int getLeftX() { return worldX + solidArea.x; }
	public int getRightX() { return worldX + solidArea.x + solidArea.width; }
	public int getTopY() { return worldY + solidArea.y; }
	public int getBottomY() { return worldY + solidArea.y + solidArea.height; }
	public int getCol() { return (worldX + solidArea.x)/ gp.tileSize; }
	public int getRow() { return (worldY + solidArea.y) / gp.tileSize; }
	public int getXDistance(Entity target) { return Math.abs(getCentreX() - target.getCentreX());}
	public int getYDistance(Entity target) { return Math.abs(getCentreY() - target.getCentreY());}
	public int getTileDistance(Entity target) {return ((getXDistance(target) + getYDistance(target))/gp.tileSize);}
	public int getGoalCol(Entity target) {return ((target.worldX + target.solidArea.x)/gp.tileSize);}
	public int getGoalRow(Entity target) {return ((target.worldY + target.solidArea.y)/gp.tileSize);}
	public int getCentreX() {return worldX + left1.getWidth()/2;}
	public int getCentreY() {return worldY + up1.getHeight()/2;}
	public String getOppositeDirection(String direction) {
		
		String oppositeDirection = "";
		
		switch(direction) {
		case"up": oppositeDirection = "down"; break;
		case"down" : oppositeDirection = "up"; break;
		case"left" : oppositeDirection = "right"; break;
		case"right" : oppositeDirection = "left"; break;
		}
		
		return oppositeDirection;
	}
	
	
	public void damagePlayer(int attack) {
		
		if(gp.player.invincible == false) {
			
			int damage = attack - gp.player.defense;
			
			// Get the opposite direction to the player's direction (basically the attacker's direction)
			String canGuardDirection = getOppositeDirection(direction);
			
			if(gp.player.guarding == true && gp.player.direction.equals(canGuardDirection)) {
				
				// PARRY 
				if(gp.player.guardCounter < 10) {  // player have to block the attack just 10 frames before the attack starts in order to successfully parry it
					damage = 0;
					gp.playSE(16);
					setKnockBack(this, gp.player, knockBackPower);
					offBalance = true;
					spriteCounter -= 60;   // V.IMP It enables to sort of stun the monster 
				}
				// Normal GUARD 
				else {
					damage /= 3;
					gp.playSE(15);
				}
			}
			else {
				gp.playSE(6);
				// Give Damage:
				if(damage < 1) {
					damage = 1;
				}
			}
			
			if(damage != 0) {
				gp.player.transparent = true;
				setKnockBack(gp.player, this, knockBackPower);
			}
			
			
			gp.player.life -= damage;
			gp.player.invincible = true;
			
			
		}
	}
	
	public BufferedImage setUp(String imagePath, int width, int height) {
		
		UtilityTool utility = new UtilityTool();
		BufferedImage image = null;
		
		try {
				
			image = ImageIO.read(getClass().getResource(imagePath + ".png"));
			image = utility.scaledImage(image, width, height);
			
			
		}catch(IOException e) {
			e.printStackTrace();
		}
		
		return image;
	}
	
	public void speak() {}
	public void move(String direction) {}
	
	public void startDialogue(Entity entity, int setNum) {
		gp.gameState = gp.dialogueState;
		gp.ui.npc = entity;
		dialogueSet = setNum;
	}
	
	public void facePlayer() {
		switch(gp.player.direction) {
		case"up": direction = "down"; break;
		case"down": direction = "up"; break;
		case"left": direction = "right"; break;
		case"right": direction = "left"; break;
			
		}
	}
	
	public void dropItem(Entity droppedItem) {
		
		for(int i=0; i<gp.obj[1].length; i++) {
			if(gp.obj[gp.currentMap][i] == null) {
				gp.obj[gp.currentMap][i] = droppedItem;
				gp.obj[gp.currentMap][i].worldX = worldX;     //  dead monster's worldX and worldY
				gp.obj[gp.currentMap][i].worldY = worldY;
				break;
			}
		}
	}
	
	public void interact() {}
	
	public void resetCounter() {
		spriteCounter = 0;
		actionLockCounter = 0;
		invincibleCounter = 0;
		shotAvailaibleCounter = 0;
		knockBackCounter = 0;
		standCounter = 0;
		dyingCounter = 0;
		hpBarCounter = 0;
		guardCounter = 0;
		offBalanceCounter = 0;
	}
	
	public void setLoot(Entity loot) {}
	
	public void dyingAnimation(Graphics2D g2) {
		
		dyingCounter++;
		int i = 5;
		
		if(dyingCounter <= i) { changeAlpha(g2, 0f); }
		if(dyingCounter > i && dyingCounter <= i*2) { changeAlpha(g2, 1f); }
		if(dyingCounter > i*2 && dyingCounter <= i*3) { changeAlpha(g2, 0f); }
		if(dyingCounter > i*3 && dyingCounter <= i*4) { changeAlpha(g2, 1f); }
		if(dyingCounter > i*4 && dyingCounter <= i*5) { changeAlpha(g2, 0f); }
		if(dyingCounter > i*5 && dyingCounter <= i*6) { changeAlpha(g2, 1f); }
		if(dyingCounter > i*6 && dyingCounter <= i*7) { changeAlpha(g2, 0f); }
		if(dyingCounter > i*7 && dyingCounter <= i*8) { changeAlpha(g2, 1f); }
		
		if(dyingCounter > i*8) {
			alive = false;
		}
	}
	
	public void changeAlpha(Graphics2D g2, float alphaValue) {
		
		g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alphaValue));
	}
	
	public Color getParticleColor() {
		Color color = null;
		return color;
	}
	
	public int getParticleSpeed() {
		int speed = 0;
		return speed;
	}
	
	public int getParticleSize() {
		int size = 0; // 0 pixels (basically that class which wants to create partice extends this(Entity) class and override these methods
		return size;
	}
	
	public int getParticleMaxLife() {
		int maxLife = 0;
		return maxLife;
	}

	public void generateParticle(Entity generator, Entity target) {
		Color color = generator.getParticleColor();
		int size = generator.getParticleSize();
		int speed = generator.getParticleSpeed();
		int maxLife = generator.getParticleMaxLife();
		
		Particle p1 = new Particle(gp, target, color, size, speed, maxLife, -2, -1);
		Particle p2 = new Particle(gp, target, color, size, speed, maxLife,  2, -1);
		Particle p3 = new Particle(gp, target, color, size, speed, maxLife, -2,  1);
		Particle p4 = new Particle(gp, target, color, size, speed, maxLife,  2,  1);
		gp.particleList.add(p1);
		gp.particleList.add(p2);
		gp.particleList.add(p3);
		gp.particleList.add(p4);
	}
	
	public void setAction() {}
	
	public void damageReaction() {}

	public boolean use(Entity entity) {return false;}
	
	public void checkDrop() {}
	
	public void checkStartChasingOrNot(Entity target, int distance, int rate) {
		if(getTileDistance(target) < distance) {
			int i = new Random().nextInt(rate);
			if(i == 0) {
				onPath = true;
			}
		}
	}
	
	public void checkStopChasingOrNot(Entity target, int distance, int rate) {
		if(getTileDistance(target) > distance) {
			int i = new Random().nextInt(rate);
			if(i == 0) {
				onPath = false;
			}
		}
	}
	
	public void getRandomDirection(int interval) {
		
		actionLockCounter++;
		
		if(actionLockCounter > interval) {
			
			Random random = new Random();
			int i = random.nextInt(100)+1;
			
			if(i <= 25) { direction = "up";}
			else if (i > 25 && i <= 50) { direction = "down";}
			else if(i > 50 && i <= 75) { direction = "left"; }
			else if (i > 75 && i <= 100) { direction = "right";}
			
			actionLockCounter = 0;
		}		
	}
	
	public void moveTowardsPlayer(int interval) {
		
		actionLockCounter++;
		
		if(actionLockCounter > interval) {
			if(getXDistance(gp.player) > getYDistance(gp.player)) {
				if(gp.player.getCentreX() < getCentreX()) {
					direction = "left";
				}
				else {
					direction = "right";
				}
			}
			else if(getXDistance(gp.player) < getYDistance(gp.player)) {
				if(gp.player.getCentreY() < getCentreY()) {
					direction = "up";
				}
				else {
					direction = "down";
				}
			}
			actionLockCounter = 0;
		} 
	}
	
	public void checkShootOrNot(int rate, int shotInterval) {
		
		// shoots projectile randomly
		int i = new Random().nextInt(rate);
					
		if(i == 0 && projectile.alive == false && shotAvailaibleCounter == shotInterval) {
			projectile.set(worldX, worldY, direction, true, this);
						
			for(int j=0; j<gp.projectileList[gp.currentMap].length; j++ ) {
				if(gp.projectileList[gp.currentMap][j] == null) {
					gp.projectileList[gp.currentMap][j] = projectile;
					break;
				}
			}
			
			shotAvailaibleCounter = 0;
		}
		
		
	}
	
	public void setKnockBack(Entity target, Entity attacker, int knockBackPower) {
		
		this.attacker = attacker;
		target.knockBackDirection = attacker.direction;
		target.speed += knockBackPower;
		target.knockBack = true;
	}
	
	public void searchPath(int goalCol, int goalRow) {

		int startCol = (worldX + solidArea.x) / gp.tileSize;
		int startRow = (worldY + solidArea.y) / gp.tileSize;
		
		gp.pFinder.setNodes(startCol, startRow, goalCol, goalRow);
		
		if(gp.pFinder.search() == true) {
			
			// Next WORLDX AND WORLDY
			int nextX = gp.pFinder.pathList.get(0).col * gp.tileSize;
			int nextY = gp.pFinder.pathList.get(0).row * gp.tileSize;
			
			// ENTITY's SOLIDAREA POSITION
			int enLeftX = worldX + solidArea.x;
			int enRightX = worldX + solidArea.x + solidArea.width;
			int enTopY = worldY + solidArea.y;
			int enBottomY = worldY + solidArea.y + solidArea.height;
			
			
			if(enTopY > nextY && enLeftX >= nextX && enRightX < nextX + gp.tileSize) {
				direction = "up";
			}
			else if(enTopY < nextY && enLeftX >= nextX && enRightX < nextX + gp.tileSize) {
				direction = "down";
			}
			else if(enTopY >= nextY && enBottomY < nextY + gp.tileSize) {
				// either left or right
				if(enLeftX > nextX) {
					direction = "left";
				}
				if(enLeftX < nextX) {
					direction = "right";
				}
			}
			else if(enTopY > nextY && enLeftX > nextX) {
				// either up or left
				direction = "up";
				checkCollision();
				if(collisionOn == true) direction = "left";
			}
			else if(enTopY > nextY && enLeftX < nextX) {
				// either up or right
				direction = "up";
				checkCollision();
				if(collisionOn == true) direction = "right";
			}
			else if(enTopY < nextY && enLeftX > nextX) {
				// either down or left
				direction = "down";
				checkCollision();
				if(collisionOn == true) direction = "left";
			}
			else if(enTopY < nextY && enLeftX < nextX) {
				// either down or right
				direction = "down";
				checkCollision();
				if(collisionOn == true) direction = "right";
			}
			
			
			// IF reach the gaol, then stpp searching
//			int nextCol = gp.pFinder.pathList.get(0).col;
//			int nextRow = gp.pFinder.pathList.get(0).row;
//			if(nextCol == goalCol && nextRow == goalRow) onPath = false;
			
		}

	}
		
	public int getDetected(Entity user, Entity target[][], String targetName) {
		
		int index = 999;
		
		int nextWorldX = user.getLeftX();
		int nextWorldY = user.getTopY();
		
		switch(user.direction) {
		case"up" : nextWorldY = user.getTopY()  - gp.player.speed; break;
		case"down" : nextWorldY = user.getBottomY() + gp.player.speed; break;
		case"left" : nextWorldX = user.getLeftX() - gp.player.speed; break;
		case"right" : nextWorldX = user.getRightX() + gp.player.speed; break;
		}
		
		int col = nextWorldX / gp.tileSize;
		int row = nextWorldY / gp.tileSize;
		
		for(int i=0; i<target[1].length; i++) {
			if(target[gp.currentMap][i] != null) {
				if(target[gp.currentMap][i].getCol() == col &&
						target[gp.currentMap][i].getRow() == row &&
						target[gp.currentMap][i].name.equals(targetName)) {
					index = i;
					break;
				}
			}
		}
		
		return index;
	}
	
}
 