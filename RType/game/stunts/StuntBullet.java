package stunts;

import engine.IBrain.IBot;
import engine.model.Entity;
import engine.model.Model;
import engine.model.Stunt;
import entities.Bullet;

public class StuntBullet extends Stunt {
	private Bullet bullet;
	private static final int MOVEMENT_DURATION = 250;

	public StuntBullet(Model m, Entity e) {
		super(m, e);
		SPEEDNERF = 45;
		double rad = Math.toRadians(e.orientation());

		e.speedX = Math.cos(rad);
		e.speedY = Math.sin(rad);
	}

	@Override
	public void tick(int elapsed) {

		if (action == null) {
			move(realSpeedX() * elapsed, realSpeedY() * elapsed);
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
