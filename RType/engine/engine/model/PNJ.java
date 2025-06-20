package engine.model;

public abstract class PNJ extends Entity {

	protected PNJ(Model m, int r, int c, int o) {
		super(m, r, c, o);
	}

	@Override
	protected void collision(Entity entity) {
		bot.setCollision(true);
		System.out.println("Collision avec " + entity.getClass().getSimpleName());
	}

}
