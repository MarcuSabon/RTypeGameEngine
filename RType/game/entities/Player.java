package entities;

import bot.PlayerBot;
import engine.model.Entity;
import engine.model.Model;
import stunts.StuntPlayer;

public class Player extends Entity {

	protected double x, y;
	private int score;
	private int HP;

	public Player(Model m, int x, int y, int o) {
		super(m, x, y, o);
		new StuntPlayer(m, this);
		new PlayerBot(m.getBrain(), this);
		m.setPlayer(this);
		score = 0;
		HP = 1000; // au pif sah
	}

	/*
	 * Move this entity in the model by the given count of meters.
	 */
	public void move(double x, double y) {
		m_model.move(this, x, y);
	}

	@Override
	protected void collision(Entity entity) {
		if (!(entity instanceof Player)) { // Si collision avec une autre entité
			bot.setCollision(true);
			bot.setCollisionWithEntity(entity);
		}

	}

	public int getScore() {
		return this.score;
	}

	public void setScore(Entity e) {
		if (!(e instanceof Bullet)) {
			this.score += e.bot.getPointsValue();
		}
	}

	public int getHP() {
		return this.HP;
	}

	public void setHP(int points) {
		this.HP -= points;
	}
}
