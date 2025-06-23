package stunts;

import engine.model.Model;
import entities.Shooter;

public class StuntShooter extends StuntShootingPNJ {
	private Shooter shooter;

	public StuntShooter(Model m, Shooter shooter) {
		super(m, shooter);
		this.shooter = shooter;
	}

}
