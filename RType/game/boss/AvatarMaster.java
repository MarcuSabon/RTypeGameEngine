package boss;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import bot.BossBot;
import engine.model.Entity;
import engine.model.Stunt.Action;
import engine.view.Avatar;
import game.GameManager;
import game.View0;

public class AvatarMaster extends Avatar {
	private String filepath;
	private BufferedImage image;
	private int framePerImage = 10;
	private View0 v;
	private Master master;

	public AvatarMaster(View0 v, Entity e, String filepath) {
		super(v, e);
		this.v = v;
		this.filepath = filepath;
		master = (Master) e;
		loadImage();
		loadSpecific();
	}

	private void loadImage() {
		try {
			image = ImageIO.read(getClass().getResource(filepath));
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	@Override
	public void render(Graphics2D g) {
		drawPNJCell(g);
		specificPrint(g);
	}

	double x, y;

	private int explosionframes = 1;
	private int flammeframes = 0;

	private void drawPNJCell(Graphics2D g) {
		if (image == null) {
			return;
		}
		int cellWidth = v.pxPerMeter();
		Action a = e.stunt.action();
		if (a != null) {
			if (a.kind() == 0) {
				StuntMaster.Motion motion = (StuntMaster.Motion) a;
				double progress = e.stunt.progress();
				double actionOffsetX = motion.ncols * cellWidth * progress;
				double actionOffsetY = motion.nrows * cellWidth * progress;
				x = (e.col() * cellWidth) + cellWidth / 2.0 + actionOffsetX - cellWidth * motion.ncols;
				y = (e.row() * cellWidth) + cellWidth / 2.0 + actionOffsetY - cellWidth * motion.nrows;
			} else if (a.kind() == 1) {
				StuntMaster.Spawn motion = (StuntMaster.Spawn) a;
				double progress = e.stunt.progress();
				double actionOffsetX = motion.ncols * cellWidth * progress;
				double actionOffsetY = motion.nrows * cellWidth * progress;
				x = (e.col() * cellWidth) + cellWidth / 2.0 + actionOffsetX - cellWidth * motion.ncols;
				y = (e.row() * cellWidth) + cellWidth / 2.0 + actionOffsetY - cellWidth * motion.nrows;
			} else {
				x = (e.col() * cellWidth) + cellWidth / 2.0;
				y = (e.row() * cellWidth) + cellWidth / 2.0;
			}
		} else {
			x = (e.col() * cellWidth) + cellWidth / 2.0;
			y = (e.row() * cellWidth) + cellWidth / 2.0;
		}

		AffineTransform transform = new AffineTransform();
		transform.translate(x, y);

		double scale = (double) cellWidth / Math.max(image.getWidth(), image.getHeight());
		transform.scale(scale, scale);

		transform.translate(-image.getWidth() / 2.0, -image.getHeight() / 2.0);
		BossBot b = (BossBot) e.bot;
		if (b.state != 4) {
			g.drawImage(image, transform, null);

		}
	}

	private void specificPrint(Graphics2D g) {
		int cellWidth = v.pxPerMeter();
		Entity[][] temp = master.body();
		BossBot b = (BossBot) master.bot;
		if (GameManager.lvl1) {
			if (!(((Master) e).body()[2][2].isDead()) && b.state != 4) {
				flammeframes = v.flamme(x + 0.15 * cellWidth, y + 2.8 * cellWidth, flammeframes, 1, 0);
			}
		}
		if (b.state == 4 && explosionframes > 0) {
			explosionframes = v.explosion(x, y, explosionframes, 50, 0);
		}
	}

	private void loadSpecific() {
		//
	}
}
