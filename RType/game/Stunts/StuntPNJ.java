package Stunts;

import engine.IModel;
import engine.model.Entity;
import engine.model.Model;
import engine.model.PNJ;
import engine.model.Stunt;

public class StuntPNJ extends Stunt {
	private PNJ pnj;
	private static final int ROTATION_DURATION = 100;
	private static final int MOVEMENT_DURATION = 250;

	public class PNJMotion implements Action {

		private StuntPNJ sp;
		private IModel m;
		private PNJ pnj;

		private int nrows, ncols;
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
			float percent = (float) (this.elapsed / duration);
			sp.setProgress((int) (100 * percent));
		}
	}

	public class PNJRotation implements Action {

		private StuntPNJ sp;
		private IModel m;
		private PNJ pnj;

		private int angle;
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
			float percent = (float) (this.elapsed / duration);
			sp.setProgress((int) (100 * percent));
		}
	}

	public class PNJRotateAndMove implements Action {

		private StuntPNJ sp;
		private IModel m;
		private PNJ pnj;

		private int targetAngle;
		private int nrows, ncols;
		private int delay, step, elapsed, duration;
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
					// First step: Rotation
					step++;
					pnj.face(targetAngle);
					delay = ROTATION_DURATION / 2;
				} else {
					// No rotation necessary, start moving immediately
					step = 2;
					moved = m.move(pnj, nrows, ncols);
					delay = MOVEMENT_DURATION / 2;
				}
				break;
			case 1:
				// End of rotation, starting to move
				step++;
				moved = m.move(pnj, nrows, ncols);
				delay = MOVEMENT_DURATION / 2;
				break;
			case 2:
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
			float percent = (float) (this.elapsed / duration);
			sp.setProgress((int) (100 * percent));
		}

	}

	public StuntPNJ(Model m, Entity e) {
		super(m, e);
		pnj = (PNJ) e;
	}

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
}
