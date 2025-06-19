package entities;

import bot.ShooterBot;
import engine.model.Model;
import engine.model.PNJ;
import stunts.StuntShooter;

public class Shooter extends PNJ {

	public Shooter(Model m, int r, int c, int o) {
		super(m, r, c, o);
		new StuntShooter(m_model, this);
		new ShooterBot(m.getBrain(), this);
	}

}
