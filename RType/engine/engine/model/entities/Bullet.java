package engine.model.entities;

import Stunts.BulletStunt;
import engine.model.Entity;
import engine.model.Model;

public class Bullet extends Entity {

	public Bullet(Model m, int r, int c, int o) {
		super(m, r, c, o);
		stunt = new BulletStunt(m_model, this);
		speedX = 1;
		speedY = 1;
	}

	@Override
	protected void collision(Entity entity) {
		// TODO : infliger des dégats au lieu de le tuer
		// entity.die();
		speedX = 0;
		speedY = 0;

	}

}
