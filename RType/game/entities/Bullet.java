package entities;

import bot.BulletBot;
import engine.model.Entity;
import engine.model.Model;
import engine.model.PNJ;
import stunts.StuntBullet;

public class Bullet extends PNJ {

	public Bullet(Model m, int r, int c, int o) {
		super(m, r, c, o);
		new StuntBullet(m_model, this);
		new BulletBot(m.getBrain(), this);
		speedX = 1;
		speedY = 1;
	}

	@Override
	protected void collision(Entity entity) {
		bot.setCollision(true);
		speedX = 0;
		speedY = 0;
	}

}
