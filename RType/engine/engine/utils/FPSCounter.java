package engine.utils;

public class FPSCounter {
	private static int frames;
	private static int fps;
	private static long lastTime;
	private static final int[] fpsHistory = new int[10];
	private static int historyIndex = 0;
	private static int historyCount = 0;

	public FPSCounter() {
		frames = 0;
		fps = 0;
		lastTime = System.currentTimeMillis();
	}

	public static void frame() {
		frames++;
		long now = System.currentTimeMillis();
		if (now - lastTime >= 1000) {
			fps = frames;
			fpsHistory[historyIndex] = fps;
			historyIndex = (historyIndex + 1) % fpsHistory.length;
			if (historyCount < fpsHistory.length)
				historyCount++;
			frames = 0;
			lastTime = now;
		}
	}

	public static double getFPS() {
		return fps;
	}

	public static double getFPS10sAvg() {
		if (historyCount == 0)
			return 0;
		int sum = 0;
		for (int i = 0; i < historyCount; i++) {
			sum += fpsHistory[i];
		}
		return (double) sum / historyCount;
	}
}