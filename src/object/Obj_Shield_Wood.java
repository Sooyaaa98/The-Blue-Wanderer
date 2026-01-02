package object;

import entity.Entity;
import main.GamePanel;

public class Obj_Shield_Wood extends Entity{

	public static final String objName = "Wood Shield";
	
	public Obj_Shield_Wood(GamePanel gp) {
		super(gp);
		
		
		name = objName;
		down1 = setUp("/objects/shield_wood", gp.tileSize, gp.tileSize);
		type = type_shield;
		defenseValue = 1;
		description = "[" + name + "]" + "\nMade by wood.";
		price = 100;
	}

}
