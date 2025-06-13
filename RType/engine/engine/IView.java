package engine;

import java.awt.Graphics2D;

import engine.model.Entity;
import oop.graphics.Canvas;

public interface IView {

	void focus(int px, int py);

	void paint(Canvas canvas, Graphics2D g);

	void scrollLeft();

	void scrollRight();

	void scrollUp();

	void scrollDown();

	void zoomIn();

	void zoomOut();

	void resetZoom();

	int px();

	int py();

	double toPixel(double x);

	double toMetricPixel(double x);
	
	double mouseViewportX();

	double mouseViewportY();

	void debug();

	void birth(Entity e);
	
	void death(Entity e);

	void zoom(double i);

	void reset();

}