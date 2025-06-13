package engine.brain;

public class Move extends ActionGAL {

	private Direction d;
	private int i;

	public Move() {
		d = Direction.F;
		i = 1;
	}

	public Move(Direction d, int i) {
		this.d = d;
		this.i = i;
	}

	@Override
	void exec(Bot b) {
		b.move(d, i);
	}

}
