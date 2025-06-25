package stunts;

import engine.IModel;
import engine.model.Entity;
import engine.model.Model;
import engine.model.PNJ;
import engine.model.Stunt;

public abstract class StuntPNJ extends Stunt {
	private PNJ pnj;
	private static final int ROTATION_DURATION = 50;
	private static final int MOVEMENT_DURATION = 150;
	private static final int COLLISION_DURATION = 250;

	// CONSTRUCTOR
	public StuntPNJ(Model m, PNJ e) {
		super(m, e);
		pnj = e;
	}

	// INNER CLASSES
	public class PNJMotion implements Action {

		private StuntPNJ sp;
		private IModel m;
		private PNJ pnj;

		public final int nrows, ncols;
		private int delay, duration, step, elapsed;
		private boolean moved;

		public PNJMotion(StuntPNJ sp, int nrows, int ncols, int duration) {
			this.sp = sp;
			this.nrows = nrows;
			this.ncols = ncols;
			this.duration = duration;

			m = sp.model();
			pnj = sp.entity();
			delay = duration / 2;
		}

		public void tick(int elapsed) {
			System.out.println(progress);
			this.elapsed += elapsed;

			delay -= elapsed;
			if (delay > 0) {
				updateProgress();
				return;
			}

			switch (step) {
				case 0:
					step++;
					moved = m.move(pnj, nrows, ncols);
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

		public int getR() {
			return nrows;
		}

		public int getC() {
			return ncols;
		}

		public boolean hasMoved() {
			return moved;
		}
	}

	public class PNJRotation implements Action {

		private StuntPNJ sp;
		private IModel m;
		private PNJ pnj;

		public final int angle;
		private int delay, duration, step, elapsed;

		public PNJRotation(StuntPNJ sp, int angle, int duration) {
			this.sp = sp;
			this.angle = angle;
			this.duration = duration;

			m = sp.model();
			pnj = sp.entity();
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
					pnj.face(angle + pnj.orientation());
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

	public class PNJRotateAndMove implements Action {

		private StuntPNJ sp;
		private IModel m;
		private PNJ pnj;

		public final int targetAngle;
		public final int nrows, ncols;

		public boolean movedDone = false;
		private int delay, step, elapsed;
		private final int rotationDuration, moveDuration;
		private final int totalDuration;
		private boolean needsRotation, moved;

		public PNJRotateAndMove(StuntPNJ sp, int targetAngle, int nrows, int ncols) {
			this.sp = sp;
			this.targetAngle = targetAngle;
			this.nrows = nrows;
			this.ncols = ncols;

			m = sp.model();
			pnj = sp.entity();
			// Vérifier si une rotation est nécessaire
			needsRotation = (pnj.orientation() != targetAngle);
			rotationDuration = needsRotation ? ROTATION_DURATION : 0;
			moveDuration = MOVEMENT_DURATION;
			totalDuration = rotationDuration + moveDuration;

			delay = needsRotation ? rotationDuration / 2 : 0;
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
					// First step: Rotation
					pnj.face(targetAngle); // On tourne directement
					step++;
					delay = ROTATION_DURATION / 2;
				} else {
					// No rotation necessary, start moving immediately
					step = 2;
					delay = MOVEMENT_DURATION / 2;
				}
				break;
			case 1:
				// End of rotation, starting to move
				step++;
				delay = MOVEMENT_DURATION / 2;
				break;
			case 2:
				// End of rotation, starting to move
				step++;
				moved = m.move(pnj, nrows, ncols);
				movedDone = true;
				delay = MOVEMENT_DURATION / 2;
				break;
			case 3:
				// End of movement
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
			double percent = Math.min(1.0, (double) elapsed / totalDuration);
			sp.setProgress(percent);
		}

		public boolean moved() {
			return moved;
		}

		public boolean isRotating() {
			return needsRotation && elapsed < rotationDuration;
		}

		public boolean isMoving() {
			if (!needsRotation) {
				return elapsed < moveDuration;
			}
			return elapsed >= rotationDuration && elapsed < totalDuration;
		}

		public double getRotationProgress() {
			if (!needsRotation)
				return 1.0;
			if (!isRotating())
				return 1.0;
			return Math.min(1.0, (double) elapsed / rotationDuration);
		}

		public double getMoveProgress() {
			if (!needsRotation) {
				return Math.min(1.0, (double) elapsed / moveDuration);
			}
			if (!isMoving())
				return 0.0;
			return Math.min(1.0, (double) (elapsed - rotationDuration) / moveDuration);
		}
	}

	class PNJCollision implements Action {

		private StuntPNJ sp;
		private PNJ p;
		private Entity collisionEntity;

		private int delay, step, elapsed, duration;

		PNJCollision(StuntPNJ sp, Entity collisionEntity, int duration) {
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

	// PUBLIC METHODS
	public PNJ entity() {
		return pnj;
	}

	public IModel model() {
		return m;
	}

	@Override
	public void rotate(int angle) {
		if (action == null)
			this.action = new PNJRotation(this, angle, ROTATION_DURATION);
	}

	@Override
	public boolean move(int nrows, int ncols) {
		if (action == null) {
			this.action = new PNJMotion(this, nrows, ncols, MOVEMENT_DURATION);
			return true;
		}
		return false;
	}

	public boolean moveAndRotate(int targetAngle, int nrows, int ncols) {
		if (action == null) {
			this.action = new PNJRotateAndMove(this, targetAngle, nrows, ncols);
			return true;
		}
		return false;
	}

	public void PNJCollision(Entity entity) {
		if (action == null) {
			this.action = new PNJCollision(this, entity, COLLISION_DURATION);
		}
	}
}
