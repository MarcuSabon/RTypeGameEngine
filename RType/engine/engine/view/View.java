package engine.view;

import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.geom.AffineTransform;

import engine.IModel;
import engine.IView;
import engine.model.Entity;
import engine.model.Player;
import engine.utils.Utils;
import oop.graphics.Canvas;
import oop.graphics.Color;
import oop.tasks.Task;

public abstract class View implements IView {

	protected Canvas m_canvas;
	protected IModel m_model;
	protected int px;
	protected int py;
	protected int cellsize;
	protected Player p;
	protected int zoom = 1;
	protected double oX = 0;
	protected double oY = 0;
	protected double speedX = 0;
	protected double speedY = 0;
	protected boolean debug;

	protected View(Canvas canvas, IModel model) {
		m_canvas = canvas;
		m_model = model;
		model.register(this);
	}

	public abstract void focus(int px, int py);

	public abstract void move(double x, double y);

	public abstract void tick(int ms);

	public void zoom(double zoom) {
		this.zoom += zoom;
		if (zoom == 0) {
			reset();
		}
		if (this.zoom > 5) {
			this.zoom = 5;
		}
		if (this.zoom <= 1) {
			this.zoom = 1;
		}
	}

	public void reset() {
		zoom = 1;
		oX = 0;
		oY = 0;
	}

	public void speed(double x, double y) {
		speedX = x;
		speedY = y;
	}

	public void moveBySpeed(int ms) {
		move(speedX * ms / 5, speedY * ms / 5);
		tick(ms);
	}

	public int distXpx(int px) {
		return (int) (px - oX);
	}

	public int distYpx(int py) {
		return (int) (py - oY);
	}

	public int pxPerMeter() {
		return cellsize;
	}

	public void cellsize() {
		int min;

		if (m_canvas.getHeight() / m_model.nrows() < m_canvas.getWidth() / m_model.ncols()) {
			min = m_canvas.getHeight();
			cellsize = min / m_model.nrows();
		} else {
			min = m_canvas.getWidth();
			cellsize = min / m_model.ncols();
		}
	}

	public void paint(Canvas canvas, Graphics2D g) {
		cellsize();
		g.setColor(java.awt.Color.LIGHT_GRAY);
		g.fillRect(0, 0, m_canvas.getWidth(), m_canvas.getHeight());
		g.translate(oX, oY);
		g.setColor(java.awt.Color.GRAY);
		g.fillRect(0, 0, m_model.ncols() * pxPerMeter() * zoom, m_model.nrows() * pxPerMeter() * zoom);
		if (debug)
			debugMode(g);
		subPaint(g, oX, oY, zoom);

		g.scale(zoom, zoom);
	}

	public abstract void subPaint(Graphics2D g, double x, double y, float zoom);

	@Override
	public void debug() {
		debug = !debug;
	}

	protected void debugMode(Graphics2D g) {
		int ligne = 1;
		g.setColor(java.awt.Color.BLACK);

		for (int i = 0; i <= m_model.nrows(); i++) {
			g.fillRect(0, i * cellsize * zoom, cellsize * zoom * m_model.ncols() + ligne, ligne);
		}
		for (int i = 0; i <= m_model.ncols(); i++) {
			g.fillRect(i * cellsize * zoom, 0, ligne, cellsize * zoom * m_model.nrows() + ligne);
		}
	}

}
