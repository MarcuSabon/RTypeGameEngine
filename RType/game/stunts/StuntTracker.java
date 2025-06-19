package stunts;

import engine.model.Model;
import entities.Tracker;

public class StuntTracker extends StuntPNJ {
	private Tracker tracker;
	private static final int ROTATION_DURATION = 100;
	private static final int MOVEMENT_DURATION = 250;

	public StuntTracker(Model m, Tracker tracker) {
		super(m, tracker);
		this.tracker = tracker;
	}

}
