package engine.brain;

public abstract class Direction {

	public static final Direction N; // north
	public static final Direction E; // east
	public static final Direction S; // south
	public static final Direction W; // west

	public static final Direction F; // forward
	public static final Direction B; // backward
	public static final Direction L; // left
	public static final Direction R; // right

	public static final Direction HERE; // here

	private static class Relative extends Direction {

		final private int angle;

		public Relative(int a) {
			angle = a;
		}

		@Override
		public int degrees() {
			return angle;
		}

		@Override
		public boolean equals(Direction d) {
			return (d instanceof Relative) && (d.degrees() == this.angle);
		}

		@Override
		public Direction rotate(int angle) {
			return new Relative((this.angle + angle) % 360);
		}

		@Override
		public Direction cardinalOf() {
			return null;
		}
	}

	private static class Absolute extends Direction {

		final private int angle;

		public Absolute(int a) {
			angle = a;
		}

		@Override
		public int degrees() {
			return angle;
		}

		@Override
		public boolean equals(Direction d) {
			return (d instanceof Absolute) && (d.degrees() == this.angle);
		}

		@Override
		public Direction rotate(int angle) {
			return new Absolute((this.angle + angle) % 360);
		}

		@Override
		public Direction cardinalOf() {
			if (225 <= angle && angle <= 315)
				return Direction.N;
			if ((315 < angle && angle < 360) || (0 <= angle && angle < 45))
				return Direction.E;
			if (45 <= angle && angle < 135)
				return Direction.S;
			if (135 <= angle && angle < 225)
				return Direction.W;
			throw new IllegalArgumentException("Invalid angle for cardinal direction: " + angle);
		}
	}

	private static class Here extends Direction {

		@Override
		public int degrees() {
			throw new UnsupportedOperationException("Here direction does not have degrees.");
		}

		@Override
		public boolean equals(Direction d) {
			return (d instanceof Here);
		}

		@Override
		public Direction rotate(int angle) {
			return this;
		}

		@Override
		public Direction cardinalOf() {
			return null;
		}

	}

	public boolean isRelative() {
		return this instanceof Relative;
	}

	public static Direction newAbsolute(int angle) {
		return new Absolute(angle % 360);
	}

	public static Direction newRelative(int angle) {
		return new Relative(angle % 360);
	}

	public abstract int degrees();

	public abstract boolean equals(Direction d);

	public abstract Direction rotate(int angle);

	public abstract Direction cardinalOf();

	static {
		N = new Absolute(270);
		E = new Absolute(0);
		S = new Absolute(90);
		W = new Absolute(180);
		F = new Relative(0);
		B = new Relative(180);
		L = new Relative(270);
		R = new Relative(90);
		HERE = new Here();
	}
}
