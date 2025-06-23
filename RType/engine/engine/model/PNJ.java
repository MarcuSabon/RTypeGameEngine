package engine.model;

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
		stunt.abortAction();
		System.out.println("Action aborted " + this.getClass().getSimpleName());
		// System.out.println("Collision avec " + entity.getClass().getSimpleName());
	}

}
