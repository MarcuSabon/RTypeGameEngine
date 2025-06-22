package map;

import bot.BotWall;
import engine.model.Entity;
import engine.model.Model;
import stunts.StuntWall;

public class Wall extends Entity {

	public Wall(Model m, int r, int c, int o) {
		super(m, r, c, o);
		stunt = new StuntWall(m, this);
		new BotWall(m.getBrain(), this);
	}

	@Override
	public void collision(Entity entity) {
		// Le mur ne réagit pas, mais on pourrait ajouter un son, un effet...
	}

}
