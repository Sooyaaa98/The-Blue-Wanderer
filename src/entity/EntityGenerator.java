package entity;

import main.GamePanel;
import object.Obj_Axe;
import object.Obj_BlueHeart;
import object.Obj_Boots;
import object.Obj_Chest;
import object.Obj_Coin_Bronze;
import object.Obj_Door;
import object.Obj_Fireball;
import object.Obj_Heart;
import object.Obj_Key;
import object.Obj_Lantern;
import object.Obj_Mana;
import object.Obj_Potion_Red;
import object.Obj_Rock;
import object.Obj_Shield_Blue;
import object.Obj_Shield_Wood;
import object.Obj_Sword_Normal;
import object.Obj_Tent;
import object.Obj_Pickaxe;
import object.Obj_Door_Iron;

public class EntityGenerator {
	
	GamePanel gp;
	
	public EntityGenerator(GamePanel gp) {
		this.gp = gp;
	}
	
	public Entity getObject(String itemName) {
		Entity obj = null;
		
		switch(itemName) {
		case Obj_Axe.objName : obj = new Obj_Axe(gp); break;
		case Obj_BlueHeart.objName : obj = new Obj_BlueHeart(gp); break;
		case Obj_Boots.objName : obj = new Obj_Boots(gp); break;
		case Obj_Chest.objName : obj = new Obj_Chest(gp); break;
		case Obj_Coin_Bronze.objName : obj = new Obj_Coin_Bronze(gp);
		case Obj_Door_Iron.objName : obj = new Obj_Door_Iron(gp); break;
		case Obj_Door.objName : obj = new Obj_Door(gp); break;
		case Obj_Fireball.objName : obj = new Obj_Fireball(gp); break;
		case Obj_Heart.objName : obj = new Obj_Heart(gp); break;
		case Obj_Key.objName: obj = new Obj_Key(gp); break;
		case Obj_Lantern.objName: obj = new Obj_Lantern(gp); break;
		case Obj_Mana.objName: obj = new Obj_Mana(gp); break;
		case Obj_Pickaxe.objName: obj = new Obj_Pickaxe(gp); break;
		case Obj_Potion_Red.objName: obj = new Obj_Potion_Red(gp); break;
		case Obj_Rock.objName: obj = new Obj_Rock(gp); break;
		case Obj_Shield_Blue.objName: obj = new Obj_Shield_Blue(gp); break;
		case Obj_Shield_Wood.objName :  obj = new Obj_Shield_Wood(gp); break;
		case Obj_Sword_Normal.objName : obj = new Obj_Sword_Normal(gp); break;
		case Obj_Tent.objName : obj = new Obj_Tent(gp); break;
		}
		
		return obj;
	}
}
