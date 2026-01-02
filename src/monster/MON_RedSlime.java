package monster;

import java.util.Random;

import entity.Entity;
import main.GamePanel;
import object.Obj_Coin_Bronze;
import object.Obj_Heart;
import object.Obj_Mana;
import object.Obj_Rock;

public class MON_RedSlime extends Entity{
	
	GamePanel gp;
	
	public MON_RedSlime(GamePanel gp) {
		super(gp);
		
		this.gp = gp;
		
		name = "Red Slime";
		defaultSpeed = 1;
		speed = defaultSpeed;
		maxLife = 1;
		life = maxLife;
		type = type_monster;
		attack = 2;
		defense = 0;
		exp = 1;
		projectile = new Obj_Rock(gp);
		
		solidArea.x = 3;
		solidArea.y = 18;
		solidArea.width = 42;
		solidArea.height = 30;
		solidAreaDefaultX = solidArea.x;
		solidAreaDefaultY = solidArea.y;
		
		getImage();
	}
	
	public void getImage() {
		
		up1 = setUp("/monster/redslime_down_1", gp.tileSize, gp.tileSize);
		up2 = setUp("/monster/redslime_down_2", gp.tileSize, gp.tileSize);
		down1 = setUp("/monster/redslime_down_1", gp.tileSize, gp.tileSize);
		down2 = setUp("/monster/redslime_down_2", gp.tileSize, gp.tileSize);
		left1 = setUp("/monster/redslime_down_1", gp.tileSize, gp.tileSize);
		left2 = setUp("/monster/redslime_down_2", gp.tileSize, gp.tileSize);
		right1 = setUp("/monster/redslime_down_1", gp.tileSize, gp.tileSize);
		right2 = setUp("/monster/redslime_down_2", gp.tileSize, gp.tileSize);
		
	}
	
	public void update() {
		super.update();
	}
	
	public void setAction() {
	
		if(onPath == true) {
			
			// Check if it stops chasing
			checkStopChasingOrNot(gp.player, 10, 50);
			
			// increase speed when monster gets angry and starts chasing player
			speed = 2;
			
			// search the optimum path between player and the monster
			searchPath(getGoalCol(gp.player), getGoalRow(gp.player));
			
			// check if it shoots a projectile 
			checkShootOrNot(200, 120);
		}
		else {
			
			// make monster angry if player is in 7 tile radius to monster
			checkStartChasingOrNot(gp.player, 10 , 100);
			
			// Monster's AI for it's random movements
			getRandomDirection(120);
		}
	}
	
	public void damageReaction() {
		
		actionLockCounter = 0;
		onPath = true;
	}
	
	public void checkDrop() {
		
		int i = new Random().nextInt(100)+1;
		
		if(i < 50) {
			dropItem(new Obj_Coin_Bronze(gp));
		}
		else if(i > 50 && i < 60) {
			dropItem(new Obj_Heart(gp));
		}
		else if(i > 60 && i < 100) {
			dropItem(new Obj_Mana(gp));
		}
	}
	
}






