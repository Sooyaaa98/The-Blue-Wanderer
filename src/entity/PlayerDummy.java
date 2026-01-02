package entity;

import main.GamePanel;

public class PlayerDummy extends Entity{
    
    public final static  String npcName = "PlayerDummy";

    public PlayerDummy(GamePanel gp){
        super(gp);

		name = npcName;
        getImage();
    }

    public void getImage() {
			
		up1 = setUp("/Player/boy_up_1", gp.tileSize, gp.tileSize);
		up2 = setUp("/Player/boy_up_2", gp.tileSize, gp.tileSize);
		down1 = setUp("/Player/boy_down_1", gp.tileSize, gp.tileSize);
		down2 = setUp("/Player/boy_down_2", gp.tileSize, gp.tileSize);
		left1 = setUp("/Player/boy_left_1", gp.tileSize, gp.tileSize);
		left2 = setUp("/Player/boy_left_2", gp.tileSize, gp.tileSize);
		right1 = setUp("/Player/boy_right_1", gp.tileSize, gp.tileSize);
		 right2 = setUp("/Player/boy_right_2", gp.tileSize, gp.tileSize);
		
	}
}
