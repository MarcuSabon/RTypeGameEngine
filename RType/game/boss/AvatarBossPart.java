package boss;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import bot.BossBot;
import bot.BossPartBot;
import engine.model.Entity;
import engine.model.Stunt.Action;
import engine.view.Avatar;
import game.View0;

public class AvatarBossPart extends Avatar {
	String filepath;
	private BufferedImage image;
	private View0 v;
	private BossPart bp;

	public AvatarBossPart(View0 v, Entity e, String filepath) {
		super(v, e);
		this.v = v;
		this.filepath = filepath;
		bp = (BossPart) e;
		loadImage();
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
		Entity master = bp.master;
		BossBot b = (BossBot) master.bot;
		if (!(b.state == 4)) {
			drawPNJCell(g);
		}
	}

	private int explosionframes = 1;

	private void drawPNJCell(Graphics2D g) {
		if (image == null) {
			return;
		}

		int cellWidth = v.pxPerMeter();
		Action a = bp.stunt.action();

		double x, y;
		if (a != null) {
			if (a.kind() == 0) {
				StuntBossPart.Motion motion = (StuntBossPart.Motion) a;
				double progress = (bp.master.stunt.progress());
				double actionOffsetX = motion.getC() * cellWidth * progress;
				double actionOffsetY = motion.getR() * cellWidth * progress;
				x = (e.col() * cellWidth) + cellWidth / 2.0 + actionOffsetX - cellWidth * motion.getC();
				y = (e.row() * cellWidth) + cellWidth / 2.0 + actionOffsetY - cellWidth * motion.getR();
			} else if (a.kind() == 1) {
				StuntBossPart.Spawn motion = (StuntBossPart.Spawn) a;
				double progress = (bp.master.stunt.progress());
				double actionOffsetX = motion.getC() * cellWidth * progress;
				double actionOffsetY = motion.getR() * cellWidth * progress;
				x = (e.col() * cellWidth) + cellWidth / 2.0 + actionOffsetX - cellWidth * motion.getC();
				y = (e.row() * cellWidth) + cellWidth / 2.0 + actionOffsetY - cellWidth * motion.getR();
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

		g.drawImage(image, transform, null);

		BossPartBot b = (BossPartBot) bp.bot;
		if (b.state == 4 && explosionframes > 0) {
			explosionframes = (v.explosion(x, y, explosionframes, 5, 0));
		}

	}

}
