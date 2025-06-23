package engine.model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import engine.IModel;
import engine.IView;
import engine.brain.Brain;
import entities.Player;

public class Model implements IModel {
	private int m_ncols, m_nrows;
	private Entity[][] m_grid;
	private Player m_player;
	private List<Entity> m_entities;
	private List<Entity> toAdd;
	private IView m_view;
	private Brain m_brain;
	private Config m_conf;
	private int m_metric;
	private double m_xsize, m_ysize;

	public Model(int nr, int nc) {
		m_ncols = nc;
		m_nrows = nr;
		m_grid = new Entity[nr][nc];
		m_entities = new LinkedList<Entity>();
		toAdd = new ArrayList<>(); // De la même façon que pour toRemove, on delay l'ajout d'une entity à la liste
									// pour éviter le ConcurrentModificationException
		m_metric = 1;
		m_xsize = m_ncols * m_metric;
		m_ysize = m_nrows * m_metric;
	}

	/*
	 * A callback from the constructor of an entity to notify this model of a new
	 * entity to add to this model.
	 */
	public void add(Entity e) {
		assert (m_grid[e.m_row][e.m_col] != null) : "There is already an entity at (" + e.m_row + "," + e.m_col + ")";

		m_grid[e.m_row][e.m_col] = e;
		m_entities.add(e);
		m_view.birth(e);
	}

	public void addAt(Entity e) {
		toAdd.add(e);
	}

	public void setPlayer(Player p) {
		assert (m_player != null) : "Player is already set";
		m_player = p;
	}

	@Override
	public void register(IView v) {
		assert (v == null) : "Cannot set a null view to the model";
		m_view = v;
	}

	@Override
	public void unregister(IView v) {
		assert (v == null) : "Cannot unset a null view to the model";
		m_view = null;
	}

	@Override
	public void cerebrate(Brain b) {
		assert (b == null) : "Cannot set a null view to the model";
		m_brain = b;
	}

	@Override
	public void decerebrate(Brain b) {
		assert (b == null) : "Cannot set a null view to the model";
		m_brain = b;
	}

	/*
	 * Move the given entity from its current location by adding the given number of
	 * rows and columns to its current location.
	 */
	public boolean move(Entity e, int nrows, int ncols) {
		int oldRow = e.m_row;
		int oldCol = e.m_col;
		int newRow = oldRow + nrows;
		int newCol = oldCol + ncols;

		newRow = normalize(newRow, m_nrows);
		newCol = normalize(newCol, m_ncols);

		if (newRow == oldRow && newCol == oldCol)
			return false; // No movement

		Entity entity = entity(newRow, newCol);

		if (entity(newRow, newCol) == null) {
			m_grid[e.m_row][e.m_col] = null; // remove from old position

			e.at(newRow, newCol); // update entity position
			if (!e.isDead()) {
				m_grid[newRow][newCol] = e; // add to new position
			}
			return true;
		}
		e.collision(entity);
		entity.collision(e);
		return false;
	}

	/*
	 * Move the given entity from its current location by adding the given number of
	 * rows and columns to its current location.
	 */
	/*
	 * Move the given entity from its current location by adding the given number of
	 * rows and columns to its current location.
	 */
	public void move(Entity e, double x, double y) {
		double oldX = e.x;
		double oldY = e.y;
		double tempX = x + e.x;
		double tempY = y + e.y;

		tempY = normalize(tempY, m_ysize);
		tempX = normalize(tempX, m_xsize);

		if (tempX == oldX && tempY == oldY)
			return; // No movement

		boolean left = isEntityAtLeftEdge(e);
		boolean right = isEntityAtRightEdge(e);
		boolean up = isEntityAtTopEdge(e);
		boolean down = isEntityAtBottomEdge(e);

		// Gestion des collisions selon la direction
		if (up && y < 0) {
			if (handleUpwardCollisions(e, left, right)) {
				tempY = e.y;
				e.speedY = 0;
			}
		}

		else if (down && y > 0) {
			if (handleDownwardCollisions(e, left, right)) {
				tempY = e.y;
				e.speedY = 0;
			}
		}

		if (right && x > 0) {
			if (handleRightwardCollisions(e, up, down)) {
				tempX = e.x;
				e.speedX = 0;
			}
		}

		else if (left && x < 0) {
			if (handleLeftwardCollisions(e, up, down)) {
				tempX = e.x;
				e.speedX = 0;
			}
		}
//		System.out.println(tempX+ " "+tempY);
		updateEntityPosition(e, tempX, tempY);
	}

	/*
	 * Normalize a number back to the range [0,lengh[
	 */
	private int normalize(int n, int length) {
		if (m_conf.tore)
			return ((n % length) + length) % length;
		else
			return n = n < 0 ? Math.max(n, 0) : Math.min(n, length - 1);
	}

	private double normalize(double n, double length) {
		if (m_conf.tore)
			return ((n % length) + length) % length;
		else
			return n = n < 0 ? Math.max(n, 0) : Math.min(n, length);
	}

	@Override
	public Entity entity(int r, int c) {
		if (!m_conf.tore) {
			if (r > m_grid.length - 1 || r < 0 || c > m_grid[0].length - 1 || c < 0) {
				// TODO!m_player a changer par un mur quand mur existe!
				return null;
			}
		}
		return m_grid[normalize(r, m_nrows)][normalize(c, m_ncols)];
	}

	@Override
	public Iterator<Entity> entities() {
		return m_entities.iterator();
	}

	@Override
	public Config config() {
		return m_conf;
	}

	@Override
	public void config(Config c) {
		m_conf = c;
	}

	@Override
	public Brain getBrain() {
		return m_brain;
	}

