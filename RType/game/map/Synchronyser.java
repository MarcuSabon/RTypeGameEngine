package map;

public class Synchronyser {
	private static double progress;
	private static int SCROLL_INTERVAL;
	private static int totElapsed;

	private static boolean stop = false;

	public Synchronyser(int SCROLL_INTERVAL) {
		this.SCROLL_INTERVAL = SCROLL_INTERVAL;
		progress = 0.0;
		totElapsed = 0;
	}

	public static void tick(int elapsed) {
		progress = (double) totElapsed / (double) SCROLL_INTERVAL;
		totElapsed += elapsed;
		if (progress > 1) {
			totElapsed = 0;
			progress = 0;
		}
	}

	public static double progress() {
		if (stop) {
			return 0;
		}
		return progress;
	}

	public static void resetProg() {
		totElapsed = 0;
	}

	public static double synchronise(double x, double cellWidth) {
		return x + (0.5 - progress()) * (double) cellWidth;
	}

	public static void stop() {
		stop = true;
	}

	public static void start() {
		stop = false;
	}
}
