package main;

import java.awt.Graphics2D;
import java.io.IOException;

import javax.imageio.ImageIO;

public class LargeRock extends TileObject {

	LargeRock(int row, int col) {
		this.row = row;
		this.col = col;
		this.cover = "full";
		this.destructible = false;
		
		this.x = row * tileSize;
		this.y = col * tileSize;
		
		try{
			this.texture = ImageIO.read(getClass().getResource("/textures/largerock.png"));
		} catch (IOException e){e.printStackTrace(); }
		   catch(Exception e){e.printStackTrace();}
	}
	
	public void draw(Graphics2D g2) {
		
		g2.drawImage(texture, x, y, null);
	
	}
	
}


