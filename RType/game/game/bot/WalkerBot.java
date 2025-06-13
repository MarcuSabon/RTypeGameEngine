package game.bot;

import engine.brain.Bot;
import engine.brain.Brain;
import engine.brain.Category;
import engine.brain.Direction;
import engine.model.Entity;

public class WalkerBot extends Bot {

	private int duration;

	public WalkerBot(Brain b, Entity e) {
		super(b, e);
		c = Category.Adversary;
		duration = delay;
	}

	@Override
	public void think() {
		Action();
	}

	private void Action() {
		if (cell(Direction.F) == null)
			move(Direction.F);
		else if (cell(Direction.L) == null)
			turn(Direction.L);
		else if (cell(Direction.R) == null)
			turn(Direction.R);
		else if (cell(Direction.B) == null)
			turn(Direction.B);

		delay = duration;
	}
}
