package stunts;

import engine.model.Model;
import entities.Bullet;
import entities.Shooter;

public class StuntShooter extends StuntPNJ {
	private Shooter shooter;
	private static final int ROTATION_DURATION = 100;
	private static final int MOVEMENT_DURATION = 250;

	public StuntShooter(Model m, Shooter shooter) {
		super(m, shooter);
		this.shooter = shooter;
	}

	public void shoot() {
		double rad = Math.toRadians(e.orientation());
		double col = e.col() + Math.cos(rad);
		double row = e.row() + Math.sin(rad);
		if (m.entity((int) row, (int) col) == null) {
			if (m.entity((int) (e.y() + Math.sin(rad)), (int) (e.x() + Math.cos(rad))) == null) {
				Bullet b = new Bullet((Model) m, (int) row, (int) col, e.orientation());
				b.at(col + Math.cos(rad), row + Math.sin(rad)); // y'avait un problème, la bullet spawnait toujours en
																// 16,16 -> pq la position ne s'updatait pas (jsp)
			}
		}

	}
}
