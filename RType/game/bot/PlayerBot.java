package bot;

import engine.brain.Bot;
import engine.brain.Brain;
import engine.brain.Category;
import engine.model.Entity;
import entities.Player;
import stunts.StuntPlayer;

public class PlayerBot extends Bot {
	private int duration;
	public boolean shooting;

	public PlayerBot(Brain b, Entity e) {
		super(b, e);
		c = Category.Player;
		delay = 100;
	}

	@Override
	public void think() {
		Playing();
	}

	private void Playing() {
		if (collision(e)) {
			playerCollision((Player) e, entityCollisionWithPlayer); // action de collision -> décrémentation des HP
		} else if (shooting) {
			StuntPlayer sp = (StuntPlayer) e.stunt;
			sp.playerShoot(e);
		}

		delay = duration;
	}

}