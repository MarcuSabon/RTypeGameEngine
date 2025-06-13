package engine.brain;

public final class False extends ConditionGAL {

	@Override
	public boolean eval(Bot b) {
		return false;
	}

}
