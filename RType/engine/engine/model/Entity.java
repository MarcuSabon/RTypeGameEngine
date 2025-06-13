package engine.model;

import engine.IBrain.IBot;

public abstract class Entity {
	protected Model m_model;
	protected int m_row, m_col;
	protected int m_orientation;

	protected double x, y;
	protected double accX = 0;
	protected double accY = 0;
	public double speedX = 0, speedY = 0;;

	public IBot bot;
	public Stunt stunt;
	public Object avatar;

	protected Entity(Model m, int r, int c, int o) {
		m_model = m;
		m_row = r;
		m_col = c;
		x = m_col * m.metric();
		y = m_row * m.metric();
		m_orientation = normalize(o);
		m_model.addAt(this);
	}

	// Normalize an angle back to the range [0:360[
	private int normalize(int angle) {
		if (angle < 0)
			angle += 360 + (angle % 360);
		else
			angle %= 360;
		return angle;
	}

	/*
	 * Rotate the entity by the given angle
	 */
	public void rotate(int theta) {
		stunt.rotate(theta);
	}

	/*
	 * Get the entity to face the orientation match the given angle
	 */
	public void face(int theta) {
		m_orientation = normalize(theta);
	}

	public int orientation() {
		return m_orientation;
	}

	public int row() {
		return m_row;
	}

	public int col() {
		return m_col;
	}

	public double x() {
		return x;
	}

	public double y() {
		return y;
	}

	public void at(int row, int col) {
		m_row = row;
		m_col = col;
	}

	public void at(double x, double y) {
		this.x = x;
		this.y = y;
	}

	public void at(double x, double y, int r, int c) {
		this.x = x;
		this.y = y;
		m_row = r;
		m_col = c;
	}

	public void die() {
		m_model.death(this);
	}

	protected abstract void collision(Entity entity);

	/**
	 * @return the accX
	 */
	public double getAccX() {
		return accX;
	}

	/**
	 * @param accX the accX to set
	 */
	public void setAccX(double accX) {
		this.accX = accX;
	}

	/**
	 * @return the accY
	 */
	public double getAccY() {
		return accY;
	}

	/**
	 * @param accY the accY to set
	 */
	public void setAccY(double accY) {
		this.accY = accY;
	}
}
