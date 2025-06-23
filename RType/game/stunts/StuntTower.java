package stunts;

import engine.model.Model;
import entities.Tower;

public class StuntTower extends StuntShootingPNJ {
	private Tower tower;
	private static final int ROTATION_DURATION = 100;
	private static final int MOVEMENT_DURATION = 250;

	public StuntTower(Model m, Tower tower) {
		super(m, tower);
		this.tower = tower;
	}

}