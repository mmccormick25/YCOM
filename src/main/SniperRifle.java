package main;

import java.util.stream.IntStream;

public class SniperRifle extends Gun {
	SniperRifle() {
		this.textureName = "/textures/sniperrifle.png";
		this.accuracyOverDistance = 0.95;
		this.damage = new int[]{3, 3, 4, 4, 4};
		this.damageRange = new int[]{4, 5};
	}
}
