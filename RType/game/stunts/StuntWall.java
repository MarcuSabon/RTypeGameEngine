package stunts;

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
		return m.move(e, nrows, ncols);
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

}