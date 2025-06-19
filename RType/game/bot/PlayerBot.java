package bot;

import engine.brain.Bot;
import engine.brain.Brain;
import engine.brain.Category;
import engine.model.Entity;
import entities.Player;

public class PlayerBot extends Bot {
	private int duration;

	public PlayerBot(Brain b, Entity e) {
		super(b, e);
		c = Category.Player;
		duration = delay;
	}

	@Override
	public void think() {
		Playing();
	}

	private void Playing() {
		if (collision(e))
			playerCollision((Player) e, entityCollisionWithPlayer); // action de collision -> décrémentation des HP

		delay = duration;
	}

}