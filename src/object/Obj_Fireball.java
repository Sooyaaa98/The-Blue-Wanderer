package object;

import java.awt.Color;

import entity.Entity;
import entity.Projectile;
import main.GamePanel;

public class Obj_Fireball extends Projectile{

	GamePanel gp;
	public static final String objName = "FireBall";
	
	public Obj_Fireball(GamePanel gp) {
		super(gp);
		
		this.gp = gp;
		
		name = objName;
		speed = 5;
		maxLife = 80;
		life = maxLife;
		attack = 1;
		useCost = 1;
		alive = false;
		knockBackPower = 0;
		getImage();
	}
	
	public void getImage() {
		
		up1 = setUp("/projectile/fireball_up_1", gp.tileSize, gp.tileSize);
		up2 = setUp("/projectile/fireball_up_2", gp.tileSize, gp.tileSize);
		down1 = setUp("/projectile/fireball_down_1", gp.tileSize, gp.tileSize);
		down2 = setUp("/projectile/fireball_down_2", gp.tileSize, gp.tileSize);
		left1 = setUp("/projectile/fireball_left_1", gp.tileSize, gp.tileSize);
		left2 = setUp("/projectile/fireball_left_2", gp.tileSize, gp.tileSize);
		right1 = setUp("/projectile/fireball_right_1", gp.tileSize, gp.tileSize);
		right2 = setUp("/projectile/fireball_right_2", gp.tileSize, gp.tileSize);
	}
	
	public boolean haveResource(Entity user) {
		if(user.mana >= useCost) return true;
		return false;
	}
	
	public void subtractResource(Entity user) {
		user.mana -= useCost;
	}
	
	public Color getParticleColor() {
		Color color = new Color(240, 50, 0);
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
