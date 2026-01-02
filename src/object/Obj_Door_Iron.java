package object;

import entity.Entity;
import main.GamePanel;

public class Obj_Door_Iron extends Entity{

	GamePanel gp;
	public static final String objName = "Iron Door";
	
	public Obj_Door_Iron(GamePanel gp) {
		super(gp);
		
		this.gp = gp;
		down1 = setUp("/objects/door_iron", gp.tileSize, gp.tileSize);
		collision = true;
		
		name  = objName;
		type = type_obstacle;
		solidArea.x = 0;
		solidArea.y = 16;
		solidArea.width = 48;
		solidArea.height = 32;
		solidAreaDefaultX = solidArea.x;
		solidAreaDefaultY = solidArea.y;
		
		setDialogue();
	}

	public void setDialogue() {
		dialogues[0][0] = "It isn't going to open easily\n";
	}
	public void interact() {
	
		startDialogue(this, 0);
	}

}
