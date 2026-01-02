package object;


import entity.Entity;
import main.GamePanel;

public class Obj_Boots extends Entity{
	
	GamePanel gp;
	int speedToIncrease;
	public static final String objName = "Boots";
	
	public Obj_Boots(GamePanel gp) {
		super(gp);
		
		this.gp = gp;
		
		description = "[Rubber Boots]\nIt increases your speed";
		price = 75;
		name = objName;
		type = type_consumables;
		speedToIncrease = 6;
		down1 = setUp("/objects/boots", gp.tileSize, gp.tileSize);
		
	}
	
	public boolean  use(Entity entity) {
		entity.speed = speedToIncrease;
		entity.changedSpeedAfterEquippingBoots = speedToIncrease;
		gp.playSE(2);
		
		
		return true;
	}
	
}
