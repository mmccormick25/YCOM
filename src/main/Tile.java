package main;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Stroke;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import javax.imageio.ImageIO;

public class Tile {

	int row;
	int col;
	int tileSize = GamePanel.tileSize;
	int x;
	int y;
	boolean highlight = false;
	TileObject tileObject;
	Set<Wall> adjWalls = new HashSet<>();
	
	BufferedImage ogImg;
	BufferedImage hImg;
	
	public Tile(int row, int col) {
		this.row = row;
		this.col = col;
		x = row * tileSize;
		y = col * tileSize;
		try{
			ogImg = ImageIO.read(getClass().getResource("/textures/grassfloor.png"));
			hImg = ImageIO.read(getClass().getResource("/textures/grassfloorhighlighted.png"));
			
		} catch (IOException e){e.printStackTrace(); }
		   catch(Exception e){e.printStackTrace();}
	}
	
	public void draw(Graphics2D g2) {
		
		if (!highlight) {
			// Draw base texture
			g2.drawImage(ogImg, x, y, null);
		} else {
			// Draw highlighted texture
			g2.drawImage(hImg, x, y, null);
		}
		
	}
	
	public int getRow() {
		return this.row;
	}
	
	public int getCol() {
		return this.col;
	}
	
	public void setHighlight(boolean h) {
		this.highlight = h;
	}
	
	public void addWall(Wall w) {
		adjWalls.add(w);
	}
	
	
}
