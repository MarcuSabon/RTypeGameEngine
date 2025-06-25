package avatars;

import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.geom.AffineTransform;

import engine.model.Entity;
import engine.model.Stunt.Action;
import engine.view.Avatar;
import engine.view.View;
import map.Synchronyser;
import stunts.StuntPNJ;
import stunts.StuntPNJ.PNJRotateAndMove;

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

	private void paintPlayer(Graphics2D g, Entity e, int x, int y, int d, Polygon pg) {
		int CellWidth = v.pxPerMeter();

		double rot = Math.toRadians(d);
		AffineTransform saved = g.getTransform();
		x = (int) Synchronyser.synchronise(x, CellWidth);

		g.translate(x, y);
		g.rotate(rot);
		g.fillPolygon(pg);
		g.setTransform(saved);
	}

	private void drawTrackerCell(Graphics2D g) {
		int CellWidth = v.pxPerMeter();
		Action a = e.stunt.action();
		int d = e.orientation();
		double x, y;
		if (a != null && a.kind() == 2) {
			StuntPNJ.PNJRotateAndMove motion = (StuntPNJ.PNJRotateAndMove) a;
			if (motion.isRotating()) {
				double progress = (((PNJRotateAndMove) e.stunt.action()).getRotationProgress());
				int angle = e.orientation() - motion.targetAngle;
				if (angle > 180) {
					angle -= 360;
				}
				if (angle < -180) {
					angle += 360;
				}
				d = (int) (e.orientation() - angle * progress * 2);
			}
			if (motion.isMoving()) {
				double progress = (((PNJRotateAndMove) e.stunt.action()).getMoveProgress());
				double actionOffsetX = motion.ncols * CellWidth * progress;
				double actionOffsetY = motion.nrows * CellWidth * progress;
				x = (e.col() * CellWidth) + CellWidth / 2.0 + actionOffsetX;
				y = (e.row() * CellWidth) + CellWidth / 2.0 + actionOffsetY;
				if (motion.movedDone) {
					x -= motion.ncols * CellWidth;
					y -= motion.nrows * CellWidth;
				}
			} else {
				x = (e.col() * CellWidth) + CellWidth / 2.0;
				y = (e.row() * CellWidth) + CellWidth / 2.0;
			}

		} else {
			x = (e.col() * CellWidth) + CellWidth / 2.0;
			y = (e.row() * CellWidth) + CellWidth / 2.0;
		}

		// create a polygon for the Tracker, a triangle pointing upwards
		Polygon pg = new Polygon();
		int polySize = (int) (CellWidth / 3);

		pg.addPoint(polySize, 0); // front point (right)
		pg.addPoint(-polySize, -polySize); // rear left
		pg.addPoint(-polySize, polySize); // rear right

		// paint the Tracker
		g.setColor(java.awt.Color.RED);
		paintPlayer(g, e, (int) x, (int) y, d, pg);

	}
}
