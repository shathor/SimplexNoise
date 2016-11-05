package ch.project.inter;

import java.awt.color.ColorSpace;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class NoiseImage {

	private int width = 128;
	private int height = 128;

	private BufferedImage bufferedImage;

	/**
	 * @return the image width
	 */
	public int getWidth() {
		return width;
	}

	/**
	 * @return the image height
	 */
	public int getHeight() {
		return height;
	}

	/**
	 * Processes the image to render simplex noise.
	 * 
	 * @param width the image width
	 * @param height the image height
	 */
	public NoiseImage(int width, int height) {
		this.width = width;
		this.height = height;
		this.bufferedImage = new BufferedImage(this.width, this.height, ColorSpace.TYPE_RGB);
		process();
	}

	private void process() {
		for (int x = 0; x < getWidth(); x++) {
			for (int y = 0; y < getHeight(); y++) {
				// Play with values here.
				// noiseValue operates between values 0.0 for black and 1.0 for white
				// you can disable one or more of the three filters
				double noiseValue = calcNoise(x, y, 30, 30, 20); // zoom in x, zoom in y, spread or "number of details"
				
				// filters
				noiseValue = positionFilter(x, y, noiseValue, 0, 0); // translate towards bottom, pow towards bottom
				noiseValue = contrastFilter(noiseValue, 1, 0.1); // contrast, brightness (between 0.0 and 1.0)
				noiseValue = rangeFilter(noiseValue, 0.7, 1.0); // range between 0.0 and 1.0, for black
				
				setPixel(x, y, noiseValue);
			}
		}
	}
	
	private double calcNoise(int x, int y, double scaleX, double scaleY, int octave) {
		return SimplexNoise.noise(x, y, 1 / scaleX, 1 / scaleY, 0.5, octave) / 2 + 0.5;
	}
	
	private double positionFilter(int x, int y, double noiseValue, int translateToBottom, int powerToBottom) {
		double temp = (y - translateToBottom) / (double) this.height;
		temp = Math.min(Math.max(temp, 0), 1);
		double powered = Math.pow(temp, powerToBottom);
		return noiseValue * powered;
	}
	
	private double contrastFilter(double noiseValue, int contrast, double brightness) {
		noiseValue += brightness;
		double factor = 259.0 / 255.0 * (contrast + 255.0) / (259.0 - contrast);
		noiseValue = factor * (noiseValue - 0.5) + 0.5;
		return Math.min(Math.max(noiseValue, 0), 1);
	}
	
	private double rangeFilter(double noiseValue, double rangeStart, double rangeEnd) {
		if(noiseValue >= rangeStart && noiseValue <= rangeEnd) {
			return 1.0;
		}
		else return 0.0;
	}

	private void setPixel(int x, int y, double noiseValue) {
		int scaled = (int) ((noiseValue) * 255);
		int rgb = 0xFF000000 | scaled << 16 | scaled << 8 | scaled;
		this.bufferedImage.setRGB(x, y, rgb);
	}

	
	/**
	 * Writes the image to file.
	 * 
	 * @param file File to write the image to
	 */
	public void serialize(File file, String formatName) {
		try {
			ImageIO.write(bufferedImage, formatName, file);
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}

}
