package Avatars;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.geom.AffineTransform;

import engine.model.Entity;
import engine.view.Avatar;
import engine.view.View;

public class AvatarPlayer extends Avatar {
	public Color color;

	public AvatarPlayer(View v, Entity e) {
		super(v, e);
	}

	@Override
	public void render(Graphics2D g) {
		double posY = e.y();
		double posX = e.x();
		double d = e.orientation();

		double rot = Math.toRadians(d);
		Color oldColor = g.getColor();
		AffineTransform saved = g.getTransform();
		g.translate(posX * v.pxPerMeter(), posY * v.pxPerMeter());
		g.rotate(rot);
		g.setColor(java.awt.Color.WHITE);
		g.fillPolygon(triangle());
		g.setTransform(saved);
		g.setColor(oldColor);
	}

	private Polygon triangle() {
		Polygon triangle;

		triangle = new Polygon();

		int cellsize = v.pxPerMeter();
		int y1 = 0, x1 = cellsize / 3;
		int y2 = cellsize / 3, x2 = -cellsize / 3;
		int y3 = -cellsize / 3, x3 = -cellsize / 3;

		triangle.addPoint(x1, y1);
		triangle.addPoint(x2, y2);
		triangle.addPoint(x3, y3);

		return triangle;
	}
}
