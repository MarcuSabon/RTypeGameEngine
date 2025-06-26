package bot;

import engine.brain.Bot;
import engine.brain.Brain;
import engine.brain.Category;
import engine.model.Entity;
import entities.Player;
import sound.SoundPlayer;
import stunts.StuntPlayer;

public class PlayerBot extends Bot {
	private int duration;
	public boolean shooting;

	public PlayerBot(Brain b, Entity e) {
		super(b, e);
		c = Category.Player;
		delay = 150;
	}

	@Override
	public void think() {
		Playing();
	}

	private void Playing() {
		if (e.col() == 0) {
			HP = 0;
		}
		if (collision(e) && !b.getModel().config().immortal) {
			playerCollision((Player) e, entityCollisionWith); // action de collision -> décrémentation des HP
		} else if (shooting) {
			StuntPlayer sp = (StuntPlayer) e.stunt;
			sp.playerShoot(e);
			SoundPlayer.shootProjectile();
		}

		delay = duration;
	}

}
