package game.bot;

import engine.brain.Bot;
import engine.brain.Brain;
import engine.brain.Category;
import engine.brain.Direction;
import engine.model.Entity;

public class Bot3HP extends Bot {
	private static final int FULL = 0;
	private static final int MID = 1;
	private static final int LOW = 2;
	private int state;

	private int duration;

	public Bot3HP(Brain b, Entity e) {
		super(b, e);
		c = Category.Adversary;
		state = FULL;
		duration = delay;
		pointsValue += 100; // valeur d'un bot de base par exemple
		HP = 3; // ici le bot peut subir 3 collisions puis meurt
	}

	@Override
	public void think() {
		switch (state) {
		case FULL:
			full();
			break;
		case MID:
			mid();
			break;
		case LOW:
			low();
			break;
		}
	}

	private void full() {
		if (collision(e)) {
			HP--;
			System.out.println("HP Bot : " + getHP());
			state = MID;
		} else {
			if (cell(Direction.F) == null)
				move(Direction.F);
			else if (cell(Direction.L) == null)
				turn(Direction.L);
			else if (cell(Direction.R) == null)
				turn(Direction.R);
			else if (cell(Direction.B) == null)
				turn(Direction.B);
		}

		delay = duration;
	}

	private void mid() {
		if (collision(e)) {
			HP--;
			System.out.println("HP Bot : " + getHP());
			state = LOW;
		} else {
			if (cell(Direction.F) == null)
				move(Direction.F);
			else if (cell(Direction.L) == null)
				turn(Direction.L);
			else if (cell(Direction.R) == null)
				turn(Direction.R);
			else if (cell(Direction.B) == null)
				turn(Direction.B);
		}

		delay = duration;
	}

	private void low() {
		if (collision(e)) {
			HP--;
			System.out.println("HP Bot : " + getHP());
			e.die();
		} else {
			if (cell(Direction.F) == null)
				move(Direction.F);
			else if (cell(Direction.L) == null)
				turn(Direction.L);
			else if (cell(Direction.R) == null)
				turn(Direction.R);
			else if (cell(Direction.B) == null)
				turn(Direction.B);
		}

		delay = duration;
	}
}