package avatars;

import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.geom.AffineTransform;

import engine.model.Entity;
import engine.view.Avatar;
import engine.view.View;

public class AvatarTracker extends Avatar {

	public AvatarTracker(View v, Entity e) {
		super(v, e);
	}

	@Override
	public void render(Graphics2D g) {
		drawTrackerCell(g);
		// You can add more rendering logic here if needed
		// For example, drawing additional information or effects related to the Tracker
		// g.drawString("Tracker Name: " + e.getName(), x, y - 10); // Example text
	}

	// ------------- Private methods -------------//
	private void paintPlayer(Graphics2D g, Entity e, int x, int y, Polygon pg) {
		int d = e.orientation();
		double rot = Math.toRadians(d);
		AffineTransform saved = g.getTransform();
		g.translate(x, y);
		g.rotate(rot);
		g.fillPolygon(pg);
		g.setTransform(saved);
	}

	private void drawTrackerCell(Graphics2D g) {
		int CellWidth = v.pxPerMeter();

		int x = e.col() * CellWidth + CellWidth / 2;
		int y = e.row() * CellWidth + CellWidth / 2;

		// create a polygon for the Tracker, a triangle pointing upwards
		Polygon pg = new Polygon();
		int polySize = (int) (CellWidth / 3);

		pg.addPoint(polySize, 0); // front point (right)
		pg.addPoint(-polySize, -polySize); // rear left
		pg.addPoint(-polySize, polySize); // rear right

		// paint the Tracker
		g.setColor(java.awt.Color.RED);
		paintPlayer(g, e, x, y, pg);

	}
}
