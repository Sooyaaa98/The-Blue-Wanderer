package object;

import entity.Entity;
import main.GamePanel;

public class Obj_Tent extends Entity {
	
	GamePanel gp;
	public static final String objName = "Tent";
	
	public Obj_Tent(GamePanel gp) {
		super(gp);
		this.gp = gp;

		type = type_consumables;
		down1 = setUp("/objects/tent", gp.tileSize, gp.tileSize);
		name = objName;
		description = "[Tent]\nYou can sleep in it\nuntil next morning";
		price = 300;
		stackable = true;
		
	}
	
	public boolean use(Entity entity) {
		
		gp.gameState = gp.sleepState;
		gp.playSE(14);
		gp.player.getSleepingImage(down1);
		return true;
	}
}
