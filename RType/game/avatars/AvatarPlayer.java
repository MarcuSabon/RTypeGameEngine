package avatars;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.AffineTransform;
import java.io.IOException;

import javax.imageio.ImageIO;

import engine.model.Entity;
import engine.view.Avatar;
import engine.view.View;

public class AvatarPlayer extends Avatar {
	public Color color;
	private Image image;

	public AvatarPlayer(View v, Entity e) {
		super(v, e);
		try {
			image = ImageIO.read(getClass().getResourceAsStream("/BC.png"));
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	@Override
	public void render(Graphics2D g) {
		double posY = e.y();
		double posX = e.x();
		double d = e.orientation();

		double rot = Math.toRadians(d);
		Color oldColor = g.getColor();
		AffineTransform saved = g.getTransform();
		g.translate(posX * v.pxPerMeter(), posY * v.pxPerMeter());
		g.rotate(rot);

		int size = (int) (v.pxPerMeter() * 1.5);
		g.drawImage(image, -size / 2, -size / 2, size, size, null);
		g.setTransform(saved);
	}

}
