package main;

public class Shotgun extends Gun {
	Shotgun() {
		this.textureName = "/textures/shotgun.png";
		this.accuracyOverDistance = 0.7;
		this.damage = new int[]{3, 3, 4, 4, 5};
		this.damageRange = new int[]{3, 5};
	}
}
