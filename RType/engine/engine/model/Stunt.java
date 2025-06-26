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
	protected double progress; // pourcentage mathematique entre 0 et 1;
	protected double SPEEDNERF = 600;
	protected double ACCNERF = 60;
	protected double SPEEDLOSS = 0;

	protected Stunt(Model m, Entity e) {
		this.e = e;
		this.m = m;
		e.stunt = this;
	}

	public double progress() {
		return progress;
	}

	public void setProgress(double progress) {
		this.progress = progress;
	}

	public Action action() {
		return action;
	}

	public void abortAction() {
		this.action = null;
	}

	public abstract boolean move(int nrows, int ncols);

	public abstract void rotate(int angle);

	public boolean move(double x, double y) {
		return e.m_model.move(e, x, y);
	}

	public double realSpeedX() {
		return e.speedX / SPEEDNERF;
	}

	public double realSpeedY() {
		return e.speedY / SPEEDNERF;
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
