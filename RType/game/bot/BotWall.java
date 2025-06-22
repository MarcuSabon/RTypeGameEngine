package bot;

import engine.brain.Bot;
import engine.brain.Brain;
import engine.brain.Category;
import engine.model.Entity;

public class BotWall extends Bot {
	private int duration;

	public BotWall(Brain b, Entity e) {
		super(b, e);
		c = Category.Obstacle;
		duration = delay;
		HP = 1;
	}

	@Override
	public void think() {
		Action();
	}

	private void Action() {
		delay = duration;
	}

}