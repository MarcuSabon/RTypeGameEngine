package avatars;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import engine.model.Entity;
import engine.model.Stunt.Action;
import engine.view.Avatar;
import engine.view.View;
import stunts.StuntPNJ;

public class AvatarPNJ extends Avatar {

	String filepath;
	private BufferedImage image;

	public AvatarPNJ(View v, Entity e) {
		super(v, e);
		loadImage();

	}

	@Override
	public void render(Graphics2D g) {
		drawPNJCell(g);
		// You can add more rendering logic here if needed
		// For example, drawing additional information or effects related to the PNJ
		// g.drawString("PNJ Name: " + e.getName(), x, y - 10); // Example text
	}

	// ------------- Private methods -------------//
	private void loadImage() {
		try {
			image = ImageIO.read(getClass().getResource("/lirililalila.png"));
		} catch (IOException ex) {
			ex.printStackTrace();
		}

	}

	private void drawPNJCell(Graphics2D g) {
		double posX = e.col() + 0.5;
		double posY = e.row() + 0.5;

		Action action = e.stunt.action();

		if (action != null && action.kind() == 0) {
			StuntPNJ.PNJMotion motion = (StuntPNJ.PNJMotion) action;
			double progress = e.stunt.progress();
			double nr = motion.getR();
			double nc = motion.getC();

			if (progress <= 0.5) {
				posX += nc * progress * 2;
				posY += nr * progress * 2;
			}
		}

		AffineTransform savedTransform = g.getTransform();

		g.translate(posX * v.pxPerMeter(), posY * v.pxPerMeter());

		int size = v.pxPerMeter();
		g.drawImage(image, -size / 2, -size / 2, size, size, null);

		g.setTransform(savedTransform);
	}

}
