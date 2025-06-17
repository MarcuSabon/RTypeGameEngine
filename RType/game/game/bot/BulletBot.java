package game.bot;

import engine.brain.Bot;
import engine.brain.Brain;
import engine.brain.Category;
import engine.model.Entity;

public class BulletBot extends Bot {
	private int duration;

	public BulletBot(Brain b, Entity e) {
		super(b, e);
		c = Category.Adversary;
		duration = delay;
		pointsValue += 100;
	}

	@Override
	public void think() {
		Fly();
	}

	private void Fly() {
		if (collision(e))
			e.die();

		delay = duration;
	}

}
