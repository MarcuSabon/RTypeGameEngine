package stunts;

import engine.IModel;
import engine.model.Entity;
import engine.model.Model;
import engine.model.Stunt;
import entities.Player;
import map.Wall;

public class StuntWall extends Stunt {

	public StuntWall(Model m, Entity e) {
		super(m, e);
	}

	@Override
	public boolean move(int nrows, int ncols) {
		Entity pushed = m.entity(e.row(), e.col() - 1);
		pushRec(pushed, 10);
		action = new Motion(this, nrows, ncols, 490);
		m.move(e, nrows, ncols);
		return true;
	}

	public void pushRec(Entity e, int depth) {
		if (e == null || e instanceof Wall || depth <= 0) {
			return;
		}

		Entity erec = m.entity(e.row(), e.col() - 1);
		pushRec(erec, depth - 1);

		if (e instanceof Player) {
			m.move(e, (double) -1, (double) 0);
		} else if (e != null && !(e instanceof Wall)) {
			m.move(e, 0, -1);
		}
	}

	@Override
	public void rotate(int angle) {
		// nothing
	}

	// INNER CLASSES
	public class Motion implements Action {

		private StuntWall sp;
		private IModel m;
		private Wall wall;

		private int nrows, ncols;
		private int delay, duration, step, elapsed;
		private boolean moved;

		public Motion(StuntWall sp, int nrows, int ncols, int duration) {
			this.sp = sp;
			this.nrows = nrows;
			this.ncols = ncols;
			this.duration = duration;

			m = sp.m;
			wall = (Wall) sp.e;
			delay = duration / 2;
		}

		public void tick(int elapsed) {
			this.elapsed += elapsed;
			delay -= elapsed;
			if (delay > 0) {
				updateProgress();
				return;
			}
			updateProgress();
		}

		@Override
		public int kind() {
			return 0;
		}

		private void updateProgress() {
			double percent = (double) this.elapsed / duration;
			if (percent > 2) {
				percent = 2;
			}
			sp.setProgress(percent);
		}

		public int getC() {
			return ncols;
		}

		public int getR() {
			return nrows;
		}
	}

}