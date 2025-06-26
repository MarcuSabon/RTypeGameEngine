package avatars;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.AffineTransform;
import java.io.IOException;

import javax.imageio.ImageIO;

import engine.model.Entity;
import engine.model.Stunt.Action;
import engine.view.Avatar;
import engine.view.View;
import map.Synchronyser;
import stunts.StuntPNJ;
import stunts.StuntPNJ.PNJRotateAndMove;

public class AvatarTracker extends Avatar {
	private Image image;

	public AvatarTracker(View v, Entity e) {
		super(v, e);
		loadImage();
	}

	@Override
	public void render(Graphics2D g) {
		drawTrackerCell(g);
		// You can add more rendering logic here if needed
		// For example, drawing additional information or effects related to the Tracker
		// g.drawString("Tracker Name: " + e.getName(), x, y - 10); // Example text
	}

	// ------------- Private methods -------------//
	private void loadImage() {
		try {
			image = ImageIO.read(getClass().getResource("/tracker.png"));
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	private void paintPlayer(Graphics2D g, Entity e, int x, int y, int d) {
		int CellWidth = v.pxPerMeter();

		double rot = Math.toRadians(d);
		AffineTransform saved = g.getTransform();
		x = (int) Synchronyser.synchronise(x, CellWidth);

		g.translate(x, y);
		g.rotate(rot);
		g.drawImage(image, -CellWidth / 2, -CellWidth / 2, CellWidth, CellWidth, null);
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
		d += 180;
		paintPlayer(g, e, (int) x, (int) y, d);

	}
}
