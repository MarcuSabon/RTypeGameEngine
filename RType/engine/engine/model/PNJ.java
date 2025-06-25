package engine.model;

import engine.brain.Category;
import entities.Bullet;
import entities.Player;

public abstract class PNJ extends Entity {

	protected PNJ(Model m, int r, int c, int o) {
		super(m, r, c, o);
	}

	protected PNJ(Model m, int r, int c, int o, String s) {
		super(m, r, c, o, s);
	}

	@Override
	protected void collision(Entity entity) {
		bot.setCollision(true);
		if (entity instanceof Player) {
			bot.setNbCollision();
		} else if (entity instanceof Bullet) {
			if (entity.bot.category() == Category.Team) { // Incrémntation de nb_collision que si c'est une
															// balle venant du player
				// Les bots peuvent être tué par des balles provenant d'autre bot, mais ça
				// n'augmente pas notre score
				bot.setNbCollision();
			}
		}
		stunt.abortAction();
	}

}
