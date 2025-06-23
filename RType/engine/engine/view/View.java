package engine.view;

import java.awt.Graphics2D;
import java.util.LinkedList;
import java.util.List;

import engine.IModel;
import engine.IView;
import engine.model.Entity;
import engine.utils.FPSCounter;
import entities.Bullet;
import entities.Player;
import oop.graphics.Canvas;

public abstract class View implements IView {

	protected Canvas m_canvas;
	protected IModel m_model;
	protected int px;
	protected int py;
	protected int cellSize;
	protected int zoom = 1;
	protected double oX;
	protected double oY;
	protected double speedX;
	protected double speedY;
	protected double pixelsPerMetricCell;
	protected boolean debug;
	protected List<Avatar> m_visibleAvatars;

	private FPSCounter fpsCounter;

	protected static final int MAX_ZOOM = 15;

	protected View(Canvas canvas, IModel model) {
		m_canvas = canvas;
		m_model = model;
		model.register(this);
		m_visibleAvatars = new LinkedList<Avatar>();
		pixelsPerMetricCell = cellSize / m_model.metric();
		fpsCounter = new FPSCounter();
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
		oX = -cellSize;
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
		return x * cellSize * zoom;
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

		if (height / m_model.nrows() < m_canvas.getWidth() / m_model.ncols() + 6) {
			min = height;
			cellSize = min * zoom / m_model.nrows();
		} else {
			min = m_canvas.getWidth();
			cellSize = min * zoom / m_model.ncols() + 6;
		}
	}

	@Override
	public void tick(int elapsed) {
		subtick(elapsed);
	}

	public abstract void subtick(int elapsed);

	@Override
	public void paint(Canvas canvas, Graphics2D g) {
		oX = -2 * cellSize;
		cellSize();
		g.translate(oX, oY);
		subPaint(canvas, g);
		if (debug) {
			fpsCounter.frame();
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
		m_model.player().setScore(e);
	}

	// ------------- PRIVATE METHODS ------------- //
	private void debugMode(Graphics2D g) {
		drawEntitie(g);
		drawGrid(g);
		drawScoreBots(g);
		drawFPS(g);
	}

	private void drawFPS(Graphics2D g) {
		int fps = fpsCounter.getFPS();
		g.setColor(java.awt.Color.RED);
		g.drawString("FPS : " + String.valueOf(fps), 10 + cellSize - (int) oX, 35 + cellSize - (int) oY);
	}

	private void drawGrid(Graphics2D g) {
		int ligne = 1;
		g.setColor(java.awt.Color.WHITE);

		for (int i = 0; i <= m_model.nrows(); i++) {
			int y = i * cellSize;
			g.fillRect(0, y, cellSize * m_model.ncols() + ligne, ligne);
		}
		for (int i = 0; i <= m_model.ncols(); i++) {
			int x = i * cellSize;
			g.fillRect(x, 0, ligne, cellSize * m_model.nrows() + ligne);
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
	
	private void drawEntitie(Graphics2D g) {
		int w = m_canvas.getWidth();
		int h = m_canvas.getHeight();
		int nrows = m_model.nrows();
		int ncols = m_model.ncols();
		int cell = Math.min(w / ncols, h / nrows);

		g.setColor(java.awt.Color.CYAN);
		int pointSize = 10;

		for (int row = 0; row < nrows; row++) {
			for (int col = 0; col < ncols; col++) {
				if (m_model.entity(row, col) != null) {
					int x = col * cell + cell / 2 - pointSize / 2;
					int y = row * cell + cell / 2 - pointSize / 2;
					g.fillOval(x, y, pointSize, pointSize);
				}
			}
		}
	}

}
