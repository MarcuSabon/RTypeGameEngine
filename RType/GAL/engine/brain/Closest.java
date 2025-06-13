package engine.brain;

public class Closest extends ConditionGAL {

	private Category cat;

	public Closest(Category c) {
		this.cat = c;
	}

	@Override
	public boolean eval(Bot b) {
		return b.closest(cat) != null;
	}

}
