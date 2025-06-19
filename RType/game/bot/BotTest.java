package bot;

import engine.brain.Bot;
import engine.brain.Brain;
import engine.brain.Category;
import engine.brain.Direction;
import engine.model.Entity;

public class BotTest extends Bot {

	private int duration;

	public BotTest(Brain b, Entity e) {
		super(b, e);
		c = Category.Adversary;
		duration = delay;
		pointsValue += 100; // valeur d'un bot de base par exemple
	}

	@Override
	public void think() {
		Action();
	}

	private void Action() {
		if (collision(e)) {
			e.die();
		} else {
			move(Direction.F);
		}

		delay = duration;
	}
}