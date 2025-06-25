package engine.model;

import engine.IBrain.IBot;
import map.Wall;
import sound.SoundPlayer;

public abstract class Entity {
	protected Model m_model;
	protected int m_row, m_col;
	protected int m_orientation;

	protected double x, y;
	protected double accX;
	protected double accY;
	public double speedX, speedY;
	protected String sprite;

	public IBot bot;
	public Stunt stunt;
	public Object avatar;

	private boolean isDead = false; // Pour pouvoir marquer l'entité comme morte en attendant sa suppression à la
									// fin du tick()

	protected Entity(Model m, int r, int c, int o) {
		m_model = m;
		m_row = r;
		m_col = c;
		x = m_col * m.metric();
		y = m_row * m.metric();
		m_orientation = normalize(o);
		m_model.addAt(this);
	}

	protected Entity(Model m, int r, int c, int o, String sprite) {
		this(m, r, c, o);
		this.sprite = sprite;
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

	public void die() { // chagngement de die(), on ne fait plus mourir l'entité directement pour éviter
						// une ConcurrentModificationException
		isDead = true;
		if (!(this instanceof Wall)) {
			SoundPlayer.play("/Sounds/BotDeath.wav");
		}
	}

	public boolean isDead() { // Marque l'entité comme morte. La suppression réelle est différée à la fin du
								// tick via Model.tick().
		return isDead;
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

	public String sprite() {
		return sprite;
	}

	public void collision() {
//
	}
}
