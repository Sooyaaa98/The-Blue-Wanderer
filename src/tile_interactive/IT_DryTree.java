package tile_interactive;

import java.awt.Color;

import entity.Entity;
import main.GamePanel;

public class IT_DryTree extends InteractiveTile{

	GamePanel gp;
	
	public IT_DryTree(GamePanel gp, int col, int row) {
		super(gp, col, row);
		this.gp = gp;
		this.worldX = gp.tileSize*col;
		this.worldY = gp.tileSize*row;
		
		down1 = setUp("/tiles_interactive/drytree", gp.tileSize, gp.tileSize);
		destructible = true;
		life = 1;
	}
	
	public boolean isCorrectItem(Entity entity) {
		boolean isCorrectItem = false;
		
		if(entity.currentWeapon.type == type_axe) {
			isCorrectItem = true;
		}
		
		return isCorrectItem;
	}
	
	public void playSE() {
		gp.playSE(11);
	}
	
	public InteractiveTile getDestroyedForm() {
		InteractiveTile destroyedForm = new IT_Trunk(gp, worldX/gp.tileSize, worldY/gp.tileSize);
		return destroyedForm ;
	}
	
	public Color getParticleColor() {
		Color color = new Color(60, 50, 30);
		return color;
	}
	
	public int getParticleSpeed() {
		int speed = 1;
		return speed;
	}
	
	public int getParticleSize() {
		int size = 6; // 6 pixels
		return size;
	}
	
	public int getParticleMaxLife() {
		int maxLife = 20;
		return maxLife;
	}
}
