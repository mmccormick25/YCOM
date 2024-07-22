package main;
import java.util.stream.IntStream; 

public class Rifle extends Gun {
	Rifle() {
		this.textureName = "/textures/rifle.png";
		this.accuracyOverDistance = 0.8;
		this.damage = new int[]{3, 3, 3, 4, 4};
		this.damageRange = new int[]{3, 4};
	}
}
