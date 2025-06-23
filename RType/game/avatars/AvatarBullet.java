package avatars;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import engine.model.Entity;
import engine.view.Avatar;
import entities.Bullet;
import game.View0;

public class AvatarBullet extends Avatar {
	private BufferedImage image = null;
	private double laserX, laserY;
	private View0 v;
	private Bullet bullet;

	public AvatarBullet(View0 v, Entity e) {
		super(v, e);
		this.v = v;
		bullet = (Bullet) e;
	}

	private void loadImage() {
		try {
			image = ImageIO.read(getClass().getResourceAsStream("/LaserShot/BlueLaser.png"));
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	double zoom;
	private int laserframes = 1;
	private int explosionframes = 1;

	@Override
	public void render(Graphics2D g) {
		if (image == null) {
			loadImage();
		}

		int cellWidth = v.pxPerMeter();

		AffineTransform transform = new AffineTransform();
		transform.translate(e.x() * cellWidth, e.y() * cellWidth);

		double scale = (double) cellWidth / Math.max(image.getWidth(), image.getHeight());
		transform.scale(scale, scale);
		double rad = Math.toRadians(e.orientation());
		transform.rotate(rad);
		transform.translate(-image.getWidth() / 2.0, -image.getHeight() / 2.0);

		if (!bullet.collided) {
			g.drawImage(image, transform, null);
		} else {
			explosionframes = (v.explosion(e.x() * cellWidth, e.y() * cellWidth, explosionframes, 1, 0));
		}
		if (laserframes > 0) {
			if (e.orientation() == 0) {
				laserframes = (v.laser((laserX + 1) * cellWidth, (laserY + 1) * cellWidth, laserframes, 1,
						e.orientation() - 180)); // j'ai faite un ptit oupsi et j'ai fait le sprite a l'envers
			} else {
				laserframes = (v.laser((laserX) * cellWidth, (laserY) * cellWidth, laserframes, 1,
						e.orientation() - 180)); // j'ai faite un ptit oupsi et j'ai fait le sprite a l'envers
			}
		}
	}
}
