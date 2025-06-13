package engine.brain;

public abstract class ActionGAL {
	/*
	 * [exec] execute the action on the behalf of the acting entity using non-null
	 * parameters of action.
	 */
	abstract void exec(Bot b);

	boolean acceptedBy(Bot b) {
		// TODO : implement the method
		return true;
	}
}
