package game;

import java.awt.Graphics2D;

import avatars.AvatarPNJ;
import avatars.AvatarPlayer;
import avatars.AvatarShooter;
import avatars.AvatarTracker;
import avatars.AvatarBullet;
import engine.IModel;
import engine.model.Entity;
import engine.view.Avatar;
import engine.view.View;
import entities.BasicPNJ;
import entities.Bullet;
import entities.Player;
import entities.Shooter;
import entities.Tracker;
import oop.graphics.Canvas;

public class View0 extends View {
	private ScrollingBackground background;
	private ViewBar viewBar;

	public View0(Canvas canvas, IModel model) {
		super(canvas, model);
		background = new ScrollingBackground(canvas, "/space.png");
		viewBar = new ViewBar(canvas, "/retroGaming.ttf");
	}

	@Override
	public void subPaint(Canvas canvas, Graphics2D g) {
		background.draw(g);
		p = m_model.player();
		viewBar.draw(g, p);
		g.scale(zoom, zoom);

		for (Avatar a : m_visibleAvatars)
			a.render(g);
	}

	@Override
	public void tick(int elapsed) {
		background.update(elapsed);
	}

	@Override
	public void birth(Entity e) {
		Avatar a = null;
		if (e instanceof Player)
			a = new AvatarPlayer(this, e);
		else if (e instanceof Bullet)
			a = new AvatarBullet(this, e);
		else if (e instanceof Tracker)
			a = new AvatarTracker(this, e);
		else if (e instanceof Shooter)
			a = new AvatarShooter(this, e);
		else if (e instanceof BasicPNJ)
			a = new AvatarPNJ(this, e);

		if (a != null)
			m_visibleAvatars.add(a);
		else
			System.err.println("Avatar not found for entity: " + e.getClass().getSimpleName());
	}

}
