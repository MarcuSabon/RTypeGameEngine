package stunts;

import engine.model.Model;
import engine.model.PNJ;
import entities.ShootingPNJ;
import sound.SoundPlayer;

public class StuntShootingPNJ extends StuntPNJ {

	public StuntShootingPNJ(Model m, PNJ e) {
		super(m, e);
	}

	public void shoot(ShootingPNJ e) {
		double rad = Math.toRadians(e.orientation());
		int col = (int) (e.col() + Math.cos(rad));
		int row = (int) (e.row() + Math.sin(rad));

		// On vérifie la case discrète
		if (m.entity(row, col) != null)
			return;

		// On vérifie la case continue
		if (m.entity((int) (e.y() + Math.sin(rad)), (int) (e.x() + Math.cos(rad))) != null)
			return;

		e.shoot(row, col, e.orientation(), rad);
	}

}
