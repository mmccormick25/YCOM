package main;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

public class PlayerSoldier extends Character {

	boolean selected;
	boolean exhausted;
	
	BufferedImage ogImg;
	BufferedImage sImg;
	BufferedImage eImg;
	
	BufferedImage gunImg;
	
	public PlayerSoldier(int row, int col, int movement, Gun gun, int maxHealth, int maxActions) {
		this.row = row;
		this.col = col;
		x = row * tileSize;
		y = col * tileSize;
		this.gun = gun;
		this.movement = movement;
		this.maxActions = maxActions;
		this.actions = maxActions;
		this.maxHealth = maxHealth;
		this.health = maxHealth;
		this.exhausted = false;
		
		try{
			ogImg = ImageIO.read(getClass().getResource("/textures/playersoldier.png"));
			sImg = ImageIO.read(getClass().getResource("/textures/playersoldierselect.png"));
			eImg = ImageIO.read(getClass().getResource("/textures/playersoldierexhausted.png"));
			
			gunImg = ImageIO.read(getClass().getResource(gun.textureName));
			
		} catch (IOException e){e.printStackTrace(); }
		   catch(Exception e){e.printStackTrace();}
		
	}
	
	public void draw(Graphics2D g2) {
		
		if (exhausted) {
			g2.drawImage(eImg, x,  y,  null);
		} else if (selected) {
			g2.drawImage(sImg, x, y, null);
		} else {
			g2.drawImage(ogImg, x, y, null);
		}
		
		g2.drawImage(gunImg, x, y, null);
		
		
	}
	
	
	public void setSelected(boolean s) {
		this.selected = s;
	}
	
	public void setExhausted(boolean e) {
		this.exhausted = e;
	}
	
}
