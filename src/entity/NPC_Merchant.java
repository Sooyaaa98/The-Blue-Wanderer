package entity;

import java.awt.Rectangle;

import main.GamePanel;
import object.Obj_Axe;
import object.Obj_Boots;
import object.Obj_Key;
import object.Obj_Lantern;
import object.Obj_Potion_Red;
import object.Obj_Shield_Blue;
import object.Obj_Shield_Wood;
import object.Obj_Sword_Normal;

public class NPC_Merchant extends Entity {
	
	public NPC_Merchant(GamePanel gp) {
		super(gp);
		
		direction = "down";
		speed = 1;
		
		solidArea = new Rectangle();
		solidArea.x = 8;
		solidArea.y = 16;
		solidAreaDefaultX = solidArea.x;
		solidAreaDefaultY = solidArea.y;
		solidArea.width = 32;
		solidArea.height = 32;
		
		getNPCImage();
		setDialogues();
		setItems();
	}
	
	
	public void getNPCImage() {
		
		
		up1 = setUp("/npc/merchant_down_1", gp.tileSize, gp.tileSize);
		up2 = setUp("/npc/merchant_down_2", gp.tileSize, gp.tileSize);
		down1 = setUp("/npc/merchant_down_1", gp.tileSize, gp.tileSize);
		down2 = setUp("/npc/merchant_down_2", gp.tileSize, gp.tileSize);
		left1 = setUp("/npc/merchant_down_1", gp.tileSize, gp.tileSize);
		left2 = setUp("/npc/merchant_down_2", gp.tileSize, gp.tileSize);
		right1 = setUp("/npc/merchant_down_1", gp.tileSize, gp.tileSize);
		right2 = setUp("/npc/merchant_down_2", gp.tileSize, gp.tileSize);
		
		
	}
	
	public void setDialogues() {
		
		dialogues[0][0] = "He he, so you found me.\nI have some good stuff.\nDo you want to trade?";
		dialogues[1][0] = "Come again, hehe!";
		dialogues[2][0] = "You need more coins to buy that!";
		dialogues[3][0] = "Inventory is full!";
		dialogues[4][0] = "You cannot sell an equipped item!";
				
	}

	public void setItems() {
		
		inventory.add(new Obj_Potion_Red(gp));
		inventory.add(new Obj_Key(gp));
		inventory.add(new Obj_Axe(gp));
		inventory.add(new Obj_Shield_Wood(gp));
		inventory.add(new Obj_Shield_Blue(gp));
		inventory.add(new Obj_Sword_Normal(gp));
		inventory.add(new Obj_Boots(gp));
		inventory.add(new Obj_Lantern(gp));
	}
	public void speak() {
		super.speak();
		gp.gameState = gp.tradeState;
		gp.ui.npc = this;
		facePlayer();
	}
}
