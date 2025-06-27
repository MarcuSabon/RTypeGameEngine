package entities;

import bot.ShooterBot;
import engine.brain.Category;
import engine.model.Model;
import stunts.StuntShooter;

public class Shooter extends ShootingPNJ {

	public Shooter(Model m, int r, int c, int o) {
		super(m, r, c, o);
		new StuntShooter(m_model, this);
		new ShooterBot(m.getBrain(), this);
	}

	@Override
	public void shoot(int row, int col, int orientation, double rad) {
		Bullet b = new Bullet(m_model, row, col, orientation, Category.Adversary);
		b.at(col + Math.cos(rad), row + Math.sin(rad) + 0.5); // On centre la bullet
	}

}
