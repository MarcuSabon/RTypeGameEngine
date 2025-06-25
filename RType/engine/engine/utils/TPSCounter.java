package engine.utils;

public class TPSCounter {
	private static int ticks = 0;
	private static int tps = 0;
	private static long lastTime = System.currentTimeMillis();

	// Pour la moyenne sur 10 secondes
	private static final int[] tpsHistory = new int[10];
	private static int historyIndex = 0;
	private static int historyCount = 0;

	// Pour la moyenne d'elapsed
	private static long elapsedSum = 0;
	private static int elapsedCount = 0;
	private static double avgElapsed = 0;

	public static void tick(int elapsed) {
		ticks++;
		elapsedSum += elapsed;
		elapsedCount++;

		long now = System.currentTimeMillis();
		if (now - lastTime >= 1000) {
			tps = ticks;
			avgElapsed = (elapsedCount > 0) ? (double) elapsedSum / elapsedCount : 0;

			// Ajoute au tableau circulaire pour la moyenne sur 10s
			tpsHistory[historyIndex] = tps;
			historyIndex = (historyIndex + 1) % tpsHistory.length;
			if (historyCount < tpsHistory.length)
				historyCount++;

			// Reset pour la prochaine seconde
			ticks = 0;
			elapsedSum = 0;
			elapsedCount = 0;
			lastTime = now;
		}
	}

	public static double getTPS() {
		return tps;
	}

	public static double getAvgElapsed() {
		return avgElapsed;
	}

	public static double getTPS10sAvg() {
		if (historyCount == 0)
			return 0;
		int sum = 0;
		for (int i = 0; i < historyCount; i++)
			sum += tpsHistory[i];
		return (double) sum / historyCount;
	}
}