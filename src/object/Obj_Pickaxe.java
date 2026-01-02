package object;

import entity.Entity;
import main.GamePanel;

public class Obj_Pickaxe extends Entity{
	
public static final String objName = "Pickaxe"; 
	
	public Obj_Pickaxe(GamePanel gp) {
		super(gp);
		
		
		name = objName;
		down1 = setUp("/objects/pickaxe", gp.tileSize, gp.tileSize);
		type =  type_pickaxe;
		attackValue = 1;
		attackArea.width = 36;
		attackArea.height = 36;
		description = "[Pickaxe]\nDig with it!";
		knockBackPower = 1;
		price = 100;
		motion1_duration = 10;
		motion2_duration = 20;
	}
}
