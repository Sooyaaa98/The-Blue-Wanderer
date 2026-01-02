package object;


import entity.Entity;
import main.GamePanel;

public class Obj_Door extends Entity{

	GamePanel gp;
	public static final String objName = "Door";
	
	public Obj_Door(GamePanel gp) {
	
		super(gp);
		this.gp = gp;
		down1 = setUp("/objects/door", gp.tileSize, gp.tileSize);
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
		dialogues[0][0] = "You need a key to open this!";
	}
	public void interact() {
	
		startDialogue(this, 0);
	}


}
