package engine.brain;

import engine.IBrain.IBot;
import engine.model.Entity;
import engine.utils.Utils;
import entities.Player;
import stunts.StuntPNJ;
import stunts.StuntPlayer;
import stunts.StuntShooter;

public abstract class Bot implements IBot {
	protected Brain b;
	protected Entity e;
	protected Category c;
	protected Mode mode;
	protected FSM fsm;

	// champs valeur d'un bot pour le score du Player
	protected int pointsValue; // J'ai mis dans bot et pas Entity pcq la difficulté à tuer l'entité dépend de
								// son bot mais on pourrait changer
	protected int HP; // nombre de fois où un bot peut subir des collisions jusqu'à sa mort (à titre
						// simplement informatif pour la vue, n'influe pas sur le comportement des bots)
	protected boolean collision; // booleen qui indique s'il y a eu une collision avec le bot
	protected Entity entityCollisionWithPlayer;

	private boolean wait;
	protected int delay;

	protected Bot(Brain b, Entity e) {
		this.b = b;
		this.e = e;
		e.bot = this;
		b.bots.add(this);
		pointsValue = 0; // valeur par défaut (valeur obstacle), on ajoute + dans le consructeur des bots
							// ennemis
		HP = 1; // les entités de bases ont 1HP
		collision = false;

		delay = 500; // 0,5 second by default
	}

	@Override
	public void think(int elapsed) {
		if (wait) {
			delay -= elapsed;
			wait = (delay > 0);
		} else if (!wait) {
			think();
			wait = (delay > 0);
		}
	}

	public abstract void think();

	@Override
	public Category category() {
		return c;
	}

	@Override
	public void setFSM(FSM fsm) {
		this.fsm = fsm;
	}

	@Override
	public int getPointsValue() {
		return pointsValue;
	}

	@Override
	public int getHP() { // Mio c'est cette méthode que tu vas utiliser
							// à la place de ton getScore() normalement ;)
		return HP;
	}

	@Override
	public void setCollision(boolean b) { // Permet à l'entité de set collision à true lorsqu'il y a une collision
		this.collision = b;
	}

	@Override
	public boolean collision(Entity e) { // Quand on a enregistrer une collision on repasse collision à false car
		// plusieurs collisions sont possibles pour un même bot
		boolean tmp = collision;
		collision = false;
		return tmp;
	}

	@Override
	public void setCollisionWithEntity(Entity e) { // Permet au bot du PLayer de savoir avec quelle entité il a eu une
													// collision
		entityCollisionWithPlayer = e;
	}

	@Override
	public Entity getCollisionWithEntity() { // Permet au bot du Player de savoir avec quelle entité il a eu une
												// collision
		return entityCollisionWithPlayer;
	}

	// ---------------- Protected ------------------------
	protected void turn(Direction d) {
		int angle;
		if (d.isRelative())
			angle = d.degrees();
		else
			angle = d.degrees() - e.orientation();
		e.stunt.rotate(angle);
	};

	protected void move(Direction d) {
		Direction cardinal = cardinalOfOrient(e.orientation(), d);

		if (cardinal.equals(Direction.N))
			e.stunt.move(-1, 0);
		else if (cardinal.equals(Direction.E))
			e.stunt.move(0, 1);
		else if (cardinal.equals(Direction.S))
			e.stunt.move(1, 0);
		else if (cardinal.equals(Direction.W))
			e.stunt.move(0, -1);
		else
			throw new IllegalArgumentException("Invalid direction: " + d);
	}

	protected void move(Direction d, int i) {
		Direction cardinal = cardinalOfOrient(e.orientation(), d);

		if (cardinal.equals(Direction.N))
			e.stunt.move(-i, 0);
		else if (cardinal.equals(Direction.E))
			e.stunt.move(0, i);
		else if (cardinal.equals(Direction.S))
			e.stunt.move(i, 0);
		else if (cardinal.equals(Direction.W))
			e.stunt.move(0, -i);
		else
			throw new IllegalArgumentException("Invalid direction: " + d);
	}

	protected Entity cell(Direction d) {
		return cellDistance(d, 1);
	}

	protected Entity cell(Direction d, int radius) {
		if (radius < 1)
			throw new IllegalArgumentException("Radius must be >= 1");

		if (radius == 1)
			return cell(d);

		for (int i = 1; i <= radius; i++) {
			Entity e = cellDistance(d, i);
			if (e != null)
				return e;
		}
		return null;
	}

	protected Entity cell(Direction d, Category c) {
		Entity e = cell(d);

		return e.bot.category().equals(c) ? e : null;
	}

	protected Entity cell(Direction d, Category c, int radius) {
		if (radius < 1)
			throw new IllegalArgumentException("Radius must be >= 1");

		if (radius == 1)
			return cell(d, c);

		for (int i = 1; i <= radius; i++) {
			Entity e = cellDistance(d, i);
			if (e != null && e.bot.category().equals(c))
				return e;
		}
		return null;
	}

	protected void moveWithRotation(Direction d) {
		if (e.stunt instanceof StuntPNJ) {
			Direction cardinal = cardinalOfOrient(e.orientation(), d);

			StuntPNJ stuntPNJ = (StuntPNJ) e.stunt;

			if (cardinal.equals(Direction.N))
				stuntPNJ.moveAndRotate(270, -1, 0);
			else if (cardinal.equals(Direction.E))
				stuntPNJ.moveAndRotate(0, 0, 1);
			else if (cardinal.equals(Direction.S))
				stuntPNJ.moveAndRotate(90, 1, 0);
			else if (cardinal.equals(Direction.W))
				stuntPNJ.moveAndRotate(180, 0, -1);
			else
				throw new IllegalArgumentException("Invalid direction: " + d);
		}
	}

	protected void shoot(Direction d) {
		if (e.stunt instanceof StuntShooter) {
			Direction cardinal = cardinalOfOrient(e.orientation(), d);

			StuntShooter stuntShooter = (StuntShooter) e.stunt;

			if (cardinal.equals(Direction.E))
				stuntShooter.shoot();
			else if (cardinal.equals(Direction.W))
				stuntShooter.shoot();
			else
				throw new IllegalArgumentException("Invalid direction: " + d);
		}
	}

	protected void playerCollision(Player p, Entity e) {
		StuntPlayer sp = (StuntPlayer) p.stunt;
		sp.playerCollision(e);
	}

	protected Entity closest(Category c) {
		// TODO: Implement a method to find the closest entity of the specified category
		return b.model.player();
	}

	protected Direction dirOf(Entity e) {
		int dRow = e.row() - this.e.row();
		int dCol = e.col() - this.e.col();

		int angle = Utils.theta(dCol, dRow);

		return Direction.newAbsolute(angle);
	}

	// ---------------------- Private methods ----------------------

	private Direction cardinalOfOrient(int orientation, Direction d) {
		if (d.isRelative()) {
			int angle = (orientation + d.degrees()) % 360;
			return Direction.newAbsolute(angle);
		} else {
			return d.cardinalOf();
		}
	}

	private Entity cellDistance(Direction d, int distance) {
		Direction cardinal = cardinalOfOrient(e.orientation(), d);

		if (cardinal.equals(Direction.N))
			return b.model.entity(e.row() - distance, e.col());
		else if (cardinal.equals(Direction.E))
			return b.model.entity(e.row(), e.col() + distance);
		else if (cardinal.equals(Direction.S))
			return b.model.entity(e.row() + distance, e.col());
		else if (cardinal.equals(Direction.W))
			return b.model.entity(e.row(), e.col() - distance);
		else
			throw new IllegalArgumentException("Invalid direction: " + d);
	}

}
