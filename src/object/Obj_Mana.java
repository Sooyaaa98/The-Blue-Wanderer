package object;

import entity.Entity;
import main.GamePanel;

public class Obj_Mana extends Entity{

	GamePanel gp;
	public static final String objName = "Mana Crystal";
	
	public Obj_Mana(GamePanel gp) {
		super(gp);
		
		this.gp = gp;
		
		type = type_pickupOnly;
		name = objName;
		value = 1;
		down1 = setUp("/objects/manacrystal_full", gp.tileSize, gp.tileSize);
		image = setUp("/objects/manacrystal_full", gp.tileSize, gp.tileSize);
		image2 = setUp("/objects/manacrystal_blank", gp.tileSize, gp.tileSize);
		
	}

	public boolean  use(Entity entity) {
		
		gp.playSE(2);
		gp.ui.addMessage("Mana + " + value);
		gp.player.mana += value;
		
		return true;
	}
}
