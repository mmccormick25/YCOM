package main;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

public abstract class TileObject {
	String cover;
	int row;
	int col;
	int x;
	int y;
	int tileSize = GamePanel.tileSize;
	BufferedImage texture;
	boolean destructible;
	
	public abstract void draw(Graphics2D g2);
}
