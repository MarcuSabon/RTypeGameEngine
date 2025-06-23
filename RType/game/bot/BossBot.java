package bot;

import java.util.Random;

import boss.Master;
import boss.StuntMaster;
import engine.brain.Bot;
import engine.brain.Brain;
import engine.brain.Category;
import engine.model.Entity;

public class BossBot extends Bot {

	private static final int MOVING = 0;
	private static final int ATTACKING = 1;
	private static final int SPAWNING = 2;
	private static final int HURT = 3;
	private static final int DYING = 4;
	private static final Random random = new Random();
	public int state;

	private int baseSpawnY;

	public BossBot(Brain b, Entity e) {
		super(b, e);
		random.setSeed(System.currentTimeMillis());
		state = SPAWNING;
		c = Category.Adversary;
		pointsValue += 100; // valeur d'un bot de base par exemple
		HP = 10; // ici le bot peut subir 3 collisions puis meurt
		baseSpawnY = e.row();

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
		case SPAWNING:
			SPAWNING(elapsed);
			break;
		case MOVING:
			Moving();
			break;
		case ATTACKING:
			Attacking(elapsed);
			break;
		case HURT:
			Hurt(elapsed);
			break;
		case DYING:
			Dying(elapsed);
			break;
		}
		Master em = ((Master) e);
		for (int i = 0; i < em.body.length; i++) {
			for (int j = 0; j < em.body[0].length; j++) {
				Entity eExplored = em.body[i][j];
				if (eExplored != null && !(eExplored instanceof Master)) {
					if (eExplored.isDead()) {
						eExplored = null;
					} else {
						eExplored.bot.think(elapsed);

					}
				}
			}
		}

	}

	int SPAWNINGTIME = 1000;

	private void SPAWNING(int elapsed) {
		SPAWNINGTIME -= elapsed;
		if (SPAWNINGTIME < 0) {
			state = MOVING;
			SPAWNINGTIME = 1000;
		}
	}

	private int oldState = 0;

	private void Moving() {
		int r = random.nextInt(4);
		if (oldState == MOVING) {
			if (r == 0) {
				state = ATTACKING;
				oldState = ATTACKING;
			} else {
				chooseDirection();
				oldState = MOVING;
			}
		} else {
			if (r == 0 || r == 1) {
				chooseDirection();
				oldState = MOVING;
			} else {
				state = ATTACKING;
				oldState = ATTACKING;

			}
		}

	}

	private int oldDirection = 1;

	private void chooseDirection() {
		if (e.row() < baseSpawnY - 3) {
			e.stunt.move(1, 0);
			oldDirection = 1;
		} else if ((e.row() > baseSpawnY + 3)) {
			e.stunt.move(-1, 0);
			oldDirection = -1;
		} else {
			int r = random.nextInt(4);
			if (r == 0)
				e.stunt.move(-oldDirection, 0);
			else
				e.stunt.move(oldDirection, 0);

		}
	}

	int ATTACKINGTIME = 1000;
	boolean shot = false;

	private void Attacking(int elapsed) {
		ATTACKINGTIME -= elapsed;
		if (ATTACKINGTIME < 0) {
			state = MOVING;
			ATTACKINGTIME = 1000;
			shot = false;
		}

		if (ATTACKINGTIME % 200 == 0) {
			int r = random.nextInt(4);
			StuntMaster sm = (StuntMaster) e.stunt;
			if (r == 0)
				sm.Missile();
			else
				sm.shoot();

			shot = true;
		}
	}

	int HURTINGTIME = 100;

	private void Hurt(int elapsed) {
		HURTINGTIME -= elapsed;
		if (HURTINGTIME < 0) {
			HP--;
			if (HP <= 0)
				state = DYING;
			else {
				state = MOVING;
				HURTINGTIME = 10;
			}
		}
	}

	int DYINGTIME = 2000;

	private void Dying(int elapsed) {
		DYINGTIME -= elapsed;
		if (DYINGTIME < 0) {
			((Master) e).preDie();
			e.die();
		}
	}

	@Override
	public void think() {
		//
	}
}
