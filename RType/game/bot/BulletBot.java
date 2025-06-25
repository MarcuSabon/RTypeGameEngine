package bot;

import engine.brain.Bot;
import engine.brain.Brain;
import engine.brain.Category;
import engine.model.Entity;

public class BulletBot extends Bot {
	private int duration;

	public BulletBot(Brain b, Entity e, Category cat) {
		super(b, e);
		c = cat;
		duration = delay;
		pointsValue += 100;
	}

	@Override
	public void think() {
		Fly();
	}

	private void Fly() {
		if (collision(e)) {
			e.die();
		}
		delay = duration;
	}

}
