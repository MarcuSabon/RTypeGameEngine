package entities;

import bot.BulletBot;
import engine.brain.Category;
import engine.model.Entity;
import engine.model.Model;
import engine.model.PNJ;
import stunts.StuntBullet;

public class Bullet extends PNJ {

	public boolean collided = false;

	public Bullet(Model m, int r, int c, int o, Category cat) {
		super(m, r, c, o);
		new StuntBullet(m_model, this);
		new BulletBot(m.getBrain(), this, cat);
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
