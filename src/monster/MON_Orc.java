package monster;

import java.util.Random;

import entity.Entity;
import main.GamePanel;
import object.Obj_Coin_Bronze;
import object.Obj_Heart;
import object.Obj_Mana;

public class MON_Orc extends Entity {

	GamePanel gp;
	
	public MON_Orc(GamePanel gp) {
		super(gp);
		
		this.gp = gp;
		
		name = "Orc";
		defaultSpeed = 1;
		speed = defaultSpeed;
		maxLife = 5;
		life = maxLife;
		type = type_monster;
		attack = 3;
		defense = 1;
		exp = 2;
		motion1_duration = 40;
		motion2_duration = 85;
		knockBackPower = 5;
		
		solidArea.x = 4;
		solidArea.y = 4;
		solidArea.width = 40;
		solidArea.height = 44;
		solidAreaDefaultX = solidArea.x;
		solidAreaDefaultY = solidArea.y;
		attackArea.width = 48;
		attackArea.height = 48;
		
		getImage();
		getAttackImage();
	}
	
	public void getImage() {
		
		up1 = setUp("/monster/orc_up_1", gp.tileSize, gp.tileSize);
		up2 = setUp("/monster/orc_up_2", gp.tileSize, gp.tileSize);
		down1 = setUp("/monster/orc_down_1", gp.tileSize, gp.tileSize);
		down2 = setUp("/monster/orc_down_2", gp.tileSize, gp.tileSize);
		left1 = setUp("/monster/orc_left_1", gp.tileSize, gp.tileSize);
		left2 = setUp("/monster/orc_left_2", gp.tileSize, gp.tileSize);
		right1 = setUp("/monster/orc_right_1", gp.tileSize, gp.tileSize);
		right2 = setUp("/monster/orc_right_2", gp.tileSize, gp.tileSize);
		
	}
	
	public void getAttackImage() {
		
		attackUp1 = setUp("/monster/orc_attack_up_1", gp.tileSize, gp.tileSize*2);
		attackUp2 = setUp("/monster/orc_attack_up_2", gp.tileSize, gp.tileSize*2);
		attackdown1 = setUp("/monster/orc_attack_down_1", gp.tileSize, gp.tileSize*2);
		attackdown2 = setUp("/monster/orc_attack_down_2", gp.tileSize, gp.tileSize*2);
		attackleft1 = setUp("/monster/orc_attack_left_1", gp.tileSize*2, gp.tileSize);
		attackleft2 = setUp("/monster/orc_attack_left_2", gp.tileSize*2, gp.tileSize);
		attackright1 = setUp("/monster/orc_attack_right_1", gp.tileSize*2, gp.tileSize);
		attackright2 = setUp("/monster/orc_attack_right_2", gp.tileSize*2, gp.tileSize);
	}
	
	public void update() {
		super.update();
		
	}
	
	public void setAction() {
	
		if(onPath == true) {
			
			// Check if it stops chasing
			checkStopChasingOrNot(gp.player, 10, 50);
			
//			// increase speed when monster gets angry and starts chasing player
//			speed = 2;
			
			// search the optimum path between player and the monster
			searchPath(getGoalCol(gp.player), getGoalRow(gp.player));
			
		}
		else {
			
			// make monster angry if player is in 7 tile radius to monster
			checkStartChasingOrNot(gp.player, 7, 100);
			
			// Monster's AI for it's random movements
			getRandomDirection(120);
		}

		// check if it attacks
		if(attacking == false){
			checkAttackOrNot(30, gp.tileSize*4, gp.tileSize);
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
