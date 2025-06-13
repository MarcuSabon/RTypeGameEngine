package engine.brain;

public final class True extends ConditionGAL {

	// CONSTRUCTORS
	public True() {
		// No initialization needed
	}

	// REQUIRED
	@Override
	public boolean eval(Bot b) {
		return true; // Always returns true
	}

}
