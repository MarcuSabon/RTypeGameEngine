package engine.utils;

public class FPSCounter {
	private int frames;
	private int fps;
	private long lastTime;

	public FPSCounter() {
		frames = 0;
		fps = 0;
		lastTime = System.currentTimeMillis();
	}

	public void frame() {
		frames++;
		long now = System.currentTimeMillis();
		if (now - lastTime >= 1000) {
			fps = frames;
			frames = 0;
			lastTime = now;
		}
	}

	public int getFPS() {
		return fps;
	}
}