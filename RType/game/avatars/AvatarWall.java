package avatars;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.AffineTransform;
import java.io.IOException;
import java.util.Random;

import javax.imageio.ImageIO;

import engine.model.Entity;
import engine.model.Stunt.Action;
import engine.view.Avatar;
import engine.view.View;
import stunts.StuntWall;

public class AvatarWall extends Avatar {
	private Image image;

	public AvatarWall(View v, Entity e) {
		super(v, e);
		try {
			Random rand = new Random();
			int n = rand.nextInt(3) + 1;
			String imagePath = "/Map1/Wall" + n + "Map1.png";
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
		Action a = e.stunt.action();

		double x, y;
		if (a != null && a.kind() == 0) {
			StuntWall.Motion motion = (StuntWall.Motion) a;
			double progress = (e.stunt.progress());
			double actionOffsetX = motion.getC() * cellWidth * progress / 2;
			double actionOffsetY = motion.getR() * cellWidth * progress / 2;
			x = (e.col() * cellWidth) + cellWidth / 2.0 + actionOffsetX - cellWidth * motion.getC();
			y = (e.row() * cellWidth) + cellWidth / 2.0 + actionOffsetY - cellWidth * motion.getR();
		} else {
			x = (e.col() * cellWidth) + cellWidth / 2.0;
			y = (e.row() * cellWidth) + cellWidth / 2.0;
		}

		AffineTransform transform = new AffineTransform();
		transform.translate(x, y);

		double scale = (double) cellWidth / Math.max(image.getWidth(null), image.getHeight(null));
		transform.scale(scale, scale);

		transform.translate(-image.getWidth(null) / 2.0, -image.getHeight(null) / 2.0);

		g.drawImage(image, transform, null);
	}
}
