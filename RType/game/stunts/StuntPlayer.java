package stunts;

import engine.IModel;
import engine.brain.Category;
import engine.model.Entity;
import engine.model.Model;
import engine.model.Stunt;
import entities.Bullet;
import entities.Player;

public class StuntPlayer extends Stunt {

	// FIELDS
	private Player p;
	private static final int ROTATION_DURATION = 100;
	private static final int MOVEMENT_DURATION = 250;
	private static final int COLLISION_DURATION = 250;
	private static final int SHOOTING_DURATION = 150;
	protected int U, D, L, R = 0;

	// CONSTRUCTOR
	public StuntPlayer(Model m, Player p) {
		super(m, p);
		this.p = p;
		SPEEDLOSS = 0.001;
	}

	// INNER CLASSES
	class PlayerMotion implements Action {

		private StuntPlayer sp;
		private IModel m;
		private Player p;

		private int nrows, ncols;
		private int delay, duration, step, elapsed;
		private boolean moved;

		PlayerMotion(StuntPlayer sp, int nrows, int ncols, int duration) {
			this.sp = sp;
			this.duration = duration;
			this.nrows = nrows;
			this.ncols = ncols;

			m = sp.model();
			p = sp.entity();
			delay = duration / 2;
		}

		public void tick(int elapsed) {
			this.elapsed += elapsed;

			delay -= elapsed;
			if (delay > 0) {
				updateProgress();
				return;
			}

			switch (step) {
			case 0:
				step++;
				moved = m.move(p, nrows, ncols);
				delay = duration / 2;
				break;
			case 1:
				step++;
				action = null;
				break;
			}
			updateProgress();
		}

		@Override
		public int kind() {
			return 0;
		}

		private void updateProgress() {
			double percent = (double) this.elapsed / duration;
			sp.setProgress(percent);
		}
	}

	class PlayerRotation implements Action {

		private StuntPlayer sp;
		private IModel m;
		private Player p;

		private int angle;
		private int delay, duration, step, elapsed;

		PlayerRotation(StuntPlayer sp, int angle, int duration) {
			this.sp = sp;
			this.angle = angle;
			this.duration = duration;

			m = sp.model();
			p = sp.entity();
			delay = duration / 2;
		}

		public void tick(int elapsed) {
			this.elapsed += elapsed;

			delay -= elapsed;
			if (delay > 0) {
				updateProgress();
				return;
			}

			switch (step) {
			case 0:
				step++;
				p.face(angle);
				delay = duration / 2;
				break;
			case 1:
				step++;
				action = null;
				break;
			}
			updateProgress();
		}

		@Override
		public int kind() {
			return 1;
		}

		private void updateProgress() {
			double percent = (double) this.elapsed / duration;
			sp.setProgress(percent);
		}
	}

	class PlayerRotateAndMove implements Action {

		private StuntPlayer sp;
		private IModel m;
		private Player p;

		private int targetAngle;
		private int nrows, ncols;
		private int delay, step, elapsed, duration;
		private boolean needsRotation, moved;

		PlayerRotateAndMove(StuntPlayer sp, int targetAngle, int nrows, int ncols) {
			this.sp = sp;
			this.targetAngle = targetAngle;
			this.nrows = nrows;
			this.ncols = ncols;

			m = sp.model();
			p = sp.entity();
			needsRotation = (p.orientation() != targetAngle);
			duration = needsRotation ? ROTATION_DURATION / 2 : MOVEMENT_DURATION / 2;
			delay = duration;
		}

		public void tick(int elapsed) {
			this.elapsed += elapsed;

			switch (step) {
			case 0:
				if (needsRotation) {
					// Étape 1: Rotation
					step++;
					p.face(targetAngle);
					delay = ROTATION_DURATION / 2;
				} else {
					// Pas de rotation nécessaire, passer directement au mouvement
					step = 2;
					moved = m.move(e, nrows, ncols);
					delay = MOVEMENT_DURATION / 2;
				}
				break;
			case 1:
				// Fin de la rotation, commencer le mouvement
				step++;
				moved = m.move(e, nrows, ncols);
				delay = MOVEMENT_DURATION / 2;
				break;
			case 2:
				// Fin du mouvement
				step++;
				action = null;
				break;
			}
			updateProgress();
		}

		@Override
		public int kind() {
			return 2;
		}

		private void updateProgress() {
			double percent = (double) this.elapsed / duration;
			sp.setProgress(percent);
		}
	}

	class PlayerCollision implements Action {

		private StuntPlayer sp;
		private Player p;
		private Entity collisionEntity;

		private int delay, step, elapsed, duration;

		PlayerCollision(StuntPlayer sp, Entity collisionEntity, int duration) {
			this.sp = sp;
			this.collisionEntity = collisionEntity;
			this.duration = duration;

			p = sp.entity();
			delay = duration;
		}

		public void tick(int elapsed) {
			this.elapsed += elapsed;

			delay -= elapsed;
			if (delay > 0) {
				updateProgress();
				return;
			}

			switch (step) {
			case 0:
				step++;
				if (collisionEntity != null && collisionEntity.bot.category() != Category.Team) { // si c'est une
																									// collision
																									// avec sa propre
																									// Bullet
																									// -> Pas de dégat
																									// subit
					int damage = collisionEntity.bot.getPointsValue();
					p.inflictHP(damage);
				}
				p.bot.setCollisionWithEntity(null);
				break;
			case 1:
				// Fin du mouvement
				step++;
				action = null;
				break;
			}
			updateProgress();
		}

		@Override
		public int kind() {
			return 3;
		}

		private void updateProgress() {
			double percent = (double) this.elapsed / duration; // Je prends de l'avance sur le fait qu'on a corrigé le
																// pourcentage en double
			sp.setProgress(percent);
		}
	}

