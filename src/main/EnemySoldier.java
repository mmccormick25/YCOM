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

public class EnemySoldier extends Character{

	BufferedImage ogImg;
	
	BufferedImage gunImg;
	
	public EnemySoldier(int row, int col, int movement, Gun gun, int maxHealth, int maxActions) {
		this.row = row;
		this.col = col;
		x = row * tileSize;
		y = col * tileSize;
		
		this.maxHealth = maxHealth;
		this.health = maxHealth;
		
		this.gun = gun;
		
		this.movement = movement;
		this.maxActions = maxActions;
		this.actions = maxActions;
		
		try{
			ogImg = ImageIO.read(getClass().getResource("/textures/enemysoldier.png"));
			
			gunImg = ImageIO.read(getClass().getResource(gun.textureName));
		} catch (IOException e){e.printStackTrace(); }
		   catch(Exception e){e.printStackTrace();}
		
	}
	
	public void draw(Graphics2D g2) {
		
		g2.drawImage(ogImg, x, y, null);
		g2.drawImage(gunImg, x, y, null);
	}
	
}
