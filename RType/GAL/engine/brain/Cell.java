package engine.brain;

public class Cell extends ConditionGAL {

	private Direction dir;
	private int radius;
	private Category cat;

	// plusieurs constructeurs

	public Cell(Direction d, Category c) {

		this(d, c, 1);
	}

	public Cell(Direction d, Category c, int r) {

		this.dir = d;
		this.cat = c;
		this.radius = r;
	}

	// REQUIRED
	public boolean eval(Bot b) {
		return b.cell(dir, cat) != null;
	}

}