	class PlayerShoot implements Action {

		private StuntPlayer sp;

		private int delay, step, elapsed, duration;

		PlayerShoot(StuntPlayer sp, int duration) {
			this.sp = sp;
			this.duration = duration;

			p = sp.entity();
			delay = duration;
		}

		public void tick(int elapsed) {
			this.elapsed += elapsed;

			switch (step) {
			// Étape 0: Tir
			case 0:
				step++;
				sp.shoot();
				break;
			// Étape 1: Attendre un délais avant de finir l'action
			case 1:
				delay -= elapsed;
				if (delay > 0) {
					updateProgress();
					return;
				}
				step++;
				break;
			// Étape 2: Fin de l'action
			case 2:
				// Fin du mouvement
				step++;
				action = null;
				break;
			}
			updateProgress();
		}

		@Override
		public int kind() {
			return 4;
		}

		private void updateProgress() {
			double percent = (double) this.elapsed / duration; // Je prends de l'avance sur le fait qu'on a corrigé le
																// pourcentage en double
			sp.setProgress(percent);
		}
	}

	// --------- PUBLIC METHODS -------------
	public IModel model() {
		return m;
	}

	public void left() {
		moveWithRotation(180, 0, -1); // Ouest
	}

	public void right() {
		moveWithRotation(0, 0, 1); // Est
	}

	public void up() {
		moveWithRotation(270, -1, 0); // Nord
	}

	public void down() {
		moveWithRotation(90, 1, 0); // Sud
	}

	public void rotateRight() {
		int angle = cardinalOfRight(e.orientation());
		rotate(angle);
	}

	public void rotateLeft() {
		int angle = cardinalOfLeft(e.orientation());
		rotate(angle);
	}

	public void U(int u) {
		U = u;
	}

	public void D(int d) {
		D = d;
	}

	public void L(int l) {
		L = l;
	}

	public void R(int r) {
		R = r;
	}

	@Override
	public void tick(int elapsed) {
		super.tick(elapsed);
		updateMove(elapsed);
	}

//  --------------- ACTIONS ----------------
	/*
	 * Move this entity in the model by the given count of rows and columns.
	 */
	// Inutilisé
	public boolean move(int nrows, int ncols) {
		if (action == null) {
			this.action = new PlayerMotion(this, nrows, ncols, MOVEMENT_DURATION);
			return true;
		}
		return false;
	}

	@Override
	// Inutilisé
	public void rotate(int angle) {
		if (action == null) {
			this.action = new PlayerRotation(this, angle, ROTATION_DURATION);
		}
	}

	// Inutilisé
	private boolean moveWithRotation(int targetAngle, int nrows, int ncols) {
		if (action == null) {
			this.action = new PlayerRotateAndMove(this, targetAngle, nrows, ncols);
			return true;
		}
		return false;
	}

	public void playerCollision(Entity entity) {
		if (action == null) {
			this.action = new PlayerCollision(this, entity, COLLISION_DURATION);
		}
	}

	public void playerShoot(Entity entity) {
		if (action == null) {
			this.action = new PlayerShoot(this, SHOOTING_DURATION);
		}
	}

	public Player entity() {
		return p;
	}

	public void shoot() {
		double rad = Math.toRadians(e.orientation());
		int col = (int) (e.col() + Math.cos(rad) + 1);
		int row = (int) (e.row() + Math.sin(rad));

		// On vérifie la case discrète
		if (m.entity(row, col) != null)
			return;

		// On vérifie la case continue
		if (m.entity((int) (e.y() + Math.sin(rad)), (int) (e.x() + Math.cos(rad))) != null)
			return;

		Bullet b = new Bullet(m, row, col, e.orientation(), Category.Team);
		b.at(col + Math.cos(rad), row + Math.sin(rad) + 0.5);
	}

	// --------------- Private methods ----------------

	/*
	 * compute the needed angle to only rotate to a cardinal direction: north,
	 * south, east, west
	 */
	private int cardinalOfRight(int angle) {

		if (0 <= angle && angle < 90) {
			return 90;
		} else if (90 <= angle && angle < 180) {
			return 180;
		} else if (180 <= angle && angle < 270) {
			return 270;
		} else {
			return 0;
		}
	}

	private int cardinalOfLeft(int angle) {
		if (0 < angle && angle <= 90) {
			return 0;
		} else if (90 < angle && angle <= 180) {
			return 90;
		} else if (180 < angle && angle <= 270) {
			return 180;
		} else {
			return 270;
		}
	}

	private void updateMove(int elapsed) {
		e.setAccX(R + L);
		e.setAccY(U + D);

		// e.speedX = R + L;
		// e.speedY = F + B;

		if (e.speedX < 5 && e.getAccX() > 0)
			e.speedX += e.getAccX() * elapsed / ACCNERF;

		if (e.speedX > -5 && e.getAccX() < 0)
			e.speedX += e.getAccX() * elapsed / ACCNERF;

		if (e.speedY < 5 && e.getAccY() > 0)
			e.speedY += e.getAccY() * elapsed / ACCNERF;

		if (e.speedY > -5 && e.getAccY() < 0)
			e.speedY += e.getAccY() * elapsed / ACCNERF;

		// ralenti avec le temps
		e.speedX = e.speedX * (1 - SPEEDLOSS * elapsed);
		e.speedY = e.speedY * (1 - SPEEDLOSS * elapsed);

		if ((e.col() <= 1 && e.speedX < 0) || (e.col() > m.ncols() - 3 && e.speedX > 0)) {
			e.speedX = 0;
		}

		move(realSpeedX() * elapsed, realSpeedY() * elapsed);
	}
}
