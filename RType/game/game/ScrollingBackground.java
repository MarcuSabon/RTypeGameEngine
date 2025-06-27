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

	// ajout accès au zoom et à l'offset
	private View0 view0;

	public ScrollingBackground(Canvas canvas, String imagePath, View0 view) {
		this.canvas = canvas;
		this.view0 = view;
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
		int imgH = backgroundImage.getHeight(null);
		int screenW = canvas.getWidth();
		int screenH = canvas.getHeight();

		if (screenW != imgW || screenH != imgH)
			backgroundImage = backgroundImage.getScaledInstance(screenW, screenH, Image.SCALE_SMOOTH);

		double zoom = view0.getZoom();
		double viewportX = view0.oX();
		double viewportY = view0.oY();

		int bgOffsetX = (int) (-viewportX / zoom % imgW);
		int bgOffsetY = (int) (-viewportY / zoom % imgH);
		for (int i = -1; i < screenW / imgW + 2; i++) {
			for (int j = -1; j < screenH / imgH + 2; j++) {
				g.drawImage(backgroundImage, offsetX + bgOffsetX + i * imgW, bgOffsetY + j * imgH, imgW, imgH, null);
			}
		}
	}
}
