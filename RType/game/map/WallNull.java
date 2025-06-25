package map;

import engine.model.Entity;
import engine.model.Model;

public class WallNull extends Entity {

	public WallNull(Model m) {
		super(m, 0, 0, 0);
		// new StuntWall(m, this);
		// new BotWall(m.getBrain(), this);
		die();
	}

	@Override
	public void collision(Entity entity) {
		// Le mur ne réagit pas, mais on pourrait ajouter un son, un effet...
	}

}
