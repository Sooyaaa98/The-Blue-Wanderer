package main;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;

import entity.PlayerDummy;
import monster.MON_SkeletonLord;
import object.Obj_BlueHeart;
import object.Obj_Door_Iron;

public class CutSceneManager {
    
    GamePanel gp;
    Graphics2D g2;
    public int sceneNum;
    public int scenePhase;
    int counter = 0;
    float alpha = 0f;
    int y;
    String endCredit;

    // Scene Number
    public final int NA = 0;
    public final int skeletonLord = 1;
    public final int ending  = 2;

    public CutSceneManager(GamePanel gp){
        this.gp = gp;

        endCredit = "A small 2D game made by me"
                    + "\n\n\n\n\n\n\n\n\n\n\n"
                    + "Special Thanks\n"
                    + " ''' Thanks for Playing'''' \n"
                    + "\n\n\n\n\n"
                    + "Well, that's it\n"
                    + " for now...\n"
                    + "\n\n\n\n\n"
                    + "See you next time!";
    }

    public void draw(Graphics2D g2){
        this.g2 = g2;

        switch(sceneNum){
            case skeletonLord : scene_skeletonLord(); break;
            case ending : scene_ending(); break;
        }
    }

    public void scene_skeletonLord(){
        
        if(scenePhase == 0){
            gp.bossBattleOn = true;

            // scan for vacant Place in gp.obj[][] for shutting the Iron Door
            for(int i=0; i<gp.obj[0].length; i++){
                if(gp.obj[gp.currentMap][i] == null){
                    gp.obj[gp.currentMap][i] = new Obj_Door_Iron(gp);
                    gp.obj[gp.currentMap][i].worldX = gp.tileSize*25;
                    gp.obj[gp.currentMap][i].worldY = gp.tileSize*28;
                    gp.obj[gp.currentMap][i].temp = true;
                    gp.playSE(21);
                    break;
                }
            }

            // Place Player Dummy for cutScene in the npcArray Slot
            for(int i=0; i<gp.npc[1].length; i++){
                if(gp.npc[gp.currentMap][i] == null){
                    gp.npc[gp.currentMap][i] = new PlayerDummy(gp);
                    gp.npc[gp.currentMap][i].worldX = gp.player.worldX;
                    gp.npc[gp.currentMap][i].worldY = gp.player.worldY;
                    gp.npc[gp.currentMap][i].direction = gp.player.direction;

                    break;
                }
            }
            gp.player.drawing = false;
            scenePhase++;
        }

        if(scenePhase == 1){ // move the camera towards the Boss
            gp.player.worldY -= 2;

            if(gp.player.worldY < gp.tileSize * 16){
                scenePhase++;
            }
        }

        if(scenePhase == 2){  // wakes up the boss
            for(int i=0; i<gp.monster[0].length; i++){
                if(gp.monster[gp.currentMap][i] != null && gp.monster[gp.currentMap][i].name.equals(MON_SkeletonLord.monName)){
                    gp.monster[gp.currentMap][i].sleep = false;
                    gp.ui.npc = gp.monster[gp.currentMap][i];
                    scenePhase++;
                    break;
                } 
            }
        }

        if(scenePhase == 3){
            gp.ui.drawDialogueState();
        }

        if(scenePhase == 4){
            // search the Dummy in npc array
            for(int i=0; i<gp.npc[1].length; i++){
                if(gp.npc[gp.currentMap][i] != null && gp.npc[gp.currentMap][i].name.equals(PlayerDummy.npcName)){
                    // Restore Player Position
                    gp.player.worldX = gp.npc[gp.currentMap][i].worldX;
                    gp.player.worldY = gp.npc[gp.currentMap][i].worldY;

                    gp.npc[gp.currentMap][i] = null;
                    break; 
                }
            }
            gp.player.drawing = true;

            // Reset Variables
            sceneNum = NA;
            scenePhase = 0;
            gp.gameState = gp.playState;

            gp.stopMusic();
            gp.playMusic(22);
        }   
    }

    public void scene_ending(){
        if(scenePhase == 0){
            gp.stopMusic();
            gp.ui.npc = new Obj_BlueHeart(gp);
            scenePhase++;
        }
        if(scenePhase == 1){
            // Display dialogues present in the Blue_Heart
            gp.ui.drawDialogueState();
        }
        if(scenePhase == 2){
            // Play the sound effects
            gp.playSE(4);
            scenePhase++;
        }
        if(scenePhase == 3){
            // wait until the sound effect ends
            if(counterReached(300) == true){ // 5 seconds = 300frames
                scenePhase++;
            }
        }
        if(scenePhase == 4){
            // draw the Black background
            alpha += 0.005f;
            if(alpha > 1f){
                alpha = 1f;
            }
            drawBlackBackground(alpha);
            if(alpha == 1f){
                alpha = 0f;
                scenePhase++;
            }
        }
        if(scenePhase == 5){
            // Display Message
            drawBlackBackground(1f);

            alpha += 0.005f;
            if(alpha > 1f){
                alpha = 1f;
            }

            String text = "After the fierce battle with the Skeleton Lord,\n"
                        + "The Blue Boy finally found his inner peace and the legendary Treasure.\n"
                        + "But this is not the end of his journey. \n"
                        + "The Blue Boy's adventure continues...\n";

            drawString(alpha, 28f, 200, text, 40);

            if(counterReached(600) == true){
                // ending music
                gp.playMusic(0);
                scenePhase++;
            }
                            
        }
        if(scenePhase == 6){
            drawBlackBackground(1f);
            drawString(1f, 80f, gp.screenHeight/2, "Blue Boy Adventure", 40);

            if(counterReached(480) == true){
                // gp.playMusic(0);
                scenePhase++;
            }
        }
        if(scenePhase == 7){ // EndCredits
             drawBlackBackground(1f);
             y = gp.screenHeight/2;
             drawString(1f, 28f, y, endCredit,  30);

             if(counterReached(480) == true){
                // gp.playMusic(0);
                scenePhase++;
            }
        }
        if(scenePhase == 8){
            drawBlackBackground(1f);
            
            // scrooling through the credits
            y--;
            drawString(1f, 28f, y, endCredit, 30);
            
            if(y < -900){
                scenePhase++;
            }
        }
        if(scenePhase == 9){
            drawBlackBackground(1f);
               alpha -= 0.005f;
                if(alpha < 0f){
                    alpha = 0f;
                    gp.stopMusic();
                    // Restart The Game for later
                }
            
        }

    }

    public boolean counterReached(int target){

        boolean counterReached = false;
        counter++;

        if(counter > target){
            counterReached = true;
            counter = 0;
        }
        return counterReached;
    }

    public void drawBlackBackground(float alpha){

        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
        g2.setColor(Color.black);
        g2.fillRect(0, 0, gp.screenWidth, gp.screenHeight);
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
    }

    public void drawString(float alpha, float fontSize, int y, String text, int lineHeight){
     
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
        g2.setColor(Color.white);
        g2.setFont(g2.getFont().deriveFont(fontSize));

        for(String line : text.split("\n")){
            int x = gp.ui.getXforCentredText(line);
            g2.drawString(line, x, y);
            y += lineHeight;
        }
         g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
    }
}
