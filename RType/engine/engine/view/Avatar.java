package engine.view;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import engine.model.Entity;

public abstract class Avatar {
	public View v;
	public Entity e;

	protected Avatar(View v, Entity e) {
		this.v = v;
		this.e = e;
		e.avatar = this;
	}

	public abstract void render(Graphics2D g);

	public static class Animator {
		private BufferedImage[] images;
		private int actualFrame = 0;

		public Animator(String folderpath, int nbframes) {
			images = new BufferedImage[nbframes];
			for (int i = 0; i < nbframes; i++) {
				try {
					images[i] = ImageIO.read(getClass().getResource(folderpath + "/" + i + ".png"));
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		public void changeFrame() {
			if (actualFrame++ >= images.length - 1) {
				actualFrame = 0;
			}
		}

		public BufferedImage actualImage() {
			return images[actualFrame];
		}
	}
}
