package stunts;

import engine.IBrain.IBot;
import engine.model.Entity;
import engine.model.Model;
import engine.model.Stunt;
import engine.utils.Utils;

public class StuntMissile extends Stunt {
	Entity target;

	public StuntMissile(Model m, Entity e, Entity target) {
		super(m, e);
		SPEEDNERF = 100;
		this.target = target;
		double rad = Math.toRadians(e.orientation());

		e.speedX = Math.cos(rad);
		e.speedY = Math.sin(rad);
	}

	@Override
	public void tick(int elapsed) {
		double XrelativePlayer = target.x() - e.x();
		double YrelativePlayer = target.y() - e.y();
		double angleToPlayer = Utils.theta((float) XrelativePlayer, (float) YrelativePlayer);
		double rad = Math.toRadians(angleToPlayer);
		e.face((int) angleToPlayer);
		e.speedX = (0.007 * Math.cos(rad)) + 0.993 * e.speedX;
		e.speedY = (0.007 * Math.sin(rad)) + 0.993 * e.speedY;
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