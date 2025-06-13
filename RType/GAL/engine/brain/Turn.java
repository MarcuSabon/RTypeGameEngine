package engine.brain;

public class Turn extends ActionGAL {

	private Direction d;

	public Turn(Direction d) {
		this.d = d;
	}

	public Turn() {
		this(Direction.F);
	}

	@Override
	void exec(Bot b) {
		b.turn(d);
	}

}
