package bot;

import engine.brain.Bot;
import engine.brain.Brain;
import engine.brain.Category;
import engine.model.Entity;

public class BossPartBot extends Bot {
	private static final int FULL = 0;
	private static final int MID = 1;
	private static final int LOW = 2;
	public int state;

	private static final int HURT = 0;
	private static final int FOLLOWING = 1;
	private static final int DYING = 4;

	public BossPartBot(Brain b, Entity e) {
		super(b, e);
		state = FOLLOWING;
		c = Category.Adversary;
		pointsValue += 100; // valeur d'un bot de base par exemple
		HP = 3; // ici le bot peut subir 3 collisions puis meurt

	}

	@Override
	public void think(int elapsed) {
		if (e.stunt.action() != null) {
			return;
		}
		if (collision(e)) {
			state = HURT;
		}
		switch (state) {
		case FOLLOWING:
			Following(elapsed);
			break;
		case HURT:
			Hurt(elapsed);
			break;
		case DYING:
			Dying(elapsed);
			break;
		}
	}

	private void Following(int elapsed) {
		// TODO Auto-generated method stub

	}

	int HURTINGTIME = 10;

	private void Hurt(int elapsed) {
		HURTINGTIME -= elapsed;
		if (HURTINGTIME < 0) {
			HP--;
			if (HP <= 0) {
				state = DYING;
			} else {
				state = FOLLOWING;
				HURTINGTIME = 10;
			}
		}
	}

	int DYINGTIME = 500;

	private void Dying(int elapsed) {
		DYINGTIME -= elapsed;
		if (DYINGTIME < 0) {
			e.die();
		}
	}

	@Override
	public void think() {
		//
	}

	private void full() {
		if (collision(e)) {
			HP--;
			System.out.println("HP Bot : " + getHP());
			state = MID;
		}
	}

	private void mid() {
		if (collision(e)) {
			HP--;
			System.out.println("HP Bot : " + getHP());
			state = LOW;
		}

	}

	private void low() {
		if (collision(e)) {
			HP--;
			System.out.println("HP Bot : " + getHP());
			e.die();
		}
	}
}