package boss;

import engine.model.Entity;
import engine.model.Model;
import entities.Bullet;
import entities.Missile;
import stunts.StuntShootingPNJ;

public class StuntMaster extends StuntShootingPNJ {

	public StuntMaster(Model m, Master e) {
		super(m, e);
	}

	@Override
	public boolean move(int nrows, int ncols) {
		Entity[][] body = ((Master) e).body();

		if (nrows > 0) {
			moveBy1(1, 0, body);
			nrows--;
		} else if (ncols > 0) {
			moveBy1(0, 1, body);
			ncols--;
		} else if (nrows < 0) {
			moveBy1(-1, 0, body);
			nrows++;
		} else if (ncols < 0) {
			moveBy1(0, -1, body);
			ncols++;
		}
		return false;
	}

	private boolean moveBy1(int nrows, int ncols, Entity[][] body) {
		// TODO : cleanup
		if (!checkMovement(nrows, ncols, body)) {
			return false;
		}
		if (nrows == -1) {
			for (int j = 0; j < body[0].length; j++) {
				for (int i = 0; i < body.length; i++) {
					if (body[i][j] instanceof Master) {
						Mmove(e, -1, 0);
					} else if (body[i][j] != null) {
						body[i][j].stunt.move(-1, 0);
					}
				}
			}

		} else if (ncols == -1) {
			for (int j = 0; j < body.length; j++) {
				for (int i = 0; i < body[0].length; i++) {
					if (body[j][i] instanceof Master) {
						Mmove(e, 0, -1);
					} else if (body[j][i] != null) {
						body[j][i].stunt.move(0, -1);
					}
				}
			}
		} else if (nrows == 1) {
			for (int j = body[0].length - 1; j >= 0; j--) {
				for (int i = body.length - 1; i >= 0; i--) {
					if (body[i][j] instanceof Master) {
						Mmove(e, 1, 0);
					} else if (body[i][j] != null) {
						body[i][j].stunt.move(1, 0);
					}
				}
			}
		} else if (ncols == 1) {
			for (int j = body.length - 1; j >= 0; j--) {
				for (int i = body[0].length - 1; i >= 0; i--) {
					if (body[j][i] instanceof Master) {
						Mmove(e, 0, 1);
					} else if (body[j][i] != null) {
						body[j][i].stunt.move(0, 1);
					}
				}
			}
		}
		return true;
	}

	private boolean checkMovement(int nrows, int ncols, Entity[][] body) {
		for (int i = 0; i < body.length; i++) {
			for (int j = 0; j < body[0].length; j++) { // Si body[0].length compt les entités null
				Entity b = body[i][j];
				if (b != null) {
					Entity ent = m.entity(b.row() + nrows, b.col() + ncols);
					if (!(ent == null || ent instanceof Master || ent instanceof BossPart)) {
						return false;
					}
				}

			}
		}
		return true;

	}

	private boolean Mmove(Entity e, int nrows, int ncols) {
		action = new Motion(this, nrows, ncols);
		return m.move(e, nrows, ncols);
	}

	protected class Motion implements Action {
		private StuntMaster s;
		private int duration = 200;
		private int remainingDuration = duration;
		public final int nrows, ncols;

		protected Motion(StuntMaster s, int nrows, int ncols) {
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
			// TODO Auto-generated method stub
			return 0;
		}

	}

	public void shoot() {
		// TODO : cleanup
		double col = e.col() - 3;
		double row = e.row() + 1;
		if (m.entity((int) row, (int) col) == null) {
			Bullet b = new Bullet(m, (int) row, (int) col, 180);
			Bullet b2 = new Bullet(m, (int) row + 1, (int) col, 130);
			Bullet b3 = new Bullet(m, (int) row - 1, (int) col, 210);
			b.at(col + 0.5, row + 0.5);
			b2.at(col + 0.5, row + 1.5);
			b3.at(col + 0.5, row - 0.5);
		}
	}

	public void Missile() {
		// TODO : cleanup
		double col = e.col() + 2;
		double row = e.row() + 3;
		double col2 = e.col() + 2;
		double row2 = e.row() - 3;
		if (m.entity((int) row, (int) col) == null) {
			Missile mis = new Missile(m, (int) row, (int) col, 90, m.player());
			mis.at(col + 0.5, row + 0.5);
			Missile mis2 = new Missile(m, (int) row2, (int) col2, 270, m.player());
			mis2.at(col2 + 0.5, row2 + 0.5);
		}
	}

}
//Missile b = new Missile(m, (int) row, (int) col, 180);