	@Override
	public Player player() {
		return m_player;
	}

	@Override
	public int ncols() {
		return m_ncols;
	}

	@Override
	public int nrows() {
		return m_nrows;
	}

	@Override
	public double metric() {
		return m_metric;
	}

	@Override
	public int toCellCoordinate(double x) {
		return (int) (x / m_metric);
	}

	@Override
	public void tick(int elapsed) {
		List<Entity> toRemove = new ArrayList<>();
		for (Entity e : m_entities) {
			if (!e.isDead() && e.stunt != null)
				e.stunt.tick(elapsed);
			if (e.isDead())
				toRemove.add(e);
		}

		for (Entity e : toAdd)
			add(e);

		toAdd.clear();
		for (Entity e : toRemove)
			death(e);

	}

	public void death(Entity entity) {
		m_grid[entity.m_row][entity.m_col] = null;
		m_entities.remove(entity);
		m_view.death(entity);
	}

	// ---------- Private methods -------------

	private boolean isEntityAtLeftEdge(Entity e) {
		double fractionalPart = (e.x / m_metric) % 1;
		return (fractionalPart > 0 && fractionalPart < 0.2);
	}

	private boolean isEntityAtRightEdge(Entity e) {
		double fractionalPart = (e.x / m_metric) % 1;
		return (fractionalPart > 0.8);
	}

	private boolean isEntityAtTopEdge(Entity e) {
		double fractionalPart = (e.y / m_metric) % 1;
		return (fractionalPart > 0 && fractionalPart < 0.2);
	}

	private boolean isEntityAtBottomEdge(Entity e) {
		double fractionalPart = (e.y / m_metric) % 1;
		return (fractionalPart > 0.8);
	}

	private boolean handleUpwardCollisions(Entity e, boolean left, boolean right) {
		// Collision directe vers le haut
		int row = normalize(toCellCoordinate(e.y), m_nrows);
		int col = normalize(toCellCoordinate(e.x), m_ncols);

		Entity upward = entity(row - 1, col); // variables pour codes plus lisible
		Entity upwardLeft = entity(row - 1, col - 1);
		Entity upwardRight = entity(row - 1, col + 1);

		if (upward != null) {
			e.collision(upward);
			upward.collision(e); // on informe l'entité dans laquelle on est entré en collision qu'il y
									// a eu une collision
			return true;
		}

		// Collision diagonale haut-gauche
		if (left && upwardLeft != null) {
			e.collision(upwardLeft);
			upwardLeft.collision(e);
			return true;
		}

		// Collision diagonale haut-droite
		if (right && upwardRight != null) {
			e.collision(upwardRight);
			upwardRight.collision(e);
			return true;
		}
		return false;
	}

	private boolean handleDownwardCollisions(Entity e, boolean left, boolean right) {
		int row = normalize(toCellCoordinate(e.y), m_nrows);
		int col = normalize(toCellCoordinate(e.x), m_ncols);

		Entity downward = entity(row + 1, col);
		Entity downwardLeft = entity(row + 1, col - 1);
		Entity downwardRight = entity(row + 1, col + 1);
		// Collision directe vers le bas
		if (downward != null) {
			e.collision(downward);
			downward.collision(e);
			return true;
		}

		// Collision diagonale bas-gauche
		if (left && downwardLeft != null) {
			e.collision(downwardLeft);
			downwardLeft.collision(e);
			return true;
		}

		// Collision diagonale bas-droite
		if (right && downwardRight != null) {
			e.collision(downwardRight);
			downwardRight.collision(e);
			return true;
		}
		return false;
	}

	private boolean handleRightwardCollisions(Entity e, boolean up, boolean down) {
		int row = normalize(toCellCoordinate(e.y), m_nrows);
		int col = normalize(toCellCoordinate(e.x), m_ncols);

		Entity right = entity(row, col + 1);
		Entity rightUp = entity(row - 1, col + 1);
		Entity rightDown = entity(row + 1, col + 1);

		// Collision directe vers la droite
		if (right != null) {
			e.collision(right);
			right.collision(e);
			return true;
		}

		// Collision diagonale droite-haut
		if (up && rightUp != null) {
			e.collision(rightUp);
			rightUp.collision(e);
			return true;
		}

		// Collision diagonale droite-bas
		if (down && rightDown != null) {
			e.collision(rightDown);
			rightDown.collision(e);
			return true;
		}
		return false;
	}

	private boolean handleLeftwardCollisions(Entity e, boolean up, boolean down) {
		int row = normalize(toCellCoordinate(e.y), m_nrows);
		int col = normalize(toCellCoordinate(e.x), m_ncols);

		Entity left = entity(row, col - 1);
		Entity leftUp = entity(row - 1, col - 1);
		Entity leftDown = entity(row + 1, col - 1);

		// Collision directe vers la gauche
		if (left != null) {
			e.collision(left);
			left.collision(e);
			return true;
		}

		// Collision diagonale gauche-haut
		if (up && leftUp != null) {
			e.collision(leftUp);
			leftUp.collision(e);
			return true;
		}

		// Collision diagonale gauche-bas
		if (down && leftDown != null) {
			e.collision(leftDown);
			leftDown.collision(e);
			return true;
		}
		return false;
	}

	private void updateEntityPosition(Entity e, double newX, double newY) {
		m_grid[e.row()][e.col()] = null;
		int newRow = normalize(toCellCoordinate(newY), m_nrows);
		int newCol = normalize(toCellCoordinate(newX), m_ncols);
		m_grid[newRow][newCol] = e;
		e.at(newX, newY, newRow, newCol);
	}

	public void emptyGrid(int row, int col) {
		m_grid[row][col] = null;
	}

}
