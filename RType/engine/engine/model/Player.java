package engine.model;

import Stunts.StuntPlayer;

public class Player extends Entity {

	protected double x, y;

	public Player(Model m, int x, int y, int o) {
		super(m, x, y, o);
		new StuntPlayer(m, this);
		m.setPlayer(this);
	}

	/*
	 * Move this entity in the model by the given count of meters.
	 */
	public void move(double x, double y) {
		m_model.move(this, x, y);
	}

	@Override
	protected void collision(Entity entity) {
		// TODO Auto-generated method stub
		System.out.println("Collision avec " + entity.getClass().getSimpleName());
	}
}
