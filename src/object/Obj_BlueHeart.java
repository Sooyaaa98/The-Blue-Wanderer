package object;

import entity.Entity;
import main.GamePanel;

public class Obj_BlueHeart extends Entity{
    
    GamePanel gp;
    public static final String objName = "Blue Heart";

    public Obj_BlueHeart(GamePanel gp){
        super(gp);
        
        this.gp = gp;
        name = objName;
        type = type_pickupOnly;
        down1 = setUp("/objects/blueheart", gp.tileSize, gp.tileSize);

        setDialogue();
    }

    public void setDialogue(){

        dialogues[0][0] = "You found a beautiful blue gem.";
        dialogues[0][1] = "It is a legendary treasure!";
    }

    public boolean use(Entity entity){
        gp.gameState = gp.cutSceneState;
        gp.csManager.sceneNum = gp.csManager.ending;

        return true;
    }
}
