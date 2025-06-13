package engine.model;

import engine.IBrain.IBot;

public abstract class Stunt {

	public interface Action {
		void tick(int elapsed);

		int kind();

	}

	protected Model m;
	protected Entity e;
	protected Action action;
	protected int progress;
	protected double SPEEDNERF = 600;
	protected double ACCNERF = 60;
	protected double SPEEDLOSS = 0;

	protected Stunt(Model m, Entity e) {
		this.e = e;
		this.m = m;
		e.stunt = this;
	}

	public int progress() {
		return progress;
	}

	public void setProgress(int progress) {
		this.progress = progress;
	}

	public Action action() {
		return action;
	}

	public abstract boolean move(int nrows, int ncols);

	public abstract void rotate(int angle);

	public void move(double x, double y) {
		e.m_model.move(e, x, y);
	}

	public void tick(int elapsed) {
		if (action == null) {
			IBot bot = e.bot;
			if (bot != null)
				bot.think(elapsed);
		} else
			action.tick(elapsed);
	}
}
