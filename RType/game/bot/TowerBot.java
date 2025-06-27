package bot;

import engine.brain.Bot;
import engine.brain.Brain;
import engine.brain.Category;
import engine.brain.Direction;
import engine.model.Entity;
import engine.model.PNJ;
import entities.ShootingPNJ;

public class TowerBot extends Bot {

	private static final int MOVING = 0;
	private static final int ACTION = 1;
	private int moveFinish = 1; // nombre de déplacement vers la gauche avant de rester immobile
	private int state;
	private int delayShoot = 0;

	private int duration;
	private ShootingPNJ e;

	public TowerBot(Brain b, ShootingPNJ e) {
		super(b, e);
		this.e = e;
		c = Category.Adversary;
		duration = delay;
		pointsValue += 300; // valeur d'un bot de base par exemple
		state = MOVING;
	}

	@Override
	public void think() {
		switch (state) {
		case MOVING:
			Moving();
			break;
		case ACTION:
			Action();
			break;
		}
	}

	private void Moving() {
		if (collision(e)) {
			PNJCollision((PNJ) e, entityCollisionWith);
			e.die();
		} else {
			moveFinish--;
			e.face(180);
		}

		if (moveFinish == 0) {
			state = ACTION;
		}

		delay = duration;
	}

	private void Action() {
		if (collision(e)) {
			PNJCollision((PNJ) e, entityCollisionWith);
			e.die();
		}
		Entity e = closest(Category.Adversary);
		if (e == null)
			return;

		if (delayShoot <= 0) {
			shoot(Direction.W, this.e);
			delayShoot = 5000;
		}

		delay = duration;
		delayShoot -= delay;
	}
}