package avatars;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.AffineTransform;
import java.io.IOException;

import javax.imageio.ImageIO;

import engine.model.Entity;
import engine.view.Avatar;
import engine.view.View;

public class AvatarWall extends Avatar {
	private Image image;

	public AvatarWall(View v, Entity e) {
		super(v, e);
		try {
			image = ImageIO.read(getClass().getResourceAsStream("/Maps/WallMap1.png"));
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}

	@Override
	public void render(Graphics2D g) {
		double posX = e.col() + 0.5;
		double posY = e.row() + 0.5;

		AffineTransform saved = g.getTransform();

		g.translate(posX * v.pxPerMeter(), posY * v.pxPerMeter());

		int size = (int) (v.pxPerMeter());
		g.drawImage(image, -size / 2, -size / 2, size, size, null);

		g.setTransform(saved);
	}
}
