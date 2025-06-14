package game;

import java.awt.Graphics2D;
import java.awt.Image;
import java.io.IOException;

import javax.imageio.ImageIO;

import oop.graphics.Canvas;

public class ScrollingBackground {

	private Image backgroundImage;
	private double offsetXFloat = 0.0;
	private int offsetX = 0;
	// Il faudra se caler avec la vitesse des entités obstacles
	private double speed = 50.0; // pixels par seconde
	private Canvas canvas;

	public ScrollingBackground(Canvas canvas, String imagePath) {
		this.canvas = canvas;
		try {
			backgroundImage = ImageIO.read(getClass().getResource(imagePath));
		} catch (IOException | IllegalArgumentException e) {
			System.err.println("Erreur de chargement de l'image de fond : " + e.getMessage());
		}
	}

	public void update(int ms) {
		if (backgroundImage != null) {
			offsetXFloat -= speed * ms / 500.0;
			int imgWidth = backgroundImage.getWidth(null);
			if (Math.abs(offsetXFloat) >= imgWidth) {
				offsetXFloat += imgWidth;
			}
			offsetX = (int) offsetXFloat;
		}
	}

	public void draw(Graphics2D g) {
		if (backgroundImage == null)
			return;

		int imgW = backgroundImage.getWidth(null);
		int screenW = canvas.getWidth();
		int screenH = canvas.getHeight();

		for (int i = -1; i < screenW / imgW + 2; i++) {
			g.drawImage(backgroundImage, offsetX + i * imgW, 0, imgW, screenH, null);
		}
	}
}
