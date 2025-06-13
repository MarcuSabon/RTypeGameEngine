package engine.brain;

public class Conjonction extends ConditionGAL {

	// FIELDS
	ConditionGAL cond1, cond2;

	// CONSTRUCTORS
	public Conjonction(ConditionGAL c1, ConditionGAL c2) {

		this.cond1 = c1;
		this.cond2 = c2;
	}

	// REQUIRED
	public boolean eval(Bot b) {
		return cond1.eval(b) && cond2.eval(b);
	}

}
