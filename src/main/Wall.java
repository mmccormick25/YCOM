package main;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Wall {
	int row;
	int col;
	int x;
	int y;
	int tileSize = GamePanel.tileSize;
	String orientation;
	BufferedImage texture;
	
	Wall(int row, int col, String orientation) {
		this.row = row;
		this.col = col;
		this.orientation = orientation;
		
		this.x = row * tileSize;
		this.y = col * tileSize;
		
		try{
			if(orientation == "vertical") {
				this.texture = ImageIO.read(getClass().getResource("/textures/vertwall.png"));
			} else {
				this.texture = ImageIO.read(getClass().getResource("/textures/horzwall.png"));
			}
		} catch (IOException e){e.printStackTrace(); }
		   catch(Exception e){e.printStackTrace();}
	}
	
	public void draw(Graphics2D g2) {
		
		if(orientation == "vertical") {
			g2.drawImage(texture, x - tileSize/2, y, null);
		} else {
			g2.drawImage(texture, x, y - tileSize/2, null);
		}
	
	}
}
 