package object;

import java.awt.Color;

import entity.Entity;
import entity.Projectile;
import main.GamePanel;

public class Obj_Rock extends Projectile{

	
	GamePanel gp;
	public static final String objName = "Rock";
	
	
	public Obj_Rock(GamePanel gp) {
		super(gp);
		
		this.gp = gp;
		
		name = objName;
		speed = 8;
		maxLife = 80;
		life = maxLife;
		attack = 2;
		useCost = 1;
		alive = false;
		getImage();
	}
	
	public void getImage() {
		
		up1 = setUp("/projectile/rock_down_1", gp.tileSize, gp.tileSize);
		up2 = setUp("/projectile/rock_down_1", gp.tileSize, gp.tileSize);
		down1 = setUp("/projectile/rock_down_1", gp.tileSize, gp.tileSize);
		down2 = setUp("/projectile/rock_down_1", gp.tileSize, gp.tileSize);
		left1 = setUp("/projectile/rock_down_1", gp.tileSize, gp.tileSize);
		left2 = setUp("/projectile/rock_down_1", gp.tileSize, gp.tileSize);
		right1 = setUp("/projectile/rock_down_1", gp.tileSize, gp.tileSize);
		right2 = setUp("/projectile/rock_down_1", gp.tileSize, gp.tileSize);
	}

	
	// Not have any use  in game yet but will add it soon so for now hence monster can shoot any number of rocks
	
	public boolean haveResource(Entity user) {
		if(user.ammo >= useCost) return true;
		return false;
	}
	
	public void subtractResource(Entity user) {
		user.ammo -= useCost;
	}
	
	public Color getParticleColor() {
		Color color = new Color(40, 50, 0);
		return color;
	}
	
	public int getParticleSpeed() {
		int speed = 1;
		return speed;
	}
	
	public int getParticleSize() {
		int size = 10; // 10 pixels
		return size;
	}
	
	public int getParticleMaxLife() {
		int maxLife = 20;
		return maxLife;
	}
}
