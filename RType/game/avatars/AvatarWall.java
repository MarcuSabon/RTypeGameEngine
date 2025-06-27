package avatars;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.AffineTransform;
import java.io.IOException;
import java.util.Random;

import javax.imageio.ImageIO;

import engine.model.Entity;
import engine.view.Avatar;
import engine.view.View;
import game.GameManager;
import map.Synchronyser;

public class AvatarWall extends Avatar {
	private Image image;

	public AvatarWall(View v, Entity e) {
		super(v, e);
		try {
			Random rand = new Random();
			int n = rand.nextInt(3) + 1;
			String imagePath;
			if (GameManager.lvl1)
				imagePath = "/Map1/Wall" + n + "Map1.png";
			else
				imagePath = "/Map2/buisson" + n + ".png";
			image = ImageIO.read(getClass().getResourceAsStream(imagePath));
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}

	@Override
	public void render(Graphics2D g) {
		if (image == null) {
			return;
		}

		int cellWidth = v.pxPerMeter();
		

		double x, y;

		x = (e.col() * cellWidth) + cellWidth / 2.0;
		y = (e.row() * cellWidth) + cellWidth / 2.0;

		int CellWidth = v.pxPerMeter();
		x = (int) Synchronyser.synchronise(x, CellWidth);

		AffineTransform transform = new AffineTransform();
		transform.translate(x, y);

		double scale = (double) cellWidth / Math.max(image.getWidth(null), image.getHeight(null));
		transform.scale(scale, scale);

		transform.translate(-image.getWidth(null) / 2.0, -image.getHeight(null) / 2.0);

		g.drawImage(image, transform, null);
	}
}
