package game;

import java.awt.Graphics2D;

import Avatars.AvatarPNJ;
import Avatars.AvatarPlayer;
import Avatars.BulletAvatar;
import engine.IModel;
import engine.model.Entity;
import engine.model.PNJ;
import engine.model.Player;
import engine.model.entities.Bullet;
import engine.view.Avatar;
import engine.view.View;
import oop.graphics.Canvas;

public class View0 extends View {
	private ScrollingBackground background;

	public View0(Canvas canvas, IModel model) {
		super(canvas, model);
		background = new ScrollingBackground(canvas, "/Ressources/space.png");
	}

	@Override
	public void paint(Canvas canvas, Graphics2D g) {
		super.paint(canvas, g);

		background.draw(g);

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
			a = new BulletAvatar(this, e);
		else if (e instanceof PNJ)
			a = new AvatarPNJ(this, e);

		if (a != null)
			m_visibleAvatars.add(a);
		else
			System.err.println("Avatar not found for entity: " + e.getClass().getSimpleName());
	}

}
