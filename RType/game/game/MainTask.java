package game;

import java.awt.Dimension;

import oop.graphics.Canvas;
import oop.runtime.EventPump;
import oop.tasks.Task;

public class MainTask {

	public static void main(String args[]) {
		EventPump ep = new EventPump();
		Dimension d = new Dimension(960, 540);
		Runnable r = new Runnable() {
			public void run() {
				Task task = Task.task();
				Canvas canvas = (Canvas) task.find("canvas");
				new Painter(canvas, 20, 42);
			}
		};
		ep.boot(d, r);
	}
}
