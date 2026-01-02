package object;


import entity.Entity;
import main.GamePanel;

public class Obj_Chest extends Entity{

	GamePanel gp;
	public static final String objName = "Chest";
	
	public Obj_Chest(GamePanel gp) {
		super(gp);
		this.gp = gp;
		
		name = objName;
		type = type_obstacle;
		image = setUp("/objects/chest", gp.tileSize, gp.tileSize);
		image2 = setUp("/objects/chest_opened", gp.tileSize, gp.tileSize);
		down1 = image;
		collision = true;
		
		solidArea.x = 4;
		solidArea.y = 16;
		solidArea.width = 40;
		solidArea.height = 32;
		solidAreaDefaultX = solidArea.x;
		solidAreaDefaultY = solidArea.y;
		
		
	}
	
	public void setLoot(Entity item) {
		this.loot = item;
		
		setDialogue();
	}
	
	public void setDialogue() {
		dialogues[0][0] = "You opened the chest and found " + loot.name + "!" + "\n...But you cannot carry more,\nyour inventory is full";
		
		dialogues[1][0] = "You opened the chest and found " + loot.name + "!" + "\nYou obtain the " + loot.name + "!";
		
		dialogues[2][0] = "It's empty!";
	}
	
	public void interact() {
		
		if(opened == false) {
			gp.playSE(3);
			
			if(gp.player.canObtainItem(loot) == false) {
				startDialogue(this, 0);
			}
			else {
				startDialogue(this, 1);
				down1 = image2;
				opened = true;
			}
		}
		else {
			startDialogue(this, 2);
		}
	}
}
