package Stunts;

import engine.IModel;
import engine.model.Entity;
import engine.model.Model;
import engine.model.Player;
import engine.model.Stunt;
import engine.model.entities.Bullet;

public class StuntPlayer extends Stunt {

	// FIELDS
	private Player p;
	private static final int ROTATION_DURATION = 100;
	private static final int MOVEMENT_DURATION = 250;
	protected int U, D, L, R = 0;

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
			float percent = (float) (this.elapsed / duration);
			sp.setProgress((int) (100 * percent));
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
			float percent = (float) (this.elapsed / duration);
			sp.setProgress((int) (100 * percent));
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

			delay -= elapsed;
			if (delay > 0) {
				updateProgress();
				return;
			}

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
			float percent = (float) (this.elapsed / duration);
			sp.setProgress((int) (100 * percent));
		}
	}

	// CONSTRUCTOR
	public StuntPlayer(Model m, Entity e) {
		super(m, e);
		p = (Player) e;
		SPEEDLOSS = 0.001;
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
		e.setAccX(R + L);
		e.setAccY(U + D);

//		e.speedX = R + L;
//		e.speedY = F + B;

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

		move(e.speedX * elapsed / SPEEDNERF, e.speedY * elapsed / SPEEDNERF);
	}

	/*
	 * Move this entity in the model by the given count of rows and columns.
	 */
	public boolean move(int nrows, int ncols) {
		if (action == null) {
			this.action = new PlayerMotion(this, nrows, ncols, MOVEMENT_DURATION);
			return true;
		}
		return false;
	}

	@Override
	public void rotate(int angle) {
		if (action == null) {
			this.action = new PlayerRotation(this, angle, ROTATION_DURATION);
		}
	}

	private boolean moveWithRotation(int targetAngle, int nrows, int ncols) {
		if (action == null) {
			this.action = new PlayerRotateAndMove(this, targetAngle, nrows, ncols);
			return true;
		}
		return false;
	}

	public void rotateRight() {
		int angle = cardinalOfRight(e.orientation());
		rotate(angle);
	}

	public void rotateLeft() {
		int angle = cardinalOfLeft(e.orientation());
		rotate(angle);
	}

	public Player entity() {
		return p;
	}

	public void shoot() {
		double rad = Math.toRadians(e.orientation());
		double col = p.col() + Math.cos(rad);
		double row = p.row() + Math.sin(rad);
		if (m.entity((int) row, (int) col) == null) {
			if(m.entity((int)(e.y() + Math.sin(rad)), (int)(e.x() + Math.cos(rad)))==null) {
				Bullet b = new Bullet((Model) m, (int) row, (int) col, p.orientation());
				b.at(e.x() + Math.cos(rad), e.y() + Math.sin(rad));
			}
		}
		
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

}
