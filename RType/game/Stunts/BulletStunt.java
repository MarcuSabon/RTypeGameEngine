package Stunts;

import engine.IBrain.IBot;
import engine.model.Entity;
import engine.model.Model;
import engine.model.Stunt;
import engine.model.entities.Bullet;

public class BulletStunt extends Stunt {
	private Bullet bullet;
	private static final int MOVEMENT_DURATION = 250;

	public BulletStunt(Model m, Entity e) {
		super(m, e);
		SPEEDNERF = 50;
	}

	@Override
	public void tick(int elapsed) {
		double rad = Math.toRadians(e.orientation());
		e.speedX = Math.cos(rad);
		e.speedY = Math.sin(rad);
		if (action == null) {
			move(e.speedX * elapsed / SPEEDNERF, e.speedY * elapsed / SPEEDNERF);
			IBot bot = e.bot;
			if (bot != null) {
				bot.think(elapsed);
			}
		}
	}

	@Override
	public void rotate(int angle) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean move(int nrows, int ncols) {
		// TODO Auto-generated method stub
		return false;
	}

}
