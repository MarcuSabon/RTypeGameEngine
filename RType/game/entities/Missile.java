package entities;

import bot.BulletBot;
import engine.model.Entity;
import engine.model.Model;
import stunts.StuntMissile;

public class Missile extends Entity {

	public boolean collided = false;

	public Missile(Model m, int r, int c, int o, Entity target) {
		super(m, r, c, o);
		new StuntMissile(m_model, this, target);
		new BulletBot(m.getBrain(), this);
	}

	@Override
	protected void collision(Entity entity) {
		speedX = 0;
		speedY = 0;
		m_model.emptyGrid(m_row, m_col);
		bot.setCollision(true);
		collided = true;
	}
}
