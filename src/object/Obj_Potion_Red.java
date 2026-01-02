package object;

import entity.Entity;
import main.GamePanel;

public class Obj_Potion_Red extends Entity{

	GamePanel gp;
	public static final String objName = "Red Potion";
	
	public Obj_Potion_Red(GamePanel gp) {
		super(gp);
		
		this.gp = gp;
		
		name = objName;
		value = 5;
		type = type_consumables;
		down1 = setUp("/objects/potion_red", gp.tileSize, gp.tileSize);
		description = "[Red Potion]\nHeals your life by" + value + ".";
		price = 150;
		stackable = true;
		
		setDialogue();
	}
	
	public void setDialogue() {
		dialogues[0][0] = "You drink the " + name + "!\n"
				+ "Your life has been recovered by " + value + ".";
		
	}
	
	public boolean use(Entity entity) {
		
		startDialogue(this, 0);
		entity.life += value;
		
		gp.playSE(2);
		
		return true;
	}

}
