package game;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import avatars.AvatarBullet;
import avatars.AvatarMissile;
import avatars.AvatarPNJ;
import avatars.AvatarPlayer;
import avatars.AvatarShooter;
import avatars.AvatarTower;
import avatars.AvatarTracker;
import avatars.AvatarWall;
import boss.AvatarBossPart;
import boss.AvatarMaster;
import boss.BossPart;
import boss.Master;
import engine.IModel;
import engine.model.Entity;
import engine.view.Avatar;
import engine.view.View;
import entities.BasicPNJ;
import entities.Bullet;
import entities.Missile;
import entities.Player;
import entities.Shooter;
import entities.Tower;
import entities.Tracker;
import map.Wall;
import oop.graphics.Canvas;

public class View0 extends View {
	private ScrollingBackground background;
	private ViewBar viewBar;
	Graphics2D g;

	private Animator animExplo;
	private Animator anim;
	private Animator animLaser;

	public View0(Canvas canvas, IModel model) {
		super(canvas, model);
		background = new ScrollingBackground(canvas, "/space.png");
		viewBar = new ViewBar(canvas, "/retroGaming.ttf");
		animExplo = new Animator("/Explosion", 47, 1);
		anim = new Animator("/Flame", 3, 10);
		animLaser = new Animator("/LaserShot", 16, 1);
	}

	@Override
	public void subPaint(Canvas canvas, Graphics2D g) {
		this.g = g;
		background.draw(g);
		Player p = m_model.player();
		viewBar.draw(g, p);
		g.scale(zoom, zoom);

		for (Avatar a : m_visibleAvatars)
			a.render(g);
	}

	@Override
	public void subtick(int elapsed) {
		background.update(elapsed);
	}

	@Override
	public void birth(Entity e) {
		Avatar a = null;
		if (e instanceof Player)
			a = new AvatarPlayer(this, e);
		else if (e instanceof Bullet)
			a = new AvatarBullet(this, e);
		else if (e instanceof Tracker)
			a = new AvatarTracker(this, e);
		else if (e instanceof Shooter)
			a = new AvatarShooter(this, e);
		else if (e instanceof BasicPNJ)
			a = new AvatarPNJ(this, e);
		else if (e instanceof Master)
			a = new AvatarMaster(this, e, e.sprite());
		else if (e instanceof BossPart)
			a = new AvatarBossPart(this, e, e.sprite());
		else if (e instanceof Missile)
			a = new AvatarMissile(this, e);
		else if (e instanceof Wall)
			a = new AvatarWall(this, e);
		else if (e instanceof Tower)
			a = new AvatarTower(this, e);

		if (a != null)
			m_visibleAvatars.add(a);
		else
			System.err.println("Avatar not found for entity: " + e.getClass().getSimpleName());
	}

	public int explosion(double x, double y, int frame, double size, int orientation) {
		return animExplo.print(g, x, y, frame, size, orientation);
	}

	public int flamme(double x, double y, int frame, double size, int orientation) {
		return anim.print(g, x, y, frame, size, orientation);
	}

	public int laser(double x, double y, int frame, double size, int orientation) {
		return animLaser.print(g, x, y, frame, size, orientation);
	}

	public class Animator {
		private BufferedImage[] images;
		private int actualFrame = 0;
		private int delay;

		public Animator(String folderpath, int nbframes, int delay) {
			this.delay = delay;
			images = new BufferedImage[nbframes];
			for (int i = 0; i < nbframes; i++) {
				try {
					images[i] = ImageIO.read(getClass().getResource(folderpath + "/" + i + ".png"));
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		public BufferedImage actualImage() {
			return images[actualFrame];
		}

		int Frames = delay;

		public int print(Graphics2D g, double posX, double posY, int frame, double size, double orientation) {
			int cellWidth = pxPerMeter();
			BufferedImage image = images[frame];

			AffineTransform transform = new AffineTransform();
			transform.translate(posX, posY);

			double scale = (double) cellWidth / Math.max(image.getWidth(), image.getHeight());
			scale *= size;
			transform.scale(scale, scale);

			transform.translate(-image.getWidth() / 2.0, -image.getHeight() / 2.0);

			double rad = Math.toRadians(orientation);
			transform.rotate(rad);

			g.drawImage(image, transform, null);
			if (frame >= images.length - 1) {
				return 0;
			} else {
				return frame + 1;
			}
		}
	}
}
