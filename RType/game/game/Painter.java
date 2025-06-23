package game;

import oop.graphics.Canvas;
import oop.graphics.Graphics;
import oop.tasks.Task;

public class Painter implements Runnable {
	private Game m_game;
	private Canvas m_canvas;
	private int m_ncols, m_nrows;

	Painter(Canvas canvas, int nr, int nc) {
		m_nrows = nr;
		m_ncols = nc;
		m_canvas = canvas;

		canvas.set(new PaintListener());
	}

	@Override
	public void run() {
		m_canvas.repaint();
		Task task = Task.task();
		task.post(this, 16); // 62,5 FPS
	}

	class PaintListener implements Canvas.PaintListener {

		@Override
		public void paint(Canvas canvas, Graphics g) {
			m_game.paint(canvas, g.getGraphics2D());
		}

		@Override
		public void visible(Canvas canvas) {
			m_game = new Game(canvas, m_nrows, m_ncols);
			run();
		}

		@Override
		public void revoked(Canvas canvas) {
			System.exit(0);
		}

	}

}
