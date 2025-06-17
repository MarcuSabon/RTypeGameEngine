package engine.model.entities;

import Stunts.BulletStunt;
import engine.model.Entity;
import engine.model.Model;
import game.bot.BulletBot;

public class Bullet extends Entity {

	public Bullet(Model m, int r, int c, int o) {
		super(m, r, c, o);
		stunt = new BulletStunt(m_model, this);
		new BulletBot(m.getBrain(), this);
		speedX = 1;
		speedY = 1;
	}

	@Override
	protected void collision(Entity entity) {
		bot.setCollision(true);
		System.out.println("Collision avec " + entity.getClass().getSimpleName());
		speedX = 0;
		speedY = 0;

	}

}
