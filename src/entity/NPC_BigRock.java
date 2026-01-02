package entity;

import java.awt.Rectangle;
import java.util.ArrayList;

import main.GamePanel;
import object.Obj_Door_Iron;
import tile_interactive.IT_MetalPlate;
import tile_interactive.InteractiveTile;

public class NPC_BigRock extends Entity{

	GamePanel gp;
	public static final String npcName = "Big Rock";
	
	public NPC_BigRock(GamePanel gp) {
		super(gp);
		
		this.gp = gp;
		
		direction = "down";
		speed = 4;
		name = npcName;
		
		solidArea = new Rectangle();
		solidArea.x = 2;
		solidArea.y = 6;
		solidAreaDefaultX = solidArea.x;
		solidAreaDefaultY = solidArea.y;
		solidArea.width = 44; // 44
		solidArea.height = 40; // 40
		
		getNPCImage();
		setDialogues();
	}
	
	
	public void getNPCImage() {
		
		
		up1 = setUp("/npc/bigrock", gp.tileSize, gp.tileSize);
		up2 = setUp("/npc/bigrock", gp.tileSize, gp.tileSize);
		down1 = setUp("/npc/bigrock", gp.tileSize, gp.tileSize);
		down2 = setUp("/npc/bigrock", gp.tileSize, gp.tileSize);
		left1 = setUp("/npc/bigrock", gp.tileSize, gp.tileSize);
		left2 = setUp("/npc/bigrock", gp.tileSize, gp.tileSize);
		right1 = setUp("/npc/bigrock", gp.tileSize, gp.tileSize);
		 right2 = setUp("/npc/bigrock", gp.tileSize, gp.tileSize);
		
		
	}
	
	public void setAction() {}
	
	public void update() {}
	
	public void setDialogues() {
		
		dialogues[0][0] = "I'm here to clean the ego of that iron door";
		
	}
	
	public void speak() {startDialogue(this, dialogueSet);}
	
	public void move(String direction) {
		this.direction = direction;
		
		checkCollision();
		
		if(collisionOn == false) {
			switch(direction) {
			case"up":   worldY -= speed; break;
			case"down":	worldY += speed; break;
			case"left":	worldX -= speed; break;
			case"right":worldX += speed; break;
		}
		}
		
		detectPlate();
	}
	
	public void detectPlate() {
		ArrayList<InteractiveTile> plateList = new ArrayList<>();
		ArrayList<Entity> rockList = new ArrayList<>();
		
		// add MetalPlates to a list
		for(int i=0; i<gp.iTile[1].length; i++) {
			
			if(gp.iTile[gp.currentMap][i] != null && 
					gp.iTile[gp.currentMap][i].name != null &&
					gp.iTile[gp.currentMap][i].name.equals(IT_MetalPlate.itName)) {
						plateList.add(gp.iTile[gp.currentMap][i]); 
			}
		}
		
		// add BigRock to a list
		for(int i=0; i<gp.npc[1].length; i++) {
			
			if(gp.npc[gp.currentMap][i] != null && 
					gp.npc[gp.currentMap][i].name.equals(NPC_BigRock.npcName)) {
						rockList.add(gp.npc[gp.currentMap][i]);
			}
		}
		
		int counter = 0; // counts the no. of rock that are on the plate
		
		// scan the PlateList
		for(int i=0; i<plateList.size(); i++) {
			int xDistance = Math.abs(worldX - plateList.get(i).worldX);
			int yDistance = Math.abs(worldY - plateList.get(i).worldY);
			
			int distance = Math.max(xDistance, yDistance);
		
			/*
			 *  if the distance btw plate and rock is less than 8 that means rock is on plate
			 *  and add metalPlate to the rock's linkedEntity and if distance is more than 8
			 *  that means rock is no more on the plate and hence we remove metalPlate from linkedEntity 
			 */
				
			if(distance < 8) {
				if(linkedEntity == null) {
					linkedEntity = plateList.get(i);
					gp.playSE(3);
				}
			}
			else {
				if(linkedEntity == plateList.get(i)) {
					linkedEntity = null;
				}
			}
		}
		
		// increase counter 
		for(int i=0; i<rockList.size(); i++) {
			if(rockList.get(i).linkedEntity != null) {
				counter++;
			}
		}
		
		if(counter == rockList.size()) {
			for(int i=0; i<gp.obj[1].length; i++) {
				if(gp.obj[gp.currentMap][i] != null && 
						gp.obj[gp.currentMap][i].name.equals(Obj_Door_Iron.objName)) {
					
					gp.obj[gp.currentMap][i] = null;
					gp.playSE(21);
				}
			}
		}
		
	}
}
