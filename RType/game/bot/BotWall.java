package bot;

import engine.brain.Bot;
import engine.brain.Brain;
import engine.brain.Category;
import engine.model.Entity;

public class BotWall extends Bot {

	public BotWall(Brain b, Entity e) {
		super(b, e);
		c = Category.Obstacle;
		HP = 1;
	}

	@Override
	public void think(int elapsed) {
		// Nothing
	}

	@Override
	public void think() {
	}

}