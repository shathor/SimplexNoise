package ch.project.inter;

import java.io.File;

public class Starter {
	
	/**
	 * Creates a noise image in root directory.
	 * 
	 * @param args not needed
	 */
	public static void main(String[] args) {
		NoiseImage noiseImage = new NoiseImage(1024, 1024);
		noiseImage.serialize(new File("noise.png"), "png");
	}
	
}
