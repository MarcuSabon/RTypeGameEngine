package bot;

import engine.brain.Bot;
import engine.brain.Brain;
import engine.brain.Category;
import engine.brain.Direction;
import engine.model.Entity;

public class TrackerBot extends Bot {

	private int duration;

	public TrackerBot(Brain b, Entity e) {
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
		}

		Entity entity = closest(Category.Adversary);
		if (entity == null) {
			return;
		}
		Direction d = dirOf(entity).cardinalOf();

		if (isFree(d)) {
			moveWithRotation(d);
		} else {
			tryAlternativeDirections();
		}

		delay = duration;
	}

	@Override
	protected boolean isFree(Direction d) {
		Entity entity = cell(d);
		return entity == null || entity == b.getModel().player();
	}
}
