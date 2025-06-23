package entities;

import engine.model.Model;
import engine.model.PNJ;

public abstract class ShootingPNJ extends PNJ {

	public ShootingPNJ(Model m, int r, int c, int o) {
		super(m, r, c, o);
	}

	public abstract void shoot(int row, int col, int orientation, double rad);

}
