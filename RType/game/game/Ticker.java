package game;

import oop.tasks.Task;

public class Ticker implements Runnable {
	private Game m_game;
	private long m_last;
	private int m_delay = 1; // milli-seconds

	Ticker(Game g) {
		m_game = g;
		Task task = Task.task();
		m_last = System.currentTimeMillis();
		m_game.tick(0);
		task.post(this, m_delay);
	}

	@Override
	public void run() {
		Task task = Task.task();
		long now = System.currentTimeMillis();
		m_game.tick((int) (now - m_last));
		m_last = now;
		task.post(this, m_delay);
	}

}
