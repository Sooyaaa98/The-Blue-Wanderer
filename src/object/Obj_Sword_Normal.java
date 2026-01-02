package object;

import entity.Entity;
import main.GamePanel;

public class Obj_Sword_Normal extends Entity{

	public static final String objName = "Normal Sword";
	
	public Obj_Sword_Normal(GamePanel gp) {
		super(gp);
		
		name = objName;
		down1 = setUp("/objects/sword_normal", gp.tileSize, gp.tileSize);
		type = type_sword;
		attackValue = 1;
		attackArea.width = 36;
		attackArea.height = 36;
		description = "[" + name + "]" + "\nAn old sword.";
		price = 150;
		knockBackPower = 4;
		motion1_duration = 5;
		motion2_duration = 25;
	}

}
