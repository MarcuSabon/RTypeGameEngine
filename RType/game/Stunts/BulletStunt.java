package Stunts;

import engine.model.Entity;
import engine.model.Model;
import engine.model.Stunt;

public class BulletStunt extends Stunt {

	private double MOVINGTIME = 10.0;

	public BulletStunt(Model m, Entity e) {
		super(m, e);
		SPEEDNERF = 100;
	}

	@Override
	public void tick(int ms) {
		double rad = Math.toRadians(e.orientation());
		e.speedX = Math.cos(rad);
		e.speedY = Math.sin(rad);
		move(e.speedX * ms / SPEEDNERF, e.speedY * ms / SPEEDNERF);
	}

	@Override
	public boolean move(int nrows, int ncols) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void rotate(int angle) {
		// TODO Auto-generated method stub

	}

}
