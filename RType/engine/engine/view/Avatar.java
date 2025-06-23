package engine.view;

import java.awt.Graphics2D;

import engine.model.Entity;

public abstract class Avatar {
	public View v;
	public Entity e;

	protected Avatar(View v, Entity e) {
		this.v = v;
		this.e = e;
		e.avatar = this;
	}

	public abstract void render(Graphics2D g);

}
