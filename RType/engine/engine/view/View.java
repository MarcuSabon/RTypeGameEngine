package engine.view;

import java.awt.Graphics2D;
import java.util.LinkedList;
import java.util.List;

import engine.IModel;
import engine.IView;
import engine.model.Entity;
import engine.model.Player;
import engine.model.entities.Bullet;
import oop.graphics.Canvas;

public abstract class View implements IView {

	protected Canvas m_canvas;
	protected IModel m_model;
	protected int px;
	protected int py;
	protected int cellSize;
	protected Player p;
	protected int zoom = 1;
	protected double oX = 0;
	protected double oY = 0;
	protected double speedX = 0;
	protected double speedY = 0;
	protected double pixelsPerMetricCell;
	protected boolean debug;
	protected List<Avatar> m_visibleAvatars;

	protected static final int MAX_ZOOM = 15;

	protected View(Canvas canvas, IModel model) {
		m_canvas = canvas;
		m_model = model;
		model.register(this);
		m_visibleAvatars = new LinkedList<Avatar>();
		pixelsPerMetricCell = cellSize / m_model.metric();
	}

	public void focus(int px, int py) {
		this.px = px;
		this.py = py;
	}

	@Override
	public void zoomIn() {
		zoom += 1;
		if (zoom > MAX_ZOOM)
			zoom = MAX_ZOOM;
	}

	@Override
	public void zoomOut() {
		zoom -= 1;
		if (zoom <= 1)
			resetZoom();
	}

	@Override
	public void resetZoom() {
		zoom = 1;
		oX = 0;
		oY = 0;
	}

	// LES MÉTHODES DE SCROLL SONT INUTILISÉES DANS CE JEU
	@Override
	public void scrollLeft() {
		double newX = oX + cellSize / zoom;

		if (newX > 0)
			newX = 0;

		oX = newX;
	}

	@Override
	public void scrollRight() {
		double newX = oX - cellSize / zoom;
		double minX = m_canvas.getWidth() * (1 - zoom);

		if (minX > 0)
			minX = 0;

		if (newX < minX)
			newX = minX;

		oX = newX;
	}

	@Override
	public void scrollUp() {
		double newY = oY + cellSize / zoom;
		if (newY > 0)
			newY = 0;
		oY = newY;
	}

	@Override
	public void scrollDown() {
		double minY = m_canvas.getHeight() * (1 - zoom);
		double newY = oY - cellSize / zoom;

		if (minY > 0)
			minY = 0;

		if (newY < minY)
			newY = minY;

		oY = newY;
	}

	public void speed(double x, double y) {
		speedX = x;
		speedY = y;
	}

	public int distXpx(int px) {
		return (int) (px - oX);
	}

	public int distYpx(int py) {
		return (int) (py - oY);
	}

	public int pxPerMeter() {
		return cellSize;
	}

	@Override
	public int px() {
		return px;
	}

	@Override
	public int py() {
		return py;
	}

	@Override
	public double toPixel(double x) {
		return x * cellSize / zoom;
	}

	@Override
	public double toMetricPixel(double x) {
		return x * pixelsPerMetricCell;
	}

	@Override
	public double mouseViewportX() {
		return (px - oX) / zoom;
	}

	@Override
	public double mouseViewportY() {
		return (py - oY) / zoom;
	}

	public void cellSize() {
		int min;
		// Pour laisser de la place pour l'HUD
		int height = (int) (m_canvas.getHeight() * 0.9);

		if (height / m_model.nrows() < m_canvas.getWidth() / m_model.ncols()) {
			min = height;
			cellSize = min / m_model.nrows();
		} else {
			min = m_canvas.getWidth();
			cellSize = min / m_model.ncols();
		}
	}

	public void paint(Canvas canvas, Graphics2D g) {
		cellSize();
		g.setColor(java.awt.Color.LIGHT_GRAY);
		g.fillRect(0, 0, m_canvas.getWidth(), m_canvas.getHeight());
		g.translate(oX, oY);
		g.setColor(java.awt.Color.GRAY);
		g.fillRect(0, 0, m_model.ncols() * pxPerMeter() * zoom, m_model.nrows() * pxPerMeter() * zoom);
		subPaint(canvas, g);

		if (debug) {
			debugMode(g);
		}

		g.scale(zoom, zoom);
	}

	public abstract void subPaint(Canvas canvas, Graphics2D g);

	@Override
	public void debug() {
		debug = !debug;
	}

	@Override
	public void death(Entity e) {
		m_visibleAvatars.remove(e.avatar);
		m_model.player().setScore(e.bot.getPointsValue());
	}

// ------------- PRIVATE METHODS ------------- //
	private void debugMode(Graphics2D g) {
		drawGrid(g);
		drawScoreBots(g);

	}

	private void drawGrid(Graphics2D g) {
		int ligne = 1;
		g.setColor(java.awt.Color.WHITE);

		for (int i = 0; i <= m_model.nrows(); i++) {
			int y = i * cellSize * zoom;
			g.fillRect(0, y, cellSize * zoom * m_model.ncols() + ligne, ligne);
		}
		for (int i = 0; i <= m_model.ncols(); i++) {
			int x = i * cellSize * zoom;
			g.fillRect(x, 0, ligne, cellSize * zoom * m_model.nrows() + ligne);
		}
	}

	private void drawScoreBots(Graphics2D g) {
		for (Avatar avatar : m_visibleAvatars) {
			Entity entity = avatar.e;

			if (entity instanceof Player || entity instanceof Bullet)
				// NE PAS AFFICHER LA BARRE DE VIE
				continue;

			int currentScore = entity.bot.getHP();

			// on récupère les coordonnées du bots pour placer la barre au dessus de lui
			int posX = (int) (entity.col() * cellSize);
			int posY = (int) (entity.row() * cellSize);

			int barX = posX;
			int barY = posY - 10;

			// TODO : affiche en rouge la barre si entité vivante, grise si morte
			// a modifier par la suite (attente de gestion de vie/collision des bots)
			if (currentScore > 0) {
				g.setColor(java.awt.Color.RED);
			} else {
				g.setColor(java.awt.Color.GRAY);
			}
			g.fillRect(barX, barY, cellSize, 6);

			g.setColor(java.awt.Color.YELLOW);
			g.drawString(String.valueOf(currentScore), barX, barY - 2);

		}
	}

}
