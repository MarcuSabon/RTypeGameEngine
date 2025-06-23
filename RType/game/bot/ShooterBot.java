package bot;

import engine.brain.Bot;
import engine.brain.Brain;
import engine.brain.Category;
import engine.brain.Direction;
import engine.model.Entity;
import entities.ShootingPNJ;

public class ShooterBot extends Bot {

	private int duration;
	private ShootingPNJ e;

	public ShooterBot(Brain b, ShootingPNJ e) {
		super(b, e);
		this.e = e;

		c = Category.Adversary;
		duration = delay;
		pointsValue += 100; // valeur d'un bot de base par exemple

	}

	@Override
	public void think() {
		Action();
	}

	private void Action() {
		if (collision(e)) {
			e.die();
		}

		Entity e = closest(Category.Adversary);
		if (e == null)
			return;
		Direction d = dirOf(e).cardinalOf();

		if (isFree(d)) {
			moveWithRotation(d);
			if (d == Direction.E && e.row() == this.e.row()) {
				shoot(Direction.E, this.e);
			}
			if (d == Direction.W && e.row() == this.e.row()) {
				shoot(Direction.W, this.e);
			}
		} else {
			tryAlternativeDirections();
		}

		delay = duration;
	}

}
