package engine.model;

import Stunts.StuntPlayer;

public class Player extends Entity {

	protected double x, y;
	private int score;
	private int HP;

	public Player(Model m, int x, int y, int o) {
		super(m, x, y, o);
		new StuntPlayer(m, this);
		m.setPlayer(this);
		this.score = 0;
		this.HP = 1000; // au pif sah
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
			this.HP -= entity.bot.getPointsValue(); // HP baisse de la valeur de l'entité touchée
		}
		System.out.println("HP : " + HP);
		System.out.println("Collision avec " + entity.getClass().getSimpleName());
	}

	public int getScore() {
		return this.score;
	}

	public void setScore(int points) {
		this.score += points;
	}

	public int getHP() {
		return this.HP;
	}

	public void setHP(int points) {
		this.HP -= points;
	}
}
