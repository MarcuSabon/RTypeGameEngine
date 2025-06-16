package engine.model;

import Stunts.StuntPNJ;
import game.bot.Bot3HP;

public class PNJ extends Entity {

	public PNJ(Model m, int r, int c, int o) {
		super(m, r, c, o);
		new StuntPNJ(m_model, this);
		bot = new Bot3HP(m.getBrain(), this);
	}

	@Override
	protected void collision(Entity entity) {
		bot.setCollision(true);
		System.out.println("Collision avec " + entity.getClass().getSimpleName());
	}

}
