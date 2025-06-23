package entities;

import bot.TowerBot;
import engine.model.Model;
import stunts.StuntTower;

public class Tower extends ShootingPNJ {

	public Tower(Model m, int r, int c, int o) {
		super(m, r, c, o);
		new StuntTower(m_model, this);
		new TowerBot(m.getBrain(), this);
	}

	@Override
	public void shoot(int row, int col, int orientation, double rad) {
		Tracker t = new Tracker(m_model, row, col, orientation);
		t.at(col + Math.cos(rad), row + Math.sin(rad));
	}

}
