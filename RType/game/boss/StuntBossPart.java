package boss;

import engine.model.Model;
import engine.model.PNJ;
import stunts.StuntPNJ;

public class StuntBossPart extends StuntPNJ {

	public StuntBossPart(Model m, PNJ e) {
		super(m, e);
	}

	@Override
	public boolean move(int nrows, int ncols) {
		action = new Motion(this, nrows, ncols);
		return m.move(e, nrows, ncols);
	}

	@Override
	public void rotate(int angle) {
		// TODO Auto-generated method stub
	}

	protected class Motion implements Action {
		private StuntBossPart s;
		private int duration = 200;
		private int remainingDuration = duration;
		private int nrows, ncols;

		protected Motion(StuntBossPart s, int nrows, int ncols) {
			this.s = s;
			this.nrows = nrows;
			this.ncols = ncols;
		}

		@Override
		public void tick(int elapsed) {
			remainingDuration -= elapsed;
			if (s.progress() < 1) {
				float percent = 1 - ((float) remainingDuration / (float) duration);
				s.setProgress(percent);
			} else {
				s.progress = 0;
				action = null;
				remainingDuration = duration;
			}
		}

		@Override
		public int kind() {
			return 0;
		}

		public int getR() {
			return nrows;
		}

		public int getC() {
			return ncols;
		}

	}

	protected class Explosion implements Action {
		private StuntBossPart s;
		private int duration = 1000;
		private int remainingDuration = duration;

		protected Explosion(StuntBossPart s, int nrows, int ncols) {
			this.s = s;
		}

		@Override
		public void tick(int elapsed) {
			remainingDuration -= elapsed;
			if (s.progress() < 1) {
				float percent = 1 - ((float) remainingDuration / (float) duration);
				s.setProgress(percent);
			} else {
				s.progress = 0;
				action = null;
				remainingDuration = duration;
			}
		}

		@Override
		public int kind() {
			return 0;
		}
	}

	public void spawn(int nrows, int ncols) {
		action = new Spawn(this, nrows, ncols);
	}

	public void explode() {
		// TODO Auto-generated method stub

	}

	protected class Spawn implements Action {
		private StuntBossPart s;
		private int duration = 2000;
		private int remainingDuration = duration;
		private int nrows, ncols;

		protected Spawn(StuntBossPart s, int nrows, int ncols) {
			this.s = s;
			this.nrows = nrows;
			this.ncols = ncols;
		}

		@Override
		public void tick(int elapsed) {
			remainingDuration -= elapsed;
			if (s.progress() < 1) {
				float percent = 1 - ((float) remainingDuration / (float) duration);
				s.setProgress(percent);
			} else {
				s.progress = 0;
				action = null;
				remainingDuration = duration;
			}
		}

		@Override
		public int kind() {
			return 1;
		}

		public int getR() {
			return nrows;
		}

		public int getC() {
			return ncols;
		}

	}

}
