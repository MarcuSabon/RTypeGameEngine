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
	public void think(int elapsed) {
		if (b.getModel().entity(e.row(), e.col()) != e) { // marche pas à chaque fois, je comprend pas pourquoi
			b.getModel().setGrid(e.row(), e.col(), (Entity) e);
			System.out.println("Replacement Wall");
		}
		Action(elapsed);
	}

	private void Action(int elapsed) {
//
	}

	@Override
	public void think() {
//
	}

}