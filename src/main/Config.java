package main;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class Config {
	GamePanel gp;
	
	public Config(GamePanel gp) {
		this.gp = gp;
	}
	
	public void saveConfig() {
		try {
			
			BufferedWriter bw = new BufferedWriter(new FileWriter("config.txt"));
			
			// FULL SCREEN 
			if(gp.fullScreenOn == true) {
				bw.write("On");
			}
			else if(gp.fullScreenOn == false) {
				bw.write("Off");
			}
			bw.newLine();
			
			// MUSIC CONTROLS 
			bw.write(String.valueOf(gp.music.volumeScale));
			bw.newLine();
			
			// SE controls 
			bw.write(String.valueOf(gp.se.volumeScale));
			bw.newLine();
			
			
			bw.close();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void loadConfig() {
		try {
			
			BufferedReader br = new BufferedReader(new FileReader("config.txt"));
			
			String s = br.readLine();
			
			// FULL SCREEN SETTINGS
			if(s.equals("On")) {
				gp.fullScreenOn = true;
			}
			else if(s.equals("Off")) {
				gp.fullScreenOn = false;
			}
			
			// Music SETTINGS 
			s = br.readLine();
			gp.music.volumeScale = Integer.parseInt(s);
			
			// SE SETTINGS
			s = br.readLine();
			gp.se.volumeScale = Integer.parseInt(s);
			
			br.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
	}
}
