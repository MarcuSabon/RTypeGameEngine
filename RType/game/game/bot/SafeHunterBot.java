package game.bot;

import engine.brain.Bot;
import engine.brain.Brain;
import engine.brain.Category;
import engine.brain.Direction;
import engine.model.Entity;

public class SafeHunterBot extends Bot {
	private static final int Flee = 0;
	private static final int Track = 1;
	private int state;

	private int duration;

	public SafeHunterBot(Brain b, Entity e) {
		super(b, e);
		c = Category.Adversary;
		duration = delay;
		state = Track;
		pointsValue += 100; // valeur d'un bot de base par exemple

	}

	public void think() {
		switch (state) {
		case Flee:
			flee();
			break;
		case Track:
			track();
			break;
		}
	}

	private void flee() {
		if (cell(Direction.F) != null) {
			moveWithRotation(Direction.B);
		} else if (cell(Direction.B) != null) {
			moveWithRotation(Direction.L);
		} else if (cell(Direction.L) != null) {
			moveWithRotation(Direction.L);
		} else if (cell(Direction.R) != null) {
			moveWithRotation(Direction.R);
		} else {
			state = Track;
		}

		delay = duration;

	}

	private void track() {
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
		state = Flee;
		delay = duration;
	}
}
