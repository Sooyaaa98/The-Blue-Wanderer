package monster;


import data.Progress;
import entity.Entity;
import main.GamePanel;
import object.Obj_Door_Iron;

public class MON_SkeletonLord extends Entity{
	
	GamePanel gp;
	public static final String monName = "Skeleton Lord";
	
	public MON_SkeletonLord(GamePanel gp) {
		super(gp);
		
		this.gp = gp;
		
		type = type_monster;
		boss = true;
		name = monName;
		defaultSpeed = 1;
		speed = defaultSpeed;
		maxLife = 50;
		life = maxLife;
		attack = 10;
		defense = 2;
		exp = 50;
		knockBackPower = 5;
		sleep = true;
		
		
		int size = gp.tileSize * 5;
		solidArea.x = 48;
		solidArea.y = 48;
		solidArea.width = size - 48*2;
		solidArea.height = size - 48;
		solidAreaDefaultX = solidArea.x;
		solidAreaDefaultY = solidArea.y;
		attackArea.width = 170;
		attackArea.height = 170;
		motion1_duration = 25;
		motion2_duration = 50;
		
		
		getImage();
		getAttackImage();
		setDialogue();
	}
	
	public void getImage() {
		
		int i = 5;
		
		if(inRage == false) {
			up1 = setUp("/monster/skeletonlord_up_1", gp.tileSize*i, gp.tileSize*i);
			up2 = setUp("/monster/skeletonlord_up_2", gp.tileSize*i, gp.tileSize*i);
			down1 = setUp("/monster/skeletonlord_down_1", gp.tileSize*i, gp.tileSize*i);
			down2 = setUp("/monster/skeletonlord_down_2", gp.tileSize*i, gp.tileSize*i);
			left1 = setUp("/monster/skeletonlord_left_1", gp.tileSize*i, gp.tileSize*i);
			left2 = setUp("/monster/skeletonlord_left_2", gp.tileSize*i, gp.tileSize*i);
			right1 = setUp("/monster/skeletonlord_right_1", gp.tileSize*i, gp.tileSize*i);
			right2 = setUp("/monster/skeletonlord_right_2", gp.tileSize*i, gp.tileSize*i);	
		}
		else if(inRage == true) {
			up1 = setUp("/monster/skeletonlord_phase2_up_1", gp.tileSize*i, gp.tileSize*i);
			up2 = setUp("/monster/skeletonlord_phase2_up_2", gp.tileSize*i, gp.tileSize*i);
			down1 = setUp("/monster/skeletonlord_phase2_down_1", gp.tileSize*i, gp.tileSize*i);
			down2 = setUp("/monster/skeletonlord_phase2_down_2", gp.tileSize*i, gp.tileSize*i);
			left1 = setUp("/monster/skeletonlord_phase2_left_1", gp.tileSize*i, gp.tileSize*i);
			left2 = setUp("/monster/skeletonlord_phase2_left_2", gp.tileSize*i, gp.tileSize*i);
			right1 = setUp("/monster/skeletonlord_phase2_right_1", gp.tileSize*i, gp.tileSize*i);
			right2 = setUp("/monster/skeletonlord_phase2_right_2", gp.tileSize*i, gp.tileSize*i);
			
		}
		
	}
	
	public void getAttackImage() {
		
		int i = 5;
		
		if(inRage == false) {
			attackUp1 = setUp("/monster/skeletonlord_attack_up_1", gp.tileSize*i, gp.tileSize*i*2);
			attackUp2 = setUp("/monster/skeletonlord_attack_up_2", gp.tileSize*i, gp.tileSize*i*2);
			attackdown1 = setUp("/monster/skeletonlord_attack_down_1", gp.tileSize*i, gp.tileSize*i*2);
			attackdown2 = setUp("/monster/skeletonlord_attack_down_2", gp.tileSize*i, gp.tileSize*i*2);
			attackleft1 = setUp("/monster/skeletonlord_attack_left_1", gp.tileSize*i*2, gp.tileSize*i);
			attackleft2 = setUp("/monster/skeletonlord_attack_left_2", gp.tileSize*i*2, gp.tileSize*i);
			attackright1 = setUp("/monster/skeletonlord_attack_right_1", gp.tileSize*i*2, gp.tileSize*i);
			attackright2 = setUp("/monster/skeletonlord_attack_right_2", gp.tileSize*i*2, gp.tileSize*i);
		}
		else if(inRage == true) {
			attackUp1 = setUp("/monster/skeletonlord_phase2_attack_up_1", gp.tileSize*i, gp.tileSize*i*2);
			attackUp2 = setUp("/monster/skeletonlord_phase2_attack_up_2", gp.tileSize*i, gp.tileSize*i*2);
			attackdown1 = setUp("/monster/skeletonlord_phase2_attack_down_1", gp.tileSize*i, gp.tileSize*i*2);
			attackdown2 = setUp("/monster/skeletonlord_phase2_attack_down_2", gp.tileSize*i, gp.tileSize*i*2);
			attackleft1 = setUp("/monster/skeletonlord_phase2_attack_left_1", gp.tileSize*i*2, gp.tileSize*i);
			attackleft2 = setUp("/monster/skeletonlord_phase2_attack_left_2", gp.tileSize*i*2, gp.tileSize*i);
			attackright1 = setUp("/monster/skeletonlord_phase2_attack_right_1", gp.tileSize*i*2, gp.tileSize*i);
			attackright2 = setUp("/monster/skeletonlord_phase2_attack_right_2", gp.tileSize*i*2, gp.tileSize*i);
		}
		
	}
	
	public void update() {
		super.update();
		
	}
	
	public void setAction() {
	
		if(inRage == false && life < maxLife/2) {
			inRage = true;
			getImage();
			getAttackImage();
			
			defaultSpeed++;
			speed = defaultSpeed;
			attack *= 2;
		}
		
		if(getTileDistance(gp.player) < 10) {
			moveTowardsPlayer(60);
		}
		else {
			// Monster's AI for it's random movements
			getRandomDirection(120);
		}

		// check if it attacks
		if(attacking == false){
			checkAttackOrNot(60, gp.tileSize*7, gp.tileSize*5);
		}
	}
	
	public void damageReaction() {
		actionLockCounter = 0;
	}
	
	public void checkDrop() {
		
		gp.bossBattleOn = false;
		Progress.skeletonLordDefeated = true;

		gp.stopMusic();
		gp.playMusic(19);
		

		// Remove the iron Door
		for(int i=0; i<gp.obj[0].length; i++){
			if(gp.obj[gp.currentMap][i] != null && gp.obj[gp.currentMap][i].name.equals(Obj_Door_Iron.objName)){
				gp.obj[gp.currentMap][i] = null;
				gp.playSE(21);
			}
		}


	}

	public void setDialogue(){
		dialogues[0][0] = "LOL ...let's see who would dare to steal my treasure!";
		dialogues[0][1] = "You foolish mortals!";
		dialogues[0][2] = "I'm the lord of the undead!";
		dialogues[0][3] = "WELCOME TO YOUR DEATH!";

	}

}
