package entities;

import bot.TrackerBot;
import engine.model.Model;
import engine.model.PNJ;
import stunts.StuntTracker;

public class Tracker extends PNJ {

	public Tracker(Model m, int r, int c, int o) {
		super(m, r, c, o);
		new StuntTracker(m_model, this);
		new TrackerBot(m.getBrain(), this);
	}

}
