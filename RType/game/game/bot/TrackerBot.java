package game.bot;

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
	}

	@Override
	public void think() {
		Action();
	}

	private void Action() {
		Entity e = closest(Category.Adversary);
		if (e == null)
			return;
		Direction d = dirOf(e).cardinalOf();

		if (d == Direction.N) {
			moveWithRotation(Direction.N);
		} else if (d == Direction.E) {
			moveWithRotation(Direction.E);
		} else if (d == Direction.S) {
			moveWithRotation(Direction.S);
		} else if (d == Direction.W) {
			moveWithRotation(Direction.W);
		}

		delay = duration;
	}
}
