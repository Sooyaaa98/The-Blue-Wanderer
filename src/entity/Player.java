package entity;

import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import main.GamePanel;
import main.KeyHandler;
import object.Obj_Fireball;
import object.Obj_Key;
import object.Obj_Lantern;
import object.Obj_Shield_Wood;
import object.Obj_Sword_Normal;

public class Player extends Entity{
	
	
	KeyHandler keyH;
	
	public final int screenX;
	public final int screenY;
	public boolean attackCanceled = false;
	public boolean lightUpdated = false;
	
	public Player(GamePanel gp, KeyHandler keyH) {
		
		super(gp);
		
		
		this.keyH = keyH;
		
		screenX = gp.screenWidth/2 - (gp.tileSize/2);
		screenY = gp.screenHeight/2 - (gp.tileSize/2);
		
		solidArea = new Rectangle();
		solidArea.x = 8;
		solidArea.y = 16;
		solidAreaDefaultX = solidArea.x;
		solidAreaDefaultY = solidArea.y;
		solidArea.width = 32;
		solidArea.height = 32;

//		attackArea.width = 36;
//		attackArea.height = 36;
		
		setDefaultValue();
		
	}
	public void setDefaultValue() {
		
		 worldX = gp.tileSize * 23;
		 worldY = gp.tileSize * 21;
		
		//  worldX = gp.tileSize * 12;
		//  worldY = gp.tileSize * 10;
		
		 defaultSpeed = 4;
		 speed = defaultSpeed;
		 direction = "down";
		 
		 
		 //Player Status
		 level = 1;
		//  level = 5;
		 maxLife = 10;
		 life = maxLife;
		 strength = 1;
		//  strength = 5;
		 dexterity = 1;
		//  dexterity = 5;
		 exp = 0;
		 nextLevelExp = 5;
		 coin = 500;
		 maxMana = 5;
		 mana = maxMana;
		 currentWeapon = new Obj_Sword_Normal(gp);
		 currentShield = new Obj_Shield_Wood(gp);
		 currentLight = null;
		 projectile = new Obj_Fireball(gp);
		 attack = getAttack();
		 defense = getDefense();
		 
		getImage();
		getAttackImage();
		getGuardImage();
		setItems();
		setDialogue();
	}
	public void setDefaultPositions() {
		gp.currentMap = 0;
		
		 worldX = gp.tileSize * 23;
		 worldY = gp.tileSize * 21;
		 direction = "down";
	}
	public void restoreStatus() {
		life = maxLife;
		mana = maxMana;
		speed = defaultSpeed;
		invincible = false;
		transparent = false;
		attacking = false;
		knockBack = false;
		guarding = false;
		lightUpdated = true;
		
		defaultSpeed = 4;
		speed = defaultSpeed;
		changedSpeedAfterEquippingBoots = 0;
	}
	public void setItems() {
		
		inventory.clear();
		inventory.add(currentWeapon);
		inventory.add(currentShield);
		inventory.add(new Obj_Lantern(gp));
		inventory.add(new Obj_Key(gp));
		
	}
	public int getAttack() {
		
		attackArea = currentWeapon.attackArea;
		motion1_duration = currentWeapon.motion1_duration;
		motion2_duration = currentWeapon.motion2_duration;
		return strength * currentWeapon.attackValue;
	}
	public int getDefense() {
		return dexterity * currentShield.defenseValue;
	}
	public int getCurrentWeaponSlot(){
		int currentWeaponSlot = 0;
		for(int i=0; i<inventory.size(); i++){
			if(currentWeapon == inventory.get(i)){
				currentWeaponSlot = i;
			}
		}
		return currentWeaponSlot;
	}
	public int getcurrentShieldSlot(){
		int currentShieldSlot = 0;
		for(int i=0; i<inventory.size(); i++){
			if(currentShield == inventory.get(i)){
				currentShieldSlot = i;
			}
		}
		return currentShieldSlot;
	}
	public void getImage() {
			
		up1 = setUp("/Player/boy_up_1", gp.tileSize, gp.tileSize);
		up2 = setUp("/Player/boy_up_2", gp.tileSize, gp.tileSize);
		down1 = setUp("/Player/boy_down_1", gp.tileSize, gp.tileSize);
		down2 = setUp("/Player/boy_down_2", gp.tileSize, gp.tileSize);
		left1 = setUp("/Player/boy_left_1", gp.tileSize, gp.tileSize);
		left2 = setUp("/Player/boy_left_2", gp.tileSize, gp.tileSize);
		right1 = setUp("/Player/boy_right_1", gp.tileSize, gp.tileSize);
		 right2 = setUp("/Player/boy_right_2", gp.tileSize, gp.tileSize);
		
	}
	public void getAttackImage() {
		
		if(currentWeapon.type == type_sword) {
			attackUp1 = setUp("/playerAttack/boy_attack_up_1", gp.tileSize, gp.tileSize*2);
			attackUp2 = setUp("/playerAttack/boy_attack_up_2", gp.tileSize, gp.tileSize*2);
			attackdown1 = setUp("/playerAttack/boy_attack_down_1", gp.tileSize, gp.tileSize*2);
			attackdown2 = setUp("/playerAttack/boy_attack_down_2", gp.tileSize, gp.tileSize*2);
			attackleft1 = setUp("/playerAttack/boy_attack_left_1", gp.tileSize*2, gp.tileSize);
			attackleft2 = setUp("/playerAttack/boy_attack_left_2", gp.tileSize*2, gp.tileSize);
			attackright1 = setUp("/playerAttack/boy_attack_right_1", gp.tileSize*2, gp.tileSize);
			attackright2 = setUp("/playerAttack/boy_attack_right_2", gp.tileSize*2, gp.tileSize);
		}
		
		if(currentWeapon.type == type_axe) {
			attackUp1 = setUp("/playerAttack/boy_axe_up_1", gp.tileSize, gp.tileSize*2);
			attackUp2 = setUp("/playerAttack/boy_axe_up_2", gp.tileSize, gp.tileSize*2);
			attackdown1 = setUp("/playerAttack/boy_axe_down_1", gp.tileSize, gp.tileSize*2);
			attackdown2 = setUp("/playerAttack/boy_axe_down_2", gp.tileSize, gp.tileSize*2);
			attackleft1 = setUp("/playerAttack/boy_axe_left_1", gp.tileSize*2, gp.tileSize);
			attackleft2 = setUp("/playerAttack/boy_axe_left_2", gp.tileSize*2, gp.tileSize);
			attackright1 = setUp("/playerAttack/boy_axe_right_1", gp.tileSize*2, gp.tileSize);
			attackright2 = setUp("/playerAttack/boy_axe_right_2", gp.tileSize*2, gp.tileSize);
		}	
		
		
		if(currentWeapon.type == type_pickaxe) {
			attackUp1 = setUp("/playerAttack/boy_pick_up_1", gp.tileSize, gp.tileSize*2);
			attackUp2 = setUp("/playerAttack/boy_pick_up_2", gp.tileSize, gp.tileSize*2);
			attackdown1 = setUp("/playerAttack/boy_pick_down_1", gp.tileSize, gp.tileSize*2);
			attackdown2 = setUp("/playerAttack/boy_pick_down_2", gp.tileSize, gp.tileSize*2);
			attackleft1 = setUp("/playerAttack/boy_pick_left_1", gp.tileSize*2, gp.tileSize);
			attackleft2 = setUp("/playerAttack/boy_pick_left_2", gp.tileSize*2, gp.tileSize);
			attackright1 = setUp("/playerAttack/boy_pick_right_1", gp.tileSize*2, gp.tileSize);
			attackright2 = setUp("/playerAttack/boy_pick_right_2", gp.tileSize*2, gp.tileSize);
		}	
	}
	public void getSleepingImage(BufferedImage image) {
		up1 = image;
		up2 = image;
		down1 = image;
		down2 = image;
		left1 = image;
		left2 = image;
		right1 = image;
		right2 = image;
		
	}
	public void getGuardImage() {
		
		guardUp = setUp("/Player/boy_guard_up", gp.tileSize, gp.tileSize);
		guardDown = setUp("/Player/boy_guard_down", gp.tileSize, gp.tileSize);
		guardLeft = setUp("/Player/boy_guard_left", gp.tileSize, gp.tileSize);
		guardRight = setUp("/Player/boy_guard_right", gp.tileSize, gp.tileSize);
	}
	public void update() {
		
		if(knockBack == true) {
			
			collisionOn = false;
			gp.cChecker.checkTile(this);
			gp.cChecker.checkEntity(this, gp.iTile);
			gp.cChecker.checkObject(this, true);
			gp.cChecker.checkEntity(this, gp.npc);
			gp.cChecker.checkEntity(this, gp.monster);
			
			
			if(collisionOn == true) {
				knockBackCounter = 0;
				knockBack = false;
				if(changedSpeedAfterEquippingBoots == 0) {
					speed = defaultSpeed;
				}
				else {
					speed = changedSpeedAfterEquippingBoots;
				}
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
				if(changedSpeedAfterEquippingBoots == 0) {
					speed = defaultSpeed;
				}
				else {
					speed = changedSpeedAfterEquippingBoots;
				}
			}
		}
		else if(attacking == true) {
			attacking();
		}
		else if(keyH.spacePressed == true) {
			guarding = true;
			guardCounter++;
		}
		else if(keyH.upPressed == true|| keyH.downPressed == true|| keyH.leftPressed == true
				|| keyH.rightPressed == true|| keyH.enterPressed == true){
			
				if(keyH.upPressed == true) {
					direction = "up";
				}
				else if(keyH.downPressed == true) {
					direction = "down";
				}
				else if(keyH.leftPressed == true) {
					direction = "left";
				}
				else if(keyH.rightPressed == true) {
					direction = "right";		
				}
			
			 
			// CHECK TILE COLLISION
			collisionOn = false;
			gp.cChecker.checkTile(this);
			
			
			// check InteractiveTile Collision
			 gp.cChecker.checkEntity(this, gp.iTile);
			
			 
			// check OBJECT's COLLISION
			int objectIndex = gp.cChecker.checkObject(this, true);
			pickUpObject(objectIndex);
			
			
			//check NPC's Collision
			int npcIndex = gp.cChecker.checkEntity(this, gp.npc);
			interactNPC(npcIndex);
			
			//CHECK MONSTER COLLISION
			int monsterIndex = gp.cChecker.checkEntity(this, gp.monster);
			contactMonster(monsterIndex);
			
			//Check Event
			gp.eHandler.checkEvent();
			
			
			// IF COLLISION IS FALSE, PLAYER CAN MOVE
			if(collisionOn == false && gp.keyH.enterPressed == false) {
				
				switch(direction) {
				case"up": worldY -= speed;
					break;
				case"down":	worldY += speed;
					break;
				case"left":	worldX -= speed;
					break;
				case"right":worldX += speed;
					break;
				}
			} 
			
			if(keyH.enterPressed == true && attackCanceled == false) {
				gp.playSE(7);
				attacking = true;
				spriteCounter = 0;
			}
			
			attackCanceled = false;
			gp.keyH.enterPressed = false;
			guarding = false;
			guardCounter = 0;
			
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
		else {
			standCounter++;
			if(standCounter >= 20) {
				spriteNum = 1;
				standCounter = 0;
			}
			guarding = false;
			guardCounter = 0;
		}
		
		
		// SHOOT PROJECTILE
		if(gp.keyH.shotKeyPressed == true && projectile.alive == false 
				&& shotAvailaibleCounter == 120 && projectile.haveResource(this) == true) {
			
			// cordinates for projectiles
			projectile.set(worldX, worldY, direction, true, this);
			
//			gp.projectileList.add(projectile);
			for(int i=0; i<gp.projectileList[1].length; i++) {
				if(gp.projectileList[gp.currentMap][i] == null) {
					gp.projectileList[gp.currentMap][i] = projectile;
					break;
				}
			}
			
			shotAvailaibleCounter = 0;
			
			projectile.subtractResource(this);
			gp.playSE(10);
		}
		
		
		//This needs to be outside of if-else block
		if(invincible == true) {
			invincibleCounter++;
			
			if(invincibleCounter > gp.FPS) {
				invincible = false;
				transparent = false;
				invincibleCounter = 0;
			}
		}
		
		if(shotAvailaibleCounter < 120) {
			shotAvailaibleCounter++;
		}
		
		if(life > maxLife) {
			life = maxLife;
		}
		
		if(mana > maxMana) {
			mana = maxMana;
		}
		
		// Show GameOver State when player dies:
		if(gp.keyH.godModeOn == false) {
			if(life <= 0) {
			 	gp.gameState = gp.gameOverState;
			 	gp.playSE(12);
			 	gp.stopMusic();
			 	gp.ui.commandNum = -1;
			 }
		}
		 
		
	}
	public void pickUpObject(int index) {
		
		if(index != 999) {
			
			// PICK ONLY ITEMS
			if(gp.obj[gp.currentMap][index].type == type_pickupOnly) {
			
				gp.obj[gp.currentMap][index].use(this);
				gp.obj[gp.currentMap][index] = null;
			}
			// OBSTACLES
			else if(gp.obj[gp.currentMap][index].type == type_obstacle) {
				if(gp.keyH.enterPressed == true) {
					attackCanceled = true;
					gp.obj[gp.currentMap][index].interact();
				}
			}
			
			// INVENTORY ITEMS
			else {
				String text;
				if(canObtainItem(gp.obj[gp.currentMap][index]) == true) {
					gp.playSE(1);
					text = "Got a " + gp.obj[gp.currentMap][index].name + "!" ;
				}
				else {
					text = "Inventory is full!";
				}
				gp.obj[gp.currentMap][index] = null;
				gp.ui.addMessage(text);
			}		
		}
	}
	public void interactNPC(int index) {
			if(index != 999) {
				
				if(gp.keyH.enterPressed == true) {
					attackCanceled = true;
					gp.npc[gp.currentMap][index].speak();
				}
				gp.npc[gp.currentMap][index].move(direction);	
			}
	}
	public void setDialogue() {
		dialogues[0][0] = "You are at level " + level + "now!\n" 
				+ "You feel stronger!";
	}
	public void contactMonster(int i) {
		
		if(i != 999) {
			if(invincible == false && gp.monster[gp.currentMap][i].dying == false) {
				gp.playSE(6);
	
				int damage = gp.monster[gp.currentMap][i].attack - defense;
				if(damage < 1) {
					damage = 1;
				}
				life -= damage;
				transparent = true;
				invincible = true;
			}
		}
	}
	public void damageMonster(int i, Entity attacker, int attack, int knockBackPower) {
		
		if(i != 999) {
			
			if(gp.monster[gp.currentMap][i].invincible == false) {
				gp.playSE(5);
				
				if(knockBackPower > 0) {
					setKnockBack(gp.monster[gp.currentMap][i], attacker,  knockBackPower);
				}
				if(gp.monster[gp.currentMap][i].offBalance == true) {
					attack *= 2;
				}
				
				int damage = attack - gp.monster[gp.currentMap][i].defense;

				if(damage < 0) {
					damage = 0;
				}
				gp.monster[gp.currentMap][i].life -= damage;
				gp.monster[gp.currentMap][i].invincible = true;
				gp.monster[gp.currentMap][i].damageReaction();
				gp.ui.addMessage(damage + " Damage!");
				
				if(gp.monster[gp.currentMap][i].life <= 0) {
					gp.monster[gp.currentMap][i].dying =true;
					gp.ui.addMessage(gp.monster[gp.currentMap][i].name + " just died!");
					gp.ui.addMessage("Exp + " + gp.monster[gp.currentMap][i].exp);
					exp += gp.monster[gp.currentMap][i].exp;
					checkLevelUp();
				}
			}
			
		}
		
	}
	public void damageInteractiveTile(int i) {
		if(i != 999 && gp.iTile[gp.currentMap][i].destructible == true && gp.iTile[gp.currentMap][i].isCorrectItem(this) == true
				&& gp.iTile[gp.currentMap][i].invincible == false) {
			gp.iTile[gp.currentMap][i].playSE();
			gp.iTile[gp.currentMap][i].life--;
			gp.iTile[gp.currentMap][i].invincible = true;
			
			generateParticle(gp.iTile[gp.currentMap][i], gp.iTile[gp.currentMap][i]);
			
			if(gp.iTile[gp.currentMap][i].life == 0) {
				gp.iTile[gp.currentMap][i] = gp.iTile[gp.currentMap][i].getDestroyedForm();
			}
		}	
	}
	public void checkLevelUp() {
		
		if(exp >= nextLevelExp) {
			
			level++;
			nextLevelExp *= 2;
			maxLife += 2;
			strength++;
			dexterity++;
			
			attack = getAttack();
			defense = getDefense();
			
			gp.playSE(8);
			
			setDialogue();
			startDialogue(this, 0);
			
		}
	}
	public void selectItem() {
		
		// called in KeyHandler.class
		
		int itemIndex = gp.ui.getItemIndexOnSlot(gp.ui.playerSlotCol, gp.ui.playerSlotRow);
		
		if(itemIndex < inventory.size()) {
			
			Entity selectedItem = inventory.get(itemIndex);
			
			if(selectedItem.type == type_sword || selectedItem.type == type_axe || selectedItem.type == type_pickaxe) {
				currentWeapon = selectedItem;
				attack = getAttack();
				getAttackImage();
			}
			
			if(selectedItem.type == type_shield) {
				currentShield = selectedItem;
				defense = getDefense();
			}
			
			if(selectedItem.type == type_light) {
				if(currentLight == selectedItem) {
					currentLight = null;
				}
				else {
					currentLight = selectedItem;
				}
				lightUpdated = true;
			}
			
			if(selectedItem.type == type_consumables) {
				
				if(selectedItem.use(this) == true) {
					if(selectedItem.amount > 1) {
						selectedItem.amount--;
					}
					else {
						inventory.remove(itemIndex);
					}
				}	
			}
		}
	}
	public void damageProjectile(int i) {
		if(i != 999) {
			
			gp.projectileList[gp.currentMap][i].alive = false;
			generateParticle(gp.projectileList[gp.currentMap][i],gp.projectileList[gp.currentMap][i] );
		}
	}
	public int searchIteminInventory(String itemName) {
		
		int itemIndex = 999;
		
		for(int i=0; i<inventory.size(); i++) {
			if(inventory.get(i).name.equals(itemName)) {
				itemIndex = i;
				break;
			}
		}
		
		return itemIndex;
	}
	public boolean canObtainItem(Entity item) {
		
		boolean canObtain = false;
		Entity newItem = gp.eGenerator.getObject(item.name);
		
		/*
		 *  check if stackable if yes, check is there already a same item present in the inventory if yes just increase amount of that item,
		 *  if that item is not present in inventory check for vacant space and add it. If the item isn't stackable check for vacant space and add it.
		 */
		
		if(newItem.stackable == true) {
			int itemIndex = searchIteminInventory(newItem.name);
			
			if(itemIndex != 999) { // item is already present in inventory and can be stackable
				inventory.get(itemIndex).amount++;
				canObtain = true;
			}
			else {
				if(inventory.size() != maxInventorySize) {
					inventory.add(newItem);
					canObtain = true;
				}
			}
		}
		else {
			if(inventory.size() != maxInventorySize) {
				inventory.add(newItem);
				canObtain = true;
			}
		}
		
		return canObtain;
	}
	public void draw(Graphics2D g2) {
				
		BufferedImage image = null;
		int tempScreenX = screenX;
		int tempScreenY = screenY;
		
		
		switch(direction){
		case "up":
			if(attacking == false) {
				if(spriteNum == 1){ image = up1; }
				if(spriteNum == 2){ image = up2; }
			}
			if(attacking == true) {
				 tempScreenY = screenY - gp.tileSize;
				if(spriteNum == 1){ image = attackUp1; }
				if(spriteNum == 2){ image = attackUp2; }
			}
			if(guarding == true) {
				image = guardUp;
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
			if(guarding == true) {
				image = guardDown;
			}
			break;
		case "left":
			if(attacking == false) {
				if(spriteNum == 1){ image = left1; }
				if(spriteNum == 2){ image = left2; }
			}
			 if(attacking == true) {
				 tempScreenX = screenX - gp.tileSize;
				if(spriteNum == 1){ image = attackleft1; }
				if(spriteNum == 2){ image = attackleft2; }
			}
			if(guarding == true) {
				image = guardLeft;
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
			if(guarding == true) {
				image = guardRight;
			}
			break;
		}
		
		
		if(transparent == true) {
			g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.4f));
		}
		

		if(drawing == true){
			g2.drawImage(image, tempScreenX, tempScreenY,  null);
		}
		
		//RESET ALPHA          V.IMP
		g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
		
	}

	
}
