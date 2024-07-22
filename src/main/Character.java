package main;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Character {
	int row;
	int col;
	
	int x;
	int y;
	
	int chanceToHit;
	
	int movement;
	int actions;
	int maxActions;
	
	int maxHealth;
	int health;
	
	int healthBarHeight = 6;
	
	Gun gun;
	
	int tileSize = GamePanel.tileSize;
	
	public int getRow() {
		return this.row;
	}
	
	public int getCol() {
		return this.col;
	}
	
	public void setRow(int r) {
		this.row = r;
		this.x = r * tileSize;
	}
	
	public void setCol(int c) {
		this.col = c;
		this.y = c * tileSize;
	}

	public void setSelected(boolean b) {
		// TODO Auto-generated method stub
	}

	public void draw(Graphics2D g2) {
		// TODO Auto-generated method stub
		
	}
	
	public void drawHealth(Graphics2D g2, Color c, int barWidth) {
		int healthRectWidth = barWidth/maxHealth;
		Stroke stroke = new BasicStroke(2f);
		g2.setStroke(stroke);
		
		for (int n = 0; n < maxHealth; n++) {
			if (n+1 <= health) {
				g2.setColor(c);
			} else {
				g2.setColor(Color.gray);
			}
			g2.fillRect(x + (n * healthRectWidth), y - healthBarHeight - 3, healthRectWidth, healthBarHeight);
		}
		
		g2.setColor(Color.black);
		for (int n = 0; n < maxHealth; n++) {
			g2.drawRect(x + (n * healthRectWidth), y - healthBarHeight - 3, healthRectWidth, healthBarHeight);
		}
		
		
	}

	public void setExhausted(boolean b) {
		// TODO Auto-generated method stub
		
	}
}
