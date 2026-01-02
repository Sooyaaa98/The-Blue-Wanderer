package object;

import entity.Entity;
import main.GamePanel;

public class Obj_Axe extends Entity {

	public static final String objName = "WoodCutter's Axe"; 
	
	public Obj_Axe(GamePanel gp) {
		super(gp);
		
		
		name = objName;
		down1 = setUp("/objects/axe", gp.tileSize, gp.tileSize);
		type =  type_axe;
		attackValue = 2;
		attackArea.width = 36;
		attackArea.height = 36;
		description = "[WoodCutter's Axe]\nA bit rusty but still can\ncut trees like a butter!";
		knockBackPower = 8;
		price = 100;
		motion1_duration = 5;
		motion2_duration = 25;
	}

}
