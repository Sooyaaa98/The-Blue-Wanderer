package object;

import entity.Entity;
import main.GamePanel;

public class Obj_Shield_Blue extends Entity {
	
	public static final String objName = "Blue Shield";
	
	public Obj_Shield_Blue(GamePanel gp) {
		super(gp);
		
		
		name = objName;
		down1 = setUp("/objects/shield_blue", gp.tileSize, gp.tileSize);
		type = type_shield;
		defenseValue = 2;
		description = "[" + name + "]" + "\nA shiny blue Shield.";
		price = 200;
	}
}
